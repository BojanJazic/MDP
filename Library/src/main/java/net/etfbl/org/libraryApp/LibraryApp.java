package net.etfbl.org.libraryApp;

import java.util.logging.Level;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.etfbl.org.libraryLogger.LibraryLogger;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;

public class LibraryApp extends Application{
	
	private static PropertiesFileLoader INSTANCE = PropertiesFileLoader.getInstance();

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(LibraryApp.class.getResource(INSTANCE.getSpecifiedProperty("login_fxml")));
			Scene scene = new Scene(loader.load());
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(INSTANCE.getSpecifiedProperty("login_icon"))));
			primaryStage.setResizable(false);
			primaryStage.show();
			
		}catch (Exception e) {
			LibraryLogger.LOGGER.log(Level.SEVERE, "An error occured while starting app", e);
		}
		
	}
	
	
	public static void main(String[] args) {
		launch();
	}

}
