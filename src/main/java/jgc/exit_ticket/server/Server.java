package jgc.exit_ticket.server;

import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.time.LocalDateTime;





public class Server {
	
	private static final int PORT = 6666;
	
	private static String questionString;
	
	private static String questionID;
	
    public static void main(String[] args) {
    	
    	
    	
    	//Get question of the day (QOTD)
    	Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Exit Ticket Question of the Day: ");
        Question question = new Question(scanner.nextLine());
        
        scanner.close();
        
        
        //Print QOTD
        System.out.println("Exit Ticket Question of the Day - " + question.questionID);
        
        questionString = question.question;
        questionID = question.questionID.replace(':', '.');
        
        System.out.println(question.question);
        
        //Start server listening on the port
        System.out.println("Starting server...");
        
        try (//Create a ServerSocket listening on port 6666
		ServerSocket serverSocket = new ServerSocket(PORT)) {
        	System.out.println("Server Started. Listening on " + serverSocket.getInetAddress().getHostAddress() + " on port " + PORT + "...");
        
        	ExecutorService pool = Executors.newCachedThreadPool();
        	
        	while(true) {
        		Socket clientSocket = serverSocket.accept();
        		System.out.println("Student Connected: " + clientSocket.getInetAddress());
        		
        		pool.execute(new ClientHandler(clientSocket));
        	}
        	
        	
        
        } catch (Exception serverException) {
        	System.out.println("Server Error: " + serverException);
        }
    }
    
    private static class ClientHandler implements Runnable {
    	
    	private Socket clientSocket;
    	
    	public ClientHandler(Socket socket) {
    		this.clientSocket = socket;
    	}
    	
    	public void saveAnswers(String answer) {
    		try {
    			File studentAnswers = new File("Exit_Ticket_" + questionID + ".txt");
    			
    			//TODO Try to create file, and check if it already exists.
    			if (!studentAnswers.exists()) {
    				System.out.println("File Created: " + studentAnswers.getName());
    				FileWriter fw = new FileWriter(studentAnswers, false);
    				fw.write("Question: " + questionString + ":\n");
    				fw.close();
    				
    			}
    			else {
    				System.out.println("File already exists.");
    			}
    			FileWriter fw = new FileWriter(studentAnswers, true);
    			fw.write("\n" + clientSocket.getInetAddress() + "'s answer: " + answer);
    			fw.close();
    			
    		} catch (IOException e) {
    			System.out.println("An error occured creating the file");
    			e.printStackTrace();
    		}
    	}
    	
    	@Override
    	public void run() {
    		String answer;
    		try(
    				PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
    				BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
    				) {
    			clientWriter.println(questionString);
    			answer = clientReader.readLine();
    			System.out.println("Answer from " + clientSocket.getInetAddress() + ": " + answer);
    			saveAnswers(answer);

    		} catch (IOException e) {
    			System.out.println("Error handling client" + e.getMessage());
    		} finally {
    			try {
    				clientSocket.close();
    			} catch (IOException e) {}
    		}
    	}
    }
}

class Question {
	
	public String questionID;
	public String question;
	public LocalDateTime timeStamp;
	
	public Question(String question) {
		
		this.timeStamp = LocalDateTime.now();
		questionID = timeStamp.toString();
		this.question = question;
	}
}