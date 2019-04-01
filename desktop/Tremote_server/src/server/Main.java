package server;
import server.UDPServer;

public class Main {

	public static void main(String[] args) throws Exception{
		
		UDPServer server = new UDPServer(UDPServer.DEFAULT_PORT);
		
		server.establishConnection();
	}
	
}
