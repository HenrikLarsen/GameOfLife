package model;

/**
 * Created by henriklarsen on 27.02.2017.
 */

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

public class FileHandler extends Reader {

    public Board playBoard;
    public GameOfLife gameOfLife;
    public String metaTitle = "";
    public String metaData = "";

    public int read(char[] input, int off, int max){
        return 5;
    }

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

    private void readGameBoard(Reader reader) throws IOException, PatternFormatException,
            ArrayIndexOutOfBoundsException, RulesFormatException{
        BufferedReader br = new BufferedReader(reader);
        String regex = ("x(?: )=(?: )(\\d+),(?: )y(?: )=(?: )(\\d+)(?:,(?: )rule(?: )=(?: )(\\S\\d*[/]\\S\\d*))?\n(.*)");
        Pattern rlePattern = Pattern.compile(regex,Pattern.MULTILINE | Pattern.DOTALL);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder metaDataRaw = new StringBuilder();

        String line;
        while((line = br.readLine()) != null) {
            stringBuilder.append(line).append("\n");
            if (line.startsWith("#") || line.startsWith("x")) {
                metaDataRaw.append(line).append("\n");
            }
        }
        stringBuilder.trimToSize();
        formatMetadata(metaDataRaw);

        Matcher rleMatcher = rlePattern.matcher(stringBuilder);
        if(!rleMatcher.find()){
            throw new PatternFormatException();
        }

        int x = Integer.parseInt(rleMatcher.group(1));
        int y = Integer.parseInt(rleMatcher.group(2));
        String loadedRules = rleMatcher.group(3);
        String str = rleMatcher.group(4);
        String rleString = str.replaceAll("[\r\n]+", "");
        String revisedRleString = newBoard(rleString);

        gameOfLife.setRuleString(loadedRules);
        byte[][] newBoard = boardFromFile(revisedRleString, x, y);

        if ((newBoard.length > playBoard.getWidth() || newBoard[0].length > playBoard.getHeight()) && playBoard instanceof StaticBoard) {
            throw new ArrayIndexOutOfBoundsException();
        } else if(newBoard.length > playBoard.getWidth() || newBoard[0].length > playBoard.getHeight() && playBoard instanceof DynamicBoard){
            ((DynamicBoard) playBoard).expandHeightDown(Math.max(playBoard.getHeight(), y) - Math.min(playBoard.getHeight(), y));
            ((DynamicBoard) playBoard).expandWidthRight(Math.max(playBoard.getWidth(), x) - Math.min(playBoard.getWidth(), x));
        }

        playBoard.setBoardFromRLE(newBoard);

    }

    private String newBoard(String rleString) throws PatternFormatException {
        int leadingNumber = 0;
        StringBuilder returnString = new StringBuilder();
        for(int i = 0; i < rleString.length(); i++) {

            char t = rleString.charAt(i);

            if(Character.isDigit(t)) {
                leadingNumber = (10 * leadingNumber) + (t - '0');
            } else if (t == 'o') {
                returnString.append(getRevisedString("1", leadingNumber));
                leadingNumber = 0;
            } else if (t == 'b'){
                returnString.append(getRevisedString("0", leadingNumber));
                leadingNumber = 0;
            } else if (t == '$') {
                returnString.append(getRevisedString("$", leadingNumber));
                leadingNumber = 0;
            } else if (t == '!') {
                return returnString.toString();
            } else {
                throw new PatternFormatException();
            }
        }
        return null;
    }

    private String getRevisedString(String s, int i) {
        if (i == 0) {
            i = 1;
        }

        StringBuilder deadOrAlive = new StringBuilder();
        for (int x = 0; x < i; x++) {
            deadOrAlive.append(s);
        }
        return deadOrAlive.toString();
    }

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

    private void formatMetadata(StringBuilder meta) {
        StringBuilder metaData = new StringBuilder();
        StringBuilder metaDataTitle = new StringBuilder();
        String[] lines = meta.toString().split("\\n");
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
        String formatedMetaTitle = metaTitleString.replaceAll("[#]\\S\\s","");
        String formatedMetadata = metaDataString.replaceAll("[#]\\S\\s","");
        this.metaTitle = formatedMetaTitle;
        this.metaData = formatedMetadata;
    }

    public void resetMetaData() {
        metaTitle = "";
        metaData = "";
    }

    public void close(){
    }


    public String patternToString(byte[][] pattern) {
        StringBuilder patternString = new StringBuilder();
        if (pattern.length == 0) {
            return "";
        }

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

            if (repetitions > 1) {
                RLEString.append(repetitions).append(rawString.charAt(curChar));
            } else {
                RLEString.append(rawString.charAt(curChar));
            }
        }

        return RLEString.toString();
    }

    public void RLEtoDisk (File file, int x, int y, String rules, TextField titleField, TextField authorField,
                           CheckBox dateCheckBox, TextArea commentField, String splitString) throws IOException{
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();
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
    }


}
