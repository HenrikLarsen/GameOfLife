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
 * Created by henriklarsen on 20.03.2017.
 */
public class StatisticsController{
    private GameOfLife gameOfLife;
    private int[][] stat;
    private int iterations;
    private Statistics statistics = new Statistics();

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private LineChart<String, Number> lineChart;

    public void setGameOfLife(GameOfLife gOL){
        this.gameOfLife = gOL;
    }

    public void setIterations(int i){
        this.iterations = i;
    }


    public void makeChart(){
        stat = statistics.getStatistics(gameOfLife, iterations);

        XYChart.Series<String, Number> cellsAliveSeries = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> cellsDiffSeries = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> simularityMeasureSeries = new XYChart.Series<String, Number>();
        cellsAliveSeries.setName("Cells alive");
        cellsDiffSeries.setName("Cells difference");
        simularityMeasureSeries.setName("Simularity measure");

        for(int i = 0; i < stat[0].length; i++){
            cellsAliveSeries.getData().add(new XYChart.Data(Integer.toString(i), stat[0][i]));
            cellsDiffSeries.getData().add(new XYChart.Data(Integer.toString(i), stat[1][i]));
            simularityMeasureSeries.getData().add(new XYChart.Data(Integer.toString(i), stat[2][i]));
        }

        lineChart.getData().addAll(cellsAliveSeries, cellsDiffSeries, simularityMeasureSeries);
    }


}
