package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * The PopUpAlerts class is a class with static methods related to showing a popup window to the user. All methods
 * are static so that they can be accessed anywhere without the need to create an instance of the PopUpAlert class.
 * @author Oscar Vladau-Husevold
 * @version 1.0
 */
public class PopUpAlerts {

    /**
     * Creates a warning when failing to load a file from disk.
     */
    public static void ioAlertFromDisk () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("File not found!");
        alert.setContentText("The file may be corrupt or not exist. Try another file.");
        alert.showAndWait();
    }

    /**
     * Creates a warning when failing to load a file from an URL.
     */
    public static void ioAlertFromURL () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid URL!");
        alert.setContentText("This URL does not contain an RLE file!");
        alert.showAndWait();
    }

    /**
     * Creates a warning if an FXML file could not be loaded.
     */
    public static void ioAlertFXML () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Can't find FXML!");
        alert.setContentText("The corresponding FXML document for this window could not be found.");
        alert.showAndWait();
    }

    /**
     * Creates a warning when a loaded file is wrongly formatted.
     */
    public static void patternFormatAlert () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Corrupt or erroneous file");
        alert.setContentText("It seems the file you are trying to load is wrongly formatted. Try loading a different" +
                " RLE-file!");
        alert.showAndWait();
    }

    /**
     * Creates a warning when a loaded file's rules are wrongly formatted, informing the user that the standard
     * rules will be used.
     */
    public static void ruleAlert1 () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid rules");
        alert.setContentText("The rules you are trying to load are invalid. Remember that the rules must be formatted" +
                " as 'Bxxx/Sxxx'. \n\n The standard rules (B3/S23) have been loaded.");
        alert.showAndWait();
    }

    /**
     * Creates a warning when the user attempts to set wrongly formatted rules. Gives formatting tips.
     */
    public static void ruleAlert2 () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Rules");
        alert.setContentText("The rules are invalid. Remember to format as such: Bxxx/Sxxx and to keep your " +
                "digits between 0 and 8.");
        alert.showAndWait();
    }

    /**
     * Creates a warning when trying to load a pattern that exceeds the maximum bounds of the cell grid.
     */
    public static void outOfBounds () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Out of bounds!");
        alert.setContentText("The pattern you are trying to load is too big.");
        alert.showAndWait();
    }

    /**
     * Creates a warning when a file cannot be saved to disk.
     */
    public static void ioeSaveError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Could not save file.");
        alert.setContentText("Your file was not saved. Please try again.");
        alert.showAndWait();
    }

    /**
     * Creates a warning when trying to export a gif where the board is too big relative to the size of the gif-picture
     * so that the cell size is less than a pixel, and therefore will not be drawn.
     */
    public static void sizeBoardError () {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning!");
        alert.setHeaderText("Board is too big");
        alert.setContentText("Your current board contains more cells than can be drawn! \nAs a result," +
                " your gif might not show anything. \n\nTry increasing the gif size, decreasing board size" +
                " or choosing the option to draw only the pattern to fix this!");
        alert.showAndWait();
    }

    /**
     * Creates a warning when trying to set the number of iterations of a gif to either 0 or above 400, telling the user
     * to select a valid value.
     */
    public static void gifFramesAlert () {
        Alert counterAlert = new Alert(Alert.AlertType.WARNING);
        counterAlert.setTitle("Error");
        counterAlert.setHeaderText("Too many iterations!");
        counterAlert.setContentText("The number of iterations you have entered is too high. " +
                "Please keep it between 1 and 400");
        counterAlert.showAndWait();
    }

    /**
     * Creates a warning when trying to set the fps of a gif above 50, telling the user to select a valid value.
     */
    public static void gifFPSAlert () {
        Alert fpsAlert = new Alert(Alert.AlertType.WARNING);
        fpsAlert.setTitle("Error");
        fpsAlert.setHeaderText("FPS too high");
        fpsAlert.setContentText("You're going too fast! Please keep your FPS input between 1 and 50");
        fpsAlert.showAndWait();
    }

    /**
     * Creates a warning when trying to set the size of the board to an invalid number, advising the user to
     * set a size between 6 and 1000.
     */
    public static void resizeAlert() {
        Alert resizeAlert = new Alert(Alert.AlertType.WARNING);
        resizeAlert.setTitle("Error");
        resizeAlert.setHeaderText("Invalid size!");
        resizeAlert.setContentText("The size you have chosen is not valid. Please write a number between 6 and 1000!");
        resizeAlert.showAndWait();
    }

    public static void editorSizeAlert() {
        Alert editorAlert = new Alert(Alert.AlertType.WARNING);
        editorAlert.setTitle("Warning");
        editorAlert.setHeaderText("Board size very large!");
        editorAlert.setContentText("The size of your board is very large. \n\nIn fact, it is so large that " +
                "it might be difficult to draw the board on the limited size of the editor window. You will " +
                "still be able to export the board as a GIF or RLE-file, but it might not be drawn in the editor.\n\n" +
                "To avoid this issue in the future, try resetting the board, and export a smaller pattern.");
        editorAlert.showAndWait();
    }

    /**
     * Creates a warning when trying to expand the board by drawing and exceeding the maximum height or width.
     * Advises the user to either continue playing within the borders, or resetting the board.
     */
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

    /**
     * Creates a popup window containing the description of the current playing rules.
     * @param ruleName - The name of the rules.
     * @param ruleString - The RLE-formatting of the current rules.
     * @param ruleDescription - The description of the current rules.
     */
    public static void ruleDescription (String ruleName, String ruleString, String ruleDescription) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rule description");
        alert.setHeaderText("Title: " + ruleName);
        alert.setContentText("RLE rule format: " + ruleString + "\n\n" + ruleDescription);
        alert.showAndWait();
    }

    /**
     * Creates a popup window containing the meta data of the currently loaded pattern.
     * @param header - The name of the pattern.
     * @param description - The metadata of the pattern.
     */
    public static void metaData (String header, String description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pattern Metadata");
        alert.setHeaderText(header);
        alert.setContentText(description);
        alert.showAndWait();
    }

    /**
     * Creates a popup when trying to save a gif, and the statistics have identified a high probability of a repeating
     * pattern. It informs the user that the pattern is likely to repeat after n-number of iterations, and asks
     * if it should set the number of images to that number. Returns true if yes or false if no.
     * @param iterations - The number of iterations identified as a possible pattern repeat.
     * @return b - The users answer.
     */
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
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                b = true;
            }

            if (result.get() == ButtonType.CANCEL) {
                b = false;
            }
        }
        return b;
    }

    /**
     * Creates a warning when trying to resize the board in the editor, warning the user that the currently active
     * cells will become inactive if the board is resized. User can then choose whether or not to continue with the
     * resizing.
     * @return b - The users answer.
     */
    public static boolean resizeClearAlert() {
        Alert resizeClearConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        resizeClearConfirmation.setTitle("Warning");
        resizeClearConfirmation.setHeaderText("Resizing will clear the board!");
        resizeClearConfirmation.setContentText("I you resize the board, the current elements will be cleared, " +
                "and you will start with a blank playing board. Are you sure you want to proceed?");
        Optional<ButtonType> result = resizeClearConfirmation.showAndWait();

        boolean b = false;
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                System.out.println("OK");
                b = true;
            }

            if (result.get() == ButtonType.CANCEL) {
                System.out.println("CANCEL");
                b = false;
            }
        }
        return b;
    }
}