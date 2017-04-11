package unitTesting;

import model.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Oscar_000 on 10.04.2017.
 */
public class FileHandlerTest {
    private Board board;
    private GameOfLife gol;
    private FileHandler fileHandler = new FileHandler();


    @Test
    public void readGameBoardFromDiskTest1() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/rlefiles/glider.rle");

        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        org.junit.Assert.assertEquals(8, board.getWidth());
        org.junit.Assert.assertEquals(8, board.getHeight());

        String loadedPattern = array2DToString(board.getLoadedPattern());
        String expectedPattern = "010001111";
        int[] loadedPatternBoundingBox = board.getLoadedPatternBoundingBox();
        int[] expectedBoundingBox = {2, 4, 2, 4};

        org.junit.Assert.assertEquals(expectedPattern, loadedPattern);
        org.junit.Assert.assertArrayEquals(expectedBoundingBox, loadedPatternBoundingBox);
    }

    @Test
    public void readGameBoardFromDiskMetadataTest1() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/rlefiles/glider.rle");

        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        String expectedMetaTitle = "Title: Glider";
        String actualMetaTitle = fileHandler.getMetaTitle();

        String expectedMetaData = "Author: Richard K. Guy\n\nThe smallest, most common, and first discovered " +
                "spaceship. Diagonal, has period 4 and speed c/4. www.conwaylife.com/wiki/index.php?title=Glider \n\n" +
                "x = 3, y = 3, rule = B3/S23";
        String actualMetaData = fileHandler.getMetaData();

        org.junit.Assert.assertEquals(expectedMetaData, actualMetaData);
        org.junit.Assert.assertEquals(expectedMetaTitle, actualMetaTitle);
    }

    @Test
    public void readGameBoardFromDiskTest2() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/rlefiles/gosperglidergun.rle");

        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        org.junit.Assert.assertEquals(36, board.getWidth());
        org.junit.Assert.assertEquals(9, board.getHeight());

        String loadedPattern = array2DToString(board.getLoadedPattern());
        String expectedPattern = "000000000000000000000000100000000000000000000000000000000010100" +
                "0000000000000000000001100000011000000000000110000000000010001000011000000000000111100" +
                "0000001000001000110000000000000011000000001000101100001010000000000000000000001000001000" +
                "0000100000000000000000000001000100000000000000000000000000000000110000000000000000000000";
        int[] loadedPatternBoundingBox = board.getLoadedPatternBoundingBox();
        int[] expectedBoundingBox = {0, 35, 0, 8};

        org.junit.Assert.assertEquals(expectedPattern, loadedPattern);
        org.junit.Assert.assertArrayEquals(expectedBoundingBox, loadedPatternBoundingBox);
    }

    @Test
    public void readGameBoardFromDiskMetadataTest2() {
        board = new DynamicBoard(8, 8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/rlefiles/gosperglidergun.rle");
        try {
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        String expectedMetaTitle = "Title: Gosper glider gun";
        String actualMetaTitle = fileHandler.getMetaTitle();

        String expectedMetaData = "Author: Bill Gosper\n\nA true period 30 glider gun. The first " +
                "known gun and the first known finite pattern with unbounded growth. " +
                "www.conwaylife.com/wiki/index.php?title=Gosper_glider_gun \n\n" +
                "x = 36, y = 9, rule = B3/S23";
        String actualMetaData = fileHandler.getMetaData();

        org.junit.Assert.assertEquals(expectedMetaData, actualMetaData);
        org.junit.Assert.assertEquals(expectedMetaTitle, actualMetaTitle);
    }

    @Test
    public void readGameBoardFromDiskNegativeTest() {
        board = new DynamicBoard(8, 8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/rlefiles/notafile.rle");

        try {
            fileHandler.readGameBoardFromDisk(file);
            org.junit.Assert.fail();
        } catch (IOException ioe) {}
    }

    @Test
    public void readGameBoardFromURLTest1() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        String url = "http://www.conwaylife.com/patterns/glider.rle";

        try{
            fileHandler.readGameBoardFromURL(url);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        org.junit.Assert.assertEquals(8, board.getWidth());
        org.junit.Assert.assertEquals(8, board.getHeight());

        String loadedPattern = array2DToString(board.getLoadedPattern());
        String expectedPattern = "010001111";
        int[] loadedPatternBoundingBox = board.getLoadedPatternBoundingBox();
        int[] expectedBoundingBox = {2, 4, 2, 4};

        org.junit.Assert.assertEquals(expectedPattern, loadedPattern);
        org.junit.Assert.assertArrayEquals(expectedBoundingBox, loadedPatternBoundingBox);
    }

    @Test
    public void readGameBoardFromURLMetadataTest1() {
        board = new DynamicBoard(8, 8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        String url = "http://www.conwaylife.com/patterns/glider.rle";

        try {
            fileHandler.readGameBoardFromURL(url);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        String expectedMetaTitle = "Title: Glider";
        String actualMetaTitle = fileHandler.getMetaTitle();

        String expectedMetaData = "Author: Richard K. Guy\n\nThe smallest, most common, and first discovered " +
                "spaceship. Diagonal, has period 4 and speed c/4. www.conwaylife.com/wiki/index.php?title=Glider \n\n" +
                "x = 3, y = 3, rule = B3/S23";
        String actualMetaData = fileHandler.getMetaData();

        org.junit.Assert.assertEquals(expectedMetaData, actualMetaData);
        org.junit.Assert.assertEquals(expectedMetaTitle, actualMetaTitle);
    }

    @Test
    public void readGameBoardFromURLTest2() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        String url = "http://www.conwaylife.com/patterns/gosperglidergun.rle";

        try {
            fileHandler.readGameBoardFromURL(url);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        org.junit.Assert.assertEquals(36, board.getWidth());
        org.junit.Assert.assertEquals(9, board.getHeight());

        String loadedPattern = array2DToString(board.getLoadedPattern());
        String expectedPattern = "000000000000000000000000100000000000000000000000000000000010100" +
                "0000000000000000000001100000011000000000000110000000000010001000011000000000000111100" +
                "0000001000001000110000000000000011000000001000101100001010000000000000000000001000001000" +
                "0000100000000000000000000001000100000000000000000000000000000000110000000000000000000000";
        int[] loadedPatternBoundingBox = board.getLoadedPatternBoundingBox();
        int[] expectedBoundingBox = {0, 35, 0, 8};

        org.junit.Assert.assertEquals(expectedPattern, loadedPattern);
        org.junit.Assert.assertArrayEquals(expectedBoundingBox, loadedPatternBoundingBox);
    }

    @Test
    public void readGameBoardFromURLMetadataTest2() {
        board = new DynamicBoard(8, 8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        String url = "http://www.conwaylife.com/patterns/gosperglidergun.rle";

        try {
            fileHandler.readGameBoardFromURL(url);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        String expectedMetaTitle = "Title: Gosper glider gun";
        String actualMetaTitle = fileHandler.getMetaTitle();

        String expectedMetaData = "Author: Bill Gosper\n\nA true period 30 glider gun. The first " +
                "known gun and the first known finite pattern with unbounded growth. " +
                "www.conwaylife.com/wiki/index.php?title=Gosper_glider_gun \n\n" +
                "x = 36, y = 9, rule = B3/S23";
        String actualMetaData = fileHandler.getMetaData();

        org.junit.Assert.assertEquals(expectedMetaData, actualMetaData);
        org.junit.Assert.assertEquals(expectedMetaTitle, actualMetaTitle);
    }

    @Test
    public void readGameBoardFromURLNegativeTest() {
        board = new DynamicBoard(8, 8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        String url = "http://www.notanactualURL.rle";

        try {
            fileHandler.readGameBoardFromURL(url);
            org.junit.Assert.fail();
        } catch (IOException ioe) {}
    }

    @Test
    public void ruleImportTest1() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/testfiles/ruletest.rle");

        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        String expecedRules = "B2456/S28";
        String actualRules = gol.getRuleString();

        org.junit.Assert.assertEquals(expecedRules, actualRules);
    }

    @Test (expected = ExceptionInInitializerError.class)
    public void negativeRulesImportTest1() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/testfiles/negativeruletest.rle");

        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }
    }

    //There appears to be a bug that makes the popup window shown when the formatting is wrong
    //to throw NoClassDefFoundError instead of ExceptionInInitializerError when using junit.
    //As long as the error comes from a popup window, the right exception is thrown in the method.
    @Test(expected = NoClassDefFoundError.class)
    public void negativeFormatImportTest() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/testfiles/negativeFormatTest.rle");

        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }
    }

    @Test
    public void patternExportToStringTest1 () {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/rlefiles/glider.rle");
        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        String expected = "bob$bbo$ooo!";
        String actual = fileHandler.patternExportToString(board.getLoadedPattern());

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void patternExportToStringTest2 () {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/rlefiles/gosperglidergun.rle");
        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        String expected = "bbbbbbbbbbbbbbbbbbbbbbbbobbbbbbbbbbb$bbbbbbbbbbbbbbbbbbbbbbobobbbbbbbbbbb$" +
                "bbbbbbbbbbbboobbbbbboobbbbbbbbbbbboo$bbbbbbbbbbbobbbobbbboobbbbbbbbbbbboo$" +
                "oobbbbbbbbobbbbbobbboobbbbbbbbbbbbbb$oobbbbbbbbobbboboobbbbobobbbbbbbbbbb$" +
                "bbbbbbbbbbobbbbbobbbbbbbobbbbbbbbbbb$bbbbbbbbbbbobbbobbbbbbbbbbbbbbbbbbbb$" +
                "bbbbbbbbbbbboobbbbbbbbbbbbbbbbbbbbbb!";
        String actual = fileHandler.patternExportToString(board.getLoadedPattern());

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test (expected = NullPointerException.class)
    public void patternExportToStringNegativeTest () {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        String expected = "";
        String actual = fileHandler.patternExportToString(board.getLoadedPattern());
        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void stringExportToRLETest1 () {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/rlefiles/glider.rle");
        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        String formatted = fileHandler.patternExportToString(board.getLoadedPattern());

        String expected = "bob$2bo$3o!";
        String actual = fileHandler.stringToRLE(fileHandler.stringToRLE(formatted));

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void stringExportToRLETest2 () {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/rlefiles/acorn.rle");
        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        String formatted = fileHandler.patternExportToString(board.getLoadedPattern());

        String expected = "bo5b$3bo3b$2o2b3o!";
        String actual = fileHandler.stringToRLE(fileHandler.stringToRLE(formatted));

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void stringExportToRLETest3 () {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/rlefiles/period45glidergun.rle");
        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        String formatted = fileHandler.patternExportToString(board.getLoadedPattern());

        String expected = "15bo2bo2bo26b$15b7o26b$48b$15b7o26b$15bo2bo2bo26b$48b$48b$48b$48b$48b$16bo2bo28b$" +
                "12b2o2bo2bo28b$12b2o2bo2bo7b2o19b$27b2o4b2o3bo2bo3b2ob$23b5o4b5ob$23b2o3bo2bo3b2ob$27b3o18b$" +
                "48b$2ob2o43b$bobo23b3o18b$bobo6b3o35b$2ob2o29bobo21b$bobo31b2o21b$bobo6b3o22bo12b$2ob2o43b$" +
                "48b$21b2o35b$21b2o7bo2bo2b2o20b$20bo2bo2b2o20b$20bo2bo24b$48b$48b$47bo$45bobo$46b2o$" +
                "18bo2bo2bo23b$18b7o23b$48b$18b7o23b$18bo2bo2bo23b!";
        String actual = fileHandler.stringToRLE(fileHandler.stringToRLE(formatted));

        org.junit.Assert.assertEquals(expected, actual);
    }

    private String array2DToString(byte[][] neighbour) {
        String str = "";
        for (int y = 0; y < neighbour[0].length; y++) {
            for (int x = 0; x < neighbour.length; x++) {
                str = str + neighbour[x][y];
            }
        }
        return str;
    }

}