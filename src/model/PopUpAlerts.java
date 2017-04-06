package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Created by Oscar_000 on 19.03.2017.
 */
public class PopUpAlerts {
    public static void ioAlertFromDisk () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("File not found!");
        alert.setContentText("The file may be corrupt or not exist. Try another file.");
        alert.showAndWait();
    }

    public static void ioAlertFromURL () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid URL!");
        alert.setContentText("This URL does not contain an RLE file!");
        alert.showAndWait();
    }

    public static void patternFormatAlert () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Corrupt or erroneous file");
        alert.setContentText("It seems the file you are trying to load is wrongly formatted. Try loading a different" +
                " RLE-file!");
        alert.showAndWait();
    }

    public static void ruleAlert1 () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid rules");
        alert.setContentText("The rules you are trying to load are invalid. Remember that the rules must be formated" +
                " as 'Bxxx/Sxxx'. \n\n The standard rules (B3/S23) have been loaded.");
        alert.showAndWait();
    }

    public static void ruleAlert2 () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Rules");
        alert.setContentText("The rules are invalid. Remember to format as such: Bxxx/Sxxx and to keep your " +
                "digits between 0 and 8.");
        alert.showAndWait();
    }

    public static void outOfBounds () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Out of bounds!");
        alert.setContentText("The pattern you are trying to load is too big.");
        alert.showAndWait();
    }

    public static void ioeSaveError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Could not save file.");
        alert.setContentText("Your file was not saved. Please try again.");
        alert.showAndWait();
    }

    public static void sizeBoardError () {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning!");
        alert.setHeaderText("Board is too big");
        alert.setContentText("Your current board contains more cells than can be drawn! \nAs a result," +
                " your gif might not show anything. \n\nTry increasing the gif size, decreasing board size" +
                " or choosing the option to draw only the pattern to fix this!");
        alert.showAndWait();
    }

    public static void gifFramesAlert () {
        Alert counterAlert = new Alert(Alert.AlertType.WARNING);
        counterAlert.setTitle("Error");
        counterAlert.setHeaderText("Number of frames is to high");
        counterAlert.setContentText("The number of frames you have entered is too high. " +
                "Please keep it between 1 and 400");
        counterAlert.showAndWait();
    }

    public static void gifFPSAlert () {
        Alert fpsAlert = new Alert(Alert.AlertType.WARNING);
        fpsAlert.setTitle("Error");
        fpsAlert.setHeaderText("FPS too high");
        fpsAlert.setContentText("You're going too fast! Please keep your FPS input between 1 and 60");
        fpsAlert.showAndWait();
    }

    public static void ruleDescription (String ruleName, String ruleString, String ruleDescription) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rule description");
        alert.setHeaderText("Title: " + ruleName);
        alert.setContentText("RLE rule format: " + ruleString + "\n\n" + ruleDescription);
        alert.showAndWait();
    }

    public static void metaData (String header, String description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pattern Metadata");
        alert.setHeaderText(header);
        alert.setContentText(description);
        alert.showAndWait();
    }

    public static void resizeAlert() {
        Alert resizeAlert = new Alert(Alert.AlertType.WARNING);
        resizeAlert.setTitle("Error");
        resizeAlert.setHeaderText("Invalid size!");
        resizeAlert.setContentText("The size you have chosen is not valid. Please write a number between 6 and 1000!");
        resizeAlert.showAndWait();
    }

    public static void edgeAlert() {
        Alert edgeAlert = new Alert(Alert.AlertType.WARNING);
        edgeAlert.setTitle("Congratulations!");
        edgeAlert.setHeaderText("End of board!");
        edgeAlert.setContentText("Congratulations! You have reached the edge of the playing board! You won't be able " +
                "to expand further in this direction, but you're welcome to keep drawing within the borders. " +
                "\n\nAlternatively, you can click reset view to go back to the middle of the board, or click reset " +
                "board to start over!");
        edgeAlert.showAndWait();
    }

    public static boolean gifSimilarityAlert(int iterations) {
        Alert gifSimilarityConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        gifSimilarityConfirmation.setTitle("Message");
        gifSimilarityConfirmation.setHeaderText("High probability of pattern repeat!");
        gifSimilarityConfirmation.setContentText("It seems to be a high probability that the pattern on the board" +
                " will be repeated in "+iterations+" iteration(s)! If this is true, we can create an endless looping " +
                "gif with "+iterations+" images, provided our calculations are correct. Do you want to try with " +
                iterations+", and see if it works?");
        Optional<ButtonType> result = gifSimilarityConfirmation.showAndWait();

        boolean b = false;
        if (result.get() == ButtonType.OK) {
            b = true;
        }

        if (result.get() == ButtonType.CANCEL) {
            b = false;
        }

        return b;
    }

    public static boolean resizeClearAlert() {
        Alert resizeClearConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        resizeClearConfirmation.setTitle("Warning");
        resizeClearConfirmation.setHeaderText("Resizing will clear the board!");
        resizeClearConfirmation.setContentText("I you resize the board, the current elements will be cleared, " +
                "and you will start with a blank playing board. Are you sure you want to proceed?");
        Optional<ButtonType> result = resizeClearConfirmation.showAndWait();

        boolean b = false;
        if (result.get() == ButtonType.OK) {
            System.out.println("OK");
            b = true;
        }

        if (result.get() == ButtonType.CANCEL) {
            System.out.println("CANCEL");
            b = false;
        }

        System.out.println("B = "+b);
        return b;
    }
}
