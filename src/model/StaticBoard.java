package model;

/**
 * The StaticBoard class handles everything with the play board.
 * It contains the methods and parameters linked to the board.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.1
 */
public class StaticBoard extends Board {
    private final int WIDTH = 64, HEIGHT = 64;
    public byte[][] cellGrid;
    public int cellsAlive = 0;

    /**
     * Constructor of the class
     * sets the parameter board as the current board.
     * @param newBoard
     */
    public StaticBoard(byte[][] newBoard){
        this.cellGrid = newBoard;
    }

    public StaticBoard() {
        this.cellGrid = new byte[WIDTH][HEIGHT];
    }

    /**
     * Method that counts neighbours to every cell.
     * Returns a byte[][] array with the count number to each cell.
     */
    public byte[][] countNeighbours() {
        int xMax = cellGrid.length;
        int yMax = cellGrid[0].length;

        //new byte[][] that sets the number of neighbours to each cell.
        byte[][] neighbourCount = new byte[xMax][yMax];

        //Iterates through the boardgrid and counts neighbours to each cell.
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {

                //Sjekker om cellen er i live, og om den er det øker den antall naboer for alle cellene rundt.
                //Klarer ikke jobbe med cellene ytterst i brettet.
                if (cellGrid[x][y] == 1) {

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
        this.cellGrid = newGrid;
    }

    /**
     * Method that resets the board by setting every cell to the dead state.
     * Resets the cell counter to 0.
     */
    public void resetBoard() {
        for (int x = 0; x < cellGrid.length; x++) {
            for (int y = 0; y < cellGrid[0].length; y++) {
                cellGrid[x][y] = 0;
            }
        }
        cellsAlive = 0;
    }

    @Override
    public String toString(){
        String str = "";
        for (int y = 0; y < cellGrid[0].length; y++) {
            for (int x = 0; x < cellGrid.length; x++) {
                if (cellGrid[x][y] == 1) {
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

        //These two makes the RLE in the middle bro. remove Start X from for-loop to revert.
        int startX = 0;
        int startY = 0;
        if (importedBoard.length < cellGrid.length) {
            startX = (cellGrid.length - importedBoard.length) / 2;
        }
        if (importedBoard[0].length < cellGrid[0].length) {
            startY = (cellGrid[0].length - importedBoard[0].length) / 2;
        }


        for (int x = 0; x < importedBoard.length; x++) {
            for (int y = 0; y < importedBoard[0].length; y++) {
                cellGrid[startX+x][startY+y] = importedBoard[x][y];
                if(cellGrid[startX+x][startY+y] == 1){
                    cellsAlive++;
                }

            }
        }
    }

    public String getBoundingBoxPattern() {
        if(cellGrid.length == 0) return "";
        int[] boundingBox = getBoundingBox();
        String str = "";
        for(int i = boundingBox[0]; i <= boundingBox[1]; i++) {
            for(int j = boundingBox[2]; j <= boundingBox[3]; j++) {
                if(cellGrid[i][j] == 1) {
                    str = str + "1";
                } else {
                    str = str + "0";
                }
            }
        }
        return str;
    }

    public int[] getBoundingBox() {
        int[] boundingBox = new int[4]; // minrow maxrow mincolumn maxcolumn
        boundingBox[0] = cellGrid.length;
        boundingBox[1] = 0;
        boundingBox[2] = cellGrid[0].length;
        boundingBox[3] = 0;
        for(int i = 0; i < cellGrid.length; i++) {
            for(int j = 0; j < cellGrid[i].length; j++) {
                if(cellGrid[i][j] == 0) continue;
                if(i < boundingBox[0]) {
                    boundingBox[0] = i;
                }
                if(i > boundingBox[1]) {
                    boundingBox[1] = i;
                }
                if(j < boundingBox[2]) {
                    boundingBox[2] = j;
                }
                if(j > boundingBox[3]) {
                    boundingBox[3] = j;
                }
            }
        }
        return boundingBox;
    }

    public Object clone(){
        byte[][] cellGrid = new byte[this.cellGrid.length][this.cellGrid[0].length];
        for (int i = 0; i < this.cellGrid.length; i++) {
            for (int j = 0; j < this.cellGrid[0].length; j++) {
                cellGrid[i][j] = this.cellGrid[i][j];
            }
        }
        StaticBoard staticBoardClone = new StaticBoard(cellGrid);
        return staticBoardClone;
    }
}