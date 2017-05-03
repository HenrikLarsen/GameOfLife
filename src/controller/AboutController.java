package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * The AboutController class is responsible for the about / help window, and the user interaction
 * within that window.
 * @author Oscar Vladau-Husevold
 * @version 1.0
 */
public class AboutController {
    @FXML private Button aboutCloseButton;

    /**
     * Method to exit the about window. Is called when the user clicks on the close-button.
     */
    public void aboutCloseClick () {
        Stage currentStage = (Stage) aboutCloseButton.getScene().getWindow();
        currentStage.close();
    }
}