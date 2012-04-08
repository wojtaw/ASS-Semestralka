package ass.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

import ass.server.multirequest.ThreadPool;
import ass.utils.ApplicationOutput;

public class WebServer {
	public int portNumber = 8080; //Default port for our webserver
	public boolean serverOn = false;
    protected ServerSocket serverSocket = null;	
	private Thread serviceThread;   
	private ThreadPool threadPool;
	private Socket connectionSocket;
	private Queue<HTTPRequestHolder> requestQueue = new LinkedList<HTTPRequestHolder>();	
    
	public WebServer(int portNumber) {
		ApplicationOutput.printLog("Server instance created");
		this.portNumber = portNumber;
		createThreadPool();
		startServer();
	} 
	
	public WebServer() {
		ApplicationOutput.printLog("Server instance created");
		createThreadPool();
		startServer();
	} 	
	
	private void createThreadPool() {
		threadPool = new ThreadPool(11,requestQueue);
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
		ApplicationOutput.printLog("Adding data to queue");
		requestQueue.add(new HTTPRequestHolder(requestData,connectionSocket));
	}
	
	class ServerListener implements Runnable {
        String clientSentence = "";
        
	    public void run() {
	    	try {
				serverSocket = new ServerSocket(portNumber);
				ApplicationOutput.printLog("Socket created listening on port "+portNumber);
				serverOn = true;
	         while(serverOn)
	         {
	        	ApplicationOutput.printLog("Accepting message");
	            connectionSocket = serverSocket.accept();
	            BufferedReader inFromClient =
	               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	            
	            String tmpStr = inFromClient.readLine();
	            while(tmpStr != null){
	            	clientSentence += (tmpStr + "\r\n");
	            	tmpStr = inFromClient.readLine();
	            }	            
	            
	            processIncomingRequest(clientSentence,connectionSocket);
	            
	            ApplicationOutput.printLog("Something recieved");
	         }	    	
			} catch (SocketException e) {
				ApplicationOutput.printWarn("SERVER WAS STILL LISTENING");				
			} catch (IOException e) {
				e.printStackTrace();				
			}	    	
	    }
	}

	public void terminateServer() {
		try {
			serverOn = false;
			if(connectionSocket != null) connectionSocket.close();
			if(serverSocket != null) serverSocket.close();
		} catch (IOException e) {
			ApplicationOutput.printErr("Closing server failed");
		}
		ApplicationOutput.printLog("SERVER CLOSED\n\n\n\n\n\n\n");
	}	
	
	public ThreadPool getTestThreadPool(){
		return threadPool;
	}
}
