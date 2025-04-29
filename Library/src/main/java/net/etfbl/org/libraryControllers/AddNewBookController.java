package net.etfbl.org.libraryControllers;

import java.io.File;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.etfbl.org.libraryLogger.LibraryLogger;
import net.etfbl.org.model.Book;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;

public class AddNewBookController {
	
	private static PropertiesFileLoader INSTANCE = PropertiesFileLoader.getInstance();

	
	  @FXML
	    private Button btnAddBook;
	  
	  @FXML
	    private Button btnInsertPicture;

	  @FXML
	    private DatePicker dpPublicationDate;

	    @FXML
	    private TextField tbAuthor;

	    @FXML
	    private TextField tbContent;

	    @FXML
	    private TextField tbID;

	    @FXML
	    private TextField tbLanguage;

	    @FXML
	    private TextField tbPicture;

	    @FXML
	    private TextField tbTitle;
	    
	    private String path;

	    @FXML
	    void addNewBook(ActionEvent event) {
	    	try {
	    		
	    		if(!tbID.getText().isEmpty() && !tbTitle.getText().isEmpty() && !tbAuthor.getText().isEmpty()
	    				&& !tbLanguage.getText().isEmpty() && !dpPublicationDate.getValue().toString().isEmpty()
	    				&& !tbContent.getText().isEmpty()) {
	    		
	    			URL url = new URL(INSTANCE.getSpecifiedProperty("add_new_book_url"));
	    			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
	    			conn.setDoOutput(true);
	    			conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/json");
				
					int id = Integer.parseInt(tbID.getText());
					String title = tbTitle.getText().trim(),
							author = tbAuthor.getText().trim(),
							publicationDate = String.valueOf(dpPublicationDate.getValue()),
							language = tbLanguage.getText().trim(),
							picture = getBase64(path),
							content = tbContent.getText().trim();
				
					Gson gson = new Gson();
					Book book = new Book(id, title, author, publicationDate, language, picture, content);
				
					try(OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)){
						out.write(gson.toJson(book));
						out.flush();
					
						if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
							new JOptionPane().showMessageDialog(null, "New book added");
						} else if(conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
							new JOptionPane().showMessageDialog(null, "404");
						}else {
							new JOptionPane().showMessageDialog(null, conn.getResponseCode());
						}	
					}		
	    		}else {
	    			new JOptionPane().showMessageDialog(null, "All fields must be filled!");
	    		}
			} catch (Exception e) {
				LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
			}
	    }
	    
	    @FXML
	    void insertPicture(ActionEvent event) {
	    	FileChooser fileChooser = new FileChooser();
	    	
	    	fileChooser.setTitle("");
	    	
	    	File initialDirectory = new File(INSTANCE.getSpecifiedProperty("pictures"));
	    	if(initialDirectory.exists()) {
	    		fileChooser.setInitialDirectory(initialDirectory);
	    	}
	    	
	    	fileChooser.getExtensionFilters().addAll(
	    			new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
	    			);
	    	
	    	File selectedFile = fileChooser.showOpenDialog(new Stage());
	    	
	    	if(selectedFile != null) {
	    		path = selectedFile.getAbsolutePath();
	    		tbPicture.setText(selectedFile.getAbsolutePath());
	    	}
	    }
	    
	    
	    public static String getBase64(String coverUrl) {

	    	try {
	    		File imageFile = new File(coverUrl);
	    		byte[] content = Files.readAllBytes(imageFile.toPath());
	    		return Base64.getEncoder().encodeToString(content);
	    	}catch (Exception e) {
	    		LibraryLogger.LOGGER.log(Level.SEVERE, "", e);
	    		return null;
			}
		}
}
