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
        if (vueloOriginal == null) {
            new Alert(Alert.AlertType.WARNING, "Primero selecciona un vuelo para editar.").showAndWait();
            return;
        }

        Aeropuerto origen = cmbOrigen.getValue();
        Aeropuerto destino = cmbDestino.getValue();
        String aerolinea = txtAerolinea.getText();
        String pesoTexto = txtPeso.getText();

        if (origen == null || destino == null || aerolinea.isEmpty() || pesoTexto.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Todos los campos son obligatorios.").showAndWait();
            return;
        }

        if (!esNumero(pesoTexto)) {
            new Alert(Alert.AlertType.ERROR, "⚠ El peso debe ser un número válido.").showAndWait();
            return;
        }

        double peso = Double.parseDouble(pesoTexto);

        // eliminar vuelo viejo y agregar el nuevo
        grafo.eliminarVuelo(vueloOriginal.getOrigen(), vueloOriginal.getDestino());
        grafo.agregarVuelo(origen, destino, peso, aerolinea);

        DatosVuelos.guardarDatos(grafo); // persistir cambios

        new Alert(Alert.AlertType.INFORMATION, "El vuelo se editó correctamente.").showAndWait();
        cerrarVentana();
    }

    
    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtPeso.getScene().getWindow();
        stage.close();
    }
    
    //Para que valide si es número
    private boolean esNumero(String texto) {
        return texto.matches("\\d+(\\.\\d+)?"); //Para que acepte decimales y enteros
    }
}
