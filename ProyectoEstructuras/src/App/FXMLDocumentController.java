/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package App;

import App.BuscarRutaFXMLController;
import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.DatosVuelos;
import aeropuertovuelos.GrafoVuelos;
import aeropuertovuelos.Utilitarios;
import aeropuertovuelos.Vuelo;
import java.awt.Color;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author arife
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private MenuItem añadirVuelo;
    @FXML
    private AnchorPane grafoPane;
    private GrafoVuelos grafo;
    private Map<Aeropuerto, Circle> nodosVisuales = new HashMap<>();
    @FXML
    private MenuItem eliminarVuelo;
    @FXML
    private MenuItem eliminarAeropuerto;
    @FXML
    private Menu buscarRuta;
    @FXML
    private MenuItem editarVuelo;

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grafo = DatosVuelos.cargarDatos(); //Carga aeropuertos y vuelos desde los archivos
        dibujarGrafo(); // Función que dibuja nodos y líneas en el AnchorPane
    }    

    private void abrirAgregarAeropuertoHandler(double posX, double posY) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/AddAeropuertoFXML.fxml"));
        Parent root = loader.load();
        AddAeropuertoFXMLController addController = loader.getController();
        addController.setGrafo(grafo);
        addController.setMainController(this);
        addController.setPosicion(posX, posY);
        
        Stage stage = new Stage();
        stage.setTitle("Agregar Aeropuerto");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        
        dibujarGrafo();
    }
//    //con metodo de utilitarios
//    private void dibujarGrafo() {
//        grafoPane.getChildren().clear();
//        nodosVisuales.clear();
//
//        // Paso 1: aplicar distribución de aeropuertos
//        List<Aeropuerto> lista = new ArrayList<>(grafo.getAeropuertos());
//
//        //  Usa coordenadas geográficas escaladas al tamaño del AnchorPane
//        Utilitarios.distribuirPorCoordenadas(lista, grafoPane.getWidth(), grafoPane.getHeight());
//
//        // Si quieres probar en círculo:
//        // Utilitarios.distribuirEnCirculo(lista, grafoPane.getWidth() / 2, grafoPane.getHeight() / 2, 200);
//
//        // Paso 2: Dibujar nodos (aeropuertos)
//        for (Aeropuerto a : lista) {
//            double cx = a.getX();
//            double cy = a.getY();
//
//            Circle nodo = new Circle(cx, cy, 15, Color.CORNFLOWERBLUE);
//            nodo.setOnMouseClicked(e -> {
//                abrirPantallaVuelos(a);  // abre los vuelos de ese aeropuerto
//                e.consume();
//            });
//
//            grafoPane.getChildren().add(nodo);
//            nodosVisuales.put(a, nodo);
//        }
//
//        // Paso 3: Dibujar aristas (vuelos con flechas)
//        for (Aeropuerto origen : grafo.getAeropuertos()) {
//            for (Vuelo v : grafo.getVuelosDesde(origen)) {
//                Circle cOrigen = nodosVisuales.get(origen);
//                Circle cDestino = nodosVisuales.get(v.getDestino());
//                if (cOrigen != null && cDestino != null) {
//                    double x1 = cOrigen.getCenterX();
//                    double y1 = cOrigen.getCenterY();
//                    double x2 = cDestino.getCenterX();
//                    double y2 = cDestino.getCenterY();
//
//                    // Ajustar salida/entrada en borde del círculo
//                    double dx = x2 - x1;
//                    double dy = y2 - y1;
//                    double dist = Math.sqrt(dx * dx + dy * dy);
//                    double r = cOrigen.getRadius();
//                    double r2 = cDestino.getRadius();
//                    double startX = x1 + dx * r / dist;
//                    double startY = y1 + dy * r / dist;
//                    double endX = x2 - dx * r2 / dist;
//                    double endY = y2 - dy * r2 / dist;
//
//                    // Línea
//                    Line linea = new Line(startX, startY, endX, endY);
//                    linea.setStrokeWidth(2);
//                    linea.setStroke(Color.GRAY);
//                    grafoPane.getChildren().add(linea);
//
//                    // Flecha
//                    double angle = Math.atan2(endY - startY, endX - startX);
//                    double arrowLength = 12;
//                    double arrowWidth = 6;
//
//                    Polygon arrowHead = new Polygon();
//                    arrowHead.getPoints().addAll(
//                        0.0, 0.0,
//                        -arrowLength, -arrowWidth / 2,
//                        -arrowLength, arrowWidth / 2
//                    );
//                    arrowHead.setFill(Color.GRAY);
//
//                    arrowHead.setLayoutX(endX);
//                    arrowHead.setLayoutY(endY);
//                    arrowHead.setRotate(Math.toDegrees(angle));
//
//                    grafoPane.getChildren().add(arrowHead);
//                }
//            }
//        }
//    }

    
    
    private void dibujarGrafo() {
        grafoPane.getChildren().clear();
        nodosVisuales.clear();

        double defaultX = 50, defaultY = 50;
        double deltaX = 120, deltaY = 80;
        int i = 0;

        // Dibujar nodos
        for (Aeropuerto a : grafo.getAeropuertos()) {
            double cx = a.getX();
            double cy = a.getY();

            if (cx == 0 && cy == 0) {
                cx = defaultX + (i % 5) * deltaX;
                cy = defaultY + (i / 5) * deltaY;
                a.setX(cx);
                a.setY(cy);
                i++;
            }

            Circle nodo = new Circle(cx, cy, 15, Color.CORNFLOWERBLUE);
            
            nodo.setOnMouseClicked(e -> {
                try {//try/catch obligatorio
                    abrirPantallaVuelos(a);//aquí llama al método
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            e.consume();
            });

            grafoPane.getChildren().add(nodo);
            nodosVisuales.put(a, nodo);


            // Dibujar aristas con flecha
            for (Aeropuerto origen : grafo.getAeropuertos()) {
                for (Vuelo v : grafo.getVuelosDesde(origen)) {
                    Circle cOrigen = nodosVisuales.get(origen);
                    Circle cDestino = nodosVisuales.get(v.getDestino());
                    if (cOrigen != null && cDestino != null) {
                        double x1 = cOrigen.getCenterX();
                        double y1 = cOrigen.getCenterY();
                        double x2 = cDestino.getCenterX();
                        double y2 = cDestino.getCenterY();

                        // Ajustar para que línea salga del borde del círculo
                        double dx = x2 - x1;
                        double dy = y2 - y1;
                        double dist = Math.sqrt(dx*dx + dy*dy);
                        double r = cOrigen.getRadius();
                        double r2 = cDestino.getRadius();
                        double startX = x1 + dx * r / dist;
                        double startY = y1 + dy * r / dist;
                        double endX = x2 - dx * r2 / dist;
                        double endY = y2 - dy * r2 / dist;

                        // Línea
                        Line linea = new Line(startX, startY, endX, endY);
                        linea.setStrokeWidth(2);
                        linea.setStroke(Color.GRAY);
                        grafoPane.getChildren().add(linea);

                        // Flecha
                        double angle = Math.atan2(endY - startY, endX - startX);
                        double arrowLength = 12; 
                        double arrowWidth = 6;   

                        Polygon arrowHead = new Polygon();
                        arrowHead.getPoints().addAll(
                            0.0, 0.0,
                            -arrowLength, -arrowWidth / 2,
                            -arrowLength, arrowWidth / 2
                        );
                        arrowHead.setFill(Color.GRAY);

                        arrowHead.setLayoutX(endX);
                        arrowHead.setLayoutY(endY);
                        arrowHead.setRotate(Math.toDegrees(angle));

                        grafoPane.getChildren().add(arrowHead);
                    }
                }
            }
        }
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

        dibujarGrafo(); // Redibuja aristas nuevas
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

        // Pasar datos al controlador de MostrarVuelos
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

        // Pasamos el grafo a la ventana BuscarRuta
        BuscarRutaFXMLController controller = loader.getController();
        controller.setGrafo(grafo);

        Stage stage = new Stage();
        stage.setTitle("Buscar Ruta");
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);

        // Usamos el grafoPane (que sí es un Node visible en la escena principal)
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
}