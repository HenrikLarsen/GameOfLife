package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.util.Duration;

public class Controller implements Initializable {
    @FXML private Slider speedSlider;
    @FXML private ColorPicker cellColorPicker;
    @FXML private ColorPicker backgroundColorPicker;
    @FXML private TextField sizeInputField;
    @FXML private Canvas canvasArea;
    @FXML private Label generationLabel;
    @FXML private Label fpsLabel;
    @FXML private Label aliveLabel;

    private Color currentCellColor = Color.LIMEGREEN;
    private Color currentBackgroundColor = Color.LIGHTGRAY;
    private StaticBoard board = new StaticBoard();
    private GameOfLife gOL = new GameOfLife(board);
    private Timeline timeline;
    private boolean gridToggle = true;
    private boolean[][] change = new boolean[board.boardGrid.length][board.boardGrid[0].length];
    private boolean isAlive = true;

    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        cellColorPicker.setValue(currentCellColor);
        backgroundColorPicker.setValue(currentBackgroundColor);
        draw();
        KeyFrame keyframe = addNewKeyFrame();
        timeline = new Timeline(keyframe);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setRate(speedSlider.getValue());
        fpsLabel.setText(Integer.toString((int)speedSlider.getValue()) + " FPS");
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {setFPS();});

    }

    public KeyFrame addNewKeyFrame(){
        return new KeyFrame(Duration.millis(1000), e -> {
            gOL.nextGeneration();
            draw();
            gOL.genCounter++;
            generationLabel.setText(Integer.toString(gOL.genCounter));
            aliveLabel.setText(Integer.toString(board.cellsAlive));
        });
    }

    public void draw(){
        GraphicsContext gc = canvasArea.getGraphicsContext2D();
        gc.setFill(currentBackgroundColor);
        gc.fillRect(0,0,canvasArea.getWidth(), canvasArea.getHeight());
        gc.setFill(currentCellColor);
        gc.setStroke(Color.BLACK);
        for (int x = 0; x < board.boardGrid.length; x++) {
            for (int y = 0; y < board.boardGrid[0].length; y++ ) {
                if (board.boardGrid[x][y] == 1) {
                    gc.fillRect(x * board.cellSize + 1, y * board.cellSize + 1, board.cellSize, board.cellSize);
                    if(gridToggle){
                        gc.strokeRect(x * board.cellSize + 1, y * board.cellSize + 1, board.cellSize, board.cellSize);
                    }
                }else if(gridToggle){
                    gc.strokeRect(x * board.cellSize + 1, y * board.cellSize + 1, board.cellSize, board.cellSize);
                }
            }
        }
    }

    public void setFPS() {
        fpsLabel.setText(Integer.toString((int)speedSlider.getValue()) + " FPS");
        timeline.setRate(speedSlider.getValue());
    }

    public void chooseCellColor(){
        currentCellColor = cellColorPicker.getValue();
        draw();
    }

    public void chooseBackgroundColor(){
        currentBackgroundColor = backgroundColorPicker.getValue();
        draw();
    }

    public void startClick(ActionEvent actionEvent) {
        timeline.play();
    }

    public void pauseClick(ActionEvent actionEvent) {
        timeline.pause();
    }

    public void resetClick(ActionEvent actionEvent) {
        timeline.stop();
        gOL.genCounter = 0;
        generationLabel.setText(Integer.toString(gOL.genCounter));
        board.resetBoard();
        aliveLabel.setText(Integer.toString(board.cellsAlive));
        draw();
    }

    public void closeClick(ActionEvent ae) {
        Platform.exit();
    }

    public void gridClick(ActionEvent ae) {
        gridToggle = !gridToggle;
        draw();
    }

    public void cellSizeOnEnter(ActionEvent ae) {
        int size = Integer.parseInt(sizeInputField.getText());
        board.setCellSize(size);
        draw();
    }

    public void mouseDrag(MouseEvent event) {
        int x = (int)(event.getX()/board.cellSize);
        int y = (int)(event.getY()/board.cellSize);

        if (board.boardGrid[x][y] == 0) {
            isAlive = false;
        }
        //  DENNE GJØR AT DE BYTTER PÅ
        // else {isAlive = true;}


        if (isAlive) {
            if (!change[x][y]) {
                if (board.boardGrid[x][y] == 1) {
                    board.boardGrid[x][y] = 0;
                    board.cellsAlive--;
                    change[x][y] = true;
                }
            }
        }

        else if (!isAlive) {
            if (!change[x][y]) {
                if (board.boardGrid[x][y] == 0) {
                    board.boardGrid[x][y] = 1;
                    board.cellsAlive++;
                    change[x][y] = true;
                }
            }
        }
        aliveLabel.setText(Integer.toString(board.cellsAlive));
        draw();
    }

    public void mouseDragOver() {
        for (int x = 0; x < change.length; x++) {
            for (int y = 0; y < change[0].length; y++) {
                change[x][y] = false;
            }
        }
    }
}