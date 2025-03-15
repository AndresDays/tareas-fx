package gm.tareas.presentacion;

import gm.tareas.TareasApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class SistemaTareasFx extends Application {

    private ConfigurableApplicationContext contextoSpring;

    /*public static void main(String[] args) {
        launch(args);
    }*/

    @Override
    public void init(){
        this.contextoSpring =
                new SpringApplicationBuilder(TareasApplication.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader =
                new FXMLLoader(TareasApplication.class.getResource("/templates/index.fxml"));
        loader.setControllerFactory(contextoSpring::getBean);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        contextoSpring.close();
        Platform.exit();
    }
}
