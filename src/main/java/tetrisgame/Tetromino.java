package tetrisgame;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Tetromino {
    private boolean active;
    private ArrayList<TetrisSquare> squares;
    private Color color;
    private enum Shapes{
        O, I, S, L, J, T, Z
    }
    private int[] topleft;
    private int[][] shape;
    private int lWidth;
    private int rWidth;
    private int height;
    private TetrisPanel TP;
    private Thread moveThread;
    private int orientation; //array of shapes method 

    private void reverseCord(int[] cord){
        int[] cordcopy = cord;
        cord[0] = cord[1];
        cord[1] = cordcopy[0];
    }

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
        this.lWidth = calcLwidth(this.shape);
        this.rWidth = calcRwidth(this.shape);
        this.height = calcheight(this.shape);
        color = Color.RED;
        this.topleft = new int[2];
        topleft[0] = x;
        topleft[1] = 0;    
        TP = t;
        squares = new ArrayList<>();
    }

    public boolean isStopped(int[] offset, int[] start){
        for(int[] cord : this.shape){
            if(TP.getSquares()[start[1]+cord[1]+offset[1]][start[0]+cord[0]+offset[0]].isLocked()){ //if it hits a locked tetro it stops
                return true;
            }
        }
        return false;
    }

    public void stopMovementThread() {
        active = false;
        if (moveThread != null) {
            moveThread.interrupt();
        }
    }

    public void lockTetro(){
        System.out.println("LOCKED!!!");
        for(TetrisSquare square : this.squares){
            square.lock();
        }
        this.active = false;
        this.stopMovementThread();
        TP.changeTetro();
    }

    public void turn(){
        //hardcoded array of shapes and just increment index by 1 (index = index mod 4) or do matrix? operations every time?
        
    }

    private int calcLwidth(int[][] shape){
        int min = 0;
        for(int[] cord : shape){
            min = (cord[0]<min) ? cord[0]:min;
        }
        return -min;
    } 

    private int calcRwidth(int[][] shape){
        int max = 0;
        for(int[] cord : shape){
            max = (cord[0]>max) ? cord[0]:max;
        }
        return max;
    }

    private int calcheight(int[][] shape){
        int max = 0;
        for(int[] cord : shape){
            max = (cord[1]>max) ? cord[1]:max;
        }
        return max;
    }

    public void activate(){
        this.active = true;
        this.setSquares();
        startMovementThread();
    }

    private void setSquares(){
        for(int[] cord:shape){
            TetrisSquare TS = TP.getSquares()[topleft[1]+cord[1]][topleft[0]+cord[0]];
            TS.setOccupied(true);
            TS.setColor(this.color);
            squares.add(TS);
        }
    }

    private void shapePicker(int i){
        int[][][] shapes = 
        {
            {{0,0},{0,1},{1,0},{1,1}},
            {{0,0},{0,1},{0,2},{0,3}},
            {{0,0},{1,0},{-1,1},{0,1}},
            {{0,0},{0,1},{0,2},{1,2}},
            {{0,0},{0,1},{0,2},{-1,2}},
            {{0,0},{1,0},{2,0},{1,1}},
            {{0,0},{1,0},{1,1},{1,2}}
        };
        this.shape = shapes[i];
    }
    
    private void clearSquares(){
        for(TetrisSquare T:squares){
            T.setColor(TP.getBase());
            T.setOccupied(false);
        }
        this.squares.clear();
    }   


      public void moveDown() {
        int[] off = {0,1};
        if(this.topleft[1]+height==TP.getSquareHeight() || (isStopped(off, this.topleft))){
            this.lockTetro();
            return;
        } 
        SwingUtilities.invokeLater(() -> {
            this.topleft[1] += 1;
            clearSquares();
            setSquares();
            TP.repaint();
            }
        );
    }

    public void moveRight(){
        int[] off = {1,0};
        SwingUtilities.invokeLater(()->{
            if(this.topleft[0]+rWidth<this.TP.getSquareWidth()){
                if(isStopped(off, this.topleft)){
                    return;
                }
                this.topleft[0] += 1;
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
            if(this.topleft[0]-lWidth>0){
                if(isStopped(off, this.topleft)){
                    return;
                }
                this.topleft[0] -= 1;
                clearSquares();
                setSquares();
                this.TP.repaint();
                }
            }
        );
    }

    public void pushDown(){
        int[] cordcopy = this.topleft;
        int[] off = new int[]{0,1};
        while((this.height + cordcopy[1] < this.TP.getSquareHeight())&&!isStopped(off, cordcopy)){
            cordcopy[1] += 1;
        }
        SwingUtilities.invokeLater(()->{
                System.out.println("PUSHED DOWN, current Y: " + this.topleft[1]);
                this.topleft = cordcopy;
                clearSquares();
                setSquares();
                this.TP.repaint();
                }
        );
    }

    public boolean getActive(){
        return this.active;
    }

    public int[] getTopleft(){
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

    public void setActive(boolean flag){
        this.active = flag;
    }
}
