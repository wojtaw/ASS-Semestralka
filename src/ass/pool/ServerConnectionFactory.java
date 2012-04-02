package ass.pool;

import ass.server.ServerConnection;

public class ServerConnectionFactory implements PoolFactory<ServerConnection> {

	
	@Override
	public ServerConnection create() {
		return new ServerConnection();
	}

}
