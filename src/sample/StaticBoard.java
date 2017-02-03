package sample;

/**
 * Created by Oscar Vladau on 30.01.2017.
 */
public class StaticBoard extends Board {
    public byte[][] boardGrid;


    //Constructor
    public StaticBoard() {
        this.boardGrid = new byte[][]{{0, 1, 1, 1,0, 1, 1, 0}, {1, 1, 1, 1, 1, 0, 1, 1}, {1, 1, 0, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 0, 1, 1, 0},{1, 1, 1, 1,0, 1, 1, 0}, {1, 1, 1, 1, 1, 0, 1, 1}, {1, 1, 0, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 0, 1, 1, 0}};
    }


    public StaticBoard(byte[][] setBoard){
        this.boardGrid = setBoard;
    }
}
