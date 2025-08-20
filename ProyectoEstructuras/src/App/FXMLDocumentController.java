package App;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class FXMLDocumentController {

    @FXML
    private AnchorPane panelPrincipal;

    // Método genérico para cargar cualquier FXML dentro del panel principal
    private void cargarVista(String fxml) {
        try {
            AnchorPane nuevaVista = FXMLLoader.load(getClass().getResource(fxml));
            panelPrincipal.getChildren().setAll(nuevaVista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --------- Aeropuerto ----------
    @FXML
    private void abrirAddAeropuerto(ActionEvent event) {
        cargarVista("AddAeropuerto.fxml");
    }

    @FXML
    private void abrirEditAeropuerto(ActionEvent event) {
        cargarVista("EditarAeropuerto.fxml");
    }

    @FXML
    private void abrirDeleteAeropuerto(ActionEvent event) {
        cargarVista("EliminarAeropuerto.fxml");
    }

    // --------- Vuelo ----------
    @FXML
    private void abrirAddVuelo(ActionEvent event) {
        cargarVista("AddVuelo.fxml");
    }

    @FXML
    private void abrirEditVuelo(ActionEvent event) {
        cargarVista("EditarVuelo.fxml");
    }

    @FXML
    private void abrirDeleteVuelo(ActionEvent event) {
        cargarVista("EliminarVuelo.fxml");
    }

    // --------- Buscar rutas ----------
    @FXML
    private void abrirBuscarRutas(ActionEvent event) {
        cargarVista("BuscarRuta.fxml");
    }
}
