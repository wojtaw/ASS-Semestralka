package ass.pool;

import ass.server.ServerConnection;

public class PoolFactory implements FondFactory<ServerConnection> {

	
	@Override
	public ServerConnection create() {
		return new ServerConnection();
	}

}
