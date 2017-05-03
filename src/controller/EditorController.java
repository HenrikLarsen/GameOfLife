package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import java.io.File;
import java.io.IOException;


/**
 * The EditorController handles all user-interaction within the export/editor window.
 * It contains the methods and parameters linked to the graphical user interface elements
 * that the user can interact with, and handles the changes that happen based on what the
 * user does within the application.
 *
 * @author Oscar Vladau-Husevold
 * @version 1.0
 */


public class EditorController implements Initializable {

    //FXML fields
    @FXML Canvas editorCanvas;
    @FXML TextField titleField;
    @FXML TextField authorField;
    @FXML TextArea commentField;
    @FXML TextField ruleInputField;
    @FXML TextField fpsInputField;
    @FXML TextField numFramesInputField;
    @FXML TextField resizeBoardInput;
    @FXML CheckBox dateCheckBox;
    @FXML ColorPicker cellColorPicker;
    @FXML ColorPicker backgroundColorPicker;
    @FXML Canvas strip;
    @FXML ChoiceBox chooseSizeBox;
    @FXML ChoiceBox chooseDrawBox;

    private Board exportBoard;
    private Board stripBoard;
    private GameOfLife gameOfLife;
    private final FileHandler fileHandler = new FileHandler();
    private final CanvasDrawer canvasDrawer = new CanvasDrawer();
    private Color currentCellColor = Color.LIMEGREEN;
    private Color currentBackgroundColor = Color.LIGHTGRAY;
    private boolean grid = false;
    private int gifSize;
    private boolean drawEntireBoard = true;
    private final ObservableList<String> chooseSizeList = FXCollections.observableArrayList("640x640", "800x800",
            "1024x1024", "1200x1200", "1600x1600", "1920x1920", "2880x2880", "3840x3840");
    private final ObservableList<String> chooseDrawList = FXCollections.observableArrayList("Entire Board",
            "Pattern Only");


    /**
     * A concrete implementation of the method in interface Initializable.
     * Initializes the editor window, setting draw colors, sets formatting limitations for text boxes and
     * selects the initial option for the Choice Boxes.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     * @see #cellColorPicker
     * @see #backgroundColorPicker
     * @see #ruleInputField
     * @see #resizeBoardInput
     * @see #fpsInputField
     * @see #numFramesInputField
     * @see #chooseSizeBox
     * @see #chooseDrawBox
     */
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        //Sets initial values for ChoiceBoxes and ColorPickers
        cellColorPicker.setValue(Color.LIMEGREEN);
        backgroundColorPicker.setValue(Color.LIGHTGRAY);
        chooseSizeBox.setItems(chooseSizeList);
        chooseSizeBox.getSelectionModel().select(2);
        chooseDrawBox.setItems(chooseDrawList);
        chooseDrawBox.getSelectionModel().selectFirst();

        //Sets formatting for the text-input fields
        TextFormatter<String> ruleFormatter = new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll("[^sSbB012345678/]", ""));
            return change;
        });
        TextFormatter<String> fpsFormatter = new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll("[^\\d]", ""));
            return change;
        });
        TextFormatter<String> numFramesFormatter = new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll("[^\\d]", ""));
            return change;
        });
        TextFormatter<String> boardResizeFormatter = new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll("[^\\d]", ""));
            return change;
        });
        ruleInputField.setTextFormatter(ruleFormatter);
        resizeBoardInput.setTextFormatter(boardResizeFormatter);
        fpsInputField.setTextFormatter(fpsFormatter);
        numFramesInputField.setTextFormatter(numFramesFormatter);
    }

    /**
     * Method responsible for calling CanvasDrawers drawBoard() with the considerations of the quadratic nature
     * of the editor board. Sets cell draw size relative to which ever is largest of the boards width or height.
     * @see #editorCanvas
     * @see Board#getWidth()
     * @see Board#getHeight()
     * @see CanvasDrawer#setCellDrawSize(double)
     * @see CanvasDrawer#drawBoard(Canvas, Board, GraphicsContext, Color, Color, boolean)
     */
    public void drawEditorBoard() {
        GraphicsContext graphicsContext = editorCanvas.getGraphicsContext2D();
        int divisor;
        if (exportBoard.getWidth() >= exportBoard.getHeight()) {
            divisor = exportBoard.getWidth();
        } else {
            divisor = exportBoard.getHeight();
        }

        canvasDrawer.setCellDrawSize(editorCanvas.getWidth() / divisor);
        canvasDrawer.drawBoard(editorCanvas, exportBoard, graphicsContext,
                currentCellColor, currentBackgroundColor, grid);
    }

    /**
     * Method responsible for drawing the "Strip". Does a copy of the current GameOfLife object and
     * calls CanvasDrawers drawStripBoard() method.
     * @see #stripBoard
     * @see #gameOfLife
     * @see #stripBoard
     * @see GameOfLife#clone()
     * @see GameOfLife#getPlayBoard()
     * @see CanvasDrawer#drawStripBoard(GameOfLife, Board, Canvas, Color)
     */
    public void drawStrip() {
        GameOfLife stripGol = (GameOfLife) gameOfLife.clone();
        stripBoard = stripGol.getPlayBoard();
        canvasDrawer.drawStripBoard(stripGol, stripBoard, strip, currentCellColor);
    }

    /**
     * Method to exit the application. Is called when the user clicks on the "exit"-button.
     * @see #editorCanvas
     */
    public void closeClick() {
        Stage currentStage = (Stage) editorCanvas.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Method to resize the editor board. Since the editor board cannot be expanded by drawing such as in the
     * main application window, the user is able to resize the board by entering a value for the number
     * of cells of a quadratic cell grid. Should the user input an invalid value, a popup window will be shown.
     * Another popup will show if valid, waring the user that the currently active cells will be set as inactive
     * when resizing the board. Draws the editor and strip board after resizing.
     * @see #resizeBoardInput
     * @see #drawEditorBoard()
     * @see #drawStrip()
     * @see DynamicBoard#setGridSize(int)
     * @see PopUpAlerts#resizeAlert()
     * @see PopUpAlerts#resizeClearAlert()
     */
    public void resizeBoardOnEnter() {
        if (Integer.parseInt(resizeBoardInput.getText()) < 6 || Integer.parseInt(resizeBoardInput.getText()) > 1000) {
            PopUpAlerts.resizeAlert();
            return;
        }

        if (!resizeBoardInput.getText().isEmpty() && exportBoard instanceof DynamicBoard) {
            boolean proceedResize = PopUpAlerts.resizeClearAlert();
            if (proceedResize) {
                int size = Integer.parseInt(resizeBoardInput.getText());
                ((DynamicBoard) exportBoard).setGridSize(size);
                drawEditorBoard();
                drawStrip();
            }
        }
    }

    /**
     * Method to change the current cell color. Is called when the user interacts with the cell color picker.
     * Sets the color and draws the board.
     * @see #currentCellColor
     * @see #cellColorPicker
     * @see #drawEditorBoard()
     */
    public void chooseCellColor() {
        currentCellColor = cellColorPicker.getValue();
        drawEditorBoard();
    }

    /**
     * Method to change the current background color. Is called when the user interacts with the cell color picker.
     * Sets the color and draws the board.
     * @see #currentBackgroundColor
     * @see #backgroundColorPicker
     * @see #drawEditorBoard()
     */
    public void chooseBackgroundColor() {
        currentBackgroundColor = backgroundColorPicker.getValue();
        drawEditorBoard();
    }

    /**
     * Method to be called when the user presses the mouse on the editor board, calling CanvasDrawers
     * drawPressed() method to let the user draw active cells on the board.
     * @param mouseEvent - The event of the mouse click.
     * @see #drawEditorBoard()
     * @see CanvasDrawer#drawPressed(MouseEvent, Board, boolean)
     */
    public void mousePressed(MouseEvent mouseEvent) {
        canvasDrawer.drawPressed(mouseEvent, exportBoard, false);
        drawEditorBoard();
    }

    /**
     * Method to be called when the user drags the mouse on the editor board, calling CanvasDrawers
     * drawDragged() method to let the user draw active cells on the board by dragging.
     * @param mouseEvent - The event of the mouse drag.
     * @see #drawEditorBoard()
     * @see CanvasDrawer#drawDragged(MouseEvent, Board, boolean)
     */
    public void mouseDragged(MouseEvent mouseEvent) {
        canvasDrawer.drawDragged(mouseEvent, exportBoard, false);
        drawEditorBoard();
    }

    /**
     * Method called when the user lets go of the mouse button after a drag. Calls CanvasDrawers mouseDragOver()
     * method and draws the strip board again.
     * @see #drawStrip()
     * @see CanvasDrawer#setEraseFalse()
     */
    public void mouseDragOver() {
        canvasDrawer.setEraseFalse();
        drawStrip();
    }

    /**
     * Method called when the user clicks the clear board button. Calls Boards reset board and makes all
     * cells inactive, before drawing both the editor board and the strip.
     * @see #drawEditorBoard()
     * @see #drawStrip()
     * @see Board#resetBoard()
     */
    public void clearBoardClick() {
        exportBoard.resetBoard();
        drawEditorBoard();
        drawStrip();
    }

    /**
     * Method called when the user clicks the grid button. Lets the user choose between having the grid drawn
     * on the board or not. Sets the boolean as opposite of its value and draws the editor board.
     * @see #grid
     * @see #drawEditorBoard()
     */
    public void toggleGridClick() {
        grid = !grid;
        drawEditorBoard();
    }

    /**
     * Method called when the user presses enter within the rule text-box. Tries to set the rules of the
     * gameOfLife with the input in the text field, and produces a warning if it fails.
     * @see #ruleInputField
     * @see #drawStrip()
     * @see GameOfLife#setRuleString(String)
     * @see PopUpAlerts#ruleAlert2()
     */
    public void setRules() {
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

    /**
     * Method called when the user interacts with the gif-size choice box. Consists of a switch statement that
     * sets the gifSize according to what the user has selected.
     * @see #chooseSizeBox
     * @see #gifSize
     */
    public void chooseSizeClick() {
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
            case "2880x2880": gifSize = 2880;
                break;
            case "3840x3840": gifSize = 3840;
                break;
            default: gifSize = 800;
                break;
        }
    }

    /**
     * Method called when the user interacts with the chooseDrawBox choice box. Consists of a if-else statement that
     * sets a boolean according to what the user has selected, and is responsible for whether the GIF exported
     * should include the entire board, or just the active pattern.
     * @see #chooseDrawBox
     * @see #drawEntireBoard
     */
    public void chooseDrawClick() {
        String draw = (String) chooseDrawBox.getValue();

        if (draw.equals("Entire Board")) {
            drawEntireBoard = true;
        } else if (draw.equals("Pattern Only")) {
            drawEntireBoard = false;
        }
    }

    /**
     * Method called when the user clicks the export RLE button. Trims the pattern down to its bounding box
     * ensuring that unnecessary lines are not included, calls FileHandler's patternExportToString() and stringToRLE()
     * methods to create an encoded RLE string of the pattern and splits it up into lines of 70 characters for
     * presentation. Lets the user choose a file path to save the RLE-file to, and calls FileHandlers RLEtoDisk()
     * with all relevant metadata text-boxes as parameters to save the RLE. Produces a warning for the user
     * if the export fails.
     * @see Board#getBoundingBox()
     * @see Board#trim()
     * @see GameOfLife#getRuleString()
     * @see FileHandler#patternExportToString(byte[][])
     * @see FileHandler#stringToRLE(String)
     * @see FileHandler#RLEtoDisk(File, int, int, String, TextField, TextField, CheckBox, TextArea, String)
     * @see PopUpAlerts#ioeSaveError()
     */
    public void saveRLEClick() {

        //Creates the fields related to x, y and rules for the RLEs' metadata.
        int[] boundingBox = exportBoard.getBoundingBox();
        int x = Math.abs(boundingBox[1] - boundingBox[0] + 1);
        int y = Math.abs(boundingBox[3] - boundingBox[2] + 1);
        String rules = gameOfLife.getRuleString();

        //Trims the pattern and encodes it to the RLE format.
        byte[][] trimmedPattern = exportBoard.trim();
        String rawString = fileHandler.patternExportToString(trimmedPattern);
        String encodedString = fileHandler.stringToRLE(rawString);

        //Adds a new line for every 70 characters for presentation.
        String splitString = encodedString.replaceAll("(.{70})", "$1\n");

        //Lets the user choose a file path to save the file to.
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

    /**
     * Method called when the user clicks the export GIF button. Does several checks on the value of the input
     * fields, to check that they are valid, producing a warning if not. Gathers statistics for 50 iterations
     * to check if there is a high probability of the pattern repeating itself, and asks the user if they want
     * to use that number of iterations to potentially create a endless loop. Creates a new GifConstructor
     * object with the relevant data, and calls exportGif() to write the gif file to disk.
     * @see #drawEntireBoard
     * @see #gifSize
     * @see #numFramesInputField
     * @see #fpsInputField
     * @see #gameOfLife
     * @see Board#getWidth()
     * @see Board#getHeight()
     * @see PopUpAlerts#sizeBoardError()
     * @see PopUpAlerts#gifFramesAlert()
     * @see PopUpAlerts#gifFPSAlert()
     * @see PopUpAlerts#gifSimilarityAlert(int)
     * @see Statistics#getStatistics(GameOfLife, int)
     * @see Statistics#getHighestSimilarity(int[][])
     * @see GameOfLife#clone()
     * @see GifConstructor#GifConstructor(GameOfLife, int, int, boolean, Color, Color, int)
     * @see GifConstructor#exportGif(String)
     */
    public void saveGifClick() {

        //Does a check if the gif size is too small to fit the board, as each cell would be smaller than a pixel.
        //Produces a warning to the user explaining what can be done to remedy the issue.
        if (drawEntireBoard && (gifSize/2 < exportBoard.getWidth() || gifSize/2 < exportBoard.getHeight())) {
            PopUpAlerts.sizeBoardError();
        }

        //Checks that the number of images is valid, and between 1 and 400. Produces a warning if not.
        int counter = 20;
        if (!numFramesInputField.getText().isEmpty()) {
            counter = Integer.parseInt(numFramesInputField.getText());
            if (counter < 1 || counter > 400) {
                PopUpAlerts.gifFramesAlert();
                return;
            }
        }

        //Checks that the frames-per-second value the user has chosen is valid, produces a warning if not.
        int fps = 5;
        if (!fpsInputField.getText().isEmpty()) {
            fps = Integer.parseInt(fpsInputField.getText());
            if (fps < 1 || fps > 50) {
                PopUpAlerts.gifFPSAlert();
                return;
            }
        }

        //Gathers statistics and informs the user if there is a high probability of a repeating pattern.
        //Sets that as the number of iterations if the user chooses to do so.
        Statistics statistics = new Statistics();
        int[][] stat = statistics.getStatistics(gameOfLife, 50);
        int repeat = statistics.getHighestSimilarity(stat);

        if (repeat != 0) {
            boolean b = PopUpAlerts.gifSimilarityAlert(repeat);
            if (b) {
                counter = repeat;
            }
        }

        //Lets the user choose an output path for their gif.
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graphics Interchange Format",
                "*.gif"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file == null) {
            return;
        }
        String filePath = file.getPath();

        GameOfLife gifGol = (GameOfLife)gameOfLife.clone();
        GifConstructor gifConstructor = new GifConstructor(gifGol, counter, fps, drawEntireBoard,
                currentBackgroundColor, currentCellColor, gifSize);

        //Calls the method to export the gif.
        gifConstructor.exportGif(filePath);
    }

    /**
     * Method to set the current exportBoard object to that of the parameter.
     * @param board - The new board to be set.
     * @see #exportBoard
     */
    public void setExportBoard(Board board) {
        this.exportBoard = board;
    }

    /**
     * Method to set the current gameOfLife object to that of the parameter.
     * @param gOL - The new GameOfLife object to be set.
     * @see #exportBoard
     */
    public void setGameOfLife(GameOfLife gOL) {
        this.gameOfLife = gOL;
    }
}