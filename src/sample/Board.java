package sample;

/**
 * Created by Oscar Vladau on 30.01.2017.
 */
public abstract class Board {
    public int startCells;
    public int height;
    public int width;
    public int cellSize = 50;

    public void setCellSize(int size){
        this.cellSize = size;
    }

    //Constructor
    public Board() {
    }
}
