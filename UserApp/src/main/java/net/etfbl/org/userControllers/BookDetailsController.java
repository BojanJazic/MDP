package net.etfbl.org.userControllers;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.etfbl.org.model.Book;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;
import net.etfbl.org.userLogger.UserLogger;

public class BookDetailsController implements Initializable{
	
	
	 @FXML
	 private ImageView ivBookImage;

	 @FXML
	 private Label lbBookTitle;

	 @FXML
	 private TextArea taBookContent;
	 
	 
	 @Override
	public void initialize(URL location, ResourceBundle resources) {
		 
		 Platform.runLater(() -> {
			 Stage stage = (Stage) lbBookTitle.getScene().getWindow();
			 Book book = (Book) stage.getUserData();
			 lbBookTitle.setText(book.getBookTitle());
			 taBookContent.setText(getFirstHundredLines(book.getBookContent()));
			 Image image = convertBase64ToImage(book.getCoverPage());
			 ivBookImage.setImage(image);
						 
		 });
		
	}
	 
	 
	 private String getFirstHundredLines(String content) {
		    String[] lines = content.split("\\R"); 
		    StringBuilder result = new StringBuilder();

		    int maxLines = Math.min(100, lines.length); // Prilagodi broj linija ako ima manje od 100
		    for (int i = 0; i < maxLines; i++) {
		        result.append(lines[i]).append(System.lineSeparator());
		    }

		    return result.toString();
		}

	 private Image convertBase64ToImage(String base64) {
	        
		 try{
			 byte[] decodedBytes = Base64.getDecoder().decode(base64);
			 ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
		     return new Image(inputStream);
		 }catch (IllegalArgumentException e) {
			 UserLogger.LOGGER.log(Level.SEVERE, "An error occured in the method convertBase64ToImage.", e);
			 return getDefaultImage();
		}
	        
	 }
	 
	 private Image getDefaultImage() {
		 return new Image(getClass().getResourceAsStream(PropertiesFileLoader.getInstance().getSpecifiedProperty("default_image")));
	 }
	 	 
}