package shared;

import java.time.LocalDateTime;

public class Question {
	
	public String questionID;
	public String question;
	public LocalDateTime timeStamp;
	
	public Question(String question) {
		
		this.timeStamp = LocalDateTime.now();
		questionID = timeStamp.toString();
		this.question = question;
	}

}
