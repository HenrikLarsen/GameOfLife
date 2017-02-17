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
    private boolean isAlive = false;
    private byte[][] boardAtMousePressed;

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

    private KeyFrame addNewKeyFrame(){
        return new KeyFrame(Duration.millis(1000), e -> {
            gOL.nextGeneration();
            draw();
            gOL.genCounter++;
            generationLabel.setText(Integer.toString(gOL.genCounter));
            aliveLabel.setText(Integer.toString(board.cellsAlive));
            System.out.println(board.toString());
        });
    }

    private void draw(){
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

    private void setFPS() {
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
        double size = Double.parseDouble(sizeInputField.getText());
        board.setCellSize(size);
        draw();
    }

    //this happens when you click, or at the beginning of a drag
    //whenever this happens it should reverse the block
    public void mousePressed(MouseEvent e) {

        int x = (int)(e.getX()/board.cellSize);
        int y = (int)(e.getY()/board.cellSize);

        if (board.boardGrid[x][y] == 1) {
            isAlive = true;
        }

        //Makes a copy of the board at the time of mouseclick
        boardAtMousePressed = new byte[board.boardGrid.length][board.boardGrid[0].length];
        for (int i = 0; i < boardAtMousePressed.length; i++) {
            for (int j = 0; j < boardAtMousePressed[i].length; j++) {
                boardAtMousePressed[i][j] = board.boardGrid[i][j];
            }
        }

        if ((x < board.boardGrid.length) && (y < board.boardGrid[0].length) && x >= 0 && y >= 0) {
            if (board.boardGrid[x][y] == 0) {
                board.boardGrid[x][y] = 1;
            } else {
                board.boardGrid[x][y] = 0;
            }
        }
        draw();
    }

    public void mouseDragged(MouseEvent e) {
        int x = (int)(e.getX()/board.cellSize);
        int y = (int)(e.getY()/board.cellSize);

        if ((x < board.boardGrid.length) && (y < board.boardGrid[0].length) && x >= 0 && y >= 0) {
            if (board.boardGrid[x][y] == boardAtMousePressed[x][y]) {
                if (isAlive) {
                    board.boardGrid[x][y] = 0;
                } else {
                    board.boardGrid[x][y] = 1;
                }
            }
        }
        draw();
    }

    public void mouseDragOver() {
        isAlive = false;
    }
}