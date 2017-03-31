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
    @FXML ColorPicker backgroundColorPicker;
    @FXML Canvas strip;
    @FXML ChoiceBox chooseSizeBox;
    @FXML ChoiceBox chooseDrawBox;



    private Board exportBoard;
    private Board stripBoard;
    private GameOfLife gameOfLife;
    private GameOfLife stripGol;
    private FileHandler fileHandler = new FileHandler();
    private CanvasDrawer canvasDrawer = new CanvasDrawer();
    private Color currentCellColor = Color.LIMEGREEN;
    private Color currentBackgroundColor = Color.LIGHTGRAY;
    private boolean grid = false;
    private ObservableList<String> chooseSizeList = FXCollections.observableArrayList("640x640", "800x800",
            "1024x1024", "1200x1200", "1600x1600", "1920x1920");
    private ObservableList<String> chooseDrawList = FXCollections.observableArrayList("Entire Board", "Pattern Only");
    private int gifSize;
    private boolean drawEntireBoard = true;



    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        cellColorPicker.setValue(Color.LIMEGREEN);
        backgroundColorPicker.setValue(Color.LIGHTGRAY);
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
        int divisor;
        if (exportBoard.getWidth() >= exportBoard.getHeight()) {
            divisor = exportBoard.getWidth();
        } else {
            divisor = exportBoard.getHeight();
        }

        canvasDrawer.setCellDrawSize(editorCanvas.getWidth() / divisor);
        canvasDrawer.drawBoard(editorCanvas, exportBoard, graphicsContext, currentCellColor, currentBackgroundColor, grid);
    }

    public void drawStrip() {
        stripGol = (GameOfLife) gameOfLife.clone();
        stripBoard = stripGol.getPlayBoard();
        canvasDrawer.drawStripBoard(stripGol, stripBoard, strip, currentCellColor);
    }

    public void closeClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) editorCanvas.getScene().getWindow();
        currentStage.close();
    }

    public void chooseCellColor(ActionEvent actionEvent) {
        currentCellColor = cellColorPicker.getValue();
        drawEditorBoard();
    }

    public void chooseBackgroundColor(ActionEvent actionEvent) {
        currentBackgroundColor = backgroundColorPicker.getValue();
        drawEditorBoard();
    }

    public void setExportBoard(Board board) {
        this.exportBoard = board;
    }

    public void setGameOfLife(GameOfLife gOL) {
        this.gameOfLife = gOL;
    }

    public void mousePressed(MouseEvent mouseEvent) {
        canvasDrawer.drawPressed(mouseEvent, exportBoard, false);
        drawEditorBoard();
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        canvasDrawer.drawDragged(mouseEvent, exportBoard, false);
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


    public void saveGifClick() {
        if (drawEntireBoard && (gifSize/2 < exportBoard.getWidth() || gifSize/2 < exportBoard.getHeight())) {
            PopUpAlerts.sizeBoardError();
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graphics Interchange Format",
                "*.gif"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file == null) {
            return;
        }

        String filePath = file.getPath();

        GifConstructor gifConstructor = new GifConstructor();
        gifConstructor.setGifGol((GameOfLife) gameOfLife.clone());

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

        gifConstructor.setCounter(counter);
        gifConstructor.setMilliseconds(fps);
        gifConstructor.setDrawEntireBoard(drawEntireBoard);
        gifConstructor.setGifBackgroundColor(currentBackgroundColor);
        gifConstructor.setGifCellColor(currentCellColor);
        gifConstructor.setGifSize(gifSize);

        gifConstructor.exportGif(filePath);
    }
}