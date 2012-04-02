package ass.server.multirequest;

import org.junit.Ignore;
import org.junit.Test;


import ass.pool.PoolGeneral;
import ass.server.ServerConnection;

public class ThreadPoolTest {
	
	@Test
	@Ignore
	public void testPoolCreation(){
		PoolGeneral<ServerConnection> testPool = new PoolGeneral<ServerConnection>();
	}
	
	

}
