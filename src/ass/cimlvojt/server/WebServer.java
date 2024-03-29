package ass.cimlvojt.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

import ass.cimlvojt.server.cache.CacheManager;
import ass.cimlvojt.server.pool.ThreadPool;
import ass.cimlvojt.utils.ApplicationOutput;

public class WebServer {
	public int portNumber = 80; //Default port for our webserver
	public boolean serverOn = false;
    protected ServerSocket serverSocket = null;	
	private Thread serviceThread;   
	private ThreadPool threadPool;
	private Socket connectionSocket;
	private long requestAccepted = 0;
	private volatile Queue<HTTPRequestHolder> requestQueue = new LinkedList<HTTPRequestHolder>();	
	private CacheManager serverCache;
    
	public WebServer(int portNumber) {
		ApplicationOutput.printLog("Server instance created");
		this.portNumber = portNumber;
		runServerCache();
		createThreadPool();
		startServer();
	} 
	

	public WebServer() {
		ApplicationOutput.printLog("Server instance created");
		runServerCache();		
		createThreadPool();
		startServer();
	}
	
	private void runServerCache() {
		serverCache = new CacheManager();
	}
	
	private void createThreadPool() {
		threadPool = new ThreadPool(20,requestQueue,this);
		threadPool.poolIsOn = true;
		threadPool.start();
	}

	public void startServer() {
		ApplicationOutput.printLog("Server is starting");
		Runnable runnable = new ServerListener();
		serviceThread = new Thread(runnable);
		serviceThread.start();		
	}	
	
	private void processIncomingRequest(String requestData, Socket connectionSocket){
		requestQueue.add(new HTTPRequestHolder(requestData,connectionSocket));
	}
	
	protected void recoverFromError(){
		//terminateServer();
		//startServer();
	}
	
	class ServerListener implements Runnable {
        StringBuilder clientSentence;
        int benchmarkCountIndex = 0;
        
	    public void run() {
	    	try {
				serverSocket = new ServerSocket(portNumber);
				ApplicationOutput.printLog("Socket created listening on port "+portNumber);
				serverOn = true;
	         while(serverOn)
	         {
	        	clientSentence = new StringBuilder();
	        	benchmarkCountIndex++;
	        	if(benchmarkCountIndex >= 100){
	        		ApplicationOutput.printWarn("Already served connections: "+requestAccepted);
	        		benchmarkCountIndex = 0;
	        	}
	        	ApplicationOutput.printLog("Accepting connections");
	            connectionSocket = serverSocket.accept();
	            requestAccepted++;
	    		DataOutputStream outputToClient = new DataOutputStream(connectionSocket.getOutputStream());
	    		
	            BufferedReader inFromClient =
	               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	            
	            String tmpStr = inFromClient.readLine();
	            while(tmpStr != null && tmpStr.length() > 0){
	            	clientSentence.append(tmpStr + "\r\n");
	            	tmpStr = inFromClient.readLine();
	            }	            

	            processIncomingRequest(clientSentence.toString(),connectionSocket);
	            
	         }	    	
			} catch (SocketException e) {
				if(!serverOn){
					ApplicationOutput.printLog("SERVER WAS SHUT DOWN");
				}else{					
					ApplicationOutput.printWarn("SERVER was still listening or sending and connection was closed");
					e.printStackTrace();
					recoverFromError();
				}
			} catch (IOException e) {
				e.printStackTrace();				
			}	    	
	    }
	}

	
	public void terminateServer() {
		try {
			serverOn = false;
			//if(connectionSocket != null) connectionSocket.close();
			if(serverSocket != null) serverSocket.close();
		} catch (IOException e) {
			ApplicationOutput.printErr("Closing server failed");
		}
		ApplicationOutput.printLog("SERVER CLOSED\n\n");
	}	
	
	public ThreadPool getTestThreadPool(){
		return threadPool;
	}
	
	public CacheManager getServerCache(){
		return serverCache;
	}
}
