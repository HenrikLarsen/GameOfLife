package test;

import org.junit.Test;
import sample.StaticBoard;

/**
 * The StaticBoardTest class is a JUnit class, doing unit testing of methods in the StaticBoard class.
 * Its main purpose is to test and verify that the functionality of the StaticBoard class works as intended
 * and to root out inherent errors in the logic of its methods.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class StaticBoardTest {
    private StaticBoard board = new StaticBoard();

    /**
     * Main test of StaticBoards countNeighbours() method. Contains several other methods that tests
     * that the method works as intended for specific configurations of a static game board.
     * @see StaticBoard#countNeighbours()
     * @see #countNeighboursTest1()
     * @see #countNeighboursTest2()
     * @see #countNeighboursTest3()
     * @throws Exception if an error occurs while testing.
     */
    @Test
    public void countNeighbours() throws Exception {
        countNeighboursTest1();
        countNeighboursTest2();
        countNeighboursTest3();
    }

    /**
     * Main test of StaticBoards resetBoard() method. Contains several other methods that tests
     * that the method works as intended for specific configurations of a static game board.
     * @see StaticBoard##resetBoardTest()
     * @see #resetBoardTest1()
     * @see #resetBoardTest2()
     * @see #resetBoardTest3()
     * @throws Exception if an error occurs while testing.
     */
    @Test
    public void resetBoard() throws Exception{
        resetBoardTest1();
        resetBoardTest2();
        resetBoardTest3();
    }

    /**
     * Does a test on the countNeighbours method of StaticBoard to see if a cells neighbours
     * are being correctly counted. <br>
     * Tests a 6/6 board configuration.
     * @see StaticBoard#countNeighbours()
     * @see StaticBoard#setBoard(byte[][])
     * @see #neighbourToString(byte[][])
     */
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

    /**
     * Does a test on the countNeighbours method of StaticBoard to see if a cells neighbours
     * are being correctly counted.<br>
     * Tests a 8/8 board configuration.
     * @see StaticBoard#countNeighbours()
     * @see StaticBoard#setBoard(byte[][])
     * @see #neighbourToString(byte[][])
     */
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

    /**
     * Does a test on the countNeighbours method of StaticBoard to see if a cells neighbours
     * are being correctly counted.<br>
     * Tests a 10/10 board configuration.
     * @see StaticBoard#countNeighbours()
     * @see StaticBoard#setBoard(byte[][])
     * @see #neighbourToString(byte[][])
     */
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

    /**
     * Does a test on the resetBoard method of StaticBoard to see if all cells goes back to zero
     * after the method has been called. <br>
     * Tests a 6/6 board configuration.
     * @see StaticBoard#resetBoard()
     * @see StaticBoard#setBoard(byte[][])
     * @see StaticBoard#toString()
     */
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

    /**
     * Does a test on the resetBoard method of StaticBoard to see if all cells goes back to zero
     * after the method has been called. <br>
     * Tests a 8/10 board configuration.
     * @see StaticBoard#resetBoard()
     * @see StaticBoard#setBoard(byte[][])
     * @see StaticBoard#toString()
     */
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

    /**
     * Does a test on the resetBoard method of StaticBoard to see if all cells goes back to zero
     * after the method has been called. <br>
     * Tests a 19/16 board configuration.
     * @see StaticBoard#resetBoard()
     * @see StaticBoard#setBoard(byte[][])
     * @see StaticBoard#toString()
     */
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

    /**
     * Converts a nested byte array to string by going through each element in the nested array
     * and placing each element after the last in a two-dimensional String.
     * @param neighbour byte[][] - The nested array to be converted to string.
     * @return String
     */
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