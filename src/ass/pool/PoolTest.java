package ass.pool;

import org.junit.Ignore;
import org.junit.Test;

import ass.server.ServerConnection;


import static org.junit.Assert.*;




public class PoolTest {
	
	public PoolTest(){
		ServerConnection test = new ServerConnection(8080);
	}
	
	@Test
	public void testFondCreation(){
		//ConnectionFond fond = new ConnectionFond<new ConnectionFactory()>();
		//assertTrue(fond != null);
	}
	
	@Ignore
	@Test
	public void requestConnectionTest(){
		//ConnectionFond fond = new ConnectionFond();
	}
	
	
	

}
