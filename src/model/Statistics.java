package model;

/**
 * Created by henriklarsen on 20.03.2017.
 */
public class Statistics {

    public static int[][] getStatistics(GameOfLife game, int iterations) {
        GameOfLife gameOfLife = (GameOfLife) game.clone();
        int[][] statistics = new int[3][iterations + 1];

        /*
        int xySum = 0;
        int cellsAlive = 0;
        int cellsDiff = 0;
        double reBoard = 0;

        for(int t = 0; t < statistics[0].length; t++){
            statistics[0][t] = gameOfLife.playBoard.cellsAlive;
            if(t == 0){
                statistics[1][t] = 0;
            }else{
                statistics[1][t] = gameOfLife.playBoard.cellsAlive - statistics[0][t-1];
            }

            cellsAlive = gameOfLife.playBoard.cellsAlive;
            cellsDiff = gameOfLife.playBoard.cellsAlive - statistics[0][t-1];
            for(int i = 0; i < gameOfLife.playBoard.getCellGrid()[0].length; i++){
                for(int j = 0; j < gameOfLife.playBoard.getCellGrid()[0].length; j++){
                    if(gameOfLife.playBoard.getCellGrid()[i][j] == 1){
                        xySum += i + j;
                    }
                }
            }

            reBoard = 0.5 * cellsAlive + 3.0 * cellsDiff + 0.25*xySum;

            if(t == 0){
                statistics[2][t] = (int)reBoard;
            }else{
                statistics[2][t] = (int) Math.floor(((Math.min((int)reBoard, statistics[2][t-1])/ Math.max((int)reBoard, statistics[2][t-1]))*100));
            }

            gameOfLife.nextGeneration();
            xySum = 0;
            cellsAlive = 0;
            cellsDiff = 0;
            reBoard = 0;
        }
        */

        // CellSize & CellDiff
        for(int j = 0; j < statistics[0].length; j++){
            if(j == 0){
                statistics[1][j] = 0;
                statistics[0][j] = gameOfLife.playBoard.countCellsAlive();
            }else{
                statistics[0][j] = gameOfLife.playBoard.countCellsAlive();
                statistics[1][j] = gameOfLife.playBoard.countCellsAlive() - statistics[0][j-1];
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
