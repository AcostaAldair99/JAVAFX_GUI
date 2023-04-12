package application;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class LoginController{

	//JSON OBJECT TO REQUEST AND RESPONSE
	private JSONObject jsonReq,jsonRes;
	@FXML
    private Button btnCloseWindow;
	
	@FXML
    private Button btnMinizeWindow;
	
	@FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignin;

    //URL
	private static final String POST_URL = "http://127.0.0.1:4040/auth/signIn";
	
	
    public static String token;
    

   
    public void handleButtonAction(MouseEvent event) throws IOException, InterruptedException {
    	jsonReq=getCredentials();
    	
    	if(!jsonReq.isEmpty()) {
    		lblErrors.setText("");
    		
    		HttpResponse<String> response=PostRequest(0,POST_URL);
    		if(response == null) {
    			Alert alert=runAlert(AlertType.ERROR,"Error de Conexión","Revisa tu conexión a internet, no pudimos comunicarnos con el Servidor");
    			alert.showAndWait();
    		}
            if(response.statusCode()==404) {
        		setLblError(Color.RED,"Usuario o contraseña Invalida");
            }else if(response.statusCode()==200){
                setAuthToken(response.body());
            	changeStage(event);
            }else if(response.statusCode() == 500) {
        		setLblError(Color.RED,"Error en el Servidor contacte con el Administrador");
            }	     		
    	}
    	
    }

    public Alert runAlert(AlertType type,String title,String header) {
    	Alert alertShowInfo = new Alert(type);
    	Stage dialogStage = (Stage) alertShowInfo.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(this.getClass().getResourceAsStream("logoLogin.png"))); 
        alertShowInfo.setTitle(title);
        alertShowInfo.setHeaderText(header);
        return alertShowInfo;
    }
    
    
    public HttpResponse<String> PostRequest(int intentos,String URL) throws InterruptedException {
    	HttpResponse<String> response = null;
    	try {
	    	HttpClient client=HttpClient.newHttpClient();
	        HttpRequest req=(HttpRequest) HttpRequest.newBuilder()
	        .setHeader("Content-Type","application/json")
	        .uri(URI.create(URL))
	        .POST(HttpRequest.BodyPublishers.ofString(jsonReq.toString()))
	        .build();        
			response= client.send(req,HttpResponse.BodyHandlers.ofString());
			return response;
		} catch (IOException | InterruptedException e) {
			if(intentos < 5) {
				Thread.sleep(2000);
				return PostRequest(intentos+1,URL);
			}
	//		e.printStackTrace();
		}
    	return response;
    }
    
    private void setAuthToken(String responseBody) {
    	String[] arrRes=responseBody.toString().split("\"",5);
    	token=arrRes[3];
    }
    

    private JSONObject getCredentials() {
    	JSONObject json=new JSONObject();
    	if(txtUsername.getText().isBlank() || txtPassword.getText().isBlank()) {
    		txtUsername.setFocusTraversable(true);
    		setLblError(Color.RED,"Ingresa completas las credenciales");
    	}else {
    		json.put("user", txtUsername.getText());
        	json.put("pass", txtPassword.getText());
    	}
    	return json;
    }
    	
    private void changeStage(MouseEvent event) throws IOException {
    	Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.setMaximized(true);
        stage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("SplashScreen.fxml")));
        stage.setScene(scene);
        stage.show();
    }
    
    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
    }
    
    
    //Custom Close and Minimize Buttons
    
    @FXML
    protected void handleCloseAction(ActionEvent event) {
    	Stage stage=(Stage)btnCloseWindow.getScene().getWindow();
    	stage.close();
    }
    
    
    @FXML
    protected void handleMinizeAction(ActionEvent event) {
    	Stage stage=(Stage)btnMinizeWindow.getScene().getWindow();
    	stage.setIconified(true);
    }

    public String getoken() {
    	return token;
    }
    
    
 
    
}
