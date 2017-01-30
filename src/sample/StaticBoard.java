package sample;

/**
 * Created by Oscar Vladau on 30.01.2017.
 */
public class StaticBoard extends Board {
    private byte[][] board;


    //Constructor
    public StaticBoard() {
        this.board = new byte[][]{{1, 0, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 0}, {1, 0, 0, 1}};
    }

    public StaticBoard(byte[][] setBoard){
        this.board = setBoard;
    }
}
