package controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.GameOfLife;
import model.Statistics;
import java.io.IOException;


/**
 * The ProgressController handles all interaction within the progressbar window. It is responsible for displaying
 * and updating a progress bar while calculating the generations and statistics when the user opens the
 * statistics window. It contains the methods and parameters linked to the graphical user interface elements. It also
 * contains two nested classes, MyTask and TaskService for running tasks concurrently.
 *
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class ProgressController implements Initializable {
    @FXML GridPane gridPane;
    @FXML private Label progressLabel;
    @FXML private ProgressBar progressBar;

    private int[][] stat;
    private int iterations;

    private GameOfLife gameOfLife;
    private final Statistics statistics = new Statistics();
    private TaskService taskService;

    /**
     * A concrete implementation of the method in initialize.
     * Initializes the progressbar window, binding the GUI with the taskService.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     * @see #progressLabel
     * @see #progressBar
     */
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        taskService = new TaskService();
        if (!progressLabel.textProperty().isBound())
        {
            // bind the messageProperty to a TextArea in JavaFX
            progressLabel.textProperty().bind(taskService.messageProperty());

            // bind the progressProperty to a ProgressBar in JavaFX
            progressBar.progressProperty().bind(taskService.progressProperty());
        }
        taskService.start();
    }

    /**
     * Method which handles the Cancel button click action.
     */
    public void cancelClick() {
        taskService.cancel();
    }

    /**
     * Method that sets the current Game of Life object.
     * @param gOL - The GameOfLife object to be considered.
     * @see #gameOfLife
     */
    public void setGameOfLife(GameOfLife gOL){
        this.gameOfLife = gOL;
    }

    /**
     * Method that sets the number of iterations to be measured and drawn.
     * @param i - The number of iterations to be considered.
     * @see #iterations
     */
    public void setIterations(int i){
        this.iterations = i;
    }

    /**
     * MyTask is a nested class within the ProgressController that extends the Task class in the javafx.concurrent
     * package. The class is invoked on the background thread, and represents the task to be done. The class is
     * nested because it is only used within the ProgressController.
     *
     * @author Henrik Finnerud Larsen
     * @version 1.0
     **/
    private class MyTask extends Task<Void> {

        /**
         * This Method will run when a MyTask object is created. Updates the progressbar and progressLabel, while
         * getting the statistics.
         */
        @Override
        public Void call() {
            final int max = 100;
            for (int i = 1; i <= max; i++) {
                if (isCancelled()){
                    break;
                }

                do{
                    stat = statistics.getStatistics(gameOfLife, iterations);
                }while(i == 0);

                updateMessage("Loading.." + " " + i + "%");
                updateProgress(i, max);
            }
            return null;
        }

        /**
         * This Method will run if the task is succeeded. Opens the statistics window.
         * for the user to choose how many iterations to show statistics for.
         */
        @Override
        protected void succeeded() {
            super.succeeded();
            try{
                Stage statisticStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Statistics.fxml"));
                Parent root = loader.load();
                StatisticsController statisticsController = loader.getController();

                //Sets the statistics data to be considered, and creates a chart of the results.
                statisticsController.setStat(stat);
                statisticsController.makeChart();

                //Opens and waits
                statisticStage.setTitle("GameOfLife statistics");
                statisticStage.setScene(new Scene(root, 800, 600));
                statisticStage.showAndWait();
            }catch (IOException ioe){
                //Shows a warning should the loading of the FXML fail.
                PopUpAlerts.ioAlertFXML();
            }

            Stage currentStage = (Stage) gridPane.getScene().getWindow();
            currentStage.close();
        }

        /**
         * This Method will run if the task is cancelled. Closes the stage of the progressbar.
         */
        @Override
        protected void cancelled() {
            super.cancelled();
            updateMessage("Loading stopped");
            Stage currentStage = (Stage) gridPane.getScene().getWindow();
            currentStage.close();
        }
        /**
         * This Method will run if the task is failed.
         */
        @Override protected void failed() {
            super.failed();
            updateMessage("Loading failed");
        }
    }

    /**
    * TaskService is a nested class within the ProgressController that extends Service in the javafx.concurrent
    * package. An object of TaskService holds an instance of MyTask. The class is nested because it is only used
    * within the ProgressController.
    *
    * @author Henrik Finnerud Larsen
    * @version 1.0
    **/
    private class TaskService extends Service<Void> {
        @Override
        protected Task<Void> createTask()
        {
            return new MyTask();
        }
    }
}