package net.etfbl.org.libraryControllers;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.etfbl.org.libraryLogger.LibraryLogger;
import net.etfbl.org.model.User;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;

public class AddNewUserController {
	
	private static PropertiesFileLoader INSTANCE = PropertiesFileLoader.getInstance();
	private static final String SALT = INSTANCE.getSpecifiedProperty("salt");

	
	@FXML
    private Button btnAddNewUser;

    @FXML
    private Label tb;

    @FXML
    private TextField tbAddress;

    @FXML
    private TextField tbEmail;

    @FXML
    private TextField tbName;

    @FXML
    private TextField tbPassword;

    @FXML
    private TextField tbSurname;

    @FXML
    private TextField tbUsername;

	    @FXML
	    void addNewUser(ActionEvent event) {
	    	if(!tbName.getText().isEmpty() && !tbName.getText().isEmpty() && !tbAddress.getText().isEmpty()
	        		&& !tbEmail.getText().isEmpty() && !tbUsername.getText().isEmpty() && !tbPassword.getText().isEmpty()) {
	    		try {
	    			URL url = new URL(INSTANCE.getSpecifiedProperty("registration_url"));
	    			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        				
	    			conn.setDoOutput(true);
	    			conn.setDoInput(true);
	    			conn.setRequestMethod("POST");
	    			conn.setRequestProperty("Content-Type", "application/json");
	        				 
	    			User user = new User(tbName.getText(), 
	    					tbName.getText(),
	    					tbAddress.getText(),
	    					tbEmail.getText(),
	    					tbUsername.getText(),
	    					getHash(tbPassword.getText()));
	        				
	        				
	    			Gson gson = new Gson();
	        				
	    			try(OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)){
	    				out.write(gson.toJson(user));
	    				out.flush();
	        					
	    				if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
	    					new JOptionPane().showMessageDialog(null, "Successfully!");
	    				} else if(conn.getResponseCode() == HttpURLConnection.HTTP_CONFLICT) {
	    					new JOptionPane().showMessageDialog(null, "User already exists!");
	    				} else {
	    					new JOptionPane().showMessageDialog(null, conn.getResponseCode());
	    				}
	        					
	    			}	
	    		}catch(Exception e) {
	    			LibraryLogger.LOGGER.log(Level.SEVERE, "An error occurred while registering a new users.", e);
	    		}		
	    	}else {
	    		new JOptionPane().showMessageDialog(null, "All fields must be filled!");

	    	}
	    }
	
	
	
	 private String getHash(String password) {
	    	try {
	    		String inputWithSalt = SALT + password;
	    		
	    		MessageDigest md = MessageDigest.getInstance("SHA-512");
	    		byte[] hashedBytes = md.digest(inputWithSalt.getBytes());
	    		
	    		return Base64.getEncoder().encodeToString(hashedBytes);
	    		
	    	}catch(NoSuchAlgorithmException e) {
	    		LibraryLogger.LOGGER.log(Level.SEVERE, "An error in method getHash(String password)", e);
	    		return null;
	    	}
	    }
	 
}
