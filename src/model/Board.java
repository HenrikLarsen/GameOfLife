package model;

/**
 * Created by Oscar Vladau on 30.01.2017.
 */
public class Board {
    public int startCells;
    public int height;
    public int width;
    public double cellSize = 15.5d; //19.4d;

    public void setCellSize(double size){
        this.cellSize = size;
    }

    //Constructor
    public Board() {
    }
}
