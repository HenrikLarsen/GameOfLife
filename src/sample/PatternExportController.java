package sample;


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
    CheckBox dateCheckBox;
    @FXML
    ColorPicker cellColorPicker;
    @FXML
    Canvas strip;


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


    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        //drawEditorBoard();
        cellColorPicker.setValue(Color.LIMEGREEN);
        TextFormatter<String> ruleFormatter = new TextFormatter<String>(change -> {
            change.setText(change.getText().replaceAll("[^sSbB012345678/]", ""));
            return change;
        });
        ruleInputField.setTextFormatter(ruleFormatter);
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

        /*
        int[] boundingBox = exportBoard.getBoundingBox();
        for (int i = boundingBox[2]; i <= boundingBox[3]; i++) {
            for (int j = boundingBox[0]; j <= boundingBox[1]; j++) {
                if (exportBoard.boardGrid[j][i] == 1) {
                    patternString.append("o");
                } else if (exportBoard.boardGrid[j][i] == 0) {
                    patternString.append("b");
                }
            }
            if (i < boundingBox[3]) {
                patternString.append("$");
            } else {
                patternString.append("!");
            }
        }

        return patternString.toString(); */
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

    public void updateStripClick(ActionEvent actionEvent) {
        drawStrip();
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
        int counter = 100;
        int width = 800;
        int height = 800;
        int milliseconds = 100;

        float red = (float) currentBackgroundColor.getRed();
        float green = (float) currentBackgroundColor.getGreen();
        float blue = (float) currentBackgroundColor.getBlue();
        float opacity = (float) currentBackgroundColor.getOpacity();

        java.awt.Color backgroundColor = new java.awt.Color(red, green, blue, opacity);

        gifCellSize = width/gifBoard.boardGrid.length;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graphics Interchange Forma", "*.gif"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            String filePath = file.getPath();
            try {
                gifWriter = new GIFWriter(width, height, filePath, milliseconds);
                gifWriter.setBackgroundColor(backgroundColor);
                writeGoLSequenceToGIF(gifWriter, gifGol, counter);
            } catch (IOException ioe) {
                System.out.println("dildo");
            }
        }
    }

    public void drawGifBoard(GIFWriter writer) {
        float red = (float) currentCellColor.getRed();
        float green = (float) currentCellColor.getGreen();
        float blue = (float) currentCellColor.getBlue();
        float opacity = (float) currentCellColor.getOpacity();

        java.awt.Color cellColor = new java.awt.Color(red, green, blue, opacity);

        int size = (int)gifCellSize-1;
        int offset = (800-(size*gifBoard.boardGrid.length))/2;
        for (int x = 1; x <= gifBoard.boardGrid.length; x++) {
            for (int y = 1; y <= gifBoard.boardGrid[0].length; y++) {
                if (gifBoard.boardGrid[x-1][y-1] == 1) {
                    writer.fillRect((x * size)-size + offset, (x*size) + offset,
                            (y* size)- size + offset, (y* size)+offset, cellColor);
                }
            }
        }
    }

    void writeGoLSequenceToGIF(GIFWriter writer, GameOfLife game, int counter) throws IOException {
        if (counter == 0) {
            writer.close();
            System.out.println("DONE!");
        }
        else {
            writer.createNextImage();
            drawGifBoard(writer);
            writer.insertCurrentImage();
            game.nextGeneration();
            System.out.println(counter);
            writeGoLSequenceToGIF(writer, game, counter-1);
        }

    }

        //TODO: Implementer lagring til GIF.
        //TODO: Fiks GUI-elementer for GIF
        //TODO: Fiks slik at man ikke kan velge en størrelse som gjør at ting føkker seg GIF.
        //TODO: Trix med metoden
}
