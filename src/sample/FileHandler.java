package sample;

/**
 * Created by henriklarsen on 27.02.2017.
 */

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.MULTILINE;

public class FileHandler extends Reader {

    public int read(char[] input, int off, int max){
        // BARE TESTING AV Å LESE FILEN
        int charCount = 0;
        for(int i = 0; i < input.length; i++){
            char h = input[i];
            if(h == 98){ // b
                System.out.println("b");
            }else if(h == 111){ // o
                System.out.println("o");
            }else if(h == 36){ // $
                System.out.println("$");
            }
            charCount++;
        }
        System.out.println(charCount);
        return charCount;
    }

    public void readGameBoardFromDisk(File file) throws IOException {
        readGameBoard(new FileReader(file));
    }

    private void readGameBoard(Reader reader) throws IOException{
        String board = "";
        BufferedReader br = new BufferedReader(reader);
        String regex = ("x(?: )=(?: )(\\d+),(?: )y(?: )=(?: )(\\d+), rule = b3/s23(.*)");

        Pattern rlePattern = Pattern.compile(regex,Pattern.MULTILINE| Pattern.DOTALL);

        char[] a = new char[2000];
        br.read(a);
        board = new String(a);
        Matcher rleMatcher = rlePattern.matcher(board);
        rleMatcher.find();

        int x = Integer.parseInt(rleMatcher.group(1));
        int y = Integer.parseInt(rleMatcher.group(2));

        System.out.println(x+" "+y);

        String str = rleMatcher.group(3);
        String rleString = str.replaceAll("[\r\n]+", "");

        System.out.println(rleString);

        String[] lines = rleString.split("[$]");

        for (int i = 0; i < lines.length; i++) {
            System.out.println(lines[i]);
        }

        byte[][] newlyReadBoard = new byte[x][y];

    }

    public void constructBoard(String newBoard) {


    }

    public void close(){
    }
}
