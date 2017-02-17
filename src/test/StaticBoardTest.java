package test;

import org.junit.Test;
import sample.GameOfLife;
import sample.StaticBoard;

import static org.junit.Assert.*;

/**
 * Created by Oscar Vladau on 16.02.2017.
 */
public class StaticBoardTest {
    private StaticBoard board = new StaticBoard();

    @Test
    public void countNeighbours() throws Exception {
        countNeighboursTest1();
        countNeighboursTest2();
        countNeighboursTest3();
    }

    @Test
    public void resetBoard() throws Exception{
        resetBoardTest1();
        resetBoardTest2();
        resetBoardTest3();
    }

    private void countNeighboursTest1() {
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 0},
                {1, 0, 0, 1, 0, 0}};


        String expectedOutput = "112231113222244432123320123121001110";

        board.setBoard(testBoard);
        byte[][]neighbourCount = board.countNeighbours();

        org.junit.Assert.assertEquals(expectedOutput, neighbourToString(neighbourCount));
    }
    private void countNeighboursTest2() {
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 1, 0 ,0},
                {1, 1, 0, 0, 0, 1, 0 ,0},
                {0, 0, 1, 0, 0, 1, 0 ,0},
                {1, 0, 0, 0, 0, 0, 0 ,0},
                {0, 0, 0, 0, 0, 1, 1 ,0},
                {0, 0, 1, 0, 0, 0, 0 ,0},
                {0, 0, 1, 0, 1, 1, 0 ,0},
                {0, 1, 1, 0, 1, 0, 0 ,1}};


        String expectedOutput = "2130101122422242121111320111135423221322121314232323132200011110";

        board.setBoard(testBoard);
        byte[][]neighbourCount = board.countNeighbours();

        org.junit.Assert.assertEquals(expectedOutput, neighbourToString(neighbourCount));
    }

    private void countNeighboursTest3() {
        byte[][] testBoard = {
                {1, 0, 0, 0, 1, 1, 0 ,0, 0, 1},
                {0, 1, 0, 0, 0, 1, 0 ,0, 0, 1},
                {0, 1, 1, 0, 0, 1, 0 ,0, 0, 1},
                {0, 0, 0, 0, 0, 1, 0 ,0, 0, 0},
                {1, 0, 1, 0, 0, 0, 0 ,1, 1, 0},
                {0, 1, 0, 1, 0, 1, 0 ,0, 1, 0},
                {0, 0, 1, 0, 0, 1, 0 ,0, 0, 1},
                {1, 0, 0, 1, 0, 1, 0 ,0, 0, 1},
                {1, 1, 0, 1, 0, 1, 0 ,0, 0, 0},
                {1, 0, 0, 0, 0, 0, 1 ,0, 0, 0}};


        String expectedOutput = "1322122232232433343313232434321212223211243233554223212122222333333331000223101123232332101212232110";

        board.setBoard(testBoard);
        byte[][]neighbourCount = board.countNeighbours();

        org.junit.Assert.assertEquals(expectedOutput, neighbourToString(neighbourCount));
    }


    private void resetBoardTest1() {
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 0},
                {1, 0, 0, 1, 0, 0}};
        board.setBoard(testBoard);
        board.resetBoard();
        String expectedOutput = "000000000000000000000000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    private void resetBoardTest2() {
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
        board.resetBoard();
        String expectedOutput = "00000000000000000000000000000000000000000000000000000000000000000000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    private void resetBoardTest3() {
        byte[][] testBoard = {
                {0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0},
                {0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0},
                {0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0},
                {0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0}};
        board.setBoard(testBoard);
        board.resetBoard();
        String expectedOutput = "00000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000";
        org.junit.Assert.assertEquals(expectedOutput, board.toString());
    }

    private String neighbourToString(byte[][] neighbour) {
        String str = "";
        for (int y = 0; y < neighbour[0].length; y++) {
            for (int x = 0; x < neighbour.length; x++) {
                str = str + neighbour[x][y];
            }
        }
        return str;
    }

}