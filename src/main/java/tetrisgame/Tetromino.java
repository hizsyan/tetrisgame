package tetrisgame;
import javax.swing.*;

import tetrisgame.TetrisUtils.ShapeType;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class Tetromino implements Serializable{
    private boolean active;
    private ArrayList<TetrisSquare> squares;
    private Color color;
    private Coord topleft;
    private TetroShape shape;
    private int lWidth;
    private int rWidth;
    private int height;
    private TetrisPanel TP;
    private transient Thread moveThread;

    public void startMovementThread() {
        moveThread = new Thread(() -> {
            while (active) {
                moveDown();
                try {
                    Thread.sleep(500); // Adjust speed as needed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        moveThread.start();
    }

    public Tetromino(int i, int x, TetrisPanel t){
        active = false;
        shapePicker(i); //to make easy lines
        this.lWidth = calcLwidth(this.shape.getRelative());
        this.rWidth = calcRwidth(this.shape.getRelative());
        this.height = calcheight(this.shape.getRelative());
        color = colorPicker(i);
        this.topleft = new Coord(x,0);   
        TP = t;
        squares = new ArrayList<>();
    }

    public boolean isStopped(int[] offset, Coord start){
        for(Coord cord : this.shape.getRelative()){
            if(TP.getSquares()[start.getY()+cord.getY()+offset[1]][start.getX()+cord.getX()+offset[0]].isLocked()){ //if it hits a locked tetro it stops
                return true;
            }
        }
        return false;
    }

    public boolean canRotate() {
        TetroShape rotatedShape = new TetroShape(shape); // Clone the current shape
        rotatedShape.rotateClock(); // Apply rotation to the copy
    
        // Check each square of the rotated shape for validity
        for (Coord coord : rotatedShape.getRelative()) {
            int newX = topleft.getX() + coord.getX();
            int newY = topleft.getY() + coord.getY();
    
            // Check boundaries
            if (newX < 0 || newX >= TP.getSquareWidth() || newY < 0 || newY >= TP.getSquareHeight()) {
                return false;
            }
    
            // Check for overlap with locked squares
            if (TP.getSquares()[newY][newX].isLocked()) {
                return false;
            }
        }
    
        return true; // All checks passed
    }

    public void stopMovementThread() {
        active = false;
        if (moveThread != null) {
            moveThread.interrupt();
        }
    }

    public void lockTetro(){
        for(TetrisSquare square : this.squares){
            square.lock();
        }
        this.active = false;
        this.stopMovementThread();
        TP.changeTetro();
    }

    private int calcLwidth(Coord[] shape){
        int min = 0;
        for(Coord cord : shape){
            min = (cord.getX()<min) ? cord.getX():min;
        }
        return -min;
    } 

    private int calcRwidth(Coord[] shape){
        int max = 0;
        for(Coord cord : shape){
            max = (cord.getX()>max) ? cord.getX():max;
        }
        return max;
    }

    private int calcheight(Coord[] shape){
        int max = 0;
        for(Coord cord : shape){
            max = (cord.getY()>max) ? cord.getY():max;
        }
        return max;
    }

    public void activate(){
        this.active = true;
        this.setSquares();
        startMovementThread();
    }

    private void setSquares(){
        for(Coord cord:shape.getRelative()){
            TetrisSquare TS = TP.getSquares()[topleft.getY()+cord.getY()][topleft.getX()+cord.getX()];
            TS.setColor(this.color);
            squares.add(TS);
        }
    }

    private Color colorPicker(int i){
        return TetrisUtils.getColorForShape(ShapeType.values()[i]);
    }

    private void shapePicker(int i){
        this.shape = new TetroShape(i); 
    }
    
    private void clearSquares(){
        for(TetrisSquare T:squares){
            T.setColor(TP.getBase());
        }
        this.squares.clear();
    }   


      public void moveDown() {
        int[] off = {0,1};
        if(this.topleft.getY()+height==TP.getSquareHeight() || (isStopped(off, this.topleft))){
            this.lockTetro();
            return;
        } 
        SwingUtilities.invokeLater(() -> {
            this.topleft.pushY(1);
            clearSquares();
            setSquares();
            TP.repaint();
            }
        );
    }

    public void moveRight(){
        int[] off = {1,0};
        SwingUtilities.invokeLater(()->{
            if(this.topleft.getX()+rWidth<this.TP.getSquareWidth()){
                if(isStopped(off, this.topleft)){
                    return;
                }
                this.topleft.pushX(1);
                clearSquares();
                setSquares();
                this.TP.repaint();
                }
            }
        );
    }

    public void moveLeft(){
        int[] off = {-1,0};
        SwingUtilities.invokeLater(()->{
            if(this.topleft.getX()-lWidth>0){
                if(isStopped(off, this.topleft)){
                    return;
                }
                this.topleft.pushX(-1);
                clearSquares();
                setSquares();
                this.TP.repaint();
                }
            }
        );
    }

    public void pushDown(){
        Coord cordcopy = this.topleft;
        int[] off = new int[]{0,1};
        while((this.height + cordcopy.getY() < this.TP.getSquareHeight())&&!isStopped(off, cordcopy)){
            cordcopy.setY(cordcopy.getY()+1);
        }
        SwingUtilities.invokeLater(()->{
                this.topleft = cordcopy;
                clearSquares();
                setSquares();
                this.TP.repaint();
                }
        );
    }

    public void rotate(){
        if(canRotate()){
            SwingUtilities.invokeLater( ()->{
                this.shape.rotateClock();
                clearSquares();
                setSquares();
                this.lWidth = this.calcLwidth(this.shape.getRelative());
                this.rWidth = this.calcRwidth(this.shape.getRelative());
                this.height = this.calcheight(this.shape.getRelative());
                this.TP.repaint();
                }
            
            );
        }
    }

    public boolean getActive(){
        return this.active;
    }

    public Coord getTopleft(){
        return this.topleft;
    }

    public Color getColor(){
        return this.color;
    }

    public java.util.List<TetrisSquare> getSquares(){
        return this.squares;
    }

    public int getLwidth(){
        return this.lWidth;
    }

    public int getRwidth(){
        return this.rWidth;
    }

    public int getHeight(){
        return this.height;
    }

    public TetrisPanel getPanel(){
        return this.TP;
    }

    public TetroShape getShape(){
        return this.shape;
    }

    public void setActive(boolean flag){
        this.active = flag;
    }
}
