package controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The Controller class handles user-interaction within the main window of the application.
 * It contains the methods and parameters linked to the graphical user interface elements
 * that the user can interact with, and handles the changes that happen based on what the
 * user does within the application.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */

public class Controller implements Initializable {

    //Fields from FXML
    @FXML private Slider speedSlider;
    @FXML private ColorPicker cellColorPicker;
    @FXML private ColorPicker backgroundColorPicker;
    @FXML private TextField sizeInputField;
    @FXML private TextField ruleInputField;
    @FXML private Canvas canvasArea;
    @FXML private Label generationLabel;
    @FXML private Label fpsLabel;
    @FXML private Label aliveLabel;
    @FXML private Label ruleLabel;
    @FXML private ChoiceBox chooseRulesBox;
    @FXML private Button startButton;
    @FXML private Button centerButton;
    @FXML private Button resetButton;
    @FXML private Button gridToggleButton;

    //List of all traversable nodes
    private List<Node> nodes = new ArrayList<>();

    private Color currentCellColor = Color.LIMEGREEN;
    private Color currentBackgroundColor = Color.LIGHTGRAY;
    private Board board = new DynamicBoard(50, 50);
    private GameOfLife gOL = new GameOfLife(board);
    private CanvasDrawer canvasDrawer = new CanvasDrawer();
    private FileHandler fileHandler = new FileHandler();
    private ThreadWorker threadWorker = ThreadWorker.getInstance();
    private Timeline timeline;
    private boolean gridToggle = true;
    private boolean isRunning = false;
    private boolean move = false;

    private Stage editorStage;
    private EditorController editorController;
    private Stage patternSelectStage;
    private Stage progressStage;
    private ProgressController progressController;

    private TextInputDialog textInputDialogStatistics = new TextInputDialog();
    private ObservableList<String> chooseRulesList = FXCollections.observableArrayList("Life", "Replicator", "Seeds",
            "Life Without Death", "34 Life", "Diamoeba", "2x2", "Highlife", "Day & Night", "Morley", "Anneal");


    /**
     * A concrete implementation of the method in interface Initializable.
     * Initializes the game, draws the first board and sets up the animation keyframe and timeline. Sets
     * formatting for text-input fields and sets initial values of ColorPickers and ChoiceBoxes.
     * Starts together with the application.
     * @see #timeline
     * @see #fpsLabel
     * @see #fileHandler
     * @see #board
     * @see #gOL
     * @see #ruleInputField
     * @see #textInputDialogStatistics
     * @see #chooseRulesBox
     * @see #cellColorPicker
     * @see #backgroundColorPicker
     * @see #draw()
     * @see #addNewKeyFrame()
     * @see #setFPS()
     * @see CanvasDrawer#setZoomOffset(Board, Canvas)
     * @see FileHandler#setBoard(Board)
     * @see FileHandler#setGol(GameOfLife)
     * @see GameOfLife#setThreadWorkers(ThreadWorker)
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        //Creates a new KeyFrame and a Timeline using that keyframe
        KeyFrame keyframe = addNewKeyFrame();
        timeline = new Timeline(keyframe);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setRate(speedSlider.getValue());

        //Sets the initial values and text of the FPS-label and the slider controlling it.
        fpsLabel.setText(Integer.toString((int)speedSlider.getValue()) + " FPS");
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {setFPS();});

        gOL.setThreadWorkers(threadWorker);

        //Sets the current board and gol to be linked with the file handler
        fileHandler.setBoard(board);
        fileHandler.setGol(gOL);

        //Sets formatting for the text-input fields.
        TextFormatter<String> formatter = new TextFormatter<String>( change -> {
            change.setText(change.getText().replaceAll("[^sSbB012345678/]", ""));
            return change;
        });
        ruleInputField.setTextFormatter(formatter);

        TextFormatter<String> onlyNumbers = new TextFormatter<String>( change -> {
            change.setText(change.getText().replaceAll("[^\\d]", ""));
            return change;
        });
        textInputDialogStatistics.getEditor().setTextFormatter(onlyNumbers);

        //Sets initial values for the choiceBoxes and colorPickers.
        chooseRulesBox.setItems(chooseRulesList);
        chooseRulesBox.getSelectionModel().selectFirst();
        cellColorPicker.setValue(currentCellColor);
        backgroundColorPicker.setValue(currentBackgroundColor);

        Node[] traversableNodes = {canvasArea, speedSlider, cellColorPicker, backgroundColorPicker, startButton,
                sizeInputField, ruleInputField, chooseRulesBox, centerButton, resetButton, gridToggleButton};
        nodes.addAll(Arrays.asList(traversableNodes));
        setFocusTraversable(true);

        //Sets the initial zoomOffset and draws the board onto the canvas.
        canvasDrawer.setZoomOffset(board, canvasArea);
        draw();
    }

    /**
     * Instantiates a new KeyFrame that includes all methods needed for each frame of the game and returns it.
     * @return KeyFrame
     * @see #generationLabel
     * @see #aliveLabel
     * @see #draw()
     * @see GameOfLife#genCounter
     * @see GameOfLife#nextGeneration()
     * @see Board#getCellsAlive()
     */
    private KeyFrame addNewKeyFrame(){
        return new KeyFrame(Duration.millis(1000), e -> {

            //Performs a check on the width of the board to see if it should run concurrently or not
            /*if (board.getWidth() < 600) {
                gOL.nextGeneration();
            } else {
                gOL.nextGenerationConcurrentPrintPerformance();
            }*/
            gOL.nextGenerationConcurrentPrintPerformance();

            draw();
            gOL.genCounter++;
            generationLabel.setText(Integer.toString(gOL.genCounter));
            aliveLabel.setText(Integer.toString(board.getCellsAlive()));
        });
    }

    /**
     * A method that calls CanvasDrawers drawBoard with the parameters relevant to the main window.
     * @see #canvasArea
     * @see #board
     * @see #currentCellColor
     * @see #currentBackgroundColor
     * @see #gridToggle
     * @see CanvasDrawer#drawBoard(Canvas, Board, GraphicsContext, Color, Color, boolean)
     */
    private void draw(){
        GraphicsContext gc = canvasArea.getGraphicsContext2D();
        canvasDrawer.drawBoard(canvasArea, board, gc, currentCellColor, currentBackgroundColor, gridToggle);
    }

    /**
     * Method to set the current FPS of the game and animation. Is called when the speedSlider listener observes
     * a value change. Sets the rate of animation and updates the fpsLabel.
     * @see #fpsLabel
     * @see #timeline
     * @see #speedSlider
     */
    private void setFPS() {
        fpsLabel.setText(Integer.toString((int)speedSlider.getValue()) + " FPS");
        timeline.setRate(speedSlider.getValue());
    }

    /**
     * Method to change the current cell color. Is called when the user interacts with the cell color picker.
     * Sets the color and calls the draw() method.
     * @see #currentCellColor
     * @see #cellColorPicker
     * @see #draw()
     */
    public void chooseCellColor(){
        currentCellColor = cellColorPicker.getValue();
        draw();
    }

    /**
     * Method to change the current background color. Is called when the user interacts with the background
     * color picker. Sets the color and calls the draw() method.
     * @see #currentBackgroundColor
     * @see #backgroundColorPicker
     * @see #draw()
     */
    public void chooseBackgroundColor(){
        currentBackgroundColor = backgroundColorPicker.getValue();
        draw();
    }

    /**
     * Method called when the user clicks the start/pause button within the game.
     * Depending on boolean isRunning, the game will start or pause the timeline. If isRunning is false, it will
     * set the text on the button to pause, the boolean to true and finalize any loaded patterns, before starting the
     * game. If it is already running, it will change the button to "start" and set relevant values before pausing
     * the timeline.
     * @param actionEvent - The event where the user clicks on the "start/pause"-button.
     * @see #isRunning
     * @see #startButton
     * @see #move
     * @see #aliveLabel
     * @see #timeline
     * @see Board#finalizeBoard()
     * @see Board#getCellsAlive()
     */
    public void startClick(ActionEvent actionEvent) {
        if(!isRunning){
            startButton.setText("Pause");
            isRunning = true;
            move = false;
            board.finalizeBoard();
            setFocusTraversable(true);
            aliveLabel.setText(Integer.toString(board.getCellsAlive()));
            draw();
            timeline.play();
        }else{
            startButton.setText("Start");
            isRunning = false;
            move = false;
            board.finalizeBoard();
            setFocusTraversable(true);
            aliveLabel.setText(Integer.toString(board.getCellsAlive()));
            draw();
            timeline.pause();
        }
    }

    /**
     * Method called when the user clicks the "center view" button within the game. Will call CanvasDrawer's
     * resetOffset() method so that the view will be back on the center of the board, and call draw().
     * @param actionEvent - The event where the user clicks on the "Center View"-button.
     * @see #board
     * @see #canvasArea
     * @see #draw()
     * @see CanvasDrawer#resetOffset(Board, Canvas)
     */
    public void backToCenterClick(ActionEvent actionEvent) {
        canvasDrawer.resetOffset(board, canvasArea);
        draw();
    }

    /**
     * Method to reset the game. Is called when the user clicks on the "reset"-button. Stops
     * the animation, sets the number of generations to 0 and makes all cells dead. Should the Board be an
     * instance of DynamicBoard, it will reset the grid to the default value.
     * @param actionEvent - The event where the user clicks on the "reset"-button.
     * @see #timeline
     * @see #startButton
     * @see #isRunning
     * @see #move
     * @see #generationLabel
     * @see #aliveLabel
     * @see #draw()
     * @see GameOfLife#genCounter
     * @see Board#resetBoard()
     * @see Board#getCellsAlive()
     * @see DynamicBoard#setGridSize(int)
     * @see CanvasDrawer#resetCellSize()
     * @see CanvasDrawer#resetOffset(Board, Canvas)
     * @see FileHandler#resetMetaData()
     */
    public void resetClick(ActionEvent actionEvent) {
        timeline.stop();
        startButton.setText("Start");
        isRunning = false;
        move = false;
        gOL.genCounter = 0;
        generationLabel.setText(Integer.toString(gOL.genCounter));
        board.resetBoard();
        if (board instanceof DynamicBoard) {
            ((DynamicBoard)board).setGridSize(50);
            canvasDrawer.resetCellSize();
        }
        aliveLabel.setText(Integer.toString(board.getCellsAlive()));
        fileHandler.resetMetaData();
        canvasDrawer.resetOffset(board, canvasArea);
        draw();
    }

    /**
     * Method to exit the application. Is called when the user clicks on the "exit"-button.
     * @param actionEvent - The event where the user clicks on the "exit"-button.
     */
    public void closeClick(ActionEvent actionEvent) {
        timeline.stop();
        threadWorker.shutDownExecutor();
        Platform.exit();
    }

    /**
     * Method used to toggle the grid on of off. Is called when user clicks on the "grid"-button.
     * @param actionEvent - The event where the user clicks on the "grid"-button.
     * @see #gridToggle
     * @see #draw()
     */
    public void gridClick(ActionEvent actionEvent) {
        gridToggle = !gridToggle;
        draw();
    }

    /**
     * Method that sets the size the size of the cellGrid quadratically. Is called when the user presses enter
     * while within the sizeInputField textfield.
     * @param actionEvent - The event where the user presses enter when within the Textfield box.
     * @see #sizeInputField
     * @see #draw()
     * @see DynamicBoard#setGridSize(int)
     * @see CanvasDrawer#resetOffset(Board, Canvas)
     */
    public void cellSizeOnEnter(ActionEvent actionEvent) {

        //Checks that the input contains something and that the board is an instance of DynamicBoard
        if (!sizeInputField.getText().isEmpty() && board instanceof DynamicBoard) {
            int size = Integer.parseInt(sizeInputField.getText());

            //Sets the grid size to that of the input
            ((DynamicBoard)board).setGridSize(size);
        }

        //Resets the offsets and draws the board.
        canvasDrawer.resetOffset(board, canvasArea);
        draw();
    }

    /**
     * Method that lets the user interact with the board by pressing the mouse button on it. If the left mouse
     * button is clicked, it will call the method to draw cells on the board, while the right button will set an
     * initial value for panning, if the user ends up dragging the mouse.
     * @param mouseEvent - The event where the user presses the left mouse button on the canvas.
     * @see #aliveLabel
     * @see #canvasArea
     * @see #board
     * @see #draw()
     * @see CanvasDrawer#drawPressed(MouseEvent, Board, boolean)
     * @see CanvasDrawer#setOriginalDrag(MouseEvent)
     * @see Board#getCellsAlive()
     */
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            canvasDrawer.drawPressed(mouseEvent, board, true);
            aliveLabel.setText("" + board.getCellsAlive());
        }else if (mouseEvent.isSecondaryButtonDown()) {
            canvasDrawer.setOriginalDrag(mouseEvent);
            canvasArea.setCursor(Cursor.MOVE);
        }
        draw();
    }

    /**
     * Method that lets the user interact with the board by pressing the mouse button on it. If the left mouse
     * button is clicked, it will call the method to draw cells on the board, while the right button will allow
     * for panning around the canvas, allowing the user to move around the board visually.
     * @param mouseEvent - The event where the user presses the left mouse button on the canvas.
     * @see #aliveLabel
     * @see #canvasArea
     * @see #board
     * @see #draw()
     * @see CanvasDrawer#drawDragged(MouseEvent, Board, boolean)
     * @see CanvasDrawer#setDragOffset(MouseEvent)
     * @see Board#getCellsAlive()
     */
    public void mouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            canvasDrawer.drawDragged(mouseEvent, board, true);
            aliveLabel.setText("" + board.getCellsAlive());
        } else if (mouseEvent.isSecondaryButtonDown()) {
            canvasDrawer.setDragOffset(mouseEvent);
        }
        draw();
    }

    /**
     * Method that is called when the user releases the mouse button, calling CanvasDrawer's setEraseFalse() method.
     * @see #canvasArea
     * @see CanvasDrawer#setEraseFalse()
     */
    public void mouseDragOver() {
        canvasDrawer.setEraseFalse();
        canvasArea.setCursor(Cursor.DEFAULT);
    }

    /**
     * Method for setting forcus traversable for all traversable nodes. Used when importing a
     * pattern to allow the arrow keys to move the pattern without moving the focus.
     * @param b - The value to set the node's traversable property.
     */
    public void setFocusTraversable(boolean b) {
        for (Node n : nodes) n.setFocusTraversable(b);
    }

    /**
     * Method that allows for importing an RLE-file from disk. Lets the user choose a file-path to load and
     * if valid will load that file through the FileHandler object. Will produce a warning if the file location
     * is invalid.
     * @param actionEvent - The event in which the user clicks the import from file button
     * @see #timeline
     * @see #generationLabel
     * @see #aliveLabel
     * @see #ruleLabel
     * @see #canvasArea
     * @see #draw()
     * @see GameOfLife#genCounter
     * @see FileHandler#readGameBoardFromDisk(File)
     * @see PopUpAlerts#ioAlertFromDisk()
     * @see CanvasDrawer#resetOffset(Board, Canvas)
     * @see Board#getCellsAlive()
     */
    public void importFileClick(ActionEvent actionEvent) {

        //Lets the user choose a file location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Run-length encoding",
                "*.rle"));
        File file = fileChooser.showOpenDialog(new Stage());

        //If a file was chosen, will stop the game and set the generation to 0 and try to load the file.
        if (file != null) {
            timeline.stop();
            gOL.genCounter = 0;
            generationLabel.setText(Integer.toString(gOL.genCounter));
            try {
                fileHandler.readGameBoardFromDisk(file);
            } catch (IOException ie) {
                //Produces a warning if unsuccessful
                PopUpAlerts.ioAlertFromDisk();
            }
        }

        aliveLabel.setText(Integer.toString(board.getCellsAlive()));
        ruleLabel.setText(gOL.getRuleString().toUpperCase());
        move = true;
        canvasArea.requestFocus();
        setFocusTraversable(false);

        //Resets offset to accommodate for the new pattern and calls draw().
        canvasDrawer.resetOffset(board, canvasArea);
        draw();
    }

    /**
     * Method that allows for importing an RLE-file from an URL. Lets the user input an URL and
     * if valid will load the URL through the FileHandler object. Will produce a warning if the URL is
     * is invalid, or does not contain an RLE file.
     * @param actionEvent - The event in which the user clicks the import from URL button
     * @see #timeline
     * @see #generationLabel
     * @see #aliveLabel
     * @see #ruleLabel
     * @see #canvasArea
     * @see #draw()
     * @see GameOfLife#genCounter
     * @see FileHandler#readGameBoardFromURL(String)
     * @see PopUpAlerts#ioAlertFromURL()
     * @see CanvasDrawer#resetOffset(Board, Canvas)
     * @see Board#getCellsAlive()
     */
    public void importURLClick(ActionEvent actionEvent) {

        //Creates an input dialog for the user to write an URL
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setHeaderText("Import RLE from URL");
        textInputDialog.setContentText("Enter URL");
        textInputDialog.showAndWait();
        String url = textInputDialog.getResult();

        //If a String is present, will try to load it, and produce warning if not able to.
        if (url != null) {
            timeline.stop();
            gOL.genCounter = 0;
            generationLabel.setText(Integer.toString(gOL.genCounter));
            try {
                fileHandler.readGameBoardFromURL(url);
                aliveLabel.setText(Integer.toString(board.getCellsAlive()));
                ruleLabel.setText(gOL.getRuleString().toUpperCase());
            } catch (IOException ie) {
                PopUpAlerts.ioAlertFromURL();
            }
        }
        move = true;
        canvasArea.requestFocus();
        setFocusTraversable(false);

        //Resets offset to accommodate for the new pattern and calls draw().
        canvasDrawer.resetOffset(board, canvasArea);
        draw();
    }

    /**
     * Method called when pressing enter within the rules input box. Allows for inputting custom rules, and will
     * try setting them. Checks the predefined set of rules to take metadata from.
     * Produces a warning if the formatting is wrong.
     * @param actionEvent - The event in which the user presses enter while in the text-input field
     * @see #ruleInputField
     * @see #ruleLabel
     * @see #chooseRulesBox
     * @see GameOfLife#setRuleString(String)
     * @see PopUpAlerts#ruleAlert2()
     */
    public void rulesOnEnter(ActionEvent actionEvent) {
        try {
            String ruleString = ruleInputField.getText().toUpperCase();
            gOL.setRuleString(ruleString);
            ruleLabel.setText(gOL.getRuleString().toUpperCase());
            ruleInputField.setText("");

            //Checks if the input matches any predefined rules. Clears the selection if not.
            if(!chooseRulesList.contains(gOL.getRuleName())){
                chooseRulesBox.getSelectionModel().clearSelection();
            }else{
                chooseRulesBox.getSelectionModel().select(gOL.getRuleName());
            }

        //Produces a warning if a RulesFormatException is thrown
        } catch (RulesFormatException rfe) {
            PopUpAlerts.ruleAlert2();
        }
    }

    /**
     * Method called when choosing an element in the rule choice box. Sets the current playing rules to
     * what is chosen.
     * Produces a warning if something goes wrong.
     * @param actionEvent - The event in which the user selects an element in the choice box.
     * @see #chooseRulesBox
     * @see #ruleLabel
     * @see GameOfLife#getRuleString()
     * @see GameOfLife#setRuleString(String)
     * @see PopUpAlerts#ruleAlert2()
     */
    public void chooseRulesClick(ActionEvent actionEvent){
        String rules = (String)chooseRulesBox.getValue();
        try{
            gOL.setRuleString(rules);
        }catch (RulesFormatException rfee) {
            PopUpAlerts.ruleAlert2();
        }
        ruleLabel.setText(gOL.getRuleString().toUpperCase());
    }

    /**
     * Method called when the user clicks the view rules description button. Calls the ruleDescription() method
     * from PopUpAlerts to produce a popup window containing a description of the selected rules.
     * @param actionEvent - The event in which the user clicks the view Rules Description button
     * @see GameOfLife#getRuleName()
     * @see GameOfLife#getRuleString()
     * @see GameOfLife#getRuleDescription()
     * @see PopUpAlerts#ruleDescription(String, String, String)
     */
    public void showRuleDescription(ActionEvent actionEvent) {
        PopUpAlerts.ruleDescription(gOL.getRuleName(), gOL.getRuleString(), gOL.getRuleDescription());
    }

    /**
     * Method called when the user clicks the view metadata button. Calls the metaData method
     * from PopUpAlerts to produce a popup window containing a description of the selected rules.
     * @param actionEvent - The event in which the user clicks the view metadata button
     * @see FileHandler#getMetaTitle()
     * @see FileHandler#getMetaData()
     * @see PopUpAlerts#metaData(String, String)
     */
    public void showMetadata(ActionEvent actionEvent) {
        String title;
        String description;
        if (fileHandler.metaTitle.equals("")) {
            title = "No title";
        } else {
            title = fileHandler.metaTitle;
        }
        if (fileHandler.metaData.equals("")) {
            description = "No description available. Try loading an RLE-file!";
        } else {
            description = fileHandler.metaData;
        }
        PopUpAlerts.metaData(title, description);
    }

    /**
     * Method called when the user presses a keyboard button related to moving or rotating a loaded pattern. Will
     * check if boolean move is true, and return otherwise. If true, it will call the related method from the Board
     * class in order to move or rotate the loaded pattern. Move will only be true if a pattern is loaded and the
     * game is not running.
     * @param keyEvent - The key pressed by the user.
     * @see #move
     * @see #draw()
     * @see Board#getCellsAlive()
     * @see Board#movePattern(String)
     * @see Board#rotate(boolean)
     * @see Board#finalizeBoard()
     * @see Board#discardPattern()
     */
    public void movePattern(KeyEvent keyEvent) {
        //Returns if boolean move is false
        if (!move) {
            return;
        }

        //Calls movePattern() from Board if the keys correspond with pattern movement (arrow or WASD)
        if(keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.UP) {
            board.movePattern("up");
        } else if (keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.DOWN) {
            board.movePattern("down");
        } else if (keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT){
            board.movePattern("left");
        } else if (keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT) {
            board.movePattern("right");

        //If the key is Q or E, it will call method to rotate counter-clockwise or clockwise respectively.
        } else if (keyEvent.getCode() == KeyCode.Q || keyEvent.getCode() == KeyCode.PERIOD) {
            board.rotate(false);
        } else if (keyEvent.getCode() == KeyCode.E || keyEvent.getCode() == KeyCode.MINUS) {
            board.rotate(true);

        //Should the key be ENTER, it will call Board's finalizeBoard() method and update the aliveLabel.
        } else if (keyEvent.getCode() == KeyCode.ENTER) {
            board.finalizeBoard();
            aliveLabel.setText(Integer.toString(board.getCellsAlive()));
            move = false;
            setFocusTraversable(true);

        //Should the key be ESCAPE, it will call Board's discardPattern() method.
        } else if (keyEvent.getCode() == KeyCode.BACK_SPACE || keyEvent.getCode() == KeyCode.ESCAPE) {
            board.discardPattern();
            move = false;
            setFocusTraversable(true);
        }
        draw();
    }

    /**
     * Method related to zooming with the mouse wheel. Called when the user scrolls. Depending on what the
     * current cell size is, it will divide the scrollEvent accordingly, so that the level of zoom remains
     * relatively consistent. Calls CanvasDrawer's setZoom() and draw().
     * @param scrollEvent - The event in which the user scrolls with the mouse wheel.
     * @see #draw()
     * @see CanvasDrawer#getCellDrawSize()
     * @see CanvasDrawer#setZoomOffset(Board, Canvas)
     */
    public void scrollZoom (ScrollEvent scrollEvent){
        //Sets the zoom value from the deltaY value of the scroll event
        double zoom = scrollEvent.getDeltaY()/40;

        //Divides the zoom value relative to the current cell draw size.
        if (canvasDrawer.getCellDrawSize() < 3) {
            zoom = zoom / 16;
        } else if (canvasDrawer.getCellDrawSize() < 5) {
            zoom = zoom / 8;
        } else if (canvasDrawer.getCellDrawSize() < 8) {
            zoom = zoom / 4;
        } else if (canvasDrawer.getCellDrawSize() < 20 || canvasDrawer.getCellDrawSize() > 5) {
            zoom = zoom / 2;
        }

        canvasDrawer.setZoom(canvasDrawer.getCellDrawSize() + zoom, canvasArea, board);
        draw();
    }

    /**
     * Method called when the user presses the "export" button. Pauses the game and opens the export/editor window.
     * Sets the GameOfLife and Board objects, and re-sizes the board if it is non-quadratically and instance of
     * DynamicBoard. Should the operation fail, the user will get a message.
     * @param actionEvent - The event in which the user presses the export button.
     * @see #timeline
     * @see #editorStage
     * @see #editorController
     * @see Board#finalizeBoard()
     * @see Board#discardPattern()
     * @see Board#getHeight()
     * @see Board#getWidth()
     * @see DynamicBoard#expandWidthRight(int)
     * @see DynamicBoard#expandHeightDown(int)
     * @see EditorController#setExportBoard(Board)
     * @see EditorController#setGameOfLife(GameOfLife)
     * @see EditorController#drawEditorBoard()
     * @see EditorController#drawStrip()
     * @see CanvasDrawer#resetOffset(Board, Canvas)
     * @see PopUpAlerts#ioAlertFXML()
     */
    public void editorButtonClick(ActionEvent actionEvent) {
        timeline.pause();

        //Creates a new Stage and loader. Sets Modality to WINDOW_MODAL.
        editorStage = new Stage();
        editorStage.initModality(Modality.WINDOW_MODAL);
        editorStage.initOwner(canvasArea.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Editor.fxml"));

        //Produces a warning to the user if the board size is larger than what the editor window can draw
        if (board.getWidth() > 1200 || board.getHeight() > 1200) {
            PopUpAlerts.editorSizeAlert();
        }

        try {
            //Tries to load the fxml and sets editorController from that.
            Parent root = loader.load();
            editorController = loader.getController();

            //Finalizes any loaded patterns
            board.finalizeBoard();
            board.discardPattern();
            setFocusTraversable(true);

            //Re-sizes the board quadratically to fit within the editor.
            if (board.getHeight() > board.getWidth() && board instanceof DynamicBoard) {
                ((DynamicBoard) board).expandWidthRight(board.getHeight() - board.getWidth());
            } else if (board.getWidth() > board.getHeight() && board instanceof DynamicBoard) {
                ((DynamicBoard) board).expandHeightDown(board.getWidth() - board.getHeight());
            }

            //Assigns the current Board and GameOfLife to the editor
            editorController.setExportBoard(board);
            editorController.setGameOfLife(gOL);

            //Draws the initial boards on the canvases
            editorController.drawEditorBoard();
            editorController.drawStrip();

            editorStage.setTitle("GameOfLife");
            editorStage.setScene(new Scene(root, 800, 600));
            editorStage.showAndWait();

            //Resets the offsets and redraws the board when the editor closes.
            canvasDrawer.resetOffset(board, canvasArea);
            draw();
            ruleLabel.setText(gOL.getRuleString().toUpperCase());

        } catch (IOException ioe){
            //Shows a warning should the loading of the FXML fail.
            PopUpAlerts.ioAlertFXML();
        }
    }

    /**
     * Method called when the user presses the "Show Statistics" button. Pauses the game and opens a text input dialog
     * for the user to choose how many iterations to show statistics for.
     * @param actionEvent - The event in which the user presses the show statistics button.
     * @see #timeline
     * @see #startButton
     * @see #isRunning
     * @see #textInputDialogStatistics
     * @see #progressStage
     * @see #progressController
     * @see StatisticsController#makeChart()
     * @see ProgressController#setIterations(int)
     * @see ProgressController#setGameOfLife(GameOfLife)
     * @see PopUpAlerts#ioAlertFXML()
     */
    public void showStatistic(ActionEvent actionEvent) {
        timeline.pause();
        startButton.setText("Start");
        isRunning = false;

        //Creates a text input dialog window so that the user can choose the number of iterations.
        textInputDialogStatistics.setHeaderText("Show Statistics");
        textInputDialogStatistics.setContentText("Enter statistic length");
        Optional<String> result =textInputDialogStatistics.showAndWait();

        String out = "";

        //Checks if the input dialog is empty, and else sets the input as "out"
        if(result.isPresent() && !textInputDialogStatistics.getResult().isEmpty()){
            out = textInputDialogStatistics.getResult();
        }

        if(out != ""){
            try {
                //Tries to load the fxml and sets the statisticsController from that.
                int iterations = Integer.parseInt(out);

                progressStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Progress.fxml"));
                Parent root = loader.load();
                progressController = loader.getController();

                //Sets iterations and the GameOfLife object to be considered.
                progressController.setIterations(iterations);
                progressController.setGameOfLife(gOL);

                //Opens and waits
                progressStage.setTitle("Loading statistics..");
                progressStage.setScene(new Scene(root, 300, 150));
                progressStage.showAndWait();

            } catch (IOException ioe){
                //Shows a warning should the loading of the FXML fail.
                PopUpAlerts.ioAlertFXML();
            }
        }
    }

    /**
     * Method called when the user presses the "Import from Preset" button. Pauses the game and opens the import
     * from preset.
     * @param actionEvent - The event in which the user presses the Import from preset button.
     * @see #timeline
     * @see #startButton
     * @see #isRunning
     * @see #patternSelectStage
     * @see #canvasArea
     * @see #fileHandler
     * @see PatternSelectController#setFileHandler(FileHandler)
     * @see CanvasDrawer#resetOffset(Board, Canvas)
     * @see PopUpAlerts#ioAlertFXML()
     */
    public void openPatternSelect(ActionEvent actionEvent) {
        timeline.pause();
        startButton.setText("Start");
        isRunning = false;

        //Creates a new stage with modality WINDOW_MODAL and loads it.
        patternSelectStage = new Stage();
        patternSelectStage.initModality(Modality.WINDOW_MODAL);
        patternSelectStage.initOwner(canvasArea.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/PatternSelect.fxml"));
        try {
            //Tries to load the fxml and sets the patternPickerController from that.
            Parent root = fxmlLoader.load();
            PatternSelectController patternPickerController = fxmlLoader.getController();
            patternPickerController.setFileHandler(fileHandler);

            //Opens and waits
            patternSelectStage.setTitle("GameOfLife");
            patternSelectStage.setScene(new Scene(root, 600, 400));
            patternSelectStage.showAndWait();

            //Resets and redraws when the window is closed.
            move = true;
            canvasDrawer.resetOffset(board, canvasArea);
            draw();
            canvasArea.requestFocus();
            setFocusTraversable(false);
        }catch (IOException ioe){
            //Shows a warning should the loading of the FXML fail.
            PopUpAlerts.ioAlertFXML();
        }
    }

    /**
     * Method called when the user presses the "About / Help" button. Opens an informational window with
     * information about the game and how to play it.
     * @see #fileHandler
     * @see AboutController
     */
    public void showAbout() {
        Stage aboutStage = new Stage();
        FXMLLoader aboutLoader = new FXMLLoader(getClass().getResource("../view/About.fxml"));
        try {
            //Tries to load the fxml for the about window
            Parent root = aboutLoader.load();

            //Opens and waits
            aboutStage.setTitle("About");
            aboutStage.setScene(new Scene(root, 600, 500));
            aboutStage.showAndWait();

        }catch (IOException ioe){
            //Shows a warning should the loading of the FXML fail.
            PopUpAlerts.ioAlertFXML();
        }
    }
}