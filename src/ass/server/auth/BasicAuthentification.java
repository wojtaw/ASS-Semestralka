package ass.server.auth;

import java.io.File;

public class BasicAuthentification {
	
	public BasicAuthentification(){
		
	}
	
	public boolean isProtected(File fileToScan){
		if(readHtaccessFile(fileToScan.getAbsolutePath())) return false;
		else return true;
	}
	
	public boolean isProtected(String fileToScanPath){
		if(readHtaccessFile(fileToScanPath)) return false;
		else return true;
	}	
	
	private boolean readHtaccessFile(String dirPath){
		File htaccess = new File(dirPath);
		if(!htaccess.exists()) return false;
		return true;
	}

}
