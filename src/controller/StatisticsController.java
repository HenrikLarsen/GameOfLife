package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

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
    @FXML private LineChart<String, Number> lineChart;

    private int[][] stat;

    /**
     * Method that sets the statistics data to be drawn on the chart.
     * @param s - Array containing the statistics
     * @see #stat
     */
    public void setStat(int[][] s){
        this.stat = s;
    }

    /**
     * Method that makes the chart with the data
     */
    public void makeChart(){
        // Chart setup
        XYChart.Series<String, Number> cellsAliveSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> cellsDiffSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> similarityMeasureSeries = new XYChart.Series<>();
        cellsAliveSeries.setName("Cells alive");
        cellsDiffSeries.setName("Cells difference");
        similarityMeasureSeries.setName("Similarity measure");

        // Adds the data to the chart
        for(int i = 0; i < stat[0].length; i++){
            cellsAliveSeries.getData().add(new XYChart.Data(Integer.toString(i), stat[0][i]));
            cellsDiffSeries.getData().add(new XYChart.Data(Integer.toString(i), stat[1][i]));
            similarityMeasureSeries.getData().add(new XYChart.Data(Integer.toString(i), stat[2][i]));
        }

        lineChart.getData().addAll(cellsAliveSeries, cellsDiffSeries, similarityMeasureSeries);
    }
}