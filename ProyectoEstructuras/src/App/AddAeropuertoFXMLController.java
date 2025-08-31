package App;

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

        if(nombre.isEmpty() || codigo.isEmpty() || ciudad.isEmpty() || pais.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos vacíos");
            alert.setHeaderText(null);
            alert.setContentText("Por favor complete los campos obligatorios.");
            alert.showAndWait();
            return;
        }

        double cx = posX;
        double cy = posY;

        // Si ingresaron lat/lon, calculamos la posición en el mapa
        if(!latStr.isEmpty() && !lonStr.isEmpty()){
            if(!esNumero(latStr) || !esNumero(lonStr)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error en formato");
                alert.setHeaderText(null);
                alert.setContentText(" Latitud y longitud deben ser valores numéricos válidos.");
                alert.showAndWait();
                return;
            }

            double lat = Double.parseDouble(latStr);
            double lon = Double.parseDouble(lonStr);

            // Conversión simple a píxeles sobre el AnchorPane
            cx = (lon + 180) * (mainController.getWidthGrafoPane() / 360.0);
            cy = (90 - lat) * (mainController.getHeightGrafoPane() / 180.0);
        }

        Aeropuerto aeropuerto = new Aeropuerto(codigo, nombre, ciudad, pais);

        if (latStr.isEmpty()) {
            aeropuerto.setLatitud(0);
        } else {
            aeropuerto.setLatitud(Double.parseDouble(latStr));
        }

        if (lonStr.isEmpty()) {
            aeropuerto.setLongitud(0);
        } else {
            aeropuerto.setLongitud(Double.parseDouble(lonStr));
        }

        aeropuerto.setX(cx);
        aeropuerto.setY(cy);

        if(grafo.contieneAeropuerto(aeropuerto)){
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
    
    //Para que valide si es número
    private boolean esNumero(String texto) {
        return texto.matches("-?\\d+(\\.\\d+)?"); //Decimales y negativos
    }
    
}
