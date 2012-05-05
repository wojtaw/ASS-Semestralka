package ass.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
	public int portNumber = 80; //Default port for our webserver
	public boolean serverOn = false;
    protected ServerSocket serverSocket = null;	
	private Thread serviceThread;   
	private ThreadPool threadPool;
	private Socket connectionSocket;
	private volatile Queue<HTTPRequestHolder> requestQueue = new LinkedList<HTTPRequestHolder>();	
    
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
	
	protected void recoverFromError(){
		//terminateServer();
		//startServer();
	}
	
	class ServerListener implements Runnable {
        StringBuilder clientSentence;
        
	    public void run() {
	    	try {
				serverSocket = new ServerSocket(portNumber);
				ApplicationOutput.printLog("Socket created listening on port "+portNumber);
				serverOn = true;
	         while(serverOn)
	         {
	        	clientSentence = new StringBuilder();
	        	ApplicationOutput.printLog("Accepting message");
	            connectionSocket = serverSocket.accept();
	    		DataOutputStream outputToClient = new DataOutputStream(connectionSocket.getOutputStream());
	    		//ApplicationOutput.printLog("client just connected "+connectionSocket.getLocalAddress().toString());
	    		
	            BufferedReader inFromClient =
	               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	            
	            ApplicationOutput.printLog("now reading from client");
	            String tmpStr = inFromClient.readLine();
	            while(tmpStr != null && tmpStr.length() > 0){
	            	clientSentence.append(tmpStr + "\r\n");
	            	tmpStr = inFromClient.readLine();
	            }	            

	            ApplicationOutput.printLog("---------------\n\nSomething recieved, now will be processed");
	            ApplicationOutput.printLog(clientSentence.toString());
	            ApplicationOutput.printLog("---------------\n\n");
	            
	            /*
	             */
	    		ApplicationOutput.printWarn("Answering to client"+connectionSocket.getLocalAddress().toString());
				String httpHeader = "HTTP/1.1 404 Not found\r\n";
				outputToClient.flush();
				String responseToSend = "<big><bold>CONNECTED, OK</bold></big>\r\n";
				try {
					outputToClient.writeBytes(responseToSend);
				} catch (SocketException e) {
					ApplicationOutput.printErr("Client disconnected, can not send response");
				}
				outputToClient.flush();
				outputToClient.close();
				

	            //processIncomingRequest(clientSentence.toString(),connectionSocket);
	            
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
}
