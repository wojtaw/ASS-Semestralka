package ass.start;

import ass.server.multirequest.ThreadPoolTest;

public class AppDriver {
	
	public AppDriver(){
		ThreadPoolTest threadPoolTest = new ThreadPoolTest();
		threadPoolTest.testPoolCreation();
	}

}
