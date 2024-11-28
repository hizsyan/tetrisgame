package tetrisgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class TetrisPanel extends JPanel{
    private GameFrame Game;
    protected TetrisSquare[][] Squares = new TetrisSquare[20][10];
    protected Color base; 
    protected Tetromino currTetro;
    protected Tetromino nextTetro;
    private Random R;
    private int squareWidth; 
    private int squareHeight;
    private int score;
    private boolean gameOn;

    private void initSquares(){
        for(int row = 0; row<20; row++){
            for(int col = 0; col<10; col++){
                Squares[row][col] = new TetrisSquare(row*30, col*30);
            }
        }
    }

    public TetrisPanel(GameFrame G){
        Game = G;
        R = new Random();
        this.setSize(300,600);
        this.squareHeight = 19;
        this.squareWidth = 9;
        initSquares();
        currTetro = new Tetromino(R.nextInt(7), 5, this);
        nextTetro = new Tetromino(R.nextInt(7), 5, this);
        this.base = new Color(0,0,0);
        this.gameOn = true;
        this.score = 0;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int row = 0; row<20; row++){
            for(int col = 0; col<10; col++){
                Color currcolor = Squares[row][col].getColor();
                g.setColor(currcolor);
                g.fillRect(30*col, 30*row, 30, 30);
            }
        }
        g.setColor(Color.white);
        for (int col = 1; col<10;col++){
            g.drawLine(col*30, 0, col*30, 600);
        }
        for (int row = 1; row<20; row++){
            g.drawLine(0, row*30, 300, row*30);
        }
    }

    public boolean isLost(){
        for(TetrisSquare square : this.Squares[0]){
            if(square.isLocked()){
                return true;
            }
        }
        return false;
    }
    
    public void checkRows(){
        int counter = 0;
        for(TetrisSquare[] row : this.Squares){
            boolean full = true;
            for(TetrisSquare currSq : row){
                if(!currSq.isLocked()){
                    full = false;
                }
            }
            if(full){
                deleteRow(counter);
            }
            counter++;
        }
    }

    public void deleteRow(int i){
        for(int row = i; row>0;row--){
            this.Squares[row] = this.Squares[row-1];
        }
        this.Squares[0] = new TetrisSquare[this.squareWidth+1];
        for(int col = 0; col<10; col++){
            Squares[0][col] = new TetrisSquare(0*30, col*30);
        }
    }

    public void changeTetro(){
        currTetro = nextTetro;
        nextTetro = new Tetromino(R.nextInt(7),5,this);
    }

    public void doFrame(){
        if(isLost()){
            this.gameOn = false;
            return;
        }
        if(!currTetro.getActive()){
            currTetro.activate();
        }
        checkRows();
        this.repaint();
    }

    public void moveLeft(){
        this.currTetro.moveLeft();
    }

    public void moveRight(){
        this.currTetro.moveRight();
    }

    public void rotateTetro(){
        this.currTetro.rotate();
    }

    public TetrisSquare[][] getSquares(){
        return Squares;
    }

    public Tetromino getCurrTetro(){
        return currTetro;
    }

    public Tetromino getNextTetro(){
        return nextTetro;
    }

    public Color getBase(){
        return base;
    }

    public int getSquareWidth(){
        return squareWidth;
    }

    public int getSquareHeight(){
        return squareHeight;
    }

    public int getScore(){
        return score;
    }

    public boolean isGameOn(){
        return gameOn;
    }
}
