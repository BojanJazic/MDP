package net.etfbl.org.supplierApp;

import java.io.IOException;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;


public class SupplierApp extends Application{
	
	
	@Override
	public void start(Stage primaryStage){
		try {
			
			System.out.println(Paths.get("src", "main", "resources", "config.properties"));

			
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader(SupplierApp.class.getResource(PropertiesFileLoader.getInstance().getSpeciefiedProperty("login_fxml"))); 
		Scene scene = new Scene(loader.load());
		primaryStage.setTitle("Supplier login page");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(PropertiesFileLoader.getInstance().getSpeciefiedProperty("login_icon"))));
		primaryStage.setScene(scene);
		primaryStage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch();
	}

}
