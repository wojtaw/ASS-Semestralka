package ass.server;

import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.*;
import java.net.*;
import ass.pool.*;

public class WebServer {
	private String recievedRequest;
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
	private Thread serviceThread;
	
	private ConnectionFond<Integer> serverFond;
	
	
	private int portNumber;
	protected boolean serverOn = false;
    protected ServerSocket serverSocket = null;	
	
	public WebServer(int portNumber) {
		printLog("Server instance created");
		this.portNumber = portNumber;
		startServer();
	
	}
	
	private void createThreadPool(){
		
	}
	
	public void startServer() {
		printLog("Server is starting");
		Runnable runnable = new ServerListener();
		serviceThread = new Thread(runnable);
		serviceThread.start();		
	}
	
	public void terminateServer() throws IOException{
		printLog("Server is terminating");
		serviceThread.interrupt();	
		serverOn = false;
		if(serverSocket != null) serverSocket.close();
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
		for (int i = 1; i < parameters.length; i++) {
			workingStr = parameters[i];
			delimiter = workingStr.indexOf(':');
			parameterType = workingStr.substring(0, delimiter);
			parameterContent = workingStr.substring(delimiter+2, workingStr.length());
			assignValues(parameterType, parameterContent);
		}

		printOutRecievedValues();
		return true;
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
		printLog("Method: "+reqMethod);
		printLog("Host: "+reqHost);
		printLog("Path: "+reqPath);
		printLog("User-Agent: "+reqUserAgent);
		printLog("Accept: "+reqAccept);
		printLog("Accept-Language: "+reqAcceptLanguage);
		printLog("Accept-Encoding: "+reqAcceptEncoding);
		printLog("Accept-Charset: "+reqAcceptCharset);
		printLog("Connection: "+reqConnection);
		printLog("Cookie: "+reqCookie);
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
	
	private void sendAnswer() throws Exception{
		File transfer = new File(serverDirectory+""+reqPath);
		InputStream in = new FileInputStream(transfer);
		ServerSocket server = new ServerSocket(440);
		Socket connection = server.accept();
		
		OutputStream output = connection.getOutputStream();
		
		byte[] buff = new byte[connection.getSendBufferSize()];
		int bytesRead = 0;
		
		printLog(transfer.length()+ " bytes");
		
		while((bytesRead = in.read(buff))>0)
		{
			output.write(buff,0,bytesRead);
		}
		in.close();
		server.close();
		connection.close();
		output.close();
	}
	
	public String getMehod() {
		return reqMethod;
	}

	public String getPath() {
		return reqPath;
	}

	public String getHost() {
		return reqHost;
	}	
	
	class ServerListener implements Runnable {
        String clientSentence = "";
        
	    public void run() {
	    	try {
				serverSocket = new ServerSocket(portNumber);
				printLog("Socket created listening on port "+portNumber);
				serverOn = true;
	         while(serverOn)
	         {
	        	printLog("Accepting message");
	            Socket connectionSocket = serverSocket.accept();
	            BufferedReader inFromClient =
	               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	            
	            String tmpStr = inFromClient.readLine();
	            while(tmpStr != null){
	            	clientSentence += (tmpStr + "\r\n");
	            	tmpStr = inFromClient.readLine();
	            }
	            
	            printLog("Something recieved");
	            recievedMessage(clientSentence);
	         }	    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    	
	    }
	}	
	
	public void printLog(String msg){
		System.out.println("LOG: "+msg);
	}

}
