package tetrisgame;

import javax.swing.*;
import tetrisgame.TetrisUtils.ShapeType;
import java.awt.*;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a Tetromino, the main building block of Tetris gameplay.
 * A Tetromino consists of multiple squares and can move or rotate on the Tetris board.
 */
public class Tetromino implements Serializable {

    private boolean active; // Indicates if the Tetromino is active
    private ArrayList<TetrisSquare> squares; // List of squares that make up the Tetromino
    private Color color; // Color of the Tetromino
    private Coord topleft; // Top-left position of the Tetromino on the board
    private TetroShape shape; // Shape of the Tetromino
    private int lWidth; // Left width offset
    private int rWidth; // Right width offset
    private int height; // Height of the Tetromino
    private TetrisPanel TP; // Reference to the Tetris panel
    private transient Thread moveThread; // Thread for managing movement of the Tetromino

    /**
     * Starts a thread to manage the movement of the Tetromino.
     * The thread continuously moves the Tetromino down while active.
     */
    public void startMovementThread() {
        moveThread = new Thread(() -> {
            while (active) {
                try {
                    Thread.sleep(500); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                moveDown();
            }
        });
        moveThread.start();
    }

    /**
     * Constructs a new Tetromino with the specified shape, initial x-position, and Tetris panel.
     *
     * @param i Index of the shape type.
     * @param x Initial x-coordinate of the Tetromino.
     * @param t Reference to the Tetris panel.
     */
    public Tetromino(int i, int x, TetrisPanel t) {
        active = false;
        shapePicker(i);
        this.lWidth = calcLwidth(this.shape.getRelative());
        this.rWidth = calcRwidth(this.shape.getRelative());
        this.height = calcheight(this.shape.getRelative());
        color = colorPicker(i);
        this.topleft = new Coord(x, 0);
        TP = t;
        squares = new ArrayList<>();
    }

    /**
     * Checks if the Tetromino is stopped by another Tetromino or the bottom of the board.
     *
     * @param offset The offset to check against.
     * @param start  The current position of the Tetromino.
     * @return True if the Tetromino is stopped, false otherwise.
     */
    public boolean isStopped(int[] offset, Coord start) {
        for (Coord cord : this.shape.getRelative()) {
            if (TP.getSquares()[start.getY() + cord.getY() + offset[1]][start.getX() + cord.getX() + offset[0]].isLocked()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the Tetromino can rotate without colliding with locked squares or boundaries.
     *
     * @return True if the Tetromino can rotate, false otherwise.
     */
    public boolean canRotate() {
        TetroShape rotatedShape = new TetroShape(shape); // Clone the current shape
        rotatedShape.rotateClock();

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

        return true;
    }


    /**
     * Stops the movement thread of the Tetromino.
     */
    public void stopMovementThread() {
        active = false;
        if (moveThread != null) {
            moveThread.interrupt();
        }
    }

  /**
     * Locks the Tetromino in place and notifies the Tetris panel to spawn a new Tetromino.
     */
    public void lockTetro() {
        for (TetrisSquare square : this.squares) {
            square.lock();
        }
        this.active = false;
        this.stopMovementThread();
        TP.changeTetro();
    }

    /**
     * Activates the Tetromino and starts its movement thread.
     */
    public void activate() {
        this.active = true;
        this.setSquares();
        startMovementThread();
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


    /**
     * Moves the Tetromino down by one unit.
     */
    public void moveDown() {
        int[] off = {0, 1};
        if (this.topleft.getY() + height == TP.getSquareHeight() || (isStopped(off, this.topleft))) {
            this.lockTetro();
            return;
        }
        SwingUtilities.invokeLater(() -> {
            this.topleft.pushY(1);
            clearSquares();
            setSquares();
            TP.repaint();
        });
    }

    /**
     * Moves the Tetromino to the right by one unit.
     */
    public void moveRight() {
        int[] off = {1, 0};
        SwingUtilities.invokeLater(() -> {
            if (this.topleft.getX() + rWidth < this.TP.getSquareWidth()) {
                if (isStopped(off, this.topleft)) {
                    return;
                }
                this.topleft.pushX(1);
                clearSquares();
                setSquares();
                this.TP.repaint();
            }
        });
    }


    /**
     * Moves the Tetromino to the left by one unit.
     */
    public void moveLeft() {
        int[] off = {-1, 0};
        SwingUtilities.invokeLater(() -> {
            if (this.topleft.getX() - lWidth > 0) {
                if (isStopped(off, this.topleft)) {
                    return;
                }
                this.topleft.pushX(-1);
                clearSquares();
                setSquares();
                this.TP.repaint();
            }
        });
    }

     /**
     * Pushes the Tetromino down to the lowest possible position.
     */
    public void pushDown() {
        Coord cordcopy = this.topleft;
        int[] off = new int[]{0, 1};
        while ((this.height + cordcopy.getY() < this.TP.getSquareHeight()) && !isStopped(off, cordcopy)) {
            cordcopy.setY(cordcopy.getY() + 1);
        }
        SwingUtilities.invokeLater(() -> {
            this.topleft = cordcopy;
            clearSquares();
            setSquares();
            this.TP.repaint();
        });
    }
    
    /**
     * Rotates the Tetromino clockwise if possible.
     */
    public void rotate() {
        if (canRotate()) {
            SwingUtilities.invokeLater(() -> {
                this.shape.rotateClock();
                clearSquares();
                setSquares();
                this.lWidth = this.calcLwidth(this.shape.getRelative());
                this.rWidth = this.calcRwidth(this.shape.getRelative());
                this.height = this.calcheight(this.shape.getRelative());
                this.TP.repaint();
            });
        }
    }
  /**
     * Checks whether the Tetromino is currently active.
     *
     * @return True if the Tetromino is active, false otherwise.
     */
    public boolean getActive() {
        return this.active;
    }

    /**
     * Sets whether the Tetromino is active.
     *
     * @param flag True to activate the Tetromino, false to deactivate it.
     */
    public void setActive(boolean flag) {
        this.active = flag;
    }

    /**
     * Retrieves the top-left coordinate of the Tetromino.
     *
     * @return A Coord object representing the top-left position.
     */
    public Coord getTopleft() {
        return this.topleft;
    }

    /**
     * Sets the top-left coordinate of the Tetromino.
     *
     * @param topleft A Coord object representing the new top-left position.
     */
    public void setTopleft(Coord topleft) {
        this.topleft = topleft;
    }

    /**
     * Retrieves the color of the Tetromino.
     *
     * @return A Color object representing the Tetromino's color.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the color of the Tetromino.
     *
     * @param color A Color object representing the new color.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Retrieves the squares that make up the Tetromino.
     *
     * @return A list of TetrisSquare objects representing the Tetromino's squares.
     */
    public ArrayList<TetrisSquare> getSquares() {
        return this.squares;
    }

    /**
     * Retrieves the left width offset of the Tetromino.
     *
     * @return The left width offset as an integer.
     */
    public int getLwidth() {
        return this.lWidth;
    }

    /**
     * Sets the left width offset of the Tetromino.
     *
     * @param lWidth The new left width offset.
     */
    public void setLwidth(int lWidth) {
        this.lWidth = lWidth;
    }

    /**
     * Retrieves the right width offset of the Tetromino.
     *
     * @return The right width offset as an integer.
     */
    public int getRwidth() {
        return this.rWidth;
    }

    /**
     * Sets the right width offset of the Tetromino.
     *
     * @param rWidth The new right width offset.
     */
    public void setRwidth(int rWidth) {
        this.rWidth = rWidth;
    }

    /**
     * Retrieves the height of the Tetromino.
     *
     * @return The height as an integer.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Sets the height of the Tetromino.
     *
     * @param height The new height of the Tetromino.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Retrieves the associated Tetris panel.
     *
     * @return The TetrisPanel object associated with the Tetromino.
     */
    public TetrisPanel getPanel() {
        return this.TP;
    }

    /**
     * Sets the associated Tetris panel.
     *
     * @param TP The TetrisPanel object to associate with the Tetromino.
     */
    public void setPanel(TetrisPanel TP) {
        this.TP = TP;
    }

    /**
     * Retrieves the shape of the Tetromino.
     *
     * @return The TetroShape object representing the Tetromino's shape.
     */
    public TetroShape getShape() {
        return this.shape;
    }

    /**
     * Sets the shape of the Tetromino.
     *
     * @param shape The new TetroShape for the Tetromino.
     */
    public void setShape(TetroShape shape) {
        this.shape = shape;
    }
}