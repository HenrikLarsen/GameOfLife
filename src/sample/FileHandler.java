package sample;

/**
 * Created by henriklarsen on 27.02.2017.
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class FileHandler extends Reader {

    //Gjør denne på et eller annet tidspunkt
    public int read(char[] file, int off, int max){
        return 5;
    }

    public void readGameBoardFromDisk(File file) throws IOException {
        try {
            readGameBoard(new FileReader(file));
        } catch (IOException io) {
            System.out.println("Dilldalldolldalldelldall1");
        }
    }

    private void readGameBoard(Reader reader) throws IOException{
        read();
        System.out.println("Dilldalldolldalldelldall");
    }

    public void close(){
    }
}
