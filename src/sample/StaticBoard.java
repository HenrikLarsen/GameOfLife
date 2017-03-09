package sample;

/**
 * The StaticBoard class handles everything with the play board.
 * It contains the methods and parameters linked to the board.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.1
 */
public class StaticBoard extends Board {
    private final int WIDTH = 200, HEIGHT = 200;
    public byte[][] boardGrid;
    public int cellsAlive = 0;

    /**
     * Constructor of the class
     * sets the parameter board as the current board.
     * @param newBoard
     */
    public StaticBoard(byte[][] newBoard){
        this.boardGrid = newBoard;
    }

    public StaticBoard() {
        this.boardGrid = new byte[WIDTH][HEIGHT];
    }

    /**
     * Method that counts neighbours to every cell.
     * Returns a byte[][] array with the count number to each cell.
     */
    public byte[][] countNeighbours() {
        int xMax = boardGrid.length;
        int yMax = boardGrid[0].length;

        //new byte[][] that sets the number of neighbours to each cell.
        byte[][] neighbourCount = new byte[xMax][yMax];

        //Iterates through the boardgrid and counts neighbours to each cell.
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {

                //Sjekker om cellen er i live, og om den er det øker den antall naboer for alle cellene rundt.
                //Klarer ikke jobbe med cellene ytterst i brettet.
                if (boardGrid[x][y] == 1) {

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

                    if (x + 1 < xMax && y - 1 >= 0) {
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
        return neighbourCount;
    }

    /**
     * Method that takes a board as a parameter and sets it as the current board.
     * @param newGrid - the new board to be set.
     */
    public void setBoard(byte[][] newGrid) {
        this.boardGrid = newGrid;
    }

    /**
     * Method that resets the board by setting every cell to the dead state.
     * Resets the cell counter to 0.
     */
    public void resetBoard() {
        for (int x = 0; x < boardGrid.length; x++) {
            for (int y = 0; y < boardGrid[0].length; y++) {
                boardGrid[x][y] = 0;
            }
        }
        cellsAlive = 0;
    }

    @Override
    public String toString(){
        String str = "";
        for (int y = 0; y < boardGrid[0].length; y++) {
            for (int x = 0; x < boardGrid.length; x++) {
                if (boardGrid[x][y] == 1) {
                    str = str + "1";
                } else {
                    str = str + "0";
                }
            }
        }
        return str;
    }


    public void setBoardFromRLE (byte[][] importedBoard) {
        resetBoard();
        for (int x = 0; x < importedBoard.length; x++) {
            for (int y = 0; y < importedBoard[0].length; y++) {
                boardGrid[x][y] = importedBoard[x][y];
                if(boardGrid[x][y] == 1){
                    cellsAlive++;
                }

            }
        }
    }
}