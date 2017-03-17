package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import lieng.GIFWriter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Oscar_000 on 09.03.2017.
 */


public class PatternExportController implements Initializable {
    @FXML
    Canvas editorCanvas;
    @FXML
    TextField titleField;
    @FXML
    TextField authorField;
    @FXML
    TextArea commentField;
    @FXML
    TextField ruleInputField;
    @FXML
    TextField fpsInputField;
    @FXML
    TextField numFramesInputField;
    @FXML
    CheckBox dateCheckBox;
    @FXML
    ColorPicker cellColorPicker;
    @FXML
    Canvas strip;
    @FXML
    ChoiceBox chooseSizeBox;
    @FXML
    ChoiceBox chooseDrawBox;


    private StaticBoard exportBoard;
    private StaticBoard stripBoard;
    private StaticBoard gifBoard;
    private GameOfLife gameOfLife;
    private GameOfLife stripGol;
    private GameOfLife gifGol;
    public double cellSize;
    private Color currentCellColor = Color.LIMEGREEN;
    private Color currentBackgroundColor = Color.LIGHTGRAY;
    private boolean erase = false;
    private byte[][] boardAtMousePressed;
    private boolean grid = false;
    private GIFWriter gifWriter;
    private double gifCellSize;
    private ObservableList<String> chooseSizeList = FXCollections.observableArrayList("640x640", "800x800", "1024x1024", "1200x1200", "1600x1600", "1920x1920");
    private ObservableList<String> chooseDrawList = FXCollections.observableArrayList("Entire Board", "Pattern Only");
    private int gifSize;
    private boolean drawEntireBoard = true;



    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        cellColorPicker.setValue(Color.LIMEGREEN);
        TextFormatter<String> ruleFormatter = new TextFormatter<String>(change -> {
            change.setText(change.getText().replaceAll("[^sSbB012345678/]", ""));
            return change;
        });
        ruleInputField.setTextFormatter(ruleFormatter);
        TextFormatter<String> fpsFormatter = new TextFormatter<String>( change -> {
            change.setText(change.getText().replaceAll("[^\\d]", ""));
            return change;
        });
        TextFormatter<String> numFramesFormater = new TextFormatter<String>( change -> {
            change.setText(change.getText().replaceAll("[^\\d]", ""));
            return change;
        });

        fpsInputField.setTextFormatter(fpsFormatter);
        numFramesInputField.setTextFormatter(numFramesFormater);

        chooseSizeBox.setItems(chooseSizeList);
        chooseSizeBox.getSelectionModel().select(2);
        chooseDrawBox.setItems(chooseDrawList);
        chooseDrawBox.getSelectionModel().selectFirst();
    }

    public void drawEditorBoard() {
        GraphicsContext graphicsContext = editorCanvas.getGraphicsContext2D();
        graphicsContext.setFill(currentBackgroundColor);
        graphicsContext.fillRect(0, 0, editorCanvas.getWidth(), editorCanvas.getHeight());
        graphicsContext.setFill(currentCellColor);
        cellSize = editorCanvas.getWidth() / exportBoard.boardGrid.length;

        for (int x = 0; x < exportBoard.boardGrid.length; x++) {
            for (int y = 0; y < exportBoard.boardGrid[0].length; y++) {
                if (exportBoard.boardGrid[x][y] == 1) {
                    graphicsContext.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }

        if (grid) {
            for (int x = 0; x < exportBoard.boardGrid.length; x++) {
                for (int y = 0; y < exportBoard.boardGrid[0].length; y++) {
                    graphicsContext.strokeRect(x * cellSize + 1, y * cellSize + 1, cellSize, cellSize);
                }
            }
        }
    }

    public void closeClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) editorCanvas.getScene().getWindow();
        currentStage.close();
    }

    public void chooseCellColor(ActionEvent actionEvent) {
        currentCellColor = cellColorPicker.getValue();
        drawEditorBoard();
    }

    public void setExportBoard(StaticBoard board) {
        this.exportBoard = board;
    }

    public void setGameOfLife(GameOfLife gOL) {
        this.gameOfLife = gOL;
    }

    public void mousePressed(MouseEvent mouseEvent) {
        //Checks the x and y coordinates of the mouse-pointer and compares it to the current cell size to find the cell.
        int x = (int) (mouseEvent.getX() / cellSize);
        int y = (int) (mouseEvent.getY() / cellSize);

        //Makes a copy of the board when the mouse button is pressed, stores as a global variable.
        boardAtMousePressed = new byte[exportBoard.boardGrid.length][exportBoard.boardGrid[0].length];
        for (int i = 0; i < boardAtMousePressed.length; i++) {
            for (int j = 0; j < boardAtMousePressed[i].length; j++) {
                boardAtMousePressed[i][j] = exportBoard.boardGrid[i][j];
            }
        }

        //Checks that the user is within the playing board during click, and changes the cells if yes.
        if ((x < exportBoard.boardGrid.length) && (y < exportBoard.boardGrid[0].length) && x >= 0 && y >= 0) {

            //Sets global variable erase to true if the first clicked cell was alive.
            //If true, drag and click will only erase until the mouse is released. If false, method will only draw.
            if (exportBoard.boardGrid[x][y] == 1) {
                erase = true;
            }

            if (exportBoard.boardGrid[x][y] == 0) {
                if (exportBoard.boardGrid[x][y] != 1) {
                    exportBoard.cellsAlive++;
                }
                exportBoard.boardGrid[x][y] = 1;
            } else {
                if (exportBoard.cellsAlive > 0 && exportBoard.boardGrid[x][y] == 1) {
                    exportBoard.cellsAlive--;
                }
                exportBoard.boardGrid[x][y] = 0;
            }
        }
        drawEditorBoard();
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        //Checks the x and y coordinates of the mouse-pointer and compares it to the current cell size to find the cell.
        int x = (int) (mouseEvent.getX() / cellSize);
        int y = (int) (mouseEvent.getY() / cellSize);

        //Checks that the user is drawing within the borders of the board.
        if ((x < exportBoard.boardGrid.length) && (y < exportBoard.boardGrid[0].length) && x >= 0 && y >= 0) {

            //Checks whether the current cell has been changed since the mouse button was pressed.
            if (exportBoard.boardGrid[x][y] == boardAtMousePressed[x][y]) {

                //If boolean erase is true, it sets the cell to 0. Else, sets the cell to 1.
                if (erase) {
                    if (exportBoard.cellsAlive > 0 && exportBoard.boardGrid[x][y] == 1) {
                        exportBoard.cellsAlive--;
                    }
                    exportBoard.boardGrid[x][y] = 0;
                } else {
                    if (exportBoard.boardGrid[x][y] != 1) {
                        exportBoard.cellsAlive++;
                    }
                    exportBoard.boardGrid[x][y] = 1;
                }
            }
        }
        drawEditorBoard();
    }

    public void mouseDragOver() {
        erase = false;
        drawStrip();
    }

    public void clearBoardClick() {
        exportBoard.resetBoard();
        drawEditorBoard();
    }

    public void toggleGridClick() {
        if (grid) {
            grid = false;
        } else {
            grid = true;
        }
        drawEditorBoard();
    }


    public void saveRLEClick() {
        int[] boundingbox = exportBoard.getBoundingBox();
        int x = Math.abs(boundingbox[1] - boundingbox[0] + 1);
        int y = Math.abs(boundingbox[3] - boundingbox[2] + 1);
        String rules = gameOfLife.getRuleString();

        byte[][] trimmedPattern = trim(exportBoard);

        String rawString = patternToString(trimmedPattern);
        System.out.println(rawString);
        String encodedString = encodeStringToRLE(rawString);

        //Adds a new line for every 70 characters
        String splitString = encodedString.replaceAll("(.{70})", "$1\n");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Run-length encoding", "*.rle"));
        File file = fileChooser.showSaveDialog(new Stage());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();

        if (file == null) {
            return;
        }

        try {
            PrintWriter printWriter = new PrintWriter(file);
            if (!titleField.getText().equals(""))
                printWriter.println("#N " + titleField.getText() + ".");
            if (!authorField.getText().equals("")) {
                if (dateCheckBox.isSelected()) {
                    printWriter.println("#O " + authorField.getText() + ". Created " + dateFormat.format(date) + ".");
                } else {
                    printWriter.println("#O " + authorField.getText() + ".");
                }
            } else if (authorField.getText().equals("") && dateCheckBox.isSelected()) {
                printWriter.println("#O Created " + dateFormat.format(date) + ".");
            }
            if (!commentField.getText().equals("")) {
                String commentText = commentField.getText().replaceAll("(.{67})", "$1\n").replaceAll("\n", "\n#C ");
                printWriter.println("#C " + commentText);
            }
            printWriter.println("x = " + x + ", y = " + y + ", rule = " + rules);
            printWriter.println(splitString);
            printWriter.close();
        } catch (IOException ioe) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save file.");
            alert.setContentText("Your pattern was not saved. Please try again.");
            alert.showAndWait();
        }

        System.out.println(file.toString());
    }

    public void setRules(ActionEvent actionEvent) {
        try {
            String ruleString = ruleInputField.getText().toUpperCase();
            gameOfLife.setRuleString(ruleString);
            System.out.println(gameOfLife.getRuleString());
            ruleInputField.setPromptText(gameOfLife.getRuleString());
        } catch (RulesFormatException rfe) {
            Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
            rulesAlert.setTitle("Error");
            rulesAlert.setHeaderText("Invalid rules");
            rulesAlert.setContentText("The rules you are trying to load are invalid. Remember to keep your digits from 0-8!");
            rulesAlert.showAndWait();
        }
        drawStrip();
    }

    public byte[][] trim(StaticBoard boardTrim) {
        int[] boundingBox = boardTrim.getBoundingBox();
        int x = Math.abs(boundingBox[1] - boundingBox[0] + 1);
        int y = Math.abs(boundingBox[3] - boundingBox[2] + 1);

        byte[][] trimmedBoard = new byte[x][y];

        int newX = 0;
        int newY = 0;

        for (int i = boundingBox[0]; i <= boundingBox[1]; i++) {
            for (int j = boundingBox[2]; j <= boundingBox[3]; j++) {
                if (boardTrim.boardGrid[i][j] == 1) {
                    trimmedBoard[newX][newY] = 1;
                }
                newY++;
            }
            newX++;
            newY = 0;
        }

        return trimmedBoard;
    }


    public String patternToString(byte[][] pattern) {
        StringBuffer patternString = new StringBuffer();
        if (pattern.length == 0) {
            return "";
        }

        for (int i = 0; i < pattern[0].length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                if (pattern[j][i] == 1) {
                    patternString.append("o");
                } else if (pattern[j][i] == 0) {
                    patternString.append("b");
                }
            }
            if (i < pattern[0].length - 1) {
                patternString.append("$");
            } else {
                patternString.append("!");
            }
        }

        return patternString.toString();
    }


    //triks litt med denne:
    public String encodeStringToRLE(String rawString) {
        StringBuilder encodedString = new StringBuilder();

        for (int i = 0; i < rawString.length(); i++) {
            int repetitions = 1;

            while ((i + 1 < rawString.length()) && (rawString.charAt(i) == rawString.charAt(i + 1))) {
                i++;
                repetitions++;
            }

            if (repetitions > 1) {
                encodedString.append(repetitions).append(rawString.charAt(i));
            } else {
                encodedString.append(rawString.charAt(i));
            }
        }
        return encodedString.toString();
    }

    public void drawStrip() {
        stripGol = (GameOfLife) gameOfLife.clone();
        stripBoard = stripGol.playBoard;
        double stripCellSize;
        GraphicsContext gc = strip.getGraphicsContext2D();
        gc.clearRect(0, 0, strip.widthProperty().doubleValue(), strip.heightProperty().doubleValue());
        gc.setFill(currentCellColor);
        Affine padding = new Affine();
        double xPadding = 10;
        double ty = (strip.getHeight() * 0.1);
        double tx = xPadding;
        padding.setTy(ty);
        gc.setLineWidth(1);
        for (int i = 0; i < 20; i++) {
            byte[][] trimmedBoard = trim(stripBoard);
            if (trimmedBoard.length >= trimmedBoard[0].length) {
                stripCellSize = (strip.getHeight() * 0.85 / trimmedBoard.length);
            } else {
                stripCellSize = (strip.getHeight() * 0.85 / trimmedBoard[0].length);
            }

            padding.setTx(tx);
            gc.setTransform(padding);

            for (int x = 0; x < trimmedBoard.length; x++) {
                for (int y = 0; y < trimmedBoard[0].length; y++) {
                    if (trimmedBoard[x][y] == 1) {
                        gc.fillRect(x * stripCellSize + 1, y * stripCellSize + 1, stripCellSize, stripCellSize);
                    }
                }
            }

            padding.setTx(tx - 13);
            gc.setTransform(padding);

            if (i > 0) {
                gc.strokeLine(0, 0, 0, strip.getHeight());
            }
            //tx += xPadding;
            stripGol.nextGeneration();
            tx += strip.getHeight() + xPadding;
        }

        //reset transform
        padding.setTx(0.0);
        gc.setTransform(padding);
    }


    public void exportGif() {
        gifGol = (GameOfLife) gameOfLife.clone();
        gifBoard = gifGol.playBoard;
        int counter = 20;


        if (gifSize/2 < exportBoard.boardGrid.length) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText("Board is too big");
            alert.setContentText("Your current board contains more cells than can be drawn! \nAs a result," +
                    " your gif might not show anything. \n\nTry increasing the gif size, decreasing board size" +
                    " or choosing the option to draw only the pattern to fix this!");
            alert.showAndWait();
        }

        if (!numFramesInputField.getText().isEmpty()) {
            counter = Integer.parseInt(numFramesInputField.getText());
            if (counter < 1 || counter > 400) {
                Alert counterAlert = new Alert(Alert.AlertType.INFORMATION);
                counterAlert.setTitle("Error");
                counterAlert.setHeaderText("Number of frames is to high");
                counterAlert.setContentText("The number of frames you have entered is too high. Please keep it between 1 and 400");
                counterAlert.showAndWait();
                return;
            }
        }
        System.out.println(counter);

        int fps = 5;

        if (!fpsInputField.getText().isEmpty()) {
            fps = Integer.parseInt(fpsInputField.getText());
            if (fps < 1 || fps > 60) {
                Alert fpsAlert = new Alert(Alert.AlertType.INFORMATION);
                fpsAlert.setTitle("Error");
                fpsAlert.setHeaderText("FPS too high");
                fpsAlert.setContentText("You're going too fast! Please keep your FPS input between 1 and 60");
                fpsAlert.showAndWait();
                return;
            }
        }
        System.out.println(fps);

        int milliseconds = 1000/fps;

        java.awt.Color backgroundColor = convertToawtColor(currentBackgroundColor);
        java.awt.Color cellColor = convertToawtColor(currentCellColor);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graphics Interchange Forma", "*.gif"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            String filePath = file.getPath();
            try {
                gifWriter = new GIFWriter(gifSize, gifSize, filePath, milliseconds);
                gifWriter.setBackgroundColor(backgroundColor);
                writeGoLSequenceToGIF(gifWriter, gifGol, counter, cellColor);
            } catch (IOException ioe) {
                System.out.println("dildo");
            }
        }
    }


    public void drawGifEntireBoard(GIFWriter writer, java.awt.Color cellColor) {
        gifCellSize = gifSize/gifBoard.boardGrid.length;
        int cellDrawSize = (int) gifCellSize - 1;
        int offset = (gifSize - (cellDrawSize * gifBoard.boardGrid.length)) / 2;
        for (int x = 1; x <= gifBoard.boardGrid.length; x++) {
            for (int y = 1; y <= gifBoard.boardGrid[0].length; y++) {
                if (gifBoard.boardGrid[x - 1][y - 1] == 1) {
                    writer.fillRect((x * cellDrawSize) - cellDrawSize + offset, (x * cellDrawSize) + offset,
                            (y * cellDrawSize) - cellDrawSize + offset, (y * cellDrawSize) + offset, cellColor);
                }
            }
        }
    }

    public void drawGifPatternOnly(GIFWriter writer, java.awt.Color cellColor) {
        byte[][] trimmed = trim(gifBoard);
        int cellDrawSize;

        if (trimmed.length >= trimmed[0].length) {
            gifCellSize = gifSize/trimmed.length;
            cellDrawSize = (int) gifCellSize - 1;
        } else {
            gifCellSize = gifSize/trimmed[0].length;
            cellDrawSize = (int) gifCellSize - 1;
        }

        int offsetX = (gifSize - (cellDrawSize * trimmed.length)) / 2;
        int offsetY = (gifSize - (cellDrawSize * trimmed[0].length)) / 2;

        for (int x = 1; x <= trimmed.length; x++) {
            for (int y = 1; y <= trimmed[0].length; y++) {
                if (trimmed[x - 1][y - 1] == 1) {
                    writer.fillRect((x * cellDrawSize) - cellDrawSize + offsetX, (x * cellDrawSize) + offsetX,
                            (y * cellDrawSize) - cellDrawSize + offsetY, (y * cellDrawSize) + offsetY, cellColor);
                }
            }
        }
    }


    void writeGoLSequenceToGIF(GIFWriter writer, GameOfLife game, int counter, java.awt.Color cellColor) throws IOException {
        if (counter == 0) {
            writer.close();
            System.out.println("DONE!");
        }
        else {
            writer.createNextImage();
            if (drawEntireBoard) {
                drawGifEntireBoard(writer, cellColor);
            } else if (!drawEntireBoard) {
                drawGifPatternOnly(writer, cellColor);
            }
            writer.insertCurrentImage();
            game.nextGeneration();
            System.out.println(counter);
            writeGoLSequenceToGIF(writer, game, counter-1, cellColor);
        }

    }

    public void chooseSizeClick(ActionEvent actionEvent) {
        String size = (String) chooseSizeBox.getValue();

        switch (size) {
            case "640x640": gifSize = 640;
                break;
            case "800x800":  gifSize = 800;
                break;
            case "1024x1024": gifSize = 1024;
                break;
            case "1200x1200": gifSize = 1200;
                break;
            case "1600x1600": gifSize = 1600;
                break;
            case "1920x1920": gifSize = 1920;
                break;
            default: gifSize = 800;
                break;
        }

        System.out.println(gifSize);

    }

    public void chooseDrawClick (ActionEvent actionEvent) {
        String draw = (String) chooseDrawBox.getValue();

        if (draw.equals("Entire Board")) {
            System.out.println("ENTIRE FUCKING BOARD!");
            drawEntireBoard = true;
        } else if (draw.equals("Pattern Only")) {
            System.out.println("FUCKING PATTERN ONLY!!");
            drawEntireBoard = false;
        }
    }

    public java.awt.Color convertToawtColor (Color color) {
        float red = (float) color.getRed();
        float green = (float) color.getGreen();
        float blue = (float) color.getBlue();
        float opacity = (float) color.getOpacity();

        java.awt.Color newColor = new java.awt.Color(red, green, blue, opacity);

        return newColor;
    }

        //TODO: Trix
}

