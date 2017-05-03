package model;

import controller.PopUpAlerts;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The FileHandler class handles the reading and writing of RLE-files from and to disk. It also contains
 * the metadata of a loaded file.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class FileHandler {
    private Board playBoard;
    private GameOfLife gameOfLife;

    //Data fields relating to the metadata of loaded files
    public String metaTitle = "";
    public String metaData = "";

    /**
     * Method to read a file from a disk. Calls the readGameBoard method, and catches several exceptions,
     * giving a popup alert for many errors, explaining to the user what is wrong.
     * @param file The file to be read from disk.
     * @see #readGameBoard(Reader)
     * @see PopUpAlerts#patternFormatAlert()
     * @see PopUpAlerts#outOfBounds()
     * @see PopUpAlerts#ruleAlert2()
     * @exception IOException - Thrown if the file cannot be found etc.
     */
    public void readGameBoardFromDisk(File file) throws IOException{
        try {
            readGameBoard(new FileReader(file));
        } catch (PatternFormatException pfe) {
            PopUpAlerts.patternFormatAlert();
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            PopUpAlerts.outOfBounds();
        } catch (RulesFormatException rfe) {
            PopUpAlerts.ruleAlert2();
        }
    }

    /**
     * Method to read a file from an URL. Calls the readGameBoard method, and catches several exceptions,
     * giving a popup alert for many errors, explaining to the user what is wrong.
     * @param url The url to the file to be read from disk.
     * @see #readGameBoard(Reader)
     * @see PopUpAlerts#patternFormatAlert()
     * @see PopUpAlerts#outOfBounds()
     * @see PopUpAlerts#ruleAlert2()
     * @exception IOException - Thrown if the file cannot be found, or the url is invalid.
     */
    public void readGameBoardFromURL(String url) throws IOException {
        URL destination = new URL(url);
        URLConnection conn = destination.openConnection();
        try {
            readGameBoard(new InputStreamReader(conn.getInputStream()));
        } catch (PatternFormatException pfe) {
            PopUpAlerts.patternFormatAlert();
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            PopUpAlerts.outOfBounds();
        } catch (RulesFormatException rfe) {
            PopUpAlerts.ruleAlert2();
        }
    }

    /**
     * Method that reads a pattern from a file. Goes through the file line for line and updates the metaData
     * and metaTitle as well as sending a pattern to the Board. Heavy use of regular expressions to ensure
     * that the file is formatted correctly, and that all necessary information is covered.
     * @param reader the reader that will iterate through the file.
     * @see #reviseRLEString(String)
     * @see #boardFromFile(String, int, int)
     * @see #formatMetadata(StringBuilder)
     * @see GameOfLife#setRuleString(String)
     * @see Board#getWidth()
     * @see Board#getHeight()
     * @see Board#setBoardFromRLE(byte[][])
     * @exception IOException - Thrown if the file cannot be found, or the url is invalid.
     * @exception PatternFormatException - Thrown if the formatting of the file is incorrect
     * @exception ArrayIndexOutOfBoundsException - Thrown if the board is static, and the pattern exceeds its borders.
     * @exception RulesFormatException - Thrown if the read rules are incorrectly formatted
     */
    public void readGameBoard(Reader reader) throws IOException, PatternFormatException,
            ArrayIndexOutOfBoundsException, RulesFormatException{
        BufferedReader br = new BufferedReader(reader);

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder metaDataRaw = new StringBuilder();

        //Reads entire file, and adds it to stringBuilder and metaDataRaw
        String line;
        while((line = br.readLine()) != null) {
            stringBuilder.append(line).append("\n");
            if (line.startsWith("#") || line.startsWith("x")) {
                metaDataRaw.append(line).append("\n");
            }
        }
        br.close();

        //Trims the entire string to size, removing unnecessary whitespace.
        stringBuilder.trimToSize();

        //Calls formatMetadata to format the read metadata and set it as current metadata.
        formatMetadata(metaDataRaw);

        //Creates a Pattern based on a regular expression to get all necessary information from the string
        String regex = ("x(?: )=(?: )(\\d+),(?: )y(?: )=(?: )(\\d+)(?:,(?: )rule(?: )=(?: )(\\S\\d*[/]\\S\\d*))?\n(.*)");
        Pattern rlePattern = Pattern.compile(regex,Pattern.MULTILINE | Pattern.DOTALL);

        //Matches the pattern to the string. Throws PatternFormatException if the Matcher can't find what it wants.
        Matcher rleMatcher = rlePattern.matcher(stringBuilder);
        if(!rleMatcher.find()){
            throw new PatternFormatException();
        }

        //Creates variables based on the groups found from the matcher.
        int x = Integer.parseInt(rleMatcher.group(1));
        int y = Integer.parseInt(rleMatcher.group(2));
        String loadedRules = rleMatcher.group(3);
        String str = rleMatcher.group(4);

        //Tries to set the read rules
        gameOfLife.setRuleString(loadedRules);

        //Does necessary operations on the RLE-pattern string to decode it to regular "linear" string.
        String rleString = str.replaceAll("[\r\n]+", "");
        String revisedRleString = reviseRLEString(rleString);

        //Creates a 2D-array based on the revisedRleString and the dimensions of the pattern.
        byte[][] newBoard = boardFromFile(revisedRleString, x, y);

        //If the pattern is bigger than the board, and the board is Static, it throws an ArrayIndexOutOfBoundsException.
        if ((newBoard.length > playBoard.getWidth() || newBoard[0].length > playBoard.getHeight())
                && playBoard instanceof StaticBoard) {
            throw new ArrayIndexOutOfBoundsException();

        //If it is bigger, but the board is Dynamic, it expands to fit the new pattern.
        } else if(newBoard.length > playBoard.getWidth() || newBoard[0].length > playBoard.getHeight()
                && playBoard instanceof DynamicBoard){
            ((DynamicBoard) playBoard).expandHeightDown(
                    Math.max(playBoard.getHeight(), y) - Math.min(playBoard.getHeight(), y));
            ((DynamicBoard) playBoard).expandWidthRight(
                    Math.max(playBoard.getWidth(), x) - Math.min(playBoard.getWidth(), x));
        }

        //Sets the new pattern as loadedPattern in the Board class.
        playBoard.setBoardFromRLE(newBoard);
    }

    /**
     * Method that revises an RLE string, and creates a long string without leading numbers, only containing
     * o, b, $ and !.
     * @param rleString The string to be revised.
     * @see #decodeRLEtoStandardString(String, int)
     * @exception PatternFormatException - Thrown if the formatting of the file is incorrect
     */
    private String reviseRLEString(String rleString) throws PatternFormatException {
        int leadingNumber = 0;
        StringBuilder returnString = new StringBuilder();
        for(int i = 0; i < rleString.length(); i++) {

            char currentChar = rleString.charAt(i);

            //Updates leading number to reflect the number read.
            if(Character.isDigit(currentChar)) {
                leadingNumber = (10 * leadingNumber) + (currentChar - '0');

            //Calls decodeRLEtoStandardString for its respective character and appends it to the returnString.
            } else if (currentChar == 'o') {
                returnString.append(decodeRLEtoStandardString("1", leadingNumber));
                leadingNumber = 0;
            } else if (currentChar == 'b'){
                returnString.append(decodeRLEtoStandardString("0", leadingNumber));
                leadingNumber = 0;
            } else if (currentChar == '$') {
                returnString.append(decodeRLEtoStandardString("$", leadingNumber));
                leadingNumber = 0;

            //Returns when it encounters '!', as it marks the end of the RLE-file.
            } else if (currentChar == '!') {
                return returnString.toString();

            //Throws a PatternFormatException if it encounters characters other than the expected.
            } else {
                throw new PatternFormatException();
            }
        }

        //Returns null if it does not encounter "!" at the end of the String.
        return null;
    }

    /**
     * Method that takes a leading number and a character, and returns a string of that character times leading number.
     * @param s The character to be repeated.
     * @param i The number of repetitions.
     */
    private String decodeRLEtoStandardString(String s, int i) {
        if (i == 0) {
            i = 1;
        }

        StringBuilder deadOrAlive = new StringBuilder();
        for (int x = 0; x < i; x++) {
            deadOrAlive.append(s);
        }

        return deadOrAlive.toString();
    }

    /**
     * Method that creates a 2D-array the size of its x and y parameters, and puts the cell states from the string
     * into the 2D-array.
     * @param s The string to be converted to a cell grid
     * @param x The width of the new grid.
     * @param y The height of the new grid.
     * @return loadedBoard - The board created from the string.
     */
    private byte[][] boardFromFile(String s, int x, int y){
        byte[][] loadedBoard = new byte[x][y];

        int column = 0, row = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '0') {
                loadedBoard[column++][row] = 0;
            } else if (c == '1') {
                loadedBoard[column++][row] = 1;
            } else if (c == '$') {
                column = 0;
                row++;
            }
        }
        return loadedBoard;
    }

    /**
     * Method that formats the read metadata into a coherent string. Removes the annotations (#C etc) and
     * adds descriptions of the lines such as title, etc, and sets metaTitle and metaData accordingly.
     * @param meta The metadata that has been read by the reader.
     * @see #metaTitle
     * @see #metaData
     */
    private void formatMetadata(StringBuilder meta) {
        StringBuilder metaData = new StringBuilder();
        StringBuilder metaDataTitle = new StringBuilder();

        //Splits up the lines into separate strings.
        String[] lines = meta.toString().split("\\n");

        //Iterates through lines and appends the metadata string in the right order.
        for (String line : lines) {
            if (line.startsWith("#N")) {
                metaDataTitle.append("Title: ").append(line);
            } else if (line.startsWith("#O")) {
                metaData.append("Author: ").append(line).append("\n\n");
            } else if (line.startsWith("#c") || line.startsWith("#C")) {
                metaData.append(line).append(" ");
            } else if (line.startsWith("#r")) {
                metaData.append("Rule-set: ").append(line).append("\n");
            } else if (line.startsWith("#R") || line.startsWith("#P")) {
                metaData.append("Starts at: ").append(line).append("\n");
            } else if (line.startsWith("x")) {
                metaData.append("\n\n").append(line);
            }
        }
        String metaDataString = metaData.toString();
        String metaTitleString = metaDataTitle.toString();

        //Removes all metadata annotations (# followed by a character)
        String formattedMetaTitle = metaTitleString.replaceAll("[#]\\S\\s","");
        String formattedMetadata = metaDataString.replaceAll("[#]\\S\\s","");

        //Sets metaTitle and metaData from the formatted metadata.
        this.metaTitle = formattedMetaTitle;
        this.metaData = formattedMetadata;
    }

    /**
     * Method that resets the metadata and title.
     * @see #metaTitle
     * @see #metaData
     */
    public void resetMetaData() {
        metaTitle = "";
        metaData = "";
    }

    /**
     * A method to convert a 2D-array to a string according to RLE standards, but without the encoding of
     * leading numbers before a character. Iterates through the 2D-array and appends the stringBuilder according
     * to the cell state.
     * @param pattern The pattern to be converted into a string
     * @return patternString.toString() - The string representation of the pattern.
     */
    public String patternExportToString(byte[][] pattern) {
        StringBuilder patternString = new StringBuilder();
        if (pattern.length == 0) {
            return "";
        }

        //Iterates through the pattern and appends patternString according to cellState and row.
        for (int i = 0; i < pattern[0].length; i++) {
            for (byte[] aPattern : pattern) {
                if (aPattern[i] == 1) {
                    patternString.append("o");
                } else if (aPattern[i] == 0) {
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

    /**
     * A method that encodes a regular string to an RLE-string, changing repeating characters to a character with a
     * leading number.
     * @param rawString The string to be encoded
     * @return RLEString.toString() - The encoded RLE-string
     */
    public String stringToRLE(String rawString) {
        StringBuilder RLEString = new StringBuilder();
        for (int curChar = 0; curChar < rawString.length(); curChar++) {
            int repetitions = 1;
            int nextChar = curChar+1;
            while ((nextChar < rawString.length()) && (rawString.charAt(nextChar) == rawString.charAt(curChar))) {
                curChar++;
                nextChar++;
                repetitions++;
            }

            //Appends the repetitions and character to the string if repetitions is more than 1, and just the character
            //otherwise.
            if (repetitions > 1) {
                RLEString.append(repetitions).append(rawString.charAt(curChar));
            } else {
                RLEString.append(rawString.charAt(curChar));
            }
        }

        return RLEString.toString();
    }

    /**
     * A method for writing an RLE file to disk using a PrintWriter. Takes all metadata and the RLE file
     * and revise it to fit into the RLE standard, such as annotating metadata.
     * @param file The file to be written to.
     * @param x The width of the pattern.
     * @param y The height of the pattern.
     * @param rules String containing the data for the rules.
     * @param titleField String containing the title metadata.
     * @param authorField String containing the author metadata.
     * @param dateCheckBox Checkbox that indicates if data information should be included.
     * @param commentField String containing the comment metadata.
     * @param splitString String containing the RLE version of the pattern, split into 70-character lines.
     * @throws IOException if the file cannot be written to disk.
     */
    public void RLEtoDisk (File file, int x, int y, String rules, TextField titleField, TextField authorField,
                           CheckBox dateCheckBox, TextArea commentField, String splitString) throws IOException{
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();
        PrintWriter printWriter = new PrintWriter(file);

        //Checks each metadata field and writes it to the file if it exists.
        if (!titleField.getText().equals(""))
            printWriter.println("#N " + titleField.getText() + ".");
        if (!authorField.getText().equals("")) {

            //Adds date to the author metadata if the dateCheckGBox is checked.
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

        //Writes the x, y, rules metadata line.
        printWriter.println("x = " + x + ", y = " + y + ", rule = " + rules);

        //Writes the RLE-version of the pattern to file.
        printWriter.println(splitString);
        printWriter.close();
    }

    /**
     * Method that sets the current playBoard.
     * @param board The board to be set as current playBoard.
     * @see #playBoard
     */
    public void setBoard (Board board) {
        this.playBoard = board;
    }

    /**
     * Method that sets the current GameOfLife.
     * @param gol The GameOfLife to be set as current gameOfLife.
     * @see #gameOfLife
     */
    public void setGol (GameOfLife gol) {
        this.gameOfLife = gol;
    }

    /**
     * Method that returns the current metaTitle.
     * @return metaTitle - The title of the current pattern.
     * @see #metaTitle
     */
    public String getMetaTitle () {
        return metaTitle;
    }

    /**
     * Method that returns the current metaData.
     * @return metaTitle - The meta data of the current pattern.
     * @see #metaData
     */
    public String getMetaData() {
        return metaData;
    }
}