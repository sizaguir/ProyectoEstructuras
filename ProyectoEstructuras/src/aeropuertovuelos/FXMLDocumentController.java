/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package aeropuertovuelos;

import java.io.IOException;
import java.net.URL;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author arife
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private MenuButton aeropuertoMenu;
    @FXML
    private MenuItem añadirAero;
    @FXML
    private MenuItem eliminarAero;
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
    
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void añadirVueloHandler(ActionEvent event) {
    }

    @FXML
    private void editarVueloHandler(ActionEvent event) {
    }

    @FXML
    private void eliminarVueloHandler(ActionEvent event) {
    }

    @FXML
    private void buscarRuta(ActionEvent event) {
    }

    @FXML
    private void abrirAgregarAeropuertoHandler(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAeropuertoFXML.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Agregar Aeropuerto");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    @FXML
    private void abrirEliminarAeroHandler(ActionEvent event) {
    }
    
}
