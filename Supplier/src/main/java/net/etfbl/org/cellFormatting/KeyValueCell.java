package net.etfbl.org.cellFormatting;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class KeyValueCell extends ListCell<KeyValue>{
	
	private final HBox hbox = new HBox();
	private final Label textLabel = new Label();
	private final Label numberLabel = new Label();
	private final Pane spacer = new Pane();
	
	
	public KeyValueCell() {
		//adding components to the hbox
		hbox.getChildren().addAll(textLabel, spacer, numberLabel);
		
		//space between left and right side
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		//align number to the right
		numberLabel.setAlignment(Pos.CENTER_RIGHT);
		
		HBox.setMargin(numberLabel, new Insets(0, 55, 0, 0));
		HBox.setMargin(textLabel, new Insets(0, 0, 0, 20));
	}
	
	@Override
	protected void updateItem(KeyValue item, boolean empty) {
		// TODO Auto-generated method stub
		super.updateItem(item, empty);
		
		if(empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			textLabel.setText(item.getText());
			numberLabel.setText(item.getNumber());
			
			setGraphic(hbox);
		}
	}
	
}
