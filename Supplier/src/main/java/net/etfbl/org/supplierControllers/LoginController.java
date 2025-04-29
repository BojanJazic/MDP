package net.etfbl.org.supplierControllers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;
import net.etfbl.org.supplierApp.SupplierApp;
import net.etfbl.org.supplierLogger.SupplierLogger;
import net.etfbl.org.xmlReader.XMLReader;
 
public class LoginController {
	
	private static HashMap<String, String> suppliers = XMLReader.getSuppliersFromXML();
	
	@FXML
    private Button btnLogin;

    @FXML
    private PasswordField tbPassword;

    @FXML
    private TextField tbUsername;

    @FXML
    void loginSupplier(ActionEvent event) {
    	boolean temp = false;
    	for(Map.Entry<String, String> entry : suppliers.entrySet()) {
    		if(tbUsername.getText().trim().equals(entry.getKey()) && tbPassword.getText().trim().equals(entry.getValue())) {
    			//JOptionPane.showMessageDialog(null, "Vazi drug!");
    			temp = true;
    		}
    		if(temp == true)
    			break;
    	}
    	if(temp) {
    		try {
    	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

				FXMLLoader loader = new FXMLLoader(SupplierApp.class.getResource(PropertiesFileLoader.getInstance().getSpeciefiedProperty("supplier_main_fxml")));
				Scene scene = new Scene(loader.load());
				
				
				SupplierMainWindowController controller = loader.getController();
				controller.setSupplier(tbUsername.getText().trim());
				 
				
				stage.setScene(scene);
				stage.setTitle("");
				
				stage.setResizable(false);
				stage.show();
			} catch (Exception e) {
				// TODO: handle exception
				SupplierLogger.LOGGER.log(Level.SEVERE, "", e);
			}
    		
    	} else{
    		JOptionPane.showMessageDialog(null, "Ne vazi drug");
    	}
    }

}
