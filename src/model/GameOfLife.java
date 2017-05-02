package model;

import controller.PopUpAlerts;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The GameOfLife class represents the logic behind this implementation of Conway's Game of Life. <br><br>
 * A GameOfLife object keeps track of the number of generations, the current playing rules and decides the values of
 * the next generations cell grid.
 *
 * @author Oscar Vladau-Husevold
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class GameOfLife {
    private int genCounter = 0;
    private final Board playBoard;
    private ThreadWorker workers;
    private byte[][] neighbourCount;
    private byte[][] newGenerationCells;

    //Callable objects containing the tasks needed for each generation. Used when running concurrently.
    private Callable<Void> countNeighboursCallable;
    private Callable<Void> enforceAndSetCallable;

    //The number of rows each thread should operate on when running concurrently.
    private int rowsPerWorker;

    //Data fields related to the current rules.
    private String ruleString = "B3/S23";
    private String bornRules = "3";
    private String surviveRules = "23";
    private String ruleName = "Life";
    private String ruleDescription = "";

    /**
     * Sole constructor, sets the parameter board as the current board.
     * @param board - The board to be used.
     */
    public GameOfLife(Board board) {
        this.playBoard = board;
    }

    /**
     * Sets the next generation of cells as the current play board.
     * Calls on Boards countNeighbours() and sets it as a 2D-array.
     * If the Board is an instance of DynamicBoard it checks if it needs to expand, and expands if yes.
     * Calls on enforceRules() and finally sets the new generation as the current play board.
     * @see #enforceRules()
     * @see #neighbourCount
     * @see #newGenerationCells
     * @see Board#resetCellsAlive()
     * @see Board#setBoard(byte[][])
     * @see Board#countNeighbours()
     * @see DynamicBoard#expandBoardDuringRunTime()
     */
    public void nextGeneration() {
        playBoard.resetCellsAlive();

        //Checks for expand and expands if necessary
        if (playBoard instanceof DynamicBoard) {
            ((DynamicBoard)playBoard).expandBoardDuringRunTime();
        }

        //Does the three main tasks of each generation: Counts neighbours, compares them to the rules and sets board.
        neighbourCount = playBoard.countNeighbours();
        enforceRules();
        playBoard.setBoard(newGenerationCells);
    }

    /**
     * Sets the next generation of cells as the current play board concurrently. If the Board is an instance of
     * DynamicBoard it checks if it needs to expand, and expands if yes. Calculates the number of rows
     * each thread should consider, and calls generateCallables() to update the Callable objects for this generation.
     * Because of how Board's countNeighbours method works, it needs to make sure that the neighbours have been
     * counted before enforcing the rules and setting the new board. Therefore there are two calls to ThreadWorker'
     * runWorkers() method, which will execute all threads and wait for them to finish.
     * @see #newGenerationCells
     * @see #rowsPerWorker
     * @see #neighbourCount
     * @see #generateCallables()
     * @see ThreadWorker#runWorkers(Callable)
     * @see Board#resetCellsAlive()
     * @see DynamicBoard#expandBoardDuringRunTime()
     */
    public void nextGenerationConcurrent() {
        playBoard.resetCellsAlive();
        if (playBoard instanceof DynamicBoard) {
            ((DynamicBoard)playBoard).expandBoardDuringRunTime();
        }

        //Creates two new 2D-Arrays the size of the current cellGrid.
        newGenerationCells = new byte[playBoard.getWidth()][playBoard.getHeight()];
        neighbourCount = new byte[playBoard.getWidth()][playBoard.getHeight()];

        //Calculates how many rows each thread will operate on.
        rowsPerWorker = (int)Math.ceil((double)playBoard.getWidth()/(double) workers.getNumWorkers());

        //Updates the Callable objects for this generation
        generateCallables();

        //Runs both Callable objects and waits till all threads are done before continuing.
        workers.runWorkers(countNeighboursCallable);
        workers.runWorkers(enforceAndSetCallable);
    }

    /**
     * Method for printing out the time it takes for a call to nextGeneration() to finish.
     * Used for verification and testing purposes, but not within the finished application.
     * @see #nextGeneration()
     */
    public void nextGenerationPrintPerformance() {
        long start = System.currentTimeMillis();
        nextGeneration();
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Counting time (ms): " + elapsed);
    }

    /**
     * Method for printing out the time it takes for a call to nextGenerationConcurrent() to finish.
     * Used for verification and testing purposes, but not within the finished application.
     * @see #nextGenerationConcurrent()
     */
    public void nextGenerationConcurrentPrintPerformance() {
        long start = System.currentTimeMillis();
        nextGenerationConcurrent();
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Counting time (ms): " + elapsed);
    }


    /**
     * Method for generating the Callable tasks the ThreadWorker class needs to perform. Is updated every generation
     * so that the rowsPerWorker is correct relative to the current cell grid. Because of how countNeighbours() in
     * Board is implemented, there are two separate Callable objects. This is because countNeighbours()
     * needs to be complete before the rules are enforced and the new board is set.
     * @see #countNeighboursCallable
     * @see #enforceAndSetCallable
     * @see #neighbourCount
     * @see #rowsPerWorker
     * @see ThreadWorker#getThreadIndex()
     * @see Board#countNeighboursConcurrent(byte[][], int, int)
     * @see Board#setBoardConcurrent(byte[][], int, int)
     */
    private void generateCallables() {
        countNeighboursCallable = ()-> {
            int index = workers.getThreadIndex();
            neighbourCount = playBoard.countNeighboursConcurrent(neighbourCount, index, rowsPerWorker);
            return null;
        };

        enforceAndSetCallable = ()-> {
            int index = workers.getThreadIndex();
            enforceRulesConcurrent(index);
            playBoard.setBoardConcurrent(newGenerationCells, index, rowsPerWorker);
            return null;
        };
    }


    /**
     * A method for enforcing the rules of the game. Iterates throughout the entire cell grid and
     * calls updateNewGenerationCells() to enforce the rules of the game.
     * @see #newGenerationCells
     * @see Board#getWidth()
     * @see Board#getHeight()
     */
    public void enforceRules() {
        //Creates a new byte[][] with the same dimensions as the current board.
        newGenerationCells = new byte[playBoard.getWidth()][playBoard.getHeight()];

        for (int x = 0; x < playBoard.getWidth(); x++) {
            for (int y = 0; y < playBoard.getHeight(); y++) {
                updateNewGenerationCells(x, y);
            }
        }
    }

    /**
     * A method for concurrently enforcing the rules of the game. Iterates through a portion of the current cell grid
     * based on the current cells index and rowsPerWorker, and calls updateNewGenerationCells() to enforce the
     * rules of the game.
     * @param curIndex - The current thread's index.
     * @see #rowsPerWorker
     * @see Board#getWidth()
     * @see Board#getHeight()
     */
    private void enforceRulesConcurrent(int curIndex) {
        for (int x = rowsPerWorker*curIndex; x < (curIndex+1)*rowsPerWorker && x < playBoard.getWidth(); x++) {
            for (int y = 0; y < playBoard.getHeight(); y++) {
                updateNewGenerationCells(x,y);
            }
        }
    }

    /**
     * Compares the current cell with the neighbour count up against the current rules, and enforces the
     * rules of the game, setting the cell state of that cell in the newGenerationCells 2D-array.
     * @param x - The x-coordinate of the current cell.
     * @param y - The y-coordinate of the current cell.
     * @see #newGenerationCells
     * @see #neighbourCount
     * @see #bornRules
     * @see #surviveRules
     * @see Board#getCellState(int, int)
     * @see Board#increaseCellsAlive()
     */
    private void updateNewGenerationCells(int x, int y) {
        //Creates a string containing the number of neighbours for the current cell.
        String neighbours = ""+neighbourCount[x][y];

        //Checks if the current cell is alive
        if (playBoard.getCellState(x, y) == 1) {

            //Checks if the surviveRules contain the number of neighbours. If yes, the cell survives.
            if (surviveRules.contains(neighbours)) {
                newGenerationCells[x][y] = 1;
                playBoard.increaseCellsAlive();
            } else {
                newGenerationCells[x][y] = 0;
            }
        }

        //Else it checks if the bornRules contain the number of neighbours. If yes, the cell is born.
        else if (playBoard.getCellState(x,y) == 0) {
            if (bornRules.contains(neighbours)) {
                newGenerationCells[x][y] = 1;
                playBoard.increaseCellsAlive();
            }
        }
    }

    /**
     * Method to set the rules from a number of presets. Checks what the user has selected, and sets the ruleString
     * accordingly. Does a call to setRuleSet at the end to update the rules.
     * @param input - The string containing either the name of the rule, or the rule formatted after RLE standards.
     * @see #ruleString
     * @see #ruleDescription
     * @see #ruleName
     * @see #setRuleSet(String)
     * @exception RulesFormatException - Thrown if the rules are formatted wrong.
     */
    public void setRuleString(String input) throws RulesFormatException{
        if(input != null){
            switch (input) {
                case "Life":
                case "B3/S23":
                    ruleString = "B3/S23";
                    ruleDescription = "The original rules for Conway's Game of Life. A chaotic rule that is by " +
                            "far the most well-known and well-studied. It exhibits highly complex behavior.";
                    ruleName = "Life";
                    break;
                case "Replicator":
                case "B1357/S1357":
                    ruleString = "B1357/S1357";
                    ruleDescription = "Edward Fredkin's replicating automaton: every pattern is eventually " +
                            "replaced by multiple copies of itself.";
                    ruleName = "Replicator";
                    break;
                case "Seeds":
                case "B2/S":
                    ruleString = "B2/S";
                    ruleDescription = "All patterns are phoenixes, meaning that every live cell immediately " +
                            "dies, and many patterns lead to explosive chaotic growth. However, some engineered " +
                            "patterns with complex behavior are known.";
                    ruleName = "Seeds";
                    break;
                case "Life Without Death":
                case "B3/S012345678":
                    ruleString = "B3/S012345678";
                    ruleDescription = "Also known as Inkspot or Flakes. Cells that become alive never die. " +
                            "It combines chaotic growth with more structured ladder-like patterns that can be " +
                            "used to simulate arbitrary Boolean circuits.";
                    ruleName = "Life Without Death";
                    break;
                case "34 Life":
                case "B34/S34":
                    ruleString = "B34/S34";
                    ruleDescription = "Was initially thought to be a stable alternative to Life, until " +
                            "computer simulation found that larger patterns tend to explode. Has many small " +
                            "oscillators and spaceships.";
                    ruleName = "34 Life";
                    break;
                case "Diamoeba":
                case "B35678/S5678":
                    ruleString = "B35678/S5678";
                    ruleDescription = "Forms large diamonds with chaotically fluctuating boundaries. First " +
                            "studied by Dean Hickerson, who in 1993 offered a $50 prize to find a pattern that " +
                            "fills space with live cells; the prize was won in 1999 by David Bell.";
                    ruleName = "Diamoeba";
                    break;
                case "2x2":
                case "B36/S125":
                    ruleString = "B36/S125";
                    ruleDescription = "If a pattern is composed of 2x2 blocks, it will continue to evolve " +
                            "in the same form; grouping these blocks into larger powers of two leads to the " +
                            "same behavior, but slower. Has complex oscillators of high periods as well as " +
                            "a small glider.";
                    ruleName = "2x2";
                    break;
                case "Highlife":
                case "B36/S23":
                    ruleString = "B36/S23";
                    ruleDescription = "Similar to Life but with a small self-replicating pattern.";
                    ruleName = "Highlife";
                    break;
                case "Day & Night":
                case "B3678/S34678":
                    ruleString = "B3678/S34678";
                    ruleDescription = "Symmetric under on-off reversal. Has engineered patterns with highly " +
                            "complex behavior.";
                    ruleName = "Day & Night";
                    break;
                case "Morley":
                case "B368/S245":
                    ruleString = "B368/S245";
                    ruleDescription = "Named after Stephen Morley; also called Move. Supports very high-period and " +
                            "slow spaceships.";
                    ruleName = "Morley";
                    break;
                case "Anneal":
                case "B4678/S35678":
                    ruleString = "B4678/S35678";
                    ruleDescription = "Also called the twisted majority rule. Symmetric under on-off reversal. " +
                            "Approximates the curve-shortening flow on the boundaries between live and dead cells.";
                    ruleName = "Anneal";
                    break;
                default:
                    //If input does not match any of the above cases, sets this as the default values
                    ruleString = input;
                    ruleDescription = "No description";
                    ruleName = "No name";
                    break;
            }
        }

        //Calls setRuleSet with the current ruleString if it is not empty.
        if(ruleString.equals("")){
            throw new  RulesFormatException();
        }else{
            setRuleSet(ruleString);
        }
    }


    /**
     * Method to set the rules from a string. Does a check for the right formatting and sets the ruleString,
     * surviveRules and bornRules if the string contains valid information. If not, it shows a popup explaining
     * to the user that the rules are formatted wrongly, and sets the rules to the standard Life rules.
     * @param rules - The string containing the rules formatted in the RLE-style (Bxxx/Sxxx).
     * @see #ruleString
     * @see #bornRules
     * @see #surviveRules
     * @see PopUpAlerts#ruleAlert1()
     */
    public void setRuleSet(String rules) {

        //Creates patterns and matcher to check for correct formatting of the rules.
        Pattern rulePattern = Pattern.compile("[^sSbB012345678/]",Pattern.MULTILINE | Pattern.DOTALL);
        Pattern formatPattern = Pattern.compile("^[bB][0-8]*/[sS][0-8]*$");

        Matcher ruleMatcher = rulePattern.matcher(rules);
        Matcher formatMatcher = formatPattern.matcher(rules);

        //Shows a popup to the user if the rules are wrongly formatted, and sets rules to default.
        if(ruleMatcher.find() || !formatMatcher.find()){
            PopUpAlerts.ruleAlert1();
            rules = "B3/S23";
        }

        //Splits the string into two parts
        String[] bothRules = rules.split("[/]");
        String surviveRulesTemp = "";
        String bornRulesTemp = "";

        //Removes the S and B from the rules and sets them in their own string.
        for (int i = 0; i < bothRules.length; i++) {
            bothRules[i] = bothRules[i].toUpperCase();
            if (bothRules[i].startsWith("S")) {
                surviveRulesTemp = bothRules[i].replaceFirst("S", "");
            } else if (bothRules[i].startsWith("B")) {
                bornRulesTemp = bothRules[i].replaceFirst("B", "");
            }
        }

        //Removes duplicate numbers and puts them in ascending order.
        StringBuilder surviveBuilder = new StringBuilder();
        StringBuilder bornBuilder = new StringBuilder();
        for (int i = 0; i < 9; i ++) {
            if (surviveRulesTemp.contains(""+i)) {
                surviveBuilder.append(i);
            }
            if (bornRulesTemp.contains(""+i)) {
                bornBuilder.append(i);
            }
        }

        //Sets the surviveRules, bornRules and ruleString.
        surviveRules = surviveBuilder.toString();
        bornRules = bornBuilder.toString();
        ruleString = "B" + bornRules + "/S" + surviveRules;
    }

    /**
     * Method that sets the current ThreadWorker.
     * @param tw - The ThreadWorker object to be set.
     * @see #workers
     */
    public void setThreadWorkers(ThreadWorker tw){
        this.workers = tw;
    }

    /**
     * Method that returns the current ruleString.
     * @return ruleString - The current full rule String
     * @see #ruleString
     */
    public String getRuleString(){
        return ruleString;
    }

    /**
     * Method that returns the current ruleName.
     * @return ruleName - The current rule name.
     * @see #ruleName
     */
    public String getRuleName(){
        return ruleName;
    }

    /**
     * Method that returns the current ruleDescription.
     * @return ruleDescription - The current rule description.
     * @see #ruleDescription
     */
    public String getRuleDescription(){
        return ruleDescription;
    }

    /**
     * Method that returns the current bornRules.
     * @return bornRules - The current rule for cell birth.
     * @see #bornRules
     */
    public String getBornRules() {
        return bornRules;
    }

    /**
     * Method that returns the current surviveRules.
     * @return surviveRules - The current rule for cell survival.
     * @see #surviveRules
     */
    public String getSurviveRules() {
        return surviveRules;
    }

    /**
     * Method that returns the current playBoard.
     * @return playBoard - The current board that is linked to this GameOfLife.
     * @see #playBoard
     */
    public Board getPlayBoard() {
        return playBoard;
    }

    /**
     * Method that returns the current genCounter.
     * @return genCounter - The current number of iterations that have been run.
     * @see #genCounter
     */
    public int getGenCounter () {
        return genCounter;
    }

    /**
     * Method that adds 1 to the generation counter.
     * @see #genCounter
     */
    public void incrementGenCounter() {
        genCounter++;
    }

    /**
     * Method sets the generation counter back to 0.
     * @see #genCounter
     */
    public void resetGenCounter() {
        genCounter = 0;
    }

    /**
     * Method that sets the neighbour count of this board. This method is only used during unit testing.
     * @param neighbours - The 2D-byte array to be set as the neighbours.
     * @see #neighbourCount
     */
    public void setNeighbourCount(byte[][] neighbours) {
        this.neighbourCount = neighbours;
    }

    /**
     * Method that returns the current newGenerationCells 2D-byte-array. This method is only used during unit testing.
     * @return newGenerationCells - The next generation of cells calculated by enforce rules.
     * @see #newGenerationCells
     */
    public byte[][] getNewGenerationCells() {
        return newGenerationCells;
    }

    /**
     * A method that does a deep copy of the current GameOfLife and
     * returns it. Overrides the clone method in the Object class.
     * @return staticBoardClone - The deep copy of the board.
     * @see #bornRules
     * @see #surviveRules
     * @see #ruleString
     * @see #ruleName
     * @see #ruleDescription
     * @see Object#clone()
     */
    @Override
    public Object clone(){
        GameOfLife golClone = new GameOfLife((Board)playBoard.clone());
        golClone.bornRules = bornRules;
        golClone.surviveRules = surviveRules;
        golClone.ruleString = ruleString;
        golClone.ruleName = ruleName;
        golClone.ruleDescription = ruleDescription;
        golClone.workers = workers;
        return golClone;
    }
}