package App;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.ExportarPDF;
import aeropuertovuelos.GrafoVuelos;
import aeropuertovuelos.Rutas;
import java.io.File;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BuscarRutaFXMLController {

    @FXML
    private ComboBox<Aeropuerto> comboOrigen;
    @FXML
    private ComboBox<Aeropuerto> comboDestino;
    @FXML
    private ListView<String> listaRuta;
    @FXML
    private Label labelDistancia;

    private GrafoVuelos grafo;

    public void setGrafo(GrafoVuelos grafo) {
        this.grafo = grafo;
        comboOrigen.setItems(FXCollections.observableArrayList(grafo.getAeropuertos()));
        comboDestino.setItems(FXCollections.observableArrayList(grafo.getAeropuertos()));
    }

    @FXML
    private void buscarRuta() {
        Aeropuerto origen = comboOrigen.getValue();
        Aeropuerto destino = comboDestino.getValue();

        if (origen == null || destino == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona origen y destino.").showAndWait();
            return;
        }

        // Usamos tu clase Rutas
        Rutas rutas = new Rutas(grafo);
        Rutas.RutaResultado resultado = rutas.dijkstra(origen, destino);

        if (resultado.getCamino().isEmpty() || resultado.getDistanciaTotal() == Double.POSITIVE_INFINITY) {
            listaRuta.setItems(FXCollections.observableArrayList("No hay ruta disponible."));
            labelDistancia.setText("");
            return;
        }

        // Mostrar el camino paso a paso
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < resultado.getCamino().size() - 1; i++) {
            Aeropuerto actual = resultado.getCamino().get(i);
            Aeropuerto siguiente = resultado.getCamino().get(i + 1);
            items.add(actual.getNombre() + " → " + siguiente.getNombre());
        }

        listaRuta.setItems(items);
        labelDistancia.setText("Distancia total: " + resultado.getDistanciaTotal());
    }
    
    @FXML
    private void cerrarVentana() {
        ((Stage) listaRuta.getScene().getWindow()).close();
    }
    
    @FXML
    private void exportarPDF() throws IOException, com.itextpdf.text.DocumentException {
        Aeropuerto origen = comboOrigen.getValue();
        Aeropuerto destino = comboDestino.getValue();

        if (origen == null || destino == null) {
            new Alert(Alert.AlertType.WARNING, "Primero selecciona origen y destino.").showAndWait();
            return;
        }

        Rutas rutas = new Rutas(grafo);
        Rutas.RutaResultado resultado = rutas.dijkstra(origen, destino);

        if (resultado.getCamino().isEmpty() || resultado.getDistanciaTotal() == Double.POSITIVE_INFINITY) {
            new Alert(Alert.AlertType.WARNING, "No hay ruta disponible para exportar.").showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Ruta en PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        File file = fileChooser.showSaveDialog(comboOrigen.getScene().getWindow());

        if (file != null) {
            ExportarPDF.exportarRuta(resultado, file.getAbsolutePath());
            new Alert(Alert.AlertType.INFORMATION, "Ruta exportada correctamente a PDF.").showAndWait();
        }
    }

    
}
