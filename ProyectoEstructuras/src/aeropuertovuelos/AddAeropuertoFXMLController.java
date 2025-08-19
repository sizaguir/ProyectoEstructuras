/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aeropuertovuelos;

import aeropuertovuelos.Aeropuerto;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void agregarAeropuerto(ActionEvent event) {
        String nombre = nombreText.getText();
        String codigo = codText.getText();
        String ciudad = ciudadText.getText();
        String pais = paísText.getText();
        double lat = Double.parseDouble(latText.getText());
        double lon = Double.parseDouble(longText.getText());

        Aeropuerto aeropuerto = new Aeropuerto(codigo, nombre, ciudad, pais, lat, lon);

        grafo.agregarAeropuerto(aeropuerto);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("Aeropuerto agregado correctamente.");
        alert.showAndWait();

        clearAll(null);
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
    
}
