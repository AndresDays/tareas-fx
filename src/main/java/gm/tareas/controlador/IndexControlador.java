package gm.tareas.controlador;

import gm.tareas.modelo.Tarea;
import gm.tareas.servicio.TareaServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class IndexControlador implements Initializable {
    private static final Logger logger =
            LoggerFactory.getLogger(IndexControlador.class);

    @Autowired
    private TareaServicio tareaServicio;

    @FXML
    private TableView<Tarea> tareaTabla;

    @FXML
    private TableColumn<Tarea, Integer> idTareaColumna;

    @FXML
    private TableColumn<Tarea,String> nombreColumna;

    @FXML
    private TableColumn<Tarea,String> responsableColumna;

    @FXML
    private TableColumn<Tarea, String> estatusColumna;

    private final ObservableList<Tarea> tareaLista = FXCollections.observableArrayList();

    @FXML
    private TextField tareaTxt;

    @FXML
    private TextField responsableTxt;

    @FXML
    private TextField estatusTxt;

    private Integer idTareaInterno;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Ejecutando listado de Tareas");
        tareaTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configurarColumnas();
        listarTareas();
    }

    private void configurarColumnas(){
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("idTarea"));
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        responsableColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        estatusColumna.setCellValueFactory(new PropertyValueFactory<>("estatus"));
    }

    private void listarTareas(){
        tareaLista.clear();
        tareaLista.addAll(tareaServicio.listarTareas());
        tareaTabla.setItems(tareaLista);
    }

    public void agregarTarea(){
        if (tareaTxt.getText().isEmpty()){
            mostrarMensaje("Error Validacion", "Debe proporcionar un nombre");
            tareaTxt.requestFocus();
            return;
        }else {
            var tarea = new Tarea();
            recolectarDatosFormulario(tarea);
            tarea.setIdTarea(null);
            tareaServicio.guardarTarea(tarea);
            mostrarMensaje("Informacion","Tarea agregada");
            limpiarFormulario();
            listarTareas();
        }
    }

    public void cargarTareaFormulario(){
        var tarea = tareaTabla.getSelectionModel().getSelectedItem();
        if (tarea != null){
            idTareaInterno = tarea.getIdTarea();
            tareaTxt.setText(tarea.getNombreTarea());
            responsableTxt.setText(tarea.getResponsable());
            estatusTxt.setText(tarea.getEstatus());
        }
    }

    private void recolectarDatosFormulario(Tarea tarea){
        if (idTareaInterno != null)
            tarea.setIdTarea(idTareaInterno);
        tarea.setNombreTarea(tareaTxt.getText());
        tarea.setResponsable(responsableTxt.getText());
        tarea.setEstatus(estatusTxt.getText());
    }

    public void modificarTarea(){
        if (idTareaInterno == null){
            mostrarMensaje("Informacion", "Debe seleccionar una tarea");
            return;
        }
        if (tareaTxt.getText().isEmpty()){
            mostrarMensaje("Error Validacion","Debe proporciona una tarea");
            tareaTxt.requestFocus();
            return;
        }
        var tarea = new Tarea();
        recolectarDatosFormulario(tarea);
        tareaServicio.guardarTarea(tarea);
        mostrarMensaje("Informacion","Tarea Modificada");
        limpiarFormulario();
        listarTareas();
    }

    public void eliminarTarea(){
        if (idTareaInterno == null){
            mostrarMensaje("Informacion", "Debe seleccionar una tarea");
            return;
        }
        if (tareaTxt.getText().isEmpty()){
            mostrarMensaje("Error Validacion","Debe proporciona una tarea");
            tareaTxt.requestFocus();
            return;
        }
        var tarea = new Tarea();
        recolectarDatosFormulario(tarea);
        tareaServicio.eliminarTarea(tarea);
        mostrarMensaje("Informacion","Tarea eliminada: " + tarea.getIdTarea());
        limpiarFormulario();
        listarTareas();
    }

    private void limpiarFormulario(){
        idTareaInterno = null;
        tareaTxt.clear();
        responsableTxt.clear();
        estatusTxt.clear();
    }

    public void limpiarForma(){
        limpiarFormulario();
    }

    private void mostrarMensaje(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
