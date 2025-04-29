package net.etfbl.org.services;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

import net.etfbl.org.logger.LibraryServerLogger;
import net.etfbl.org.model.User;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;

public class UserService {
	
	private ArrayList<User> users = new ArrayList<User>();	
	
	public boolean registerNewUser(User user) {
		retrieveUsersFromXMLFile();
		boolean userExists = users.stream().anyMatch(u -> u.equals(user));
		
		if(!userExists) {
			users.add(user);
			writeUserToXMLFile();
			LibraryServerLogger.LOGGER.info("User " + user.getUsername() + " registred successfully.");
			return true;
		}
		
		return false;
		
	}
	
	public boolean login(User user) {
		retrieveUsersFromXMLFile();
						
		return users.stream().anyMatch(u -> (user.equals(u) && user.getPassword().equals(u.getPassword()) 
															  && (u.getAccountActivated() == true) && (u.getAccountBlocked() == false)));
		
	}
	
	
	public ArrayList<User> getAllUsers(){
			
		retrieveUsersFromXMLFile();
		return users;
		
		
	}
	
	public User getUserByUsername(String username) {
		retrieveUsersFromXMLFile();
		
		return users.stream().filter(u -> u.getUsername().equals(username))
							 .findFirst()
							 .orElse(new User());
	}
	
	public ArrayList<User> getRegistrationRequests(){
		retrieveUsersFromXMLFile();
		
		return users.stream().filter(u -> u.getAccountActivated() == false).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public boolean acceptRegistrationRequest(User user) {
		retrieveUsersFromXMLFile();
		
		Optional<User> tempUser = users.stream().filter(u -> u.equals(user)).findFirst();
		
		if(tempUser.isPresent()) {
			tempUser.get().setAccountActivated(true);
			writeUserToXMLFile();
			LibraryServerLogger.LOGGER.info("User account(" + tempUser.get().getUsername() + ") accepted successfully!");
			return true;
		}
		
		return false;
	}
	
	public boolean declineRegistrationRequest(User user) {
		retrieveUsersFromXMLFile();
		
		Optional<User> tempUser = users.stream().filter(u -> u.equals(user)).findFirst();
		
		if(tempUser.isPresent()) {
			users.remove(tempUser.get());
			writeUserToXMLFile();
			LibraryServerLogger.LOGGER.info("Account declined for user: " + user.getUsername());
			return true;
		}
		
		return false;
	}
	

	public boolean updateAccount(User user) {
		retrieveUsersFromXMLFile();
		
		Optional<User> userToUpdate = users.stream().filter(u -> u.equals(user))
													.findFirst();

		if(userToUpdate.isPresent()) {
			users.remove(userToUpdate.get());
			users.add(user);
			writeUserToXMLFile();
			return true;
		}
		
		return false;
				
	}
	
	public boolean deleteAccount(User user) {
		retrieveUsersFromXMLFile();
		
		Optional<User> userToDelete = users.stream().filter(u -> u.equals(user))
													.findFirst();
		
		if(userToDelete.isPresent()) {
			users.remove(userToDelete.get());
			LibraryServerLogger.LOGGER.info("The user " + userToDelete.get() + " has been deleted from XML file! Date and time: " + new SimpleDateFormat().toString());
			writeUserToXMLFile();
			
			return true;
		}
		
		return false;
	}
	
	
	public void writeUserToXMLFile() {
		String fileName = PropertiesFileLoader.getInstance().getSpeciefiedProperty("users_xml");

		
		try(XMLEncoder encoder = new XMLEncoder(new FileOutputStream(new File(fileName)))){
			encoder.writeObject(users);
		}catch(Exception e) {
			LibraryServerLogger.LOGGER.log(Level.SEVERE, "An error occured while writing the user object to the file!", e);
		}
		
	}
	
	public void retrieveUsersFromXMLFile() {
		String fileName = PropertiesFileLoader.getInstance().getSpeciefiedProperty("users_xml");
		
		try (XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(new File(fileName))))) {
            users = (ArrayList<User>) xmlDecoder.readObject();
        } catch (Exception e) {
			LibraryServerLogger.LOGGER.log(Level.SEVERE, "An error occured while reading the XML file!", e);
        }
	}
	
}
