package model;

/**
 * StaticBoard is a concrete implementation of the abstract Board class. It handles the playing board of the game,
 * containing the current cell grid and is responsible for manipulating the current cell grid.
 * StaticBoard's cellGrid is static in width and height, meaning that its width and height is final.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 * @deprecated replaced by {@link DynamicBoard}
 */
@Deprecated
public class StaticBoard extends Board {
    private final int WIDTH, HEIGHT;
    private final byte[][] cellGrid;

    /**
     * Sole constructor, takes parameters x and y for width and height respectively, and creates a new 2D-array
     * with those dimensions which is set as the cellGrid.
     * @param x The width of the playing board.
     * @param y The width of the playing board.
     */
    public StaticBoard(int x, int y) {
        WIDTH = x;
        HEIGHT = y;
        this.cellGrid = new byte[WIDTH][HEIGHT];
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
     * @see Board#getHeight() ()
     */
    @Override
    public int getHeight() {
        return HEIGHT;
    }

    /**
     * Concrete implementation of getCellState in the Board class. Returns the value of the cell in the
     * coordinates requested (x, y)
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return state - A byte value representing the state of the requested cell.
     * @exception ArrayIndexOutOfBoundsException - If the cell requested is not within the bounds of the grid.
     * @see Board#getCellState(int, int)
     */
    @Override
    public byte getCellState(int x, int y) {
        if (x < getWidth() && y < getHeight()) {
            return cellGrid[x][y];
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Concrete implementation of setCellState in the Board class. Sets the value of the cell in the
     * coordinates (x, y) equal to the state parameter.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param state The state the cell should be set to.
     * @exception ArrayIndexOutOfBoundsException - If the cell requested is not within the bounds of the grid.
     * @see Board#setCellState(int, int, byte)
     */
    @Override
    public void setCellState(int x, int y, byte state) {
        if (state == 1 || state == 0) {
            if (x < getWidth() && y < getHeight()) {
                cellGrid[x][y] = state;
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
    }

    /**
     * Concrete implementation of clone in the Board class. Does a deep copy of the current StaticBoard and
     * returns it. Overrides the clone method in the Object class.
     * @return staticBoardClone - The deep copy of the board.
     * @see Board#clone()
     * @see Object#clone()
     */
    @Override
    public Object clone(){
        byte[][] cellGrid = new byte[this.cellGrid.length][this.cellGrid[0].length];
        for (int i = 0; i < this.cellGrid.length; i++) {
            for (int j = 0; j < this.cellGrid[0].length; j++) {
                cellGrid[i][j] = this.cellGrid[i][j];
            }
        }
        StaticBoard staticBoardClone = new StaticBoard(WIDTH, HEIGHT);
        staticBoardClone.setBoard(cellGrid);
        return staticBoardClone;
    }
}