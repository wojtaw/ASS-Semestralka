package ass.cimlvojt.server.pool;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import ass.cimlvojt.generalPool.PoolFactoryInterface;
import ass.cimlvojt.server.HTTPRequestHolder;
import ass.cimlvojt.server.WebServer;
import ass.cimlvojt.server.processing.ServerConnectionProcessing;
import ass.cimlvojt.utils.ApplicationOutput;

public class ThreadPool extends Thread{
	private int fondCapacity = 15;
	private Queue<ServerConnectionProcessing> readyThreadsQueue;
	public boolean poolIsOn = false;
	private Queue<HTTPRequestHolder> requestsQueueReference; 
	private WebServer webServer;
	
	
	public ThreadPool(int capacity, Queue<HTTPRequestHolder> requestsQueueReference, WebServer webServer){
		this.webServer = webServer;
		this.requestsQueueReference = requestsQueueReference;
		this.fondCapacity = capacity;
		try {
			init();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public ThreadPool(Queue<HTTPRequestHolder> requestsQueueReference, WebServer webServer){
		this.webServer = webServer;
		this.requestsQueueReference = requestsQueueReference;
		try {
			init();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	
	
	public void run(){
		while(poolIsOn){
			//ThreadPool is constantly checking how many free threads there are
			//If there is many times more free than capacity is, than it will kill them

			if(requestsQueueReference.size() > 0){
				requestProcessing(requestsQueueReference.poll());
			} else {				
				if(readyThreadsQueue.size() > fondCapacity){
					int difference = readyThreadsQueue.size() - fondCapacity;
					ApplicationOutput.printWarn("Too many threads waiting("+readyThreadsQueue.size()+") TIME TO KILL SOME THREADS");
					for (int i = 0; i < difference; i++) {
						readyThreadsQueue.poll();
					}
				}
			}
		}
	}
	
	private synchronized void init() throws InterruptedException{
		//We will make 10 initial Threads ready in queue, this can be changed as desired
		ApplicationOutput.printLog("Fond will create: "+fondCapacity);
		
		readyThreadsQueue = new LinkedList<ServerConnectionProcessing>();
		
		for (int i = 0; i < fondCapacity; i++) {
			ServerConnectionProcessing tmpConnection = new ServerConnectionProcessing(this, webServer.getServerCache());
			tmpConnection.start();
		}
	}
	
	public void requestProcessing(HTTPRequestHolder recievedRequest){
		String recievedRequestMessage = recievedRequest.getHttpRequestMessage();
		//First figure out if there is more requests in single string, split it
		String singleRequest = "";
		String tmpStr = "";
		for (int i = 0; i < recievedRequestMessage.length(); i++) {
			tmpStr+=recievedRequestMessage.charAt(i);
			if(tmpStr.charAt(tmpStr.length()-1)=='\n' && tmpStr.charAt(tmpStr.length()-2)=='\r') {
				//Check wether it contains HTTP sign, if not add it to the request
				if(tmpStr.contains("HTTP/1") && singleRequest.length()>3){
					processSingleRequest(singleRequest,recievedRequest.getClientConnection());
					singleRequest = tmpStr;
				} else {
					singleRequest += tmpStr;
				}

				tmpStr = "";
			}
		}
		if(singleRequest != null || !singleRequest.equals(""))processSingleRequest(singleRequest,recievedRequest.getClientConnection());
		

	}
	
	private synchronized void processSingleRequest(String recievedRequest, Socket clientSocket){
		if(readyThreadsQueue.size() > 6) ApplicationOutput.printLog("Remaining threads in pool: "+readyThreadsQueue.size());
		//else ApplicationOutput.printWarn("Remaining threads in pool: "+readyThreadsQueue.size());
		
		
		ServerConnectionProcessing serverConnectionWorker;
		if(readyThreadsQueue.size() == 0) serverConnectionWorker = createNewInstance();
		else {
			serverConnectionWorker = readyThreadsQueue.poll();
		}
		
		serverConnectionWorker.setInFromClient(recievedRequest,clientSocket);
		synchronized (serverConnectionWorker) {			
			if(!serverConnectionWorker.isAlive()){
				serverConnectionWorker.start();
			} else {
				serverConnectionWorker.notify();					
			}
		}			
	}
		
	private ServerConnectionProcessing createNewInstance() {		
		return new ServerConnectionProcessing(this, webServer.getServerCache());
	}	
	
	public synchronized void closeConnection(ServerConnectionProcessing returnedCon){		
		ApplicationOutput.printLog("Returning USED THREAD");
		//Put it into pool
		readyThreadsQueue.add(returnedCon);
	}	
		
	//To avoid some complicated reflection, we aded Getter for testing purposes
	public int getTestFreeThreads(){
		return readyThreadsQueue.size();
	}

	public int getCapacity(){
		return fondCapacity;
	}	
}
