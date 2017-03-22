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
    private byte[][] loadedPattern;
    private int[] loadedPatternBoundingBox;

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
        discardPattern();
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
        byte[][] boardAtLoad = new byte[cellGrid.length][cellGrid[0].length];
        loadedPattern = importedBoard;

        //These two makes the RLE in the middle bro. remove Start X from for-loop to revert.
        int startX = 0;
        int startY = 0;
        if (loadedPattern.length < boardAtLoad.length) {
            startX = (boardAtLoad.length - loadedPattern.length) / 2;
        }
        if (loadedPattern[0].length < boardAtLoad[0].length) {
            startY = (boardAtLoad[0].length - loadedPattern[0].length) / 2;
        }


        for (int x = 0; x < loadedPattern.length; x++) {
            for (int y = 0; y < loadedPattern[0].length; y++) {
                boardAtLoad[startX+x][startY+y] = loadedPattern[x][y];
            }
        }

        loadedPatternBoundingBox = getBoundingBox(boardAtLoad);
    }

    public void finalizeBoard() {
        if (loadedPattern != null && loadedPatternBoundingBox != null) {
            int xLoaded = 0;
            int yLoaded = 0;
            for (int x = loadedPatternBoundingBox[0]; x <= loadedPatternBoundingBox[1]; x++) {
                for (int y = loadedPatternBoundingBox[2]; y <= loadedPatternBoundingBox[3]; y++) {
                    if (loadedPattern[xLoaded][yLoaded] == 1) {
                        cellGrid[x][y] = 1;
                    }
                    yLoaded++;
                }
                xLoaded++;
                yLoaded = 0;
            }
        }
        loadedPattern = null;
        loadedPatternBoundingBox = null;
        cellsAlive = countCellsAlive();
    }

    public void discardPattern() {
        loadedPattern = null;
        loadedPatternBoundingBox = null;
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
        int xStart = loadedPatternBoundingBox[0];
        int xStop = loadedPatternBoundingBox[1];
        int yStart = loadedPatternBoundingBox[2];
        int yStop = loadedPatternBoundingBox[3];

        for(int x = loadedPatternBoundingBox[0]; x <= loadedPatternBoundingBox[1]; x++) {
            for(int y = loadedPatternBoundingBox[2]; y <= loadedPatternBoundingBox[3]; y++) {
                if (direction.equals("up") && loadedPatternBoundingBox[2] > 0) {
                    loadedPatternBoundingBox[2] = yStart-1;
                    loadedPatternBoundingBox[3] = yStop-1;
                } else if (direction.equals("down") && loadedPatternBoundingBox[3] < cellGrid[0].length-1) {
                    loadedPatternBoundingBox[2] = yStart+1;
                    loadedPatternBoundingBox[3] = yStop+1;
                } else if (direction.equals("left") && loadedPatternBoundingBox[0] > 0) {
                    loadedPatternBoundingBox[0] = xStart-1;
                    loadedPatternBoundingBox[1] = xStop-1;
                } else if (direction.equals("right") && loadedPatternBoundingBox[1] < cellGrid.length - 1) {
                    loadedPatternBoundingBox[0] = xStart+1;
                    loadedPatternBoundingBox[1] = xStop+1;
                } else {
                    return;
                }
            }
        }
    }

    public void rotate(boolean clockwise){
        if (clockwise) {
            byte[][] transposedPattern = transposePattern(loadedPattern);
           // if (transposedPattern != null) {
                byte[][] rotatedPattern = reverseRows(transposedPattern);
                int[] newBoundingBox = setNewBoundingBox(rotatedPattern);
                if (newBoundingBox != null) {
                    loadedPatternBoundingBox = newBoundingBox;
                    loadedPattern = rotatedPattern;
              //  }
            }
        } else if (!clockwise){
            byte[][] reversedPattern = reverseRows(loadedPattern);
            byte[][] rotatedPattern = transposePattern(reversedPattern);
            //if (rotatedPattern != null) {
                int[] newBoundingBox = setNewBoundingBox(rotatedPattern);
                if (newBoundingBox != null) {
                    loadedPatternBoundingBox = newBoundingBox;
                    loadedPattern = rotatedPattern;
               // }
            }
        }
    }

    public int[] setNewBoundingBox(byte[][] newPattern) {
        int xTotal = loadedPatternBoundingBox[1]-loadedPatternBoundingBox[0];
        int yTotal= loadedPatternBoundingBox[3]-loadedPatternBoundingBox[2];
        System.out.println("xDifference = "+xTotal+"\n yDifference = "+yTotal);

        int diff = (xTotal-yTotal)/2;
        int[] newBoundingBox = new int[4];
        newBoundingBox[0] = loadedPatternBoundingBox[0]+diff;
        newBoundingBox[1] = loadedPatternBoundingBox[1]-diff;
        newBoundingBox[2] = loadedPatternBoundingBox[2]-diff;
        newBoundingBox[3] = loadedPatternBoundingBox[3]+diff;

        if (newBoundingBox[0] < 0 || newBoundingBox[1] > cellGrid.length-1 || newBoundingBox[2] < 0
                || newBoundingBox[3] > cellGrid[0].length-1) {
            System.out.println("NOPE!");
            return null;
        }
        return newBoundingBox;
    }


    public byte[][] transposePattern(byte[][] board) {
        byte[][] transposedTrimmed = new byte[board[0].length][board.length];
        for (int x = 0; x < board[0].length; x++) {
            for (int y = 0; y < board.length; y++){
                transposedTrimmed[x][y] = board[y][x];
            }
        }
        return transposedTrimmed;
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

    public int[] getLoadedPatternBoundingBox() {
        return loadedPatternBoundingBox;
    }

    public byte[][] getLoadedPattern() {
        return loadedPattern;
    }
}