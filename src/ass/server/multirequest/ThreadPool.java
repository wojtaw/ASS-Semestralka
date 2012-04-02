package ass.server.multirequest;

import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.Queue;

import ass.pool.PoolFactoryInterface;
import ass.server.ServerConnectionProcessing;
import ass.utils.ApplicationOutput;

public class ThreadPool {
	private int fondCapacity = 15;
	private int readyConnections;
	private Queue<ServerConnectionProcessing> requestsQueue;
	
	public ThreadPool(int capacity){
		this.fondCapacity = capacity;
		init();
	}
	
	public ThreadPool(){
		init();
	}	
	
	private void init(){
		//We will make 10 initial Threads ready in queue, this can be changed as desired
		if(fondCapacity > 10) readyConnections = fondCapacity / 2;
		else readyConnections = fondCapacity;
		ApplicationOutput.printLog("Fond will create: "+readyConnections);
		
		requestsQueue = new LinkedList<ServerConnectionProcessing>();
		
		for (int i = 0; i < readyConnections; i++) {
			ServerConnectionProcessing tmpConnection = new ServerConnectionProcessing();
			requestsQueue.add(tmpConnection);
		}
	}
	
	public synchronized void requestProcessing(BufferedReader recievedRequest){
		ServerConnectionProcessing serverConnectionWorker;
		if(requestsQueue.size() == 0) serverConnectionWorker = createNewInstance();
		else serverConnectionWorker = requestsQueue.poll();
		serverConnectionWorker.setInFromClient(recievedRequest);
		serverConnectionWorker.start();
	}
		
	private ServerConnectionProcessing createNewInstance() {
		return new ServerConnectionProcessing();
	}	
		

	public int getCapacity(){
		return fondCapacity;
	}	
}
