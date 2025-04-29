package net.etfbl.org.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javafx.util.converter.LocalDateTimeStringConverter;

public class Message implements Comparable<Message>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String sender;
	private String recipient;
	private String content;
	private LocalDateTime dateTime;
	
	
	public Message(String sender, String recipient, String content) {
		this.sender = sender;
		this.recipient = recipient;
		this.content = content;
		this.dateTime = LocalDateTime.now();
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getRecipient() {
		return recipient;
	}


	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public LocalDateTime getDateTime() {
		return dateTime;
	}


	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}


	@Override
	public int compareTo(Message o) {
		return this.dateTime.compareTo(o.getDateTime());
	}
	
	
	
}
