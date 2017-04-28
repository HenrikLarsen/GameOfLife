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
    private double gifCellSize;
    private final int gifSize;
    private final Board gifBoard;
    private final GameOfLife gifGol;
    private final int counter;
    private final int milliseconds;
    private final java.awt.Color gifBackgroundColor;
    private final java.awt.Color gifCellColor;
    private final boolean drawEntireBoard;

    /**
     * Sole constructor for a GifConstructor object. Sets all needed parameters to create a
     * GifConstructor object.
     * @param gol - The GameOfLife object to be considered.
     * @param iterations - The number of iterations to create a gif with.
     * @param fps - The number of images per seconds to produce.
     * @param entireBoardBool - Boolean representing whether to draw the entire board, or just the active pattern.
     * @param backgroundColor - The background color to be drawn in the gif.
     * @param cellColor - The color of the cells to be drawn in the gif.
     * @param size - The size of the gif in pixels.
     * @see #gifGol
     * @see #gifBoard
     * @see #counter
     * @see #milliseconds
     * @see #drawEntireBoard
     * @see #gifBackgroundColor
     * @see #gifCellColor
     * @see #gifSize
     */
    public GifConstructor (GameOfLife gol, int iterations, int fps, boolean entireBoardBool, Color backgroundColor,
                           Color cellColor, int size) {
        this.gifGol = gol;
        this.gifBoard = gifGol.getPlayBoard();
        this.counter = iterations;
        this.milliseconds = 1000/fps;
        this.drawEntireBoard = entireBoardBool;
        this.gifBackgroundColor = convertToAwtColor(backgroundColor);
        this.gifCellColor = convertToAwtColor(cellColor);
        this.gifSize = size;
    }

    /**
     * Method that exports a gif to file by creating a new GIFWriter and calling the writeGoLSequenceToGif method.
     * Catches an IOException if thrown, and shows a popup to the user explaining what went wrong.
     * @param filePath - The filepath to write the new file in.
     * @see #writeGoLSequenceToGIF(GIFWriter, GameOfLife, int, java.awt.Color)
     * @see PopUpAlerts#ioeSaveError()
     * @see GIFWriter#GIFWriter(int, int, String, int)
     * @see GIFWriter#setBackgroundColor(java.awt.Color)
     */
    public void exportGif(String filePath) {
        try {
            GIFWriter gifWriter = new GIFWriter(gifSize, gifSize, filePath, milliseconds);
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
    private void writeGoLSequenceToGIF(GIFWriter writer, GameOfLife game, int counter, java.awt.Color cellColor)
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
    private void drawGifEntireBoard(GIFWriter writer, java.awt.Color cellColor) {
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
    private void drawGifPatternOnly(GIFWriter writer, java.awt.Color cellColor) {
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
    private java.awt.Color convertToAwtColor(Color color) {
        float red = (float) color.getRed();
        float green = (float) color.getGreen();
        float blue = (float) color.getBlue();
        float opacity = (float) color.getOpacity();

        return new java.awt.Color(red, green, blue, opacity);
    }
}
