package tetrisgame;

import java.io.Serializable;

public class TetroShape implements Serializable{
    private Coord[] relative;
    private boolean[][] absolute;

    public TetroShape(int i){
        int[][] shapeCoords = TetrisUtils.getShapeCoords(TetrisUtils.ShapeType.values()[i]);
        this.relative = new Coord[4];
        for(int j = 0; j<4;j++){
            try{
                this.relative[j] = new Coord(shapeCoords[j]);
            }
            catch(Exception e){}
        }
    }

    public TetroShape(TetroShape copied){
        this.relative = new Coord[copied.relative.length];
        for(int j = 0; j<copied.relative.length;j++){
            this.relative[j] = new Coord(copied.relative[j].getX(), copied.relative[j].getY());
        }
    }

    private Coord calcTopLeft(){
        for(int row = 0; row<4;row++){
            for(int col = 0; col<4;col++){
                if(this.absolute[row][col]){
                    return new Coord(row, col);
                }
            }
        }
        return new Coord(-1,-1);
    }

    private void calcRelative(){
        Coord topleft = calcTopLeft();
        for(int row = 0; row<4;row++){
            for(int col = 0; col < 4; col++){
                Coord current = new Coord(row,col);
                if(absolute[row][col] && !topleft.equals(current)){
                    this.relative[this.relative.length-1] = current;
                }
            }
        }
    }

    public void rotateClock(){
        Coord[] newcoords = new Coord[4];
        for(int curr = 0; curr<4;curr++){
            Coord c = this.relative[curr];
            Coord make = new Coord(-c.getY(), c.getX());
            newcoords[curr] = make;
        }
        this.relative = newcoords;
    }

    public Coord[] getRelative(){
        return relative;
    }

    public boolean[][] getAbsolute(){
        return absolute;
    }

}
