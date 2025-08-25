package App;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.DatosVuelos;
import aeropuertovuelos.GrafoVuelos;
import aeropuertovuelos.Utilitarios;
import aeropuertovuelos.Vuelo;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    private Group mapaGroup;
    private double lastX; //arrastrar el mapa
    private double lastY;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grafo = DatosVuelos.cargarDatos();
        if (grafo == null) grafo = new GrafoVuelos();
        mapaGroup = new Group();
        grafoPane.getChildren().add(mapaGroup);

        // Imagen de fondo del mapa
        mapaView = new ImageView(new Image(getClass().getResourceAsStream("/resources/mapa.png")));
        mapaView.setPreserveRatio(true);
        mapaView.setFitWidth(grafoPane.getWidth());
        mapaView.setFitHeight(grafoPane.getHeight());
        mapaView.setOpacity(0.25);
        mapaGroup.getChildren().add(mapaView);

        // Ajustar cuando cambie el tamaño
        grafoPane.widthProperty().addListener((obs, oldVal, newVal) -> mapaView.setFitWidth(newVal.doubleValue()));
        grafoPane.heightProperty().addListener((obs, oldVal, newVal) -> mapaView.setFitHeight(newVal.doubleValue()));

        // Zoom con scroll
        grafoPane.setOnScroll(e -> {
            double zoomFactor = e.getDeltaY() > 0 ? 1.1 : 0.9;
            mapaGroup.setScaleX(mapaGroup.getScaleX() * zoomFactor);
            mapaGroup.setScaleY(mapaGroup.getScaleY() * zoomFactor);
            e.consume();
        });

        // Arrastrar el mapa
        mapaGroup.setOnMousePressed(e -> {
            lastX = e.getSceneX();
            lastY = e.getSceneY();
        });
        mapaGroup.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - lastX;
            double dy = e.getSceneY() - lastY;
            mapaGroup.setLayoutX(mapaGroup.getLayoutX() + dx);
            mapaGroup.setLayoutY(mapaGroup.getLayoutY() + dy);
            lastX = e.getSceneX();
            lastY = e.getSceneY();
        });
        
        
        
        dibujarGrafo();
    }

    private void habilitarArrastre() {
        mapaGroup.setOnMousePressed(e -> {
            lastX = e.getSceneX();
            lastY = e.getSceneY();
        });

        mapaGroup.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - lastX;
            double dy = e.getSceneY() - lastY;

            mapaGroup.setTranslateX(mapaGroup.getTranslateX() + dx);
            mapaGroup.setTranslateY(mapaGroup.getTranslateY() + dy);

            lastX = e.getSceneX();
            lastY = e.getSceneY();
        });
    }

    void dibujarGrafo() {
        // Limpia nodos y aristas anteriores (excepto la imagen de fondo)
        mapaGroup.getChildren().removeIf(node -> node != mapaView && !(node instanceof Circle && nodosVisuales.containsValue(node)) && !(node instanceof Line || node instanceof Polygon || node instanceof Label));
        nodosVisuales.clear();

        List<Aeropuerto> lista = new ArrayList<>(grafo.getAeropuertos());

        for (Aeropuerto a : lista) {
            double cx = a.getX();
            double cy = a.getY();

            // Si es la primera vez que se agrega y tiene lat/lon
            if (cx == 0 && cy == 0 && a.getLatitud() != 0 && a.getLongitud() != 0) {
                cx = convertirLongitudAX(a.getLongitud());
                cy = convertirLatitudAY(a.getLatitud());
                a.setX(cx);
                a.setY(cy);
            }

            Circle nodo = new Circle(cx, cy, 14, Color.CORNFLOWERBLUE);
            Tooltip.install(nodo, new Tooltip(a.getNombre() + " (" + a.getCodigo() + ")\n" + a.getCiudad() + ", " + a.getPais()));
            nodo.setOnMouseClicked(e -> {
                try {
                    abrirPantallaVuelos(a);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            mapaGroup.getChildren().add(nodo);
            nodosVisuales.put(a, nodo);
        }

        // Dibujar aristas y flechas
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

                double mx = (n1.getCenterX() + n2.getCenterX()) / 2;
                double my = (n1.getCenterY() + n2.getCenterY()) / 2;
                Label peso = new Label(String.valueOf(v.getPeso()));
                peso.setLayoutX(mx);
                peso.setLayoutY(my);

                mapaGroup.getChildren().addAll(l, flecha, peso);
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
    private void handleAnchorPaneClick(MouseEvent event) throws IOException {
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
    private void agregarVuelo(ActionEvent event) throws IOException {
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
    }

    @FXML
    private void eliminarVuelo(ActionEvent event) throws IOException {
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
    }

    @FXML
    private void eliminarAeropuerto(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/EliminarAeropuerto.fxml"));
            Parent root = loader.load();

            EliminarAeropuertoController controller = loader.getController();
            controller.setGrafo(grafo);
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Eliminar Aeropuerto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            dibujarGrafo();
    }

    private void abrirPantallaVuelos(Aeropuerto aeropuerto) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/MostrarVuelos.fxml"));
            Parent root = loader.load();

            MostrarVuelosFXMLController controller = loader.getController();
            controller.setDatos(aeropuerto, grafo);

            Stage stage = new Stage();
            stage.setTitle("Vuelos desde " + aeropuerto.getNombre());
            stage.setScene(new Scene(root));
            stage.show();
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
    private void abrirAgregarAeropuertoHandler(double x, double y) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/AddAeropuertoFXML.fxml"));
        Parent root = loader.load();

        AddAeropuertoFXMLController controller = loader.getController();
        controller.setGrafo(grafo);
        controller.setMainController(this);
        controller.setPosicion(x, y);

        Stage stage = new Stage();
        stage.setTitle("Agregar Aeropuerto");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        dibujarGrafo();
    }
    
   
    private double convertirLongitudAX(double lon) {
        double width = mapaView.getFitWidth();
        return (lon + 180) * (width / 360.0);
    }

    private double convertirLatitudAY(double lat) {
        double height = mapaView.getFitHeight();
        return (90 - lat) * (height / 180.0);
    }
    
    public double getWidthGrafoPane() {
        return grafoPane.getWidth();
    }

    public double getHeightGrafoPane() {
        return grafoPane.getHeight();
    }
}
