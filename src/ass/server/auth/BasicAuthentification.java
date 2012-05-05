package ass.server.auth;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import ass.utils.ApplicationOutput;

public class BasicAuthentification {
	private StringBuilder readedHtaccess = new StringBuilder();
	
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
	
	private boolean readHtaccessFile(String dirPath){
		File htaccess = new File(dirPath + "/.htaccess");
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

}
