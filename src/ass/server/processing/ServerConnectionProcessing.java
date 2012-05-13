package ass.server.processing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Queue;

import ass.generalPool.PoolGeneral;
import ass.server.auth.BasicAuthentification;
import ass.server.cache.CacheManager;
import ass.server.errors.HTTPStatusCodes;
import ass.server.pool.ThreadPool;
import ass.utils.ApplicationOutput;

public class ServerConnectionProcessing extends Thread{
	private String clientRequestString = null;
	private Socket clientSocketToAnswer = null;
	private String reqAuthorization;
	private String reqMethod;
	private String reqPath;
	private String reqHost;	
	private String reqUserAgent;
	private String reqAccept;
	private String reqConnection;
	private String reqCookie;
	private String reqAcceptLanguage;
	private String reqAcceptEncoding;
	private String reqAcceptCharset;	
	private String serverDirectory = "testFiles";
	private BasicAuthentification authentification;
	private ThreadPool homePool = null;
	private CacheManager cacheReference;
	
	
	public ServerConnectionProcessing(ThreadPool homePool, CacheManager cacheReference){
		this.homePool = homePool;
		this.cacheReference = cacheReference;
		this.authentification = new BasicAuthentification();
	}
	
	public void run(){
		while(true){			
			ApplicationOutput.printLog("Connection processor up and running");
			if(clientRequestString != null && !clientRequestString.equals("")) {
				recievedMessage(clientRequestString);  
				ApplicationOutput.printLog("Thread should be returned to the pool or terminated");
			}
			returnThisConnection();
			synchronized (this) {			
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	private void returnThisConnection() {
		if(homePool == null) return;
		//We have to null all values
		clientRequestString = "";
		
		//Then we can return this thread to the pool
		homePool.closeConnection(this);
	}

	public String getInFromClient() {
		return clientRequestString;
	}

	public void setInFromClient(String clientRequestString, Socket clientSocketToAnswer) {
		this.clientRequestString = clientRequestString;
		this.clientSocketToAnswer = clientSocketToAnswer;
	}

	private boolean parseRequestValues(String recReq) {
		String[] parameters = recReq.split("\r\n");
		String workingStr;
		String parameterType;
		String parameterContent;
		int delimiter;
		
		//First field should contain method parse it
		workingStr = parameters[0];
		delimiter = workingStr.indexOf(" ");
		reqMethod = workingStr.substring(0, delimiter);
		
		//After methos there should be path
		reqPath = workingStr.substring(delimiter+1,workingStr.indexOf("HTTP/")-1);
		
		//For rest of the parameters, parse type of parameter, if it is wanted
		//Than assign value
		for (int i = 1; i < parameters.length; i++) {		
			workingStr = parameters[i];
			delimiter = workingStr.indexOf(':');
			if(delimiter == -1 || i == (parameters.length-1)){
				continue;
			} else {				
				parameterType = workingStr.substring(0, delimiter);
				parameterContent = workingStr.substring(delimiter+2, workingStr.length());
				assignValues(parameterType, parameterContent);
			}
		}

		ApplicationOutput.printLog("DOSTAL JSEM SE K HODNOTAM");		
		//printOutRecievedValues();
		return true;
	}	
	
	private void recievedMessage(String message) {
		if(parseRequestValues(message)) {
			try {
				sendAnswer();
			} catch (SocketException e) {
				ApplicationOutput.printErr("Client disconnected before recieving response");
			} catch (IOException e) {
				ApplicationOutput.printErr("File loading failed");
				e.printStackTrace();				
			} catch (Exception e) {
				ApplicationOutput.printErr("sendAnswer was unsuccessfull");
				e.printStackTrace();
			}
		}
	}	
	
	private void assignValues(String parameterType, String parameterContent) {
		if(parameterType.equals("Host")) reqHost = parameterContent;
		else if(parameterType.equals("User-Agent")) reqUserAgent = parameterContent;
		else if(parameterType.equals("Accept")) reqAccept = parameterContent;
		else if(parameterType.equals("Accept-Language")) reqAcceptLanguage = parameterContent;
		else if(parameterType.equals("Accept-Encoding")) reqAcceptEncoding = parameterContent;
		else if(parameterType.equals("Accept-Charset")) reqAcceptCharset = parameterContent;
		else if(parameterType.equals("Connection")) reqConnection = parameterContent;
		else if(parameterType.equals("Cookie")) reqCookie = parameterContent;
		else if(parameterType.equals("Authorization")) reqAuthorization = parameterContent;
		
	}	
	
	public void printOutRecievedValues(){
		ApplicationOutput.printLog("Method: "+reqMethod);
		ApplicationOutput.printLog("Host: "+reqHost);
		ApplicationOutput.printLog("Path: "+reqPath);
		ApplicationOutput.printLog("User-Agent: "+reqUserAgent);
		ApplicationOutput.printLog("Accept: "+reqAccept);
		ApplicationOutput.printLog("Accept-Language: "+reqAcceptLanguage);
		ApplicationOutput.printLog("Accept-Encoding: "+reqAcceptEncoding);
		ApplicationOutput.printLog("Accept-Charset: "+reqAcceptCharset);
		ApplicationOutput.printLog("Connection: "+reqConnection);
		ApplicationOutput.printLog("Cookie: "+reqCookie);
	}
	
	private File checkFile(){
		File fileToTransfer = new File(serverDirectory+""+reqPath);
		//Check if it is folder and exists, attempt to find directory index
		if(fileToTransfer.isDirectory()){
			File tmpIndex = new File(fileToTransfer.getAbsolutePath()+"/index.html");
			if(!tmpIndex.exists()) tmpIndex = new File(fileToTransfer.getAbsolutePath()+"/index.html");
			else if(!tmpIndex.exists()) tmpIndex = new File(fileToTransfer.getAbsolutePath()+"/index.htm");
			else if(!tmpIndex.exists()) tmpIndex = new File(fileToTransfer.getAbsolutePath()+"/default.html");
			else if(!tmpIndex.exists()) tmpIndex = new File(fileToTransfer.getAbsolutePath()+"/default.htm");
			else if(!tmpIndex.exists()) return null;
			else return tmpIndex;
		//If not folder, attempt to return file or not found
		}else{
			if(fileToTransfer.exists()) return fileToTransfer;
			else return null;
		}
		return fileToTransfer;
	}

	
	private boolean isFileProtected(File fileToTest){
		BasicAuthentification authentification = new BasicAuthentification();
		//Check if file is protected
		//If so, check if request do not contain authorization
		if(authentification.isProtected(fileToTest)){
			//Test wether or not was authorization successfull
			//User was authorized, file is not protected
			if(authentification.authorizeCode(reqAuthorization)) return false;
			//User was rejected, file is protected
			else return true;
		}else{
			return false;
		}
	}
	
	private boolean sendAnswer() throws Exception{
		ApplicationOutput.printLog("Processing");
		if(clientSocketToAnswer == null) return false;
		ApplicationOutput.printLog("Answering to client"+clientSocketToAnswer.getLocalAddress().toString());	
		DataOutputStream outputToClient = new DataOutputStream(clientSocketToAnswer.getOutputStream());

		File fileToTransfer = checkFile();
		
		//File do not exists, return NOT FOUND
		if(fileToTransfer == null){
			//Return 404 Not found
			String httpHeader = HTTPStatusCodes.NOT_FOUND.toString();
			outputToClient.writeBytes(httpHeader);
			//Or you can return custom error 404 page here
			/*
			String displayImage = "<big><bold>FILE NOT FOUND, OR BROKEN</bold></big>\r\n";
			outputToClient.writeBytes(displayImage);
			*/
			outputToClient.flush();
			outputToClient.close();
			return false;
		}
		
		if(isFileProtected(fileToTransfer)){
			ApplicationOutput.printLog("Authentification required");			
			String httpHeader = HTTPStatusCodes.ACCESS_DENIED.toString();
			outputToClient.writeBytes(httpHeader);
			outputToClient.flush();
            outputToClient.close();
			return false;
		}
		
		//Chceck if file is cached take it or try to put in in cache
		if(cacheReference.isObjectCached(fileToTransfer)){
			ApplicationOutput.printWarn("CACHE HIT");
			outputToClient.write(cacheReference.getCachedObject(fileToTransfer).getBytes());
		} else {
			ApplicationOutput.printWarn("CACHE MISS");			
			cacheReference.cacheNewObject(fileToTransfer);
			outputToClient.write(createByteArrayFromFile(fileToTransfer));
		}
		

		outputToClient.flush();			
		outputToClient.close();
		return true;
	}
	
	//Create byte array
	private byte[] createByteArrayFromFile(File fileToRead) throws IOException{
		byte answerContent[];
		InputStream in = new FileInputStream(fileToRead);
		answerContent = new byte[(int)fileToRead.length()];
		in.read(answerContent);
		
		ApplicationOutput.printLog("SENDING OUT BYTES: "+answerContent.length+ " bytes");		
		return answerContent;
	}

	public String getReqMethod() {
		return reqMethod;
	}

	public String getReqPath() {
		return reqPath;
	}

	public String getReqHost() {
		return reqHost;
	}

	public String getReqUserAgent() {
		return reqUserAgent;
	}

	public String getReqAccept() {
		return reqAccept;
	}

	public String getReqConnection() {
		return reqConnection;
	}

	public String getReqCookie() {
		return reqCookie;
	}

	public String getReqAcceptLanguage() {
		return reqAcceptLanguage;
	}

	public String getReqAcceptEncoding() {
		return reqAcceptEncoding;
	}

	public String getReqAcceptCharset() {
		return reqAcceptCharset;
	}	
}
