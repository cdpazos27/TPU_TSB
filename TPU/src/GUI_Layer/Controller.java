package GUI_Layer;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import Bussiness_Layer.*;

import javax.swing.*;
import java.io.File;

public class Controller {
    public Button btnRegiones;
    public Button btnCounts;
    public Button btnPostulaciones;
    public Button btnRead;
    public TextField txtFilePostulations;
    public TextField txtFileRegions;
    public TextField txtFileCount;

    public void btnRegionesClick(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archivos de Texto","*.dsv"));
        File selected = fc.showOpenDialog(null);
        if (selected != null) {
            txtFileRegions.setText(selected.getAbsolutePath());
        }
    }

    public void btnConteos_Click(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archivos de Texto","*.dsv"));
        File selected = fc.showOpenDialog(null);
        if (selected != null) {
            txtFileCount.setText(selected.getAbsolutePath());
        }
    }

    public void btnPostulaciones_click(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archivos de Texto","*.dsv"));
        File selected = fc.showOpenDialog(null);
        if (selected != null) {
            txtFilePostulations.setText(selected.getAbsolutePath());
        }
    }

    public void cboCircuitoSelection(ActionEvent actionEvent) {
    }

    public void cboDistritoSelection(ActionEvent actionEvent) {
    }

    public void cboRegionSelection(ActionEvent actionEvent) {
    }

    public void cboCandidatoSelection(ActionEvent actionEvent) {
    }

    public void btnContarClick(ActionEvent actionEvent)
    {
        JOptionPane.showMessageDialog(null, "La cantidad de votos es: " + Math.random());
    }

    public void btnReadClick(ActionEvent actionEvent)
    {
        // Comunicarse con el gestorConteoVotos para que este lea todos los archivos y haga un parsing de las regiones, distritos, circuitos y candidatos
    }
}
