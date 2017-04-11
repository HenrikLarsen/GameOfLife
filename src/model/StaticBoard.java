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

    @Override
    public byte getCellState(int x, int y) {
        if (x < getWidth() && y < getHeight()) {
            return cellGrid[x][y];
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
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