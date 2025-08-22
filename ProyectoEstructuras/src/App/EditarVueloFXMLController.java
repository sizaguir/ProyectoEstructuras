package App;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.DatosVuelos;
import aeropuertovuelos.GrafoVuelos;
import aeropuertovuelos.Vuelo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditarVueloFXMLController {

    @FXML
    private ComboBox<Aeropuerto> cmbOrigen;
    @FXML
    private ComboBox<Aeropuerto> cmbDestino;
    @FXML
    private TextField txtPeso;
    @FXML
    private TextField txtAerolinea;

    private GrafoVuelos grafo;
    private Vuelo vueloOriginal;  // el vuelo que se está editando

    public void setGrafo(GrafoVuelos grafo) {
        this.grafo = grafo;
        cmbOrigen.getItems().setAll(grafo.getAeropuertos());
        cmbDestino.getItems().setAll(grafo.getAeropuertos());
    }

    // Recibir el vuelo desde la tabla
    public void setVuelo(Vuelo vuelo) {
        this.vueloOriginal = vuelo;
        cmbOrigen.setValue(vuelo.getOrigen());
        cmbDestino.setValue(vuelo.getDestino());
        txtPeso.setText(String.valueOf(vuelo.getPeso()));
        txtAerolinea.setText(vuelo.getAerolinea());
    }

    
    @FXML
    private void guardarCambios() {
        try {
            // Validar si realmente se cargó un vuelo
            if (vueloOriginal == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, 
                    "Primero selecciona un vuelo para editar.");
                alert.showAndWait();
                return; // No hace nada más
            }

            Aeropuerto origen = cmbOrigen.getValue();
            Aeropuerto destino = cmbDestino.getValue();
            String aerolinea = txtAerolinea.getText();

            if (origen == null || destino == null || txtPeso.getText().isEmpty() || aerolinea.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios");
            }

            double peso = Double.parseDouble(txtPeso.getText());

            // eliminar vuelo viejo y agregar el nuevo
            grafo.eliminarVuelo(vueloOriginal.getOrigen(), vueloOriginal.getDestino());
            grafo.agregarVuelo(origen, destino, peso, aerolinea);

            DatosVuelos.guardarDatos(grafo); // persistir cambios

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Vuelo editado");
            alert.setHeaderText(null);
            alert.setContentText("El vuelo se editó correctamente.");
            alert.showAndWait();

            cerrarVentana();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al guardar cambios: " + e.getMessage());
            alert.showAndWait();
        }
    }



    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtPeso.getScene().getWindow();
        stage.close();
    }
}
