package ass.benchmark;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Benchmark {
	
	
	public static void main(String[] args) {
		System.out.println("Runing");
		runBenchmark(10);
	}

	private static void runBenchmark(int numberOfThreads) {
		for (int i = 0; i < numberOfThreads; i++) {
			RequestGenerator requestGenerator = new RequestGenerator();
			requestGenerator.start();
		}
	}
	
	private static class RequestGenerator extends Thread{
		public RequestGenerator(){
			
		}
		
		public void run(){
			while(true){
				sendRequest();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
		private static void sendRequest() {
			try	{			
					String requestToSend = "GET /protected1.jpg HTTP/1.1\r\n" +
							"Host: localhost:80\r\n"; 
							
					StringBuilder answerRecieved = new StringBuilder();
					
					System.out.println("Opening socket");
					Socket serverSocket = new Socket("localhost", 80);
					DataOutputStream outToServer = new DataOutputStream(serverSocket.getOutputStream());
					BufferedReader inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
					
					System.out.println("Server opened");
					
					System.out.println("Prepare for sending");
					outToServer.writeBytes(requestToSend + '\n');
					outToServer.flush();
					//outToServer.close();
					System.out.println("Send! Now waiting for reply");
					
				
					System.out.println("Reader from server opened");
					String tmpStr="";
					if(inFromServer.ready()){				
						tmpStr = inFromServer.readLine();
					} else {
						System.out.println("ServerStream is not ready");
					}
		            while(tmpStr != null){
		            	System.out.println("Readed another line");
		            	answerRecieved.append(tmpStr + "\r\n");
		            	tmpStr = inFromServer.readLine();
		            }				
					System.out.println("FROM SERVER: " + answerRecieved.toString());
					serverSocket.close();			
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}		
		
	}
		

}
