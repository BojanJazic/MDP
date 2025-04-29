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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.etfbl.org.libraryApp.LibraryApp;
import net.etfbl.org.libraryLogger.LibraryLogger;
import net.etfbl.org.model.Book;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;


public class BookPageController implements Initializable{
	
	private static PropertiesFileLoader INSTANCE = PropertiesFileLoader.getInstance(); 

	 @FXML
	 private Button btnAddNewBook;

	 @FXML
	 private Button btnDeleteBook;

	 @FXML
	 private Button btnShowAllBooks;

	 @FXML
	 private Button btnUpdateBook;

	 @FXML
	 private TableColumn<Book, String> columnAuthor;

	 @FXML
	 private TableColumn<Book, String> columnDetails;

	 @FXML
	 private TableColumn<Book, Integer> columnId;

	 @FXML
	 private TableColumn<Book, String> columnLanguage;

	 @FXML
	 private TableColumn<Book, String> columnReleaseDate;

	 @FXML
	 private TableColumn<Book, String> columnTitle;

	 @FXML
	 private TableView<Book> tvBookTable;
	 
	 private ArrayList<Book> books;
	 private ObservableList<Book> obsBooks;
	 
	 
	 @Override
		public void initialize(URL location, ResourceBundle resources) {
			Platform.runLater(() -> {
					
			// TODO Auto-generated method stub
			columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
			columnTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
			columnAuthor.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
			columnReleaseDate.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
			columnLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
			
			
			columnDetails.setCellFactory(col -> new TableCell<>() {
				private final Button details = new Button("Details");
				
				
				{
					details.setOnAction(e -> {
						Book book = getTableView().getItems().get(getIndex());
						showBookDetails(book);
						
					});
				}
				
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if(empty || getTableView().getItems().get(getIndex()) == null) {
						setGraphic(null);	//hide button if book doesn't exist in the row
					}else {
						setGraphic(details);
					}
				}
				
			});
			
			});
		}
	 
	
	 
	 @FXML
	 void addNewBook(ActionEvent event) {
		 try {
			FXMLLoader loader = new FXMLLoader(LibraryApp.class.getResource(INSTANCE.getSpecifiedProperty("add_new_book_fxml")));
			Scene scene = new Scene(loader.load());
			
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Add new book");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			tvBookTable.refresh();
			
		} catch (Exception e) {
			LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
		}
	 }

	 @FXML
	 void deleteBook(ActionEvent event) {
		 Book book = (Book)tvBookTable.getSelectionModel().getSelectedItem();
		 
		 if(book != null) {
			 try {
				URL url = new URL(INSTANCE.getSpecifiedProperty("delete_book_url"));
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				
				conn.setDoOutput(true);
				conn.setRequestMethod("DELETE");
				conn.setRequestProperty("Content-Type", "application/json");
				
				Gson gson = new Gson();
				
				try(OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)){
					out.write(gson.toJson(book));
					out.flush();
					
					if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						new JOptionPane().showMessageDialog(null, "Successfully deleted!");
						tvBookTable.refresh();
					}else if(conn.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
						new JOptionPane().showMessageDialog(null, conn.getResponseCode());
					}else {
						new JOptionPane().showMessageDialog(null, conn.getResponseCode());
					}
				}
				
			} catch (Exception e) {
				LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
			}
			 tvBookTable.refresh();
		 }else {
			 new JOptionPane().showMessageDialog(null, "Row must be selected.");
		 }
	 }

	 
	 @FXML
	 void showAllBooks(ActionEvent event) {
		 try {
			URL url = new URL(INSTANCE.getSpecifiedProperty("books_url"));
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			
			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))){
					StringBuilder builder = new StringBuilder();
					
					String responseLine;
					while((responseLine = br.readLine()) != null) {
						builder.append(responseLine.trim());
					}
					
					//pretvaranje json objekta u listu knjiga
					ObjectMapper mapper = new ObjectMapper();
					books = mapper.readValue(builder.toString(), new TypeReference<ArrayList<Book>>() {});
					obsBooks = FXCollections.observableArrayList(books);
					if(books.isEmpty()) {
						new JOptionPane().showMessageDialog(null, "There are no books.");
					}else {
						tvBookTable.setItems(obsBooks);
					}
				}
			}else {
				new JOptionPane().showMessageDialog(null, conn.getResponseCode());
			}
				
		} catch (Exception e) {
			LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
		}
		 
		 
	 }
	 

	 @FXML
	 void updateBook(ActionEvent event) {
		 Book book = (Book)tvBookTable.getSelectionModel().getSelectedItem();
		 
		 if(book != null) {
		 
			 try {
				 FXMLLoader loader = new FXMLLoader(LibraryApp.class.getResource(INSTANCE.getSpecifiedProperty("update_book_fxml")));
				Scene scene = new Scene(loader.load());
			
				Stage stage = new Stage();
				stage.setUserData(book);
				stage.setScene(scene);
				stage.setTitle("Update book");
				stage.setResizable(false);
				stage.initModality(Modality.APPLICATION_MODAL);
				
				stage.showAndWait();
				tvBookTable.refresh();
			
			 } catch (Exception e) {
				 LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
			 }
		 }
	 }
	 
	 
	 private void showBookDetails(Book book) {
			try {
				FXMLLoader loader = new FXMLLoader(LibraryApp.class.getResource(INSTANCE.getSpecifiedProperty("book_details_fxml")));
				Scene scene = new Scene(loader.load());
				
				Stage primaryStage = new Stage();
				primaryStage.setTitle("Book details");
				primaryStage.getIcons().add(new Image(getClass().getResourceAsStream((INSTANCE.getSpecifiedProperty("books_icon")))));
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.initModality(Modality.APPLICATION_MODAL);
				primaryStage.initOwner((Stage)tvBookTable.getScene().getWindow());
				
				primaryStage.setUserData(book);
				
				primaryStage.showAndWait();
				tvBookTable.refresh();
			}catch (Exception e) {
				LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
			}
			
		}
	 
	 
}
