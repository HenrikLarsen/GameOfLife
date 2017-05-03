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
 * The CanvasDrawer handles all drawing to the applications canvases, and handles functionality allowing the user
 * to use the mouse to draw active cells on the cellGrid. It also handles all functionality relating to zooming
 * and panning on the canvas area, allowing the user to visually "move around" the play board.
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */

class CanvasDrawer {
    private boolean erase;
    private double cellDrawSize = 20d;
    private double stripCellSize;

    //Fields relating to the offset created by dragging or zooming on the board.
    private double xZoomOffset = 0;
    private double yZoomOffset = 0;
    private double xDragOffset = 0;
    private double yDragOffset = 0;
    private double xOnStartDrag = 0;
    private double yOnStartDrag = 0;

    /**
     * The main method for drawing the cell grid onto the canvas. Iterates through each element of the play board and
     * draws it on the canvas if it is a live cell, relative to the offset created by dragging or zooming on the
     * play board. It will also draw a grid around each cell, dead or alive, should the grid parameter be true.
     * Should the board have a pattern loaded from file or URL, it will call the drawLoadedPattern() method.
     * @param canvas The canvas to be drawn upon.
     * @param board The active board to be drawn
     * @param gc The Graphics Context related to the canvas.
     * @param cellColor The color of live cells to be drawn
     * @param backgroundColor The background color to be drawn.
     * @param grid Boolean deciding whether or not to draw a grid around each cell.
     * @see #xZoomOffset
     * @see #yZoomOffset
     * @see #xDragOffset
     * @see #yDragOffset
     * @see #cellDrawSize
     * @see #drawGrid(GraphicsContext, double, double)
     * @see #drawLoadedPattern(GraphicsContext, byte[][], int[])
     * @see Board#getWidth()
     * @see Board#getHeight()
     * @see Board#getCellState(int, int)
     * @see DynamicBoard#hasExpandedUp
     * @see DynamicBoard#hasExpandedLeft
     */
    void drawBoard(Canvas canvas, Board board, GraphicsContext gc,
                   Color cellColor, Color backgroundColor, boolean grid) {

        gc.setFill(backgroundColor);
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(cellColor);

        //Adjusts the offset if the board has expanded either left or up, as not doing so would cause the board
        //to shift by one cell.
        if (board instanceof DynamicBoard && ((DynamicBoard) board).getHasExpandedLeft()) {
            xZoomOffset -= cellDrawSize;
        }
        if (board instanceof DynamicBoard && ((DynamicBoard) board).getHasExpandedUp()) {
            yZoomOffset -= cellDrawSize;
        }

        //Iterates through the board and draws active cells.
        double xOffset = xZoomOffset + xDragOffset;
        double yOffset = yZoomOffset + yDragOffset;
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                if (board.getCellState(x,y) == 1) {
                    gc.fillRect(x * cellDrawSize + xOffset, y * cellDrawSize + yOffset,
                            cellDrawSize, cellDrawSize);
                }
            }
        }

        //Calls drawLoadedPattern() if the play board has a pattern loaded.
        if (board.getLoadedPattern() != null && board.getLoadedPatternBoundingBox() != null) {
            byte[][] loadedPattern = board.getLoadedPattern();
            int[] boundingBox = board.getLoadedPatternBoundingBox();
            drawLoadedPattern(gc, loadedPattern, boundingBox);
        }

        //Calls drawGrid() if the grid boolean is true.
        if (grid) {
            drawGrid(gc, xOffset, yOffset);
        }
    }

    /**
     * Method for drawing the grid around each cell. Draws a grid for every cell that can be seen on the current canvas
     * disregarding whether or not the board fill, so that the user is not actively aware of how large the current
     * cell grid is. Draws one line per x and y cell that can be seen.
     * @param gc The graphic context to draw on.
     * @param xOffset The sum of drag and zoom offset on the x axis
     * @param yOffset The sum of drag and zoom offset on the y axis
     * @see #cellDrawSize
     */
    private void drawGrid(GraphicsContext gc, double xOffset, double yOffset) {

        //Sets the line with relative to the cell draw size.
        gc.setLineWidth(cellDrawSize/40);

        //Adjusts the offset so that only the offset of the first cell is considered by using modulo.
        double xGridOffset = 0;
        double yGridOffset = 0;
        if (xOffset > 0) {
            xGridOffset = xOffset%cellDrawSize;
        } else if (xOffset < 0) {
            xGridOffset = (xOffset%cellDrawSize)+cellDrawSize;
        }
        if (yOffset > 0) {
            yGridOffset = yOffset%cellDrawSize;
        } else if (yOffset < 0) {
            yGridOffset = (yOffset%cellDrawSize)+cellDrawSize;
        }

        //Draws the horizontal lines
        for (int y = 0; y <= gc.getCanvas().getHeight()/cellDrawSize; y++) {
            double yStart = y*cellDrawSize+yGridOffset;
            gc.strokeLine(0, yStart, gc.getCanvas().getWidth(), yStart);
        }

        //Draws the vertical lines
        for (int x = 0; x <= gc.getCanvas().getWidth()/cellDrawSize; x++) {
            double xStart = x*cellDrawSize+xGridOffset;
            gc.strokeLine(xStart, 0, xStart, gc.getCanvas().getHeight());
        }
    }

    /**
     * Method for drawing boards loaded pattern onto the canvas. Will produce a semi-transparent representation
     * of the loaded pattern so that the user can move it around without it interfering with already active cells.
     * @param gc The graphic context to draw on.
     * @param loadedPattern A 2D-array containing the cell grid of the loaded pattern
     * @param boundingBox The patterns bounding box relative to the active board.
     * @see #cellDrawSize
     * @see #xZoomOffset
     * @see #xDragOffset
     * @see #yDragOffset
     * @see #yZoomOffset
     */
    private void drawLoadedPattern(GraphicsContext gc, byte[][] loadedPattern, int[] boundingBox) {

        //Sets the alpha of the graphics context so as to draw the pattern semi-transparent.
        gc.setGlobalAlpha(0.5);
        gc.setFill(Color.BLACK);

        //Sets the start values for drawing relative to the board.
        double xStart = boundingBox[0]*cellDrawSize;
        double yStart = boundingBox[2]*cellDrawSize;

        //Draws each cell.
        for (int x = 0; x < loadedPattern.length; x++) {
            for (int y = 0; y < loadedPattern[0].length; y++) {
                if (loadedPattern[x][y] == 1) {
                    gc.fillRect(x * cellDrawSize + xZoomOffset + xDragOffset +xStart, y * cellDrawSize +
                            yZoomOffset + yDragOffset + yStart, cellDrawSize, cellDrawSize);
                }
            }
        }

        //Sets the alpha for the gc back to 100%
        gc.setGlobalAlpha(1);
    }

    /**
     * Method for drawing the strip in the editor, a sequence of iterations after each other.
     * This differs from the regular draw method in that the drawing head of the canvas is moved to the right
     * for each iteration, allowing all iterations to be drawn side by side.
     * @param stripGol The GameOfLife object to be iterated through
     * @param stripBoard The Board object to be considered.
     * @param strip The canvas to be drawn upon.
     * @param cellColor The color in which to draw the active cells.
     * @see #stripCellSize
     * @see Board#trim()
     */
    void drawStripBoard(GameOfLife stripGol, Board stripBoard, Canvas strip, Color cellColor){
        GraphicsContext gc = strip.getGraphicsContext2D();

        //Clears the entire canvas before starting to draw.
        gc.clearRect(0, 0, strip.widthProperty().doubleValue(), strip.heightProperty().doubleValue());

        gc.setFill(cellColor);

        //Creates a new Affine, and sets the initial padding to start drawing with the right offset.
        Affine padding = new Affine();
        double xPadding = 10;
        double ty = (strip.getHeight() * 0.1);
        double tx = xPadding;
        padding.setTy(ty);
        gc.setLineWidth(1);

        //Goes through 20 iterations of the game, drawing the board, a line between each and moving the
        //draw head to be ready for the next iteration.
        for (int i = 0; i < 20; i++) {

            //Trims the board to only the active area, so that not to draw unnecessary inactive cells.
            byte[][] trimmedBoard = stripBoard.trim();

            //Sets the size relative to whether or not the height or width of the board is largest.
            if (trimmedBoard.length >= trimmedBoard[0].length) {
                stripCellSize = (strip.getHeight() * 0.85 / trimmedBoard.length);
            } else {
                stripCellSize = (strip.getHeight() * 0.85 / trimmedBoard[0].length);
            }

            //Sets the offset and iterates through the board, drawing the active cells.
            padding.setTx(tx);
            gc.setTransform(padding);
            for (int x = 0; x < trimmedBoard.length; x++) {
                for (int y = 0; y < trimmedBoard[0].length; y++) {
                    if (trimmedBoard[x][y] == 1) {
                        gc.fillRect(x * stripCellSize + 1, y * stripCellSize + 1, stripCellSize, stripCellSize);
                    }
                }
            }

            //Moves the draw head to the right to draw a separating line between each iteration.
            padding.setTx(tx - 13);
            gc.setTransform(padding);
            if (i > 0) {
                gc.strokeLine(0, 0, 0, strip.getHeight());
            }

            //Goes to the next generation
            stripGol.nextGeneration();

            //Adds the new offset to the total offset for the next generation.
            tx += strip.getHeight() + xPadding;
        }

        //Resets the offset
        padding.setTx(0.0);
        gc.setTransform(padding);
    }

    /**
     * The method that allows the user to draw on the board by clicking the left mouse button.
     * If the board is an instance of DynamicBoard, it will also expand the board should the user draw outside
     * of the already defined board, in order to house that cell, up to a certain point. If the action comes
     * from the EditorController, the board will not expand.
     * @param mouseEvent The event of the mouse clicked.
     * @param board The Board object to be considered.
     * @param expandable A boolean deciding whether or not the board is expandable through drawing.
     * @see #cellDrawSize
     * @see #xZoomOffset
     * @see #xDragOffset
     * @see #yZoomOffset
     * @see #yDragOffset
     * @see #addOffsetIfExpand(int, int)
     * @see Board#getCellsAlive()
     * @see Board#increaseCellsAlive()
     * @see Board#decreaseCellsAlive()
     * @see Board#getCellState(int, int)
     * @see Board#setCellState(int, int, byte)
     */
    void drawPressed(MouseEvent mouseEvent, Board board, boolean expandable) {

        //Does a check to see if the board has a loaded pattern. Returns if yes, as the user is not allowed to
        //modify the board while a pattern is loaded without being finalized.
        if (board.getLoadedPattern() != null) {
            return;
        }

        //Checks the x and y coordinates of the mouse-pointer and compares it to the current cell size to find the cell.
        int x = (int) ((mouseEvent.getX() - (xZoomOffset + xDragOffset)) / cellDrawSize);
        int y = (int) ((mouseEvent.getY() - (yZoomOffset + yDragOffset)) / cellDrawSize);

        //Checks that the cell clicked is within the already defined grid.
        if ((x < board.getWidth()) && (y < board.getHeight()) && x >= 0 && y >= 0) {

            //Sets global variable erase to true if the first clicked cell was alive.
            //If true, drag will only erase until the mouse is released. If false, method will only draw.
            if (board.getCellState(x, y) == 1) {
                erase = true;
            }

            //If cell is inactive, it will become active, and Boards cellsAlive will be added 1.
            if (board.getCellState(x, y) == 0) {
                if (board.getCellState(x, y) != 1) {
                    board.increaseCellsAlive();
                }
                board.setCellState(x, y, (byte) 1);

                //If cell is active, it will become inactive, and Boards cellsAlive will be subtracted 1.
            } else {
                if (board.getCellsAlive() > 0 && board.getCellState(x, y) == 1) {
                    board.decreaseCellsAlive();
                }
                board.setCellState(x, y, (byte) 0);
            }

        //Should the cell be outside of the defined cell grid, and expandable is true, the board wil expand.
        } else if (expandable){

            if (x < 0) {
                x -= 1;
            }
            if (y < 0) {
                y -= 1;
            }

            //Sets global variable erase to false, since the cell pressed is outside and therefore inactive.
            erase = false;

            //Tries to set cell state, and adds the necessary offsets to compensate for the expansions.
            if (board instanceof DynamicBoard) {
                board.setCellState(x, y, (byte) 1);
                board.increaseCellsAlive();
                addOffsetIfExpand(x, y);
            }
        }
    }

    /**
     * The method that allows the user to draw on the board by dragging the mouse while the left button is clicked.
     * If the board is an instance of DynamicBoard, it will also expand the board should the user draw outside
     * of the already defined board, in order to house that cell, up to a certain point. If the action comes
     * from the EditorController, the board will not expand.
     * @param mouseEvent The event of the mouse clicked.
     * @param board The Board object to be considered.
     * @param expandable A boolean deciding whether or not the board is expandable through drawing.
     * @see #cellDrawSize
     * @see #xZoomOffset
     * @see #xDragOffset
     * @see #yZoomOffset
     * @see #yDragOffset
     * @see #addOffsetIfExpand(int, int)
     * @see Board#getCellsAlive()
     * @see Board#increaseCellsAlive()
     * @see Board#decreaseCellsAlive()
     * @see Board#getCellState(int, int)
     * @see Board#setCellState(int, int, byte)
     */
    void drawDragged(MouseEvent mouseEvent, Board board, boolean expandable) {

        //Does a check to see if the board has a loaded pattern. Returns if yes, as the user is not allowed to
        //modify the board while a pattern is loaded without being finalized.
        if (board.getLoadedPattern() != null) {
            return;
        }

        //Checks the x and y coordinates of the mouse-pointer and compares it to the current cell size to find the cell.
        int x = (int) ((mouseEvent.getX() - (xZoomOffset + xDragOffset)) / cellDrawSize);
        int y = (int) ((mouseEvent.getY() - (yZoomOffset + yDragOffset)) / cellDrawSize);

        //Checks that the cell clicked is within the already defined grid.
        if ((x < board.getWidth()) && (y < board.getHeight()) && x >= 0 && y >= 0) {

            //If boolean erase is true, it sets the cell to 0. Else, sets the cell to 1. Subtracts and adds to
            //Boards cellsAlive respectively.
            if (erase) {
                if (board.getCellsAlive() > 0 && board.getCellState(x, y) == 1) {
                    board.decreaseCellsAlive();
                }
                board.setCellState(x, y, (byte) 0);
            } else {
                if (board.getCellState(x, y) != 1) {
                    board.increaseCellsAlive();
                }
                board.setCellState(x, y, (byte) 1);
            }

        //Should the cell be outside of the defined cell grid, erase be false and expandable true, the board wil expand.
        } else if (!erase && expandable) {
            erase = false;

            //Tries to set cell state, and adds the necessary offsets to compensate for the expansions.
            if (board instanceof DynamicBoard) {
                board.setCellState(x, y, (byte) 1);
                board.increaseCellsAlive();
                addOffsetIfExpand(x,y);
            }
        }
    }

    /**
     * Method that sets the boolean erase to false.
     * @see #erase
     */
    void setEraseFalse() {
        erase = false;
    }

    /**
     * Method that sets the cellDrawSize to the value of the parameter, should it be of a valid value
     * @param size The size to be set
     * @see #cellDrawSize
     */
    public void setCellDrawSize (double size) {
        if (size >= 0.3 && size < 60) {
            this.cellDrawSize = size;
        }
    }

    /**
     * Method that sets the cellDrawSize when zooming using the mouse wheel, and compensates for the offset
     * by setting x and y offsets from that value. This will always zoom relative to the center of the active board.
     * @param size The size to be set
     * @param canvas The canvas in question
     * @param board The current board.
     * @see #cellDrawSize
     * @see #xZoomOffset
     * @see #yZoomOffset
     */
    public void setZoom (double size, Canvas canvas, Board board) {
        double originalSize = cellDrawSize;
        if (size >= 0.38 && size < 60) {
            this.cellDrawSize = size;
        }

        //Calculates the difference between the original and new cell size and adds it to the respective zoom offset.
        double xDeltaZoom = ((canvas.getWidth()-(board.getWidth()*cellDrawSize))/2)-
                ((canvas.getWidth()-(board.getWidth()*originalSize))/2);
        double yDeltaZoom = ((canvas.getHeight()-(board.getHeight()*cellDrawSize))/2)-
                ((canvas.getHeight()-(board.getHeight()*originalSize))/2);
        xZoomOffset += xDeltaZoom;
        yZoomOffset += yDeltaZoom;
    }

    /**
     * Method that sets the initial zoom offset relative to the width and height of the canvas and the current
     * cellDrawSize, so that the board is centered in the view of the user.
     * @param canvas The canvas in question
     * @param board The current board.
     * @see #cellDrawSize
     * @see #xZoomOffset
     * @see #yZoomOffset
     */
    public void setZoomOffset(Board board, Canvas canvas) {
        xZoomOffset = (canvas.getWidth()-(board.getWidth()*cellDrawSize))/2;
        yZoomOffset = (canvas.getHeight()-(board.getHeight()*cellDrawSize))/2;
    }

    /**
     * Method that sets drag offset when the user drags with the right mouse button clicked. Allows the user
     * to pan around the board in order to view different areas of the board.
     * @param drag The event of the user dragging the mouse
     * @see #xOnStartDrag
     * @see #yOnStartDrag
     * @see #xDragOffset
     * @see #yDragOffset
     */
    public void setDragOffset(MouseEvent drag) {

        //Sets a value relative to where the drag started and adds it to the x and y dragOffsets.
        double xCurOffset = drag.getX()- xOnStartDrag;
        double yCurOffset = drag.getY()- yOnStartDrag;
        xDragOffset += xCurOffset;
        yDragOffset += yCurOffset;

        //Updates where the next drag event will start.
        xOnStartDrag = drag.getX();
        yOnStartDrag = drag.getY();
    }

    /**
     * Method that sets the x and y coordinates of where a drag event started.
     * @param mouseEvent The event of the user clicking and dragging the mouse.
     * @see #xOnStartDrag
     * @see #yOnStartDrag
     */
    public void setOriginalDrag(MouseEvent mouseEvent) {
        xOnStartDrag = mouseEvent.getX();
        yOnStartDrag = mouseEvent.getY();
    }

    /**
     * Method that resets the drag offsets and sets the zoom offset to the initial value, resulting in a centering
     * of the board within the canvas.
     * @param board The current play board
     * @param canvas The canvas the board is drawn on.
     * @see #xDragOffset
     * @see #yDragOffset
     * @see #setZoomOffset(Board, Canvas)
     */
    public void resetOffset(Board board, Canvas canvas) {
        xDragOffset = 0;
        yDragOffset = 0;
        setZoomOffset(board, canvas);
    }

    /**
     * Method that returns the current cell draw size
     * @return cellDrawSize - The current size of the cells being drawn.
     * @see #cellDrawSize
     */
    public double getCellDrawSize () {
        return cellDrawSize;
    }

    /**
     * Method that resets the cellDrawSize to its default value.
     * @see #cellDrawSize
     */
    public void resetCellSize() {
        cellDrawSize = 20d;
    }

    /**
     * Method that adjusts the zoom offset when the board is expanding in negative direction, so that the
     * board does not "move about". Does a check if the x and y value are negative, and adds to the offset
     * if they are.
     * @param x The x coordinate of the cell in question
     * @param y The y coordinate of the cell in question
     * @see #xZoomOffset
     * @see #yZoomOffset
     * @see #cellDrawSize
     */
    private void addOffsetIfExpand(int x, int y){
        if (x < 0) {
            xZoomOffset += x * cellDrawSize;
        }
        if (y < 0) {
            yZoomOffset += y * cellDrawSize;
        }
    }
}