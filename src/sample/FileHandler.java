package sample;

/**
 * Created by henriklarsen on 27.02.2017.
 */

import javafx.scene.control.Alert;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileHandler extends Reader {

    public StaticBoard playBoard;
    public GameOfLife gameOfLife;

    public int read(char[] input, int off, int max){
        return 5;
    }

    public void readGameBoardFromDisk(File file) throws IOException, PatternFormatException, ArrayIndexOutOfBoundsException, RulesFormatException{
        readGameBoard(new FileReader(file));
    }

    private void readGameBoard(Reader reader) throws IOException, PatternFormatException, ArrayIndexOutOfBoundsException, RulesFormatException{
        BufferedReader br = new BufferedReader(reader);
        String regex = ("x(?: )=(?: )(\\d+),(?: )y(?: )=(?: )(\\d+)(?:,(?: )rule(?: )=(?: )(\\S\\d+[/]\\S\\d+))?\n(.*)");
        Pattern rlePattern = Pattern.compile(regex,Pattern.MULTILINE | Pattern.DOTALL);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder metaDataRaw = new StringBuilder();

        String line;
        while((line = br.readLine()) != null) {
            stringBuilder.append(line).append("\n");
            if (line.startsWith("#")) {
                metaDataRaw.append(line + "\n");
            }
        }
        stringBuilder.trimToSize();

        System.out.println(metaDataRaw);
        System.out.println(stringBuilder);

        Matcher rleMatcher = rlePattern.matcher(stringBuilder);
        if(!rleMatcher.find()){
            throw new PatternFormatException();
        }

        int x = Integer.parseInt(rleMatcher.group(1));
        int y = Integer.parseInt(rleMatcher.group(2));

        System.out.println("x = " + x + " y = " + y);

        String loadedRules = rleMatcher.group(3);

        gameOfLife.setRuleSet(loadedRules);

        String str = rleMatcher.group(4);

        System.out.println(loadedRules);
        System.out.println(rleMatcher.group(4));

        String rleString = str.replaceAll("[\r\n]+", "");

        //System.out.println(rleString);

        String revisedRleString = newBoard(rleString);

        //System.out.println(revisedRleString);

        byte[][] newBoard = boardFromFile(revisedRleString, x, y);

        String str2 = "";
        for (int t = 0; t < newBoard.length; t++) {
            for (int g = 0; g < newBoard[0].length; g++) {
                if (newBoard[t][g] == 1) {
                    str2 = str2 + "1";
                } else {
                    str2 = str2 + "0";
                }
            }
        }
        //System.out.println(str2);

        if (newBoard.length > playBoard.boardGrid.length || newBoard[0].length > playBoard.boardGrid[0].length) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            playBoard.setBoardFromRLE(newBoard);
        }

        String metaData = formatMetadata(metaDataRaw);
        System.out.println(metaData);
    }

    public String newBoard(String rleString) throws PatternFormatException {
        int leadingNumber = 0;
        String returnString = "";
        for(int i = 0; i < rleString.length(); i++) {
            char t = rleString.charAt(i);

            if(Character.isDigit(t)) {
                leadingNumber = (10 * leadingNumber) + (int) (t - '0');
            } else if (t == 'o') {
                returnString += getRevisedString("1", leadingNumber);
                leadingNumber = 0;
            } else if (t == 'b'){
                returnString += getRevisedString("0", leadingNumber);
                leadingNumber = 0;
            } else if (t == '$') {
                returnString += getRevisedString("$", leadingNumber);
                leadingNumber = 0;
            } else if (t == '!') {
                return returnString;
            } else {
                throw new PatternFormatException();
            }
        }
        return null;
    }

    public String getRevisedString(String s, int i) {
        if (i == 0) {
            i = 1;
        }

        String deadOrAlive = "";
        for (int x = 0; x < i; x++) {
            deadOrAlive += s;
        }
        return deadOrAlive;
    }

    public byte[][] boardFromFile(String s, int x, int y){
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

    public void readGameBoardFromURL(String url) throws IOException, PatternFormatException, RulesFormatException {
        URL destination = new URL(url);
        URLConnection conn = destination.openConnection();
        readGameBoard(new InputStreamReader(conn.getInputStream()));
    }

    public String formatMetadata(StringBuilder meta) {
        String formatedMetaData = "";
        String[] lines = meta.toString().split("\\n");
        for (int x = 0; x < lines.length; x++) {
            if (lines[x].startsWith("#N")) {
                formatedMetaData += "Title: " + lines[x] + "\n";
            } else if (lines[x].startsWith("#O")) {
                formatedMetaData += "Author: " + lines[x] + "\n";
            } else if (lines[x].startsWith("#c") || lines[x].startsWith("#C")) {
                formatedMetaData += lines[x] + "\n";
            } else if (lines[x].startsWith("#r")) {
                formatedMetaData += "Rule-set: " + lines[x] + "\n";
            } else if (lines[x].startsWith("#R") || lines[x].startsWith("#P")) {
                formatedMetaData += "Starts at: " + lines[x] + "\n";
            }
        }
        formatedMetaData = formatedMetaData.replaceAll("[#]\\S\\s","");
        return formatedMetaData;
    }

    public void close(){
    }



}
