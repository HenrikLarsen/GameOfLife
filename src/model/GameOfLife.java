package model;

import controller.PopUpAlerts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    public int genCounter = 0;
    public Board playBoard;
    public byte[][] neighbourCount;
    public byte[][] newGenerationCells;

    //Data fields related to concurrency
    private Runnable countNeighboursTask;
    private Runnable enforceRulesTask;
    private Runnable setBoardTask;
    private int threadIndex = 0;
    private int rowsPerWorker;

    //The number of threads to be used. Equal to the system's available processors times two, because we assume the CPU
    //supports hyper-threading
    private int numWorkers = Runtime.getRuntime().availableProcessors()*2;

    //Data fields related to the current rules.
    private String ruleString = "B3/S23";
    private String bornRules = "3";
    private String surviveRules = "23";
    private String ruleName = "Life";
    private String ruleDescription = "";

    /**
     * Sole constructor, sets the parameter board as the current board.
     * @param board - The board to be played.
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
     * Sets the next generation of cells as the current play board concurrently.
     * Creates several threads to do each task concurrently, by calling generateTasks() and createWorkerList() for
     * each task. Between each task it waits for all active threads to finish their task before moving on to the next.
     * Calls on Boards countNeighbours() and sets it as a 2D-array.
     * If the Board is an instance of DynamicBoard it checks if it needs to expand, and expands if yes.
     * Calls on enforceRules() and finally sets the new generation as the current play board.
     * @see #newGenerationCells
     * @see #rowsPerWorker
     * @see #neighbourCount
     * @see #generateTasks()
     * @see #waitForThreads(List)
     * @see #createWorkerList(Runnable)
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
        rowsPerWorker = (int)Math.ceil((double)playBoard.getWidth()/(double)numWorkers);

        generateTasks();

        //Creates the list of threads, starts each thread and waits for all to finnish before moving on.
        List<Thread> neighbourWorkers = createWorkerList(countNeighboursTask);
        neighbourWorkers.forEach(Thread::start);
        waitForThreads(neighbourWorkers);

        List<Thread> enforceWorkers = createWorkerList(enforceRulesTask);
        enforceWorkers.forEach(Thread::start);
        waitForThreads(enforceWorkers);

        List<Thread> setBoardWorkers = createWorkerList(setBoardTask);
        setBoardWorkers.forEach(Thread::start);
        waitForThreads(setBoardWorkers);
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
     * Method that returns the current threadIndex and adds 1 to it. If it is greater or equal to the number
     * of active threads, it resets to 0. The method is synchronized to avoid a race condition.
     * @return threadIndex - The index of the current thread relative to the rest.
     * @see #threadIndex
     * @see #numWorkers
     */
    public synchronized int getThreadIndex() {
        if (threadIndex >= numWorkers) threadIndex=0;
        return threadIndex++;
    }

    /**
     * Method for making sure that all active threads related to a task is finished. Important because every
     * task needs to be completed before the next task is initialised. Iterates through the list of threads and
     * calls Threads join() method before moving on. If an InterruptedException is thrown, it will interrupt that
     * thread and print a message to the console.
     * @param workerList - The list of threads to wait on.
     */
    public void waitForThreads(List<Thread> workerList) {
        for (Thread t : workerList) {
            try {
                t.join();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.out.println("Error waiting for thread");
            }
        }
    }

    /**
     * Method for generating the runnable tasks the threads needs to perform. Is updated every generation
     * so that the rowsPerWorker is correct relative to the current cell grid. Creates three Runnable objects.
     * @see #countNeighboursTask
     * @see #enforceRulesTask
     * @see #setBoardTask
     * @see #neighbourCount
     * @see #rowsPerWorker
     * @see #getThreadIndex()
     * @see Board#countNeighboursConcurrent(byte[][], int, int)
     * @see Board#setBoardConcurrent(byte[][], int, int)
     */
    private void generateTasks() {
        countNeighboursTask = ()-> {
            int index = getThreadIndex();
            neighbourCount = playBoard.countNeighboursConcurrent(neighbourCount, index, rowsPerWorker);
        };

        enforceRulesTask = ()-> {
            int index = getThreadIndex();
            enforceRulesConcurrent(index);
        };

        setBoardTask = ()-> {
            int index = getThreadIndex();
            playBoard.setBoardConcurrent(newGenerationCells, index, rowsPerWorker);
        };
    }

    /**
     * Method for generating a List of threads the size of numWorkers. The parameter task is a Runnable Object
     * that each Thread in that list will perform.
     * @param task - The Runnable object that each Thread in the list needs to perform.
     * @return list - The list of threads.
     * @see #numWorkers
     */
    private List<Thread> createWorkerList(Runnable task) {
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < numWorkers; i++) {
            Thread t = new Thread(task);
            list.add(t);
        }
        return list;
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
    public void enforceRulesConcurrent(int curIndex) {
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
            if(input == "Life" || input.equals("B3/S23")){
                ruleString = "B3/S23";
                ruleDescription = "Highly complex behavior.";
                ruleName = "Life";
            }else if(input == "Replicator" || input.equals("B1357/S1357")){
                ruleString = "B1357/S1357";
                ruleDescription = "Edward Fredkin's replicating automaton: every pattern is eventually replaced by " +
                        "multiple copies of itself.";
                ruleName = "Replicator";
            }else if(input == "Seeds" || input.equals("B2/S")){
                ruleString = "B2/S";
                ruleDescription = "All patterns are phoenixes, meaning that every live cell immediately dies, and " +
                        "many patterns lead to explosive chaotic growth. However, some engineered patterns with " +
                        "complex behavior are known.";
                ruleName = "Seeds";
            }else if(input == "Life Without Death" || input.equals("B3/S012345678")){
                ruleString = "B3/S012345678";
                ruleDescription = "Also known as Inkspot or Flakes. Cells that become alive never die. It combines " +
                        "chaotic growth with more structured ladder-like patterns that can be used to simulate " +
                        "arbitrary Boolean circuits.";
                ruleName = "Life Without Death";
            }else if(input == "34 Life" || input.equals("B34/S34")){
                ruleString = "B34/S34";
                ruleDescription = "Was initially thought to be a stable alternative to Life, until computer simulation " +
                        "found that larger patterns tend to explode. Has many small oscillators and spaceships.";
                ruleName = "34 Life";
            }else if(input == "Diamoeba" || input.equals("B35678/S5678")){
                ruleString = "B35678/S5678";
                ruleDescription = "Forms large diamonds with chaotically fluctuating boundaries. First studied by Dean " +
                        "Hickerson, who in 1993 offered a $50 prize to find a pattern that fills space with live cells; " +
                        "the prize was won in 1999 by David Bell.";
                ruleName = "Diamoeba";
            }else if(input == "2x2" || input.equals("B36/S125")){
                ruleString = "B36/S125";
                ruleDescription = "If a pattern is composed of 2x2 blocks, it will continue to evolve in the same form; " +
                        "grouping these blocks into larger powers of two leads to the same behavior, but slower. Has " +
                        "complex oscillators of high periods as well as a small glider.";
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
                ruleDescription = "Named after Stephen Morley; also called Move. Supports very high-period and " +
                        "slow spaceships.";
                ruleName = "Morley";
            }else if(input == "Anneal" || input.equals("B4678/S35678")){
                ruleString = "B4678/S35678";
                ruleDescription = "Also called the twisted majority rule. Symmetric under on-off reversal. Approximates " +
                        "the curve-shortening flow on the boundaries between live and dead cells.";
                ruleName = "Anneal";
            } else{
                //If input does not match any of the above cases, sets this as the default values
                ruleString = input;
                ruleDescription = "No description";
                ruleName = "No name";
            }
        }

        //Calls setRuleSet with the current ruleString if it is not empty.
        if(ruleString == ""){
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
    public void setRuleSet(String rules) throws RulesFormatException {

        //Creates patterns and matchers to check for correct formatting of the rules.
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

        //Removes duplicate numbers and puts them in accending order.
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
        return golClone;
    }
}