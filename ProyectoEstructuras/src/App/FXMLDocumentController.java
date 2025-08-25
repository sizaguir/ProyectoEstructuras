package App;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.DatosVuelos;
import aeropuertovuelos.GrafoVuelos;
import aeropuertovuelos.Utilitarios;
import aeropuertovuelos.Vuelo;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {
    
    @FXML private AnchorPane grafoPane;
    private GrafoVuelos grafo;
    private Map<Aeropuerto, Circle> nodosVisuales = new HashMap<>();
    private ImageView mapaView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grafo = DatosVuelos.cargarDatos(); // carga archivos
        // Fondo de mapa
        mapaView = new ImageView(new Image(getClass().getResourceAsStream("/resources/mapa.png")));
        mapaView.setPreserveRatio(true);
        mapaView.setOpacity(0.25);
        grafoPane.getChildren().add(0, mapaView);

        // Redibujar al cambiar tamaño
        grafoPane.widthProperty().addListener((o,ov,nv) -> redibujar());
        grafoPane.heightProperty().addListener((o,ov,nv) -> redibujar());

        redibujar();
    }

    private void redibujar() {
        mapaView.setFitWidth(grafoPane.getWidth());
        mapaView.setFitHeight(grafoPane.getHeight());
        dibujarGrafo();
    }

    private void dibujarGrafo() {
        grafoPane.getChildren().remove(1, grafoPane.getChildren().size());
        nodosVisuales.clear();

        // 1) calcular posiciones por lat/lon
        List<Aeropuerto> lista = new ArrayList<>(grafo.getAeropuertos());
        Utilitarios.distribuirPorCoordenadas(lista, grafoPane.getWidth(), grafoPane.getHeight());

        // 2) dibujar nodos
        for (Aeropuerto a : lista) {
            double cx = a.getX(), cy = a.getY();
            Circle nodo = new Circle(cx, cy, 14, Color.CORNFLOWERBLUE);
            Tooltip.install(nodo, new Tooltip(a.getNombre()+" ("+a.getCodigo()+")\n"+a.getCiudad()+", "+a.getPais()));
            nodo.setOnMouseClicked(e -> { abrirPantallaVuelos(a); e.consume(); });

            grafoPane.getChildren().add(nodo);
            nodosVisuales.put(a, nodo);
        }

        // 3) dibujar aristas
        for (Aeropuerto origen : lista) {
            for (Vuelo v : grafo.getVuelosDesde(origen)) {
                Aeropuerto destino = v.getDestino();
                Circle n1 = nodosVisuales.get(origen);
                Circle n2 = nodosVisuales.get(destino);
                if (n1 == null || n2 == null) continue;

                Line l = new Line(n1.getCenterX(), n1.getCenterY(), n2.getCenterX(), n2.getCenterY());
                l.setStrokeWidth(1.5);
                l.setStroke(Color.GRAY);

                Polygon flecha = crearFlecha(n1.getCenterX(), n1.getCenterY(), n2.getCenterX(), n2.getCenterY());
                flecha.setFill(Color.GRAY);

                double mx = (n1.getCenterX() + n2.getCenterX())/2;
                double my = (n1.getCenterY() + n2.getCenterY())/2;
                Label peso = new Label(String.valueOf(v.getPeso()));
                peso.setLayoutX(mx); peso.setLayoutY(my);

                grafoPane.getChildren().addAll(l, flecha, peso);
            }
        }
    }

    private Polygon crearFlecha(double x1, double y1, double x2, double y2) {
        double ang = Math.atan2(y2 - y1, x2 - x1);
        double len = 10;
        double angA = Math.toRadians(25);

        double xA = x2 - len * Math.cos(ang - angA);
        double yA = y2 - len * Math.sin(ang - angA);
        double xB = x2 - len * Math.cos(ang + angA);
        double yB = y2 - len * Math.sin(ang + angA);

        return new Polygon(x2, y2, xA, yA, xB, yB);
    }

    @FXML
    private void handleAnchorPaneClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        boolean clicEnNodo = false;
        for (Circle c : nodosVisuales.values()) {
            if (c.contains(x, y)) {
                clicEnNodo = true;
                break;
            }
        }

        if (!clicEnNodo) {
            abrirAgregarAeropuertoHandler(x, y);
        }
    }

    @FXML
    private void agregarVuelo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/AddVuelo.fxml"));
            Parent root = loader.load();
            AddVueloFXMLController vueloController = loader.getController();
            vueloController.setGrafo(grafo);

            Stage stage = new Stage();
            stage.setTitle("Agregar Vuelo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            dibujarGrafo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void eliminarVuelo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/EliminarVuelo.fxml"));
            Parent root = loader.load();

            EliminarVueloFXMLController controller = loader.getController();
            controller.setGrafo(grafo);

            Stage stage = new Stage();
            stage.setTitle("Eliminar Vuelo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            dibujarGrafo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void eliminarAeropuerto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/EliminarAeropuerto.fxml"));
            Parent root = loader.load();

            EliminarAeropuertoController controller = loader.getController();
            controller.setGrafo(grafo);

            Stage stage = new Stage();
            stage.setTitle("Eliminar Aeropuerto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            dibujarGrafo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void abrirPantallaVuelos(Aeropuerto aeropuerto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/MostrarVuelos.fxml"));
            Parent root = loader.load();

            MostrarVuelosFXMLController controller = loader.getController();
            controller.setDatos(aeropuerto, grafo);

            Stage stage = new Stage();
            stage.setTitle("Vuelos desde " + aeropuerto.getNombre());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void buscarRuta(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/BuscarRuta.fxml"));
            Scene scene = new Scene(loader.load());

            BuscarRutaFXMLController controller = loader.getController();
            controller.setGrafo(grafo);

            Stage stage = new Stage();
            stage.setTitle("Buscar Ruta");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(grafoPane.getScene().getWindow());
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void abrirEstadisticas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Estadisticas.fxml"));
            Parent root = loader.load();

            EstadisticasFXMLController controller = loader.getController();
            controller.setGrafo(grafo);

            Stage stage = new Stage();
            stage.setTitle("Estadísticas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(grafoPane.getScene().getWindow());
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Stub para cuando clicas en el fondo
    private void abrirAgregarAeropuertoHandler(double x, double y) {
        // aquí tu lógica para abrir ventana "Agregar Aeropuerto"
        System.out.println("Click en vacío: " + x + "," + y);
    }
}
