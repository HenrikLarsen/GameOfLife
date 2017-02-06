package sample;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.util.Duration;


public class Controller implements Initializable {
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Button resetButton;
    @FXML private Slider speedSlider;
    @FXML private ColorPicker cellColorPicker;
    @FXML private ColorPicker backgroundColorPicker;
    @FXML private Button gridToggleButton;
    @FXML private TextField sizeInputField;
    @FXML private MenuBar menuBar;
    @FXML private Canvas canvasArea;
    @FXML private Label generationLabel;

    private Color currentCellColor = Color.BLACK;
    private Color currentBackgroundColor = Color.WHITE;
    private StaticBoard board = new StaticBoard();
    private GameOfLife gOL = new GameOfLife(board);
    private Timeline timeline;
    private KeyFrame keyframe;

    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        draw();
        Duration duration = Duration.millis(200);
        keyframe = new KeyFrame(duration, e -> {
            gOL.nextGeneration();
            draw();
            gOL.genCounter++;
            generationLabel.setText(Integer.toString(gOL.genCounter));
        });
        timeline = new Timeline(keyframe);
        timeline.setCycleCount(Animation.INDEFINITE);

    }


    public void draw(){
        GraphicsContext gc = canvasArea.getGraphicsContext2D();
        gc.clearRect(0,0, 800, 800);
        gc.setFill(currentCellColor);
        for (int x = 0; x < board.boardGrid.length; x++) {
            for (int y = 0; y < board.boardGrid[0].length; y++ ) {
                if (board.boardGrid[x][y] == 1) {
                    gc.fillRect(x * board.cellSize + 1, y * board.cellSize + 1, board.cellSize, board.cellSize);
                }
            }
        }
    }


    public void trueCellColor(){
        currentCellColor = cellColorPicker.getValue();
        System.out.println(currentCellColor);
        draw();
    }

    public void trueBackgroundColor(){
        currentBackgroundColor = backgroundColorPicker.getValue();
        System.out.println(currentBackgroundColor);
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
        gOL.killAll();
        draw();
    }


    public void cellSizeOnEnter(ActionEvent ae) {
        int size = Integer.parseInt(sizeInputField.getText());
        board.setCellSize(size);
        draw();
    }
}
