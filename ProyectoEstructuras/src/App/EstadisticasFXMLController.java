package App;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.GrafoVuelos;
import aeropuertovuelos.Vuelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.*;

public class EstadisticasFXMLController {

    @FXML private Label lblAeropuertos;
    @FXML private Label lblVuelos;
    @FXML private Label lblMasConectado;
    @FXML private Label lblMenosConectado;
    @FXML private Label lblPromedio;

    @FXML private TableView<Fila> tabla;
    @FXML private TableColumn<Fila, String> colAeropuerto;
    @FXML private TableColumn<Fila, Number> colSalientes;
    @FXML private TableColumn<Fila, Number> colEntrantes;
    @FXML private TableColumn<Fila, Number> colTotal;

    private GrafoVuelos grafo;

    public void setGrafo(GrafoVuelos grafo) {
        this.grafo = grafo;
        cargar();
    }

    private void cargar() {
        // Totales
        int nA = grafo.getAeropuertos().size();
        int nV = grafo.getTodosLosVuelos().size();
        lblAeropuertos.setText(String.valueOf(nA));
        lblVuelos.setText(String.valueOf(nV));

        // Conteos por aeropuerto
        Map<Aeropuerto, Integer> sal = new HashMap<>();
        Map<Aeropuerto, Integer> ent = new HashMap<>();
        for (Aeropuerto a : grafo.getAeropuertos()) {
            sal.put(a, grafo.getVuelosDesde(a).size());
            ent.put(a, 0);
        }
        for (Aeropuerto origen : grafo.getAeropuertos()) {
            for (Vuelo v : grafo.getVuelosDesde(origen)) {
                ent.put(v.getDestino(), ent.getOrDefault(v.getDestino(), 0) + 1);
            }
        }

        // filas para tabla
        List<Fila> filas = new ArrayList<>();
        for (Aeropuerto a : grafo.getAeropuertos()) {
            int s = sal.getOrDefault(a, 0);
            int e = ent.getOrDefault(a, 0);
            filas.add(new Fila(a.getNombre(), s, e));
        }
        ObservableList<Fila> data = FXCollections.observableArrayList(filas);

        // columnas
        colAeropuerto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().nombre));
        colSalientes.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().salientes));
        colEntrantes.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().entrantes));
        colTotal.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().salientes + c.getValue().entrantes));
        tabla.setItems(data);

        // más/menos conectado (por total conexiones)
        Comparator<Aeropuerto> cmp = Comparator.comparingInt(a ->
                sal.getOrDefault(a, 0) + ent.getOrDefault(a, 0));

        Aeropuerto mas = grafo.getAeropuertos().stream().max(cmp).orElse(null);
        Aeropuerto menos = grafo.getAeropuertos().stream().min(cmp).orElse(null);

        lblMasConectado.setText(mas == null ? "-" :
                mas.getNombre() + " (S:" + sal.get(mas) + ", E:" + ent.get(mas) + ")");

        lblMenosConectado.setText(menos == null ? "-" :
                menos.getNombre() + " (S:" + sal.get(menos) + ", E:" + ent.get(menos) + ")");

        // promedio conexiones salientes por aeropuerto
        double promedio = nA == 0 ? 0.0 : (double) nV / nA;
        lblPromedio.setText(String.format(Locale.US, "%.2f", promedio));
    }

    @FXML
    private void cerrar() {
        ((Stage) tabla.getScene().getWindow()).close();
    }

    // --- clase fila para la tabla ---
    public static class Fila {
        String nombre;
        int salientes;
        int entrantes;

        public Fila(String nombre, int salientes, int entrantes) {
            this.nombre = nombre;
            this.salientes = salientes;
            this.entrantes = entrantes;
        }
    }
}
