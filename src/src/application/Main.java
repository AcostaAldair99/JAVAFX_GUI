package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("SplashScreen.fxml"));
			primaryStage.setScene(new Scene(root));
	        primaryStage.setResizable(false);
	        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("logoLogin.png")));

	        //set stage borderless
	        primaryStage.initStyle(StageStyle.UNDECORATED);
	        
	        //drag it here
	        /*root.setOnMousePressed(event -> {
	            x = event.getSceneX();
	            y = event.getSceneY();
	        });
	        root.setOnMouseDragged(event -> {

	            primaryStage.setX(event.getScreenX() - x);
	            primaryStage.setY(event.getScreenY() - y);

	        });*/
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
