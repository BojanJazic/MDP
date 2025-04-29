package net.etfbl.org.xmlReader;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {
	
	public static HashMap<String, String> getSuppliersFromXML(){
		HashMap<String, String> suppliers = new HashMap<String, String>();
		try {
			File file = new File("src/main/resources/suppliers/suppliers.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("user");
			
			for(int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element)node;
					String username = element.getElementsByTagName("username").item(0).getTextContent();
					String password = element.getElementsByTagName("password").item(0).getTextContent();
					
					suppliers.put(username, password);
				}
			}
		
		
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Greska u getSuppliersFromXML");
		}
		
		return suppliers;
	}

}
