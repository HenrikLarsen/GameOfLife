package unitTesting;

import model.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * JUnit class for unit testing of methods in the GameOfLife class.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class GameOfLifeTest {
    private Board board;
    private GameOfLife gol;
    private final ThreadWorker threadWorker = ThreadWorker.getInstance();

    @Test
    public void nextGenerationTest1() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};

        board.setBoard(testBoard);
        gol.nextGeneration();
        String expectedOutput = "000000000000000000010100000001100000001000000000000000000000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(9, board.getWidth());
        org.junit.Assert.assertEquals(8, board.getHeight());
    }

    @Test
    public void nextGenerationTest2 () {
        board = new DynamicBoard(4,5);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 0, 1, 1, 0},
                {0, 1, 0, 0, 1},
                {0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0}
        };

        board.setBoard(testBoard);
        gol.nextGeneration();
        String expectedOutput = "000000010001010010100010000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(5, board.getWidth());
        org.junit.Assert.assertEquals(6, board.getHeight());
    }

    @Test
    public void nextGenerationTest3 () {
        board = new DynamicBoard(6,6);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 0, 1, 0, 0, 0},
                {1, 0, 1, 0, 0, 0},
                {0, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        board.setBoard(testBoard);
        gol.nextGeneration();
        String expectedOutput = "0000000000000001010000011000001000000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(7, board.getWidth());
        org.junit.Assert.assertEquals(7, board.getHeight());

        gol.nextGeneration();
        expectedOutput = "0000000000000000010000101000001100000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());

        gol.nextGeneration();
        expectedOutput = "0000000000000000100000001100001100000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());

        gol.nextGeneration();
        expectedOutput = "0000000000000000010000000100001110000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    @Test
    public void nextGenerationTest4 () {
        board = new DynamicBoard(6,6);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 0, 1},
                {1, 0, 0, 1, 0, 1},
                {1, 0, 1, 0, 0, 1},
                {1, 0, 0, 0, 1, 1},
                {1, 1, 1, 1, 1, 1}
        };

        board.setBoard(testBoard);
        gol.nextGeneration();
        String expectedOutput = "0011110001011110100000111101001111001011110000010111101000111100";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(8, board.getWidth());
        org.junit.Assert.assertEquals(8, board.getHeight());

        gol.nextGeneration();
        expectedOutput = "000011000000010001000010000010010010000010010000011" +
                "0000010010000010010010000010000100010000000110000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(10, board.getWidth());
        org.junit.Assert.assertEquals(10, board.getHeight());

        gol.nextGeneration();
        expectedOutput = "00000000000000000100000000001100000000011000000000111000" +
                "0000011000000000000000000110000000011100000000011000000000110000000000100000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(12, board.getWidth());
        org.junit.Assert.assertEquals(12, board.getHeight());
    }

    @Test
    public void nextGenerationConcurrentTest1() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};

        board.setBoard(testBoard);
        gol.setThreadWorkers(threadWorker);
        gol.nextGenerationConcurrent();
        String expectedOutput = "000000000000000000010100000001100000001000000000000000000000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(9, board.getWidth());
        org.junit.Assert.assertEquals(8, board.getHeight());
    }

    @Test
    public void nextGenerationConcurrentTest2 () {
        board = new DynamicBoard(4,5);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 0, 1, 1, 0},
                {0, 1, 0, 0, 1},
                {0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0}
        };

        board.setBoard(testBoard);
        gol.setThreadWorkers(threadWorker);
        gol.nextGenerationConcurrent();
        String expectedOutput = "000000010001010010100010000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(5, board.getWidth());
        org.junit.Assert.assertEquals(6, board.getHeight());
    }

    @Test
    public void nextGenerationConcurrentTest3 () {
        board = new DynamicBoard(6,6);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 0, 1, 0, 0, 0},
                {1, 0, 1, 0, 0, 0},
                {0, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        board.setBoard(testBoard);
        gol.setThreadWorkers(threadWorker);
        gol.nextGenerationConcurrent();
        String expectedOutput = "0000000000000001010000011000001000000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(7, board.getWidth());
        org.junit.Assert.assertEquals(7, board.getHeight());

        gol.nextGenerationConcurrent();
        expectedOutput = "0000000000000000010000101000001100000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());

        gol.nextGenerationConcurrent();
        expectedOutput = "0000000000000000100000001100001100000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());

        gol.nextGenerationConcurrent();
        expectedOutput = "0000000000000000010000000100001110000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    @Test
    public void nextGenerationConcurrentTest4 () {
        board = new DynamicBoard(6,6);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 0, 1},
                {1, 0, 0, 1, 0, 1},
                {1, 0, 1, 0, 0, 1},
                {1, 0, 0, 0, 1, 1},
                {1, 1, 1, 1, 1, 1}
        };

        board.setBoard(testBoard);
        gol.setThreadWorkers(threadWorker);
        gol.nextGenerationConcurrent();
        String expectedOutput = "0011110001011110100000111101001111001011110000010111101000111100";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(8, board.getWidth());
        org.junit.Assert.assertEquals(8, board.getHeight());

        gol.nextGenerationConcurrent();
        expectedOutput = "000011000000010001000010000010010010000010010000011" +
                "0000010010000010010010000010000100010000000110000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(10, board.getWidth());
        org.junit.Assert.assertEquals(10, board.getHeight());

        gol.nextGenerationConcurrent();
        expectedOutput = "00000000000000000100000000001100000000011000000000111000" +
                "0000011000000000000000000110000000011100000000011000000000110000000000100000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
        org.junit.Assert.assertEquals(12, board.getWidth());
        org.junit.Assert.assertEquals(12, board.getHeight());
    }

    @Test
    public void nextGenerationConcurrentTest5 () {
        board = new DynamicBoard(20, 20);
        gol = new GameOfLife(board);
        FileHandler fileHandler = new FileHandler();
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        //An RLE-file that oscillates every 6 generations.
        File file = new File("src/resources/testFiles/blonkerTest.rle");

        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        board.finalizeBoard();
        gol.setThreadWorkers(threadWorker);
        String expectedOutput = board.toString();

        for (int x = 0; x < 1200; x++) {
            gol.nextGenerationConcurrent();
        }

        String actualOutput = board.toString();
        org.junit.Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void nextGenerationConcurrentTest6 () {
        board = new DynamicBoard(20, 20);
        gol = new GameOfLife(board);
        FileHandler fileHandler = new FileHandler();
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        //An RLE-file that oscillates every 16 generations.
        File file = new File("src/resources/testFiles/achimTest.rle");

        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }

        board.finalizeBoard();
        gol.setThreadWorkers(threadWorker);
        String expectedOutput = board.toString();

        for (int x = 0; x < 1600; x++) {
            gol.nextGenerationConcurrent();
        }

        String actualOutput = board.toString();
        org.junit.Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void enforceRulesTest1() {
        board = new DynamicBoard(6,6);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 0},
                {1, 0, 0, 1, 0, 0}};

        byte[][] neighbours = {
                {1, 1, 2, 1, 1, 0},
                {1, 1, 4, 2, 2, 0},
                {2, 3, 4, 3, 3, 1},
                {2, 2, 4, 3, 1, 1},
                {3, 2, 3, 2, 2, 1},
                {1, 2, 2, 0, 1, 0}};

        String expectedOutput = "000010001110000010011100001000000000";

        board.setBoard(testBoard);
        gol.setNeighbourCount(neighbours);
        gol.enforceRules();
        board.setBoard(gol.getNewGenerationCells());

        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    @Test
    public void enforceRulesTest2() {
        board = new DynamicBoard(9,10);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 1, 1, 0, 0, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1},
                {0, 1, 0, 1, 0, 0, 0, 1, 0, 1},
                {0, 1, 1, 0, 0, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1}};

        board.setBoard(testBoard);
        gol.setNeighbourCount(board.countNeighbours());
        gol.enforceRules();
        board.setBoard(gol.getNewGenerationCells());
        String expectedOutput = "011111110101010101001010000000100000011111110101" +
                "110001000000000100000001100000001010111000";

        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    @Test
    public void enforceRulesTest3() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 0, 1, 1, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 0},
                {1, 0, 1, 1, 0, 0, 0, 1},
                {0, 0, 0, 1, 0, 0, 0, 1},
                {0, 0, 0, 0, 1, 0, 1, 1},
                {1, 0, 0, 0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 1, 1, 0, 0, 0}};

        board.setBoard(testBoard);
        gol.setNeighbourCount(board.countNeighbours());
        gol.enforceRules();
        board.setBoard(gol.getNewGenerationCells());
        String expectedOutput = "0000001000100001001100001011001110111101000011100110101000011100";

        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    @Test
    public void enforceRulesAlternateRulesTest1() {
        board = new DynamicBoard(6,6);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 0},
                {1, 0, 0, 1, 0, 0}};

        board.setBoard(testBoard);
        gol.setNeighbourCount(board.countNeighbours());

        gol.setRuleSet("B012345678/S");
        gol.enforceRules();
        board.setBoard(gol.getNewGenerationCells());

        String expectedOutput = "111110101001110111100110111011111111";

        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    @Test
    public void enforceRulesAlternateRulesTest2() {
        board = new DynamicBoard(6,6);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 0},
                {1, 0, 0, 1, 0, 0}};

        board.setBoard(testBoard);
        gol.setNeighbourCount(board.countNeighbours());

        gol.setRuleSet("B/S");
        gol.enforceRules();
        board.setBoard(gol.getNewGenerationCells());

        String expectedOutput = "000000000000000000000000000000000000";

        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    @Test
    public void enforceRulesAlternateRulesTest3() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 0, 1, 1, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 0},
                {1, 0, 1, 1, 0, 0, 0, 1},
                {0, 0, 0, 1, 0, 0, 0, 1},
                {0, 0, 0, 0, 1, 0, 1, 1},
                {1, 0, 0, 0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 1, 1, 0, 0, 0}};

        board.setBoard(testBoard);
        gol.setNeighbourCount(board.countNeighbours());
        try {
            gol.setRuleString("Life Without Death");
        } catch (RulesFormatException rfe) {
            org.junit.Assert.fail();
        }
        gol.enforceRules();
        board.setBoard(gol.getNewGenerationCells());

        String expectedOutput = "0110011100100001101100011011001110111101010011100110101010111110";

        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    @Test
    public void enforceRulesAlternateRulesTest4() {
        board = new DynamicBoard(9,10);
        gol = new GameOfLife(board);
        byte[][] testBoard = {
                {0, 1, 1, 0, 0, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1},
                {0, 1, 0, 1, 0, 0, 0, 1, 0, 1},
                {0, 1, 1, 0, 0, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1}};

        board.setBoard(testBoard);
        gol.setNeighbourCount(board.countNeighbours());
        try {
            gol.setRuleString("34 Life");
        } catch (RulesFormatException rfe) {
            org.junit.Assert.fail();
        }
        gol.enforceRules();
        board.setBoard(gol.getNewGenerationCells());

        String expectedOutput = "00111010000111010001101101000000000001111111" +
                "0011101001000000000101110001100000101010101000";

        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    @Test
    public void setRuleStringTest1() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);

        try {
            gol.setRuleString("Replicator");
        } catch (RulesFormatException rfe) {
            org.junit.Assert.fail();
        }

        String expectedRuleString = "B1357/S1357";
        String expectedBornRule = "1357";
        String expectedSurviveRule = "1357";
        String expectedRuleName = "Replicator";

        org.junit.Assert.assertEquals(expectedRuleString, gol.getRuleString());
        org.junit.Assert.assertEquals(expectedBornRule, gol.getBornRules());
        org.junit.Assert.assertEquals(expectedSurviveRule, gol.getSurviveRules());
        org.junit.Assert.assertEquals(expectedRuleName, gol.getRuleName());
    }

    @Test
    public void setRuleStringTest2() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);

        try {
            gol.setRuleString("B368/S245");
        } catch (RulesFormatException rfe) {
            org.junit.Assert.fail();
        }

        String expectedRuleString = "B368/S245";
        String expectedBornRule = "368";
        String expectedSurviveRule = "245";
        String expectedRuleName = "Morley";

        org.junit.Assert.assertEquals(expectedRuleString, gol.getRuleString());
        org.junit.Assert.assertEquals(expectedBornRule, gol.getBornRules());
        org.junit.Assert.assertEquals(expectedSurviveRule, gol.getSurviveRules());
        org.junit.Assert.assertEquals(expectedRuleName, gol.getRuleName());
    }

    @Test
    public void setRuleStringTest3() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);

        try {
            gol.setRuleString("");
            org.junit.Assert.fail();
        } catch (RulesFormatException rfe) {
            System.out.println("Success");
        }
    }

    @Test
    public void setRuleSetTest1() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);

        gol.setRuleSet("B4678/S35678");

        String expectedRuleString = "B4678/S35678";
        String expectedBornRule = "4678";
        String expectedSurviveRule = "35678";

        org.junit.Assert.assertEquals(expectedRuleString, gol.getRuleString());
        org.junit.Assert.assertEquals(expectedBornRule, gol.getBornRules());
        org.junit.Assert.assertEquals(expectedSurviveRule, gol.getSurviveRules());
    }

    @Test
    public void setRuleSetTest2() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);

        gol.setRuleSet("B0123456777778/S000012333344567853478");

        String expectedRuleString = "B012345678/S012345678";
        String expectedBornRule = "012345678";
        String expectedSurviveRule = "012345678";

        org.junit.Assert.assertEquals(expectedRuleString, gol.getRuleString());
        org.junit.Assert.assertEquals(expectedBornRule, gol.getBornRules());
        org.junit.Assert.assertEquals(expectedSurviveRule, gol.getSurviveRules());
    }

    @Test
    public void setRuleSetTest3() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);

        gol.setRuleSet("B/S");

        String expectedRuleString = "B/S";
        String expectedBornRule = "";
        String expectedSurviveRule = "";

        org.junit.Assert.assertEquals(expectedRuleString, gol.getRuleString());
        org.junit.Assert.assertEquals(expectedBornRule, gol.getBornRules());
        org.junit.Assert.assertEquals(expectedSurviveRule, gol.getSurviveRules());
    }

    //There appears to be a bug that makes the popup window shown when the formatting is wrong
    //to throw NoClassDefFoundError instead of ExceptionInInitializerError when using junit.
    //As long as the error comes from a popup window, the right exception is thrown in the method.
    @Test(expected = NoClassDefFoundError.class)
    //@Test(expected = ExceptionInInitializerError.class)
    public void setRuleSetTest4() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        gol.setRuleSet("B3/S9");
    }

    @Test
    public void cloneTest() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);

        GameOfLife shallowGol = gol;
        org.junit.Assert.assertEquals(gol, shallowGol);

        GameOfLife clonedGol = (GameOfLife)gol.clone();
        org.junit.Assert.assertFalse(clonedGol.equals(gol));
        org.junit.Assert.assertFalse(clonedGol.getPlayBoard().equals(board));

        board = new DynamicBoard(2,2);
        org.junit.Assert.assertFalse(clonedGol.getPlayBoard().getHeight() == board.getHeight());
        org.junit.Assert.assertFalse(clonedGol.getPlayBoard().getWidth() == board.getWidth());
    }
}