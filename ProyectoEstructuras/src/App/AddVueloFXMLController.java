package App;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.GrafoVuelos;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddVueloFXMLController {

    @FXML private ComboBox<Aeropuerto> cbOrigen;
    @FXML private ComboBox<Aeropuerto> cbDestino;
    @FXML private TextField txtPeso;
    @FXML private TextField txtAerolinea;
    @FXML private Label lblMensaje;

    private GrafoVuelos grafo;

    public void setGrafo(GrafoVuelos grafo) {
        this.grafo = grafo;

        // Cargar aeropuertos en los ComboBox
        cbOrigen.getItems().addAll(grafo.getAeropuertos());
        cbDestino.getItems().addAll(grafo.getAeropuertos());
    }

    @FXML
    private void agregarVuelo() {
        Aeropuerto origen = cbOrigen.getValue();
        Aeropuerto destino = cbDestino.getValue();
        String aerolinea = txtAerolinea.getText();

        if (origen == null || destino == null || txtPeso.getText().isEmpty() || aerolinea.isEmpty()) {
            lblMensaje.setText("⚠️ Todos los campos son obligatorios");
            return;
        }

        try {
            double peso = Double.parseDouble(txtPeso.getText());

            grafo.agregarVuelo(origen, destino, peso, aerolinea);
            lblMensaje.setText("✅ Vuelo agregado correctamente");

            // limpiar campos
            cbOrigen.setValue(null);
            cbDestino.setValue(null);
            txtPeso.clear();
            txtAerolinea.clear();

        } catch (NumberFormatException e) {
            lblMensaje.setText("⚠️ El peso debe ser un número");
        }
    }
}
