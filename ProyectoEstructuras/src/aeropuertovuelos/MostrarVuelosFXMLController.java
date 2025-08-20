package aeropuertovuelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
}
