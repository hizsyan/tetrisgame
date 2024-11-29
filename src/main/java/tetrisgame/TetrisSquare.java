package tetrisgame;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class TetrisSquare implements Serializable{
    private Coord position;
    private boolean occupied;
    private Color color;
    private boolean locked;
    
    public TetrisSquare(int xX, int yY){
        this.position = new Coord(xX,yY);
        occupied = false;
        color = TetrisUtils.BASE_COLOR;
        locked = false;
    }

    public void setY(int y){
        this.position.setY(y);
    }

    public int getY(){
        return this.position.getY();
    }

    public void setX(int x){
        this.position.setX(x);
    }

    public int getX(){
        return this.position.getX();
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

    public Coord getPosition(){
        return this.position;
    }

    public void setPosition(Coord co){
        this.position = co;
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
