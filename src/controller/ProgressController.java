package controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.GameOfLife;
import model.Statistics;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by henriklarsen on 21.04.2017.
 */
public class ProgressController {
    private final int max = 100;
    private int[][] stat;
    private int iterations;

    private Stage statisticStage;
    private StatisticsController statisticsController;
    private GameOfLife gameOfLife;
    private Statistics statistics = new Statistics();

    private MyTask myTask;
    private TaskService taskService;

    @FXML GridPane gridpane;
    @FXML public Label lblProgress;
    @FXML public ProgressBar pbar;


    public ProgressController() {
        myTask = new MyTask();
        taskService = new TaskService();
    }

    @FXML
    public void initialize() {
        if (!lblProgress.textProperty().isBound())
        {
            // bind the messageProperty to a TextArea in JavaFX
            lblProgress.textProperty().bind(taskService.messageProperty());

            // bind the progressProperty to a ProgressBar in JavaFX
            pbar.progressProperty().bind(taskService.progressProperty());
        }
        startProgress();
    }

    /**
     * Method which handles the Cancel button click action
     */
    public void cancelClick(ActionEvent actionEvent) {
        taskService.cancel();
    }

    public void startProgress(){
        taskService.restart();
    }

    /**
     * Method that sets the current gameoflife.
     * @see #gameOfLife
     */
    public void setGameOfLife(GameOfLife gOL){
        this.gameOfLife = gOL;
    }

    /**
     * Method that sets the number of iterations to be measured and drawn.
     * @see #iterations
     */
    public void setIterations(int i){
        this.iterations = i;
    }

    /**
     * A class demonstrating the use of the Task class
     */
    private class MyTask extends Task<Void> {
        @Override
        public Void call() {
            for (int i = 1; i <= max; i++) {
                if (isCancelled()){
                    break;
                }

                do{
                    stat = statistics.getStatistics(gameOfLife, iterations);
                }while(i == 0);

                updateMessage("Working!" + " " + i + "%");
                updateProgress(i, max);

                //Block the thread for a short time, but be sure
                //to check the InterruptedException for cancellation
                /*
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interrupted) {
                    if (isCancelled()) break;
                }
                */
            }
            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            try{
                statisticStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Statistics.fxml"));
                Parent root = loader.load();
                statisticsController = loader.getController();

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

            Stage currentStage = (Stage) gridpane.getScene().getWindow();
            currentStage.close();
        }

        @Override
        protected void cancelled() {
            super.cancelled();
            updateMessage("Stopped!");
            Stage currentStage = (Stage) gridpane.getScene().getWindow();
            currentStage.close();
        }

        @Override protected void failed() {
            super.failed();
            updateMessage("Failed!");
        }
    }


    /**
     * A class demonstrating the use of the Service class
     */
    private class TaskService extends Service<Void>
    {
        @Override
        protected Task<Void> createTask()
        {
            return new MyTask();
        }

    }
}
