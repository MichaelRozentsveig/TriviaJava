package Server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	@SuppressWarnings("resource")
	public static void main(String args[]) throws Exception{ 
	        try {
	            Sql.conectingToSQL(); // connect to mySql server
	            ServerSocket socket = null;
	            socket = new ServerSocket(10001);
	            while (true) {
	                Socket incoming = null;
	                incoming = socket.accept();
	                new socketHandler(incoming).start();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
