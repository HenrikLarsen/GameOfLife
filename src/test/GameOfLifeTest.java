package test;

import org.junit.Test;
import sample.GameOfLife;
import sample.StaticBoard;

import static org.junit.Assert.*;

/**
 * Created by Oscar Vladau on 16.02.2017.
 */
public class GameOfLifeTest {
    private StaticBoard board = new StaticBoard();
    private GameOfLife gol = new GameOfLife(board);

    @Test
    public void nextGeneration() throws Exception {
        nextGenerationTest1();
        nextGenerationTest2();
        nextGenerationTest3();
        nextGenerationTest4();
    }

    @Test
    public void enforceRules() throws Exception{
        enforceRulesTest1();
        enforceRulesTest2();
        enforceRulesTest3();
    }

    private void nextGenerationTest1 () {
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