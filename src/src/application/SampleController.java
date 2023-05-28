package application;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;

import javafx.scene.control.Label;

import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Year;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
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
	private BigInteger id_Sinoidal=null;
	public String user;
	public String action;
	
	private static final String domain = "http://127.0.0.1:4040/";
	
	@FXML
	private VBox vContainerSigns;
	
	@FXML
	private HBox hStatus,hActasFirmadas;
	
	@FXML
	private Label titleActa,lblStatus,lblNumberActas,titleSinodal;
	
	@FXML
    private Button btnCloseWindow,btnActualizarSinoidal,btnEliminarSinoidales,btnAgregarTelefono,btnAgregarEmail,btnEliminarTelefono,btnEliminarEmail,btnDeleteSinoidal,btnEliminarFirmaSinoidal;
	
	@FXML
    private Button btnMinizeWindow,btnAgregarSinoidales,btnCrearSinoidal,btnCrearFolder,btnAgregarFirmaSinoidal;
	
	@FXML
	private ComboBox<String> cbSinoidales,cbFolders,cbCeremonias,cbCarreras,cbCiclo,cbEstantes,cbFilterFolder,cbFilterCeremonia,cbFilterAreaSinodal,cbFilterSinodal;
	
	@FXML
	private ComboBox<Circle> cbFilterEstatus;
	
	@FXML
	private DatePicker datePickerCeremonia;
	
	@FXML
	private Button btnCeremonia,btnCrearActa,btnAsignarSinoidal,btnEliminarSinoidal,btnCrearCeremonia,btnModificarCeremonia,btnEliminarCeremonia;
    
	@FXML
	private Button btnFolder,btnModificarFolder,btnEliminarFolder;
	
	@FXML
	private Button btnConfiguracion;
	
	@FXML
	private Button btnLogout;
	
	@FXML
    private Button btnInicio;

    @FXML
    private Button btnActas,btnActualizarActa,btnEliminarActa,btnReestablecerSinodales;

    @FXML
    private Button btnSinoidales;
    
    @FXML
    private Button btnAgregarActa;
    
    @FXML
    private Button btnAgregarSinoidal;
    
    @FXML
    private Button btnBuscar,btnReestablecer;
    
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
    private ListView<String> listSinoidales,listTelefonos,listCorreos,listFirmas;
    
    LoginController lc=new LoginController();
    
    @FXML
    private TextField txtNameStudent,txtIdStudent,txtPlanStudent,txtApellidosStudent,txtSearch,txtSearchSinodal;
    @FXML
    private TextField txtNombreSinoidal,txtApellidosSinoidal,txtIdSinoidal,txtEmailSinoidal,txtTelefonoSinoidal,txtCoordinacionSinoidal;
	@Override
    public void initialize(URL location, ResourceBundle resources) {
    	setUser();
    	LoginController swe=new LoginController();
    	token=swe.getoken();
    	setTableViewActas();
    	setTableViewSinoidales();
    	setTableViewCeremony();
    	setTableViewFolders();
    	try {
	    	setFilterCombobox();
			fillTableActas();
			fillTableSinoidales();
		} catch (JsonProcessingException | InterruptedException e) {
			e.printStackTrace();
		}
    	
    	
    	cbFilterFolder.getSelectionModel().selectedItemProperty().addListener((options,oldValue,newValue)->{
    		if(newValue != null) {
    			try {
    				fillTableActas();
    			} catch (JsonProcessingException | InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            		ObservableList<Actas> filteredList = FXCollections.observableArrayList();
            		for(Actas ac : tableActas.getItems()) {
            			if(String.valueOf(ac.getId_Folder_fk()).contains(newValue)) {
            				filteredList.add(ac);
            			}
            		}
            		tableActas.setItems(filteredList);
    		}
    		
    	});
    	
    	cbFilterCeremonia.getSelectionModel().selectedItemProperty().addListener((options,oldValue,newValue)->{
    		
    		if(newValue != null) {
    			try {
    				fillTableActas();
    			} catch (JsonProcessingException | InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}	
        			ObservableList<Actas> filteredList = FXCollections.observableArrayList();
            		for(Actas ac : tableActas.getItems()) {
            			if(String.valueOf(ac.getId_ceremony_fk()).contains(newValue)) {
            				filteredList.add(ac);
            			}
            		}
            		tableActas.setItems(filteredList);
    		}
    		
    	});
    	
    	cbFilterEstatus.getSelectionModel().selectedIndexProperty().addListener((options,oldValue,newValue)->{
    		if(newValue != null) {
    			try {
    				fillTableActas();
    			} catch (JsonProcessingException | InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}	
        			ObservableList<Actas> filteredList = FXCollections.observableArrayList();
            		for(Actas ac : tableActas.getItems()) {
            			if(newValue.intValue()==ac.getSignatures()) {
            				filteredList.add(ac);
            			}
            		}
            		tableActas.setItems(filteredList);
    		}
    	});
    	
    	cbFilterAreaSinodal.getSelectionModel().selectedItemProperty().addListener((options,oldValue,newValue)->{
    		if(newValue != null) {
    			try {
    				fillTableSinoidales();
    				ObservableList<Sinoidales> filteredList = FXCollections.observableArrayList();
            		for(Sinoidales ac : tableSinoidales.getItems()) {
            			if(ac.getArea().contains(newValue)) {
            				filteredList.add(ac);
            			}
            		}
            		tableSinoidales.setItems(filteredList);
    			} catch (JsonProcessingException | InterruptedException e) {
    				// TODO Auto-generated catch block
    				//e.printStackTrace();
    			}	
    		}
    	});
    	
    	cbFilterSinodal.getSelectionModel().selectedItemProperty().addListener((options,oldValue,newValue)->{
    		if(newValue != null) {
    			if(newValue != "Sinodal") {
    				String id[] = newValue.split("-");
        			try {
        				tableActas.getItems().clear();
        				List<Actas_Sinoidales> response = handleResponseActasSinoidales(domain+"api/certificates/actasSinoidales/sinoidales/"+id[0]);
    					if(response!=null) {
    						for(Actas_Sinoidales as:response) {
    							List<Actas> res = handleResponseActas(domain+"api/certificates/acta/"+as.getId_actas_fk());
    							tableActas.getItems().addAll(res);
    						}
    					}
    				} catch (InterruptedException | JsonProcessingException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    			
    		}
    	});    	
    	
    }

    protected String splitData(String date,String delimeter) {
    	String[] data;
    	data=date.split(delimeter);
    	return String.valueOf(data[0]);
    }
    
    
    public void setFilterCombobox() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	cbFilterFolder.getItems().clear();
    	cbFilterCeremonia.getItems().clear();
    	cbFilterEstatus.getItems().clear();
    	cbFilterSinodal.getItems().clear();
    	
    	cbFilterFolder.getItems().add("Folder");
    	cbFilterCeremonia.getItems().add("Ceremonia");
    	//cbFilterEstatus.getItems().add("Estatus");
    	cbFilterSinodal.getItems().add("Sinodal");
    	
    	List<Folder> folders = handleResponseFolder(domain+"api/folders");
    	List<Ceremonias> ceremonias=handleResponseCeremony(domain+"api/ceremonies");
    	List<Sinoidales> sinoidales=handleResponseSinoidales(domain+"api/sinoidales");

    
    	for(Folder f : folders) {
    		cbFilterFolder.getItems().add(f.getId_folder().toString());
    	}
    	for(Ceremonias c: ceremonias) {
    		cbFilterCeremonia.getItems().add(c.getId_ceremony().toString());
    	}
    	
    	for(Sinoidales s: sinoidales) {
    		String sample = s.getId_sinoidales()+"-"+s.getFirst_Name() + " " + s.getSecond_Name();
    		cbFilterSinodal.getItems().add(sample);
    	}
    	
    	Circle c0 = new Circle(5);
    	c0.setFill(Color.RED);
    	Circle c1 = new Circle(5);
    	c1.setFill(Color.ORANGE);
    	Circle c2 = new Circle(5);
    	c2.setFill(Color.YELLOW);
    	Circle c3 = new Circle(5);
    	c3.setFill(Color.GREEN);
    	cbFilterEstatus.getItems().addAll(c0,c1,c2,c3);
    	
    	cbFilterFolder.getSelectionModel().select(0);
    	cbFilterCeremonia.getSelectionModel().select(0);
    	cbFilterEstatus.getSelectionModel().select(0);
    	cbFilterSinodal.getSelectionModel().select(0);
    }
    
    public void handleClicks(ActionEvent actionEvent) throws InterruptedException, IOException {
      if (actionEvent.getSource() == btnInicio) {
    	  	try {
    	  		
    	    	setFilterCombobox();
				fillTableActas();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
            inicioPane.toFront();
        }
        if (actionEvent.getSource() == btnActas || actionEvent.getSource() == btnAgregarActa) {
        	titleActa.setText("CREAR ACTA");
            actasPane.toFront();
            try {
            	clearFormActas();
			} catch (JsonProcessingException | InterruptedException e) {
				e.printStackTrace();
			}
        }
        if (actionEvent.getSource() == btnSinoidales) {
        	try {
				fillTableSinoidales();
				 setFilterComboboxSinodal();
			} catch (JsonProcessingException e) {
				
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
        
        if(actionEvent.getSource() == btnDeleteSinoidal) {
        	//deleteSinoidal();
        }
        
        if(actionEvent.getSource() == btnActualizarSinoidal) {
        	updateSinoidal();
        }
        
        if(actionEvent.getSource() == btnAgregarFirmaSinoidal) {
        	asignFirma();
        }
        if(actionEvent.getSource() == btnEliminarFirmaSinoidal) {
        	//deleteSelected(listFirmas,"FIRMA ELIMINADA","",btnEliminarFirmaSinoidal);
        	deleteSign();
        }
        
        if(actionEvent.getSource() == btnBuscar) {

        }
        
        if(actionEvent.getSource() == btnReestablecer) {
        	cbFilterFolder.getSelectionModel().select(0);
        	cbFilterCeremonia.getSelectionModel().select(0);
        	cbFilterEstatus.getSelectionModel().select(0);
        	cbFilterSinodal.getSelectionModel().select(0);
        	fillTableActas();
        }
        
        if(actionEvent.getSource() == btnReestablecerSinodales) {
        	cbFilterAreaSinodal.getSelectionModel().select(0);
        	fillTableSinoidales();
        }
        
        if(actionEvent.getSource() == btnModificarFolder) {
        	updateFolder();
        }
        
        if(actionEvent.getSource() == btnEliminarFolder) {
        	deleteFolder();
        }
        
        if(actionEvent.getSource() == btnEliminarCeremonia) {
        	deleteCeremonia();
        }
        
    }
    
    public void deleteCeremonia() {
    	Ceremonias c = tableCeremonias.getSelectionModel().getSelectedItem();
    	
    }
    
    public void deleteFolder() throws IOException, InterruptedException {
    	Folder f = tableFolders.getSelectionModel().getSelectedItem();
    	if(f.getActas_num() == 0) {
    		Dialog<String> validate = getValidateModal();
        	Optional<String> result = validate.showAndWait();
        	if(result.get().matches("auth")) {
        		HttpResponse<String> res = deleteRequest(0,domain+"api/folders/"+f.getId_folder());
        		if(res.statusCode() == 201) {
        			setFormCeremonia();
        			filltableFolder();
        			alert=lc.runAlert(AlertType.INFORMATION, "Folder Eliminado", "Folder eliminado correctamente.");
        			alert.show();
        		}else {
        			alert=lc.runAlert(AlertType.ERROR, "ERROR FOLDER", "Hubo un error al momento de eliminar la información\nError:"+res.statusCode());
        			alert.show();
        		}
        	}else if(result.get().matches("no auth")) {
        		alert=lc.runAlert(AlertType.INFORMATION, "NO VALIDADO", "El movimiento no fue validado, verifica tu contraseña.");
    			alert.show();
        	}
    	}else{
    		alert=lc.runAlert(AlertType.INFORMATION, "ERROR FOLDER", "No se puede eliminar el folder ya que tiene actas asignadas.");
			alert.show();
    	}
    	
    	
    	
    }
    

    public void updateFolder() throws IOException, InterruptedException {
    	if(validateComboBox(cbEstantes,"Estantes")) {
    		Folder f = tableFolders.getSelectionModel().getSelectedItem();
        	System.out.println("The id: "+f.getId_folder()+" the new case: "+ cbEstantes.getValue());
        	JSONObject j = new JSONObject();
        	Dialog<String> validate = getValidateModal();
        	Optional<String> result = validate.showAndWait();
        	if(result.get().matches("auth")) {
        		HttpResponse<String> res = putRequest(0,domain+"api/folders/"+f.getId_folder()+"/"+cbEstantes.getValue(),j);
        		if(res.statusCode() == 201) {
        			setFormCeremonia();
        			filltableFolder();
        			alert=lc.runAlert(AlertType.INFORMATION, "Folder Actualizado", "Folder Actualizado con exito.");
        			alert.show();
        		}else {
        			alert=lc.runAlert(AlertType.ERROR, "ERROR FOLDER", "Hubo un error al momento de actualizar la información\nError:"+res.statusCode());
        			alert.show();
        		}
        	}else if(result.get().matches("no auth")) {
        		alert=lc.runAlert(AlertType.INFORMATION, "NO VALIDADO", "El movimiento no fue validado, verifica tu contraseña.");
    			alert.show();
        	}
        	
    	}
    }
    
    
    
    
    public void searchSinodalInfo() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	String filter = txtSearchSinodal.getText();
    	if(filter == null || filter.length() == 0 || filter.isBlank()) {
    		fillTableSinoidales();
    	}else {
    		ObservableList<Sinoidales> filteredList = FXCollections.observableArrayList();
    		for(Sinoidales ac : tableSinoidales.getItems()) {
    			if(ac.getFirst_Name().toLowerCase().contains(filter.toLowerCase()) ||
    					ac.getSecond_Name().toLowerCase().contains(filter.toLowerCase()) || ac.getId_professor().toString().contains(filter.toLowerCase())
    					|| ac.getId_sinoidales().toString().contains(filter.toLowerCase())) {
    				filteredList.add(ac);
    			}
    		}
    		tableSinoidales.setItems(filteredList);
    	}
    }
    

    public void searchNamesActas() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	String filter = txtSearch.getText();
    	if(filter == null || filter.length() == 0 || filter.isBlank()) {
    		fillTableActas();
    	}else {
    		ObservableList<Actas> filteredList = FXCollections.observableArrayList();
    		for(Actas ac : tableActas.getItems()) {
    			if(ac.getName_Student().toLowerCase().contains(filter.toLowerCase()) ||
    					ac.getLastName_Student().toLowerCase().contains(filter.toLowerCase())) {
    				filteredList.add(ac);
    			}
    		}
    		tableActas.setItems(filteredList);
    	}

   }

    
    
    private void asignFirma() {
    	MultipleSelectionModel<String>select = listSinoidales.getSelectionModel();
    	if(select.getSelectedItem() != null) {
    		Alert alert=lc.runAlert(AlertType.CONFIRMATION, "FIRMA SINODAL","¿Seguro que ya firmo el sinodal "+select.getSelectedItem()+" ?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.get() == ButtonType.OK){
        		String sinoidal = select.getSelectedItem();
        		listFirmas.getItems().add(sinoidal);
    			listSinoidales.getItems().remove(select.getSelectedIndex());
        		if(listFirmas.getItems().size()==3) {
        			btnEliminarSinoidal.setDisable(true);
        			btnAsignarSinoidal.setDisable(true);
        			cbSinoidales.setDisable(true);
        		}else {
        			btnEliminarFirmaSinoidal.setDisable(false);
        			alert=lc.runAlert(AlertType.INFORMATION, "Acta Firma", "Se agrego una firma al acta correctamente.\nDa clic en Actualizar para guardar los cambios.");
        			alert.show();
        			btnActualizarActa.setFocusTraversable(true);
        		}
        		
        	}
    	}else {
    		alert = lc.runAlert(AlertType.ERROR,"Error ","Selecciona un elemento de la lista para eliminar");
			alert.showAndWait();
    	}
    }
    
    private void deleteSign() {
    	MultipleSelectionModel<String> selected = listFirmas.getSelectionModel();
    	if(selected.getSelectedItem() != null) {
    		Alert alert=lc.runAlert(AlertType.CONFIRMATION, "FIRMA ELIMINADA","¿Seguro que quieres eliminar la firma del sinodal "+selected.getSelectedItem()+" ?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.get() == ButtonType.OK){
        		listSinoidales.getItems().add(selected.getSelectedItem());
        		listFirmas.getItems().remove(selected.getSelectedIndex());
        		btnAsignarSinoidal.setDisable(false);
        		btnEliminarSinoidal.setDisable(false);
        		cbSinoidales.setDisable(false);
        		if(listFirmas.getItems().size() == 0 ) {
        			btnEliminarFirmaSinoidal.setDisable(true);
        		}
        		alert=lc.runAlert(AlertType.INFORMATION, "FIRMA ELIMINADA", "Firma eliminada correctamente.");
        		alert.show();
        	}
    	}else {
    		alert = lc.runAlert(AlertType.ERROR,"Error ","Selecciona un elemento de la lista para eliminar");
			alert.showAndWait();
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
		if(validateTextField(txtEmailSinoidal,"El valor del campo email es nulo o no cuenta con el formato indicado.","[a-zA-Z0-9_!#$%&'*+/=?``{|}~^.]+@[a-zA-Z0-9.]+$",255)){
			Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Agregar Email", "¿Seguro que quieres asignar este email a este sinodal?");
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
    		Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Agregar Telefono", "¿Seguro que quieres asignar este telefono a este sinodal?");
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
    
    private Dialog<String> getValidateModal( ) throws IOException {
    	Dialog<String> dialog = new Dialog<>();
    	dialog.setTitle("Validar Movimiento");
    	dialog.setHeaderText("Ingresa tu contraseña para validar la modificación en el sistema.");
    	dialog.setResizable(false);
    	dialog.initModality(Modality.APPLICATION_MODAL); 
    	dialog.initStyle(StageStyle.UNDECORATED);
    	dialog.getDialogPane().setPrefSize(400, 200);
    	PasswordField text1 = new PasswordField();
    	text1.setPrefWidth(150);
    	VBox grid = new VBox();
    	grid.getChildren().add(text1);
    	dialog.getDialogPane().setContent(grid);
    	         
    	ButtonType buttonTypeOk = new ButtonType("Validar", ButtonData.OK_DONE);
    	ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonData.NO);
    	
    	//Styling the dialogpane
    	dialog.getDialogPane().getStylesheets().add(getClass().getResource("dialogStyle.css").toExternalForm());;
    	dialog.getDialogPane().getStyleClass().add("myDialog");
    	
    	dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk,buttonTypeCancel);    	
    	
    	dialog.setResultConverter(new Callback<ButtonType, String>() {
    	    @Override
    	    public String call(ButtonType b) {
    	 
    	        if (b == buttonTypeOk) {
    	        	try {
    	        		JSONObject json = new JSONObject();
        	        	json.put("user", user);
        	        	json.put("pass", text1.getText());
						HttpResponse<String> resp = postRequest(0,domain+"auth/signIn/validate",json);
						if(resp.statusCode()==201) {
							return "auth";
						}else {
							return "no auth";
						}
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
    	        }
    	        
    	        if(b==buttonTypeCancel) {
    	        	return "cancel";
    	        }
    	        
    	        return "null";
    	    }
    	});
    	
    	
    	
    	return dialog;
    }
    
    
   //Check update acta 
   private void updateActa( ) throws InterruptedException, IOException {
    	int i,j;     	
    	if(validateListSinodales("No se han asignado todos los 3 sinodales o firmas necesariass para crear el Acta")) {
    		Dialog<String> validate = getValidateModal();
        	Optional<String> result = validate.showAndWait();
        	if(result.get().matches("auth")) {
        		JSONObject json = new JSONObject();
        		json.put("idCeremony", Integer.parseInt(cbCeremonias.getValue()));
        		json.put("idFolder", Integer.parseInt(cbFolders.getValue()));
        		HttpResponse<String> rps = putRequest(0,domain+"api/certificates/updateActa/"+id_Acta,json);
        		if(rps == null || rps.statusCode()!=201) {
        			alert=lc.runAlert(AlertType.ERROR, "ERROR Acta", "Hubo un error al momento de actualizar el acta, verifica tu conexión a internet.\nStatus: "+rps.statusCode());
            		alert.show();
        		}else {
        			i=1;	
        			for(String ids:listFirmas.getItems()) {
        				String id[] = ids.split("-");
        				rps = putRequest(0,domain+"api/certificates/updateActasSignatures/"+id_Acta+"/"+id[0],json);
        				if(rps == null || rps.statusCode()!=201) {
        					alert=lc.runAlert(AlertType.ERROR, "ERROR Acta", "Hubo un error al momento de actualizar las firmas del acta, verifica tu conexión a internet.\nStatus: "+rps.statusCode());
                    		alert.show();
        				}
        			}
        			
        			if(listSinoidales.getItems().size() != 0) {
        				rps = putRequest(0,domain+"api/certificates/updateActa/addSignature/"+id_Acta+"/"+listFirmas.getItems().size(),json);
    					if(rps==null || rps.statusCode()!=201) {
    						alert=lc.runAlert(AlertType.ERROR, "ERROR Acta", "Hubo un error al momento de actualizar las firmas del acta, verifica tu conexión a internet.\nStatus: "+rps.statusCode());
                    		alert.show();
    					}else {
    						rps = deleteRequest(0,domain+"api/certificates/actasSinoidales/delete/"+id_Acta);
        					j=1;
                			if(rps == null || rps.statusCode()!=201) {
                				alert=lc.runAlert(AlertType.ERROR, "ERROR Acta", "Hubo un error al momento de actualizar el acta, verifica tu conexión a internet.\nStatus: "+rps.statusCode());
                        		alert.show();
                			}else {	
                				for(String s:listSinoidales.getItems()) {
                					String x[] =s.split("-");
                        			rps = postRequest(0,domain+"api/certificates/addSinoidales/"+id_Acta+"/"+cbCeremonias.getValue()+"/"+x[0],json);
                        			if(rps == null || rps.statusCode()!=201) {
                        				alert=lc.runAlert(AlertType.ERROR, "ERROR Acta", "Hubo un error al momento de actualizar el acta, verifica tu conexión a internet.\nStatus: "+rps.statusCode());
                                		alert.show();
                        			}else if(j>=listSinoidales.getItems().size()) {
                        				inicioPane.toFront();
                        				fillTableActas();
                        				alert=lc.runAlert(AlertType.INFORMATION, "Acta Actualizada", "El acta fue actualizada correctamente");
                                		alert.show();
                        			}
                        			j++;
                				}
                			}
    					}
        			}
        				i++;
        			}
        	}else if(result.get().matches("no auth")) {
        		alert=lc.runAlert(AlertType.INFORMATION, "NO VALIDADO", "El movimiento no fue validado, verifica tu contraseña.");
    			alert.show();
        	}
    	}
    	
    }
    
    private void updateSinoidal() throws IOException, InterruptedException {
    	HttpResponse<String> res = null;
    	JSONObject js = new JSONObject();
    	if(validateList(listTelefonos,1,"Ingresa al menos un telefono para asignarlo al sinodal") &&
    			validateList(listCorreos,1,"Ingresa al menos un email para asignarlo al sinodal")) {
    		Dialog<String> d = getValidateModal();
    		Optional<String> result = d.showAndWait();
    		
        	if(result.get().matches("auth")) {
        			res = deleteRequest(0,domain+"api/sinoidales/phones/delete/"+id_Sinoidal);
        			if(res == null || res.statusCode()!=201) {
        				alert=lc.runAlert(AlertType.ERROR, "ERROR Sinodal", "Hubo un error al momento de actualizar el sinodal, verifica tu conexión a internet.\nStatus: "+res.statusCode());
                		alert.show();
        			}else {
        				res = deleteRequest(0,domain+"api/sinoidales/emails/delete/"+id_Sinoidal);
        				if(res == null || res.statusCode()!=201) {
            				alert=lc.runAlert(AlertType.ERROR, "ERROR Sinodal", "Hubo un error al momento de actualizar el sinodal, verifica tu conexión a internet.\nStatus: "+res.statusCode());
                    		alert.show();
            			}else {
            				for(String src: listTelefonos.getItems()) {
    	        				js.clear();
    	        				js.put("phone", src);
    	        				res = postRequest(0,domain+"api/sinoidales/addPhone/"+id_Sinoidal,js);
    	        				if(res == null || res.statusCode() != 201) {
    	        					alert=lc.runAlert(AlertType.ERROR, "ERROR SINOIDAL", "Hubo un error al momento de crear el sinodal, verifica tu conexión a internet.\nStatus: "+res.statusCode());
    	    	            		alert.show();
    	        				}
    	        			}
    	        			
    	        			for(String src:listCorreos.getItems()) {
    	        				js.clear();
    	        				js.put("email", src);
    	        				res = postRequest(0,domain+"api/sinoidales/addEmail/"+id_Sinoidal,js);
    	        				if(res == null || res.statusCode() != 201) {
    	        					alert=lc.runAlert(AlertType.ERROR, "ERROR SINODAL", "Hubo un error al momento de crear el sinodal, verifica tu conexión a internet.\nStatus: "+res.statusCode());
    	    	            		alert.show();
    	        				}
    	        			}

    	        			fillTableSinoidales();
    	        			sinoidalesPane.toFront();
    	        			alert=lc.runAlert(AlertType.INFORMATION, "SINODAL ACTUALIZADO", "El Sinodal ha sido actualizado exitosamente.");
    	            		alert.show();

        			}
        			
        		}
        		
        		
        	}else if(result.get().matches("no auth")) {
        		alert=lc.runAlert(AlertType.INFORMATION, "NO VALIDADO", "El movimiento no fue validado, verifica tu contraseña.");
    			alert.show();
        	}
    	}
    }
    private void createFolder() throws InterruptedException, IOException {
    	if(validateComboBox(cbEstantes,"Estantes")) {
    		Dialog<String> di = getValidateModal();
    		Optional<String> result = di.showAndWait();
        	if(result.get().matches("auth")) {
        		JSONObject j = new JSONObject();
        		j.put("case",cbEstantes.getValue());
        		HttpResponse<String> res = postRequest(0,domain+"api/folders",j);
        		if(res == null||res.statusCode() !=201) {
        			alert=lc.runAlert(AlertType.ERROR, "ERROR Folder", "Hubo un error al momento de crear el Folder, verifica tu conexión a internet.\nStatus: "+res.statusCode());
            		alert.show();
        		}else {
        			filltableFolder();
        			alert=lc.runAlert(AlertType.INFORMATION, "Folder CREADO", "El nuevo Folder ha sido creado exitosamente.");
            		alert.show();
        		}
        	}else if (result.get().matches("no auth")){
        		alert=lc.runAlert(AlertType.INFORMATION, "NO VALIDADO", "El movimiento no fue validado, verifica tu contraseña.");
    			alert.show();
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
    
    private boolean checkCeremoniasList(String date) {
    	for(Ceremonias c: tableCeremonias.getItems()) {
    		if(c.getDate().contains(date)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    private void createActa() throws InterruptedException, IOException{
    	if(validateTextField(txtNameStudent,"El valor del campo nombre es nulo o numerico","[a-zA-Z\u00f1\u00d1\s]+",255) && 
    			validateTextField(txtApellidosStudent,"El valor del campo apellidos es nulo o numerico","[a-zA-Z\u00f1\u00d1\s]+",255) &&
    			validateTextField(txtIdStudent,"El valor del campo matricula es nulo o contiene letras","[0-9]+",10) && 
    			validateTextField(txtPlanStudent,"El valor del campo plan de estudios es nulo o contiene letras","[0-9]+",3) &&
    			validateComboBox(cbCeremonias,"Ceremonias") &&
    			validateComboBox(cbFolders,"Folder") &&
    			validateComboBox(cbCarreras,"carreras") &&
    			validateList(listSinoidales,3,"No se han asignado todos los 3 sinodales necesarios para crear el Acta")) {
    		if(!checkActasList(txtIdStudent.getText())) {
    			/*Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Crear Acta", "¿Seguro que quieres crear esta acta?");
            	Optional<ButtonType> result = alert.showAndWait();
            	*/
    			Dialog<String> validate = getValidateModal();
            	Optional<String> result = validate.showAndWait();
    			
            	if (result.get().matches("auth")){
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
            		HttpResponse<String> res = postRequest(0,domain+"api/certificates",json);
            		String s[]=res.body().split("[.!:;?{}]");
            		if(res.statusCode() == 201) {
            			for(String a: sinoidalesAsign) {
                			String[] x=a.split("-");
                			res = postRequest(0,domain+"api/certificates/addSinoidales/"+s[2]+"/"+cbCeremonias.getValue()+"/"+x[0],json);
                			//System.out.println(res);
                		}
            			if(res.statusCode() == 201) {
            				res = putRequest(0,domain+"api/folders/addActa/"+cbFolders.getValue(),json);
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
            		
            	}else if(result.get().matches("no auth")) {
            		alert=lc.runAlert(AlertType.INFORMATION, "NO VALIDADO", "El movimiento no fue validado, verifica tu contraseña.");
        			alert.show();
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
    	cbCarreras.setDisable(false);
    	vContainerSigns.setVisible(false);
    	hStatus.setVisible(false);
    	btnAsignarSinoidal.setDisable(false);
		btnEliminarSinoidal.setDisable(false);
		cbSinoidales.setDisable(false);
		cbCeremonias.setDisable(false);
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
    	btnDeleteSinoidal.setVisible(false);
    	listTelefonos.getItems().clear();
    	listCorreos.getItems().clear();
    	btnEliminarTelefono.setDisable(true);
    	btnEliminarEmail.setDisable(true);
    	hActasFirmadas.setVisible(false);
    	titleSinodal.setText("Crear Sinodal");
    }
    
    private void createSinoidal() throws InterruptedException, IOException {
    	if(validateTextField(txtNombreSinoidal,"El valor del campo nombre es nulo o numerico","[a-zA-Z\u00f1\u00d1\s]+",255) && 
    			validateTextField(txtApellidosSinoidal,"El valor del campo apellidos es nulo o numerico","[a-zA-Z\u00f1\u00d1\s]+",255) &&
    			validateTextField(txtIdSinoidal,"El valor del campo matricula es nulo o numerico","[0-9]+",10)&&
    			validateTextField(txtCoordinacionSinoidal,"El valor del coordinación es nulo o contiene letras","[a-zA-Z\u00f1\u00d1\s]+",255) &&
    			validateList(listTelefonos,1,"Ingresa al menos un telefono para asignarlo al sinodal") &&
    			validateList(listCorreos,1,"Ingresa al menos un email para asignarlo al sinodal")){
    		if(!checkSinoidalesList(txtIdSinoidal.getText())) {
    			Dialog<String> di = getValidateModal();
    			Optional<String> result = di.showAndWait();
	        	
	        	if(result.get().matches("auth")) {
	        		JSONObject js = new JSONObject();
	        		js.put("first_Name", txtNombreSinoidal.getText());
	        		js.put("second_Name", txtApellidosSinoidal.getText());
	        		js.put("id_professor", txtIdSinoidal.getText());
	        		//js.put("email", txtEmailSinoidal.getText());
	        		js.put("area", txtCoordinacionSinoidal.getText());
	        		//js.put("telephone", txtTelefonoSinoidal.getText());
	        		js.put("disponibility",1);
	        		js.put("isActive",1);
	        		HttpResponse<String> response = postRequest(0,domain+"api/sinoidales",js);
	        		String s[]=response.body().split("[,.!:;?{}]");
	        		if( response == null || response.statusCode()!=201) {
	        			alert=lc.runAlert(AlertType.ERROR, "ERROR SINOIDAL", "Hubo un error al momento de crear el sinodal, verifica tu conexión a internet.\nStatus: "+response.statusCode());
	            		alert.show();
	        		}else {
	        			for(String src: listTelefonos.getItems()) {
	        				
	        				js.put("phone", src);
	        				response = postRequest(0,domain+"/api/sinoidales/addPhone/"+s[2],js);
	        				if(response == null || response.statusCode() != 201) {
	        					alert=lc.runAlert(AlertType.ERROR, "ERROR SINOIDAL", "Hubo un error al momento de crear el sinodal, verifica tu conexión a internet.\nStatus: "+response.statusCode());
	    	            		alert.show();
	        				}
	        			}
	        			
	        			for(String src:listCorreos.getItems()) {
	        				js.clear();
	        				js.put("email", src);
	        				response = postRequest(0,domain+"api/sinoidales/addEmail/"+s[2],js);
	        				if(response == null || response.statusCode() != 201) {
	        					alert=lc.runAlert(AlertType.ERROR, "ERROR SINOIDAL", "Hubo un error al momento de crear el sinodal, verifica tu conexión a internet.\nStatus: "+response.statusCode());
	    	            		alert.show();
	        				}
	        			}

	        			fillTableSinoidales();
	        			sinoidalesPane.toFront();
	        			alert=lc.runAlert(AlertType.INFORMATION, "SINOIDAL CREADO", "El nuevo Sinodal ha sido creado exitosamente.");
	            		alert.show();
	        		}
	        	}else if(result.get().matches("no auth")) {
	        		alert=lc.runAlert(AlertType.INFORMATION, "NO VALIDADO", "El movimiento no fue validado, verifica tu contraseña.");
        			alert.show();
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
        	Dialog<String> di = getValidateModal();
        	Optional<String> result = di.showAndWait();
        	
        	if(result.get().matches("auth")) {
        		res = deleteRequest(0,domain+"api/certificates/actasSinoidales/delete/"+id_Acta);
        		if(res == null || res.statusCode() !=201) {
        			alert=lc.runAlert(AlertType.ERROR, "ERROR ELIMINAR ACTA", "Hubo un error al momento de eliminar el acta, verifica tu conexión a internet.\nStatus: "+res.statusCode());
        			alert.show();
        		}else {
        			res = deleteRequest(0,domain+"api/certificates/"+id_Acta);
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
        	}else if(result.get().matches("no auth")) {
        		alert=lc.runAlert(AlertType.INFORMATION, "NO VALIDADO", "El movimiento no fue validado, verifica tu contraseña.");
    			alert.show();
        	}
    }
    
    //Check delete Sinoidal
    
    /*private void deleteSinoidal() throws IOException, InterruptedException {
    	HttpResponse<String> res = null;
		Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Eliminar Sinoidal", "¿Seguro que quieres eliminar el sinoidal con el id #"+id_Sinoidal+" ?");
    	Optional<ButtonType> result = alert.showAndWait();
    	if(result.get()==ButtonType.OK) {
    		res = deleteRequest(0,"http://127.0.0.1:4040/api/sinoidales/phones/delete/"+id_Sinoidal);
    		if(res == null || res.statusCode() !=201) {
    			alert=lc.runAlert(AlertType.ERROR, "ERROR ELIMINAR ACTA", "Hubo un error al momento de eliminar el acta, verifica tu conexión a internet.\nStatus: "+res.statusCode());
    			alert.show();
    		}else {
    			res = deleteRequest(0,"http://127.0.0.1:4040/api/sinoidales/emails/delete/"+id_Sinoidal);
    			if(res == null || res.statusCode() != 201) {
    				alert=lc.runAlert(AlertType.ERROR, "ERROR ELIMINAR ACTA", "Hubo un error al momento de eliminar el acta, verifica tu conexión a internet.\nStatus: "+res.statusCode());
            		alert.show();
    			}else {
    				res = deleteRequest(0,"http://127.0.0.1:4040/api/sinoidales/delete/"+id_Sinoidal);
    				if(res == null || res.statusCode() != 201) {
        				alert=lc.runAlert(AlertType.ERROR, "ERROR ELIMINAR SINOIDAL", "Hubo un error al momento de eliminar el sinoidal, verifica tu conexión a internet.\nStatus: "+res.statusCode());
                		alert.show();
        			}else {
        				fillTableActas();
        				sinoidalesPane.toFront();
        				alert=lc.runAlert(AlertType.INFORMATION, "SINOIDAL ELIMINADO", "El sinoidal fue eliminado correctamente del sistema.");
                		alert.show();
        			}
    			}
    		}
    	}
    }*/
    
  
    
    
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
    
    
    private void createCeremonia() throws InterruptedException, IOException {
    	String date = datePickerCeremonia.getValue().toString();
    	if(date != null) {
    		if(!checkCeremoniasList(date)) {
    			Dialog<String> di = getValidateModal();
        		Optional<String> result = di.showAndWait();
            	if(result.get().matches("auth")) {
            		JSONObject json = new JSONObject();
            		json.put("date",datePickerCeremonia.getValue());
            		json.put("cicle", getCicle(datePickerCeremonia.getValue()));
            		HttpResponse<String> res = postRequest(0,domain+"api/ceremonies",json);
            		if(res.statusCode()!=201) {
            			alert=lc.runAlert(AlertType.ERROR, "ERROR CEREMONIA", "Hubo un error al momento de crear la ceremonia, verifica tu conexión a internet\nStatus: "+res.statusCode());
                		alert.show();
            		}else {
        				fillTableCeremony();
            			alert=lc.runAlert(AlertType.ERROR, "CEREMONIA CREADA", "Se creo la ceremonia de manera exitosa !\nComienza a asignar actas a la nueva ceremonia");
                		alert.show();
                		datePickerCeremonia.setValue(null);
            		}
            	}else if(result.get().matches("no auth")) {
            		alert=lc.runAlert(AlertType.INFORMATION, "NO VALIDADO", "El movimiento no fue validado, verifica tu contraseña.");
        			alert.show();
            	}
    		}else {
    			alert=lc.runAlert(AlertType.ERROR, "ERROR FECHA", "Ingresa un valor en el campo fecha ya se encuentra registrado.");
        		alert.show();
    		}
    		
    		
    	}else {
    		alert=lc.runAlert(AlertType.ERROR, "ERROR FECHA", "Ingresa un valor en el campo fecha");
    		alert.show();
    	}
    }
    
	private String getCicle(LocalDate date_limit){
		Year currentYear = Year.now();
		String currentDateLimit = currentYear.toString()+"-06-01";
		ChronoLocalDate dt= LocalDate.parse(currentDateLimit);
    	if(date_limit.isAfter(dt)) {
    		return "AGO-DIC";
    	}else {
    		return "ENE-JUN";
    	}
    }
    
	public HttpResponse<String> postRequest(int intentos,String URL,JSONObject jsonBody) throws InterruptedException{
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
	
	private boolean validateListSinodales(String message) {
		if(listSinoidales.getItems().size()+listFirmas.getItems().size() < 3) {
			Alert alert=lc.runAlert(AlertType.ERROR, "ERROR SINODALES",message);
    		alert.show();
    		listSinoidales.requestFocus();
		}else {
			return true;
		}
		return false;
	}
	
    private boolean validateList(ListView<String> list,int SIZE,String message) {
    	if(list.getItems().size() < SIZE) {
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
    	btnModificarCeremonia.setDisable(true);
		btnEliminarCeremonia.setDisable(true);
		btnModificarFolder.setDisable(true);
		btnEliminarFolder.setDisable(true);
		
		
    	cbEstantes.getItems().clear();
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
        
        datePickerCeremonia.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });

        datePickerCeremonia.setValue(null);
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
	        		if((listSinoidales.getItems().size()+listFirmas.getItems().size()) < 3) {
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
    
    private void setFilterComboboxSinodal() {
    	cbFilterAreaSinodal.getItems().clear();
    	cbFilterAreaSinodal.getItems().add("Area");
    	
    	for(Sinoidales a:tableSinoidales.getItems()) {
    		if(!cbFilterAreaSinodal.getItems().contains(a.getArea())) {
  	  			cbFilterAreaSinodal.getItems().add(a.getArea());
  			}
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
    	listFirmas.getItems().clear();
    	cbFolders.getItems().clear();
    	cbCeremonias.getItems().clear();
    	cbSinoidales.getItems().clear();
    	//cbFolders.setPromptText("Selecciona un Folder");
    	//cbCeremonias.setPromptText("Selecciona la Ceremonia");
    	//cbSinoidales.setPromptText("Selecciona el sinoidal");
    	String sample;
    	List<Sinoidales> sinoidales=handleResponseSinoidales(domain+"api/sinoidales");
    	List<Folder> folders = handleResponseFolder(domain+"api/folders");
    	List<Ceremonias> ceremonias=handleResponseCeremony(domain+"api/ceremonies");
    	for(Folder f : folders) {
    		cbFolders.getItems().add(f.getId_folder().toString());
    	}
    	
    	for(Sinoidales s: sinoidales) {
    		HttpResponse<String> rsp = getRequest(0,domain+"api/sinoidales/phone/"+s.getId_sinoidales());
    		if(rsp.statusCode()==201) {
    			String phone[]=rsp.body().split("[.!:;?{}\"]");
    			
    			HttpResponse<String> rp = getRequest(0,domain+"api/sinoidales/email/"+s.getId_sinoidales());
    			if(rp.statusCode()==201) {
    				String email[] = rp.body().split("[!:;?{}\"]");
    				sample = s.getId_sinoidales()+"-"+s.getFirst_Name() + " " + s.getSecond_Name()+" || "+phone[5] + " || "+email[5];
            		cbSinoidales.getItems().add(sample);
    			}
    			
    		}
    		
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
    	List<Actas> actas = handleResponseActas(domain+"api/certificates");
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
    	List<Sinoidales> sinoidales=handleResponseSinoidales(domain+"api/sinoidales");
    	for(Sinoidales s:sinoidales) {
  			tableSinoidales.getItems().add(s);	
  		}
    	
    }
    
    private void fillTableCeremony() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	tableCeremonias.getItems().clear();
    	List<Ceremonias> ceremonias=handleResponseCeremony(domain+"api/ceremonies");
    	for(Ceremonias s:ceremonias) {
  			s.setDate(splitData(s.getDate(), "T"));
  			tableCeremonias.getItems().add(s);
  		}
    }
    
    private void filltableFolder() throws JsonMappingException, JsonProcessingException, InterruptedException {
    	HttpResponse<String> res;
    	tableFolders.getItems().clear();
    	List<Folder> folders = handleResponseFolder(domain+"api/folders");
    	for(Folder f : folders) {
    		res = getRequest(0,domain+"api/folder/count/"+f.getId_folder());
    		String s[] = res.body().split("[.!:;?{}\"]");
    		f.setActas_num(Integer.parseInt(s[4]));
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
    
    protected List<String> handleResponseStrings(String URL) throws InterruptedException, JsonMappingException, JsonProcessingException{
    	HttpResponse<String> response=getRequest(0,URL);
    	List<String> data = null;
    	if(response.statusCode() == 201) {
    		ObjectMapper objectMapper = new ObjectMapper();
    		data = objectMapper.readValue(response.body(), new TypeReference<List<String>>(){});
    	}
    	return data;
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
    	if(response.statusCode()==201) {
    		ObjectMapper objectMapper = new ObjectMapper();
    		listActasSinoidales = objectMapper.readValue(response.body(), new TypeReference<List<Actas_Sinoidales>>(){});
    	}
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
    	lastNameColumn.setMinWidth(135);

    	
    	TableColumn  idFolderColumn= new TableColumn("Folder");
    	idFolderColumn.setCellValueFactory(new PropertyValueFactory<>("id_Folder_fk"));
    	idFolderColumn.setStyle( "-fx-alignment: center;");
    	idFolderColumn.setSortable(false);
    	idFolderColumn.setResizable(false);
    	idFolderColumn.setMinWidth(50);
    	
    	/*TableColumn  signaturesColumn= new TableColumn("#Firmas");
    	signaturesColumn.setCellValueFactory(new PropertyValueFactory<>("signatures"));
    	signaturesColumn.setStyle( "-fx-alignment: center;");
    	signaturesColumn.setSortable(false);
    	signaturesColumn.setResizable(false);
    	signaturesColumn.setMinWidth(30);*/

    	TableColumn statusColumn = new TableColumn("Status");
    	statusColumn.setMinWidth(107);
    	statusColumn.setStyle( "-fx-alignment: center;");
    	statusColumn.setSortable(false);
    	statusColumn.setResizable(false);
    	statusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Actas,Circle>,ObservableValue<Circle>>(){

			@Override
			public ObservableValue<Circle> call(CellDataFeatures<Actas, Circle> arg0) {
				Circle circle = new Circle(7);
				switch(arg0.getValue().getSignatures()) {
				case 1:
	                circle.setFill(Color.ORANGE);
					break;
				case 2:
	                circle.setFill(Color.YELLOW);
					break;
				case 3:
	                circle.setFill(Color.GREEN);
					break;
				default:
	                circle.setFill(Color.RED);
					break;
				}
				
				return new SimpleObjectProperty (circle);
			}
    		
    	});
    	
    	
    	TableColumn  dateLimitColumn= new TableColumn("Fecha Limite");
    	dateLimitColumn.setCellValueFactory(new PropertyValueFactory<>("date_limit_fk"));
    	dateLimitColumn.setStyle( "-fx-alignment: center;");
    	dateLimitColumn.setSortable(false);
    	dateLimitColumn.setResizable(false);
    	dateLimitColumn.setMinWidth(100);
    	
    	TableColumn  ceremonyColumn= new TableColumn("# Ceremonia");
    	ceremonyColumn.setCellValueFactory(new PropertyValueFactory<>("id_ceremony_fk"));
    	ceremonyColumn.setStyle( "-fx-alignment: center;");
    	ceremonyColumn.setSortable(false);
    	ceremonyColumn.setResizable(false);
    	ceremonyColumn.setMinWidth(100);

    	
    	tableActas.getColumns().addAll(idActaColumn,nameColumn,lastNameColumn,idStudentColumn,idFolderColumn,dateLimitColumn,ceremonyColumn/*,signaturesColumn*/);
    	tableActas.getColumns().add(statusColumn);
    	
    	tableActas.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
    		if(e.getClickCount() == 2) {
    			Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Crear Acta", "¿Seguro que quieres visualizar esta acta?");
            	Optional<ButtonType> result = alert.showAndWait();
            	
            	if (result.get() == ButtonType.OK){
            		Actas selectedItem = tableActas.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                    	actasPane.toFront();
                    	titleActa.setText("ACTUALIZAR ACTA");
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
							List<Actas_Sinoidales> actas_sinoidales=handleResponseActasSinoidales(domain+"api/certificates/actasSinoidales/"+selectedItem.getId_actas());
					  		if(actas_sinoidales == null) {
					  			lc.runAlert(AlertType.ERROR,"Error de Conexion","Revisa tu conexión");
					  		}else {
					  			for(Actas_Sinoidales as:actas_sinoidales) {
					  				List<Sinoidales> sinoidalesActa = handleResponseSinoidales(domain+"api/sinoidales/search/"+as.getId_sinoidales_fk());
					  				for(Sinoidales i : sinoidalesActa) {
					  					HttpResponse<String> rsp = getRequest(0,domain+"api/sinoidales/phone/"+as.getId_sinoidales_fk());
						  	    		if(rsp.statusCode()==201) {
						  	    			String phone[]=rsp.body().split("[.!:;?{}\"]");
						  	    			
						  	    			HttpResponse<String> rp = getRequest(0,domain+"api/sinoidales/email/"+as.getId_sinoidales_fk());
						  	    			if(rp.statusCode()==201) {
						  	    				String email[] = rp.body().split("[!:;?{}\"]");
						  	    				String sample = as.getId_sinoidales_fk()+"-"+i.getFirst_Name()+" "+i.getSecond_Name()+" || "+phone[5] + " || "+email[5];
						  	    				if(as.getSigned()==1) {
							  						btnEliminarFirmaSinoidal.setDisable(false);
							  						listFirmas.getItems().add(sample);
							  					}else{
							  						listSinoidales.getItems().add(sample);
							  					}
							  					cbSinoidales.getItems().remove(sample);
						  	    			}
						  	    			
						  	    		}
					  					
					  				}
					  	  		}

					  			
					  			
					  			if(listFirmas.getItems().size()==3) {
					  				listSinoidales.setDisable(true);
					  				cbSinoidales.setDisable(true);
					  				btnAsignarSinoidal.setDisable(true);
					  				btnEliminarSinoidal.setDisable(true);
					  				btnEliminarFirmaSinoidal.setDisable(true);
					  				btnAgregarFirmaSinoidal.setDisable(true);
					  				btnEliminarActa.setDisable(true);
					  				cbCeremonias.setDisable(true);
					  			}
					  			
					  			getStatus(listFirmas);

					  		
					  			hStatus.setVisible(true);
					  			vContainerSigns.setVisible(true);
					  			cbFolders.getSelectionModel().select(String.valueOf(selectedItem.getId_Folder_fk()));
		                    	cbCeremonias.getSelectionModel().select(selectedItem.getId_ceremony_fk().toString());
		                    	cbCarreras.getSelectionModel().select(selectedItem.getDegree());
		                    	cbCarreras.setDisable(true);
		                    	btnActualizarActa.setVisible(true);
		                    	btnEliminarActa.setVisible(true);
		                    	btnCrearActa.setVisible(false);
		                    	listSinoidales.setDisable(false);
					  		}
						} catch (JsonProcessingException | InterruptedException e1) {
							lc.runAlert(AlertType.ERROR,"Error de Conexion","Revisa tu conexión");
							e1.printStackTrace();
						}
                    	
                    	
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
		idSinoidal.setMinWidth(150);
		
    	TableColumn FirstName = new TableColumn("Nombre");
    	FirstName.setCellValueFactory(new PropertyValueFactory<>("first_Name"));
    	FirstName.setStyle( "-fx-alignment: center;");
    	FirstName.setSortable(false);
    	FirstName.setResizable(false);
    	FirstName.setMinWidth(150);
    	
    	TableColumn  secondName= new TableColumn("Apellidos");
    	secondName.setCellValueFactory(new PropertyValueFactory<>("second_Name"));
    	secondName.setStyle( "-fx-alignment: center;");
    	secondName.setSortable(false);
    	secondName.setResizable(false);
    	secondName.setMinWidth(150);

    	TableColumn  idProfessor= new TableColumn("Matricula");
    	idProfessor.setCellValueFactory(new PropertyValueFactory<>("id_professor"));
    	idProfessor.setStyle( "-fx-alignment: center;");
    	idProfessor.setSortable(false);
    	idProfessor.setResizable(false);
    	idProfessor.setMinWidth(200);
    	
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
    	areaColumn.setMinWidth(200);

    	
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
    	
    	tableSinoidales.getColumns().addAll(idSinoidal,FirstName,secondName,idProfessor,areaColumn);
    
    	tableSinoidales.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
    		if(e.getClickCount() == 2) {
    			Alert alert=lc.runAlert(AlertType.CONFIRMATION, "Crear Acta", "¿Seguro que quieres visualizar este sinoidal?");
            	Optional<ButtonType> result = alert.showAndWait();
            	
            	if (result.get() == ButtonType.OK){
            		Sinoidales selectedItem = tableSinoidales.getSelectionModel().getSelectedItem();
                    if (selectedItem!=null) {
                    	listTelefonos.getItems().clear();
                    	listCorreos.getItems().clear();
                    	addSinoidales.toFront();
                    	//id_Sinoidal = selectedItem.getId_sinoidales();
                    	txtNombreSinoidal.setText(selectedItem.getFirst_Name());
                    	txtApellidosSinoidal.setText(selectedItem.getSecond_Name());
                    	txtIdSinoidal.setText(selectedItem.getId_professor().toString());
                    	//txtEmailSinoidal.setText(selectedItem.getEmail());
                    	//txtTelefonoSinoidal.setText(selectedItem.getTelephone());
                    	txtCoordinacionSinoidal.setText(selectedItem.getArea());  
                    	btnCrearSinoidal.setVisible(false);
                    	btnActualizarSinoidal.setVisible(true);
                    	btnDeleteSinoidal.setVisible(true);
                    	txtNombreSinoidal.setDisable(true);
                    	txtApellidosSinoidal.setDisable(true);
                    	txtIdSinoidal.setDisable(true);
                    	txtCoordinacionSinoidal.setDisable(true);
                    	btnEliminarTelefono.setDisable(false);
                    	btnEliminarEmail.setDisable(false);
                    	hActasFirmadas.setVisible(true);
                    	titleSinodal.setText("Editar Sinodal");
						try {
							List<Telephone> telephones = handleResponseTelephone(domain+"api/sinoidales/phones/"+selectedItem.getId_sinoidales());
	                    	for(Telephone t : telephones) {
	                    		listTelefonos.getItems().add(t.getTelephone());
	                    		
	                    	}
	                    	List<Email>emails = handleResponseEmail(domain+"api/sinoidales/emails/"+selectedItem.getId_sinoidales());
							
							for(Email em : emails) {
	                    		listCorreos.getItems().add(em.getEmail());
	                    		
	                    	}
							
							HttpResponse<String> res = getRequest(0,domain+"api/certificates/signed/"+selectedItem.getId_sinoidales());
							if(res.statusCode() == 201) {
								//System.out.println(res.body());
								String s[]=res.body().split("[.!:;?{}]");
								lblNumberActas.setText(s[2]);
							}
							
						} catch (JsonProcessingException | InterruptedException e1) {
							lc.runAlert(AlertType.ERROR,"Error de Conexion","Revisa tu conexión");
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
    	
    	tableCeremonias.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
			if(e.getClickCount()==2) {
				btnModificarCeremonia.setDisable(false);
				btnEliminarCeremonia.setDisable(false);
        		Ceremonias selectedItem = tableCeremonias.getSelectionModel().getSelectedItem();	
        		String dateF = selectedItem.getDate().replace("-","/");
        		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        		LocalDate date = LocalDate.parse(dateF, formatter);
        		System.out.println(date); 
        		
        		datePickerCeremonia.setValue(date);
			}
		});
    	
    	
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
		
		@SuppressWarnings("rawtypes")
		TableColumn  actasNumColumn= new TableColumn("Numero de Actas");
		actasNumColumn.setCellValueFactory(new PropertyValueFactory<>("actas_num"));
		actasNumColumn.setStyle( "-fx-alignment: center;");
		actasNumColumn.setSortable(false);
		actasNumColumn.setResizable(false);
		actasNumColumn.setMinWidth(140);
		
		tableFolders.getColumns().addAll(idFolder,caseColumn,actasNumColumn);
		tableFolders.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
			if(e.getClickCount()==2) {
				btnModificarFolder.setDisable(false);
				btnEliminarFolder.setDisable(false);
        		Folder selectedItem = tableFolders.getSelectionModel().getSelectedItem();
        		cbEstantes.getSelectionModel().select(selectedItem.getId_case().toString());
			}
		});
		
	}
	
	public void getStatus(ListView<String> list) {
		int len=list.getItems().size();
		switch(len) {
			case 1:
				setStatus("POR FIRMAR ("+(3-len)+" SINOIDALES)",Color.ORANGE);
			break;
			
			case 2:
				setStatus("POR FIRMAR ("+(3-len)+" SINOIDAL)",Color.YELLOW);
			break;
			
			case 3:
				setStatus("COMPLETA",Color.GREEN);
			break;
				
			default:
				setStatus("SIN FIRMAS",Color.RED);
			break;
		}
	}
	
	public void setStatus(String status,Color color) {
		lblStatus.setText(status);
		lblStatus.setTextFill(color);
	}
	
	public void setUser() {
		user = lc.getUser();
	}

	public String getUser() {
		return user;
	}
	
}
