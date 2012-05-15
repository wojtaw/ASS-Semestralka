package ass.cimlvojt.server.auth;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.Gson;

import ass.cimlvojt.utils.ApplicationOutput;

public class BasicAuthentification {
	private StringBuilder readedHtaccess = new StringBuilder();
	private String directoryPath = "";
	private String requestedUsername;
	private String requestedPassword;
	
	public BasicAuthentification(){
		
	}
	
	public boolean isProtected(File fileToScan){
		if(!readHtaccessFile(fileToScan.getParent())) return false;
		else return true;
	}
	
	public boolean isProtected(String directoryPath){
		if(!readHtaccessFile(directoryPath)) return false;
		else return true;
	}
	
	public boolean authorizeCode(String accessCode){
		if(accessCode==null) return false;
		parseAccessCode(accessCode);
		return parseAndCompareHtpasswd();
	}
	
	private boolean parseAndCompareHtpasswd() {
		ApplicationOutput.printWarn("Parsing and comparing");
		File htapasswd = new File(directoryPath + "/.htpasswd");
		if(!htapasswd.exists()) return false;
		Gson gson = new Gson();
		 
		try {
	 
			BufferedReader br = new BufferedReader(
				new FileReader(htapasswd));
	 
			//convert the json string back to object
			LoginRecordJSON loginObject = gson.fromJson(br, LoginRecordJSON.class);

			for (int i = 0; i < loginObject.username.length; i++) {
				if(loginObject.username[i].equals(requestedUsername)){
					if(loginObject.password[i].equals(requestedPassword)) return true;
				}
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		
	}
	
	private String hashPassword(String passwordToHash){
		return DigestUtils.md5Hex(passwordToHash);
	}

	private boolean readHtaccessFile(String dirPath){
		this.directoryPath = dirPath;
		File htaccess = new File(directoryPath + "/.htaccess");
		ApplicationOutput.printLog("testing "+htaccess.getAbsolutePath());
		if(!htaccess.exists()) return false;

		try {
			BufferedReader reader = new BufferedReader (
			        new FileReader(htaccess));
			String tmpString = reader.readLine(); 
			while(tmpString != null){
				readedHtaccess.append(tmpString+"\r\n");
				tmpString = reader.readLine();
			}
		} catch (IOException e) {
			ApplicationOutput.printErr("There was problem reading htaccess file");
			e.printStackTrace();
		}
        
		return true;
	}
	
	private void parseAccessCode(String accessCode){
		//Cut standart HTTP "basic " string
		accessCode = accessCode.substring(6);
		accessCode = decodeBase64(accessCode);
		String[] tmpArray = accessCode.split(":");
		if(tmpArray.length>1){			
			requestedUsername = tmpArray[0];
			requestedPassword = hashPassword(tmpArray[1]);
		}
	}
	
	private String decodeBase64(String base64){
        byte[] decoded = Base64.decodeBase64(base64); 
        String returnedString = new String(decoded);
        System.out.println(returnedString);
		return returnedString;
	}
	
	public String getUsername(){
		return requestedUsername;
	}
	
	public String getPassword(){
		return requestedPassword;
	}	

}
