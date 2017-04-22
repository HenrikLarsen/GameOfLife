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
    private int[][] stat;

    //FXML fields
    @FXML private LineChart<String, Number> lineChart;

    public void setStat(int[][] s){
        this.stat = s;
    }

    /**
     * Method that makes the chart with the data
     */
    public void makeChart(){
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


