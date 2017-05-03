package unitTesting;

import model.Board;
import org.junit.Test;
import model.DynamicBoard;

/**
 * JUnit class for unit testing of methods in the DynamicBoard class.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class DynamicBoardTest {
    private Board board;

    @Test
    public void countNeighboursTest1() {
        board = new DynamicBoard(6,6);

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
        board = new DynamicBoard(8,8);
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
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(6,6);
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
        board = new DynamicBoard(10,8);
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
        board = new DynamicBoard(16,19);
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
        board = new DynamicBoard(10, 10);
        board.setCellState(5, 5, (byte)1);

        byte expected = 1;
        byte actual = board.getCellState(5,5);

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void setCellStateTest2() {
        board = new DynamicBoard(10, 10);
        board.setCellState(50,50, (byte)1);

        byte expected = 1;
        byte actual = board.getCellState(50,50);

        org.junit.Assert.assertEquals(expected, actual);
        org.junit.Assert.assertEquals(0, board.getCellState(49,49));
        org.junit.Assert.assertEquals(0, board.getCellState(51,51));
    }

    @Test
    public void expandBoardTest1() {
        board = new DynamicBoard(10, 10);
        board.setCellState(50,50, (byte)1);
        board.setCellState(0,1, (byte) 1);

        org.junit.Assert.assertEquals(51, board.getHeight());
        org.junit.Assert.assertEquals(51, board.getWidth());

        ((DynamicBoard)board).expandBoardDuringRunTime();

        org.junit.Assert.assertEquals(52, board.getHeight());
        org.junit.Assert.assertEquals(53, board.getWidth());
    }

    @Test
    public void expandBoardTest2() {
        board = new DynamicBoard(10, 10);
        board.setCellState(20,20, (byte)1);
        board.setCellState(0,1, (byte) 1);
        board.setCellState(15, 0, (byte)1);


        org.junit.Assert.assertEquals(21, board.getHeight());
        org.junit.Assert.assertEquals(21, board.getWidth());

        ((DynamicBoard)board).expandBoardDuringRunTime();

        org.junit.Assert.assertEquals(23, board.getHeight());
        org.junit.Assert.assertEquals(23, board.getWidth());
    }

    @Test
    public void expandBoardTest3() {
        board = new DynamicBoard(998, 998);

        org.junit.Assert.assertEquals(998, board.getHeight());
        org.junit.Assert.assertEquals(998, board.getWidth());

        board.setCellState(997,997, (byte)1);
        ((DynamicBoard)board).expandBoardDuringRunTime();

        org.junit.Assert.assertEquals(999, board.getHeight());
        org.junit.Assert.assertEquals(999, board.getWidth());
    }

    @Test
    public void expandBoardTest4() {
        board = new DynamicBoard(999, 999);
        board.setCellState(0,0, (byte)1);
        ((DynamicBoard)board).expandBoardDuringRunTime();

        org.junit.Assert.assertEquals(1000, board.getHeight());
        org.junit.Assert.assertEquals(1000, board.getWidth());
        org.junit.Assert.assertTrue(((DynamicBoard)board).getHasExpandedLeft());
        org.junit.Assert.assertTrue(((DynamicBoard)board).getHasExpandedUp());
    }

    @Test
    public void expandBoardTest5() {
        board = new DynamicBoard(1200, 1200);
        board.setCellState(0,0, (byte)1);
        ((DynamicBoard)board).expandBoardDuringRunTime();

        org.junit.Assert.assertEquals(1200, board.getHeight());
        org.junit.Assert.assertEquals(1200, board.getWidth());
        org.junit.Assert.assertFalse(((DynamicBoard)board).getHasExpandedLeft());
        org.junit.Assert.assertFalse(((DynamicBoard)board).getHasExpandedUp());
    }

    @Test
    public void getCellStateNegativeTest1() {
        board = new DynamicBoard(10, 10);

        byte expected = 0;
        byte actual = board.getCellState(10,10);


        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void getCellStateNegativeTest2() {
        board = new DynamicBoard(10, 10);

        byte expected = 0;
        byte actual = board.getCellState(10,5);

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void setGridSizeTest1() {
        board = new DynamicBoard(10, 10);

        ((DynamicBoard)board).setGridSize(20);

        org.junit.Assert.assertEquals(20, board.getWidth());
        org.junit.Assert.assertEquals(20, board.getHeight());
    }

    @Test
    public void setGridSizeTest2() {
        board = new DynamicBoard(10, 10);

        ((DynamicBoard)board).setGridSize(-2);

        org.junit.Assert.assertEquals(10, board.getWidth());
        org.junit.Assert.assertEquals(10, board.getHeight());
    }

    @Test
    public void expandWidthRightTest() {
        board = new DynamicBoard(10, 10);
        board.setCellState(9,9, (byte)1);

        ((DynamicBoard)board).expandWidthRight(4);

        org.junit.Assert.assertEquals(14, board.getWidth());
        org.junit.Assert.assertEquals(10, board.getHeight());
        org.junit.Assert.assertEquals(1, board.getCellState(9,9));
        org.junit.Assert.assertEquals(0, board.getCellState(11,9));
    }

    @Test
    public void expandWidthLeftTest() {
        board = new DynamicBoard(10, 10);
        board.setCellState(9,9, (byte)1);

        ((DynamicBoard)board).expandWidthLeft(4);

        org.junit.Assert.assertEquals(14, board.getWidth());
        org.junit.Assert.assertEquals(10, board.getHeight());
        org.junit.Assert.assertEquals(1, board.getCellState(13,9));
        org.junit.Assert.assertEquals(0, board.getCellState(11,9));
    }

    @Test
    public void expandHeightDown() {
        board = new DynamicBoard(10, 10);
        board.setCellState(9,9, (byte)1);

        ((DynamicBoard)board).expandHeightDown(4);

        org.junit.Assert.assertEquals(10, board.getWidth());
        org.junit.Assert.assertEquals(14, board.getHeight());
        org.junit.Assert.assertEquals(1, board.getCellState(9,9));
        org.junit.Assert.assertEquals(0, board.getCellState(9,12));
    }

    @Test
    public void expandHeightUp() {
        board = new DynamicBoard(10, 10);
        board.setCellState(9,9, (byte)1);

        ((DynamicBoard)board).expandHeightUp(4);

        org.junit.Assert.assertEquals(10, board.getWidth());
        org.junit.Assert.assertEquals(14, board.getHeight());
        org.junit.Assert.assertEquals(1, board.getCellState(9,13));
        org.junit.Assert.assertEquals(0, board.getCellState(9,12));
    }

    @Test
    public void countCellsAliveTest1() {
        board = new DynamicBoard(6,6);
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
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(6,6);
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
        board = new DynamicBoard(6,6);
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
        board = new DynamicBoard(10,8);
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
        board = new DynamicBoard(8,8);
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
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(8,8);
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
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(100,100);
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
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(10,10);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        for (int i = 0; i < 50; i++) {
            board.movePattern("down");
            board.movePattern("right");
        }

        int[] expectedPatternBoundingBox = {53, 55, 53, 55};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();

        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
    }

    @Test
    public void rotateTest1() {
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(10,10);
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
        board = new DynamicBoard(10,10);
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

        int[] expectedPatternBoundingBox = {0, 5, 3, 5};
        int[] actualPatternBoundingBox = board.getLoadedPatternBoundingBox();
        String expectedPattern = "111111111100111010";
        String actualPattern = array2DToString(board.getLoadedPattern());

        org.junit.Assert.assertArrayEquals(expectedPatternBoundingBox, actualPatternBoundingBox);
        org.junit.Assert.assertEquals(expectedPattern, actualPattern);
    }

    @Test
    public void setOutOfBoundsTest1() {
        board = new DynamicBoard(1000, 1000);

        int actual = ((DynamicBoard)board).calculateOutOfBounds(1005, board.getHeight());
        int expected = 6;

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void setOutOfBoundsTest2() {
        board = new DynamicBoard(1000, 1000);

        int actual = ((DynamicBoard)board).calculateOutOfBounds(-80, board.getHeight());
        int expected = 80;

        org.junit.Assert.assertEquals(expected, actual);
    }
    @Test

    public void setOutOfBoundsTest3() {
        board = new DynamicBoard(1000, 1000);

        int actual = ((DynamicBoard)board).calculateOutOfBounds(150, board.getWidth());
        int expected = 0;

        org.junit.Assert.assertEquals(expected, actual);
    }


    @Test
    public void cloneTest() {
        board = new DynamicBoard(10,10);

        Board shallowCopyBoard = board;
        org.junit.Assert.assertEquals(board, shallowCopyBoard);

        Board clonedBoard = (Board)board.clone();
        org.junit.Assert.assertFalse(clonedBoard.equals(board));

        clonedBoard.setCellState(3,3, (byte)1);
        org.junit.Assert.assertFalse(clonedBoard.getCellState(3,3) == board.getCellState(3,3));

        board.setCellState(15,15, (byte)1);
        org.junit.Assert.assertFalse(board.getHeight() == clonedBoard.getHeight());

        org.junit.Assert.assertFalse(clonedBoard.getCellState(15,15) ==
                board.getCellState(15,15));
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