package App;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.DatosVuelos;
import aeropuertovuelos.GrafoVuelos;
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
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
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
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {

    @FXML private AnchorPane grafoPane;
    private GrafoVuelos grafo;
    private Map<Aeropuerto, ImageView> nodosVisuales = new HashMap<>();
    private ImageView mapaView;
    private Group mapaGroup;
    private double lastX; // arrastrar el mapa
    private double lastY;

    private Image iconoAeropuerto;
    private Image iconoAvion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grafo = DatosVuelos.cargarDatos();
        if (grafo == null) grafo = new GrafoVuelos();
        mapaGroup = new Group();
        grafoPane.getChildren().add(mapaGroup);

        // Iconos
        iconoAeropuerto = new Image(getClass().getResourceAsStream("/resources/aeropuerto.png"));
        iconoAvion = new Image(getClass().getResourceAsStream("/resources/avion.png"));

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

        // Zoom con scroll centrado en el mouse
        grafoPane.setOnScroll(e -> {
            double zoomFactor = e.getDeltaY() > 0 ? 1.1 : 0.9;

            double oldScale = mapaGroup.getScaleX();
            double newScale = oldScale * zoomFactor;

            // Límites de zoom
            double minScale = 1.0;
            double maxScale = 5.0;

            if (newScale < minScale) newScale = minScale;
            else if (newScale > maxScale) newScale = maxScale;

            double f = (newScale / oldScale) - 1;
            Bounds bounds = mapaGroup.localToScene(mapaGroup.getBoundsInLocal());
            double dx = e.getSceneX() - (bounds.getMinX() + bounds.getWidth() / 2);
            double dy = e.getSceneY() - (bounds.getMinY() + bounds.getHeight() / 2);

            mapaGroup.setScaleX(newScale);
            mapaGroup.setScaleY(newScale);

            if (f != 0) {
                mapaGroup.setTranslateX(mapaGroup.getTranslateX() - f * dx);
                mapaGroup.setTranslateY(mapaGroup.getTranslateY() - f * dy);
            }

            e.consume();
        });

        // Arrastrar con mouse
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

        dibujarGrafo();
    }

    void dibujarGrafo() {
        mapaGroup.getChildren().clear();
        mapaGroup.getChildren().add(mapaView);
        nodosVisuales.clear();

        List<Aeropuerto> lista = new ArrayList<>(grafo.getAeropuertos());

        // Dibujar aeropuertos como imágenes
        for (Aeropuerto a : lista) {
            double cx = a.getX();
            double cy = a.getY();

            if (cx == 0 && cy == 0 && a.getLatitud() != 0 && a.getLongitud() != 0) {
                cx = convertirLongitudAX(a.getLongitud());
                cy = convertirLatitudAY(a.getLatitud());
                a.setX(cx);
                a.setY(cy);
            }

            ImageView nodo = new ImageView(iconoAeropuerto);
            nodo.setFitWidth(28);
            nodo.setFitHeight(28);
            nodo.setX(cx - 14);
            nodo.setY(cy - 14);

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

        // Dibujar vuelos con animación
        for (Aeropuerto origen : lista) {
            for (Vuelo v : grafo.getVuelosDesde(origen)) {
                Aeropuerto destino = v.getDestino();
                ImageView n1 = nodosVisuales.get(origen);
                ImageView n2 = nodosVisuales.get(destino);
                if (n1 == null || n2 == null) continue;

                double x1 = n1.getX() + n1.getFitWidth() / 2;
                double y1 = n1.getY() + n1.getFitHeight() / 2;
                double x2 = n2.getX() + n2.getFitWidth() / 2;
                double y2 = n2.getY() + n2.getFitHeight() / 2;

                Line l = new Line(x1, y1, x2, y2);
                l.setStrokeWidth(1.5);
                l.setStroke(Color.GRAY);

                Polygon flecha = crearFlecha(x1, y1, x2, y2);
                flecha.setFill(Color.GRAY);

                // Avioncito animado
                ImageView avion = new ImageView(iconoAvion);
                avion.setFitWidth(20);
                avion.setFitHeight(20);

                PathTransition animacion = new PathTransition();
                animacion.setNode(avion);
                animacion.setPath(l);
                animacion.setDuration(Duration.seconds(6));
                animacion.setCycleCount(PathTransition.INDEFINITE);
                animacion.setAutoReverse(false);
                animacion.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                animacion.play();

                double mx = (x1 + x2) / 2;
                double my = (y1 + y2) / 2;
                Label peso = new Label(String.valueOf(v.getPeso()));
                peso.setLayoutX(mx);
                peso.setLayoutY(my);

                mapaGroup.getChildren().addAll(l, flecha, peso, avion);
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
        Point2D punto = mapaGroup.sceneToLocal(event.getSceneX(), event.getSceneY());
        double x = punto.getX();
        double y = punto.getY();

        boolean clicEnNodo = false;
        for (ImageView iv : nodosVisuales.values()) {
            if (iv.getBoundsInParent().contains(x, y)) {
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
            DatosVuelos.guardarDatos(grafo);

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
    private void buscarRuta(ActionEvent event) throws IOException {
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
    }

    @FXML
    private void abrirEstadisticas(ActionEvent event) throws IOException {
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
