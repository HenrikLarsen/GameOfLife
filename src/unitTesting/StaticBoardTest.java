package unitTesting;

import model.Board;
import org.junit.Test;
import model.StaticBoard;

/**
 * JUnit class for unit testing of methods in the StaticBoard class.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class StaticBoardTest {
    private Board board;

    @Test
    public void countNeighboursTest1() {
        board = new StaticBoard(6,6);

        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 0},
                {1, 0, 0, 1, 0, 0}};
        board.setBoard(testBoard);
        byte[][]neighbourCount = board.countNeighbours();

        String expectedNeighbours = "112231113222244432123320123121001110";
        String actualNeighbours = array2DToString(neighbourCount);

        org.junit.Assert.assertEquals(expectedNeighbours, actualNeighbours);
    }

    @Test
    public void countNeighboursTest2() {
        board = new StaticBoard(8,8);
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 1, 0 ,0},
                {1, 1, 0, 0, 0, 1, 0 ,0},
                {0, 0, 1, 0, 0, 1, 0 ,0},
                {1, 0, 0, 0, 0, 0, 0 ,0},
                {0, 0, 0, 0, 0, 1, 1 ,0},
                {0, 0, 1, 0, 0, 0, 0 ,0},
                {0, 0, 1, 0, 1, 1, 0 ,0},
                {0, 1, 1, 0, 1, 0, 0 ,1}};
        board.setBoard(testBoard);
        byte[][]neighbourCount = board.countNeighbours();

        String expectedNeighbours = "2130101122422242121111320111135423221322121314232323132200011110";
        String actualNeighbours = array2DToString(neighbourCount);

        org.junit.Assert.assertEquals(expectedNeighbours, actualNeighbours);
    }

    @Test
    public void countNeighboursTest3() {
        board = new StaticBoard(10,10);
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
        board.setBoard(testBoard);
        byte[][] neighbourCount = board.countNeighbours();

        String expectedNeighbours = "1322122232232433343313232434321212223211243233554" +
                "223212122222333333331000223101123232332101212232110";
        String actualNeighbours = array2DToString(neighbourCount);

        org.junit.Assert.assertEquals(expectedNeighbours, actualNeighbours);
    }

    @Test
    public void resetBoardTest1() {
        board = new StaticBoard(6,6);
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 0},
                {1, 0, 0, 1, 0, 0}};
        board.setBoard(testBoard);
        board.resetBoard();

        String expectedPattern = "000000000000000000000000000000000000";
        String actualPattern = board.toString();
        int expectedCells = 0;
        int actualCells = board.getCellsAlive();

        org.junit.Assert.assertEquals(expectedPattern, actualPattern);
        org.junit.Assert.assertEquals(expectedCells, actualCells);
    }

    @Test
    public void resetBoardTest2() {
        board = new StaticBoard(10,8);
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

        String expectedPattern = "00000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String actualPattern = board.toString();
        int expectedCells = 0;
        int actualCells = board.getCellsAlive();

        org.junit.Assert.assertEquals(expectedPattern, actualPattern);
        org.junit.Assert.assertEquals(expectedCells, actualCells);
    }

    @Test
    public void resetBoardTest3() {
        board = new StaticBoard(16,19);
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
        String expectedPattern = "00000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000";
        String actualPattern = board.toString();
        int expectedCells = 0;
        int actualCells = board.getCellsAlive();

        org.junit.Assert.assertEquals(expectedPattern, actualPattern);
        org.junit.Assert.assertEquals(expectedCells, actualCells);
    }

    @Test
    public void setCellStateTest1() {
        board = new StaticBoard(10, 10);
        board.setCellState(5, 5, (byte)1);

        byte expected = 1;
        byte actual = board.getCellState(5,5);

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void setCellStateTest2() {
        board = new StaticBoard(10, 10);
        board.setCellState(11,11, (byte)1);
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void getCellStateNegativeTest1() {
        board = new StaticBoard(10, 10);

        byte expected = 1;
        byte actual = board.getCellState(10,10);

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void getCellStateNegativeTest2() {
        board = new StaticBoard(10, 10);
        board.setCellState(5, 5, (byte)1);

        byte expected = 1;
        byte actual = board.getCellState(10,5);

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void countCellsAliveTest1() {
        board = new StaticBoard(6,6);
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 0},
                {1, 0, 0, 1, 0, 0}};

        board.setBoard(testBoard);

        int expectedOutput = 9;
        int output = board.countCellsAlive();

        org.junit.Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void countCellsAliveTest2() {
        board = new StaticBoard(10,10);
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

        board.setBoard(testBoard);

        int expectedOutput = 33;
        int output = board.countCellsAlive();

        org.junit.Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void countCellsAliveTest3() {
        board = new StaticBoard(6,6);
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}};

        board.setBoard(testBoard);

        int expectedOutput = 0;
        int output = board.countCellsAlive();

        org.junit.Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void getSumXYCoordinatesTest1() {
        board = new StaticBoard(6,6);
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 0},
                {1, 0, 0, 1, 0, 0}};
        board.setBoard(testBoard);

        int expectedOutput = 44;
        int output = board.getSumXYCoordinates();

        org.junit.Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void getSumXYCoordinatesTest2() {
        board = new StaticBoard(10,8);
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

        int expectedOutput = 18;
        int output = board.getSumXYCoordinates();

        org.junit.Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void getBoundingBoxTest1() {
        board = new StaticBoard(8,8);
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

        int[] expectedOutput = {0, 2, 1, 3};
        int[] output = board.getBoundingBox();

        org.junit.Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void getBoundingBoxTest2() {
        board = new StaticBoard(10,10);
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        board.setBoard(testBoard);

        int[] expectedOutput = {3, 8, 1, 8};
        int[] output = board.getBoundingBox();

        org.junit.Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void trimTest1() {
        board = new StaticBoard(8,8);
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

        byte[][] expectedOutput = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1}};
        byte[][] output = board.trim();

        org.junit.Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void trimTest2() {
        board = new StaticBoard(10,10);
        byte[][] testBoard = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        board.setBoard(testBoard);

        byte[][] expectedOutput = {
                {0, 1, 0, 0 ,0, 0, 1, 0},
                {0, 0, 0, 0 ,0, 0, 0, 0},
                {0, 0, 0, 1 ,1, 1, 0, 0},
                {0, 0, 0 ,0, 0, 0, 0, 1},
                {0, 0, 0, 0 ,0, 0, 0, 0},
                {1, 0, 1, 0 ,0, 0, 1, 0}};
        byte[][] output = board.trim();

        org.junit.Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void setBoardFromRLETest1() {
        board = new StaticBoard(10,10);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1}};
        board.setBoardFromRLE(rleBoard);

        byte[][] actualPattern = board.getLoadedPattern();
        int[] expectedPatternBoundingBox = {3, 5, 3, 5};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();

        org.junit.Assert.assertArrayEquals(rleBoard, actualPattern);
        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
    }

    @Test
    public void setBoardFromRLETest2() {
        board = new StaticBoard(10,10);
        byte[][] rleBoard = {{1}};
        board.setBoardFromRLE(rleBoard);

        byte[][] actualPattern = board.getLoadedPattern();
        int[] expectedPatternBoundingBox = {4, 4, 4, 4};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();

        org.junit.Assert.assertArrayEquals(rleBoard, actualPattern);
        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
    }

    @Test
    public void setBoardFromRLETest3() {
        board = new StaticBoard(100,100);
        byte[][] rleBoard = {
                {0, 0, 1, 0, 1, 0, 0, 0, 1},
                {1, 0, 1, 1, 0, 1, 1, 0, 1},
                {0, 1, 1, 1, 0, 0, 0, 1, 0},
                {1, 0, 1, 1, 0, 1, 0, 1, 0},
                {0, 1, 1, 1, 0, 0, 0, 1, 0},
                {1, 0, 1, 1, 0, 1, 0, 1, 0},
                {0, 1, 1, 1, 0, 0, 0, 1, 0},
                {1, 0, 1, 1, 0, 1, 0, 1, 0},
                {0, 1, 1, 1, 0, 0, 0, 1, 0},
                {1, 0, 1, 1, 0, 1, 0, 1, 0},
                {0, 1, 1, 1, 0, 0, 0, 0, 1}};
        board.setBoardFromRLE(rleBoard);

        byte[][] actualPattern = board.getLoadedPattern();
        int[] expectedPatternBoundingBox = {44, 54, 45, 53};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();

        org.junit.Assert.assertArrayEquals(rleBoard, actualPattern);
        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
    }

    @Test
    public void movePatternTest1() {
        board = new StaticBoard(10,10);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        board.movePattern("up");

        int[] expectedPatternBoundingBox = {3, 5, 2, 4};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();

        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
    }

    @Test
    public void movePatternTest2() {
        board = new StaticBoard(10,10);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1},
                {1, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        board.movePattern("right");
        board.movePattern("up");
        board.movePattern("up");
        board.movePattern("left");
        board.movePattern("left");
        board.movePattern("down");

        int[] expectedPatternBoundingBox = {2, 5, 2, 4};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();

        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
    }

    @Test
    public void movePatternTest3() {
        board = new StaticBoard(10,10);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        for (int i = 0; i < 50; i++) {
            board.movePattern("up");
            board.movePattern("left");
        }

        int[] expectedPatternBoundingBox = {0, 2, 0, 2};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();

        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
    }

    @Test
    public void movePatternTest4() {
        board = new StaticBoard(10,10);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        for (int i = 0; i < 50; i++) {
            board.movePattern("down");
            board.movePattern("right");
        }

        int[] expectedPatternBoundingBox = {7, 9, 7, 9};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();

        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
    }

    @Test
    public void rotateTest1() {
        board = new StaticBoard(10,10);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        board.rotate(true);
        int[] expectedPatternBoundingBox = {3, 5, 3, 5};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();
        String expectedPattern = "100101110";
        String actualPattern = array2DToString(board.getLoadedPattern());

        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
        org.junit.Assert.assertEquals(expectedPattern, actualPattern);
    }

    @Test
    public void rotateTest2() {
        board = new StaticBoard(10,10);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        board.rotate(false);
        int[] expectedPatternBoundingBox = {4, 6, 1, 6};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();
        String expectedPattern = "111111111011101001";
        String actualPattern = array2DToString(board.getLoadedPattern());

        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
        org.junit.Assert.assertEquals(expectedPattern, actualPattern);
    }

    @Test
    public void rotateTest3() {
        board = new StaticBoard(10,10);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        board.rotate(true);
        for (int i = 0; i < 40; i++) {
            board.movePattern("left");
        }
        int[] expectedPatternBoundingBox = {0, 2, 1, 6};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();
        String expectedPattern = "100101110111111111";
        String actualPattern = array2DToString(board.getLoadedPattern());

        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
        org.junit.Assert.assertEquals(expectedPattern, actualPattern);
    }

    @Test
    public void rotateTest4() {
        board = new StaticBoard(10,10);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}};
        board.setBoardFromRLE(rleBoard);

        board.rotate(true);
        for (int i = 0; i < 40; i++) {
            board.movePattern("left");
        }
        board.rotate(true);

        int[] expectedPatternBoundingBox = {0, 2, 1, 6};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();
        String expectedPattern = "100101110111111111";
        String actualPattern = array2DToString(board.getLoadedPattern());

        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
        org.junit.Assert.assertEquals(expectedPattern, actualPattern);
    }

    @Test
    public void cloneTest() {
        board = new StaticBoard(10,10);

        Board shallowCopyBoard = board;
        org.junit.Assert.assertEquals(board, shallowCopyBoard);

        Board clonedBoard = (Board)board.clone();
        org.junit.Assert.assertFalse(clonedBoard.equals(board));

        clonedBoard.setCellState(3,3, (byte)1);
        org.junit.Assert.assertFalse(clonedBoard.getCellState(3,3) == board.getCellState(3,3));
    }

    private String array2DToString(byte[][] neighbour) {
        StringBuilder str = new StringBuilder();
        for (int y = 0; y < neighbour[0].length; y++) {
            for (byte[] aNeighbour : neighbour) {
                str.append(aNeighbour[y]);
            }
        }
        return str.toString();
    }
}