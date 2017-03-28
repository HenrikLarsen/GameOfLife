package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar Vladau on 27.03.2017.
 */
public class DynamicBoard extends Board{
    private ArrayList<ArrayList<Byte>> cellGrid;
    private int WIDTH, HEIGHT;


    public DynamicBoard(ArrayList<ArrayList<Byte>> newBoard){
        this.cellGrid = newBoard;
    }

    public DynamicBoard(int i, int j) {
        WIDTH = i;
        HEIGHT = j;
        ArrayList<ArrayList<Byte>> newBoard = new ArrayList<>();
        for (int x = 0; x < i; x++) {
            newBoard.add(new ArrayList<>());
            for (int y = 0; y < j; y++) {
                newBoard.get(x).add(y, (byte)0);
            }
        }
        setBoard(newBoard);
    }

    public void setBoard(ArrayList<ArrayList<Byte>> newGrid) {
        this.cellGrid = newGrid;
    }

    @Override
    public void setCellState(int x, int y, byte state) {
        if (state == 1 || state == 0) {
            cellGrid.get(x).set(y, state);
        }
    }

    @Override
    public byte getCellState(int x, int y) {
        return cellGrid.get(x).get(y);
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
    public Object clone(){
        ArrayList<ArrayList<Byte>> cloneGrid = new ArrayList<ArrayList<Byte>>();
        for (int x = 0; x < getWidth(); x++) {
            cloneGrid.add(new ArrayList<>());
            for (int y = 0; y < getHeight(); y++) {
                cloneGrid.get(x).add(y, getCellState(x,y));
            }
        }
        DynamicBoard dynamicBoardClone = new DynamicBoard(cloneGrid);
        dynamicBoardClone.WIDTH = getWidth();
        dynamicBoardClone.HEIGHT = getHeight();
        dynamicBoardClone.cellsAlive = countCellsAlive();
        return dynamicBoardClone;
    }

}
