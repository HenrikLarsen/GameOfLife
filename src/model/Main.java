package model;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/GUI.fxml"));
        primaryStage.setTitle("GameOfLife");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
        //ThreadWorker workers = ThreadWorker.getInstance();
        //primaryStage.setOnCloseRequest(event -> workers.shutDownExecutor());
    }
    public static void main(String[] args) {
        launch(args);
    }
}


//TODO: Gjør stagen non-resizable evnt sett ny størrelse osv
//TODO: Spør om hvordan man strukturerer innad i MVC
//TODO: Spør om hvordan man closer executorService overalt.
//TODO: Sjekk på RejectedExecutionException når man ikke pauser timeline