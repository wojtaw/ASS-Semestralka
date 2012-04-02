package ass.server.multirequest;

import ass.pool.PoolFactoryInterface;
import ass.server.ServerConnection;

public class ServerConnectionFactory implements PoolFactoryInterface<ServerConnection> {

	
	@Override
	public ServerConnection create() {
		return new ServerConnection();
	}

}
