package App;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.DatosVuelos;
import aeropuertovuelos.GrafoVuelos;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author arife
 */
public class AddAeropuertoFXMLController implements Initializable {

    @FXML
    private TextField nombreText;
    @FXML
    private TextField codText;
    @FXML
    private TextField ciudadText;
    @FXML
    private TextField paísText;
    @FXML
    private TextField latText;
    @FXML
    private TextField longText;
    @FXML
    private Button addAeropuertoButton;
    @FXML
    private Button returnToMainButton;
    @FXML
    private Button clearAllButton;
    private GrafoVuelos grafo;
    private FXMLDocumentController mainController;
    private double posX;
    private double posY;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void agregarAeropuerto(ActionEvent event) {
        String nombre = nombreText.getText().trim();
        String codigo = codText.getText().trim();
        String ciudad = ciudadText.getText().trim();
        String pais = paísText.getText().trim();
        String latStr = latText.getText().trim();
        String lonStr = longText.getText().trim();
        
        if(nombre.isEmpty()||codigo.isEmpty()||ciudad.isEmpty()||pais.isEmpty()||latStr.isEmpty()||lonStr.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos vacíos");
            alert.setHeaderText(null);
            alert.setContentText("Por favor complete todos los campos.");
            alert.showAndWait();
            return;
        }
        
        double lat, lon;
        try {
            lat = Double.parseDouble(latStr);
            lon = Double.parseDouble(lonStr);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en formato");
            alert.setHeaderText(null);
            alert.setContentText("⚠ Latitud y longitud deben ser valores numéricos válidos.");
            alert.showAndWait();
            return;
        }
        
        Aeropuerto aeropuerto = new Aeropuerto(codigo, nombre, ciudad, pais, lat, lon);
        aeropuerto.setX(posX);
        aeropuerto.setY(posY);
        
        if (grafo.contieneAeropuerto(aeropuerto)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicado");
            alert.setHeaderText(null);
            alert.setContentText("⚠ Ya existe un aeropuerto con ese código.");
            alert.showAndWait();
            return;
        }
        
        grafo.agregarAeropuerto(aeropuerto);
        DatosVuelos.guardarDatos(grafo);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("✅ Aeropuerto agregado correctamente.");
        alert.showAndWait();
        
        Stage stage = (Stage) addAeropuertoButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void returnToMain(ActionEvent event) {
        Stage stage = (Stage) returnToMainButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clearAll(ActionEvent event) {
        nombreText.clear();
        codText.clear();
        ciudadText.clear();
        paísText.clear();
        latText.clear();
        longText.clear();
    }
    
    public void setGrafo(GrafoVuelos grafo) {
        this.grafo = grafo;
    }

    public void setMainController(FXMLDocumentController mainController) {
        this.mainController = mainController;
    }
    
    public void setPosicion(double x, double y) {
        this.posX = x;
        this.posY= y;
    }
    
}
