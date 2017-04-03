package model;

/**
 * Created by henriklarsen on 20.03.2017.
 */
public class Statistics{
    private  int[][] statistics;
    private GameOfLife gameOfLife;
    private double[] sim;

    public Statistics(){
    }

    public int[][] getStatistics(GameOfLife game, int iterations) {
        gameOfLife = (GameOfLife) game.clone();
        statistics = new int[3][iterations + 1];

        // First board to be measured with
        int firstxySum = gameOfLife.playBoard.getSumXYCoordinates();
        int firstCellsAlive = gameOfLife.playBoard.countCellsAlive();
        int firstCellsDifference = 0;
        double firstReducedBoard = 0.5 * firstCellsAlive + 3.0 * firstCellsDifference + 0.25 * firstxySum;



        for(int j = 0; j < statistics[0].length; j++){

            int xySum = gameOfLife.playBoard.getSumXYCoordinates();
            int cellsAlive = gameOfLife.playBoard.countCellsAlive();
            int cellsDifference;
            if(j == 0){
                cellsDifference = 0;
            }else{
                cellsDifference = cellsAlive - statistics[0][j-1];
            }
            double reducedBoard = 0.5 * cellsAlive + 3.0 * cellsDifference + 0.25 * xySum;

            double similarity = Math.min(firstReducedBoard, reducedBoard) / Math.max(firstReducedBoard, reducedBoard)*100;
            double similarityFloored =  Math.floor(similarity);

            sim = new double[statistics[0].length];
            sim[j] = similarity;

            // Entering stats
            if(j == 0 || firstCellsAlive == 0){
                statistics[0][j] = cellsAlive;
                statistics[1][j] = 0;
                statistics[2][j] = 100;
            }else{
                statistics[0][j] = cellsAlive;
                statistics[1][j] = cellsDifference;
                statistics[2][j] = (int)similarityFloored;
            }
            gameOfLife.nextGeneration();
        }

        System.out.println(statToString());
        return statistics;
    }

    public int getHighestSimilarity(int[][] stat){
        int iteration = 0;
        double probability = 99.5;
        for(int i = 2; i < stat[2].length; i++){
            if(stat[2][i] > probability){
                probability = stat[2][i];
                iteration = i;
            }
        }
        return iteration;
    }


    public String statToString(){
        String CellsAlive = "CellsAlive:";
        for (int j = 0; j < statistics[0].length; j++) {
            CellsAlive += " " + statistics[0][j];
        }

        String cellsDiff = "cellsDiff: ";
        for (int j = 0; j < statistics[0].length; j++) {
            cellsDiff += " " + statistics[1][j];
        }

        String similarityMeasure = "similarityMeasure: ";
        for (int j = 0; j < statistics[0].length; j++) {
            similarityMeasure += " " + statistics[2][j];
        }

        String out = CellsAlive + "\n" + cellsDiff + "\n" + similarityMeasure;
        return  out;
    }
}
