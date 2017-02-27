package sample;

/**
 * Created by Oscar Vladau on 30.01.2017.
 */
public class StaticBoard extends Board {
    private final int WIDTH = 10, HEIGHT = 10;
    public byte[][] boardGrid;
    public int cellsAlive = 0;

    //Constructor
    public StaticBoard(byte[][] newBoard){
        this.boardGrid = newBoard;
    }

    public StaticBoard() {
        this.boardGrid = new byte[WIDTH][HEIGHT];
    }

    public byte[][] countNeighbours() {
        //new byte[][] som setter antall naboer for hver celle.
        byte[][] neighbourCount = new byte[boardGrid.length][boardGrid[0].length];

        int xMax = boardGrid.length;
        int yMax = boardGrid[0].length;

        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {

                //Sjekker om cellen er i live, og om den er det øker den antall naboer for alle cellene rundt.
                //Klarer ikke jobbe med cellene ytterst i brettet.
                if (boardGrid[x][y] == 1) {

                    if (x - 1 >= 0 && y - 1 >= 0) {
                        neighbourCount[x - 1][y - 1]++;
                    }

                    if (x - 1 >= 0) {
                        neighbourCount[x - 1][y]++;
                    }

                    if (x - 1 >= 0 && y + 1 < yMax) {
                        neighbourCount[x - 1][y + 1]++;
                    }

                    if (y - 1 >= 0) {
                        neighbourCount[x][y - 1]++;
                    }

                    if (y + 1 < yMax) {
                        neighbourCount[x][y + 1]++;
                    }

                    if (x + 1 < xMax && y - 1 >= 0) {
                        neighbourCount[x + 1][y - 1]++;
                    }

                    if (x + 1 < xMax) {
                        neighbourCount[x + 1][y]++;
                    }

                    if (x + 1 < xMax && y + 1 < yMax) {
                        neighbourCount[x + 1][y + 1]++;
                    }
                }
            }
        }
        return neighbourCount;
    }

    public void setBoard(byte[][] newGrid) {
        this.boardGrid = newGrid;
    }

    public void resetBoard() {
        for (int x = 0; x < boardGrid.length; x++) {
            for (int y = 0; y < boardGrid[0].length; y++) {
                boardGrid[x][y] = 0;
            }
        }
        cellsAlive = 0;
    }

    @Override
    public String toString(){
        String str = "";
        for (int y = 0; y < boardGrid[0].length; y++) {
            for (int x = 0; x < boardGrid.length; x++) {
                if (boardGrid[x][y] == 1) {
                    str = str + "1";
                } else {
                    str = str + "0";
                }
            }
        }
        return str;
    }

}