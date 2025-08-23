package App;

import App.EditarVueloFXMLController;
import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.DatosVuelos;
import aeropuertovuelos.GrafoVuelos;
import aeropuertovuelos.Vuelo;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MostrarVuelosFXMLController {

    @FXML
    private TableView<Vuelo> tablaVuelos;
    @FXML
    private TableColumn<Vuelo, String> colOrigen;
    @FXML
    private TableColumn<Vuelo, String> colPaisOrigen;
    @FXML
    private TableColumn<Vuelo, String> colCiudadOrigen;
    @FXML
    private TableColumn<Vuelo, String> colDestino;
    @FXML
    private TableColumn<Vuelo, String> colPaisDestino;
    @FXML
    private TableColumn<Vuelo, String> colCiudadDestino;
    @FXML
    private TableColumn<Vuelo, Double> colPeso;
    @FXML
    private TableColumn<Vuelo, String> colAerolinea;

    @FXML
    private Label tituloLabel;

    private GrafoVuelos grafo;
    private Aeropuerto aeropuerto;
    private ObservableList<Vuelo> listaVuelos;

    // Recibe datos desde la ventana principal
    public void setDatos(Aeropuerto aeropuerto, GrafoVuelos grafo) {
        this.aeropuerto = aeropuerto;
        this.grafo = grafo;

        tituloLabel.setText("Vuelos desde " + aeropuerto.getNombre());
        cargarVuelos();
    }

    private void cargarVuelos() {
        listaVuelos = FXCollections.observableArrayList(grafo.getVuelosDesde(aeropuerto));

        colOrigen.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getOrigen().getCodigo()));
        colPaisOrigen.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getOrigen().getPais()));
        colCiudadOrigen.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getOrigen().getCiudad()));

        colDestino.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getDestino().getCodigo()));
        colPaisDestino.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getDestino().getPais()));
        colCiudadDestino.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getDestino().getCiudad()));

        colPeso.setCellValueFactory(data ->
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPeso()));
        colAerolinea.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getAerolinea()));

        tablaVuelos.setItems(listaVuelos);
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaVuelos.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void editarVuelo() {
        Vuelo vuelo = tablaVuelos.getSelectionModel().getSelectedItem();
        if (vuelo == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona un vuelo primero.").showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditarVuelo.fxml"));
            Scene scene = new Scene(loader.load());

            EditarVueloFXMLController controller = loader.getController();
            controller.setGrafo(grafo);
            controller.setVuelo(vuelo);

            Stage stage = new Stage();
            stage.setTitle("Editar Vuelo");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tablaVuelos.getScene().getWindow());
            stage.showAndWait();

            // refrescar tabla
            tablaVuelos.setItems(FXCollections.observableArrayList(
                    grafo.getVuelosDesde(aeropuerto)
            ));

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de edición.").showAndWait();
        }
    }

    @FXML
    private void eliminarVuelo() {
        Vuelo vuelo = tablaVuelos.getSelectionModel().getSelectedItem();
        if (vuelo == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona un vuelo primero.").showAndWait();
            return;
        }

        grafo.eliminarVuelo(vuelo.getOrigen(), vuelo.getDestino());
        tablaVuelos.setItems(FXCollections.observableArrayList(
                grafo.getVuelosDesde(aeropuerto)
        ));
        DatosVuelos.guardarDatos(grafo);
    }

    @FXML
    private void volver() {
        Stage stage = (Stage) tablaVuelos.getScene().getWindow();
        stage.close();
    }
}

