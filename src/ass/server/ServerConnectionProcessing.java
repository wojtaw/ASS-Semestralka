package ass.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Queue;

import ass.generalPool.PoolGeneral;
import ass.server.multirequest.ThreadPool;
import ass.utils.ApplicationOutput;

public class ServerConnectionProcessing extends Thread{
	private String inFromClient = null;
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
	private String serverDirectory = "C:\\vojtaciml\\eclipse_work\\ASS_Week4_server1\\src\\wwwFiles";
	private ThreadPool homePool = null;
	
	
	public ServerConnectionProcessing(ThreadPool homePool){
		this.homePool = homePool;
		ApplicationOutput.printLog("Hello everybody, here's new server connection");
	}
	
	public void run(){
		while(true){			
			ApplicationOutput.printWarn("Up and running on");
			//ApplicationOutput.printWarn("Up and running on"+inFromClient);
			if(inFromClient != null && !inFromClient.equals("")) {
				recievedMessage(inFromClient);  
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
		inFromClient = "";
		
		//Then we can return this thread to the pool
		homePool.closeConnection(this);
	}

	public String getInFromClient() {
		return inFromClient;
	}

	public void setInFromClient(String inFromClient) {
		ApplicationOutput.printLog("INFROM CLIENT SET");
		this.inFromClient = inFromClient;
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
		reqPath = workingStr.substring(delimiter+1,workingStr.indexOf("HTTP/"));
		
		//For rest of the parameters, parse type of parameter, if it is wanted
		//Than assign value
		ApplicationOutput.printWarn(parameters.length+"");
		for (int i = 1; i < parameters.length; i++) {		
			workingStr = parameters[i];
			delimiter = workingStr.indexOf(':');
			if(delimiter == -1 || i == (parameters.length-1)){
				continue;
			} else {				
				parameterType = workingStr.substring(0, delimiter);
				parameterContent = workingStr.substring(delimiter+2, workingStr.length());
				assignValues(parameterType, parameterContent);
				//ApplicationOutput.printWarn("Asigning "+parameterType+" to "+parameterContent);
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
	
	private void sendAnswer() throws Exception{

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
