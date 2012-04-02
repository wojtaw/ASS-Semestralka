package ass.server.multirequest;

import java.util.LinkedList;
import java.util.Queue;

import ass.pool.PoolFactoryInterface;
import ass.server.ServerConnection;

public class ThreadPool {
	private int fondCapacity = 15;
	private Queue<ServerConnection> requestsQueue;
	
	public ThreadPool(int capacity){
		this.fondCapacity = capacity;
	}
	
	private void init(){
		//We will make 10 initial Threads ready in queue, this can be changed as desired
		int tmpNum;
		if(fondCapacity > 10) tmpNum = 10;
		else tmpNum = fondCapacity;
		
		requestsQueue = new LinkedList<ServerConnection>();
		
		for (int i = 0; i < tmpNum; i++) {
			requestsQueue.add(new ServerConnection());
		}
		
	}
		

	public int getCapacity(){
		return fondCapacity;
	}	
}
