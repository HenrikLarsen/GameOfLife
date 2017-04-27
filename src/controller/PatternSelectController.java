package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.FileHandler;

import java.io.File;
import java.io.IOException;

/**
 * The PatternSelectController allows the user to choose from a variety of predetermined patterns to load into
 * the board. It is mostly controlled by a choice box, which updates all relevant fields on the window with
 * information about the pattern in question.
 * @author Oscar Vladau-Husevold
 * @version 1.0
 */
public class PatternSelectController implements Initializable {
    @FXML ImageView imageView;
    @FXML ChoiceBox patternSelect;
    @FXML Label titleLabel;
    @FXML Label sizeLabel;
    @FXML Label rulesLabel;
    @FXML Label cellsLabel;
    @FXML Label authorLabel;
    @FXML Label discoveredLabel;
    @FXML TextArea descriptionArea;

    private FileHandler fileHandler;
    private File currentFile;

    //The items to be set in the choice box
    private final ObservableList<String> choosePatternList = FXCollections.observableArrayList("Glider", "Acorn",
            "Switch Engine", "Achim's p16", "Gosper Glider Gun", "112P51", "Penny lane", "56P6H1V0",
            "Flower of Eden", "Period-45 glider gun", "Sidecar Gun", "Moving Sawtooth", "Primer",
            "Star Gate", "p690 60P5H2V0 gun", "Turing Machine");

    /**
     * A concrete implementation of the method in interface Initializable.
     * Initializes the pattern selector window, setting the items of the choice box and selecting an item.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     * @see #choosePatternList
     * @see #patternSelect
     */
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        patternSelect.setItems(choosePatternList);
        patternSelect.getSelectionModel().selectFirst();
    }

    /**
     * Method that sets the controllers active FileHandler object
     * @param fh - The FileHandler object to be set.
     * @see #fileHandler
     */
    public void setFileHandler(FileHandler fh) {
        fileHandler = fh;
    }

    /**
     * Method that calls FileHandler's readGameBoardFromDisk() method using the currentFile as the file.
     * Will load the pattern and close the window if successful, otherwise it will produce a warning to the user.
     * @see #currentFile
     * @see FileHandler#readGameBoardFromDisk(File)
     * @see PopUpAlerts#ioAlertFromDisk()
     */
    public void loadPatternClick() {
        try {
            fileHandler.readGameBoardFromDisk(currentFile);
            Stage currentStage = (Stage) imageView.getScene().getWindow();
            currentStage.close();
        } catch (IOException ie) {
            PopUpAlerts.ioAlertFromDisk();
        }
    }

    /**
     * Method called when the user clicks the Cancel button. Will close the window.
     */
    public void cancelClick() {
        Stage currentStage = (Stage) imageView.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Method called when the user chooses an item from the patternSelect choice box. Will call the
     * newPatternInfo() method in order to update all info fields in the window.
     * @see #patternSelect
     * @see #newPatternInfo(String)
     */
    public void patternSelectClick() {
        String pattern = (String)patternSelect.getValue();
        newPatternInfo(pattern);
    }

    /**
     * Method that updates all information about a pattern and sets the current file to be the relevant file on
     * the disk. Consists of a switch-statement going through all possible options.
     * @param chosenPattern - The string representing the pattern chosen from the ChoiceBox
     */
    private void newPatternInfo(String chosenPattern) {
        switch (chosenPattern) {

            case "Glider":
                titleLabel.setText("Glider");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("3x3");
                cellsLabel.setText("5");
                authorLabel.setText("Richard K. Guy");
                discoveredLabel.setText("1970");
                descriptionArea.setText("The glider is the smallest, most common, and first-discovered spaceship." +
                        " It travels diagonally across the Life grid. The glider is often produced by " +
                        "randomly-generated starting patterns.");
                Image gliderImage = new Image(new File("src/resources/images/glider.png").toURI().toString());
                imageView.setImage(gliderImage);
                currentFile = new File("src/resources/rlefiles/glider.rle");
                break;

            case "Gosper Glider Gun":
                titleLabel.setText("Gosper Glider Gun");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("36x9");
                cellsLabel.setText("36");
                authorLabel.setText("Bill Gosper");
                discoveredLabel.setText("1970");
                descriptionArea.setText("The Gosper glider gun is the first known gun, and indeed the first " +
                        "known finite pattern with unbounded growth, found by Bill Gosper in November 1970. " +
                        "It consists of two queen bee shuttles stabilized by two blocks.");
                Image gggImage = new Image(new File("src/resources/images/gosper.png").toURI().toString());
                imageView.setImage(gggImage);
                currentFile = new File("src/resources/rlefiles/gosperglidergun.rle");
                break;

            case "Achim's p16":
                titleLabel.setText("Achim's p16");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("13x13");
                cellsLabel.setText("32");
                authorLabel.setText("Achim Flammenkamp");
                discoveredLabel.setText("1994");
                descriptionArea.setText("Achim's p16 is a period 16 oscillator that was found by Achim Flammenkamp " +
                        "on July 27, 1994. With only 32 cells, it is the smallest known period 16 oscillator.");
                Image p16Image = new Image(new File("src/resources/images/p16.png").toURI().toString());
                imageView.setImage(p16Image);
                currentFile = new File("src/resources/rlefiles/achimsp16.rle");
                break;

            case "Acorn":
                titleLabel.setText("Acorn");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("7x3");
                cellsLabel.setText("7");
                authorLabel.setText("Charles Corderman");
                discoveredLabel.setText("Before 1971");
                descriptionArea.setText("Acorn is a methuselah found by Charles Corderman. It was discovered no " +
                        "later than 1971, though its exact year of discovery is unknown. Its maximum population, " +
                        "1057, occurs in generation 4408.");
                Image acornImage = new Image(new File("src/resources/images/acorn.png").toURI().toString());
                imageView.setImage(acornImage);
                currentFile = new File("src/resources/rlefiles/acorn.rle");
                break;

            case "Penny lane":
                titleLabel.setText("Penny lane");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("15x10");
                cellsLabel.setText("34");
                authorLabel.setText("David Buckingham");
                discoveredLabel.setText("1972");
                descriptionArea.setText("Penny lane is a period 4 oscillator that was found by David " +
                        "Buckingham in 1972. It uses two blocks and a tub as induction coils.");
                Image pennyImage = new Image(new File("src/resources/images/penny.png").toURI().toString());
                imageView.setImage(pennyImage);
                currentFile = new File("src/resources/rlefiles/pennylane.rle");
                break;


            case "Switch Engine":
                titleLabel.setText("Switch Engine");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("6x4");
                cellsLabel.setText("8");
                authorLabel.setText("Charles Corderman");
                discoveredLabel.setText("1971");
                descriptionArea.setText("A switch engine is a methuselah that was found by Charles Corderman " +
                        "in 1971. It produces a copy of itself after 48 generation, glide-reflected 4 cells " +
                        "northwest, along with some active junk.");
                Image switchImage = new Image(new File("src/resources/images/switch.png").toURI().toString());
                imageView.setImage(switchImage);
                currentFile = new File("src/resources/rlefiles/switchengine.rle");
                break;

            case "112P51":
                titleLabel.setText("112P51");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("37x37");
                cellsLabel.setText("112");
                authorLabel.setText("Nicolay Beluchenko");
                discoveredLabel.setText("2009");
                descriptionArea.setText("112P51 is an unnamed period-51 oscillator. It was the first non-trivial " +
                        "period-51 oscillator to be found, all previously known period-51 oscillators being made " +
                        "up of disjoint period-3 and period-17 oscillators.");
                Image p51Image = new Image(new File("src/resources/images/112p51.png").toURI().toString());
                imageView.setImage(p51Image);
                currentFile = new File("src/resources/rlefiles/112p51.rle");
                break;

            case "Flower of Eden":
                titleLabel.setText("Flower of Eden");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("11x11");
                cellsLabel.setText("69");
                authorLabel.setText("Nicolay Beluchenko");
                discoveredLabel.setText("2009");
                descriptionArea.setText("Flower of Eden, otherwise known as the Garden of Eden 5, was the " +
                        "smallest known Garden of Eden until December 2011. A Garden of Eden is " +
                        "a pattern that has no parents and thus can only occur in generation 0.");
                Image flowerImage = new Image(new File("src/resources/images/flower.png").toURI().toString());
                imageView.setImage(flowerImage);
                currentFile = new File("src/resources/rlefiles/flowerofeden.rle");
                break;

            case "Period-45 glider gun":
                titleLabel.setText("Period-45 glider gun");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("48x40");
                cellsLabel.setText("132");
                authorLabel.setText("Matthias Merzenich");
                discoveredLabel.setText("2010");
                descriptionArea.setText("Period-45 glider gun is a true period 45 gun discovered by Matthias " +
                        "Merzenich in 2010, with improvements by Adam P. Goucher and Dave Greene. " +
                        "It consists of an object hassled by pentadecathlons.");
                Image gliderGun45Image = new Image(new File("src/resources/images/period45gun." +
                        "png").toURI().toString());
                imageView.setImage(gliderGun45Image);
                currentFile = new File("src/resources/rlefiles/period45glidergun.rle");
                break;

            case "56P6H1V0":
                titleLabel.setText("56P6H1V0");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("26x12");
                cellsLabel.setText("56");
                authorLabel.setText("Hartmut Holzwart");
                discoveredLabel.setText("2009");
                descriptionArea.setText("56P6H1V0 is a spaceship discovered by Hartmut Holzwart in April 2009 " +
                        "that travels at a speed of c/6 orthogonally. With 56 cells it is currently the smallest " +
                        "known orthogonal c/6 spaceship, surpassing dragon with 102 cells.");
                Image p6h1Image = new Image(new File("src/resources/images/56P6H1V0.png").toURI().toString());
                imageView.setImage(p6h1Image);
                currentFile = new File("src/resources/rlefiles/56p6h1v0.rle");
                break;

            case "Primer":
                titleLabel.setText("Primer");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("440x294");
                cellsLabel.setText("2953");
                authorLabel.setText("Dean Hickerson");
                discoveredLabel.setText("1991");
                descriptionArea.setText("Primer is a pattern that was constructed by Dean Hickerson on November " +
                        "1, 1991 that produces a stream of lightweight spaceships representing prime numbers. " +
                        "It was the first pattern created that computes prime numbers.");
                Image primerImage = new Image(new File("src/resources/images/primer.png").toURI().toString());
                imageView.setImage(primerImage);
                currentFile = new File("src/resources/rlefiles/primer.rle");
                break;

            case "Sidecar Gun":
                titleLabel.setText("Sidecar Gun");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("213x142");
                cellsLabel.setText("1307");
                authorLabel.setText("Jason Summers");
                discoveredLabel.setText("2000");
                descriptionArea.setText("Sidecar gun is a period 60 gun that was constructed by Jason Summers " +
                        "on March 7, 2000 to fire sidecars. It works by using several variants of the Gosper " +
                        "glider gun to construct the necessary heavyweight spaceship and sidecar via glider synthesis.");
                Image sidecarImage = new Image(new File("src/resources/images/" +
                        "sidecargun.png").toURI().toString());
                imageView.setImage(sidecarImage);
                currentFile = new File("src/resources/rlefiles/sidecargun.rle");
                break;

            case "Star Gate":
                titleLabel.setText("Star Gate");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("155x126");
                cellsLabel.setText("1099");
                authorLabel.setText("Dietrich Leithner");
                discoveredLabel.setText("1996");
                descriptionArea.setText("Star gate is a pattern based on the Fast Forward Force Field that was " +
                        "created on October 26, 1996 by Dietrich Leithner. It is a period 60 oscillator that " +
                        "allows lightweight spaceships to jump forward at the superluminous speed of 15c/14.");
                Image sgImage = new Image(new File("src/resources/images/stargate.png").toURI().toString());
                imageView.setImage(sgImage);
                currentFile = new File("src/resources/rlefiles/stargate.rle");
                break;

            case "Moving Sawtooth":
                titleLabel.setText("Moving Sawtooth");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("128x173");
                cellsLabel.setText("1239");
                authorLabel.setText("David Bell");
                discoveredLabel.setText("2005");
                descriptionArea.setText("Moving sawtooth is an orthogonal sawtooth with expansion factor 3. " +
                        "Its minimum infinite repeating population is 1239, and it is notable because it " +
                        "and its slight modifications are the only known sawtooths that move.");
                Image msImage = new Image(new File("src/resources/images/sawtooth.png").toURI().toString());
                imageView.setImage(msImage);
                currentFile = new File("src/resources/rlefiles/movingsawtooth.rle");
                break;

            case "p690 60P5H2V0 gun":
                titleLabel.setText("p690 60P5H2V0 gun");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("871x854");
                cellsLabel.setText("12511");
                authorLabel.setText("Jason Summers");
                discoveredLabel.setText("2003");
                descriptionArea.setText("p690 60P5H2V0 gun is a period 690 gun that was constructed to fire copies " +
                        "of the 60P5H2V0 spaceship. It works by using several variants twin bees shuttle collisions " +
                        "to construct the necessary glider synthesis");
                Image p690Image = new Image(new File("src/resources/images/p690.png").toURI().toString());
                imageView.setImage(p690Image);
                currentFile = new File("src/resources/rlefiles/p69060p5h2v0gun.rle");
                break;

            case "Turing Machine":
                titleLabel.setText("Turing Machine");
                rulesLabel.setText("B3/S23");
                sizeLabel.setText("1714x1647");
                cellsLabel.setText("36549");
                authorLabel.setText("Paul Rendell");
                discoveredLabel.setText("2000");
                descriptionArea.setText("The Turing machine is, as its name suggests, a pattern that is capable of " +
                        "turing-complete computation. It was created by Paul Rendell and its " +
                        "construction was completed on April 2, 2000. \nWARNING: The application might run slowly.");
                Image turingImage = new Image(new File("src/resources/images/turing.png").toURI().toString());
                imageView.setImage(turingImage);
                currentFile = new File("src/resources/rlefiles/turingmachine.rle");
                break;
        }
    }
}
