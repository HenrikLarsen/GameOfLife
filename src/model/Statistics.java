package model;

/**
 * Created by henriklarsen on 20.03.2017.
 */
public class Statistics {
    public int progress = 0;

    public static int[][] getStatistics(GameOfLife game, int iterations) {
        GameOfLife gameOfLife = (GameOfLife) game.clone();
        int[][] statistics = new int[3][iterations + 1];

        // First board to be measured with
        int firstxySum = gameOfLife.playBoard.getSumXYCordinates();
        int firstCellsAlive = gameOfLife.playBoard.countCellsAlive();
        int firstCellsDifference = 0;
        double firstReducedBoard = 0.5 * firstCellsAlive + 3.0 * firstCellsDifference + 0.25 * firstxySum;


        for(int j = 0; j < statistics[0].length; j++){

            int xySum = gameOfLife.playBoard.getSumXYCordinates();
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
            if(j == 0 || cellsAlive == 0){
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

        String out = "CellsAlive:";
        for (int j = 0; j < statistics[0].length; j++) {
            out += " " + statistics[0][j];
        }
        System.out.println(out);

        String cellsDiff2 = "cellsDiff: ";
        for (int j = 0; j < statistics[0].length; j++) {
            cellsDiff2 += " " + statistics[1][j];
        }
        System.out.println(cellsDiff2);

        String sM = "simularityMeasure: ";
        for (int j = 0; j < statistics[0].length; j++) {
            sM += " " + statistics[2][j];
        }
        System.out.println(sM);

        return statistics;
    }
}
