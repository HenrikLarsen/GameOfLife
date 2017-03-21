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
    private double cellDrawSize = 15.5d;
    private double stripCellSize;
    private double xZoomOffset = 0;
    private double yZoomOffset = 0;
    private double xDragOffset = 0;
    private double yDragOffset = 0;

    protected void drawBoard(Canvas canvas, GraphicsContext gc, Color cellColor, Color backgroundColor,
                             byte[][] cellGrid, boolean grid) {

        gc.setFill(backgroundColor);
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(cellColor);

        setZoomOffset(cellGrid, canvas);
        for (int x = 0; x < cellGrid.length; x++) {
            for (int y = 0; y < cellGrid[0].length; y++) {
                if (cellGrid[x][y] == 1) {
                    gc.fillRect(x * cellDrawSize + xZoomOffset + xDragOffset, y * cellDrawSize + yZoomOffset +
                                    yDragOffset, cellDrawSize, cellDrawSize);
                    if (grid) {
                        gc.strokeRect(x * cellDrawSize + xZoomOffset + xDragOffset, y * cellDrawSize +
                                yZoomOffset + yDragOffset, cellDrawSize, cellDrawSize);
                    }
                } else if (grid) {
                    gc.strokeRect(x * cellDrawSize + xZoomOffset + xDragOffset, y * cellDrawSize + yZoomOffset +
                            yDragOffset, cellDrawSize, cellDrawSize);
                }
            }
        }
    }

    protected void drawPressed(MouseEvent mouseEvent, StaticBoard board) {
        byte[][] cellGrid = board.getCellGrid();

        //Checks the x and y coordinates of the mouse-pointer and compares it to the current cell size to find the cell.
        int x = (int)((mouseEvent.getX()- (xZoomOffset+xDragOffset))/ cellDrawSize);
        int y = (int)((mouseEvent.getY()- (yZoomOffset+yDragOffset))/ cellDrawSize);

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

    protected void drawDragged(MouseEvent mouseEvent, StaticBoard board) {
        byte[][] cellGrid = board.getCellGrid();

        //Checks the x and y coordinates of the mouse-pointer and compares it to the current cell size to find the cell.
        int x = (int)((mouseEvent.getX()- (xZoomOffset+xDragOffset))/ cellDrawSize);
        int y = (int)((mouseEvent.getY()- (yZoomOffset+yDragOffset))/ cellDrawSize);

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

    public void setCellDrawSize (double size) {
        if (size >= 0.3 && size < 321) {
            this.cellDrawSize = size;
        }
    }

    public double getCellDrawSize () {
        return cellDrawSize;
    }

    public void setZoomOffset(byte[][] cellGrid, Canvas canvas) {
        if (cellGrid.length * cellDrawSize > canvas.getWidth()) {
            xZoomOffset = (canvas.getWidth()-(cellGrid.length * cellDrawSize))/2;
        } else if (cellGrid.length * cellDrawSize < canvas.getWidth()){
            xZoomOffset = (canvas.getWidth()-(cellGrid.length * cellDrawSize))/2;
        }

        if (cellGrid[0].length * cellDrawSize > canvas.getHeight()){
            yZoomOffset = (canvas.getHeight() - (cellGrid[0].length * cellDrawSize))/2;
        } else if (cellGrid[0].length * cellDrawSize < canvas.getHeight()){
            yZoomOffset = (canvas.getHeight()-(cellGrid[0].length*cellDrawSize))/2;
        }
    }

    public void setDragOffset(MouseEvent drag, MouseEvent click) {
        double originalX = xDragOffset+click.getX();
        double originalY = yDragOffset+click.getY();
        double xCurOffset = drag.getX()-click.getX();
        double yCurOffset = drag.getY()-click.getY();

        xDragOffset = xCurOffset;
        yDragOffset = yCurOffset;
    }
}