package App;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.GrafoVuelos;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditarVueloFXMLController {

    @FXML
    private ComboBox<Aeropuerto> comboOrigen;

    @FXML
    private ComboBox<Aeropuerto> comboDestino;

    @FXML
    private TextField txtNuevoPeso;

    @FXML
    private TextField txtNuevaAerolinea;

    @FXML
    private Button btnEditarVuelo;

    private GrafoVuelos grafo;

    public void setGrafo(GrafoVuelos grafo) {
        this.grafo = grafo;
        comboOrigen.getItems().addAll(grafo.getAeropuertos());
        comboDestino.getItems().addAll(grafo.getAeropuertos());
    }

    @FXML
    private void editarVuelo() {
        Aeropuerto origen = comboOrigen.getValue();
        Aeropuerto destino = comboDestino.getValue();
        String aerolinea = txtNuevaAerolinea.getText();

        if (origen == null || destino == null || txtNuevoPeso.getText().isEmpty() || aerolinea.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        try {
            double nuevoPeso = Double.parseDouble(txtNuevoPeso.getText());

            boolean editado = grafo.editarVuelo(origen, destino, nuevoPeso, aerolinea);

            if (editado) {
                mostrarAlerta("Éxito", "El vuelo fue editado correctamente.");
            } else {
                mostrarAlerta("Error", "El vuelo no existe en el grafo.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El peso debe ser un número válido.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}