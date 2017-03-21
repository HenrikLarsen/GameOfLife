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
    private final int WIDTH = 60, HEIGHT = 60;
    private byte[][] cellGrid;
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
        int[] boundingBox = getBoundingBox(cellGrid);
        return boundingBox;
    }

    public int[] getBoundingBox(byte[][] board) {
        int[] boundingBox = new int[4]; // minrow maxrow mincolumn maxcolumn
        boundingBox[0] = board.length;
        boundingBox[1] = 0;
        boundingBox[2] = board[0].length;
        boundingBox[3] = 0;
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] == 0) continue;
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

    @Override
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

    public byte[][] trim() {
        byte[][] trimmedBoard = trim(cellGrid);
        return trimmedBoard;
    }

    public byte[][] trim(byte[][] board) {
        int[] boundingBox = getBoundingBox();
        int x = Math.abs(boundingBox[1] - boundingBox[0] + 1);
        int y = Math.abs(boundingBox[3] - boundingBox[2] + 1);

        byte[][] trimmedBoard = new byte[x][y];

        int newX = 0;
        int newY = 0;

        for (int i = boundingBox[0]; i <= boundingBox[1]; i++) {
            for (int j = boundingBox[2]; j <= boundingBox[3]; j++) {
                if (board[i][j] == 1) {
                    trimmedBoard[newX][newY] = 1;
                }
                newY++;
            }
            newX++;
            newY = 0;
        }

        return trimmedBoard;
    }

    public byte[][] getCellGrid() {
        return cellGrid;
    }

    public void setCellState(int x, int y, byte state) {
        if (state == 1 || state == 0) {
            cellGrid[x][y] = state;
        }
    }

    public byte getCellState(int x, int y) {
        return cellGrid[x][y];
    }


    public void movePattern(String direction) {
        int[] boundingBox = getBoundingBox();
        byte[][] newBox = new byte[cellGrid.length][cellGrid[0].length];
        for(int x = boundingBox[0]; x <= boundingBox[1]; x++) {
            for(int y = boundingBox[2]; y <= boundingBox[3]; y++) {
                if (direction.equals("up") && boundingBox[2] > 0) {
                    newBox[x][y - 1] = cellGrid[x][y];
                } else if (direction.equals("down") && boundingBox[3] < cellGrid[0].length-1) {
                    newBox[x][y+1] = cellGrid[x][y];
                } else if (direction.equals("left") && boundingBox[0] > 0) {
                    newBox[x-1][y] = cellGrid[x][y];
                } else if (direction.equals("right") && boundingBox[1] < cellGrid.length - 1) {
                    newBox[x + 1][y] = cellGrid[x][y];
                } else {
                    return;
                }
            }
        }
        setBoard(newBox);
    }

    public void rotate(boolean clockwise){
        if (clockwise) {
            byte[][] transposedPattern = transposePattern(cellGrid);
            if (transposedPattern != null) {
                byte[][] rotatedPattern = reverseRows(transposedPattern);
                setBoard(rotatedPattern);
            }
        } else if (!clockwise){
            byte[][] reversedPattern = reverseRows(cellGrid);
            byte[][] rotatedPattern = transposePattern(reversedPattern);
            if (rotatedPattern != null) {
                setBoard(rotatedPattern);
            }
        }
    }


    public byte[][] transposePattern(byte[][] board) {
        byte[][] trimmed = trim(board);
        byte[][] transposedTrimmed = new byte[trimmed[0].length][trimmed.length];
        for (int x = 0; x < trimmed[0].length; x++) {
            for (int y = 0; y < trimmed.length; y++){
                transposedTrimmed[x][y] = trimmed[y][x];
            }
        }

        int[] boundingBox = getBoundingBox();

        int xStart = boundingBox[0];
        int yStart = boundingBox[2];


        int trimmedX = 0;
        int trimmedY = 0;

        byte[][] transposedPattern = new byte[cellGrid.length][cellGrid[0].length];
        if (transposedTrimmed.length+xStart > transposedPattern.length || transposedTrimmed[0].length+yStart > transposedPattern[0].length){
            return null;
        }
        for (int x = xStart; x < transposedTrimmed.length+xStart; x++) {
            for (int y = yStart; y < transposedTrimmed[0].length+yStart; y++){
                transposedPattern[x][y] = transposedTrimmed[trimmedX][trimmedY];
                trimmedY++;
            }
            trimmedX++;
            trimmedY = 0;
        }
        return transposedPattern;
    }

    public byte[][] reverseRows(byte[][] board) {
        int[] boundingBox = getBoundingBox(board);
        byte[][] reversedPattern = new byte[board.length][board[0].length];
        int xBack = 0;
        for (int x = boundingBox[0]; x <= boundingBox[1]; x++) {
            for (int y = boundingBox[2]; y <= boundingBox[3]; y++) {
                reversedPattern[x][y] = board[boundingBox[1] - xBack][y];
            }
            xBack++;
        }
        return reversedPattern;
    }

    public int countCellsAlive(){
        int count = 0;
        for (int x = 0; x < cellGrid[0].length; x++) {
            for (int y = 0; y < cellGrid.length; y++){
               if(cellGrid[x][y] == 1){
                   count++;
                }
            }
        }
        return count;
    }
}