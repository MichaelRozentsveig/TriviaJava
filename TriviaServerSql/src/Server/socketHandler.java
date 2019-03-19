package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class socketHandler extends Thread {
	private Socket incoming;

	socketHandler(Socket in) throws IOException { // C'tor
		this.incoming = in;
	}

	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(incoming.getInputStream())); //get command from client
			DataOutputStream outToClient = new DataOutputStream(incoming.getOutputStream()); //send strings to client
			String str;
			
			while (true) {
				str = inFromClient.readLine(); // get from client his choice
				if (str != null) {
					switch (str.charAt(0)) {
					case '1': // connection success
						System.out.println("get connection");
						str = "success";
						outToClient.writeBytes(str + "\n");
						break;

					case '2': // get all questions from database
						System.out.println("get all the quetions");
						str = Sql.getQuestions();
						outToClient.writeBytes(str + "\n");
						break;

					case '3': // get all scores from database
						System.out.println("get all the scores");
						str = Sql.getScores();
						outToClient.writeBytes(str + "\n");
						break;

                      case '4':  // set new score to "high scores" table in database
                    	 System.out.println("set new score");
                    	 String newScore = inFromClient.readLine();
                         Sql.insertScoreToDB(newScore);
                         str = "success";
						outToClient.writeBytes(str + "\n");
                      break;
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Connection reset!");
		}

	}

}
