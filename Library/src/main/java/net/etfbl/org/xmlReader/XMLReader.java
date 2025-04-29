package net.etfbl.org.xmlReader;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.etfbl.org.libraryLogger.LibraryLogger;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;

public class XMLReader {
	
	public static HashMap<String, String> getLibrariansFromXML(){
		HashMap<String, String> librarians = new HashMap<String, String>();
		
		try {
			File file = new File(PropertiesFileLoader.getInstance().getSpecifiedProperty("librarians_xml"));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("librarian");
			
			for(int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element)node;
					String username = element.getElementsByTagName("username").item(0).getTextContent();
					String password = element.getElementsByTagName("password").item(0).getTextContent();
					
					librarians.put(username, password);
				}
			}
		}catch (Exception e) {
			LibraryLogger.LOGGER.log(Level.SEVERE, "An error", e);
		}
		
		
		return librarians;
	}

}
