package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import lieng.GIFWriter;
import model.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Oscar_000 on 09.03.2017.
 */


public class EditorController implements Initializable {
    @FXML Canvas editorCanvas;
    @FXML TextField titleField;
    @FXML TextField authorField;
    @FXML TextArea commentField;
    @FXML TextField ruleInputField;
    @FXML TextField fpsInputField;
    @FXML TextField numFramesInputField;
    @FXML CheckBox dateCheckBox;
    @FXML ColorPicker cellColorPicker;
    @FXML Canvas strip;
    @FXML ChoiceBox chooseSizeBox;
    @FXML ChoiceBox chooseDrawBox;


    private StaticBoard exportBoard;
    private StaticBoard stripBoard;
    private StaticBoard gifBoard;
    private GameOfLife gameOfLife;
    private GameOfLife stripGol;
    private GameOfLife gifGol;
    private FileHandler fileHandler = new FileHandler();
    private CanvasDrawer canvasDrawer;
    private Color currentCellColor = Color.LIMEGREEN;
    private Color currentBackgroundColor = Color.LIGHTGRAY;
    private boolean grid = false;
    private GIFWriter gifWriter;
    private double gifCellSize;
    private ObservableList<String> chooseSizeList = FXCollections.observableArrayList("640x640", "800x800",
            "1024x1024", "1200x1200", "1600x1600", "1920x1920");
    private ObservableList<String> chooseDrawList = FXCollections.observableArrayList("Entire Board", "Pattern Only");
    private int gifSize;
    private boolean drawEntireBoard = true;



    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        cellColorPicker.setValue(Color.LIMEGREEN);
        TextFormatter<String> ruleFormatter = new TextFormatter<String>(change -> {
            change.setText(change.getText().replaceAll("[^sSbB012345678/]", ""));
            return change;
        });
        ruleInputField.setTextFormatter(ruleFormatter);
        TextFormatter<String> fpsFormatter = new TextFormatter<String>( change -> {
            change.setText(change.getText().replaceAll("[^\\d]", ""));
            return change;
        });
        TextFormatter<String> numFramesFormater = new TextFormatter<String>( change -> {
            change.setText(change.getText().replaceAll("[^\\d]", ""));
            return change;
        });

        fpsInputField.setTextFormatter(fpsFormatter);
        numFramesInputField.setTextFormatter(numFramesFormater);

        chooseSizeBox.setItems(chooseSizeList);
        chooseSizeBox.getSelectionModel().select(2);
        chooseDrawBox.setItems(chooseDrawList);
        chooseDrawBox.getSelectionModel().selectFirst();
    }

    public void drawEditorBoard() {
        GraphicsContext graphicsContext = editorCanvas.getGraphicsContext2D();
        canvasDrawer.setCellDrawSize(editorCanvas.getWidth() / exportBoard.getCellGrid().length);
        canvasDrawer.drawBoard(editorCanvas, exportBoard, graphicsContext, currentCellColor, currentBackgroundColor,
                exportBoard.getCellGrid(), grid);
    }

    public void closeClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) editorCanvas.getScene().getWindow();
        currentStage.close();
    }

    public void chooseCellColor(ActionEvent actionEvent) {
        currentCellColor = cellColorPicker.getValue();
        drawEditorBoard();
    }

    public void setExportBoard(StaticBoard board) {
        this.exportBoard = board;
    }

    public void setGameOfLife(GameOfLife gOL) {
        this.gameOfLife = gOL;
    }

    public void setCanvasDrawer(CanvasDrawer cd) {
        this.canvasDrawer = cd;
    }

    public void mousePressed(MouseEvent mouseEvent) {
        canvasDrawer.drawPressed(mouseEvent, exportBoard);
        drawEditorBoard();
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        canvasDrawer.drawDragged(mouseEvent, exportBoard);
        drawEditorBoard();
    }

    public void mouseDragOver() {
        canvasDrawer.setEraseFalse();
        drawStrip();
    }

    public void clearBoardClick() {
        exportBoard.resetBoard();
        drawEditorBoard();
        drawStrip();
    }

    public void toggleGridClick() {
        grid = !grid;
        drawEditorBoard();
    }


    public void saveRLEClick() {
        int[] boundingBox = exportBoard.getBoundingBox();
        int x = Math.abs(boundingBox[1] - boundingBox[0] + 1);
        int y = Math.abs(boundingBox[3] - boundingBox[2] + 1);
        String rules = gameOfLife.getRuleString();

        byte[][] trimmedPattern = exportBoard.trim();

        String rawString = fileHandler.patternToString(trimmedPattern);
        System.out.println(rawString);
        String encodedString = fileHandler.stringToRLE(rawString);

        //Adds a new line for every 70 characters
        String splitString = encodedString.replaceAll("(.{70})", "$1\n");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Run-length encoding",
                "*.rle"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file == null) {
            return;
        }

        try {
            fileHandler.RLEtoDisk(file, x, y, rules, titleField, authorField, dateCheckBox, commentField, splitString);
        } catch (IOException ioe) {
            PopUpAlerts.ioeSaveError();
        }

        System.out.println(file.toString());
    }

    public void setRules(ActionEvent actionEvent) {
        try {
            String ruleString = ruleInputField.getText().toUpperCase();
            gameOfLife.setRuleString(ruleString);
            System.out.println(gameOfLife.getRuleString());
            ruleInputField.setPromptText(gameOfLife.getRuleString());
        } catch (RulesFormatException rfe) {
            PopUpAlerts.ruleAlert2();
        }
        drawStrip();
    }


    public void drawStrip() {
        stripGol = (GameOfLife) gameOfLife.clone();
        stripBoard = stripGol.playBoard;
        canvasDrawer.drawStripBoard(stripGol, stripBoard, strip, currentCellColor);
    }

    public void chooseSizeClick(ActionEvent actionEvent) {
        String size = (String) chooseSizeBox.getValue();

        switch (size) {
            case "640x640": gifSize = 640;
                break;
            case "800x800":  gifSize = 800;
                break;
            case "1024x1024": gifSize = 1024;
                break;
            case "1200x1200": gifSize = 1200;
                break;
            case "1600x1600": gifSize = 1600;
                break;
            case "1920x1920": gifSize = 1920;
                break;
            default: gifSize = 800;
                break;
        }

        System.out.println(gifSize);

    }

    public void chooseDrawClick (ActionEvent actionEvent) {
        String draw = (String) chooseDrawBox.getValue();

        if (draw.equals("Entire Board")) {
            System.out.println("ENTIRE FUCKING BOARD!");
            drawEntireBoard = true;
        } else if (draw.equals("Pattern Only")) {
            System.out.println("FUCKING PATTERN ONLY!!");
            drawEntireBoard = false;
        }
    }


    public void exportGif() {
        gifGol = (GameOfLife) gameOfLife.clone();
        gifBoard = gifGol.playBoard;
        if (gifSize/2 < exportBoard.getCellGrid().length) {
            PopUpAlerts.sizeBoardError();
        }

        int counter = 20;
        if (!numFramesInputField.getText().isEmpty()) {
            counter = Integer.parseInt(numFramesInputField.getText());
            if (counter < 1 || counter > 400) {
                PopUpAlerts.gifFramesAlert();
                return;
            }
        }

        int fps = 5;
        if (!fpsInputField.getText().isEmpty()) {
            fps = Integer.parseInt(fpsInputField.getText());
            if (fps < 1 || fps > 60) {
                PopUpAlerts.gifFPSAlert();
                return;
            }
        }

        System.out.println(fps);

        int milliseconds = 1000/fps;

        java.awt.Color backgroundColor = convertToAwtColor(currentBackgroundColor);
        java.awt.Color cellColor = convertToAwtColor(currentCellColor);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graphics Interchange Format",
                "*.gif"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            String filePath = file.getPath();
            try {
                gifWriter = new GIFWriter(gifSize, gifSize, filePath, milliseconds);
                gifWriter.setBackgroundColor(backgroundColor);
                writeGoLSequenceToGIF(gifWriter, gifGol, counter, cellColor);
            } catch (IOException ioe) {
                PopUpAlerts.ioeSaveError();
            }
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
            if (drawEntireBoard) {
                drawGifEntireBoard(writer, cellColor);
            } else if (!drawEntireBoard) {
                drawGifPatternOnly(writer, cellColor);
            }
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
}

