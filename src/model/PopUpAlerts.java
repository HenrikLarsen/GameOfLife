package model;

import javafx.scene.control.Alert;

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
}
