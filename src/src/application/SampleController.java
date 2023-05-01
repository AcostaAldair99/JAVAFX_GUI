package application;


import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import models.Actas;
import models.Actas_Sinoidales;
import models.Ceremonias;
import models.Email;
import models.Folder;
import models.Sinoidales;
import models.Telephone;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
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
	private BigInteger id_Acta=null;
	
	
	@FXML
    private Button btnCloseWindow,btnActualizarSinoidal,btnEliminarSinoidales,btnAgregarTelefono,btnAgregarEmail,btnEliminarTelefono,btnEliminarEmail;
	
	@FXML
    private Button btnMinizeWindow,btnAgregarSinoidales,btnCrearSinoidal,btnCrearFolder;
	
	@FXML
	private ComboBox<String> cbSinoidales,cbFolders,cbCeremonias,cbCarreras,cbCiclo,cbEstantes;
	
	@FXML
	private DatePicker datePickerCeremonia;
	
	@FXML
	private Button btnCeremonia,btnCrearActa,btnAsignarSinoidal,btnEliminarSinoidal,btnCrearCeremonia;
    
	@FXML
	private Button btnFolder;
	
	@FXML
	private Button btnConfiguracion;
	
	@FXML
	private Button btnLogout;
	
	@FXML
    private Button btnInicio;

    @FXML
    private Button btnActas,btnActualizarActa,btnEliminarActa;

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
    private Pane addSinoidales;
    
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
    private TableView<Folder>tableFolders;
    
    @FXML
    private ListView<String> listSinoidales,listTelefonos,listCorreos;
    
    LoginController lc=new LoginController();
    
    @FXML
    private TextField txtNameStudent,txtIdStudent,txtPlanStudent,txtApellidosStudent;
    @FXML
    private TextField txtNombreSinoidal,txtApellidosSinoidal,txtIdSinoidal,txtEmailSinoidal,txtTelefonoSinoidal,txtCoordinacionSinoidal;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	LoginController swe=new LoginController();
    	token=swe.getoken();
    	setTableViewActas();
    	setTableViewSinoidales();
    	setTableViewCeremony();
    	setTableViewFolders();
    	try {
			fillTableActas();
			fillTableSinoidales();
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
    
    public void handleClicks(ActionEvent actionEvent) throws InterruptedException, IOException {
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
            	clearFormActas();
				
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
        		setFormCeremonia();
				fillTableCeremony();
				filltableFolder();
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
        
        if(actionEvent.getSource()==btnAgregarSinoidal) {
        	clearFormSinoidales();
        	addSinoidales.toFront();
        }
  
        if(actionEvent.getSource() == btnEliminarSinoidal) {
        	deleteSelectedSinoidal();
        }
        
        if(actionEvent.getSource() == btnCrearActa) {
        	createActa();
        	
        }
        if(actionEvent.getSource()==btnCrearSinoidal) {
        	createSinoidal();
        }
        
        if(actionEvent.getSource()==btnCrearCeremonia) {
        	createCeremonia();
        }
        
        if(actionEvent.getSource()==btnCrearFolder) {
        	createFolder();
        }
        
        if(actionEvent.getSource() == btnEliminarActa) {
        	deleteActa();
        }
        
        if(actionEvent.getSource() == btnActualizarActa) {
        	updateActa();
        	
        }
        
        if(actionEvent.getSource() == btnAgregarTelefono) {
        	addTelephoneToList();
        }
        
        if(actionEvent.getSource() == btnAgregarEmail) {
        	addEmailToList();
        }
        
        if(actionEvent.getSource() == btnEliminarEmail) {
        	deleteSelected(listCorreos,"Eliminar Email","¿Seguro que quieres eliminar este email?",btnEliminarEmail);
        }
        
        if(actionEvent.getSource() == btnEliminarTelefono) {
        	deleteSelected(listTelefonos,"Eliminar Telefono","¿Seguro que quieres eliminar este telefono?",btnEliminarTelefono);
        }
    }
    
    
    private void deleteSelected(ListView<String> list,String Title,String message,Button btn) {
    	MultipleSelectionModel<String> selected = list.getSelectionModel();
    	if(selected.getSelectedItem() != null) {
    		Alert alert=lc.runAlert(AlertType.CONFIRMATION, Title,message);
        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.get() == ButtonType.OK){
        		list.getItems().remove(selected.getSelectedIndex());
        		if(list.getItems().size() == 0 ) {
        			btn.setDisable(true);
        		}
        		alert=lc.runAlert(AlertType.INFORMATION, Title, "Elemento eliminado correctamente");
        		alert.show();
        	}
    	}else {
    		alert = lc.runAlert(AlertType.ERROR,"Error ","Selecciona un elemento de la lista para eliminar");
			alert.showAndWait();
    	}
    }
    
    private void addEmailToList() {
		if(validateTextField(txtEmailSinoidal,"El valor del campo email es nulo o contiene letras","\"[a-zA-Z0-9_!#$%&'*+/=?``{|}~^.]+@[a-zA-Z0-9.]+$\"g",255)){
			Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Agregar Email", "¿Seguro que quieres asignar este email a este sinoidal?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.get() == ButtonType.OK) {
        		if(listCorreos.getItems().contains(txtEmailSinoidal.getText())) {
        			alert=lc.runAlert(AlertType.ERROR, "ERROR EMAIL", "El email ingresado ya se encuentra asignado");
        			alert.show();
        			txtEmailSinoidal.clear();
        		}else {
        			listCorreos.getItems().add(txtEmailSinoidal.getText());
        			txtEmailSinoidal.clear();
        			btnEliminarEmail.setDisable(false);
        		}
        	}
		}

    }
    
    private void addTelephoneToList() {
    	if(validateTextField(txtTelefonoSinoidal,"El valor en el campo telefono es nulo o contiene letras","[0-9]+",10)){
    		Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Agregar Telefono", "¿Seguro que quieres asignar este telefono a este sinoidal?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.get() == ButtonType.OK) {
        		if(listTelefonos.getItems().contains(txtTelefonoSinoidal.getText())) {
        			alert=lc.runAlert(AlertType.ERROR, "ERROR TELEFONO", "El telefono ingresado ya se encuentra asignado.");
        			alert.show();
        			txtTelefonoSinoidal.clear();
        		}else {
        			listTelefonos.getItems().add(txtTelefonoSinoidal.getText());
        			txtTelefonoSinoidal.clear();
        			alert=lc.runAlert(AlertType.INFORMATION, "TELEFONO ASIGNADO", "El telefono asignado correctamente.");
        			alert.show();
        			btnEliminarTelefono.setDisable(false);
        		}
        	}
    	}
    }
    
    
    private void updateActa() throws InterruptedException, IOException {
    	int i=1;
    	if(validateList(listSinoidales,3,"No se han asignado todos los 3 sinoidales necesarios para crear el Acta")) {
    		Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Crear Folder", "¿Seguro que quieres actualizar esta Acta en el sistema?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.get() == ButtonType.OK) {
        		JSONObject json = new JSONObject();
        		json.put("idCeremony", Integer.parseInt(cbCeremonias.getValue()));
        		json.put("idFolder", Integer.parseInt(cbFolders.getValue()));
        		HttpResponse<String> rps = putRequest(0,"http://127.0.0.1:4040/api/certificates/updateActa/"+id_Acta,json);
        		if(rps == null || rps.statusCode()!=201) {
        			alert=lc.runAlert(AlertType.ERROR, "ERROR Acta", "Hubo un error al momento de actualizar el acta, verifica tu conexión a internet.\nStatus: "+rps.statusCode());
            		alert.show();
        		}else {
        			rps = deleteRequest(0,"http://127.0.0.1:4040/api/certificates/actasSinoidales/delete/"+id_Acta);
        			if(rps == null || rps.statusCode()!=201) {
        				alert=lc.runAlert(AlertType.ERROR, "ERROR Acta", "Hubo un error al momento de actualizar el acta, verifica tu conexión a internet.\nStatus: "+rps.statusCode());
                		alert.show();
        			}else {	
        				for(String s:listSinoidales.getItems()) {
        					String x[] =s.split("-");
        					System.out.println(x[0]);
                			rps = postRequest(0,"http://127.0.0.1:4040/api/certificates/addSinoidales/"+id_Acta+"/"+cbCeremonias.getValue()+"/"+x[0],json);
                			if(rps == null || rps.statusCode()!=201) {
                				alert=lc.runAlert(AlertType.ERROR, "ERROR Acta", "Hubo un error al momento de actualizar el acta, verifica tu conexión a internet.\nStatus: "+rps.statusCode());
                        		alert.show();
                			}else if(i>=listSinoidales.getItems().size()) {
                				inicioPane.toFront();
                				fillTableActas();
                				alert=lc.runAlert(AlertType.INFORMATION, "Acta Actualizada", "El acta fue actualizada correctamente");
                        		alert.show();
                			}
                			i++;
        				}
        			}
        		}
        	}
    	}
    	
    }
    
    
    private void createFolder() throws InterruptedException, JsonMappingException, JsonProcessingException {
    	if(validateComboBox(cbEstantes,"Estantes")) {
    		Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Crear Folder", "¿Seguro que quieres crear este nuevo folder en el sistema?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.get() == ButtonType.OK) {
        		JSONObject j = new JSONObject();
        		j.put("case",cbEstantes.getValue());
        		
        		System.out.println(j);
        		HttpResponse<String> res = postRequest(0,"http://127.0.0.1:4040/api/folders",j);
        		if(res == null||res.statusCode() !=201) {
        			alert=lc.runAlert(AlertType.ERROR, "ERROR Folder", "Hubo un error al momento de crear el Folder, verifica tu conexión a internet.\nStatus: "+res.statusCode());
            		alert.show();
        		}else {
        			filltableFolder();
        			alert=lc.runAlert(AlertType.INFORMATION, "Folder CREADO", "El nuevo Folder ha sido creado exitosamente.");
            		alert.show();
        		}
        	}
    	}
    	
    	
    }
    
    private boolean checkActasList(String idStudent) {
    	for(Actas act : tableActas.getItems()) {
    		if(act.getId_Student().toString().equals(idStudent)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    private boolean checkSinoidalesList(String idStudent) {
    	for(Sinoidales sino : tableSinoidales.getItems()) {
    		if(sino.getId_professor().toString().equals(idStudent)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    private void createActa() throws InterruptedException, JsonMappingException, JsonProcessingException{
    	
    	if(validateTextField(txtNameStudent,"El valor del campo nombre es nulo o numerico","[a-zA-Z\u00f1\u00d1\s]+",255) && 
    			validateTextField(txtApellidosStudent,"El valor del campo apellidos es nulo o numerico","[a-zA-Z\u00f1\u00d1\s]+",255) &&
    			validateTextField(txtIdStudent,"El valor del campo matricula es nulo o contiene letras","[0-9]+",10) && 
    			validateTextField(txtPlanStudent,"El valor del campo plan de estudios es nulo o contiene letras","[0-9]+",3) &&
    			validateComboBox(cbCeremonias,"Ceremonias") &&
    			validateComboBox(cbFolders,"Folder") &&
    			validateComboBox(cbCarreras,"carreras") &&
    			validateList(listSinoidales,3,"No se han asignado todos los 3 sinoidales necesarios para crear el Acta")) {
    		if(!checkActasList(txtIdStudent.getText())) {
    			Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Crear Acta", "¿Seguro que quieres crear esta acta?");
            	Optional<ButtonType> result = alert.showAndWait();
            	
            	if (result.get() == ButtonType.OK){
            		JSONObject json=new JSONObject();
            		
            		json.put("name_Student",txtNameStudent.getText());
            		json.put("lastName_Student",txtApellidosStudent.getText());
            		json.put("id_Student", txtIdStudent.getText());
            		json.put("degree_plan", txtPlanStudent.getText());
            		json.put("id_ceremony", cbCeremonias.getValue());
            		json.put("id_Folder", cbFolders.getValue());
            		json.put("degree", cbCarreras.getValue());
            		json.put("signatures", 0);
            		ObservableList<String>sinoidalesAsign = listSinoidales.getItems();
            		HttpResponse<String> res = postRequest(0,"http://127.0.0.1:4040/api/certificates",json);
            		String s[]=res.body().split("[.!:;?{}]");
            		if(res.statusCode() == 201) {
            			for(String a: sinoidalesAsign) {
                			String[] x=a.split("-");
                			res = postRequest(0,"http://127.0.0.1:4040/api/certificates/addSinoidales/"+s[2]+"/"+cbCeremonias.getValue()+"/"+x[0],json);
                			//System.out.println(res);
                		}
            			if(res.statusCode() == 201) {
            				res = putRequest(0,"http://127.0.0.1:4040/api/folders/addActa/"+cbFolders.getValue(),json);
            				if(res.statusCode()!=201) {
            					alert=lc.runAlert(AlertType.ERROR, "ERROR ACTA", "Hubo un error al momento de crear el acta, verifica tu conexión a internet\nStatus: "+res.statusCode());
                        		alert.show();
            				}else {
            					inicioPane.toFront();
            					fillTableActas();
            					alert=lc.runAlert(AlertType.INFORMATION, "Acta Creada", "Nueva Acta Creada Exitosamente.");
                        		alert.show();
            				}
            			}
            		}else {
            			alert=lc.runAlert(AlertType.ERROR, "ERROR ACTA", "Hubo un error al momento de crear el acta, verifica tu conexión a internet\nStatus: "+res.statusCode());
                		alert.show();
            		}
            		
            	}
        	}else {
        		alert=lc.runAlert(AlertType.ERROR, "ERROR ACTA", "Ya hay un Acta registrada con el número de matricula ingresado.");
        		alert.show();
        		txtIdStudent.clear();
        		txtIdStudent.requestFocus();
        	}
    		
    		
    	}
    }
    
    private void clearFormActas() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	setComboboxActas();
    	txtNameStudent.clear();
    	txtIdStudent.clear();
    	txtPlanStudent.clear();
    	txtApellidosStudent.clear();
    	txtNameStudent.setDisable(false);
    	txtIdStudent.setDisable(false);
    	txtPlanStudent.setDisable(false);
    	txtApellidosStudent.setDisable(false);
    	btnActualizarActa.setVisible(false);
    	btnEliminarActa.setVisible(false);
    	btnCrearActa.setVisible(true);
    }
    
    private void clearFormSinoidales() {
    	txtNombreSinoidal.clear();
    	txtApellidosSinoidal.clear();
    	txtIdSinoidal.clear();
    	txtEmailSinoidal.clear();
    	txtTelefonoSinoidal.clear();
    	txtCoordinacionSinoidal.clear();
    	txtNombreSinoidal.setDisable(false);
    	txtApellidosSinoidal.setDisable(false);
    	txtIdSinoidal.setDisable(false);
    	txtEmailSinoidal.setDisable(false);
    	txtTelefonoSinoidal.setDisable(false);
    	txtCoordinacionSinoidal.setDisable(false);
    	btnCrearSinoidal.setVisible(true);
    	btnActualizarSinoidal.setVisible(false);
    	btnEliminarSinoidales.setVisible(false);
    	listTelefonos.getItems().clear();
    	listCorreos.getItems().clear();
    	btnEliminarTelefono.setDisable(true);
    	btnEliminarEmail.setDisable(true);
    }
    
    private void createSinoidal() throws InterruptedException, JsonMappingException, JsonProcessingException {
    	if(validateTextField(txtNombreSinoidal,"El valor del campo nombre es nulo o numerico","[a-zA-Z\u00f1\u00d1\s]+",255) && 
    			validateTextField(txtApellidosSinoidal,"El valor del campo apellidos es nulo o numerico","[a-zA-Z\u00f1\u00d1\s]+",255) &&
    			validateTextField(txtIdSinoidal,"El valor del campo matricula es nulo o numerico","[0-9]+",10)&&
    			validateTextField(txtCoordinacionSinoidal,"El valor del coordinación es nulo o contiene letras","[a-zA-Z\u00f1\u00d1\s]+",255) &&
    			validateList(listTelefonos,1,"Ingresa al menos un telefono para asignarlo al sinoidal") &&
    			validateList(listCorreos,1,"Ingresa al menos un email para asignarlo al sinoidal")){
    		if(!checkSinoidalesList(txtIdSinoidal.getText())) {
    			Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Crear Sinoidal", "¿Seguro que quieres crear este sinoidal?");
	        	Optional<ButtonType> result = alert.showAndWait();
	        	if(result.get()==ButtonType.OK) {
	        		JSONObject js = new JSONObject();
	        		js.put("first_Name", txtNombreSinoidal.getText());
	        		js.put("second_Name", txtApellidosSinoidal.getText());
	        		js.put("id_professor", txtIdSinoidal.getText());
	        		js.put("email", txtEmailSinoidal.getText());
	        		js.put("area", txtCoordinacionSinoidal.getText());
	        		js.put("telephone", txtTelefonoSinoidal.getText());
	        		js.put("disponibility",1);
	        		js.put("isActive",1);
	        		HttpResponse<String> response = postRequest(0,"http://127.0.0.1:4040/api/sinoidales",js);
	        		if( response == null || response.statusCode()!=201) {
	        			alert=lc.runAlert(AlertType.ERROR, "ERROR SINOIDAL", "Hubo un error al momento de crear el sinoidal, verifica tu conexión a internet.\nStatus: "+response.statusCode());
	            		alert.show();
	        		}else {
	        			fillTableSinoidales();
	        			sinoidalesPane.toFront();
	        			alert=lc.runAlert(AlertType.INFORMATION, "SINOIDAL CREADO", "El nuevo Sinoidal ha sido creado exitosamente.");
	            		alert.show();
	        		}
	        	}
    		}else {
    			alert=lc.runAlert(AlertType.ERROR, "ERROR SINOIDAL", "Ya hay un Sinoidal registrado con el número de matricula ingresado.");
        		alert.show();
        		txtIdSinoidal.clear();
        		txtIdSinoidal.requestFocus();
    		}
	    		
    	}
    }
   
    private void deleteActa() throws IOException, InterruptedException {
    		HttpResponse<String> res = null;
			Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Eliminar Acta", "¿Seguro que quieres eliminar el acta con el id #"+id_Acta+" ?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.get()==ButtonType.OK) {
        		res = deleteRequest(0,"http://127.0.0.1:4040/api/certificates/actasSinoidales/delete/"+id_Acta);
        		if(res == null || res.statusCode() !=201) {
        			alert=lc.runAlert(AlertType.ERROR, "ERROR ELIMINAR ACTA", "Hubo un error al momento de eliminar el acta, verifica tu conexión a internet.\nStatus: "+res.statusCode());
        			alert.show();
        		}else {
        			res = deleteRequest(0,"http://127.0.0.1:4040/api/certificates/"+id_Acta);
        			if(res == null || res.statusCode() != 201) {
        				alert=lc.runAlert(AlertType.ERROR, "ERROR ELIMINAR ACTA", "Hubo un error al momento de eliminar el acta, verifica tu conexión a internet.\nStatus: "+res.statusCode());
                		alert.show();
        			}else {
        				fillTableActas();
        				inicioPane.toFront();
        				alert=lc.runAlert(AlertType.INFORMATION, "ACTA ELIMINADA", "El Acta fue eliminada correctamente.");
                		alert.show();
        			}
        		}
        	}
    }
    
    private HttpResponse<String> deleteRequest(int intentos,String URL) throws IOException, InterruptedException {
		HttpResponse<String> response=null;
    	try {
    		HttpClient cli = HttpClient.newHttpClient();
            HttpRequest req=(HttpRequest) HttpRequest.newBuilder()
            .setHeader("Content-Type","application/json")
            .setHeader("x-access-token", token)
            .uri(URI.create(URL))
            .DELETE()
            .build();        
    		response= cli.send(req,HttpResponse.BodyHandlers.ofString());
    		return response;
    	}catch(IOException | InterruptedException e) {
    		if(intentos < 5) {
				Thread.sleep(2000);
				return deleteRequest(intentos+1,URL);
			}
    	}
    	return response;
    }
    
    
    private void createCeremonia() throws InterruptedException, JsonMappingException, JsonProcessingException {
    	if(datePickerCeremonia.getValue()!=null) {
    		Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Crear Ceremonia", "¿Seguro que quieres crear esta ceremonia con fecha del "+datePickerCeremonia.getValue()+" ?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.get() == ButtonType.OK) {
        		JSONObject json = new JSONObject();
        		json.put("date",datePickerCeremonia.getValue());
        		json.put("cicle", getCicle(datePickerCeremonia.getValue()));
        		HttpResponse<String> res = postRequest(0,"http://127.0.0.1:4040/api/ceremonies",json);
        		if(res.statusCode()!=201) {
        			alert=lc.runAlert(AlertType.ERROR, "ERROR CEREMONIA", "Hubo un error al momento de crear la ceremonia, verifica tu conexión a internet\nStatus: "+res.statusCode());
            		alert.show();
        		}else {
    				fillTableCeremony();
        			alert=lc.runAlert(AlertType.ERROR, "CEREMONIA CREADA", "Se creo la ceremonia de manera exitosa !\nComienza a asignar actas a la nueva ceremonia");
            		alert.show();
        		}
        	}
    	}else {
    		alert=lc.runAlert(AlertType.ERROR, "ERROR FECHA", "Ingresa un valor en el campo fecha");
    		alert.show();
    	}
    }
    
	private String getCicle(LocalDate date_limit){
		ChronoLocalDate dt= LocalDate.parse("2023-06-01");
    	if(date_limit.isAfter(dt)) {
    		return "ENE-JUN";
    	}else {
    		return "AGO-DIC";
    	}
    }
    
	private HttpResponse<String> postRequest(int intentos,String URL,JSONObject jsonBody) throws InterruptedException{
    	HttpResponse<String> response = null;
    	try {
	    	HttpClient client=HttpClient.newHttpClient();
	        HttpRequest req=(HttpRequest) HttpRequest.newBuilder()
	        .setHeader("Content-Type","application/json")
	        .setHeader("x-access-token", token)
	        .uri(URI.create(URL))
	        .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
	        .build();        
			response= client.send(req,HttpResponse.BodyHandlers.ofString());
			return response;
		} catch (IOException | InterruptedException e) {
			if(intentos < 5) {
				Thread.sleep(2000);
				return postRequest(intentos+1,URL,jsonBody);
			}
	//		e.printStackTrace();
		}
    	return response;
    }
	
	private HttpResponse<String> putRequest(int intentos,String URL,JSONObject json) throws InterruptedException{
    	HttpResponse<String> response = null;
    	try {
	    	HttpClient client=HttpClient.newHttpClient();
	        HttpRequest req=(HttpRequest) HttpRequest.newBuilder()
	        .setHeader("Content-Type","application/json")
	        .setHeader("x-access-token", token)
	        .uri(URI.create(URL))
	        .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
	        .build();        
			response= client.send(req,HttpResponse.BodyHandlers.ofString());
			return response;
		} catch (IOException | InterruptedException e) {
			if(intentos < 5) {
				Thread.sleep(2000);
				return putRequest(intentos+1,URL,json);
			}
	//		e.printStackTrace();
		}
    	return response;
	}
	

    private boolean validateList(ListView<String> listSinoidales,int SIZE,String message) {
    	if(listSinoidales.getItems().size() < SIZE) {
    		Alert alert=lc.runAlert(AlertType.ERROR, "ERROR LISTA",message);
    		alert.show();
    		listSinoidales.requestFocus();
    	}else {
    		return true;
    	}
    	return false;
    }
    
    
    private boolean validateComboBox(ComboBox<String> cb,String message) {
    	if((cb.getSelectionModel().getSelectedItem()!=null)) {
    		return true;
    	}else {
    		Alert alert=lc.runAlert(AlertType.ERROR, "ERROR", "No haz seleccionado ninguna opcion de "+message);
    		alert.show();
    	}
    	return false;
    }
    
    private boolean validateTextField(TextField txt,String message,String regex,int maxlenght) {
    	String source=txt.getText();
    	if(maxlenght == 255) {
    		source = txt.getText().concat("\s");
    	}
    	if(source.length()>1 && source.matches(regex) && source.length() <= maxlenght ) {
    		return true;
    	}else{
    		Alert alert=lc.runAlert(AlertType.ERROR, "ERROR", "Datos no Validos\n"+message);
    		alert.show();
    		txt.clear();
    		txt.requestFocus();
    	}
    	return false;
    }
    
    private void setFormCeremonia() {
    	for(int i=1;i<=10;i++) {
    		cbEstantes.getItems().add(String.valueOf(i));
    	}
    	Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        LocalDate today = LocalDate.now();
                        setDisable(empty || item.compareTo(today) < 0);
                    }

                };
            }

        };
        datePickerCeremonia.setDayCellFactory(callB);
    	
       /* datePickerCeremonia.setConverter(new StringConverter<LocalDate>() {
        	String patt = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(patt);
			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;
		         }
				
			}
			
			@Override
			public String toString(LocalDate date) {
				if (date != null) {
		             return dateFormatter.format(date);
		         } else {
		             return "";
		         }
			}

        });*/
        
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
        		alert=lc.runAlert(AlertType.INFORMATION, "Eliminar Sinoidal", "Sinoidal eliminado correctamente");
        		alert.show();
        	}
    	}else {
    		alert = lc.runAlert(AlertType.ERROR,"Error Sinoidales","Selecciona un elemento de la lista para eliminar");
			alert.showAndWait();
    	}
    }
    
    private void addSelectSinoidales() {
    		SingleSelectionModel<String> id = cbSinoidales.getSelectionModel();
	    	if(id.getSelectedItem() != null ) {
	    		Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Asignar Sinoidal", "¿Seguro que quieres asignar este sinoidal a esta acta?");
	        	Optional<ButtonType> result = alert.showAndWait();
	        	if(result.get() == ButtonType.OK) {
	        		if(listSinoidales.getItems().size() < 3) {
		    			listSinoidales.getItems().add(id.getSelectedItem());
		    			cbSinoidales.getItems().remove(id.getSelectedIndex());
		    			btnEliminarSinoidal.setDisable(false);
		    		}else {
		    			alert = lc.runAlert(AlertType.ERROR,"Error Sinoidales","Ya seleccionaste los 3 sinoidales necesarios");
		    			alert.showAndWait();
		    		}
	        	}
	    		
	    	}else {
	    		alert = lc.runAlert(AlertType.ERROR,"Error Sinoidal","Selecciona un Sinoidal para asignarlo al Acta");
	    		alert.showAndWait();
	    	}
	    	
    }
    
    private void setComboboxActas() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	cbCarreras.getItems().addAll("IAS",
    			"IB",
    			"IA",
    			"IEA",
    			"IEC",
    			"IMT",
    			"IMA",
    			"IME",
    			"ITS","IMA","IME"
    			);
    	listSinoidales.getItems().clear();
    	cbFolders.getItems().clear();
    	cbCeremonias.getItems().clear();
    	cbSinoidales.getItems().clear();
    	//cbFolders.setPromptText("Selecciona un Folder");
    	//cbCeremonias.setPromptText("Selecciona la Ceremonia");
    	//cbSinoidales.setPromptText("Selecciona el sinoidal");
    	String sample;
    	List<Sinoidales> sinoidales=handleResponseSinoidales("http://127.0.0.1:4040/api/sinoidales");
    	List<Folder> folders = handleResponseFolder("http://127.0.0.1:4040/api/folders");
    	List<Ceremonias> ceremonias=handleResponseCeremony("http://127.0.0.1:4040/api/ceremonies");
    	for(Folder f : folders) {
    		cbFolders.getItems().add(f.getId_folder().toString());
    	}
    	
    	for(Sinoidales s: sinoidales) {
    		sample =s.getId_sinoidales()+"-"+s.getFirst_Name() + " " + s.getSecond_Name();
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
    		
    	}
    }
    
    
    private void fillTableActas() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	tableActas.getItems().clear();
    	List<Actas> actas=handleResponseActas("http://127.0.0.1:4040/api/certificates");
  		if(actas == null) {
  			lc.runAlert(AlertType.ERROR,"Error de Conexion","Revisa tu conexión");
  		}else {
  			for(Actas a:actas) {
  	  			a.setDate_limit_fk(splitData(a.getDate_limit_fk(),"T"));
  	  			tableActas.getItems().add(a);
  	  		}
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
    
    private void filltableFolder() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	tableFolders.getItems().clear();
    	List<Folder> folders = handleResponseFolder("http://127.0.0.1:4040/api/folders");
    	for(Folder f : folders) {
    		tableFolders.getItems().add(f);
    	}
    }
    
    protected List<Actas> handleResponseActas(String URL) throws JsonMappingException, JsonProcessingException, InterruptedException {
    	HttpResponse<String> response=getRequest(0,URL);
    	List<Actas> listActas=null;
    	if(response.statusCode()==201) {
        	ObjectMapper objectMapper = new ObjectMapper();
    		listActas = objectMapper.readValue(response.body(), new TypeReference<List<Actas>>(){});
    	}
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
    
    protected List<Actas_Sinoidales> handleResponseActasSinoidales(String URL) throws InterruptedException, JsonMappingException, JsonProcessingException{
    	HttpResponse<String> response=getRequest(0,URL);
    	List<Actas_Sinoidales> listActasSinoidales=null;
    	ObjectMapper objectMapper = new ObjectMapper();
		listActasSinoidales = objectMapper.readValue(response.body(), new TypeReference<List<Actas_Sinoidales>>(){});
        return listActasSinoidales;
    }
    
    protected List<Telephone> handleResponseTelephone(String URL) throws JsonMappingException, JsonProcessingException, InterruptedException{
    	HttpResponse<String> response = getRequest(0,URL);
    	List<Telephone> listTelephones = null;
    	ObjectMapper objectMapper = new ObjectMapper();
		listTelephones = objectMapper.readValue(response.body(), new TypeReference<List<Telephone>>(){});
        return listTelephones;
    }
    
    protected List<Email> handleResponseEmail(String URL) throws JsonMappingException, JsonProcessingException, InterruptedException{
    	HttpResponse<String> response = getRequest(0,URL);
    	List<Email> listEmails = null;
    	ObjectMapper objectMapper = new ObjectMapper();
		listEmails = objectMapper.readValue(response.body(), new TypeReference<List<Email>>(){});
        return listEmails;
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
    	
    	TableColumn  nameColumn= new TableColumn("Nombre");
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name_Student"));
    	nameColumn.setStyle( "-fx-alignment: center;");
    	nameColumn.setSortable(false);
    	nameColumn.setResizable(false);
    	nameColumn.setMinWidth(150);

    	TableColumn  lastNameColumn= new TableColumn("Apellidos");
    	lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName_Student"));
    	lastNameColumn.setStyle( "-fx-alignment: center;");
    	lastNameColumn.setSortable(false);
    	lastNameColumn.setResizable(false);
    	lastNameColumn.setMinWidth(150);
    	
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
    	idFolderColumn.setMinWidth(30);
    	
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
    	dateLimitColumn.setMinWidth(150);
    	
    	TableColumn  ceremonyColumn= new TableColumn("id Ceremonia");
    	ceremonyColumn.setCellValueFactory(new PropertyValueFactory<>("id_ceremony_fk"));
    	ceremonyColumn.setStyle( "-fx-alignment: center;");
    	ceremonyColumn.setSortable(false);
    	ceremonyColumn.setResizable(false);


    	
    	tableActas.getColumns().addAll(idActaColumn,nameColumn,lastNameColumn,idStudentColumn,idFolderColumn,dateLimitColumn,ceremonyColumn,signaturesColumn);
    	
    	tableActas.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
    		if(e.getClickCount() == 2) {
    			Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Crear Acta", "¿Seguro que quieres visualizar esta acta?");
            	Optional<ButtonType> result = alert.showAndWait();
            	
            	if (result.get() == ButtonType.OK){
            		Actas selectedItem = tableActas.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                    	actasPane.toFront();
                    	txtNameStudent.setText(selectedItem.getName_Student());
                    	txtApellidosStudent.setText(selectedItem.getLastName_Student());
                    	txtIdStudent.setText(selectedItem.getId_Student().toString());
                    	txtPlanStudent.setText(String.valueOf(selectedItem.getDegree_plan()));
                    	
                    	id_Acta = selectedItem.getId_actas();
                    	
                    	txtNameStudent.setDisable(true);
                    	txtApellidosStudent.setDisable(true);
                    	txtIdStudent.setDisable(true);
                    	txtPlanStudent.setDisable(true);
                    	btnEliminarSinoidal.setDisable(false);
                    	try {
							setComboboxActas();
							List<Actas_Sinoidales> actas_sinoidales=handleResponseActasSinoidales("http://127.0.0.1:4040/api/certificates/actasSinoidales/"+selectedItem.getId_actas());
					  		if(actas_sinoidales == null) {
					  			lc.runAlert(AlertType.ERROR,"Error de Conexion","Revisa tu conexión");
					  		}else {
					  			for(Actas_Sinoidales as:actas_sinoidales) {
					  				List<Sinoidales> sin = handleResponseSinoidales("http://127.0.0.1:4040/api/sinoidales/search/"+as.getId_sinoidales_fk());
					  				for(Sinoidales i : sin) {
					  					listSinoidales.getItems().add(as.getId_sinoidales_fk()+"-"+i.getFirst_Name()+" "+i.getSecond_Name());
					  					cbSinoidales.getItems().remove(as.getId_sinoidales_fk()+"-"+i.getFirst_Name()+" "+i.getSecond_Name());
					  				}
					  			
					  	  		}
					  		}
						} catch (JsonProcessingException | InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    	
                    	cbFolders.getSelectionModel().select(String.valueOf(selectedItem.getId_Folder_fk()));
                    	cbCeremonias.getSelectionModel().select(selectedItem.getId_ceremony_fk().toString());
                    	cbCarreras.getSelectionModel().select(selectedItem.getDegree());
                    	cbCarreras.setDisable(true);
                    	btnActualizarActa.setVisible(true);
                    	btnEliminarActa.setVisible(true);
                    	btnCrearActa.setVisible(false);
                    }else{
                    	alert=lc.runAlert(AlertType.CONFIRMATION, "Selecciona un Registro", "Selecciona un registro para ver su información.");
                		alert.show();
                    }
            	}
    			
    		}
    	});
    
    }
	
    
   
    
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setTableViewSinoidales() {
		TableColumn idSinoidal = new TableColumn("Id");
		idSinoidal.setCellValueFactory(new PropertyValueFactory<>("id_sinoidales"));
		idSinoidal.setStyle( "-fx-alignment: center;");
		idSinoidal.setSortable(false);
		idSinoidal.setResizable(false);
		idSinoidal.setMinWidth(50);
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

    	
    	/*TableColumn  emailColumn= new TableColumn("Email");
    	emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    	emailColumn.setStyle( "-fx-alignment: center;");
    	emailColumn.setSortable(false);
    	emailColumn.setResizable(false);
    	emailColumn.setMinWidth(200);*/
    	
    	TableColumn  areaColumn= new TableColumn("Area");
    	areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
    	areaColumn.setStyle( "-fx-alignment: center;");
    	areaColumn.setSortable(false);
    	areaColumn.setResizable(false);
    	areaColumn.setMinWidth(150);

    	
    	/*TableColumn  telefonoColumn= new TableColumn("Telefono");
    	telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
    	telefonoColumn.setStyle( "-fx-alignment: center;");
    	telefonoColumn.setSortable(false);
    	telefonoColumn.setResizable(false);
    	telefonoColumn.setMinWidth(80);*/
    	
    	TableColumn  disponibilityColumn= new TableColumn("Disponibilidad");
    	disponibilityColumn.setCellValueFactory(new PropertyValueFactory<>("disponibility"));
    	disponibilityColumn.setStyle( "-fx-alignment: center;");
    	disponibilityColumn.setSortable(false);
    	disponibilityColumn.setResizable(false);
    	disponibilityColumn.setMaxWidth(50);
    	
    	TableColumn  activeColumn= new TableColumn("");
    	activeColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));
    	activeColumn.setStyle( "-fx-alignment: center;");
    	activeColumn.setSortable(false);
    	activeColumn.setResizable(false);
    	activeColumn.setMaxWidth(47);
    	
    	tableSinoidales.getColumns().addAll(idSinoidal,FirstName,secondName,idProfessor,areaColumn,disponibilityColumn,activeColumn);
    
    	tableSinoidales.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
    		if(e.getClickCount() == 2) {
    			Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Crear Acta", "¿Seguro que quieres visualizar este sinoidal?");
            	Optional<ButtonType> result = alert.showAndWait();
            	
            	if (result.get() == ButtonType.OK){
            		Sinoidales selectedItem = tableSinoidales.getSelectionModel().getSelectedItem();
                    if (!selectedItem.equals(null)) {
                    	listTelefonos.getItems().clear();
                    	listCorreos.getItems().clear();
                    	addSinoidales.toFront();
                    	txtNombreSinoidal.setText(selectedItem.getFirst_Name());
                    	txtApellidosSinoidal.setText(selectedItem.getSecond_Name());
                    	txtIdSinoidal.setText(selectedItem.getId_professor().toString());
                    	//txtEmailSinoidal.setText(selectedItem.getEmail());
                    	//txtTelefonoSinoidal.setText(selectedItem.getTelephone());
                    	txtCoordinacionSinoidal.setText(selectedItem.getArea());  
                    	btnCrearSinoidal.setVisible(false);
                    	btnActualizarSinoidal.setVisible(true);
                    	btnEliminarSinoidales.setVisible(true);
                    	txtNombreSinoidal.setDisable(true);
                    	txtApellidosSinoidal.setDisable(true);
                    	txtIdSinoidal.setDisable(true);
                    	txtCoordinacionSinoidal.setDisable(true);
                    	btnEliminarTelefono.setDisable(false);
                    	btnEliminarEmail.setDisable(false);
                    	//Todo make the scheme for telephones and emails
                    	System.out.println(selectedItem.getId_sinoidales());

						try {
							List<Telephone> telephones = handleResponseTelephone("http://127.0.0.1:4040/api/sinoidales/phones/"+selectedItem.getId_sinoidales());
	                    	for(Telephone t : telephones) {
	                    		listTelefonos.getItems().add(t.getTelephone());
	                    		
	                    	}
	                    	List<Email>emails = handleResponseEmail("http://127.0.0.1:4040/api/sinoidales/emails/"+selectedItem.getId_sinoidales());
							
							for(Email em : emails) {
	                    		listCorreos.getItems().add(em.getEmail());
	                    		
	                    	}
						} catch (JsonProcessingException | InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							
						}
                    	
                    	
                    }
            	}
    			
    		}
    	});
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
    	cicleColumn.setMinWidth(144);

  
    	tableCeremonias.getColumns().addAll(idCeremony,dateLimit,cicleColumn);
    }

	@SuppressWarnings("unchecked")
	public void setTableViewFolders() {
		@SuppressWarnings("rawtypes")
		TableColumn idFolder = new TableColumn("Id");
		idFolder.setCellValueFactory(new PropertyValueFactory<>("id_folder"));
		idFolder.setStyle( "-fx-alignment: center;");
		idFolder.setSortable(false);
		idFolder.setResizable(false);
		idFolder.setMinWidth(100);
		@SuppressWarnings("rawtypes")
		TableColumn  caseColumn= new TableColumn("Estante");
		caseColumn.setCellValueFactory(new PropertyValueFactory<>("id_case"));
		caseColumn.setStyle( "-fx-alignment: center;");
		caseColumn.setSortable(false);
		caseColumn.setResizable(false);
		caseColumn.setMinWidth(164);
		
		TableColumn  actasNumColumn= new TableColumn("Numero de Actas");
		actasNumColumn.setCellValueFactory(new PropertyValueFactory<>("actas_num"));
		actasNumColumn.setStyle( "-fx-alignment: center;");
		actasNumColumn.setSortable(false);
		actasNumColumn.setResizable(false);
		actasNumColumn.setMinWidth(140);
		
		tableFolders.getColumns().addAll(idFolder,caseColumn,actasNumColumn);
		
	}
}
