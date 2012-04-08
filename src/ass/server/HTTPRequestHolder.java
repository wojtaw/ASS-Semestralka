package ass.server;

import java.net.Socket;

public class HTTPRequestHolder {
	private String httpRequestMessage;
	private Socket clientConnection;
	
	public HTTPRequestHolder(String httpRequestMessage,Socket clientConnection){
		this.httpRequestMessage = httpRequestMessage;
		this.clientConnection = clientConnection;
	}

	public String getHttpRequestMessage() {
		return httpRequestMessage;
	}

	public Socket getClientConnection() {
		return clientConnection;
	}
}
