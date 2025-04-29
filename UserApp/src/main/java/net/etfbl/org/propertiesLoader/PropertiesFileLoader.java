package net.etfbl.org.propertiesLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import net.etfbl.org.userLogger.UserLogger;

public class PropertiesFileLoader {

	private static PropertiesFileLoader loaderInstance;
	private Properties properties = new Properties();
	
	private PropertiesFileLoader() {
		loadPropertiesFile();
	}
	
	public static PropertiesFileLoader getInstance() {
		if(loaderInstance == null) {
			loaderInstance = new PropertiesFileLoader();
		}
		return loaderInstance;
	}
	
	private void loadPropertiesFile() {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")){
			if(is == null) {
				System.out.println("Config file doesn't exist!");
				return;
			}
			
			properties.load(is);
			
		} catch (IOException e) {
			// TODO: handle exception
			UserLogger.LOGGER.log(Level.SEVERE, "Failed to load properties file!", e);
		}
	}
	
	public String getSpecifiedProperty(String keyWord) {
		return properties.getProperty(keyWord);
	}
	
	
}
