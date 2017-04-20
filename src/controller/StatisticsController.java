package controller;


import com.sun.deploy.ui.ProgressDialog;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.GameOfLife;
import model.Statistics;

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
        stat = statistics.getStatistics(gameOfLife, iterations);

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
