package controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

import java.io.File;
import java.io.IOException;

/**
 * The Controller class handles all user-interaction within the application.
 * It contains the methods and parameters linked to the graphical user interface elements
 * that the user can interact with, and handles the changes that happen based on what the
 * user does within the application.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */

public class Controller implements Initializable {
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

    private Color currentCellColor = Color.LIMEGREEN;
    private Color currentBackgroundColor = Color.LIGHTGRAY;
    private StaticBoard board = new StaticBoard();
    private GameOfLife gOL = new GameOfLife(board);
    private CanvasDrawer canvasDrawer = new CanvasDrawer();
    private Timeline timeline;
    private boolean gridToggle = false;
    private FileHandler fileHandler = new FileHandler();
    private Stage editorStage;
    private EditorController editorController;
    private Statistics statistics;
    private Stage statisticStage;
    private StatisticsController statisticsController;

    private ObservableList<String> chooseRulesList = FXCollections.observableArrayList("Life", "Replicator", "Seeds",
            "Life Without Death", "34 Life", "Diamoeba", "2x2", "Highlife", "Day & Night", "Morley", "Anneal");
    private boolean move = false;
    private MouseEvent initialDrag;


    /**
     * A concrete implementation of the method in interface Initializable.
     * Initializes the game, draws the first board and sets up the animation keyframe and timeline.
     * Starts together with the application.
     *
     * @see #draw()
     * @see #addNewKeyFrame()
     * @see javafx.animation.Timeline#Timeline()
     * @see javafx.animation.Timeline#setCycleCount(int)
     * @see javafx.animation.Timeline#setRate(double)
     * @see javafx.scene.control.ColorPicker#setValue(Object)
     * @see javafx.scene.control.Label#setText(String)
     * @see Slider#valueProperty()
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        KeyFrame keyframe = addNewKeyFrame();
        timeline = new Timeline(keyframe);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setRate(speedSlider.getValue());
        fpsLabel.setText(Integer.toString((int)speedSlider.getValue()) + " FPS");
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {setFPS();});
        fileHandler.playBoard = board;
        fileHandler.gameOfLife = gOL;
        TextFormatter<String> formatter = new TextFormatter<String>( change -> {
            change.setText(change.getText().replaceAll("[^sSbB012345678/]", ""));
            return change;
        });
        ruleInputField.setTextFormatter(formatter);

        chooseRulesBox.setItems(chooseRulesList);
        chooseRulesBox.getSelectionModel().selectFirst();
        cellColorPicker.setValue(currentCellColor);
        backgroundColorPicker.setValue(currentBackgroundColor);
        draw();
    }

    /**
     * Instantiates a new KeyFrame that includes all methods needed for each frame of the game and returns it.
     * @see #draw()
     * @see GameOfLife#genCounter
     * @see GameOfLife#nextGeneration()
     * @see javafx.animation.KeyFrame#KeyFrame(Duration, KeyValue...)
     * @see javafx.scene.control.Label#setText(String)
     * @return KeyFrame
     */
    private KeyFrame addNewKeyFrame(){
        return new KeyFrame(Duration.millis(1000), e -> {
            gOL.nextGeneration();
            draw();
            gOL.genCounter++;
            generationLabel.setText(Integer.toString(gOL.genCounter));
            aliveLabel.setText(Integer.toString(board.cellsAlive));
        });
    }

    /**
     * The main method for drawing the game onto the canvas. Iterates through each element of the play board and
     * draws it on the canvas if it is a live cell. If the gridToggle is true, it will also draw a grid around
     * each cell, dead or alive.
     * @see #gridToggle
     * @see StaticBoard
     * @see StaticBoard#cellGrid
     * @see javafx.scene.canvas.GraphicsContext
     * @see javafx.scene.canvas.GraphicsContext#setFill(Paint)
     * @see javafx.scene.canvas.GraphicsContext#fillRect(double, double, double, double)
     * @see javafx.scene.canvas.GraphicsContext#setStroke(Paint)
     * @see javafx.scene.canvas.Canvas
     * @see Canvas#getGraphicsContext2D()
     */
    private void draw(){
        GraphicsContext gc = canvasArea.getGraphicsContext2D();
        canvasDrawer.drawBoard(canvasArea, board, gc, currentCellColor, currentBackgroundColor,
                board.getCellGrid(), gridToggle);
    }

    /**
     * Method to set the current FPS of the game and animation. Is called when the speedSlider listener observes
     * a value change. Sets the rate of animation and updates the fpsLabel.
     * @see Slider#getValue()
     * @see javafx.scene.control.Label#setText(String)
     * @see javafx.animation.Animation#setRate(double)
     */
    private void setFPS() {
        fpsLabel.setText(Integer.toString((int)speedSlider.getValue()) + " FPS");
        timeline.setRate(speedSlider.getValue());
    }

    /**
     * Method to change the current cell color. Is called when the user interacts with the cell color picker.
     * Sets the color and draws the board.
     * @see #draw() draw()
     * @see javafx.scene.control.ColorPicker
     * @see ColorPicker#getValue()
     */
    public void chooseCellColor(){
        currentCellColor = cellColorPicker.getValue();
        draw();
    }

    /**
     * Method to change the current background color. Is called when the user interacts with the background
     * color picker. Sets the color and draws the board.
     * @see #draw() draw()
     * @see javafx.scene.control.ColorPicker
     * @see ColorPicker#getValue()
     */
    public void chooseBackgroundColor(){
        currentBackgroundColor = backgroundColorPicker.getValue();
        draw();
    }

    /**
     * Method to start the animation of the game. Is called when the user clicks on the "start"-button.
     * @see javafx.animation.Timeline
     * @see Timeline#play()
     * @param actionEvent - The event where the user clicks on the "start"-button.
     */
    public void startClick(ActionEvent actionEvent) {
        move = false;
        board.finalizeBoard();
        aliveLabel.setText(Integer.toString(board.cellsAlive));
        draw();
        timeline.play();
    }

    /**
     * Method to pause the animation of the game. Is called when the user clicks on the "pause"-button.
     * @see javafx.animation.Timeline
     * @see Timeline#pause()
     * @param actionEvent - The event where the user clicks on the "pause"-button.
     */
    public void pauseClick(ActionEvent actionEvent) {
        move = false;
        board.finalizeBoard();
        aliveLabel.setText(Integer.toString(board.cellsAlive));
        draw();
        timeline.pause();
    }

    /**
     * Method to reset the game. Is called when the user clicks on the "reset"-button. Stops
     * the animation, sets the number of generations to 0 and makes all cells dead.
     * @see GameOfLife#genCounter
     * @see StaticBoard#resetBoard()
     * @see javafx.animation.Timeline
     * @see Timeline#stop()
     * @see javafx.scene.control.Label#setText(String)
     * @param actionEvent - The event where the user clicks on the "reset"-button.
     */
    public void resetClick(ActionEvent actionEvent) {
        timeline.stop();
        move = false;
        gOL.genCounter = 0;
        generationLabel.setText(Integer.toString(gOL.genCounter));
        board.resetBoard();
        aliveLabel.setText(Integer.toString(board.cellsAlive));
        fileHandler.resetMetaData();
        draw();
    }

    /**
     * Method to exit the application. Is called when the user clicks on the "exit"-button.
     * @see javafx.application.Platform#exit()
     * @param actionEvent - The event where the user clicks on the "exit"-button.
     */
    public void closeClick(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Method used to toggle the grid on of off. Is called whn user clicks on the "grid"-button.
     * @see #gridToggle
     * @see #draw()
     * @param actionEvent - The event where the user clicks on the "grid"-button.
     */
    public void gridClick(ActionEvent actionEvent) {
        gridToggle = !gridToggle;
        draw();
    }

    /**
     * Method that sets the size each cell is drawn on the canvas. Is called when the user presses enter
     * while within the sizeInputField textfield.
     * @see #sizeInputField
     * @see #draw()
     * @see StaticBoard(double)
     * @see javafx.scene.control.TextField
     * @see TextField#getText()
     * @param actionEvent - The event where the user presses enter when within the Textfield box.
     */
    public void cellSizeOnEnter(ActionEvent actionEvent) {
        if (!sizeInputField.getText().isEmpty()) {
            double size = Double.parseDouble(sizeInputField.getText());
            canvasDrawer.setCellDrawSize(size);
            draw();
        }
    }

    /**
     * Method that lets the user "draw" on the canvas by clicking on the canvas, inverting the clicked cell. Is called
     * when the user click the left mouse button on the canvas.
     * @see #draw()
     * @see Board
     * @see StaticBoard#cellGrid
     * @param mouseEvent - The event where the user presses the left mouse button on the canvas.
     */
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            canvasDrawer.drawPressed(mouseEvent, board);
            aliveLabel.setText("" + board.cellsAlive);
        }else if (mouseEvent.isSecondaryButtonDown()) {
            initialDrag = mouseEvent;
            canvasArea.setCursor(Cursor.MOVE);
        }
        draw();
    }

    /**
     * Method that lets the user "draw" on the canvas by dragging the mouse on the canvas. Is an "extension" of the
     * mousePressed() method, and continues from that if the mouse is dragged. Is called when the user drags
     * the mouse while holding mouse button clicked on the canvas.
     * @see #draw()
     * @see Board
     * @see StaticBoard#cellGrid
     * @param mouseEvent - The event where the user presses the left mouse button on the canvas.
     */
    public void mouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            canvasDrawer.drawDragged(mouseEvent, board);
            aliveLabel.setText("" + board.cellsAlive);
        } else if (mouseEvent.isSecondaryButtonDown()) {
            canvasDrawer.setDragOffset(mouseEvent, initialDrag);
        }
        draw();
    }

    /**
     * Method that is called when the user releases the left mouse button. Sets global variable erase back to false.
     */
    public void mouseDragOver() {
        canvasDrawer.setEraseFalse();
        canvasArea.setCursor(Cursor.DEFAULT);
    }


    public void importFileClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"/* + "/rleFiles"*/)));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Run-length encoding",
                "*.rle"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            timeline.stop();
            gOL.genCounter = 0;
            generationLabel.setText(Integer.toString(gOL.genCounter));
            try {
                fileHandler.readGameBoardFromDisk(file);
            } catch (IOException ie) {
                PopUpAlerts.ioAlertFromDisk();
            }
        }
        aliveLabel.setText(Integer.toString(board.cellsAlive));
        ruleLabel.setText(gOL.ruleString.toUpperCase());
        move = true;
        canvasArea.requestFocus();
        draw();

    }

    public void importURLClick(ActionEvent actionEvent) {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setHeaderText("Import RLE from URL");
        textInputDialog.setContentText("Enter URL");
        textInputDialog.showAndWait();
        String url = textInputDialog.getResult();
        if (url != null) {
            timeline.stop();
            gOL.genCounter = 0;
            generationLabel.setText(Integer.toString(gOL.genCounter));
            try {
                fileHandler.readGameBoardFromURL(url);
                aliveLabel.setText(Integer.toString(board.cellsAlive));
                ruleLabel.setText(gOL.ruleString.toUpperCase());
            } catch (IOException ie) {
                PopUpAlerts.ioAlertFromURL();
            }
        }
        move = true;
        canvasArea.requestFocus();
        draw();
    }

    public void rulesOnEnter(ActionEvent actionEvent) {
        try {
            String ruleString = ruleInputField.getText().toUpperCase();
            gOL.setRuleString(ruleString);
            ruleLabel.setText(gOL.ruleString.toUpperCase());
            ruleInputField.setText("");
        } catch (RulesFormatException rfe) {
            PopUpAlerts.ruleAlert2();
        }
    }

    public void chooseRulesClick(ActionEvent actionEvent){
        String rules = (String)chooseRulesBox.getValue();
        try{
            gOL.setRuleString(rules);
        }catch (RulesFormatException rfee) {
            PopUpAlerts.ruleAlert2();
        }
        ruleLabel.setText(gOL.ruleString.toUpperCase());
    }

    public void showRuleDescription(ActionEvent actionEvent) {
        timeline.pause();
        PopUpAlerts.ruleDescription(gOL.ruleName, gOL.ruleString, gOL.ruleDescription);
        timeline.play();
    }

    public void showMetadata(ActionEvent actionEvent) {
        String title;
        String description;
        if (fileHandler.metaTitle.equals("")) {
            title = "No title";
        } else {
            title = fileHandler.metaTitle;
        }
        if (fileHandler.metaData.equals("")) {
            description = "No description avaliable. Try loading an RLE-file!";
        } else {
            description = fileHandler.metaData;
        }
        timeline.pause();
        PopUpAlerts.metaData(title, description);
        timeline.play();
    }

    public void exportButtonClick(ActionEvent actionEvent) throws Exception{
        timeline.pause();
        editorStage = new Stage();
        editorStage.initModality(Modality.WINDOW_MODAL);
        editorStage.initOwner(canvasArea.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Editor.fxml"));
        Parent root = loader.load();
        editorController = loader.getController();
        editorController.setExportBoard(board);
        editorController.setGameOfLife(gOL);
        editorController.setCanvasDrawer(canvasDrawer);
        editorController.drawEditorBoard();
        editorController.drawStrip();

        editorStage.setTitle("GameOfLife");
        editorStage.setScene(new Scene(root, 800, 600));

        editorStage.showAndWait();
        draw();
        ruleLabel.setText(gOL.ruleString.toUpperCase());
    }

    public void movePattern(KeyEvent keyEvent) {
        if (!move) {
            return;
        }
        if(keyEvent.getCode() == KeyCode.W) {
            board.movePattern("up");
        } else if (keyEvent.getCode() == KeyCode.S) {
            board.movePattern("down");
        } else if (keyEvent.getCode() == KeyCode.A){
            board.movePattern("left");
        } else if (keyEvent.getCode() == KeyCode.D) {
            board.movePattern("right");
        } else if (keyEvent.getCode() == KeyCode.Q) {
            board.rotate(false);
        } else if (keyEvent.getCode() == KeyCode.E) {
            board.rotate(true);
        } else if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) {
            board.finalizeBoard();
            aliveLabel.setText(Integer.toString(board.cellsAlive));
            move = false;
        } else if (keyEvent.getCode() == KeyCode.BACK_SPACE || keyEvent.getCode() == KeyCode.ESCAPE) {
            board.discardPattern();
            move = false;
        }
        draw();
    }

    public void scrollZoom (ScrollEvent scrollEvent){
        double zoom = scrollEvent.getDeltaY()/40;
        if (canvasDrawer.getCellDrawSize() > 160) {
            zoom = zoom*8;
        } else if (canvasDrawer.getCellDrawSize() > 80) {
            zoom = zoom*4;
        } else if (canvasDrawer.getCellDrawSize() > 40) {
            zoom = zoom*2;
        } else if (canvasDrawer.getCellDrawSize() < 3) {
            zoom = zoom / 16;
        } else if (canvasDrawer.getCellDrawSize() < 5) {
            zoom = zoom / 8;
        } else if (canvasDrawer.getCellDrawSize() < 8) {
            zoom = zoom / 4;
        } else if (canvasDrawer.getCellDrawSize() < 10) {
            zoom = zoom / 2;
        }
        canvasDrawer.setCellDrawSize(canvasDrawer.getCellDrawSize() + zoom);
        System.out.println(zoom);
        System.out.println(canvasDrawer.getCellDrawSize());
        draw();
    }

    public void showStatistic(ActionEvent actionEvent) throws Exception {
        timeline.pause();
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setHeaderText("Show Statistics");
        textInputDialog.setContentText("Enter statistic length");
        textInputDialog.showAndWait();
        String url = textInputDialog.getResult();

        if(url != null){
            int iterations = Integer.parseInt(url);
            statisticStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Statistics.fxml"));
            Parent root = loader.load();
            statisticsController = loader.getController();
            statisticsController.setIterations(iterations);
            statisticsController.setGameOfLife(gOL);
            statisticsController.makeChart();

            statisticStage.setTitle("GameOfLife");
            statisticStage.setScene(new Scene(root, 800, 600));

            statisticStage.showAndWait();
        }
    }
}