package GUI_Layer;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import Bussiness_Layer.*;
import Entities.*;
import javax.swing.*;
import java.io.File;
import java.util.*;

public class Controller {
    public Button btnRegiones;
    public Button btnCounts;
    public Button btnPostulaciones;
    public Button btnRead;
    public TextField txtFilePostulations;
    public TextField txtFileRegions;
    public TextField txtFileCount;
    public ComboBox cboDistrito;
    public ComboBox cboCandidato;
    public Button btnContar;
    public ComboBox cboSecciones;
    public ComboBox cboCircuito;
    private static Gestor gestor2;
    public Button btnClear;
    public CheckBox ckTodos;
    public Button btnSalir;
    private boolean loaded=false;

    public void btnRegionesClick(ActionEvent actionEvent) {


            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archivos de Texto", "*.dsv"));
            File selected = fc.showOpenDialog(null);
            if (selected != null) {
                txtFileRegions.setText(selected.getAbsolutePath());
            }

    }

    public void btnConteos_Click(ActionEvent actionEvent) {

            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archivos de Texto", "*.dsv"));
            File selected = fc.showOpenDialog(null);
            if (selected != null) {
                txtFileCount.setText(selected.getAbsolutePath());
            }


    }

    public void btnPostulaciones_click(ActionEvent actionEvent) {

            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Archivos de Texto", "*.dsv"));
            File selected = fc.showOpenDialog(null);
            if (selected != null) {
                txtFilePostulations.setText(selected.getAbsolutePath());
            }

    }

    public void cboCircuitoSelection(ActionEvent actionEvent) {
        cboSecciones.setValue(null);
        cboDistrito.setValue(null);
        cboSecciones.setDisable(true);
        cboDistrito.setDisable(true);
    }

    public void btnContarClick(ActionEvent actionEvent)
    {
        if (!(ckTodos.isSelected())) {
            int cantidad = 0;
            if (cboCandidato.getValue() != null) {
                if (cboDistrito.getValue() == null && cboSecciones.getValue() == null && cboCircuito.getValue() == null) {
                    //filtrar solo por candidato
                    String cod = cboCandidato.getValue().toString().split(" ")[0];
                    cantidad = gestor2.contarVotosPorAgrupacion(cod);

                } else {
                    // fitrar por candidato y...
                    if (cboCircuito.getValue() != null) {
                        cantidad = gestor2.contarVotosPorCircuito(cboCircuito.getValue().toString(), cboCandidato.getValue().toString());

                    } else {
                        if (cboSecciones.getValue() != null) {
                            // por Seccion
                            cantidad = gestor2.contarVotosPorSeccion(cboSecciones.getValue().toString(), cboCandidato.getValue().toString());
                        } else {
                            // por Distrito
                            cantidad = gestor2.contarVotosPorDistrito(cboDistrito.getValue().toString(), cboCandidato.getValue().toString());

                        }
                    }
                }
                String agro = cboCandidato.getValue().toString().split(" ")[1];
                JOptionPane.showMessageDialog(null, "La cantidad de votos para " + cboCandidato.getValue() + " es: " + cantidad);
            }
        }
        else
        {
            List<Agrupacion> resultados = gestor2.contarTodos();
            String show = "Lista de conteo de votos totales para cada agrupación en candidato a presidente: ";
            for (Agrupacion a: resultados
                 ) {
                show += "\n " + a.getNombre() + " -> " + a.getVotos() + " votos";
            }
            JOptionPane.showMessageDialog(null, show);
        }
        }



    public void btnReadClick(ActionEvent actionEvent)
    {
        // Comunicarse con el gestorConteoVotos para que este lea todos los archivos y haga un parsing de las regiones, distritos, circuitos y candidatos
        if (txtFilePostulations.getText() == "" && txtFileCount.getText() == "" & txtFileRegions.getText() == "") {
            JOptionPane.showMessageDialog(null, "Faltan cargarse archivos.");
            return;}
        else {
            try {
                gestor2 = new Gestor();
                gestor2.obtenerCandidatos(txtFilePostulations.getText());
                gestor2.obtenerDistritos(txtFileRegions.getText());
                cargarComboAgrupaciones(gestor2.getCandidatos());
                cargarRegiones(gestor2.getDistritos().getDistritos().values());
                gestor2.cargarConteoVotos(txtFileCount.getText());
                loaded = true;
                btnPostulaciones.setDisable(true);
                btnRead.setDisable(true);
                btnRegiones.setDisable(true);
                btnCounts.setDisable(true);
                cboCandidato.setDisable(false);
                cboCircuito.setDisable(false);
                cboSecciones.setDisable(false);
                cboDistrito.setDisable(false);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error \n Los archivos ingresados no tienen el formato correcto");
            }
        }
    }

    private void cargarRegiones(Collection<Distrito> set) {
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Distrito d = (Distrito) it.next();
            cboDistrito.getItems().add(d.getCodigo() + " " + d.getNombre());
            Iterator it2 = d.getSecciones().getSecciones().values().iterator();
            while (it2.hasNext()) {
                Seccion s = (Seccion) it2.next();
                cboSecciones.getItems().add(s.getCodigo() + " " + s.getNombre());
                Iterator it3 = s.getCircuitos().getCircuitos().values().iterator();
                while (it3.hasNext()) {
                    Circuito c = (Circuito) it3.next();
                    cboCircuito.getItems().add(c.getCodigo() + " " + c.getNombre());
                }
            }
        }
    }

    private void cargarComboAgrupaciones(List<String> list) {
        if (!(list.isEmpty())) {
            for (String elem: list
                 ) {
                cboCandidato.getItems().add(elem);
            }
        }
    }


    public void cboSeccionSelection(ActionEvent actionEvent) {
        if (cboSecciones.getValue() != null) {
            cboCircuito.getItems().clear();
            cboDistrito.setValue(null);
            String[] lectura = cboSecciones.getValue().toString().split(" ");
            String codDistrito = lectura[0].substring(0, 2);
            String codigoSeccion = lectura[0];
            Collection<Circuito> circ = gestor2.filtrarCircuitos(codigoSeccion, codDistrito);
            cboDistrito.setDisable(true);
            Iterator<Circuito> it = circ.iterator();
            while (it.hasNext()) {
                Circuito c = it.next();
                cboCircuito.getItems().add(c.getCodigo() + " " + c.getNombre());
            }

        }
    }

    public void cboDistritoSelection(ActionEvent actionEvent) {
        if (cboDistrito.getValue() != null) {
            cboSecciones.getItems().clear();
            cboCircuito.getItems().clear();
            String codigo;
            String[] s = cboDistrito.getValue().toString().split(" ");
            codigo = s[0];
            Collection<Seccion> col = gestor2.filtrarSecciones(codigo);
            Iterator it = col.iterator();
            while (it.hasNext()) {
                Seccion seccion = (Seccion) it.next();
                cboSecciones.getItems().add(seccion.getCodigo() + " " + seccion.getNombre());
                Iterator it2 = seccion.getCircuitos().getCircuitos().values().iterator();
                while (it2.hasNext()) {
                    Circuito c = (Circuito) it2.next();
                    cboCircuito.getItems().add(c.getCodigo() + " " + c.getNombre());
                }
            }
        }
    }

    public void cboCandidatoSelection(ActionEvent actionEvent) {
        btnContar.setDisable(false);
    }

    public void btnClear_Click(ActionEvent actionEvent) {
        if (loaded) {
            cboDistrito.getItems().clear();
            cboSecciones.getItems().clear();
            cboCircuito.getItems().clear();
            cargarRegiones(gestor2.getDistritos().getDistritos().values());
            cboSecciones.setDisable(false);
            cboDistrito.setDisable(false);
            cboCircuito.setDisable(false);
            cboCandidato.setValue(null);
            btnContar.setDisable(true);
            ckTodos.setSelected(false);
            cboCandidato.setDisable(false);
        }
    }

    public void ckCheched(ActionEvent actionEvent) {
        if (ckTodos.isSelected()) {
            if (loaded) {
                btnContar.setDisable(false);
                cboCircuito.setDisable(true);
                cboSecciones.setDisable(true);
                cboDistrito.setDisable(true);
                cboCandidato.setDisable(true);
            }
        }
        else
        {
            if (loaded) {btnContar.setDisable(true);
                cboCandidato.setDisable(false);
                cboSecciones.setDisable(false);
                cboDistrito.setDisable(false);
                cboCircuito.setDisable(false);}
        }

    }

    public void btnSalir_Click(ActionEvent actionEvent) {
        System.exit(0);
    }
}

