package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
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


    private StaticBoard exportBoard;
    private GameOfLife gameOfLife;
    public double cellSize;
    private Color currentCellColor = Color.LIMEGREEN;
    private Color currentBackgroundColor = Color.LIGHTGRAY;
    private boolean erase = false;
    private byte[][] boardAtMousePressed;
    private boolean grid = false;


    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        //drawEditorBoard();
        cellColorPicker.setValue(Color.LIMEGREEN);
        TextFormatter<String> ruleFormatter = new TextFormatter<String>( change -> {
            change.setText(change.getText().replaceAll("[^sSbB012345678/]", ""));
            return change;
        });
        ruleInputField.setTextFormatter(ruleFormatter);
    }

    public void drawEditorBoard() {
        GraphicsContext graphicsContext = editorCanvas.getGraphicsContext2D();
        graphicsContext.setFill(currentBackgroundColor);
        graphicsContext.fillRect(0,0,editorCanvas.getWidth(), editorCanvas.getHeight());
        graphicsContext.setFill(currentCellColor);
        cellSize = editorCanvas.getWidth() / exportBoard.boardGrid.length;

        for (int x = 0; x < exportBoard.boardGrid.length; x++) {
            for (int y = 0; y < exportBoard.boardGrid[0].length; y++) {
                if (exportBoard.boardGrid[x][y] == 1) {
                        graphicsContext.fillRect(x * cellSize + 1, y * cellSize + 1, cellSize, cellSize);
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
        int x = (int)(mouseEvent.getX()/cellSize);
        int y = (int)(mouseEvent.getY()/cellSize);

        //Checks that the user is within the playing board during click, and changes the cells if yes.
        if ((x < exportBoard.boardGrid.length) && (y < exportBoard.boardGrid[0].length) && x >= 0 && y >= 0) {

            //Sets global variable erase to true if the first clicked cell was alive.
            //If true, drag and click will only erase until the mouse is released. If false, method will only draw.
            if (exportBoard.boardGrid[x][y] == 1) {
                erase = true;
            }

            //Makes a copy of the board when the mouse button is pressed, stores as a global variable.
            boardAtMousePressed = new byte[exportBoard.boardGrid.length][exportBoard.boardGrid[0].length];
            for (int i = 0; i < boardAtMousePressed.length; i++) {
                for (int j = 0; j < boardAtMousePressed[i].length; j++) {
                    boardAtMousePressed[i][j] = exportBoard.boardGrid[i][j];
                }
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
        int x = Math.abs(boundingbox[1]-boundingbox[0]+1);
        int y = Math.abs(boundingbox[3]-boundingbox[2]+1);
        String rules = gameOfLife.getRuleString();

        byte[][] trimmedPattern = trim();

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
                printWriter.println("#N "+titleField.getText()+".");
            if (!authorField.getText().equals("")) {
                if (dateCheckBox.isSelected()) {
                    printWriter.println("#O "+authorField.getText()+". Created "+ dateFormat.format(date) + ".");
                } else {
                    printWriter.println("#O "+authorField.getText()+".");
                }
            } else if (authorField.getText().equals("") && dateCheckBox.isSelected()){
                printWriter.println("#O Created "+ dateFormat.format(date) + ".");
            }
            if (!commentField.getText().equals("")) {
                String commentText = commentField.getText().replaceAll("(.{67})", "$1\n").replaceAll("\n", "\n#C ");
                printWriter.println("#C " + commentText);
            }
            printWriter.println("x = "+x+", y = "+y+", rule = "+rules);
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
        } catch (RulesFormatException rfe) {
            Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
            rulesAlert.setTitle("Error");
            rulesAlert.setHeaderText("Invalid rules");
            rulesAlert.setContentText("The rules you are trying to load are invalid. Remember to keep your digits from 0-8!");
            rulesAlert.showAndWait();
        }
    }

    public byte[][] trim() {
        int[] boundingBox = exportBoard.getBoundingBox();
        int x = Math.abs(boundingBox[1] - boundingBox[0] + 1);
        int y = Math.abs(boundingBox[3] - boundingBox[2] + 1);

        byte[][] trimmedBoard = new byte[x][y];

        int newX = 0;
        int newY = 0;

        System.out.println(trimmedBoard.length);
        System.out.println(trimmedBoard[0].length);

        for (int i = boundingBox[0]; i <= boundingBox[1]; i++) {
            for (int j = boundingBox[2]; j <= boundingBox[3]; j++) {
                if (exportBoard.boardGrid[i][j] == 1) {
                    trimmedBoard[newX][newY] = 1;
                }
                newY++;
            }
            newX++;
            newY = 0;
        }

        return trimmedBoard;
    }


    public String patternToString (byte[][] pattern) {
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
            if (i < pattern[0].length-1) {
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
        StringBuffer encodedString = new StringBuffer();

        for (int i = 0; i < rawString.length(); i++) {
            int repetitions = 1;

            while ((i+1 < rawString.length()) && (rawString.charAt(i) == rawString.charAt(i+1))) {
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

    /*
    public void drawStrip() {
        GraphicsContext gc = strip.getGraphicsContext2D();
        gc.clearRect(0, 0, strip.widthProperty().doubleValue(), strip.heightProperty().doubleValue());
        Affine xform = new Affine();
        double tx = xpadding;
        < start strip loop>
                xform.setTx(tx);
                gc.setTransform(xform);
                <next generation call>
                <draw game board call>
                tx += <width of game board + xpadding>
        <end strip loop>

        //reset transform
        xform.setTx(0.0);
        gc.setTransform(xform);
    }*/


    //TODO: Prøv å lage en trim() metode som i oppgavesettet.  OK! DONE
    //TODO: Prøv å limiter antallet man kan skrive per linje i comments til 67. (UMULIG uten ganske hard koding)
    //TODO: Implementer "The Strip"
    //TODO: Implementer lagring til GIF.

    //TODO: HØR ANG FLYTTING AV METODER TIL ANDRE KLASSER. SPESIFIKT DET SOM RELATERER TIL SKRIVING AV FIL OG DUPLIKATKODE FRA CONTROLLER.JAVA

}
