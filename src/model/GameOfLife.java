package model;

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
    public String ruleName = "Life";
    public String bornRules = "3";
    public String surviveRules = "23";
    public String ruleDescription = "";

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



    public void setRuleString(String input)throws RulesFormatException{
        if(input == "Life" || input.equals("B3/S23")){
            ruleString = "B3/S23";
            ruleDescription = "Highly complex behavior.";
            ruleName = "Life";
        }else if(input == "Replicator" || input.equals("B1357/S1357")){
            ruleString = "B1357/S1357";
            ruleDescription = "Edward Fredkin's replicating automaton: every pattern is eventually replaced by multiple copies of itself.";
            ruleName = "Replicator";
        }else if(input == "Seeds" || input.equals("B2/S")){
            ruleString = "B2/S";
            ruleDescription = "All patterns are phoenixes, meaning that every live cell immediately dies, and many patterns lead to explosive chaotic growth. However, some engineered patterns with complex behavior are known.";
            ruleName = "Seeds";
        }else if(input == "Life Without Death" || input.equals("B3/S012345678")){
            ruleString = "B3/S012345678";
            ruleDescription = "Also known as Inkspot or Flakes. Cells that become alive never die. It combines chaotic growth with more structured ladder-like patterns that can be used to simulate arbitrary Boolean circuits.";
            ruleName = "Life Without Death";
        }else if(input == "34 Life" || input.equals("B3/S32")){
            ruleString = "B34/S34";
            ruleDescription = "Was initially thought to be a stable alternative to Life, until computer simulation found that larger patterns tend to explode. Has many small oscillators and spaceships.";
            ruleName = "34 Life";
        }else if(input == "Diamoeba" || input.equals("B34/S34")){
            ruleString = "B35678/S5678";
            ruleDescription = "Forms large diamonds with chaotically fluctuating boundaries. First studied by Dean Hickerson, who in 1993 offered a $50 prize to find a pattern that fills space with live cells; the prize was won in 1999 by David Bell.";
            ruleName = "Diamoeba";
        }else if(input == "2x2" || input.equals("B36/S125")){
            ruleString = "B36/S125";
            ruleDescription = "If a pattern is composed of 2x2 blocks, it will continue to evolve in the same form; grouping these blocks into larger powers of two leads to the same behavior, but slower. Has complex oscillators of high periods as well as a small glider.";
            ruleName = "2x2";
        }else if(input == "Highlife" || input.equals("B36/S23")){
            ruleString = "B36/S23";
            ruleDescription = "Similar to Life but with a small self-replicating pattern.";
            ruleName = "Highlife";
        }else if(input == "Day & Night" || input.equals("B3678/S34678")){
            ruleString = "B3678/S34678";
            ruleDescription = "Symmetric under on-off reversal. Has engineered patterns with highly complex behavior.";
            ruleName = "Day & Night";
        }else if(input == "Morley" || input.equals("B368/S245")){
            ruleString = "B368/S245";
            ruleDescription = "Named after Stephen Morley; also called Move. Supports very high-period and slow spaceships.";
            ruleName = "Morley";
        }else if(input == "Anneal" || input.equals("B4678/S35678")){
            ruleString = "B4678/S35678";
            ruleDescription = "Also called the twisted majority rule. Symmetric under on-off reversal. Approximates the curve-shortening flow on the boundaries between live and dead cells.";
            ruleName = "Anneal";
        } else{
            ruleString = input;
            ruleDescription = "No description";
            ruleName = "No name";
        }

        if(ruleString == ""){
            throw new  RulesFormatException();
        }else{
            setRuleSet(ruleString);
        }
    }

    public void setRuleSet(String rules) throws RulesFormatException{

        Pattern rulePattern = Pattern.compile("[^sSbB012345678/]",Pattern.MULTILINE | Pattern.DOTALL);
        Pattern formatPattern = Pattern.compile("^[bB][0-8]*/[sS][0-8]*$");

        //Pattern rulePattern = Pattern.compile("[sbSB]{1}[0-8]*/[sbSB]{1}[0-8]*",Pattern.MULTILINE | Pattern.DOTALL);
        Matcher ruleMatcher = rulePattern.matcher(rules);
        Matcher formatMatcher = formatPattern.matcher(rules);

        if(ruleMatcher.find() || !formatMatcher.find()){
            PopUpAlerts.ruleAlert1();
        }

        String[] bothRules = rules.split("[/]");
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



    /**
     * Compares the current board with the neighbour count and enforces the
     * rules of the game, creating a new nested array to be the next generation's board.
     * @see #newGenerationCells
     * @see #neighbourCount
     * @see StaticBoard#cellGrid
     */
    public void enforceRules() {

        //Creates a new byte[][] with the same dimensions as the current board.
        newGenerationCells = new byte[playBoard.cellGrid.length][playBoard.cellGrid[0].length];

        //Compares the current play board with the neighbour count.
        //Sets the values in newGenerationCells based on the rules of the Game of Life.
        for (int x = 0; x < playBoard.cellGrid.length; x++) {
            for (int y = 0; y < playBoard.cellGrid[0].length; y++) {
                String neighbours = ""+neighbourCount[x][y];


                //Checks if the current cell is alive
                if (playBoard.cellGrid[x][y] == 1) {

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
                else if (playBoard.cellGrid[x][y] == 0) {
                    if (bornRules.contains(neighbours)) {
                        newGenerationCells[x][y] = 1;
                        playBoard.cellsAlive++;
                    }
                }
            }
        }
    }

    public String getRuleString(){
        return ruleString;
    }

    @Override
    public Object clone(){
        GameOfLife golClone = new GameOfLife((StaticBoard)playBoard.clone());
        golClone.bornRules = bornRules;
        golClone.surviveRules = surviveRules;
        golClone.ruleString = ruleString;
        return golClone;
    }
}
