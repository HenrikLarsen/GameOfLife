package model;

/**
 * Statistics is a implementation of the statistics in the game.
 * containing the a clone of the current board and calculates and saves the data from the board.
 *
 * @author Henrik Finnerud Larsen
 * @version 1.0
 */
public class Statistics{
    private  int[][] statistics;

    /**
     * Constructor of the Statistics class.
     */
    public Statistics(){
    }

    /**
     * Method to get the statistics data. Goes through to every generations and saves data from the generations.
     * The number of cells alive, the difference between the cells alive to the number of cells alive in the
     * previous generation and a similarity measure where each board will be measured with the first generation
     * will be saved and returned as a byte array.
     * @param game - the game.
     * @param iterations - number of iteration to return.
     * @return statistics - A nested byte array containing the data to the statistics.
     * @see GameOfLife#clone()
     * @see GameOfLife#getPlayBoard()
     * @see Board#countCellsAlive()
     * @see Board#getSumXYCoordinates()
     */
    public int[][] getStatistics(GameOfLife game, int iterations) {
        // Clones the current game
        GameOfLife gameOfLife = (GameOfLife) game.clone();

        if (gameOfLife.getPlayBoard() instanceof DynamicBoard) {
            ((DynamicBoard) gameOfLife.getPlayBoard()).setNonExpandable();
        }
        statistics = new int[3][iterations + 1];
        if(gameOfLife.getPlayBoard() instanceof DynamicBoard){
            ((DynamicBoard) gameOfLife.getPlayBoard()).expandBoardDuringRunTime();
        }

        // First playBoard to be measured with
        int firstXYSum = gameOfLife.getPlayBoard().getSumXYCoordinates();
        int firstCellsAlive = gameOfLife.getPlayBoard().countCellsAlive();
        int firstCellsDifference = 0;
        double firstReducedBoard = 0.5 * firstCellsAlive + 3.0 * firstCellsDifference + 0.25 * firstXYSum;

        // Loops through the number of generations given from the parameter.
        for(int j = 0; j < statistics[0].length; j++){
            // the playBoard from current generation to be measured with the first board.
            int xySum = gameOfLife.getPlayBoard().getSumXYCoordinates();
            int cellsAlive = gameOfLife.getPlayBoard().countCellsAlive();

            int cellsDifference;
            if(j == 0){
                cellsDifference = 0;
            }else{
                cellsDifference = cellsAlive - statistics[0][j-1];
            }
            double reducedBoard = 0.5 * cellsAlive + 3.0 * cellsDifference + 0.25 * xySum;

            // Calculates the similarity measure
            double similarity = Math.min(firstReducedBoard, reducedBoard)/Math.max(firstReducedBoard, reducedBoard)*100;
            double similarityFloored =  Math.floor(similarity);

            // Entering statistics into the statistics array
            if(j == 0 || firstCellsAlive == 0){
                statistics[0][j] = cellsAlive;
                statistics[1][j] = 0;
                statistics[2][j] = 100;
            }else{
                statistics[0][j] = cellsAlive;
                statistics[1][j] = cellsDifference;
                statistics[2][j] = (int)similarityFloored;
            }

            // Loads the next generation
            gameOfLife.nextGeneration();
        }

        return statistics;
    }

    /**
     * Method that runs through a nested int array with statistics returning the generation with the
     * highest probability of a recurring pattern by looking at the similarity measure.
     * @param stat - The statistics to array to be considered.
     * @return iteration - The iteration with the highest probability of a similar pattern.
     */
    public int getHighestSimilarity(int[][] stat){
        int iteration = 0;
        double probability = 99.5;
        for(int i = 2; i < stat[2].length; i++){
            if(stat[2][i] > probability){
                probability = stat[2][i];
                iteration = i;
            }
        }
        return iteration;
    }

    /**
     * Method that runs through a nested int array with statistics returning a string with the values for
     * each parameter in its own line.
     * @return String - The string containing the value of a patterns statistics
     * @see #statistics
     */
    public String statToString(){
        String CellsAlive = "CellsAlive:";
        for (int j = 0; j < statistics[0].length; j++) {
            CellsAlive += " " + statistics[0][j];
        }

        String cellsDiff = "cellsDiff: ";
        for (int j = 0; j < statistics[0].length; j++) {
            cellsDiff += " " + statistics[1][j];
        }

        String similarityMeasure = "similarityMeasure: ";
        for (int j = 0; j < statistics[0].length; j++) {
            similarityMeasure += " " + statistics[2][j];
        }

        return CellsAlive + "\n" + cellsDiff + "\n" + similarityMeasure;
    }
}