package model;

/**
 * Created by henriklarsen on 20.03.2017.
 */
public class Statistics {

    public int[][] getStatistics(GameOfLife game, int iterations) {
        GameOfLife gameOfLife = (GameOfLife) game.clone();
        int[][] statistics = new int[3][iterations + 1];

        for(int j = 0; j < statistics[0].length; j++){
            statistics[0][j] = gameOfLife.playBoard.cellsAlive;
            if(j == 0){
                statistics[1][j] = 0;
            }else{
                statistics[1][j] = gameOfLife.playBoard.cellsAlive - statistics[0][j-1];
            }

            gameOfLife.nextGeneration();
        }


        String out = "CellsAlive:";
        for (int j = 0; j < statistics[0].length; j++) {
            out += " " + statistics[0][j];
        }
        System.out.println(out);

        String cellsDiff = "cellsDiff: ";
        for (int j = 0; j < statistics[0].length; j++) {
            cellsDiff += " " + statistics[1][j];
        }
        System.out.println(cellsDiff);

        return statistics;
    }
}
