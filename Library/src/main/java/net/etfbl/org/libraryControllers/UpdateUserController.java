package net.etfbl.org.libraryControllers;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.etfbl.org.libraryLogger.LibraryLogger;
import net.etfbl.org.model.User;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;


public class UpdateUserController implements Initializable{
	
	  @FXML
	  private Button btnUpdateData;

	  @FXML
	  private TextField tbAddress;

	  @FXML
	  private TextField tbEmail;

	  @FXML
	  private TextField tbName;

	  @FXML
	  private TextField tbSurname;

	  @FXML
	  private TextField tbUsername;
	  
	  @FXML
	  private TextField tbActiveStatus;
	  
	  @FXML
	  private TextField tbAccountBlocked;
	  
	  private User user;
	  

	  
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			Platform.runLater(() -> {
				Stage stage = (Stage)tbName.getScene().getWindow();
				user = (User)stage.getUserData();
				tbName.setText(user.getFirstName());
				tbSurname.setText(user.getLastName());
				tbAddress.setText(user.getAddress());
				tbEmail.setText(user.getEmail());
				tbUsername.setText(user.getUsername());
				if(user.getAccountActivated())
					tbActiveStatus.setText("true");
				else
					tbActiveStatus.setText("false");
				if(user.getAccountBlocked())
					tbAccountBlocked.setText("true");
				else
					tbAccountBlocked.setText("false");
			});
		}catch(Exception e) {
			LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
		}
		
	}
	  

	  @FXML
	  void updateData(ActionEvent event) {
		  
		  try {
			  user.setAddress(tbAddress.getText().trim());
			  user.setEmail(tbEmail.getText().trim());
			  if("true".equals(tbActiveStatus.getText().trim()))
				  user.setAccountActivated(true);
			  else
				  user.setAccountActivated(false);
			  
			  if("true".equals(tbAccountBlocked.getText().trim()))
				  user.setAccountBlocked(true);
			  else
				  user.setAccountBlocked(false);
			  
			  URL url = new URL(PropertiesFileLoader.getInstance().getSpecifiedProperty("user_update"));
			  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					
			  conn.setDoOutput(true);
			  conn.setRequestMethod("PUT");
			  conn.setRequestProperty("Content-Type", "application/json");
					
			  Gson gson = new Gson();
					
			  try(OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)){
				  out.write(gson.toJson(user));
				  out.flush();
				  if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					  new JOptionPane().showMessageDialog(null, "Successfully updated!");
				  } else if(conn.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
					  new JOptionPane().showMessageDialog(null, conn.getResponseCode());
				  } else {
					  new JOptionPane().showMessageDialog(null, conn.getResponseCode());
				  }
			  }	
		  } catch (Exception e) {
			  LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
		  }

	  }
	  
	  public void setUser(User user) {
		  this.user = user;
	  }
	
	
}
