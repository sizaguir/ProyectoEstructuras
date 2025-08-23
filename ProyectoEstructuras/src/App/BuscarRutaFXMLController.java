package App;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.GrafoVuelos;
import aeropuertovuelos.Rutas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    
}
