package ass.server.multirequest;

import java.util.LinkedList;
import java.util.Queue;

import ass.pool.PoolFactoryInterface;
import ass.server.ServerConnection;
import ass.utils.ApplicationOutput;

public class ThreadPool {
	private int fondCapacity = 15;
	private int readyConnections;
	private Queue<ServerConnection> requestsQueue;
	
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
		
		requestsQueue = new LinkedList<ServerConnection>();
		
		for (int i = 0; i < readyConnections; i++) {
			ServerConnection tmpConnection = new ServerConnection(8080);
			tmpConnection.start();
			requestsQueue.add(tmpConnection);
		}
	}
	
		

	public int getCapacity(){
		return fondCapacity;
	}	
}
