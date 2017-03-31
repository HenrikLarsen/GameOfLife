package model;

import controller.CanvasDrawer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar Vladau on 27.03.2017.
 */
public class DynamicBoard extends Board{
    private ArrayList<ArrayList<Byte>> cellGrid;
    private int WIDTH, HEIGHT;
    private boolean expandLeft = false;
    private boolean expandRight = false;
    private boolean expandUp = false;
    private boolean expandDown = false;
    private boolean hasExpandedLeft = false;
    private boolean hasExpandedUp = false;


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
        int row = x;
        int column = y;

        if (x >= getWidth()) {
            int expand = x-getWidth()+1;
            expandWidthRight(expand);
        } else if (x < 0) {
            int expand = Math.abs(x);
            expandWidthLeft(expand);
            row = 0;
        }

        if (y >= getHeight()) {
            int expand = y-getHeight()+1;
            expandHeightDown(expand);
        } else if (y < 0) {
            int expand = Math.abs(y);
            expandHeightUp(expand);
            column = 0;
        }


        if (state == 1 || state == 0) {
            cellGrid.get(row).set(column, state);
        }

        if (state == 1) {
            checkForExpand(x, y);
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

    public void increaseWidth(int increase) {
        WIDTH += increase;
    }

    public void increaseHeight(int increase) {
        HEIGHT += increase;
    }

    public void expandWidthRight(int expansion) {
        for (int x = getWidth(); x < getWidth()+expansion; x++) {
            cellGrid.add(new ArrayList<>());
            for (int y = 0; y < getHeight(); y++) {
                cellGrid.get(x).add(y, (byte)0);
            }
        }
        increaseWidth(expansion);
    }

    public void expandWidthLeft(int expansion) {
        for (int x = 0; x < expansion; x++) {
            cellGrid.add(0, new ArrayList<>());
            for (int y = 0; y < getHeight(); y++) {
                cellGrid.get(0).add(y, (byte)0);
            }
        }
        increaseWidth(expansion);
    }

    public void expandHeightDown(int expansion) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = getHeight(); y < getHeight()+expansion; y++) {
                cellGrid.get(x).add(y, (byte)0);
            }
        }
        increaseHeight(expansion);
    }

    public void expandHeightUp(int expansion) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < expansion; y++) {
                cellGrid.get(x).add(0, (byte)0);
            }
        }
        increaseHeight(expansion);
    }

    private void checkForExpand(int x, int y) {
        if (x == 0) {
            expandLeft = true;
        }
        if (x == getWidth()-1) {
            expandRight = true;
        }
        if (y == 0) {
            expandUp = true;
        }
        if (y == getHeight()-1) {
            expandDown = true;
        }
    }

    public void expandBoard() {
        if (getHeight() >= 1000 && getWidth() >= 1000) {
            return;
        }

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

        System.out.println("Width = "+getWidth()+", Height = "+getHeight());
    }

    public void setGridSize(int size) {
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

    public boolean getHasExpandedLeft() {
        boolean returnValue = hasExpandedLeft;
        hasExpandedLeft = false;
        return returnValue;
    }

    public void setHasExpandedLeft(Boolean b) {
        hasExpandedLeft = b;
    }

    public boolean getHasExpandedUp() {
        boolean returnValue = hasExpandedUp;
        hasExpandedUp = false;
        return returnValue;
    }

    public void setHasExpandedUp(Boolean b) {
        hasExpandedUp = b;
    }




}
