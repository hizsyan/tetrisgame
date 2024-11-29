package tetrisgame;

import java.io.Serializable;

public class Coord implements Serializable{
    private int x;
    private int y;

    public Coord(int xX, int yY){
        x = xX;
        y = yY;
    }

    public Coord(int[] vals)throws Exception{
        if(vals.length!=2){
            throw new Exception("Must have two values exactly");
        }
        x = vals[0];
        y = vals[1];

    }

    public Coord(){
        x = 0;
        y = 0;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int X){
        x = X;
    }

    public void setY(int Y){
        y = Y;
    }

    public void makeRelative(Coord origin){
        this.x -= origin.x;
        this.y -= origin.y; 
    }

    public void flip(){
        int buffy = this.y;
        this.y = this.x;
        this.x = buffy;
    }

    public int[] toArray(){
        return new int[]{this.x,this.y};
    }

    public void add(Coord xy){
        this.x += xy.getX();
        this.y += xy.getY();
    }

    public void pushX(int off){
        this.x+=off;
    }

    public void pushY(int off){
        this.y += off;
    }
}
