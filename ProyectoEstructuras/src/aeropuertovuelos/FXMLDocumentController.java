/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package aeropuertovuelos;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author arife
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private MenuButton vueloMenu;
    @FXML
    private MenuItem añadirVuelo;
    @FXML
    private MenuItem editarVuelo;
    @FXML
    private MenuItem eliminarVuelo;
    @FXML
    private Button buscarRutaButtom;
    @FXML
    private AnchorPane grafoPane;
    private GrafoVuelos grafo;
    private Map<Aeropuerto, Circle> nodosVisuales = new HashMap<>();
    private Aeropuerto origenSeleccionado;
    private Aeropuerto destinoSeleccionado;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grafo = DatosVuelos.cargarDatos(); //Carga aeropuertos y vuelos desde los archivos
        dibujarGrafo(); // Función que dibuja nodos y líneas en el AnchorPane
    }    

    private void abrirAgregarAeropuertoHandler(double posX, double posY) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAeropuertoFXML.fxml"));
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
        } catch (IOException e) {
        e.printStackTrace();
        }
    }
    
    private void dibujarGrafo(){
        grafoPane.getChildren().clear();
        nodosVisuales.clear();

        double defaultX = 50, defaultY = 50;
        double deltaX = 120, deltaY = 80;
        int i = 0;

        for (Aeropuerto a : grafo.getAeropuertos()) {
            double cx = a.getX();
            double cy = a.getY();

            // Si el nodo no tiene posición, asignamos una por defecto
            if (cx == 0 && cy == 0) {
                cx = defaultX + (i % 5) * deltaX;
                cy = defaultY + (i / 5) * deltaY;
                a.setX(cx);
                a.setY(cy);
                i++;
            }

            Circle nodo = new Circle(cx, cy, 15, Color.BLUE);
            nodo.setOnMouseClicked(e -> manejarClickNodo(a, nodo));

            grafoPane.getChildren().add(nodo);
            nodosVisuales.put(a, nodo);
        }

        for (Aeropuerto origen : grafo.getAeropuertos()){
            for (Vuelo v : grafo.getVuelosDesde(origen)){
                Circle cOrigen = nodosVisuales.get(origen);
                Circle cDestino = nodosVisuales.get(v.getDestino());
                if (cOrigen != null && cDestino != null){
                    Line linea = new Line(cOrigen.getCenterX(), cOrigen.getCenterY(),
                                          cDestino.getCenterX(), cDestino.getCenterY());
                    linea.setStrokeWidth(2);
                    linea.setStroke(Color.GRAY);
                    grafoPane.getChildren().add(linea);
                }
            }
        }
    }
    
    private void manejarClickNodo(Aeropuerto aeropuerto, Circle nodo){
        if (origenSeleccionado == null){
            origenSeleccionado = aeropuerto;
            nodo.setFill(Color.RED); // Nodo origen
        } else if (destinoSeleccionado == null && aeropuerto != origenSeleccionado){
            destinoSeleccionado = aeropuerto;
            nodo.setFill(Color.ORANGE); // Nodo destino

            abrirAgregarVuelo(origenSeleccionado, destinoSeleccionado);

            origenSeleccionado = null;
            destinoSeleccionado = null;
            dibujarGrafo(); // Redibuja todo para resetear colores
        }
    }
    
    private void abrirAgregarVuelo(Aeropuerto origen, Aeropuerto destino) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddVuelo.fxml"));
            Parent root = loader.load();
            AddVueloFXMLController vueloController = loader.getController();
            vueloController.setGrafo(grafo);
            vueloController.setOrigenDestino(origen, destino);

            Stage stage = new Stage();
            stage.setTitle("Agregar Vuelo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            dibujarGrafo(); // Redibuja las aristas actualizadas
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}
