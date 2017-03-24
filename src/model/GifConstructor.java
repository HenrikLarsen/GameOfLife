package model;

import javafx.scene.paint.Color;

import lieng.GIFWriter;

import java.io.IOException;

/**
 * Created by Oscar Vladau on 24.03.2017.
 */
public class GifConstructor {
    private double gifCellSize;
    private int gifSize;
    private StaticBoard gifBoard;
    private GameOfLife gifGol;
    private int counter;
    private int milliseconds;
    private java.awt.Color gifBackgroundColor;
    private java.awt.Color gifCellColor;
    private lieng.GIFWriter gifWriter;
    private boolean drawEntireBoard;


    public void exportGif(String filePath) {
        try {
            gifWriter = new GIFWriter(gifSize, gifSize, filePath, milliseconds);
            gifWriter.setBackgroundColor(gifBackgroundColor);
            writeGoLSequenceToGIF(gifWriter, gifGol, counter, gifCellColor);
        } catch (IOException ioe) {
            PopUpAlerts.ioeSaveError();
        }
    }

    void writeGoLSequenceToGIF(GIFWriter writer, GameOfLife game, int counter, java.awt.Color cellColor)
            throws IOException {
        if (counter == 0) {
            writer.close();
            System.out.println("DONE!");
        }
        else {
            writer.createNextImage();
            if (drawEntireBoard)
                drawGifEntireBoard(writer, cellColor);
            else if (!drawEntireBoard)
                drawGifPatternOnly(writer, cellColor);
            writer.insertCurrentImage();
            game.nextGeneration();
            System.out.println(counter);
            writeGoLSequenceToGIF(writer, game, counter-1, cellColor);
        }
    }

    public void drawGifEntireBoard(GIFWriter writer, java.awt.Color cellColor) {
        gifCellSize = gifSize/gifBoard.getCellGrid().length;
        int cellDrawSize = (int) gifCellSize - 1;
        int offset = (gifSize - (cellDrawSize * gifBoard.getCellGrid().length)) / 2;
        for (int x = 1; x <= gifBoard.getCellGrid().length; x++) {
            for (int y = 1; y <= gifBoard.getCellGrid()[0].length; y++) {
                if (gifBoard.getCellGrid()[x - 1][y - 1] == 1) {
                    writer.fillRect((x * cellDrawSize) - cellDrawSize + offset, (x * cellDrawSize) + offset,
                            (y * cellDrawSize) - cellDrawSize + offset, (y * cellDrawSize) + offset, cellColor);
                }
            }
        }
    }

    public void drawGifPatternOnly(GIFWriter writer, java.awt.Color cellColor) {
        byte[][] trimmed = gifBoard.trim();
        int cellDrawSize;

        if (trimmed.length >= trimmed[0].length) {
            gifCellSize = gifSize/trimmed.length;
            cellDrawSize = (int) gifCellSize - 1;
        } else {
            gifCellSize = gifSize/trimmed[0].length;
            cellDrawSize = (int) gifCellSize - 1;
        }

        int offsetX = (gifSize - (cellDrawSize * trimmed.length)) / 2;
        int offsetY = (gifSize - (cellDrawSize * trimmed[0].length)) / 2;

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

    public java.awt.Color convertToAwtColor(Color color) {
        float red = (float) color.getRed();
        float green = (float) color.getGreen();
        float blue = (float) color.getBlue();
        float opacity = (float) color.getOpacity();

        java.awt.Color newColor = new java.awt.Color(red, green, blue, opacity);

        return newColor;
    }

    public void setGifGol(GameOfLife gameOfLife) {
        gifGol = gameOfLife;
        gifBoard = gifGol.playBoard;
    }

    public void setCounter(int iterations) {
        counter = iterations;
    }

    public void setMilliseconds(int fps) {
        milliseconds = 1000/fps;
    }

    public void setGifBackgroundColor(Color color) {
        gifBackgroundColor = convertToAwtColor(color);
    }

    public void setGifCellColor(Color color) {
        gifCellColor = convertToAwtColor(color);
    }

    public void setDrawEntireBoard(boolean yesOrNo) {
        drawEntireBoard = yesOrNo;
    }

    public void setGifSize(int size) {
        gifSize = size;
    }
}
