package tetrisgame;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class TetrisSquare {
    private int x;
    private int y;
    private boolean occupied;
    //private int[] color = new int[3];
    private Color color;
    private boolean locked;
    
    public TetrisSquare(int X, int Y){
        x = X;
        y = X;
        occupied = false;
        color = new Color(0,0,0);
        locked = false;
    }

    public int getx(){
        return x;
    }

    public int gety(){
        return y;
    }

    public boolean getStatus(){
        return occupied;
    }

    public Color getColor(){
        return color;
    }
    
    public void setOccupied(boolean o){
        occupied = o;
    }

    public void setColor(Color c){
        color = c;
    }

    public void lock(){
        this.locked = true;
    }

    public void unlock(){
        this.locked = false;
    }

    public boolean isLocked(){
        return locked;
    }
}
