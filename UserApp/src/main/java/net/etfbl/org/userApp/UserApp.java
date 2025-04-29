package net.etfbl.org.userApp;

import java.io.IOException;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;
import net.etfbl.org.userLogger.UserLogger;

public class UserApp extends Application{

	@Override
	public void start(Stage primaryStage){
		try {
			// TODO Auto-generated method stub
			FXMLLoader loader = new FXMLLoader(getClass().getResource(PropertiesFileLoader.getInstance().getSpecifiedProperty("login_fxml")));
			Scene scene = new Scene(loader.load());
		
			primaryStage.setTitle("");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch(IOException e) {
			UserLogger.LOGGER.log(Level.SEVERE, "An error occured in the main app - UserApp", e);
		}
	}
	
	public static void main(String[] args) {
		launch();
	}
}
