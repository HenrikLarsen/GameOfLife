package sample;

/**
 * Created by henriklarsen on 27.02.2017.
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class FileHandler extends Reader {

    public int read(char[] file, int off, int max){
        return 5;
    }

    public void readGameBoardFromDisk(File file) throws IOException {
        readGameBoard(new FileReader(file));
    }

    private void readGameBoard(Reader reader) throws IOException{

    }

    public void close(){

    }
}
