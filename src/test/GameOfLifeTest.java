package test;

import org.junit.Test;
import sample.GameOfLife;
import sample.StaticBoard;

import static org.junit.Assert.*;

/**
 * The GameOfLifeTest class is a JUnit class, doing unit testing of methods in the GameOfLife class. <br>
 * Its main purpose is to test and verify that the functionality of the GameOfLife class works as intended
 * and to root out inherent errors in the logic of its methods.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class GameOfLifeTest {
    private StaticBoard board = new StaticBoard();
    private GameOfLife gol = new GameOfLife(board);

    /**
     * Main test of GameOfLife's nextGeneration() method. Contains several other methods that tests
     * that the method works as intended for specific configurations of a static game board.
     * @see GameOfLife#nextGeneration()
     * @see #nextGenerationTest1()
     * @see #nextGenerationTest2()
     * @see #nextGenerationTest3()
     * @see #nextGenerationTest4()
     * @throws Exception if an error occurs while testing.
     */
    @Test
    public void nextGeneration() throws Exception {
        nextGenerationTest1();
        nextGenerationTest2();
        nextGenerationTest3();
        nextGenerationTest4();
    }

    /**
     * Main test of GameOfLife's enforceRules() method. Contains several other methods that tests
     * that the method works as intended for specific configurations of a static game board.
     * @see GameOfLife#enforceRules()
     * @see #enforceRulesTest1()
     * @see #enforceRulesTest2()
     * @see #enforceRulesTest3()
     * @throws Exception if an error occurs while testing.
     */
    @Test
    public void enforceRules() throws Exception{
        enforceRulesTest1();
        enforceRulesTest2();
        enforceRulesTest3();
    }

    /**
     * Does a test on the nextGeneration method of GameOfLife to see if the method correctly
     * sets the cells of the next generation based on the game's rules. <br>
     * Tests a 8/10 board configuration.
     * @see GameOfLife#nextGeneration()
     * @see StaticBoard#setBoard(byte[][])
     * @see StaticBoard#toString()
     */
    private void nextGenerationTest1() {
        byte[][] testBoard = {
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};

        board.setBoard(testBoard);
        gol.nextGeneration();
        String expectedOutput = "00000000000000000000101000000001100000000100000000000000000000000000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, gol.playBoard.toString());
    }

    /**
     * Does a test on the nextGeneration method of GameOfLife to see if the method correctly
     * sets the cells of the next generation based on the game's rules. <br>
     * Tests a 5/4 board configuration.
     * @see GameOfLife#nextGeneration()
     * @see StaticBoard#setBoard(byte[][])
     * @see StaticBoard#toString()
     */
    private void nextGenerationTest2 () {
        byte[][] testBoard = {
                {0, 0, 1, 1, 0},
                {0, 1, 0, 0, 1},
                {0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0}
        };

        board.setBoard(testBoard);
        gol.nextGeneration();
        String expectedOutput = "00000100101010100100";
        org.junit.Assert.assertEquals(expectedOutput, gol.playBoard.toString());
    }

    /**
     * Does a test on the nextGeneration method of GameOfLife to see if the method correctly
     * sets the cells of the next generation based on the game's rules. Iterates
     * through several generations to see that the method works consistently.<br>
     * Tests a glider (A figure that's supposed to move across the board diagonally) on a 6/6 board.
     * @see GameOfLife#nextGeneration()
     * @see StaticBoard#setBoard(byte[][])
     * @see StaticBoard#toString()
     */
    private void nextGenerationTest3 () {
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
        String expectedOutput = "000000101000011000010000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, gol.playBoard.toString());

        gol.nextGeneration();
        expectedOutput = "000000001000101000011000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, gol.playBoard.toString());

        gol.nextGeneration();
        expectedOutput = "000000010000001100011000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, gol.playBoard.toString());

        gol.nextGeneration();
        expectedOutput = "000000001000000100011100000000000000";
        org.junit.Assert.assertEquals(expectedOutput, gol.playBoard.toString());
    }

    /**
     * Does a test on the nextGeneration method of GameOfLife to see if the method correctly
     * sets the cells of the next generation based on the game's rules. Iterates
     * through several generations to see that the method works consistently.<br>
     * Tests the corners on a 6/6 board.
     * @see GameOfLife#nextGeneration()
     * @see StaticBoard#setBoard(byte[][])
     * @see StaticBoard#toString()
     */
    private void nextGenerationTest4 () {
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
        String expectedOutput = "101111000001101001100101100000111101";
        org.junit.Assert.assertEquals(expectedOutput, gol.playBoard.toString());

        gol.nextGeneration();
        expectedOutput = "000111001001010001100010100100111000";
        org.junit.Assert.assertEquals(expectedOutput, gol.playBoard.toString());

        gol.nextGeneration();
        expectedOutput = "000111001101010011110010101100111000";
        org.junit.Assert.assertEquals(expectedOutput, gol.playBoard.toString());
    }

    /**
     * Does a test on the enforceRules method of GameOfLife to see if the method correctly
     * enforces the rules of Conways game of life using the board and its neighbours.<br>
     * Tests a 6/6 board configuration.
     * @see GameOfLife#enforceRules()
     * @see StaticBoard#setBoard(byte[][])
     * @see StaticBoard#toString()
     */
    private void enforceRulesTest1() {
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
        gol.neighbourCount = neighbours;
        gol.enforceRules();
        board.setBoard(gol.newGenerationCells);

        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    /**
     * Does a test on the enforceRules method of GameOfLife to see if the method correctly
     * enforces the rules of Conways game of life using the board and its neighbours.<br>
     * Tests a 10/9 board configuration.
     * @see GameOfLife#enforceRules()
     * @see StaticBoard#setBoard(byte[][])
     * @see StaticBoard#countNeighbours()
     * @see StaticBoard#toString()
     */
    private void enforceRulesTest2() {
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
        gol.neighbourCount = board.countNeighbours();
        gol.enforceRules();
        board.setBoard(gol.newGenerationCells);
        String expectedOutput = "011111110101010101001010000000100000011111110101110001000000000100000001100000001010111000";

        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    /**
     * Does a test on the enforceRules method of GameOfLife to see if the method correctly
     * enforces the rules of Conways game of life using the board and its neighbours.<br>
     * Tests a 8/8 board configuration.
     * @see GameOfLife#enforceRules()
     * @see StaticBoard#setBoard(byte[][])
     * @see StaticBoard#countNeighbours()
     * @see StaticBoard#toString()
     */
    private void enforceRulesTest3() {
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
        gol.neighbourCount = board.countNeighbours();
        gol.enforceRules();
        board.setBoard(gol.newGenerationCells);
        String expectedOutput = "0000001000100001001100001011001110111101000011100110101000011100";

        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }
}