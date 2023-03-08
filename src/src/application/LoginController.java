package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class LoginController{

	//JSON OBJECT TO REQUEST AND RESPONSE
	private JSONObject jsonReq;
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
    
    @FXML
    public void handleButtonAction(MouseEvent event) throws IOException, InterruptedException {
    	
    	jsonReq=getCredentials();
    	System.out.println(jsonReq.toString());
    	if(!jsonReq.isEmpty()) {
    		lblErrors.setText("");
    		URL urlob=new URL(POST_URL);
        	HttpURLConnection con=(HttpURLConnection) urlob.openConnection();
        	con.setDoOutput(true);
        	con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.connect();
            
            byte[] outputBytes=jsonReq.toString().getBytes("UTF-8");
            OutputStream os=con.getOutputStream();
            os.write(outputBytes);
            os.close();
            
            
            if(con.getResponseCode()==404) {
        		setLblError(Color.RED,"Usuario o contraseña Invalida");
            }else if(con.getResponseCode()==200){
                Login(con,event);
            }else if(con.getResponseCode() == 500) {
        		setLblError(Color.RED,"Error en el Servidor contacte con el Administrador");
            }	    
    	}
    	
    }

    private void Login(HttpURLConnection con,MouseEvent event) throws UnsupportedEncodingException, IOException, InterruptedException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
        StringBuilder response=new StringBuilder();
        String resline=null;
        while((resline = br.readLine()) != null) {
        	response.append(resline.toString().trim());
        }
        String[] arrRes=response.toString().split("\"",5);
        token=arrRes[3];
        try {
            //add you loading or delays - ;-)
            changeStage(event);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
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
        System.out.println(text);
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
