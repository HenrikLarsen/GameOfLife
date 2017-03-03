package sample;

/**
 * Created by henriklarsen on 27.02.2017.
 */

import java.io.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.MULTILINE;

public class FileHandler extends Reader {

    public int read(char[] input, int off, int max){
        return 5;
    }

    public void readGameBoardFromDisk(File file) throws IOException {
        try {
            readGameBoard(new FileReader(file));
        } catch (PatternFormatException pfe) {}
    }

    private void readGameBoard(Reader reader) throws IOException, PatternFormatException{
        String board = "";
        BufferedReader br = new BufferedReader(reader);
        String regex = ("x(?: )=(?: )(\\d+),(?: )y(?: )=(?: )(\\d+), rule = b3/s23(.*)");
        //String regexrle = "([1-9]\\d*)?([bo$])";

        Pattern rlePattern = Pattern.compile(regex,Pattern.MULTILINE| Pattern.DOTALL);

        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
                sb.append(line);
        }
        sb.trimToSize();

        System.out.println(sb);

        Matcher rleMatcher = rlePattern.matcher(sb);
        rleMatcher.find();

        int x = Integer.parseInt(rleMatcher.group(1));
        int y = Integer.parseInt(rleMatcher.group(2));

        System.out.println("x = " + x + " y = " + y);

        String str = rleMatcher.group(3);
        String rleString = str.replaceAll("[\r\n]+", "");

        System.out.println(rleString);

        String[] lines = rleString.split("[$]");

        byte[][] newlyReadBoard = makeNewBoard(rleString, x, y);

        String str2 = "";


        for (int t = 0; t < newlyReadBoard[0].length; t++) {
            for (int g = 0; g < newlyReadBoard.length; g++) {
                if (newlyReadBoard[t][g] == 1) {
                    str2 = str2 + "1";
                } else {
                    str2 = str2 + "0";
                }
            }
        }
        System.out.println(str2);

    }

    public byte[][] makeNewBoard(String rleString, int x, int y){
        int antall = 0;
        byte[][] newlyReadBoard = new byte[x][y];
        for (int i = 0; i < newlyReadBoard[0].length; i++) {
            for (int j = 0; j < newlyReadBoard.length; j++) {
                newlyReadBoard[i][j]= 0;
            }
        }
        int from = 0;
        int m = 0;

        for(int i = 0; i < rleString.length(); i++){
            char t = rleString.charAt(i);
            if(Character.isDigit(t)) {
                antall = (10*antall) + (int) (t - '0');
            }else if(t == 'o') {
                Arrays.fill(newlyReadBoard[m], from, Math.min(x, from+Math.max(1, antall)), (byte) 1);
                from += Math.max(1, antall);
                antall = 0;
            }else if(t == 'b') {
                from += Math.max(1, antall);
                antall = 0;
            }else if(t == '$') {
                m += Math.max(1, antall);
                from = 0;
                antall = 0;
            }else if(t == '!'){
                return newlyReadBoard;
            }
        }
        return null;
    }

    public void constructBoard(String newBoard) {


    }

    public void close(){
    }
}
