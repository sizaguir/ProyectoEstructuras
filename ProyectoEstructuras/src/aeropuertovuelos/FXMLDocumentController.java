package aeropuertovuelos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class FXMLDocumentController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane panelCentral;

    @FXML
    public void onAddVuelo() {
        cargarEnCentro("AddVuelo.fxml");
    }

    @FXML
    public void onEditVuelo() {
        cargarEnCentro("EditarVuelo.fxml");
    }

    @FXML
    public void onDeleteVuelo() {
        cargarEnCentro("EliminarVuelo.fxml");
    }

    private void cargarEnCentro(String fxmlFile) {
        try {
            AnchorPane vista = FXMLLoader.load(getClass().getResource(fxmlFile));
            borderPane.setCenter(vista);
            System.out.println("Se cargó: " + fxmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
