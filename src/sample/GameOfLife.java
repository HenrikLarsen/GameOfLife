package sample;

/**
 * Created by Oscar Vladau on 30.01.2017.
 */
public class GameOfLife {
    public int genCounter = 0;
    public StaticBoard playBoard;
    private byte[][] neighbourCount;
    public byte[][] newGenerationCells;
    public int cellsAlive = 0;

    public GameOfLife(StaticBoard board) {
        this.playBoard = board;
    }

    public void nextGeneration() {
        cellsAlive = 0;
        countNeighbours();
        enforceRules();
        playBoard.setBoard(newGenerationCells);
    }

    public void resetBoard() {
        for (int x = 0; x < playBoard.boardGrid.length; x++) {
            for (int y = 0; y < playBoard.boardGrid[0].length; y++) {
                playBoard.boardGrid[x][y] = 0;
            }
        }
        cellsAlive = 0;
    }

    private void countNeighbours() {

        //new byte[][] som setter antall naboer for hver celle.
        neighbourCount = new byte[playBoard.boardGrid.length][playBoard.boardGrid[0].length];

        int xMax = playBoard.boardGrid.length;
        int yMax = playBoard.boardGrid[0].length;

        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {

                //Sjekker om cellen er i live, og om den er det øker den antall naboer for alle cellene rundt.
                //Klarer ikke jobbe med cellene ytterst i brettet.
                if (playBoard.boardGrid[x][y] > 0) {

                    if (x - 1 >= 0 && y - 1 >= 0) {
                        neighbourCount[x - 1][y - 1]++;
                    }

                    if (x - 1 >= 0) {
                        neighbourCount[x - 1][y]++;
                    }

                    if (x - 1 >= 0 && y + 1 < yMax) {
                        neighbourCount[x - 1][y + 1]++;
                    }

                    if (y - 1 >= 0) {
                        neighbourCount[x][y - 1]++;
                    }

                    if (y + 1 < yMax) {
                        neighbourCount[x][y + 1]++;
                    }

                    if (x + 1 < xMax && y - 1 > 0) {
                        neighbourCount[x + 1][y - 1]++;
                    }

                    if (x + 1 < xMax) {
                        neighbourCount[x + 1][y]++;
                    }

                    if (x + 1 < xMax && y + 1 < yMax) {
                        neighbourCount[x + 1][y + 1]++;
                    }
                }
            }
        }
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
                        cellsAlive++;
                    }
                }

                else if (playBoard.boardGrid[x][y] == 0) {
                    if (neighbourCount[x][y] == 3) {
                        newGenerationCells[x][y] = 1;
                        cellsAlive++;
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
