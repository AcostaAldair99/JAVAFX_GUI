package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SingleSelectionModel;
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
import models.Ceremonias;
import models.Folder;
import models.Sinoidales;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SampleController implements Initializable{
	
	//API REQUEST AND RESPONSE HANDLERS		
	private String token;
	Alert alert;

	@FXML
    private Button btnCloseWindow;
	
	@FXML
    private Button btnMinizeWindow,btnAgregarSinoidales;
	
	@FXML
	private ComboBox<String> cbSinoidales,cbFolders,cbCeremonias;
	
	@FXML
	private Button btnCeremonia,btnCrearActa,btnAsignarSinoidal,btnEliminarSinoidal;
    
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
    private Button btnAgregarActa;
    
    @FXML
    private Button btnAgregarSinoidal;
    
    @FXML
    private Button btnBuscar;
    
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
        
    @FXML
    private TableView<Sinoidales> tableSinoidales;
    
    @FXML
    private TableView<Ceremonias> tableCeremonias;
    @FXML
    private ListView<String> listSinoidales;
    
    LoginController lc=new LoginController();
   

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	LoginController swe=new LoginController();
    	token=swe.getoken();
    	setTableViewActas();
    	setTableViewSinoidales();
    	setTableViewCeremony();
    	try {
			fillTableActas();
		} catch (JsonProcessingException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    protected String splitData(String date,String delimeter) {
    	String[] data;
    	data=date.split(delimeter);
    	return String.valueOf(data[0]);
    }
    
    public void handleClicks(ActionEvent actionEvent) throws InterruptedException {
      if (actionEvent.getSource() == btnInicio) {
    	  	try {
				fillTableActas();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            inicioPane.toFront();
        }
        if (actionEvent.getSource() == btnActas || actionEvent.getSource() == btnAgregarActa) {
            actasPane.toFront();
            try {
				setComboboxActas();
			} catch (JsonProcessingException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if (actionEvent.getSource() == btnSinoidales) {
        	try {
				fillTableSinoidales();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            sinoidalesPane.toFront();
        }
        if(actionEvent.getSource()==btnCeremonia)
        {
        	try {
				fillTableCeremony();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
        
        if(actionEvent.getSource() == btnAsignarSinoidal) {
        	addSelectSinoidales();
        }
  
        if(actionEvent.getSource() == btnEliminarSinoidal) {
        	deleteSelectedSinoidal();
        }
        
        if(actionEvent.getSource() == btnCrearActa) {
        	
        }
        
    }
    
    private void deleteSelectedSinoidal() {
    	MultipleSelectionModel<String> selected = listSinoidales.getSelectionModel();
    	if(selected.getSelectedItem() != null) {
    		Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Eliminar Sinoidal", "¿Seguro que quieres desasignar esta acta al sinoidal?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.get() == ButtonType.OK){
        		cbSinoidales.getItems().add(selected.getSelectedItem());
        		listSinoidales.getItems().remove(selected.getSelectedIndex());
        		if(listSinoidales.getItems().size() == 0 ) {
        			btnEliminarSinoidal.setDisable(true);
        		}
        	}
    	}else {
    		alert = lc.runAlert(AlertType.ERROR,"Error Sinoidales","Selecciona un elemento de la lista para eliminar");
			alert.showAndWait();
    	}
    }
    
    private void addSelectSinoidales() {
    	SingleSelectionModel<String> id = cbSinoidales.getSelectionModel();
    	if(id.getSelectedItem() != null ) {
    		if(listSinoidales.getItems().size() < 3) {
    			listSinoidales.getItems().add(id.getSelectedItem());
    			cbSinoidales.getItems().remove(id.getSelectedIndex());
    			btnEliminarSinoidal.setDisable(false);
    		}else {
    			alert = lc.runAlert(AlertType.ERROR,"Error Sinoidales","Ya seleccionaste los 3 sinoidales necesarios");
    			alert.showAndWait();
    		}
    	}else {
    		alert = lc.runAlert(AlertType.ERROR,"Error Sinoidal","Selecciona un Sinoidal para asignarlo al Acta");
    		alert.showAndWait();
    	}
    }
    
    private void setComboboxActas() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	listSinoidales.getItems().clear();
    	cbFolders.getItems().clear();
    	cbCeremonias.getItems().clear();
    	cbSinoidales.getItems().clear();
    	cbFolders.setPromptText("Selecciona un Folder");
    	cbCeremonias.setPromptText("Selecciona la Ceremonia");
    	cbSinoidales.setPromptText("Selecciona el sinoidal");
    	String sample;
    	List<Sinoidales> sinoidales=handleResponseSinoidales("http://127.0.0.1:4040/api/sinoidales");
    	List<Folder> folders = handleResponseFolder("http://127.0.0.1:4040/api/folders");
    	List<Ceremonias> ceremonias=handleResponseCeremony("http://127.0.0.1:4040/api/ceremonies");
    	for(Folder f : folders) {
    		cbFolders.getItems().add(f.getId_case().toString());
    	}
    	
    	for(Sinoidales s: sinoidales) {
    		sample = s.getFirst_Name() + " " + s.getSecond_Name();
    		cbSinoidales.getItems().add(sample);
    	}
    	
    	for(Ceremonias c: ceremonias) {
    		cbCeremonias.getItems().add(c.getId_ceremony().toString());
    	}
    }
  
    private void logOut() {
    	Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Cerrar Sesion", "¿Seguro que quieres cerrar sesion?");
    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
    		Stage stage=(Stage)btnLogout.getScene().getWindow();
        	stage.close();
    		
    	} else {
    		System.out.println("Nos quedamos");
    	}
    	
    }
    
    private void fillTableActas() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	tableActas.getItems().clear();
    	List<Actas> actas=handleResponseActas("http://127.0.0.1:4040/api/certificates");
  		/*if(actas == null) {
  			lc.runAlert(AlertType.ERROR,"Error de Conexion","Revisa tu conexión");
  		}*/
  		for(Actas a:actas) {
  			a.setDate_limit_fk(splitData(a.getDate_limit_fk(),"T"));
  			tableActas.getItems().add(a);
  		}
    }
    
    
    private void fillTableSinoidales() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	tableSinoidales.getItems().clear();
    	List<Sinoidales> sinoidales=handleResponseSinoidales("http://127.0.0.1:4040/api/sinoidales");
    	for(Sinoidales s:sinoidales) {
  			//splitData(,"T");
  			tableSinoidales.getItems().add(s);
  		}
    }
    
    private void fillTableCeremony() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	tableCeremonias.getItems().clear();
    	List<Ceremonias> ceremonias=handleResponseCeremony("http://127.0.0.1:4040/api/ceremonies");
    	for(Ceremonias s:ceremonias) {
  			s.setDate(splitData(s.getDate(), "T"));
  			tableCeremonias.getItems().add(s);
  		}
    }
    
    protected List<Actas> handleResponseActas(String URL) throws JsonMappingException, JsonProcessingException, InterruptedException {
    	HttpResponse<String> response=getRequest(0,URL);
    	List<Actas> listActas=null;
    	ObjectMapper objectMapper = new ObjectMapper();
		listActas = objectMapper.readValue(response.body(), new TypeReference<List<Actas>>(){});
        return listActas;
    }
    
    
    protected List<Folder> handleResponseFolder(String URL) throws InterruptedException, JsonMappingException, JsonProcessingException{
    	HttpResponse<String> res = getRequest(0,URL);
    	List<Folder> listFolder = null;
    	ObjectMapper om = new ObjectMapper();
		listFolder = om.readValue(res.body(), new TypeReference<List<Folder>>(){});
		return listFolder;
    }
    
    protected List<Sinoidales> handleResponseSinoidales(String URL) throws JsonMappingException, JsonProcessingException, InterruptedException{
    	HttpResponse<String> response=getRequest(0,URL);
    	List<Sinoidales> listSinoidales=null;
    	ObjectMapper objectMapper = new ObjectMapper();
		listSinoidales = objectMapper.readValue(response.body(), new TypeReference<List<Sinoidales>>(){});
        return listSinoidales;
    }
    
    protected List<Ceremonias> handleResponseCeremony(String URL) throws InterruptedException, JsonMappingException, JsonProcessingException{
    	HttpResponse<String> response=getRequest(0,URL);
    	List<Ceremonias> listCeremonias=null;
    	ObjectMapper objectMapper = new ObjectMapper();
		listCeremonias = objectMapper.readValue(response.body(), new TypeReference<List<Ceremonias>>(){});
        return listCeremonias;
    }
    
    
    protected HttpResponse<String> getRequest(int intentos, String URL) throws InterruptedException{
    	HttpResponse<String> response=null;
    	try {
    	HttpClient client=HttpClient.newHttpClient();
        HttpRequest req=(HttpRequest) HttpRequest.newBuilder()
        .setHeader("Content-Type","application/json")
        .setHeader("x-access-token", token)
        .uri(URI.create(URL))
        .build();
		response = client.send(req,HttpResponse.BodyHandlers.ofString());
        }catch(IOException | InterruptedException e) {
        	if(intentos < 5) {
        		System.out.println(intentos);
    			return getRequest(intentos+1,URL);
        	}
        	alert = lc.runAlert(AlertType.ERROR,"Error de Conexion","Revisa tu conexión");
        	alert.showAndWait();
        	e.printStackTrace();
        }
        return response;
    }
    
    
    
    @FXML
    protected void handleMinizeAction(ActionEvent event) {
    	Stage stage=(Stage)btnMinizeWindow.getScene().getWindow();
    	stage.setIconified(true);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void setTableViewActas() {
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
    	signaturesColumn.setMinWidth(150);

    	
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setTableViewSinoidales() {
		TableColumn idSinoidal = new TableColumn("Id");
		idSinoidal.setCellValueFactory(new PropertyValueFactory<>("id_sinoidales"));
		idSinoidal.setStyle( "-fx-alignment: center;");
		idSinoidal.setSortable(false);
		idSinoidal.setResizable(false);
		
    	TableColumn FirstName = new TableColumn("Nombre");
    	FirstName.setCellValueFactory(new PropertyValueFactory<>("first_Name"));
    	FirstName.setStyle( "-fx-alignment: center;");
    	FirstName.setSortable(false);
    	FirstName.setResizable(false);
    	
    	TableColumn  secondName= new TableColumn("Apellidos");
    	secondName.setCellValueFactory(new PropertyValueFactory<>("second_Name"));
    	secondName.setStyle( "-fx-alignment: center;");
    	secondName.setSortable(false);
    	secondName.setResizable(false);
    	//secondName.setMinWidth(200);

    	TableColumn  idProfessor= new TableColumn("Matricula");
    	idProfessor.setCellValueFactory(new PropertyValueFactory<>("id_professor"));
    	idProfessor.setStyle( "-fx-alignment: center;");
    	idProfessor.setSortable(false);
    	idProfessor.setResizable(false);

    	
    	TableColumn  emailColumn= new TableColumn("Email");
    	emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    	emailColumn.setStyle( "-fx-alignment: center;");
    	emailColumn.setSortable(false);
    	emailColumn.setResizable(false);
    	emailColumn.setMinWidth(200);
    	
    	TableColumn  areaColumn= new TableColumn("Area");
    	areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
    	areaColumn.setStyle( "-fx-alignment: center;");
    	areaColumn.setSortable(false);
    	areaColumn.setResizable(false);
    	areaColumn.setMinWidth(150);

    	
    	TableColumn  telefonoColumn= new TableColumn("Telefono");
    	telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
    	telefonoColumn.setStyle( "-fx-alignment: center;");
    	telefonoColumn.setSortable(false);
    	telefonoColumn.setResizable(false);
    	telefonoColumn.setMinWidth(100);
    	
    	TableColumn  disponibilityColumn= new TableColumn("Disponibilidad");
    	disponibilityColumn.setCellValueFactory(new PropertyValueFactory<>("disponibility"));
    	disponibilityColumn.setStyle( "-fx-alignment: center;");
    	disponibilityColumn.setSortable(false);
    	disponibilityColumn.setResizable(false);
    	disponibilityColumn.setMaxWidth(50);
    	
    	TableColumn  activeColumn= new TableColumn("Activo");
    	activeColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));
    	activeColumn.setStyle( "-fx-alignment: center;");
    	activeColumn.setSortable(false);
    	activeColumn.setResizable(false);
    	activeColumn.setMaxWidth(50);
    	
    	tableSinoidales.getColumns().addAll(idSinoidal,FirstName,secondName,idProfessor,emailColumn,areaColumn,telefonoColumn,disponibilityColumn,activeColumn);
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setTableViewCeremony() {
		TableColumn idCeremony = new TableColumn("Id");
		idCeremony.setCellValueFactory(new PropertyValueFactory<>("id_ceremony"));
		idCeremony.setStyle( "-fx-alignment: center;");
		idCeremony.setSortable(false);
		idCeremony.setResizable(false);
		
    	TableColumn dateLimit = new TableColumn("Fecha Limite");
    	dateLimit.setCellValueFactory(new PropertyValueFactory<>("date"));
    	dateLimit.setStyle( "-fx-alignment: center;");
    	dateLimit.setSortable(false);
    	dateLimit.setResizable(false);
    	dateLimit.setMinWidth(200);
    	
    	TableColumn  cicleColumn= new TableColumn("Ciclo");
    	cicleColumn.setCellValueFactory(new PropertyValueFactory<>("cicle"));
    	cicleColumn.setStyle( "-fx-alignment: center;");
    	cicleColumn.setSortable(false);
    	cicleColumn.setResizable(false);
    	cicleColumn.setMinWidth(200);

  
    	tableCeremonias.getColumns().addAll(idCeremony,dateLimit,cicleColumn);
    }
}
