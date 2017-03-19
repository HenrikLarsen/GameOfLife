package controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import model.GameOfLife;
import model.StaticBoard;

/**
 * Created by Oscar Vladau on 17.03.2017.
 */
public class CanvasDrawer {
    private boolean erase;
    private byte[][] boardAtMousePressed;

    protected void drawBoard(Canvas canvas, GraphicsContext gc, Color cellColor, Color backgroundColor,
                             double cellSize, byte[][] cellGrid, boolean grid) {
        gc.setFill(backgroundColor);
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(cellColor);

        for (int x = 0; x < cellGrid.length; x++) {
            for (int y = 0; y < cellGrid[0].length; y++) {
                if (cellGrid[x][y] == 1) {
                    gc.fillRect(x * cellSize + 1, y * cellSize + 1, cellSize, cellSize);
                    if (grid) {
                        gc.strokeRect(x * cellSize + 1, y * cellSize + 1, cellSize, cellSize);
                    }
                } else if (grid) {
                    gc.strokeRect(x * cellSize + 1, y * cellSize + 1, cellSize, cellSize);
                }
            }
        }
    }

    protected void drawPressed(double cellSize, MouseEvent mouseEvent, StaticBoard board) {
        byte[][] cellGrid = board.getCellGrid();

        //Checks the x and y coordinates of the mouse-pointer and compares it to the current cell size to find the cell.
        int x = (int)(mouseEvent.getX()/cellSize);
        int y = (int)(mouseEvent.getY()/cellSize);

        //Makes a copy of the board when the mouse button is pressed, stores as a global variable.
        boardAtMousePressed = new byte[cellGrid.length][cellGrid[0].length];
        for (int i = 0; i < boardAtMousePressed.length; i++) {
            for (int j = 0; j < boardAtMousePressed[i].length; j++) {
                boardAtMousePressed[i][j] = cellGrid[i][j];
            }
        }

        //Checks that the user is within the playing board during click, and changes the cells if yes.
        if ((x < cellGrid.length) && (y < cellGrid[0].length) && x >= 0 && y >= 0) {

            //Sets global variable erase to true if the first clicked cell was alive.
            //If true, drag and click will only erase until the mouse is released. If false, method will only draw.
            if (cellGrid[x][y] == 1) {
                erase = true;
            }

            if (cellGrid[x][y] == 0) {
                if (cellGrid[x][y] != 1) {
                    board.cellsAlive++;
                }
                cellGrid[x][y] = 1;
            } else {
                if (board.cellsAlive > 0 && cellGrid[x][y] == 1) {
                    board.cellsAlive--;
                }
                cellGrid[x][y] = 0;
            }
        }
    }

    protected void drawDragged(double cellSize, MouseEvent mouseEvent, StaticBoard board) {
        byte[][] cellGrid = board.getCellGrid();

        //Checks the x and y coordinates of the mouse-pointer and compares it to the current cell size to find the cell.
        int x = (int) (mouseEvent.getX() / cellSize);
        int y = (int) (mouseEvent.getY() / cellSize);

        //Checks that the user is drawing within the borders of the board.
        if ((x < cellGrid.length) && (y < cellGrid[0].length) && x >= 0 && y >= 0) {

            //Checks whether the current cell has been changed since the mouse button was pressed.
            if (cellGrid[x][y] == boardAtMousePressed[x][y]) {

                //If boolean erase is true, it sets the cell to 0. Else, sets the cell to 1.
                if (erase) {
                    if (board.cellsAlive > 0 && cellGrid[x][y] == 1) {
                        board.cellsAlive--;
                    }
                    cellGrid[x][y] = 0;
                } else {
                    if (cellGrid[x][y] != 1) {
                        board.cellsAlive++;
                    }
                    cellGrid[x][y] = 1;
                }
            }
        }
    }

    protected void setEraseFalse() {
        erase = false;
    }

    protected void drawStripBoard (GameOfLife stripGol, StaticBoard stripBoard, Canvas strip, Color cellColor){
        double stripCellSize;
        GraphicsContext gc = strip.getGraphicsContext2D();
        gc.clearRect(0, 0, strip.widthProperty().doubleValue(), strip.heightProperty().doubleValue());
        gc.setFill(cellColor);
        Affine padding = new Affine();
        double xPadding = 10;
        double ty = (strip.getHeight() * 0.1);
        double tx = xPadding;
        padding.setTy(ty);
        gc.setLineWidth(1);
        for (int i = 0; i < 20; i++) {
            byte[][] trimmedBoard = stripBoard.trim();
            if (trimmedBoard.length >= trimmedBoard[0].length) {
                stripCellSize = (strip.getHeight() * 0.85 / trimmedBoard.length);
            } else {
                stripCellSize = (strip.getHeight() * 0.85 / trimmedBoard[0].length);
            }

            padding.setTx(tx);
            gc.setTransform(padding);

            for (int x = 0; x < trimmedBoard.length; x++) {
                for (int y = 0; y < trimmedBoard[0].length; y++) {
                    if (trimmedBoard[x][y] == 1) {
                        gc.fillRect(x * stripCellSize + 1, y * stripCellSize + 1, stripCellSize, stripCellSize);
                    }
                }
            }

            padding.setTx(tx - 13);
            gc.setTransform(padding);

            if (i > 0) {
                gc.strokeLine(0, 0, 0, strip.getHeight());
            }
            //tx += xPadding;
            stripGol.nextGeneration();
            tx += strip.getHeight() + xPadding;
        }

        //reset transform
        padding.setTx(0.0);
        gc.setTransform(padding);

    }
}