package controller;


import com.sun.deploy.ui.ProgressDialog;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.GameOfLife;
import model.Statistics;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * The StatisticsController handles all user-interaction within the statistics window.
 * It contains the methods and parameters linked to the graphical user interface elements
 * that the user can interact with, and handles the changes that happen based on what the
 * user does within the application.
 *
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class StatisticsController{
    private GameOfLife gameOfLife;
    private int[][] stat;
    private int iterations;
    private Statistics statistics = new Statistics();
    final int max = 10;

    //private Stage progressStage;
    //private progressController progressController;

    //FXML fields
    @FXML private CategoryAxis x;
    @FXML private NumberAxis y;
    @FXML private LineChart<String, Number> lineChart;


    /**
     * Method that sets the current gameoflife.
     * @see #gameOfLife
     */
    public void setGameOfLife(GameOfLife gOL){
        this.gameOfLife = gOL;
    }

    public void setStat( int[][] s){
        this.stat = s;
    }

    /**
     * Method that sets the number of iterations to be measured and drawn.
     * @see #iterations
     */
    public void setIterations(int i){
        this.iterations = i;
    }

    /**
     * Method that gets statistics data from the statistics class.
     * and then makes the chart with the data
     * @see Statistics#getStatistics(GameOfLife, int)
     */
    public void makeChart(){
        // Gets the data to the statistics chart.
        /*
        try{
            progressStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../controller/progress.fxml"));
            Parent root = loader.load();
            progressController = loader.getController();


            //Opens and waits
            progressStage.setTitle("Please wait");
            progressStage.setScene(new Scene(root, 300, 200));
            progressStage.showAndWait();

        } catch (IOException ioe){
        //Shows a warning should the loading of the FXML fail.
        PopUpAlerts.ioAlertFXML();
        }
        */

        //stat = statistics.getStatistics(gameOfLife, iterations);

        // Chart setup
        XYChart.Series<String, Number> cellsAliveSeries = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> cellsDiffSeries = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> simularityMeasureSeries = new XYChart.Series<String, Number>();
        cellsAliveSeries.setName("Cells alive");
        cellsDiffSeries.setName("Cells difference");
        simularityMeasureSeries.setName("Simularity measure");

        // Adds the data to the chart
        for(int i = 0; i < stat[0].length; i++){
            cellsAliveSeries.getData().add(new XYChart.Data(Integer.toString(i), stat[0][i]));
            cellsDiffSeries.getData().add(new XYChart.Data(Integer.toString(i), stat[1][i]));
            simularityMeasureSeries.getData().add(new XYChart.Data(Integer.toString(i), stat[2][i]));
        }

        lineChart.getData().addAll(cellsAliveSeries, cellsDiffSeries, simularityMeasureSeries);
    }
}


