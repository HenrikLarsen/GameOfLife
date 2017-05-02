package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/GUI.fxml"));
        primaryStage.setTitle("GameOfLife");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();

        //Makes sure that ThreadWorker's shutDownExecutor() method is called when JVM shuts down
        //to ensure that the ExecutorService is properly terminated.
        ThreadWorker workers = ThreadWorker.getInstance();
        primaryStage.setOnCloseRequest(event -> workers.shutDownExecutor());
        Runtime.getRuntime().addShutdownHook(new Thread() {public void run() {workers.shutDownExecutor();}});
    }

    public static void main(String[] args) {
        launch(args);
    }
}
