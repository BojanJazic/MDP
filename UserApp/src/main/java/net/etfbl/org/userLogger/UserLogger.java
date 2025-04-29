package net.etfbl.org.userLogger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import net.etfbl.org.propertiesLoader.PropertiesFileLoader;


public class UserLogger {
	
public static Logger LOGGER = Logger.getLogger(UserLogger.class.getName());
	
	static {
		try {
			FileHandler handler = new FileHandler(PropertiesFileLoader.getInstance().getSpecifiedProperty("log_file"), true);
			handler.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(handler);
			LOGGER.setUseParentHandlers(false);
		} catch (IOException e) {
			// TODO: handle exception
			LOGGER.log(Level.SEVERE, "An error occured while initializing file handler!", e); 
		}
		
	}
	
	public static Logger getLOGGER() {
		return LOGGER;
	}

}
