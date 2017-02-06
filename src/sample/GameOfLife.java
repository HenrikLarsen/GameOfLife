package sample;

/**
 * Created by Oscar Vladau on 30.01.2017.
 */
public class GameOfLife {
    public int genCounter;
    public StaticBoard playBoard;
    public boolean isAlive;

    public GameOfLife(StaticBoard board) {
        this.playBoard = board;
    }

    public void nextGeneration() {
        for (int x = 0; x < playBoard.boardGrid.length; x++) {
            for (int y = 0; y < playBoard.boardGrid[0].length; y++) {
                if (playBoard.boardGrid[x][y] == 1) {
                    playBoard.boardGrid[x][y] = 0;
                } else {
                    playBoard.boardGrid[x][y] = 1;
                }
            }
        }
    }

    private void countNeighbours() {
    }

    private void checkCells() {
    }


    //For testing
    //public String toString(){}
    //public void setBoard(){}

}
