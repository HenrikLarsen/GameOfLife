package sample;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;


public class Controller {
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

    private Color currentCellColor = Color.BLACK;
    private Color currentBackgroundColor = Color.WHITE;
    private StaticBoard board = new StaticBoard();


    public void drawStartGrid(){
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
       // gc.fillRect(0, 0, board.cellSize, board.cellSize);
    }


    public void trueCellColor(){
        currentCellColor = cellColorPicker.getValue();
        System.out.println(currentCellColor);
        drawStartGrid();
    }

    public void trueBackgroundColor(){
        currentBackgroundColor = backgroundColorPicker.getValue();
        System.out.println(currentBackgroundColor);
    }

    public void startDraw(ActionEvent actionEvent) {
        drawStartGrid();
    }

    public void cellSizeOnEnter(ActionEvent ae) {
        int size = Integer.parseInt(sizeInputField.getText());
        board.setCellSize(size);
        drawStartGrid();
    }
}
