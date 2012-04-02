package test.generics;

import java.util.LinkedList;
import java.util.Queue;

public class GenericMain <E> {
	

		private int fondCapacity = 50;
		private Queue<E> requestsQueue;
		
		public GenericMain() {
		}	

		
		public GenericMain(int capacity){
			this.fondCapacity = capacity;
			init();
		}
		
		private void init(){
			requestsQueue = new LinkedList<E>();
		}
		
	

}
