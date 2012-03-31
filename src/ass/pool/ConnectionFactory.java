package ass.pool;

public class ConnectionFactory<E> implements FondFactory<E> {

	public ConnectionFactory(){
		ServerConnection test = new ServerConnection();
	}
	
	@Override
	public ServerConnection create() {
		//WTF? I do not get it :( Abstract instances suxx
		ServerConnection testCon = new ServerConnection();
		return testCon;
	}

}
