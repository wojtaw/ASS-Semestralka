package ass.pool;

import java.util.LinkedList;
import java.util.Queue;

public class ConnectionFond <E extends Comparable<E>>{
	private int fondCapacity = 50;
	private Queue<E> requestsQueue;
	private final FondFactory<E> factory;
	
	public ConnectionFond(FondFactory<E> factory) {
		this.factory = factory;
		init();
	}
	
	public ConnectionFond(int capacity, FondFactory<E> factory){
		this.fondCapacity = capacity;
		this.factory = factory;
		init();
	}
	
	private void init(){
		//Create first instances in storage
		int tmpNum;
		if(fondCapacity > 10) tmpNum = 10;
		else tmpNum = fondCapacity;
		
		for (int i = 0; i < tmpNum; i++) {
			requestsQueue.add(createNewInstance());
		}
		
		requestsQueue = new LinkedList<E>();
	}
	
	public synchronized E requestConnection(){
		if(requestsQueue.size() == 0) return createNewInstance();
		else return requestsQueue.poll();
	}
	
	public synchronized void closeConnection(E returnedCon){
		//Reset values
		//Put it into pool
		requestsQueue.add(returnedCon);
	}
	
	private E createNewInstance() {
		E newInstance = factory.create();
		return newInstance;
	}

	
	
}
