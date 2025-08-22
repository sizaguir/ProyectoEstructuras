/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package App;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.DatosVuelos;
import aeropuertovuelos.GrafoVuelos;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author arife
 */
public class EliminarAeropuertoController implements Initializable {

    @FXML
    private TableView<Aeropuerto> tablaAeropuerto;
    @FXML
    private TableColumn<Aeropuerto, String> codAero;
    @FXML
    private TableColumn<Aeropuerto, String> nomAero;
    @FXML
    private TableColumn<Aeropuerto, String> paísAero;
    @FXML
    private TableColumn<Aeropuerto, String> cudadAero;
    
    private GrafoVuelos grafo;
    private ObservableList<Aeropuerto> listaAeropuertos;

    /**
     * Initializes the controller class.
     */
    
    public void setGrafo(GrafoVuelos grafo) {
        this.grafo = grafo;
        cargarAeropuertos();
    }
    
    private void cargarAeropuertos() {
    listaAeropuertos = FXCollections.observableArrayList(grafo.getAeropuertos());

    codAero.setCellValueFactory(data ->
        new javafx.beans.property.SimpleStringProperty(data.getValue().getCodigo()));
    nomAero.setCellValueFactory(data ->
        new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
    paísAero.setCellValueFactory(data ->
        new javafx.beans.property.SimpleStringProperty(data.getValue().getPais()));
    cudadAero.setCellValueFactory(data ->
        new javafx.beans.property.SimpleStringProperty(data.getValue().getCiudad()));

    tablaAeropuerto.setItems(listaAeropuertos);
}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void volver(ActionEvent event) {
        Stage stage = (Stage) tablaAeropuerto.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void eliminarAeropuerto() {
        Aeropuerto aeropuertoSeleccionado = tablaAeropuerto.getSelectionModel().getSelectedItem();

        if (aeropuertoSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún aeropuerto seleccionado");
            alert.setHeaderText(null);
            alert.setContentText("Por favor selecciona un aeropuerto de la tabla.");
            alert.showAndWait();
            return;
        }

        // Confirmación antes de eliminar
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro de que deseas eliminar el aeropuerto " 
                                    + aeropuertoSeleccionado.getNombre() 
                                    + " y todos sus vuelos asociados?");

        if (confirmacion.showAndWait().get() != ButtonType.OK) {
            return;
        }

        grafo.eliminarAeropuerto(aeropuertoSeleccionado);
        listaAeropuertos.remove(aeropuertoSeleccionado);

        // Guardar cambios
        DatosVuelos.guardarDatos(grafo);

        // Mensaje de éxito
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aeropuerto eliminado");
        alert.setHeaderText(null);
        alert.setContentText("El aeropuerto fue eliminado correctamente.");
        alert.showAndWait();  
    }
    
}
