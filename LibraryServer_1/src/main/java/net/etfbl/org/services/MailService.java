package net.etfbl.org.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.etfbl.org.logger.LibraryServerLogger;
import net.etfbl.org.model.Book;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;
import net.etfbl.org.zipper.BookZipper;

public class MailService {
	
	private static PropertiesFileLoader INSTANCE = PropertiesFileLoader.getInstance();

	public boolean sendMail(String reciever, ArrayList<Book> books) {
		
		String username = INSTANCE.getSpeciefiedProperty("username");
		String password = INSTANCE.getSpeciefiedProperty("password");
		
		BookZipper zipper = new BookZipper();
		File zipFile = zipper.zipBooks(books);
		
		 Properties props = new Properties();
		 props.put("mail.smtp.auth", PropertiesFileLoader.getInstance().getSpeciefiedProperty("mail.smtp.auth"));
		 props.put("mail.smtp.starttls.enable", PropertiesFileLoader.getInstance().getSpeciefiedProperty("mail.smtp.starttls.enable"));
		 props.put("mail.smtp.host", PropertiesFileLoader.getInstance().getSpeciefiedProperty("mail.smtp.host"));
		 props.put("mail.smtp.port", PropertiesFileLoader.getInstance().getSpeciefiedProperty("mail.smtp.port"));
		 
		 Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			 protected PasswordAuthentication getPasswordAuthentication(){
				 return new PasswordAuthentication(username, password);
			 }
		 });
		 
		 
		 try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username, INSTANCE.getSpeciefiedProperty("mail_from")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reciever));
			message.setSubject(INSTANCE.getSpeciefiedProperty("mail_subject"));
			
			MimeBodyPart part = new MimeBodyPart();
			String line = "";
			
			for(Book b : books) {
				line += b.toString();
			}
			
			part.setText(line);
			
			MimeBodyPart attachment = new MimeBodyPart();
			attachment.attachFile(zipFile);
			
			Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(part);
	        multipart.addBodyPart(attachment);

	        message.setContent(multipart);

	        Transport.send(message);

		} catch (Exception e) {
			LibraryServerLogger.LOGGER.log(Level.SEVERE, "", e);
		}
		return true;
	}
	
}
