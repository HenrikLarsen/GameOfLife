package sample;

import javafx.scene.control.Alert;
import org.junit.Rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The GameOfLife class represents the logic behind this implementation of Conway's Game of Life. <br><br>
 * A GameOfLife object keeps track of the number of generations and decides the values of
 * the next generation board.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class GameOfLife {
    public int genCounter = 0;
    public StaticBoard playBoard;
    public byte[][] neighbourCount;
    public byte[][] newGenerationCells;
    public String ruleString = "B3/S23";
    public String bornRules = "3";
    public String surviveRules = "23";

    /**
     * Sole constructor, sets the parameter board as the current board.
     * @param board StaticBoard - The board to be played.
     */
    public GameOfLife(StaticBoard board) {
        this.playBoard = board;
    }

    /**
     * Sets the next generation of cells as the current play board.
     * Calls on Boards countNeighbours() and sets it as a nested byte array.
     * Calls on enforceRules() and finally sets the new generation as the current play board.
     * @see #enforceRules()
     * @see StaticBoard#setBoard(byte[][])
     * @see StaticBoard#countNeighbours()
     */
    public void nextGeneration() {
        playBoard.cellsAlive = 0;
        neighbourCount = playBoard.countNeighbours();
        enforceRules();
        playBoard.setBoard(newGenerationCells);
    }

    /**
     * Compares the current board with the neighbour count and enforces the
     * rules of the game, creating a new nested array to be the next generation's board.
     * @see #newGenerationCells
     * @see #neighbourCount
     * @see StaticBoard#boardGrid
     */
    /*public void enforceRules() {

        //Creates a new byte[][] with the same dimensions as the current board.
        newGenerationCells = new byte[playBoard.boardGrid.length][playBoard.boardGrid[0].length];

        //Compares the current play board with the neighbour count.
        //Sets the values in newGenerationCells based on the rules of the Game of Life.
        for (int x = 0; x < playBoard.boardGrid.length; x++) {
            for (int y = 0; y < playBoard.boardGrid[0].length; y++) {

                //Checks if the current cell is alive
                if (playBoard.boardGrid[x][y] == 1) {

                    //Checks if the live cell has less than two or more than three living neighbours.
                    //If yes, the cell dies.
                    if (neighbourCount[x][y] < 2 || neighbourCount[x][y] > 3) {
                        newGenerationCells[x][y] = 0;
                    }

                    //Checks if the live cell has exactly two or three living neighbours.
                    //If yes, the cell survives to the next generation.
                    else if (neighbourCount[x][y] == 2 || neighbourCount[x][y] == 3) {
                        newGenerationCells[x][y] = 1;
                        playBoard.cellsAlive++;
                    }
                }

                //If the current cell is dead and has exactly three living neighbours, it comes alive.
                else if (playBoard.boardGrid[x][y] == 0) {
                    if (neighbourCount[x][y] == 3) {
                        newGenerationCells[x][y] = 1;
                        playBoard.cellsAlive++;
                    }
                }
            }
        }
    }*/

    public void setRuleSet(String rules) throws RulesFormatException{
        System.out.println();

        Pattern rulePattern = Pattern.compile("[^sSbB012345678/]",Pattern.MULTILINE | Pattern.DOTALL);
        Pattern formatPattern = Pattern.compile("^[bB][0-8]*/[sS][0-8]*$");

        //Pattern rulePattern = Pattern.compile("[sbSB]{1}[0-8]*/[sbSB]{1}[0-8]*",Pattern.MULTILINE | Pattern.DOTALL);
        Matcher ruleMatcher = rulePattern.matcher(rules);
        Matcher formatMatcher = formatPattern.matcher(rules);

        if(ruleMatcher.find() || !formatMatcher.find()){
            throw new RulesFormatException();
        }

        String[] bothRules = rules.split("[/]");
        System.out.println(bothRules.length);
        for (int i = 0; i < bothRules.length; i++) {
            bothRules[i] = bothRules[i].toUpperCase();
            if (bothRules[i].startsWith("S")) {
                surviveRules = bothRules[i].replaceFirst("S", "");
            } else if (bothRules[i].startsWith("B")) {
                bornRules = bothRules[i].replaceFirst("B", "");
            }
        }
        ruleString = "B" + bornRules + "/S" + surviveRules;
    }

    public void enforceRules() {

        //Creates a new byte[][] with the same dimensions as the current board.
        newGenerationCells = new byte[playBoard.boardGrid.length][playBoard.boardGrid[0].length];

        //Compares the current play board with the neighbour count.
        //Sets the values in newGenerationCells based on the rules of the Game of Life.
        for (int x = 0; x < playBoard.boardGrid.length; x++) {
            for (int y = 0; y < playBoard.boardGrid[0].length; y++) {
                String neighbours = ""+neighbourCount[x][y];


                //Checks if the current cell is alive
                if (playBoard.boardGrid[x][y] == 1) {

                    //Checks if the live cell has less than two or more than three living neighbours.
                    //If yes, the cell dies.
                    if (surviveRules.contains(neighbours)) {
                        newGenerationCells[x][y] = 1;
                        playBoard.cellsAlive++;
                    } else {
                        newGenerationCells[x][y] = 0;
                    }
                }

                //If the current cell is dead and has exactly three living neighbours, it comes alive.
                else if (playBoard.boardGrid[x][y] == 0) {
                    if (bornRules.contains(neighbours)) {
                        newGenerationCells[x][y] = 1;
                        playBoard.cellsAlive++;
                    }
                }
            }
        }
    }
}
