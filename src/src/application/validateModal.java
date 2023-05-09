package application;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class validateModal{
	@FXML
	private Button cancelButton,validateButton;
	@FXML
	private TextField inputDialog;
	
	private String data;
	
	private Stage stage;
	
	public Stage showModal(ActionEvent event) throws IOException {
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setResizable(false);
    	stage.initStyle(StageStyle.UNDECORATED);

		Parent root = FXMLLoader.load(validateModal.class.getResource("validateModal.fxml"));
		Scene sc = new Scene(root);
		stage.setScene(sc);
		stage.initOwner(
			        ((Node)event.getSource()).getScene().getWindow() );
		
	
		
		return stage;
		//stage.showAndWait();	
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public void handleClicks(ActionEvent ac) {
		if(ac.getSource() == cancelButton) {
			Stage stage=(Stage)cancelButton.getScene().getWindow();
	    	stage.close();
		}
		
		if(ac.getSource() == validateButton) {
			setData(inputDialog.getText());
			System.out.println(data);
			Stage stage=(Stage)cancelButton.getScene().getWindow();
	    	stage.close();
		}
	}
	
	
}
