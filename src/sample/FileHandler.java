package sample;

/**
 * Created by henriklarsen on 27.02.2017.
 */

import java.io.*;

public class FileHandler extends Reader {
    //Gjør denne på et eller annet tidspunkt
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

        board = br.readLine();
        while (br.readLine() != null) {
            board = board + br.readLine();
        }

        char[] input = board.toCharArray();

        read(input, 0,1000);
        System.out.println(board);
        System.out.println(input);
    }

    public void close(){
    }
}
