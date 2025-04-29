package net.etfbl.org.cellFormatting;

public class KeyValue {
	
	private final String text;
	private final String number;
	
	public KeyValue(String text, String number) {
		this.text = text;
		this.number = number;
	}
	
	public String getText() {
		return text;
	}

	public String getNumber() {
		return number;
	}

}
