package ass.server.multirequest;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import ass.generalPool.PoolFactoryInterface;
import ass.server.ServerConnectionProcessing;
import ass.utils.ApplicationOutput;

public class ThreadPool extends Thread{
	private int fondCapacity = 15;
	private int readyConnections;
	private Queue<ServerConnectionProcessing> requestsQueue;
	private boolean poolIsOn = false;
	
	public ThreadPool(int capacity){
		this.fondCapacity = capacity;
		try {
			init();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public ThreadPool(){
		try {
			init();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	
	
	public void run(){
		while(poolIsOn){
			
		}
	}
	
	private synchronized void init() throws InterruptedException{
		//We will make 10 initial Threads ready in queue, this can be changed as desired
		if(fondCapacity > 5) readyConnections = fondCapacity / 2;
		else readyConnections = fondCapacity;
		ApplicationOutput.printLog("Fond will create: "+readyConnections);
		
		requestsQueue = new LinkedList<ServerConnectionProcessing>();
		
		for (int i = 0; i < readyConnections; i++) {
			ServerConnectionProcessing tmpConnection = new ServerConnectionProcessing(this);
			tmpConnection.start();
			requestsQueue.add(tmpConnection);
		}
	}
	
	public void requestProcessing(String recievedRequest){
		//First figure out if there is more requests in single string, split it
		String singleRequest = "";
		String tmpStr = "";
		for (int i = 0; i < recievedRequest.length(); i++) {
			tmpStr+=recievedRequest.charAt(i);
			if(tmpStr.charAt(tmpStr.length()-1)=='\n' && tmpStr.charAt(tmpStr.length()-2)=='\r') {
				//Check wether it contains HTTP sign, if not add it to the request
				if(tmpStr.contains("HTTP/1") && tmpStr.length()>1){
					processSingleRequest(singleRequest);
					singleRequest = tmpStr;
				} else {
					singleRequest += tmpStr;
				}
				tmpStr = "";
			}
		}
		

	}
	
	private void processSingleRequest(String recievedRequest){
		ApplicationOutput.printWarn("Remaining threads in pool: "+requestsQueue.size());
		ServerConnectionProcessing serverConnectionWorker;
		if(requestsQueue.size() == 0) serverConnectionWorker = createNewInstance();
		else {
			ApplicationOutput.printLog("Connection taken from pool, was available");
			serverConnectionWorker = requestsQueue.poll();
		}
		
		serverConnectionWorker.setInFromClient(recievedRequest);
		synchronized (serverConnectionWorker) {			
			if(!serverConnectionWorker.isAlive()){
				ApplicationOutput.printLog("Starting");
				serverConnectionWorker.start();
			} else {
				ApplicationOutput.printLog("Notyfing");
				serverConnectionWorker.notify();					
			}
		}			
	}
		
	private ServerConnectionProcessing createNewInstance() {		
		return new ServerConnectionProcessing(this);
	}	
	
	public synchronized void closeConnection(ServerConnectionProcessing returnedCon){		
		ApplicationOutput.printLog("Returning USED THREAD");
		//Put it into pool
		requestsQueue.add(returnedCon);
	}	
		
	public int getTestFreeThreads(){
		return requestsQueue.size();
	}

	public int getCapacity(){
		return fondCapacity;
	}	
}
