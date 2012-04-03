package ass.server.multirequest;

import java.io.BufferedReader;
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
	
	private void init() throws InterruptedException{
		//We will make 10 initial Threads ready in queue, this can be changed as desired
		if(fondCapacity > 5) readyConnections = fondCapacity / 2;
		else readyConnections = fondCapacity;
		ApplicationOutput.printLog("Fond will create: "+readyConnections);
		
		requestsQueue = new LinkedList<ServerConnectionProcessing>();
		
		for (int i = 0; i < readyConnections; i++) {
			ServerConnectionProcessing tmpConnection = new ServerConnectionProcessing(this);
			requestsQueue.add(tmpConnection);
		}
	}
	
	public synchronized void requestProcessing(String recievedRequest){
		ServerConnectionProcessing serverConnectionWorker;
		if(requestsQueue.size() == 0) serverConnectionWorker = createNewInstance();
		else serverConnectionWorker = requestsQueue.poll();
		serverConnectionWorker.setInFromClient(recievedRequest);
		if(!serverConnectionWorker.isAlive()) serverConnectionWorker.start();
	}
		
	private ServerConnectionProcessing createNewInstance() {
		return new ServerConnectionProcessing(this);
	}	
	
	public synchronized void closeConnection(ServerConnectionProcessing returnedCon){
		//Put it into pool
		//requestsQueue.add(returnedCon);
	}	
		

	public int getCapacity(){
		return fondCapacity;
	}	
}
