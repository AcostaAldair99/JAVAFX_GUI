package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashController implements Initializable{
	
	@FXML
	private	 AnchorPane rootPane;
	
	@FXML
	private ProgressBar progressBar;
	
	private double progress=0;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		progressBar.setStyle("-fx-accent:#00FF00");
		new SplashScreen().start();
	}
	
	
	class SplashScreen extends Thread{
		@Override
		public void run() {
			try {
				for(int i=0;i<20;i++) {
					progress+=0.05;
					progressBar.setProgress(progress);
					Thread.sleep(100);
				}
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Parent root=null;
						try {
							root = FXMLLoader.load(getClass().getResource("Sample.fxml"));		
						}catch(IOException e) {
							e.printStackTrace();
						}
						Scene es=new Scene(root);
						
						Stage st=new Stage();
						st.setScene(es);
						st.getIcons().add(new Image(this.getClass().getResourceAsStream("logoLogin.png")));
						st.initStyle(StageStyle.UNDECORATED);
						st.show();
						rootPane.getScene().getWindow().hide();		
					}
				});
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
