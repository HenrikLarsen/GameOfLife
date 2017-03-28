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
    private final int WIDTH, HEIGHT;
    private byte[][] cellGrid;

    /**
     * Constructor of the class
     * sets the parameter board as the current board.
     *
     */
    /*public StaticBoard(byte[][] newBoard){
        this.cellGrid = newBoard;
    }*/

    public StaticBoard(int x, int y) {
        WIDTH = x;
        HEIGHT = y;
        this.cellGrid = new byte[WIDTH][HEIGHT];
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }


    /**
     * Method that takes a board as a parameter and sets it as the current board.
     * @param newGrid - the new board to be set.
     */
    public void setBoard(byte[][] newGrid) {
        this.cellGrid = newGrid;
    }

    @Override
    public void setCellState(int x, int y, byte state) {
        if (state == 1 || state == 0) {
            cellGrid[x][y] = state;
        }
    }

    @Override
    public byte getCellState(int x, int y) {
        return cellGrid[x][y];
    }

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