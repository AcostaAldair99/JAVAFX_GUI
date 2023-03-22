package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Actas;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SampleController implements Initializable{
	
	//API REQUEST AND RESPONSE HANDLERS
	private static final String GET_ACTAS_URL = "http://127.0.0.1:4040/api/certificates";
		
	private String token;

	@FXML
    private Button btnCloseWindow;
	
	@FXML
    private Button btnMinizeWindow;
	
	@FXML
	private Button btnCeremonia;
    
	@FXML
	private Button btnFolder;
	
	@FXML
	private Button btnConfiguracion;
	
	@FXML
	private Button btnLogout;
	
	@FXML
    private Button btnInicio;

    @FXML
    private Button btnActas;

    @FXML
    private Button btnSinoidales;
    
    @FXML
    private Pane inicioPane;
    @FXML
    private Pane actasPane;
    @FXML
    private Pane sinoidalesPane;
    
    @FXML
    private Pane ceremoniasPane;
    
    @FXML
    private Pane foldersPane;
    
    @FXML
    private Pane configuracionPane;
    
    @FXML
    private StackPane paneMain;
    
    @FXML
    private TableView<Actas> tableActas;
        
    LoginController lc=new LoginController();
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	LoginController swe=new LoginController();
    	token=swe.getoken();
	  	fillTable();
    }

    protected void splitData(Actas a,String delimeter) {
    	String[] data;
    	data=a.getDate_limit_fk().split(delimeter);
    	a.setDate_limit_fk(data[0]);
    	//return data[1];
    }
    
    public void handleClicks(ActionEvent actionEvent) {
      if (actionEvent.getSource() == btnInicio) {
    	  	fillTable();
            inicioPane.toFront();
        }
        if (actionEvent.getSource() == btnActas) {
            actasPane.toFront();
        }
        if (actionEvent.getSource() == btnSinoidales) {
            sinoidalesPane.toFront();
        }
        if(actionEvent.getSource()==btnCeremonia)
        {
            ceremoniasPane.toFront();
        }
        
        if(actionEvent.getSource()==btnFolder)
        {
            foldersPane.toFront();
        }
        
        if(actionEvent.getSource()==btnConfiguracion)
        {
            configuracionPane.toFront();
        }
        
        if(actionEvent.getSource()==btnLogout)
        {
            logOut();
        }
        
  }

  
    private void logOut() {
    	Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Cerrar Sesion", "¿Seguro que quieres cerrar sesion?");
    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
    		System.out.println("Vamonos");
    	} else {
    		System.out.println("Nos quedamos");
    	}
    	
    }
    
    private void fillTable() {
    	tableActas.getItems().clear();
    	List<Actas> actas=getActasRequest(0);
  		if(actas == null) {
  			lc.runAlert(AlertType.ERROR,"Error de Conexion","Revisa tu conexión");
  		}
  		setTableView();
  		for(Actas a:actas) {
  			splitData(a,"T");
  			tableActas.getItems().add(a);
  		}
    }
    
    
    protected List<Actas> getActasRequest(int intentos) {
    	List<Actas> listActas=null;;
    	HttpClient client=HttpClient.newHttpClient();
        HttpRequest req=(HttpRequest) HttpRequest.newBuilder()
        .setHeader("Content-Type","application/json")
        .setHeader("x-access-token", token)
        .uri(URI.create(GET_ACTAS_URL))
        .build();
        try {
			HttpResponse<String> response = client.send(req,HttpResponse.BodyHandlers.ofString());
			ObjectMapper objectMapper = new ObjectMapper();
			listActas = objectMapper.readValue(response.body(), new TypeReference<List<Actas>>(){});
		} catch (IOException | InterruptedException e) {
			if (intentos > 5) {
				System.out.println(intentos);
				return getActasRequest(intentos+1);
			}
			e.printStackTrace();
		}
        return listActas;
    }
    
   
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void setTableView() {
    	TableColumn idActaColumn = new TableColumn("Id Acta");
    	idActaColumn.setCellValueFactory(new PropertyValueFactory<>("id_actas"));
    	idActaColumn.setStyle( "-fx-alignment: center;");
    	idActaColumn.setSortable(false);
    	idActaColumn.setResizable(false);
    	
    	TableColumn  nameColumn= new TableColumn("Estudiante");
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name_Student"));
    	nameColumn.setStyle( "-fx-alignment: center;");
    	nameColumn.setSortable(false);
    	nameColumn.setResizable(false);
    	nameColumn.setMinWidth(200);

    	
    	
    	TableColumn  idStudentColumn= new TableColumn("Matricula");
    	idStudentColumn.setCellValueFactory(new PropertyValueFactory<>("id_Student"));
    	idStudentColumn.setStyle( "-fx-alignment: center;");
    	idStudentColumn.setSortable(false);
    	idStudentColumn.setResizable(false);

    	
    	TableColumn  idFolderColumn= new TableColumn("Folder");
    	idFolderColumn.setCellValueFactory(new PropertyValueFactory<>("id_Folder_fk"));
    	idFolderColumn.setStyle( "-fx-alignment: center;");
    	idFolderColumn.setSortable(false);
    	idFolderColumn.setResizable(false);

    	
    	TableColumn  signaturesColumn= new TableColumn("Status");
    	signaturesColumn.setCellValueFactory(new PropertyValueFactory<>("signatures"));
    	signaturesColumn.setStyle( "-fx-alignment: center;");
    	signaturesColumn.setSortable(false);
    	signaturesColumn.setResizable(false);
    	signaturesColumn.setMinWidth(100);

    	
    	TableColumn  dateLimitColumn= new TableColumn("Fecha Limite");
    	dateLimitColumn.setCellValueFactory(new PropertyValueFactory<>("date_limit_fk"));
    	dateLimitColumn.setStyle( "-fx-alignment: center;");
    	dateLimitColumn.setSortable(false);
    	dateLimitColumn.setResizable(false);
    	dateLimitColumn.setMinWidth(200);
    	
    	TableColumn  ceremonyColumn= new TableColumn("id Ceremonia");
    	ceremonyColumn.setCellValueFactory(new PropertyValueFactory<>("id_ceremony_fk"));
    	ceremonyColumn.setStyle( "-fx-alignment: center;");
    	ceremonyColumn.setSortable(false);
    	ceremonyColumn.setResizable(false);


    	
    	tableActas.getColumns().addAll(idActaColumn,nameColumn,idStudentColumn,idFolderColumn,dateLimitColumn,ceremonyColumn,signaturesColumn);
    }
	
}
