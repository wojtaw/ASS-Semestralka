package ass.server.multirequest;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;



import ass.pool.PoolGeneral;
import ass.server.ServerConnection;

public class ThreadPoolTest {
	
	@Test
	@Ignore
	public void testPoolCreation(){
		PoolGeneral<ServerConnection> testPool = new PoolGeneral<ServerConnection>(100, new ServerConnectionFactory());
		assertEquals(100, testPool.getCapacity());
	}
}
