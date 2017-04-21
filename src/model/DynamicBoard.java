package model;

import controller.PopUpAlerts;

import java.util.ArrayList;

/**
 * DynamicBoard is a concrete implementation of the abstract Board class. It handles the playing board of the game,
 * containing the current cell grid and is responsible for manipulating the current cell grid.
 * DynamicBoard's cellGrid is of a dynamic data structure, and it is able to expand to a certain point when needed.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class DynamicBoard extends Board{
    private ArrayList<ArrayList<Byte>> cellGrid;
    private int WIDTH, HEIGHT;

    //Boolean value to set whether or not the board is expandable.
    private boolean expandable = true;

    //Boolean values representing if it should expand in the direction indicated.
    private boolean expandLeft = false;
    private boolean expandRight = false;
    private boolean expandUp = false;
    private boolean expandDown = false;

    //Boolean values representing whether or not the grid has been expanded. Needed to expand dynamically
    //During game time, so that the CanvasDrawer can adjust its offset.
    private boolean hasExpandedLeft = false;
    private boolean hasExpandedUp = false;


    /**
     * Constructor that takes an 2D-ArrayList, width and height as parameters, and sets it as the new board with
     * width and height as the width and height.
     * @param newBoard - 2D-ArrayList to be set as the new board
     * @param x - The width of the playing board.
     * @param y - The width of the playing board.
     */
    public DynamicBoard(ArrayList<ArrayList<Byte>> newBoard, int x, int y){
        this.cellGrid = newBoard;
        this.WIDTH = x;
        this.HEIGHT = y;
    }

    /**
     * Constructor that takes width and height as parameters, and creates a new 2D-arrayList from those dimensions.
     * Sets all elements to be 0 (inactive)
     * @param width - The width of the new playing board.
     * @param height - The width of the new playing board.
     */
    public DynamicBoard(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        ArrayList<ArrayList<Byte>> newBoard = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            newBoard.add(new ArrayList<>());
            for (int y = 0; y < height; y++) {
                newBoard.get(x).add(y, (byte)0);
            }
        }
        setBoard(newBoard);
    }

    /**
     * A method for setting the cell grid from an existing 2D-arrayList. Takes 2D-arrayList as parameter, and sets
     * cellGrid to be this.
     * @param newGrid - The grid to be placed in the current cell grid.
     * @see #cellGrid
     */
    private void setBoard(ArrayList<ArrayList<Byte>> newGrid) {
        this.cellGrid = newGrid;
    }

    /**
     * Concrete implementation of setCellState in the Board class. Sets the value of the cell in the
     * coordinates (x, y) equal to the state parameter. If the cell requested is outside of the current cell grid
     * the grid will expand, as long as it is within the limit.
     * @param x - the x coordinate.
     * @param y - the y coordinate.
     * @param state - The state the cell should be set to.
     * @see #expandWidthRight(int)
     * @see #expandWidthLeft(int)
     * @see #expandHeightDown(int)
     * @see #expandHeightUp(int)
     * @see #checkForExpand(int, int)
     * @see Board#setCellState(int, int, byte)
     * @see PopUpAlerts#edgeAlert()
     */
    @Override
    public void setCellState(int x, int y, byte state) {
        int row = x;
        int column = y;

        //Check to see if the cell is within the width limit.
        if ((x >= getWidth() || x < 0) && (getWidth() > 1900)) { //|| Math.abs(x)+getWidth() > 1900)) {
            //If it is outside of the current board and the limit, it will show a notification to the user.
            PopUpAlerts.edgeAlert();
            return;

        //Checks if it is outside of the current board and the grid is expandable, and expands if it is.
        } else if (x >= getWidth() && expandable) {
            int expand = x-getWidth()+1;
            expandWidthRight(expand);
        } else if (x < 0 && expandable) {
            int expand = Math.abs(x);
            expandWidthLeft(expand);
            row = 0;
        }

        //Check to see if the cell is within the height limit.
        if ((y >= getHeight() || y < 0) && (getHeight() > 1900)) {// || Math.abs(y)+getHeight() > 1900)) {
            //If it is outside of the current board and the limit, it will show a notification to the user.
            PopUpAlerts.edgeAlert();
            return;

        //Checks if it is outside of the current board and the grid is expandable, and expands if it is.
        } else if (y >= getHeight() && expandable) {
            int expand = y-getHeight()+1;
            expandHeightDown(expand);
        } else if (y < 0 && expandable) {
            int expand = Math.abs(y);
            expandHeightUp(expand);
            column = 0;
        }

        //Sets the state of the cell if it is of a valid value
        if (state == 1 || state == 0) {
            cellGrid.get(row).set(column, state);
        }

        //Checks if an active cell is on the edge of the grid, and marks the grid for expansion during run time.
        if (state == 1) {
            checkForExpand(x, y);
        }
        //TODO: Fiks limitern (DEN FORRIGE HADDE RÆVVA BØGG)
    }

    /**
     * Concrete implementation of getCellState in the Board class. Returns the value of the cell in the
     * coordinates requested (x, y). Returns 0 if the cell is outside of the cell grid.
     * @param x - the x coordinate.
     * @param y - the y coordinate.
     * @return state - A byte value representing the state of the requested cell.
     * @see Board#getCellState(int, int)
     */
    @Override
    public byte getCellState(int x, int y) {
        try {
            return cellGrid.get(x).get(y);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            return 0;
        }
    }

    /**
     * Concrete implementation of getWidth in the Board class. Returns an integer value representing
     * the width of the current cellGrid.
     * @return WIDTH - The width of the cellGrid.
     * @see Board#getWidth()
     */
    @Override
    public int getWidth() {
        return WIDTH;
    }

    /**
     * Concrete implementation of getHeight in the Board class. Returns an integer value representing
     * the height of the current cellGrid.
     * @return WIDTH - The width of the cellGrid.
     * @see Board#getHeight()
     */
    @Override
    public int getHeight() {
        return HEIGHT;
    }

    /**
     * Concrete implementation of clone in the Board class. Does a deep copy of the current DynamicBoard and
     * returns it. Overrides the clone method in the Object class.
     * @return dynamicBoardClone - The deep copy of the board.
     * @see Board#cellsAlive
     * @see Board#clone()
     * @see Object#clone()
     */
    @Override
    public Object clone(){
        ArrayList<ArrayList<Byte>> cloneGrid = new ArrayList<ArrayList<Byte>>();
        for (int x = 0; x < getWidth(); x++) {
            cloneGrid.add(new ArrayList<>());
            for (int y = 0; y < getHeight(); y++) {
                cloneGrid.get(x).add(y, getCellState(x,y));
            }
        }
        DynamicBoard dynamicBoardClone = new DynamicBoard(cloneGrid, getWidth(), getHeight());
        dynamicBoardClone.cellsAlive = countCellsAlive();
        return dynamicBoardClone;
    }

    /**
     * Method to resize the grid size quadratically. Takes a size parameter, and creates a new 2D-arrayList of
     * that dimension filled with inactive cells and sets it as the new cellGrid.
     * @return staticBoardClone - The deep copy of the board.
     * @param size - The size the quadratic grid should be.
     * @see #WIDTH
     * @see #HEIGHT
     * @see #setBoard(ArrayList)
     */
    public void setGridSize(int size) {
        //Check that the size is larger than 0 and returns if it is.
        if (size <= 0) {
            return;
        }

        WIDTH = size;
        HEIGHT = size;
        ArrayList<ArrayList<Byte>> newBoard = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            newBoard.add(new ArrayList<>());
            for (int y = 0; y < size; y++) {
                newBoard.get(x).add(y, (byte)0);
            }
        }
        setBoard(newBoard);
    }

    /**
     * Method to increase the WIDTH field of the Dynamic Board. Takes an integer
     * to increase with, and adds it to WIDTH.
     * @param increase - The size of the increase.
     * @see #WIDTH
     */
    private void increaseWidth(int increase) {
        WIDTH += increase;
    }

    /**
     * Method to increase the HEIGHT field of the Dynamic Board. Takes an integer
     * to increase with, and adds it to HEIGHT.
     * @param increase - The size of the increase.
     * @see #HEIGHT
     */
    private void increaseHeight(int increase) {
        HEIGHT += increase;
    }

    /**
     * Method that flags the grid for an increase on each of the borders during run time. Checks if the cell is on
     * the borders of the grid, and if it is, it sets the corresponding boolean to true so that
     * the program knows to increase during a call to nextGeneration (GameofLife)
     * @param x - The x-coordinate of the cell.
     * @param y - The y-coordinate of the cell.
     * @see #expandLeft
     * @see #expandRight
     * @see #expandUp
     * @see #expandDown
     * @see #getWidth()
     * @see #getHeight()
     */
    private void checkForExpand(int x, int y) {

        //checks if the cell is on the left border
        if (x == 0) {
            expandLeft = true;
        }

        //checks if the cell is on the right border
        if (x == getWidth()-1) {
            expandRight = true;
        }

        //checks if the cell is on the upper border
        if (y == 0) {
            expandUp = true;
        }

        //checks if the cell is on the lower border
        if (y == getHeight()-1) {
            expandDown = true;
        }
    }

    /**
     * Method to expand the board during run time. Checks whether or not the board is within the limits of
     * expansions, and if it is expandable. Checks each boolean for expansion, and expands if true.
     * If it has expanded upward or left, it will set the corresponding "hasExpanded" boolean to true, so
     * that the CanvasDrawer can check to adjust its offset.
     * @see #expandLeft
     * @see #expandRight
     * @see #expandUp
     * @see #expandDown
     * @see #getWidth()
     * @see #getHeight()
     */
    public void expandBoardDuringRunTime() {
        //Checks if the board is non-expandable or bigger than the runtime-expansion limit and returns if yes.
        if ((getHeight() >= 1000 && getWidth() >= 1000) || !expandable) {
            return;
        }

        //Checks that width is within run-time expansion limit, and expands if the corresponding boolean is true.
        if (getWidth() < 1000) {
            if (expandLeft) {
                expandWidthLeft(1);
                hasExpandedLeft = true;
                expandLeft = false;
            }
            if (expandRight) {
                expandWidthRight(1);
                expandRight = false;
            }
        }

        //Checks that height is within run-time expansion limit, and expands if the corresponding boolean is true.
        if (getHeight() < 1000) {
            if (expandUp) {
                expandHeightUp(1);
                expandUp = false;
                hasExpandedUp = true;
            }
            if (expandDown) {
                expandHeightDown(1);
                expandDown = false;
            }
        }
        //System.out.println("Width = "+getWidth()+", Height = "+getHeight());
    }

    /**
     * Method to expand the board in the right border. Checks that the expansion parameter is of a valid value,
     * and adds a list for each row and fills the list with empty elements. Then calls increaseWidth to
     * add the expansion to WIDTH.
     * @param expansion - The number of rows to be added.
     * @see #getWidth()
     * @see #getHeight()
     * @see #increaseWidth(int)
     */
    public void expandWidthRight(int expansion) {
        if (expansion <= 0) {
            return;
        }

        for (int x = getWidth(); x < getWidth()+expansion; x++) {
            cellGrid.add(new ArrayList<>());
            for (int y = 0; y < getHeight(); y++) {
                cellGrid.get(x).add(y, (byte)0);
            }
        }
        increaseWidth(expansion);
    }

    /**
     * Method to expand the board in the left border. Checks that the expansion parameter is of a valid value,
     * and adds a list for each row on index 0 and fills the list with empty elements. Then calls increaseWidth to
     * add the expansion to WIDTH.
     * @param expansion - The number of rows to be added.
     * @see #getWidth()
     * @see #getHeight()
     * @see #increaseWidth(int)
     */
    public void expandWidthLeft(int expansion) {
        if (expansion <= 0) {
            return;
        }

        for (int x = 0; x < expansion; x++) {
            cellGrid.add(0, new ArrayList<>());
            for (int y = 0; y < getHeight(); y++) {
                cellGrid.get(0).add(y, (byte)0);
            }
        }
        increaseWidth(expansion);
    }

    /**
     * Method to expand the board in the lower border. Checks that the expansion parameter is of a valid value,
     * and adds the right number of elements to each row. Then calls increaseHeight to
     * add the expansion to HEIGHT.
     * @param expansion - The number of rows to be added.
     * @see #getWidth()
     * @see #getHeight()
     * @see #increaseHeight(int)
     */
    public void expandHeightDown(int expansion) {
        if (expansion <= 0) {
            return;
        }

        for (int x = 0; x < getWidth(); x++) {
            for (int y = getHeight(); y < getHeight()+expansion; y++) {
                cellGrid.get(x).add(y, (byte)0);
            }
        }
        increaseHeight(expansion);
    }

    /**
     * Method to expand the board in the upper border. Checks that the expansion parameter is of a valid value,
     * and adds the right number of elements to each row from index 0. Then calls increaseHeight to
     * add the expansion to HEIGHT.
     * @param expansion - The number of rows to be added.
     * @see #getWidth()
     * @see #getHeight()
     * @see #increaseHeight(int)
     */
    public void expandHeightUp(int expansion) {
        if (expansion <= 0) {
            return;
        }

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < expansion; y++) {
                cellGrid.get(x).add(0, (byte)0);
            }
        }
        increaseHeight(expansion);
    }

    /**
     * Method that sets a boolean value to hasExpandedUp.
     * @param b - boolean value to be assigned.
     * @see #hasExpandedUp
     */
    public void setHasExpandedUp(Boolean b) {
        hasExpandedUp = b;
    }

    /**
     * Method that sets a boolean value to hasExpandedLeft.
     * @param b - boolean value to be assigned.
     * @see #hasExpandedLeft
     */
    public void setHasExpandedLeft(Boolean b) {
        hasExpandedLeft = b;
    }

    /**
     * Method that sets a boolean value to expandable, determining whether or not the grid can be expanded.
     * @param b - boolean value to be assigned.
     * @see #expandable
     */
    public void setExpandable(Boolean b) {
        expandable = b;
    }

    /**
     * Method that returns a boolean representing if the grid has been expanded left, and then sets hasExpandedLeft
     * to false. Needed for CanvasDrawer to be able to adjust offset if the grid has expanded left.
     * @return returnValue = The value of boolean hasExpandedLeft
     * @see #hasExpandedLeft
     */
    public boolean getHasExpandedLeft() {
        boolean returnValue = hasExpandedLeft;
        hasExpandedLeft = false;
        return returnValue;
    }

    /**
     * Method that returns a boolean representing if the grid has been expanded upward, and then sets hasExpandedUp
     * to false. Needed for CanvasDrawer to be able to adjust offset if the grid has expanded upwards.
     * @return returnValue = The value of boolean hasExpandedUp
     * @see #hasExpandedUp
     */
    public boolean getHasExpandedUp() {
        boolean returnValue = hasExpandedUp;
        hasExpandedUp = false;
        return returnValue;
    }


}
