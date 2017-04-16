package model;

import controller.PopUpAlerts;
import javafx.scene.paint.Color;

import lieng.GIFWriter;

import java.io.IOException;

/**
 * The GifConstructor class interacts with lieng.GIFWriter and sends all necessary information in order to create
 * a gif of the current pattern or board. It sets all necessary variables gotten from the export controller
 * and performs the necessary calculations to successfully create a gif.
 *
 * @author Oscar Vladau-Husevold
 * @version 1.0
 */
public class GifConstructor {
    private lieng.GIFWriter gifWriter;
    private double gifCellSize;
    private int gifSize;
    private Board gifBoard;
    private GameOfLife gifGol;
    private int counter;
    private int milliseconds;
    private java.awt.Color gifBackgroundColor;
    private java.awt.Color gifCellColor;
    private boolean drawEntireBoard;

    /**
     * Method that exports a gif to file by creating a new GIFwriter and calling the writeGoLSequenceToGif method.
     * Catches an IOException if thrown, and shows a popup to the user explaining what went wrong.
     * @param filePath - The filepath to write the new file in.
     * @see #writeGoLSequenceToGIF(GIFWriter, GameOfLife, int, java.awt.Color)
     * @see PopUpAlerts#ioeSaveError()
     * @see GIFWriter#GIFWriter(int, int, String, int)
     * @see GIFWriter#setBackgroundColor(java.awt.Color)
     */
    public void exportGif(String filePath) {
        try {
            gifWriter = new GIFWriter(gifSize, gifSize, filePath, milliseconds);
            gifWriter.setBackgroundColor(gifBackgroundColor);
            writeGoLSequenceToGIF(gifWriter, gifGol, counter, gifCellColor);
        } catch (IOException ioe) {
            PopUpAlerts.ioeSaveError();
        }
    }

    /**
     * Method that creates a sequence of images based on the generations of the GameOfLife and inserts them into
     * the GIFWriter in order to be written to disk. Creates images based on whether or not the entire board
     * should be drawn, or just the active pattern.
     * Uses recursion by counting down the number of iterations to be considered, and closes the writer when
     * the counter reaches 0.
     * @param writer - The GIFWriter to handle the writing to disk.
     * @param game - The GameOfLife object to be considered when creating images.
     * @param counter - The number of iterations to be written.
     * @param cellColor - The color to draw active cells.
     * @see #drawEntireBoard
     * @see #drawGifEntireBoard(GIFWriter, java.awt.Color)
     * @see #drawGifPatternOnly(GIFWriter, java.awt.Color)
     * @see GameOfLife#nextGeneration()
     * @see GIFWriter#createNextImage()
     * @see GIFWriter#insertCurrentImage()
     * @see GIFWriter#close()
     */
    void writeGoLSequenceToGIF(GIFWriter writer, GameOfLife game, int counter, java.awt.Color cellColor)
            throws IOException {
        if (counter == 0) {
            writer.close();
            System.out.println("DONE!");
        }
        else {
            writer.createNextImage();

            //Creates image based on whether or not the entire board is to be drawn, or just the active pattern
            if (drawEntireBoard)
                drawGifEntireBoard(writer, cellColor);
            else if (!drawEntireBoard)
                drawGifPatternOnly(writer, cellColor);

            writer.insertCurrentImage();
            game.nextGeneration();
            System.out.println(counter);

            //Recursive call to writeGoLSequenceToGif
            writeGoLSequenceToGIF(writer, game, counter-1, cellColor);
        }
    }

    /**
     * Method that creates an image of the entire cell grid. Sets cell draw size from the width of the board
     * and constructs an image by iterating through the entire board.
     * @param writer - The GIFWriter to handle the writing to disk.
     * @param cellColor - The color to draw the active cells in.
     * @see #gifCellSize
     * @see #gifSize
     * @see Board#getWidth()
     * @see Board#getHeight()
     * @see Board#getCellState(int, int)
     * @see GIFWriter#fillRect(int, int, int, int, java.awt.Color)
     */
    public void drawGifEntireBoard(GIFWriter writer, java.awt.Color cellColor) {
        gifCellSize = gifSize/gifBoard.getWidth();
        int cellDrawSize = (int) gifCellSize - 1;

        //Sets the offset to center the drawn elements to the center of the image.
        int offset = (gifSize - (cellDrawSize * gifBoard.getWidth())) / 2;

        //Iterates through the entire board
        for (int x = 1; x <= gifBoard.getWidth(); x++) {
            for (int y = 1; y <= gifBoard.getHeight(); y++) {

                //Checks if cell is active, and draws it according to position and offset.
                if (gifBoard.getCellState(x - 1,y - 1) == 1) {
                    writer.fillRect((x * cellDrawSize) - cellDrawSize + offset, (x * cellDrawSize) + offset,
                            (y * cellDrawSize) - cellDrawSize + offset, (y * cellDrawSize) + offset, cellColor);
                }
            }
        }
    }

    /**
     * Method that creates an image of the active part of the cell grid. It creates a trimmed version of the
     * board, containing only the active bounding box, and sets cell draw size from the bounding box.
     * Iterates through the trimmed board and draws the active cells.
     * @param writer - The GIFWriter to handle the writing to disk.
     * @param cellColor - The color to draw the active cells in.
     * @see #gifCellSize
     * @see #gifSize
     * @see Board#trim()
     * @see Board#getCellState(int, int)
     * @see GIFWriter#fillRect(int, int, int, int, java.awt.Color)
     */
    public void drawGifPatternOnly(GIFWriter writer, java.awt.Color cellColor) {
        byte[][] trimmed = gifBoard.trim();
        int cellDrawSize;

        //Sets the cell draw size based on whether the height or width of the new 2D-array is largest.
        if (trimmed.length >= trimmed[0].length) {
            gifCellSize = gifSize/trimmed.length;
            cellDrawSize = (int) gifCellSize - 1;
        } else {
            gifCellSize = gifSize/trimmed[0].length;
            cellDrawSize = (int) gifCellSize - 1;
        }

        //Sets the offset in x and y direction based on the size of the gif and the cells to be drawn, to center
        //the elements in the image.
        int offsetX = (gifSize - (cellDrawSize * trimmed.length)) / 2;
        int offsetY = (gifSize - (cellDrawSize * trimmed[0].length)) / 2;

        //Iterates through the trimmed 2D-array and draws active cells according to position and offset.
        for (int x = 1; x <= trimmed.length; x++) {
            for (int y = 1; y <= trimmed[0].length; y++) {
                if (trimmed[x - 1][y - 1] == 1) {
                    writer.fillRect((x * cellDrawSize) - cellDrawSize + offsetX, (x * cellDrawSize) + offsetX,
                            (y * cellDrawSize) - cellDrawSize + offsetY, (y * cellDrawSize) +
                                    offsetY, cellColor);
                }
            }
        }
    }

    /**
     * Method that converts a javafx color to a java.awt color, by using the rgb values of the color.
     * @param color - The javafx color to be converted
     * @return newColor - The converted color.
     */
    public java.awt.Color convertToAwtColor(Color color) {
        float red = (float) color.getRed();
        float green = (float) color.getGreen();
        float blue = (float) color.getBlue();
        float opacity = (float) color.getOpacity();

        java.awt.Color newColor = new java.awt.Color(red, green, blue, opacity);

        return newColor;
    }

    /**
     * Method that sets the current GameOfLife to be considered when drawing the gif.
     * @param gameOfLife - The GameOfLife to be set as current gameOfLife.
     * @see #gifGol
     */
    public void setGifGol(GameOfLife gameOfLife) {
        gifGol = gameOfLife;
        gifBoard = gifGol.playBoard;
    }

    /**
     * Method that sets the current counter.
     * @param iterations - The number of iterations to be drawn.
     * @see #counter
     */
    public void setCounter(int iterations) {
        counter = iterations;
    }

    /**
     * Method that sets the current milliseconds between each image based on the frames per second.
     * @param fps - The number of frames to be shown per second.
     * @see #milliseconds
     */
    public void setMilliseconds(int fps) {
        milliseconds = 1000/fps;
    }

    /**
     * Method that sets the size of the gif in pixels..
     * @param size - The size to be set in pixels.
     * @see #gifSize
     */
    public void setGifSize(int size) {
        gifSize = size;
    }

    /**
     * Method that sets the background color to be drawn. Calls convertToAwtColor to convert
     * a javafx color to java.awt color.
     * @param color - The color to be set as background color.
     * @see #gifBackgroundColor
     */
    public void setGifBackgroundColor(Color color) {
        gifBackgroundColor = convertToAwtColor(color);
    }

    /**
     * Method that sets the cell color to be drawn. Calls convertToAwtColor to convert
     * a javafx color to java.awt color.
     * @param color - The color to be set as cell color.
     * @see #gifCellColor
     */
    public void setGifCellColor(Color color) {
        gifCellColor = convertToAwtColor(color);
    }

    /**
     * Method that sets the whether or not to draw the whole board, or just the active pattern.
     * @param b - Boolean value to be set.
     * @see #drawEntireBoard
     */
    public void setDrawEntireBoard(boolean b) {
        drawEntireBoard = b;
    }

}
