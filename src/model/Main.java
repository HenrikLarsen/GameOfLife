package model;

import controller.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This is the driver class for this implementation of Game of Life. It contains the start method from
 * Application, and the main method for the application.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
        primaryStage.setTitle("Conway's Game Of Life");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.getIcons().add(new Image(MainWindowController.class.getResourceAsStream("/resources/" +
                "images/GameofLife2.png")));
        primaryStage.show();

        //Makes sure that ThreadWorker's shutDownExecutor() method is called when JVM shuts down
        //to ensure that the ExecutorService is properly terminated.
        ThreadWorker workers = ThreadWorker.getInstance();
        primaryStage.setOnCloseRequest(event -> workers.shutDownExecutor());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> workers.shutDownExecutor()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}