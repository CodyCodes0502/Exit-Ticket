package jgc.exit_ticket.client;

import java.io.IOException;
import java.net.Socket;
import java.io.*;

public class Client {

	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 6666;
		
		try (Socket socket = new Socket(host, port)){
			
			PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader clientReader = new BufferedReader(new InputStreamReader(System.in));
			
			String userAnswer;
			
			System.out.println(serverReader.readLine());
			
			while ((userAnswer = clientReader.readLine()) != null) {
				serverWriter.println(userAnswer);
			}
			
			serverWriter.close();
			serverReader.close();
			clientReader.close();
			
		} catch (IOException e) {
			System.out.println("Could not connect to server. " + e.getMessage());
		}

	}
		
}

