package model;

/**
 * The Board class is the abstract superclass for the concrete classes StaticBoard and DynamicBoard.
 * The Board classes handles the playing board of the game, containing the current generations cells
 * and their neighbours. Manipulation of the current cell grid is done through these classes.
 * The Board class is also responsible for adding any loaded patterns to the current cell grid.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public abstract class Board {
    private int cellsAlive = 0;

    //These two fields are related to a loaded pattern.
    private byte[][] loadedPattern;
    private int[] loadedPatternBoundingBox;

    /**
     * A method that creates a 2D-array the size of the cell grid for counting neighbours.
     * Iterates through the entire current cell grid and calls setNeighbours if the cell is active for it
     * to add 1 to each surrounding cell. Returns the 2D-array with the neighbour count for each cell.
     * @return neighbours - 2D-array with neighbours counted for each cell.
     * @see #getCellState(int, int)
     * @see #setNeighbours(byte[][], int, int)
     */
    public byte[][] countNeighbours() {
        //new 2D-array the size of the current cell grid.
        byte[][] neighbours = new byte[getWidth()][getHeight()];

        //Iterates through the cell grid and checks whether the current cell is active.
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (getCellState(x,y) == 1) {
                    setNeighbours(neighbours, x, y);
                }
            }
        }
        return neighbours;
    }

    /**
     * A method that iterates through and counts neighbours for the current cell grid concurrently. The
     * neighbours 2D-byte array is split up according to how many active threads there are, and each thread
     * iterates through its portion of the array. Calls setNeighbours if the cell is active for it
     * to add 1 to each surrounding cell. Returns the 2D-array with the neighbour count for each cell.
     * @param neighbours - 2D-byte array where new neighbours are set.
     * @param curIndex - The current thread's index.
     * @param rowsPerWorker - The number of rows each thread should consider.
     * @return neighbours - 2D-array with neighbours counted for each cell.
     * @see #getCellState(int, int)
     * @see #setNeighbours(byte[][], int, int)
     */
    public byte[][] countNeighboursConcurrent(byte[][] neighbours, int curIndex, int rowsPerWorker) {
        //Iterates through the cell grid and checks whether the current cell is active.
        for (int x = rowsPerWorker*curIndex; x < (curIndex+1)*rowsPerWorker && x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {

                //If active, it calls setNeighbours to assign value to nearby cells.
                if (getCellState(x,y) == 1) {
                    setNeighbours(neighbours, x, y);
                }
            }
        }
        return neighbours;
    }

    /**
     * A synchronized method for adding neighbours around an active cell. Adds 1 to each cell surrounding the
     * current cell. Is synchronized in order to avoid a race condition.
     * @param neighbours - The 2D-array to operate on
     * @param x - The x coordinate of the cell to be considered.
     * @param y - The y coordinate of the cell to be considered.
     */
    private synchronized void setNeighbours(byte[][] neighbours, int x, int y){
        //Adds a neighbour to the upper left corner cell.
        if (x - 1 >= 0 && y - 1 >= 0) {
            neighbours[x - 1][y - 1]++;
        }

        //Adds a neighbour to the left cell.
        if (x - 1 >= 0) {
            neighbours[x - 1][y]++;
        }

        //Adds a neighbour to the lower left corner cell.
        if (x - 1 >= 0 && y + 1 < getHeight()) {
            neighbours[x - 1][y + 1]++;
        }

        //Adds a neighbour to the cell above.
        if (y - 1 >= 0) {
            neighbours[x][y - 1]++;
        }

        //Adds a neighbour to the cell below.
        if (y + 1 < getHeight()) {
            neighbours[x][y + 1]++;
        }

        //Adds a neighbour to the upper right corner cell.
        if (x + 1 < getWidth() && y - 1 >= 0) {
            neighbours[x + 1][y - 1]++;
        }

        //Adds a neighbour to the right cell.
        if (x + 1 < getWidth()) {
            neighbours[x + 1][y]++;
        }

        //Adds a neighbour to the lower right corner cell.
        if (x + 1 < getWidth() && y + 1 < getHeight()) {
            neighbours[x + 1][y + 1]++;
        }
    }

    /**
     * A method for making every cell in the cell grid inactive. Iterates through the grid and
     * sets every cell to 0, sets the number of live cells to 0, as well as discarding any pattern
     * that has been loaded without being finalized.
     * @see #cellsAlive
     * @see #setCellState(int, int, byte)
     * @see #discardPattern()
     */
    public void resetBoard() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                setCellState(x,y, (byte)0);
            }
        }
        cellsAlive = 0;
        discardPattern();
    }

    /**
     * A method for setting the cell grid from an existing 2D-array. Iterates through newGrid and sets the cells
     * of the corresponding places in the cell grid to be equal to those of newGrid.
     * @param newGrid - The grid to be placed in the current cell grid.
     * @see #setCellState(int, int, byte)
     */
    public void setBoard(byte[][] newGrid) {
        for (int x = 0; x < newGrid.length; x++) {
            for (int y = 0; y < newGrid[0].length; y++) {
                setCellState(x,y, newGrid[x][y]);
            }
        }
    }

    /**
     * A method for setting the cell grid from an existing 2D-array concurrently.
     * The new grid is split into as many parts as there are threads, and each thread iterates through
     * it's portion of the newGrid and sets the cells of the corresponding (x,y)-coordinates in the cell grid to be
     * equal to those of newGrid.
     * @param newGrid - The grid to be placed in the current cell grid.
     * @param curIndex - The current thread's index.
     * @param rowsPerWorker - The number of rows each thread should operate on.
     * @see #setCellState(int, int, byte)
     */
    public void setBoardConcurrent(byte[][] newGrid, int curIndex, int rowsPerWorker) {
        for (int x = rowsPerWorker*curIndex; x < (curIndex+1)*rowsPerWorker && x < newGrid.length; x++) {
            for (int y = 0; y < newGrid[0].length; y++) {
                setCellState(x,y, newGrid[x][y]);
            }
        }
    }

    /**
     * A method for counting the current live cells. Iterates through the cell grid and adds 1 to the counter
     * for every active cell, before returning the counter.
     * @return count - The final count of active cells.
     * @see #getCellState(int, int)
     */
    public int countCellsAlive(){
        int count = 0;
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++){
                if(getCellState(x,y) == 1){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * A method that returns the sum of all active cells x and y coordinates. Iterates through the cell grid and
     * adds the x and y value to the sum for each active cell.
     * @return xySum - The sum of all active cells x and y coordinates.
     * @see #getCellState(int, int)
     */
    public int getSumXYCoordinates(){
        int xySum = 0;
        for(int x = 0; x < getWidth(); x++){
            for(int y = 0; y < getHeight(); y++){
                if(getCellState(x,y) == 1){
                    xySum += x + y;
                }
            }
        }
        return xySum;
    }

    /**
     * A method that returns the bounding box of the current cell grid. The bounding box is the smallest area
     * around the active cells of the cell grid, and is represented by 4 values, the minimum and maximum of rows and
     * columns. Returns an int array.
     * @return boundingBox - An int array containing the minimum and maximum values for rows and columns.
     * @see #getCellState(int, int)
     */
    public int[] getBoundingBox() {

        //Creates an array with 4 entries for the bounding box values.
        //The 4 entries are min x, max x, min y, max y, respectively.
        int[] boundingBox = new int[4];
        boundingBox[0] = getWidth();
        boundingBox[1] = 0;
        boundingBox[2] = getHeight();
        boundingBox[3] = 0;

        //Iterates through the cell grid and updates the bounding box array if a cell is active.
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                if(getCellState(x,y) == 0) {
                    continue;
                }
                if(x < boundingBox[0]) {
                    boundingBox[0] = x;
                }
                if(x > boundingBox[1]) {
                    boundingBox[1] = x;
                }
                if(y < boundingBox[2]) {
                    boundingBox[2] = y;
                }
                if(y > boundingBox[3]) {
                    boundingBox[3] = y;
                }
            }
        }
        return boundingBox;
    }

    /**
     * A method that returns the bounding box of a 2D-byte array. The bounding box is the smallest area
     * around the active cells of the cell grid, and is represented by 4 values, the minimum and maximum of rows and
     * columns. Returns an int array.
     * @param grid - the grid that should be considered when creating the bounding box.
     * @return boundingBox - An int array containing the minimum and maximum values for rows and columns.
     * @see #getCellState(int, int)
     */
    private int[] getBoundingBox(byte[][] grid) {

        //Creates an array with 4 entries for the bounding box values.
        //The 4 entries are min x, max x, min y, max y, respectively.
        int[] boundingBox = new int[4];
        boundingBox[0] = grid.length;
        boundingBox[1] = 0;
        boundingBox[2] = grid[0].length;
        boundingBox[3] = 0;

        //Iterates through the cell grid and updates the bounding box array if a cell is active.
        for(int x = 0; x < grid.length; x++) {
            for(int y = 0; y < grid[0].length; y++) {
                if(grid[x][y] == 0){
                    continue;
                }
                if(x < boundingBox[0]) {
                    boundingBox[0] = x;
                }
                if(x > boundingBox[1]) {
                    boundingBox[1] = x;
                }
                if(y < boundingBox[2]) {
                    boundingBox[2] = y;
                }
                if(y > boundingBox[3]) {
                    boundingBox[3] = y;
                }
            }
        }
        return boundingBox;
    }


    /**
     * A method that returns the smallest grid containing all active cells from the current cell grid.
     * It calls #getBoundingBox() and creates a new 2D-array with the number of rows and columns from that.
     * Then it iterates through the current cell grid with the values of the bounding box and sets each cell
     * in the new 2D-array to be the same as the cell grid in that area.
     * @return trimmedBoard - The smallest possible 2D-array containing all active cells from the current cell grid.
     * @see #getCellState(int, int)
     * @see #getBoundingBox()
     */
    public byte[][] trim() {
        int[] boundingBox = getBoundingBox();

        //Creates a new 2D-array with the dimensions of the bounding box.
        int x = Math.abs(boundingBox[1] - boundingBox[0] + 1);
        int y = Math.abs(boundingBox[3] - boundingBox[2] + 1);
        byte[][] trimmedBoard = new byte[x][y];

        //The coordinates in the trimmedBoard relative to the bounding box.
        int trimmedX = 0;
        int trimmedY = 0;

        //Iterates through the cell grid with coordinates set by the bounding box.
        for (int i = boundingBox[0]; i <= boundingBox[1]; i++) {
            for (int j = boundingBox[2]; j <= boundingBox[3]; j++) {
                if (getCellState(i,j) == 1) {
                    trimmedBoard[trimmedX][trimmedY] = 1;
                }
                trimmedY++;
            }
            trimmedX++;
            trimmedY = 0;
        }

        return trimmedBoard;
    }

    /**
     * A method that sets loadedPattern and loadedPatternBoundingBox from a loaded pattern. The bounding box
     * of the loaded pattern is initially set so that the pattern will be placed in the middle of the
     * current cell grid.
     * @param importedBoard - The grid that has been loaded, and is to be set.
     * @see #loadedPattern
     * @see #loadedPatternBoundingBox
     * @see #getBoundingBox(byte[][])
     */
    public void setBoardFromRLE (byte[][] importedBoard) {

        loadedPattern = importedBoard;

        //Creates a new 2D-Array the length and width of the cell grid when the pattern was loaded for the
        //sake of centering the pattern in the middle of the board.
        byte[][] boardSizeAtLoad = new byte[getWidth()][getHeight()];
        int startX = 0;
        int startY = 0;

        //If the width of the pattern is smaller than the width of the cell grid, it sets the start x-coordinate
        //of the pattern so that the pattern will be placed in the middle of the board.
        if (loadedPattern.length < boardSizeAtLoad.length) {
            startX = (boardSizeAtLoad.length - loadedPattern.length) / 2;
        }

        //If the height of the pattern is smaller than the height of the cell grid, it sets the start x-coordinate
        //of the pattern so that the pattern will be placed in the middle of the board.
        if (loadedPattern[0].length < boardSizeAtLoad[0].length) {
            startY = (boardSizeAtLoad[0].length - loadedPattern[0].length) / 2;
        }

        //Places the pattern in the 2D-array the size of the grid at load, and sets loadedPatternBoundingBox
        //to be equal to the patterns bounding box within boardSizeAtLoad
        for (int x = 0; x < loadedPattern.length; x++) {
            for (int y = 0; y < loadedPattern[0].length; y++) {
                boardSizeAtLoad[startX+x][startY+y] = loadedPattern[x][y];
            }
        }
        loadedPatternBoundingBox = getBoundingBox(boardSizeAtLoad);
    }

    /**
     * A method for moving a loaded pattern around in the cell grid. This allows the user to place the pattern
     * where wanted instead of locking it to the initial position. If the board is an instance of DynamicBoard, the
     * cell grid will expand when the pattern reaches the edges of the cell grid. If the board is an instance of
     * StaticBoard and the pattern reaches the edge, it will do nothing.
     * @param direction - the direction the pattern should be moved.
     * @see #loadedPattern
     * @see #loadedPatternBoundingBox
     * @see DynamicBoard#expandHeightUp(int)
     * @see DynamicBoard#expandHeightDown(int)
     * @see DynamicBoard#expandWidthRight(int)
     * @see DynamicBoard#expandWidthLeft(int)
     * @see DynamicBoard#setHasExpandedLeftTrue()
     * @see DynamicBoard#setHasExpandedUpTrue()
     */
    public void movePattern(String direction) {
        //Sets the initial values from the loadedPatternBoundingBox
        int xStart = loadedPatternBoundingBox[0];
        int xStop = loadedPatternBoundingBox[1];
        int yStart = loadedPatternBoundingBox[2];
        int yStop = loadedPatternBoundingBox[3];

        switch (direction) {
            //Moves the pattern up
            case "up":
                if (loadedPatternBoundingBox[2] > 0) {
                    loadedPatternBoundingBox[2] = yStart - 1;
                    loadedPatternBoundingBox[3] = yStop - 1;
                } else if (loadedPatternBoundingBox[2] == 0) { //Checks if it is on the edge already.

                    //Checks if the board is an instance of DynamicBoard and the height is within the limits, and expands.
                    if (this instanceof DynamicBoard && getHeight() < 1900) {
                        ((DynamicBoard) this).expandHeightUp(1);
                        ((DynamicBoard) this).setHasExpandedUpTrue();
                    }
                }
                break;

            //Moves the pattern down
            case "down":
                if (loadedPatternBoundingBox[3] < getHeight() - 1) {
                    loadedPatternBoundingBox[2] = yStart + 1;
                    loadedPatternBoundingBox[3] = yStop + 1;
                } else if (loadedPatternBoundingBox[3] == getHeight() - 1) { //Checks if it is on the edge already.

                    //Checks if the board is an instance of DynamicBoard and the height is within the limits, and expands.
                    if (this instanceof DynamicBoard && getHeight() < 1900) {
                        ((DynamicBoard) this).expandHeightDown(1);
                        loadedPatternBoundingBox[2] = yStart + 1;
                        loadedPatternBoundingBox[3] = yStop + 1;
                    }
                }
                break;

            //Moves the pattern left
            case "left":
                if (loadedPatternBoundingBox[0] > 0) {
                    loadedPatternBoundingBox[0] = xStart - 1;
                    loadedPatternBoundingBox[1] = xStop - 1;
                } else if (loadedPatternBoundingBox[0] == 0) { //Checks if it is on the edge already.

                    //Checks if the board is an instance of DynamicBoard and the width is within the limits, and expands.
                    if (this instanceof DynamicBoard && getWidth() < 1900) {
                        ((DynamicBoard) this).expandWidthLeft(1);
                        ((DynamicBoard) this).setHasExpandedLeftTrue();
                    }
                }
                break;

            //Moves the pattern right
            case "right":
                if (loadedPatternBoundingBox[1] < getWidth() - 1) {
                    loadedPatternBoundingBox[0] = xStart + 1;
                    loadedPatternBoundingBox[1] = xStop + 1;
                } else if (loadedPatternBoundingBox[1] == getWidth() - 1) { //Checks if it is on the edge already.

                    //Checks if the board is an instance of DynamicBoard and the width is within the limits, and expands.
                    if (this instanceof DynamicBoard && getWidth() < 1900) {
                        ((DynamicBoard) this).expandWidthRight(1);
                        loadedPatternBoundingBox[0] = xStart + 1;
                        loadedPatternBoundingBox[1] = xStop + 1;
                    }
                }
                break;
        }
    }

    /**
     * Method for rotating the loaded pattern within the cell grid. Takes a parameter which indicates if it should
     * be rotated clockwise or counter clockwise, in which the order of operations is different.
     * @param clockwise - A boolean describing whether of not the pattern should be rotated clockwise.
     * @see #loadedPattern
     * @see #loadedPatternBoundingBox
     * @see #transposePattern(byte[][])
     * @see #reverseRows(byte[][])
     * @see #setNewBoundingBox()
     */
    public void rotate(boolean clockwise){

        //If rotating clockwise, the pattern needs to be transposed, its rows reversed, and a new bounding box be set.
        if (clockwise) {
            byte[][] transposedPattern = transposePattern(loadedPattern);
            byte[][] rotatedPattern = reverseRows(transposedPattern);
            int[] newBoundingBox = setNewBoundingBox();

            //If newBoundingBox returns a valid array, loadedPattern and loadedPatternBoundingBox is set.
            if (newBoundingBox != null) {
                loadedPatternBoundingBox = newBoundingBox;
                loadedPattern = rotatedPattern;
            }

        //If rotating counterclockwise, it needs to reverse rows before transposing, and then set a new bounding box.
        } else if (!clockwise){
            byte[][] reversedPattern = reverseRows(loadedPattern);
            byte[][] rotatedPattern = transposePattern(reversedPattern);
            int[] newBoundingBox = setNewBoundingBox();

            //If newBoundingBox returns a valid array, loadedPattern and loadedPatternBoundingBox is set.
            if (newBoundingBox != null) {
                loadedPatternBoundingBox = newBoundingBox;
                loadedPattern = rotatedPattern;
            }
        }
    }

    /**
     * Method that transposes a pattern by making the rows columns and vice versa. Takes a pattern
     * as parameter and returns a transposed version of its 2D-array.
     * @param patternGrid - The 2D-array to be transposed.
     * @return transposedPattern - The transposed 2D-array.
     */
    private byte[][] transposePattern(byte[][] patternGrid) {

        //Creates a new 2D-array with the originals height as width, and width as height.
        byte[][] transposedPattern = new byte[patternGrid[0].length][patternGrid.length];

        //Places the cells of the y,x coordinates of the original grid in the x,y coordinates of the new grid.
        for (int x = 0; x < patternGrid[0].length; x++) {
            for (int y = 0; y < patternGrid.length; y++){
                transposedPattern[x][y] = patternGrid[y][x];
            }
        }
        return transposedPattern;
    }

    /**
     * Method that reverses each row of a 2D-array, so that the last element is placed first etc. Needed
     * for rotating a pattern.
     * @param patternGrid - The 2D-array to be reversed.
     * @return reversedPattern - The reversed 2D-array.
     * @see #getBoundingBox(byte[][])
     */
    private byte[][] reverseRows(byte[][] patternGrid) {

        //Creates a new 2D-array with the dimensions of the original grid.
        byte[][] reversedPattern = new byte[patternGrid.length][patternGrid[0].length];

        int[] boundingBox = getBoundingBox(patternGrid);
        int xBack = 0;

        for (int x = boundingBox[0]; x <= boundingBox[1]; x++) {
            for (int y = boundingBox[2]; y <= boundingBox[3]; y++) {
                reversedPattern[x][y] = patternGrid[boundingBox[1] - xBack][y];
            }
            xBack++;
        }
        return reversedPattern;
    }

    /**
     * Method for setting a new bounding box for a loaded pattern after rotating. If the board is an instance of
     * the StaticBoard class, the pattern will not be allowed to rotate if the end bounding box is outside of the
     * current cell grid. If the board is an instance of the DynamicBoard class, the grid will expand to allow for
     * rotating even when the new bounding box would be outside of the cellGrid.
     * @return newBoundingBox - The new bounding box for the rotated pattern.
     * @see #loadedPatternBoundingBox
     * @see DynamicBoard#expandHeightUp(int)
     * @see DynamicBoard#expandHeightDown(int)
     * @see DynamicBoard#expandWidthRight(int)
     * @see DynamicBoard#expandWidthLeft(int)
     * @see DynamicBoard#setHasExpandedLeftTrue()
     * @see DynamicBoard#setHasExpandedUpTrue()
     */
    private int[] setNewBoundingBox() {

        //Creates two variables holding the total width and height of the pattern respectively.
        int xTotal = loadedPatternBoundingBox[1]-loadedPatternBoundingBox[0];
        int yTotal= loadedPatternBoundingBox[3]-loadedPatternBoundingBox[2];

        //Sets the difference in width and height between the old and new bounding box.
        int startDiff = (xTotal-yTotal)/2;
        int endDiff = (xTotal-yTotal)/2;

        //If xTotal+yTotal is an odd number, it needs to adjust the difference according to whether or not it is
        //xTotal or yTotal that is odd, so that the differences remains constant while rotating.
        if ((xTotal+yTotal)%2!=0) {
            if (xTotal % 2 == 0) {
                startDiff = (xTotal - yTotal - 1) / 2;
                endDiff = (xTotal - yTotal + 1)/2;
            } else {
                startDiff = (xTotal - yTotal + 1) / 2;
                endDiff = (xTotal - yTotal - 1)/2;
            }
        }

        //Sets a new bounding box based on the original bounding box plus/minus the differences calculated above.
        int[] newBoundingBox = new int[4];
        newBoundingBox[0] = loadedPatternBoundingBox[0]+startDiff;
        newBoundingBox[1] = loadedPatternBoundingBox[1]-endDiff;
        newBoundingBox[2] = loadedPatternBoundingBox[2]-startDiff;
        newBoundingBox[3] = loadedPatternBoundingBox[3]+endDiff;

        //If the board is an instance of StaticBoard, and the new bounding box is outside of the cell grid,
        //the method returns null
        if (this instanceof StaticBoard) {
            if (newBoundingBox[0] < 0 || newBoundingBox[1] > getWidth() - 1 || newBoundingBox[2] < 0
                    || newBoundingBox[3] > getHeight() - 1) {
                return null;
            }

        //If it's an instance of DynamicBoard and the new bounding box is outside of the cell grid, the grid will
        //expand to allow for the new bounding box, and do the necessary changes to the bounding box values.
        } else if (this instanceof DynamicBoard) {

            //Checks if the new bounding box is outside of the border to the left.
            if (newBoundingBox[0] < 0) {
                int over = Math.abs(newBoundingBox[0]);
                ((DynamicBoard) this).expandWidthLeft(over);
                ((DynamicBoard) this).setHasExpandedLeftTrue();
                newBoundingBox[0] += over;
                newBoundingBox[1] += over;
            }

            //Checks if the new bounding box is outside of the upper border.
            if (newBoundingBox[2] < 0) {
                int over = Math.abs(newBoundingBox[2]);
                ((DynamicBoard) this).expandHeightUp(over);
                ((DynamicBoard) this).setHasExpandedUpTrue();
                newBoundingBox[2] += over;
                newBoundingBox[3] += over;
            }

            //Checks if the new bounding box is outside of the border to the right.
            if (newBoundingBox[1] > getWidth()-1) {
                int over = newBoundingBox[1]-getWidth()+1;
                ((DynamicBoard) this).expandWidthRight(over);
            }

            //Checks if the new bounding box is outside of the lower border..
            if (newBoundingBox[3] > getHeight()-1) {
                int over = newBoundingBox[3]-getHeight()+1;
                ((DynamicBoard) this).expandHeightDown(over);
            }
        }
        return newBoundingBox;
    }

    /**
     * Method that places the loaded pattern permanently into the current cell grid, and sets
     * loadedPattern and loadedPatternBoundingBox to null and does a new count of active cells.
     * @see #loadedPattern
     * @see #loadedPatternBoundingBox
     * @see #cellsAlive
     * @see #setCellState(int, int, byte)
     */
    public void finalizeBoard() {

        //Checks if loadedPattern and loadedPatternBoundingBox is null.
        if (loadedPattern != null && loadedPatternBoundingBox != null) {
            int xLoaded = 0;
            int yLoaded = 0;

            //Sets each cell contained in the loaded pattern into the current cell grid.
            for (int x = loadedPatternBoundingBox[0]; x <= loadedPatternBoundingBox[1]; x++) {
                for (int y = loadedPatternBoundingBox[2]; y <= loadedPatternBoundingBox[3]; y++) {
                    if (loadedPattern[xLoaded][yLoaded] == 1) {
                        setCellState(x, y, (byte)1);
                    }
                    yLoaded++;
                }
                xLoaded++;
                yLoaded = 0;
            }
        }

        //Sets loadedPattern and its bounding box to null, and does a fresh count of active cells.
        loadedPattern = null;
        loadedPatternBoundingBox = null;
        cellsAlive = countCellsAlive();
    }

    /**
     * Method for discarding a loaded pattern. Sets loadedPattern and its bounding box to null.
     * @see #loadedPattern
     * @see #loadedPatternBoundingBox
     */
    public void discardPattern() {
        loadedPattern = null;
        loadedPatternBoundingBox = null;
    }

    /**
     * Method that returns the bounding box of the current loaded pattern.
     * @return loadedPatternBoundingBox - The current bounding box for the loaded pattern.
     * @see #loadedPatternBoundingBox
     */
    public int[] getLoadedPatternBoundingBox() {
        return loadedPatternBoundingBox;
    }

    /**
     * Method that returns the current loaded pattern.
     * @return loadedPattern - The current loaded pattern.
     * @see #loadedPattern
     */
    public byte[][] getLoadedPattern() {
        return loadedPattern;
    }

    /**
     * Method that returns the current cellsAlive count.
     * @return cellsAlive - The number of currently active cells.
     * @see #cellsAlive
     */
    public int getCellsAlive() {
        return cellsAlive;
    }

    /**
     * Method to set the counter of active cells to that of the aliveCells parameter.
     * @param aliveCells - The number of active cells to be set.
     * @see #cellsAlive
     */
    void setCellsAlive(int aliveCells) {
        cellsAlive = aliveCells;
    }

    /**
     * Method to set the counter of active cells to zero.
     * @see #cellsAlive
     */
    public void resetCellsAlive() {
        cellsAlive = 0;
    }

    /**
     * Method that increases cellsAlive by 1. Synchronized to avoid a race condition.
     * @see #cellsAlive
     */
    public synchronized void increaseCellsAlive() {
        cellsAlive++;
    }

    /**
     * Method that decreases cellsAlive by 1. Synchronized to avoid a race condition.
     * @see #cellsAlive
     */
    public synchronized void decreaseCellsAlive() {
        if (cellsAlive > 0) {
            cellsAlive--;
        }
    }

    /**
     * Method that returns a string representation of the current cell grid, placing each cell in a long
     * string of 1s and 0s. Overrides Objects toString method.
     * @return str.toString - The string representation of the current cellGrid.
     * @see #getCellState(int, int)
     * @see Object#toString()
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (getCellState(x,y) == 1) {
                    str.append("1");
                } else {
                    str.append("0");
                }
            }
        }
        return str.toString();
    }

    /**
     * Abstract method for returning an integer representing the width of the current cellGrid.
     * Needs to be overridden in a concrete subclass.
     * @return The width of the playing board.
     */
    public abstract int getWidth();

    /**
     * Abstract method for returning an integer representing the height of the current cellGrid.
     * Needs to be overridden in a concrete subclass.
     * @return The height of the playing board.
     */
    public abstract int getHeight();

    /**
     * Abstract method for returning a byte value representing the state of a certain cell with coordinates (x,y).
     * Needs to be overridden in a concrete subclass.
     * @param x - the x coordinate.
     * @param y - the y coordinate.
     * @return byte - A byte value representing the state of the requested cell.
     */
    public abstract byte getCellState(int x, int y);

    /**
     * Abstract method for setting the state of a cell with coordinates (x,y).
     * Needs to be overridden in a concrete subclass.
     * @param x - the x coordinate.
     * @param y - the y coordinate.
     * @param state - The state the cell should be set to.
     */
    public abstract void setCellState(int x, int y, byte state);

    /**
     * Abstract method for doing a deep copy of the current Board.
     * Needs to be overridden in a concrete subclass.
     * @return Board - The clone of the board.
     */
    @Override
    public abstract Object clone();
}
