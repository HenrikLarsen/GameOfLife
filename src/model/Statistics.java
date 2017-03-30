package model;

/**
 * Created by henriklarsen on 20.03.2017.
 */
public class Statistics{
    private  int[][] statistics;
    private GameOfLife gameOfLife;

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

            double simumlarity = Math.floor(Math.min(firstReducedBoard, reducedBoard) / Math.max(firstReducedBoard, reducedBoard)*100);

            // Entering stats
            if(j == 0 || firstCellsAlive == 0){
                statistics[0][j] = cellsAlive;
                statistics[1][j] = 0;
                statistics[2][j] = 100;
            }else{
                statistics[0][j] = cellsAlive;
                statistics[1][j] = cellsDifference;
                statistics[2][j] = (int)simumlarity;
            }
            gameOfLife.nextGeneration();
        }

        System.out.println(statToString());
        return statistics;
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

        String simularityMeasure = "simularityMeasure: ";
        for (int j = 0; j < statistics[0].length; j++) {
            simularityMeasure += " " + statistics[2][j];
        }

        String out = CellsAlive + "\n" + cellsDiff + "\n" + simularityMeasure;
        return  out;
    }
}
