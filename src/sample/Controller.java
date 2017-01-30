package sample;


import javafx.scene.control.*;
import javafx.fxml.FXML;
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

    private Color currentCellColor = Color.BLACK;
    private Color currentBackgroundColor = Color.WHITE;

    public void trueCellColor(){
        currentCellColor = cellColorPicker.getValue();
        System.out.println(currentCellColor);
    }

    public void trueBackgroundColor(){
        currentBackgroundColor = backgroundColorPicker.getValue();
        System.out.println(currentBackgroundColor);
    }

}
