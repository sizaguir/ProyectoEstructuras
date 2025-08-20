package App;

import aeropuertovuelos.GrafoVuelos;
import aeropuertovuelos.Vuelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EliminarVueloFXMLController {

    @FXML
    private TableView<Vuelo> tablaVuelos;
    @FXML
    private TableColumn<Vuelo, String> colOrigen;
    @FXML
    private TableColumn<Vuelo, String> colDestino;
    @FXML
    private TableColumn<Vuelo, Double> colPeso;
    @FXML
    private TableColumn<Vuelo, String> colAerolinea;

    private GrafoVuelos grafo;
    private ObservableList<Vuelo> listaVuelos;

    // Recibir el grafo desde la ventana principal
    public void setGrafo(GrafoVuelos grafo) {
        this.grafo = grafo;
        cargarVuelos();
    }

    private void cargarVuelos() {
        listaVuelos = FXCollections.observableArrayList(grafo.getTodosLosVuelos());

        colOrigen.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getOrigen().getCodigo()));
        colDestino.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getDestino().getCodigo()));
        colPeso.setCellValueFactory(data ->
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPeso()));
        colAerolinea.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getAerolinea()));

        tablaVuelos.setItems(listaVuelos);
    }

    @FXML
    private void eliminarVuelo() {
        Vuelo vueloSeleccionado = tablaVuelos.getSelectionModel().getSelectedItem();
        if (vueloSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún vuelo seleccionado");
            alert.setHeaderText(null);
            alert.setContentText("Por favor selecciona un vuelo de la tabla.");
            alert.showAndWait();
            return;
        }

        grafo.eliminarVuelo(vueloSeleccionado.getOrigen(), vueloSeleccionado.getDestino());
        listaVuelos.remove(vueloSeleccionado);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vuelo eliminado");
        alert.setHeaderText(null);
        alert.setContentText("El vuelo fue eliminado correctamente.");
        alert.showAndWait();
    }

    @FXML
    private void volver() {
        Stage stage = (Stage) tablaVuelos.getScene().getWindow();
        stage.close();
    }
}
