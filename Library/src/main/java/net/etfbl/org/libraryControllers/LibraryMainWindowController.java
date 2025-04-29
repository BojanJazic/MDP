package net.etfbl.org.libraryControllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.etfbl.org.libraryApp.LibraryApp;
import net.etfbl.org.libraryLogger.LibraryLogger;
import net.etfbl.org.model.User;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;


public class LibraryMainWindowController implements Initializable{
	
	private static PropertiesFileLoader INSTANCE = PropertiesFileLoader.getInstance();

	
	@FXML
    private Button btnAddNewUser;
	  
	  @FXML
	  private Button btnUpdateUser;
	
	 @FXML
	 private Button btnBlockAccount;

	 @FXML
	 private Button btnBooks;
	 
	 @FXML
	 private Button btnRefreshTable;

	 @FXML
	 private Button btnDeleteAccount;

	 @FXML
	 private Button btnRequests;
	 
	 @FXML
	 private Button btnSuggestions;
	 
	 @FXML
	 private Button btnSuppliers;

	 @FXML
	 private TableColumn<User, String> columnActivated;

	 @FXML
	 private TableColumn<User, String> columnAddress;

	 @FXML
	 private TableColumn<User, String> columnBlocked;

	 @FXML
	 private TableColumn<User, String> columnEmail;

	 @FXML
	 private TableColumn<User, Integer> columnID;

	 @FXML
	 private TableColumn<User, String> columnName;

	 @FXML
	 private TableColumn<User, String> columnSurname;

	 @FXML
	 private TableColumn<User, String> columnUsername;

	 @FXML
	 private Label lbUsername;

	 @FXML
	 private TextField tbSearchField;

	 @FXML
	 private TableView<User> tvMembersReview;

	 ObservableList<User> u;
	 FilteredList<User> filteredUsers;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 columnID.setCellValueFactory(cellData -> 
		    new ReadOnlyObjectWrapper<>(tvMembersReview.getItems().indexOf(cellData.getValue()) + 1)
		);
		 columnName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		 columnSurname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		 columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		 columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		 columnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
		 columnActivated.setCellValueFactory(new PropertyValueFactory<>("accountActivated"));
		 columnBlocked.setCellValueFactory(new PropertyValueFactory<>("accountBlocked"));
		 
		 
		initialize();
		if(u != null) {
			filteredUsers = new FilteredList<>(u, p -> true) ;
			tvMembersReview.setItems(filteredUsers);
		}
		 
		 tbSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
		        filteredUsers.setPredicate(user -> {
		            // Ako je polje za pretragu prazno, prikazuju se svi korisnici
		            if (newValue == null || newValue.isEmpty()) {
		                return true;
		            }

		            // Filtriranje na osnovu korisničkog imena (ili drugih atributa)
		            String lowerCaseFilter = newValue.toLowerCase();
		            return user.getFirstName().toLowerCase().contains(lowerCaseFilter) 
		            	   || user.getLastName().toLowerCase().contains(lowerCaseFilter);
		        });
		    });
		 
	}
	 
	
	@FXML
    void addNewUser(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(LibraryApp.class.getResource(INSTANCE.getSpecifiedProperty("add_new_user_fxml")));
			Scene scene = new Scene(loader.load());
			
			Stage stage = new Stage();
			stage.setTitle("Add new user");
			stage.setResizable(false);
			stage.setScene(scene); 
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			tvMembersReview.refresh();
			
		} catch (Exception e) {
			LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
		}
    }
	
	
	@FXML
    void updateUser(ActionEvent event) {
		User user = (User)tvMembersReview.getSelectionModel().getSelectedItem();
		
		if(user != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(INSTANCE.getSpecifiedProperty("update_fxml")));
				Scene scene = new Scene(loader.load());
				
				UpdateUserController controller = loader.getController();
				controller.setUser(user);
				
				Stage stage = new Stage();
				stage.setUserData(user);
				stage.setScene(scene);
				stage.setTitle("Update user");
				stage.setResizable(false);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				tvMembersReview.refresh();
				
				
			} catch (Exception e) {
				LibraryLogger.LOGGER.log(Level.SEVERE, "", e.getMessage());
			}
		}
		
    }

	
	
	 @FXML
	 void blockAccount(ActionEvent event) {
		 boolean tmp = false;
		 User user = (User)tvMembersReview.getSelectionModel().getSelectedItem();
		 if(user != null) {
			 if(user.getAccountBlocked()) {}
				
			 else {
			user.setAccountBlocked(true);
			
			 try {
				URL url = new URL(INSTANCE.getSpecifiedProperty("user_update"));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
				conn.setDoOutput(true);
				conn.setRequestMethod("PUT");
				conn.setRequestProperty("Content-Type", "application/json");
				
				Gson gson = new Gson();
				
				try(OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)){
					out.write(gson.toJson(user));
					out.flush();
					
					if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						new JOptionPane().showMessageDialog(null, "User blocked!");
						
						tvMembersReview.refresh();
					} else {
						new JOptionPane().showMessageDialog(null, conn.getResponseCode());
					}
				}
				conn.disconnect();
			} catch (Exception e) {
				LibraryLogger.LOGGER.log(Level.SEVERE, "", e.getMessage());
			}
			 tvMembersReview.refresh();
			 }
		 } else
			 new JOptionPane().showMessageDialog(null, "The row must be selected!");
	 }

	 
	 @FXML
	 void deleteAccount(ActionEvent event) {
		 User user = (User)tvMembersReview.getSelectionModel().getSelectedItem();
		 if(user != null) {
			 try {
				 URL url = new URL(INSTANCE.getSpecifiedProperty("user_delete"));
				 HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				 
				 conn.setDoOutput(true);
				 conn.setRequestMethod("DELETE");
				 conn.setRequestProperty("Content-Type", "application/json");
				 
				 Gson gson = new Gson();
				 u.remove(user);
				 try(OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)){
					 out.write(gson.toJson(user));
					 out.flush();
					 
					 if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						 new JOptionPane().showMessageDialog(null, "User deleted!");
						 
						 tvMembersReview.refresh();
					 } else if(conn.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
						 new JOptionPane().showMessageDialog(null, conn.getResponseCode());
					 } else {
						 new JOptionPane().showMessageDialog(null, conn.getResponseCode());
					 }
				 }
				 
			 }catch(Exception e) {
				 LibraryLogger.LOGGER.log(Level.SEVERE, "", e.getMessage());
			 }
			 tvMembersReview.refresh();
		 }
	 }

	 
	 @FXML
	 void getRequests(ActionEvent event) {
		 try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(INSTANCE.getSpecifiedProperty("requests_fxml")));
			Scene scene = new Scene(loader.load());
			
	        Stage stage = new Stage();
	        stage.setTitle("");
	        stage.setResizable(false);
	        stage.setScene(scene);
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.showAndWait();
			tvMembersReview.refresh();
		} catch (Exception e) {
			LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
		}
		
	 }

	 
	 @FXML
	 void showBooks(ActionEvent event) {
		 
		 try {
			FXMLLoader loader = new FXMLLoader(LibraryApp.class.getResource(INSTANCE.getSpecifiedProperty("books_page_fxml")));
			Scene scene = new Scene(loader.load());
			
			Stage stage = new Stage();
			stage.setTitle("");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			
			stage.showAndWait();
		} catch (Exception e) {
			LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
		}
		 
		 

	 }
	
	 
	 public void setUsername(String username){
		 lbUsername.setText(username);
	 }
	 
	 
	 private void initialize() {
		 try {
				URL url = new URL(INSTANCE.getSpecifiedProperty("users_url"));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type", "application/json");
				
				if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))){
						StringBuilder builder = new StringBuilder();
						
						String responseLine;
						
						while((responseLine = br.readLine()) != null) {
							builder.append(responseLine.trim());
						}
						
						ObjectMapper mapper = new ObjectMapper();
						ArrayList<User> users = mapper.readValue(builder.toString(), new TypeReference<ArrayList<User>>() {});
						
						u = FXCollections.observableArrayList(users);
						tvMembersReview.setItems(u);
					}
				}
				
			} catch (Exception e) {
				LibraryLogger.LOGGER.log(Level.SEVERE, "", e.getMessage());
			}
	 }
	 
	 
	 @FXML
	 void refreshTable(ActionEvent event) {
		 try {
			 URL url = new URL(INSTANCE.getSpecifiedProperty("users_url"));
			 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 
			 conn.setDoOutput(true);
			 conn.setRequestMethod("GET");
			 conn.setRequestProperty("Content-Type", "application/json");
			 
			 if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				 try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
					 StringBuilder builder = new StringBuilder();
					 
					 String responseLine;
					 
					 while((responseLine = br.readLine()) != null) {
						 builder.append(responseLine.trim());
					 }
					 
					 ObjectMapper mapper = new ObjectMapper();
					 ArrayList<User> u = mapper.readValue(builder.toString(), new TypeReference<ArrayList<User>>() {});
					 Platform.runLater(() -> {
						 tvMembersReview.setItems(FXCollections.observableArrayList(u));
					 });
				 }
			 }
			 tvMembersReview.refresh();
		 }catch(Exception e) {
			 LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
		 }
	 }
	 
	 
	 @FXML
	 void showSuggestions(ActionEvent event) {
		 try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(INSTANCE.getSpecifiedProperty("suggestions_fxml")));
			
			Scene scene = new Scene(loader.load());
			
			Stage stage = new Stage();
			
			SuggestionsController controller = loader.getController();
			controller.setUsername(lbUsername.getText());
			
			stage.setTitle("");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			
			stage.show();
			
			
		} catch (Exception e) {
			LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
		}
	 }
	 
	 
	 @FXML
	 void loadSuppliers(ActionEvent event) {
		 try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(INSTANCE.getSpecifiedProperty("suppliers_fxml")));
			Scene scene = new Scene(loader.load());
			
			Stage stage = new Stage();
			
			stage.setResizable(false);
			stage.setTitle("");
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			
			stage.show();
			
			
		} catch (Exception e) {
			LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
		}
	 }
	 
	
}
