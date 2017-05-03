package unitTesting;

import model.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * JUnit class for unit testing of methods in the Statistics class.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class StatisticsTest {
    private Board board;
    private GameOfLife gol;
    private final Statistics stats = new Statistics();


    @Test
    public void getStatisticsTest1(){
        board = new DynamicBoard(10,10);
        gol = new GameOfLife(board);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        board.finalizeBoard();

        int[][] statistics = stats.getStatistics(gol, 5);

        int expectedCellsAlive = 5;
        int actualCellsAlive = statistics[0][1];

        int expectedCellDifference = 0;
        int actualCellDifference = statistics[1][1];

        // ExpectedSimilarity is calculated from
        // Math.min(firstReducedBoard, reducedBoard) / Math.max(firstReducedBoard, reducedBoard)*100
        int expectedSimilarity = 96;
        int actualSimilarity = statistics[2][1];

        org.junit.Assert.assertEquals(expectedCellsAlive, actualCellsAlive);
        org.junit.Assert.assertEquals(expectedCellDifference, actualCellDifference);
        org.junit.Assert.assertEquals(expectedSimilarity, actualSimilarity);
    }

    @Test
    public void getStatisticsTest2(){
        board = new DynamicBoard(10,10);
        gol = new GameOfLife(board);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        board.finalizeBoard();

        int[][] statistics = stats.getStatistics(gol, 5);

        int expectedCellsAlive = 5;
        int actualCellsAlive = statistics[0][3];

        int expectedCellDifference = 0;
        int actualCellDifference = statistics[1][3];

        // ExpectedSimilarity is calculated from
        // Math.min(firstReducedBoard, reducedBoard) / Math.max(firstReducedBoard, reducedBoard)*100
        int expectedSimilarity = 88;
        int actualSimilarity = statistics[2][3];

        org.junit.Assert.assertEquals(expectedCellsAlive, actualCellsAlive);
        org.junit.Assert.assertEquals(expectedCellDifference, actualCellDifference);
        org.junit.Assert.assertEquals(expectedSimilarity, actualSimilarity);
    }

    @Test
    public void getStatisticsTest3(){
        board = new DynamicBoard(10,10);
        gol = new GameOfLife(board);
        byte[][] rleBoard = {
                {0, 0, 1, 1, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 0},
                {1, 0, 1, 1, 0, 0, 0, 1},
                {0, 0, 0, 1, 0, 0, 0, 1},
                {0, 0, 0, 0, 1, 0, 1, 1},
                {1, 0, 0, 0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 1, 1, 0, 0, 0}};
        board.setBoardFromRLE(rleBoard);
        board.finalizeBoard();

        int[][] statistics = stats.getStatistics(gol, 7);

        int expectedCellsAlive = 13;
        int actualCellsAlive = statistics[0][5];

        int expectedCellDifference = -2;
        int actualCellDifference = statistics[1][5];

        // ExpectedSimilarity is calculated from
        // Math.min(firstReducedBoard, reducedBoard) / Math.max(firstReducedBoard, reducedBoard)*100
        int expectedSimilarity = 45;
        int actualSimilarity = statistics[2][5];

        org.junit.Assert.assertEquals(expectedCellsAlive, actualCellsAlive);
        org.junit.Assert.assertEquals(expectedCellDifference, actualCellDifference);
        org.junit.Assert.assertEquals(expectedSimilarity, actualSimilarity);
    }

    @Test
    public void getStatisticsTest4(){
        board = new DynamicBoard(10,10);
        gol = new GameOfLife(board);
        byte[][] rleBoard = {
                {0, 1, 1, 0, 0, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1},
                {0, 1, 0, 1, 0, 0, 0, 1, 0, 1},
                {0, 1, 1, 0, 0, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1}};
        board.setBoardFromRLE(rleBoard);
        board.finalizeBoard();

        int[][] statistics = stats.getStatistics(gol, 7);

        int expectedCellsAlive = 23;
        int actualCellsAlive = statistics[0][4];

        int expectedCellDifference = -1;
        int actualCellDifference = statistics[1][4];

        // ExpectedSimilarity is calculated from
        // Math.min(firstReducedBoard, reducedBoard) / Math.max(firstReducedBoard, reducedBoard)*100
        int expectedSimilarity = 39;
        int actualSimilarity = statistics[2][4];

        org.junit.Assert.assertEquals(expectedCellsAlive, actualCellsAlive);
        org.junit.Assert.assertEquals(expectedCellDifference, actualCellDifference);
        org.junit.Assert.assertEquals(expectedSimilarity, actualSimilarity);
    }

    @Test
    public void getStatisticsTest5(){
        board = new DynamicBoard(10,10);
        gol = new GameOfLife(board);
        byte[][] rleBoard = {
                {1, 1},
                {1, 1}};
        board.setBoardFromRLE(rleBoard);
        board.finalizeBoard();

        int[][] statistics = stats.getStatistics(gol, 100);

        int expectedCellsAlive = 4;
        int actualCellsAlive = statistics[0][99];

        int expectedCellDifference = 0;
        int actualCellDifference = statistics[1][99];

        int expectedSimilarity = 100;
        int actualSimilarity = statistics[2][99];

        org.junit.Assert.assertEquals(expectedCellsAlive, actualCellsAlive);
        org.junit.Assert.assertEquals(expectedCellDifference, actualCellDifference);
        org.junit.Assert.assertEquals(expectedSimilarity, actualSimilarity);
    }

    @Test
    public void getHighestSimilarityTest1() {
        board = new DynamicBoard(8,8);
        gol = new GameOfLife(board);
        FileHandler fileHandler = new FileHandler();
        fileHandler.setBoard(board);
        fileHandler.setGol(gol);

        File file = new File("src/resources/rlefiles/pennylane.rle");
        try{
            fileHandler.readGameBoardFromDisk(file);
        } catch (IOException ioe) {
            org.junit.Assert.fail();
        }
        board.finalizeBoard();

        int[][] statistics = stats.getStatistics(gol, 10);

        int expected = 4;
        int actual = stats.getHighestSimilarity(statistics);

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void getHighestSimilarityTest2() {
        board = new DynamicBoard(10, 10);
        gol = new GameOfLife(board);
        byte[][] rleBoard = {
                {0, 1, 1, 0, 0, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1},
                {0, 1, 0, 1, 0, 0, 0, 1, 0, 1},
                {0, 1, 1, 0, 0, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 1, 1, 0, 1}};
        board.setBoardFromRLE(rleBoard);
        board.finalizeBoard();

        int[][] statistics = stats.getStatistics(gol, 7);
        int expected = 0;
        int actual = stats.getHighestSimilarity(statistics);

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void getHighestSimilarityTest3() {
        board = new DynamicBoard(10, 10);
        gol = new GameOfLife(board);
        byte[][] rleBoard = {{1, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        board.finalizeBoard();

        int[][] statistics = stats.getStatistics(gol, 7);
        int expected = 2;
        int actual = stats.getHighestSimilarity(statistics);

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void statToStringTest1() {
        board = new DynamicBoard(10,10);
        gol = new GameOfLife(board);
        byte[][] rleBoard = {
                {0, 0, 1},
                {1, 0, 1},
                {0, 1, 1}};
        board.setBoardFromRLE(rleBoard);
        board.finalizeBoard();

        stats.getStatistics(gol, 5);

        String expectedOutput = "CellsAlive: 5 5 5 5 5 5\ncellsDiff:  0 0 0 0 0 0\n" +
                "similarityMeasure:  100 96 91 88 84 81";
        String actualOutput = stats.statToString();

        org.junit.Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void statToStringTest2() {
        board = new DynamicBoard(10,10);
        gol = new GameOfLife(board);
        byte[][] rleBoard = {
                {0, 0, 1, 1, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 0},
                {1, 0, 1, 1, 0, 0, 0, 1},
                {0, 0, 0, 1, 0, 0, 0, 1},
                {0, 0, 0, 0, 1, 0, 1, 1},
                {1, 0, 0, 0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 1, 1, 0, 0, 0}};
        board.setBoardFromRLE(rleBoard);
        board.finalizeBoard();

        stats.getStatistics(gol, 7);

        String expectedOutput = "CellsAlive: 24 30 21 17 15 13 14 11\ncellsDiff:  0 6 -9 -4 -2 -2 1 -3\n" +
                "similarityMeasure:  100 62 51 53 53 45 63 31";
        String actualOutput = stats.statToString();

        org.junit.Assert.assertEquals(expectedOutput, actualOutput);
    }
}