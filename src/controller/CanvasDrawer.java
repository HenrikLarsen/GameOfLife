package controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import model.Board;
import model.DynamicBoard;
import model.GameOfLife;

/**
 * Created by Oscar Vladau on 17.03.2017.
 */
public class CanvasDrawer {
    private boolean erase;
    private double cellDrawSize = 10d;
    private double stripCellSize;
    private double xZoomOffset = 0;
    private double yZoomOffset = 0;
    private double xDragOffset = 0;
    private double yDragOffset = 0;
    private double xOnStartDrag = 0;
    private double yOnStartDrag = 0;
    private boolean dragged = false;
    private boolean zoomed = false;
    private boolean draggingOutOfBounds = false;


    protected void drawBoard(Canvas canvas, Board board, GraphicsContext gc, Color cellColor, Color backgroundColor, boolean grid) {

        gc.setFill(backgroundColor);
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(cellColor);

        if (!draggingOutOfBounds) {
            //xZoomOffset = (canvas.getWidth() - (board.getWidth() * cellDrawSize)) / 2;
            //yZoomOffset = (canvas.getHeight() - (board.getHeight() * cellDrawSize)) / 2;
            if (dragged || zoomed) {
                //xDragOffset = checkDragOffset(xDragOffset, xZoomOffset);
                //yDragOffset = checkDragOffset(yDragOffset, yZoomOffset);
                zoomed = false;
                dragged = false;
            }
        }

        if (board instanceof DynamicBoard && ((DynamicBoard) board).getHasExpandedLeft()) {
            xZoomOffset -= cellDrawSize;
        }
        if (board instanceof DynamicBoard && ((DynamicBoard) board).getHasExpandedUp()) {
            yZoomOffset -= cellDrawSize;
        }

        double xOffset = xZoomOffset + xDragOffset;
        double yOffset = yZoomOffset + yDragOffset;
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                if (board.getCellState(x,y) == 1) {
                    gc.fillRect(x * cellDrawSize + xOffset, y * cellDrawSize + yOffset, cellDrawSize, cellDrawSize);
                }
            }
        }

        if (board.getLoadedPattern() != null && board.getLoadedPatternBoundingBox() != null) {
            byte[][] loadedPattern = board.getLoadedPattern();
            int[] boundingBox = board.getLoadedPatternBoundingBox();
            drawLoadedPattern(gc, loadedPattern, boundingBox);
        }

        if (grid) {
            drawGrid(gc, board, xOffset, yOffset);
        }
    }

    public void drawGrid(GraphicsContext gc, Board board, double xOffset, double yOffset) {
        gc.setLineWidth(cellDrawSize/40);

        //Draws the horizontal lines
        for (int y = 0; y <= board.getHeight(); y++) {
            gc.strokeLine(xOffset, y*cellDrawSize+yOffset, board.getWidth()*cellDrawSize+xOffset, y*cellDrawSize+yOffset);
        }

        //Draws the vertical lines
        for (int x = 0; x <= board.getWidth(); x++) {
            gc.strokeLine(x*cellDrawSize+xOffset, yOffset, x*cellDrawSize+xOffset, board.getHeight()*cellDrawSize+yOffset);
        }
    }



    protected void drawPressed(MouseEvent mouseEvent, Board board, boolean expandable) {
        if (board.getLoadedPattern() != null) {
            return;
        }
        //Checks the x and y coordinates of the mouse-pointer and compares it to the current cell size to find the cell.
        int x = (int) ((mouseEvent.getX() - (xZoomOffset + xDragOffset)) / cellDrawSize);
        int y = (int) ((mouseEvent.getY() - (yZoomOffset + yDragOffset)) / cellDrawSize);

        if ((x < board.getWidth()) && (y < board.getHeight()) && x >= 0 && y >= 0) {

            //Sets global variable erase to true if the first clicked cell was alive.
            //If true, drag and click will only erase until the mouse is released. If false, method will only draw.
            if (board.getCellState(x, y) == 1) {
                erase = true;
            }

            if (board.getCellState(x, y) == 0) {
                if (board.getCellState(x, y) != 1) {
                    board.cellsAlive++;
                }
                board.setCellState(x, y, (byte) 1);
            } else {
                if (board.cellsAlive > 0 && board.getCellState(x, y) == 1) {
                    board.cellsAlive--;
                }
                board.setCellState(x, y, (byte) 0);
            }
        } else if (expandable){
            if (x < 0) {
                x -= 1;
            }
            if (y < 0) {
                y -= 1;
            }
            erase = false;
            board.setCellState(x, y, (byte) 1);

            board.cellsAlive++;
            if (board instanceof DynamicBoard) {
                if (x < 0) {
                    xZoomOffset += x * cellDrawSize;
                }
                if (y < 0) {
                    yZoomOffset += y * cellDrawSize;
                }
            }
        }
    }


    protected void drawDragged(MouseEvent mouseEvent, Board board, boolean expandable) {
        if (board.getLoadedPattern() != null) {
            return;
        }

        //Checks the x and y coordinates of the mouse-pointer and compares it to the current cell size to find the cell.
        int x = (int) ((mouseEvent.getX() - (xZoomOffset + xDragOffset)) / cellDrawSize);
        int y = (int) ((mouseEvent.getY() - (yZoomOffset + yDragOffset)) / cellDrawSize);

        if ((x < board.getWidth()) && (y < board.getHeight()) && x >= 0 && y >= 0) {
                //If boolean erase is true, it sets the cell to 0. Else, sets the cell to 1.
            if (erase) {
                if (board.cellsAlive > 0 && board.getCellState(x, y) == 1) {
                    board.cellsAlive--;
                }
                board.setCellState(x, y, (byte) 0);
            } else {
                if (board.getCellState(x, y) != 1) {
                    board.cellsAlive++;
                }
                board.setCellState(x, y, (byte) 1);
            }
        } else if (!erase && expandable) {
            draggingOutOfBounds = true;
            erase = false;
            board.setCellState(x, y, (byte) 1);
            board.cellsAlive++;
            if (board instanceof DynamicBoard) {
                if (x < 0) {
                    xZoomOffset += x * cellDrawSize;
                }
                if (y < 0) {
                    yZoomOffset += y * cellDrawSize;
                }
            }
        }
    }


    protected void setEraseFalse() {
        erase = false;
    }

    protected void drawStripBoard (GameOfLife stripGol, Board stripBoard, Canvas strip, Color cellColor){
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
        if (size >= 0.3 && size < 60) { //321) {
            this.cellDrawSize = size;
        }
        zoomed = true;
    }

    public void setZoom (double size, Canvas canvas, Board board) {
        double originalSize = cellDrawSize;
        if (size >= 0.3 && size < 60) { //321) {
            this.cellDrawSize = size;
        }
        zoomed = true;

        double xDeltaZoom = ((canvas.getWidth()-(board.getWidth()*cellDrawSize))/2)-((canvas.getWidth()-(board.getWidth()*originalSize))/2);
        double yDeltaZoom = ((canvas.getHeight()-(board.getHeight()*cellDrawSize))/2)-((canvas.getHeight()-(board.getHeight()*originalSize))/2);

        xZoomOffset += xDeltaZoom;
        yZoomOffset += yDeltaZoom;
    }

    public double getCellDrawSize () {
        return cellDrawSize;
    }

    public void drawLoadedPattern(GraphicsContext gc, byte[][] loadedPattern, int[] boundingBox) {
        gc.setGlobalAlpha(0.5);
        gc.setFill(Color.BLACK);
        double xStart = boundingBox[0]*cellDrawSize;
        double yStart = boundingBox[2]*cellDrawSize;
        System.out.println(boundingBox[0]+" "+boundingBox[1]+" "+boundingBox[2]+" "+boundingBox[3]);
        for (int x = 0; x < loadedPattern.length; x++) {
            for (int y = 0; y < loadedPattern[0].length; y++) {
                if (loadedPattern[x][y] == 1) {
                    gc.fillRect(x * cellDrawSize + xZoomOffset + xDragOffset +xStart, y * cellDrawSize + yZoomOffset +
                            yDragOffset + yStart, cellDrawSize, cellDrawSize);
                }
            }
        }
        gc.setGlobalAlpha(1);
    }

    public void setZoomOffset(Board board, Canvas canvas) {
        xZoomOffset = (canvas.getWidth()-(board.getWidth()*cellDrawSize))/2;
        yZoomOffset = (canvas.getHeight()-(board.getHeight()*cellDrawSize))/2;
    }

    public void setDragOffset(MouseEvent drag) {
        double xCurOffset = drag.getX()- xOnStartDrag;
        double yCurOffset = drag.getY()- yOnStartDrag;
        xDragOffset += xCurOffset;
        xOnStartDrag = drag.getX();
        yOnStartDrag = drag.getY();
        yDragOffset += yCurOffset;
        dragged = true;
    }

    public void setOriginalDrag(MouseEvent mouseEvent) {
        xOnStartDrag = mouseEvent.getX();
        yOnStartDrag = mouseEvent.getY();
    }

    public double checkDragOffset(double dragOffset, double zoomOffset) {
        double newOffset;
        if (dragOffset-zoomOffset+cellDrawSize < 0 && dragOffset-zoomOffset-cellDrawSize > -2*zoomOffset) {
            newOffset = 0;
        } else if (dragOffset-zoomOffset+cellDrawSize < 0) {
            newOffset = zoomOffset - cellDrawSize;
        } else if (dragOffset-zoomOffset-cellDrawSize > -2*zoomOffset) {
            newOffset = -zoomOffset+cellDrawSize;
        } else {
            newOffset = dragOffset;
        }
        return newOffset;
    }

    public void setDraggingOutOfBounds(boolean outOfBounds) {
        draggingOutOfBounds = outOfBounds;
    }


    public void resetOffset(Board board, Canvas canvas) {
        xDragOffset = 0;
        yDragOffset = 0;
        cellDrawSize = 10d;
        setZoomOffset(board, canvas);
    }
}
//TODO: Fiks at den zoomer mot midten (Canvas.setScale() kan hjelpe her)
// http://stackoverflow.com/questions/12523033/how-do-you-zoom-in-on-a-javafx-2-canvas-node
// http://stackoverflow.com/questions/12375276/panning-on-a-canvas-in-javafx
// http://stackoverflow.com/questions/29506156/javafx-8-zooming-relative-to-mouse-pointer
