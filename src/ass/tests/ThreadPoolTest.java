package ass.tests;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;



import ass.pool.PoolGeneral;
import ass.server.ServerConnectionProcessing;
import ass.server.multirequest.ThreadPool;

public class ThreadPoolTest {
	
	
	@Test	
	public void testThreadPoolCreation(){
		ThreadPool testPool = new ThreadPool(50);
		assertEquals(50, testPool.getCapacity());
	}	
}
