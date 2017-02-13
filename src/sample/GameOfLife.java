package sample;

/**
 * Created by Oscar Vladau on 30.01.2017.
 */
public class GameOfLife {
    public int genCounter = 0;
    public StaticBoard playBoard;
    public byte[][] neighbourCount;
    public byte[][] newGenerationCells;

    public GameOfLife(StaticBoard board) {
        this.playBoard = board;
    }

    public void nextGeneration() {
        playBoard.cellsAlive = 0;
        neighbourCount = playBoard.countNeighbours();
        enforceRules();
        playBoard.setBoard(newGenerationCells);
    }


    private void enforceRules() {

        //ny byte[][] hvor de nye cellene settes.
        newGenerationCells = new byte[playBoard.boardGrid.length][playBoard.boardGrid[0].length];

        //sjekker om cellen er i live, og ser så på antall-naboer for å sette nye celler i newGeneratioNCells.
        for (int x = 0; x < playBoard.boardGrid.length; x++) {
            for (int y = 0; y < playBoard.boardGrid[0].length; y++) {

                if (playBoard.boardGrid[x][y] == 1) {
                    if (neighbourCount[x][y] < 2) {
                        newGenerationCells[x][y] = 0;
                    }

                    else if (neighbourCount[x][y] > 3) {
                        newGenerationCells[x][y] = 0;
                    }

                    else if (neighbourCount[x][y] == 2 || neighbourCount[x][y] == 3) {
                        newGenerationCells[x][y] = 1;
                        playBoard.cellsAlive++;
                    }
                }

                else if (playBoard.boardGrid[x][y] == 0) {
                    if (neighbourCount[x][y] == 3) {
                        newGenerationCells[x][y] = 1;
                        playBoard.cellsAlive++;
                    }
                }
            }
        }
    }




    private void checkCells() {
    }

    //For testing
    //public String toString(){}
    //public void setBoard(){}

}
