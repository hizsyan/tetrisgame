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
public class Tetromino implements Serializable{
    /**
     * Boolean flag storing whether the Tetro is currently acitve in a Game instance
     */
    private boolean active;
    /**
     * The ArrayList of {@link TetrisSquare} occupied by the Tetromino
     */
    private ArrayList<TetrisSquare> squares;
    /**
     * The Color of the Tetromino
     */
    private Color color;
    /**
     * The grid coordinates of the origin of the Tetromino
     */
    private Coord topleft;
    /**
     * The shape of the Tetromino
     */
    private TetroShape shape;
    /**
     * The leftward width of the Tetro relative to its origin in squares
     */
    private int lWidth;
    /**
     * The rightward widht of the Tetro relative ot its origin in squares
     */
    private int rWidth;
    /**
     * The height of the Tetromino in squares
     */
    private int height;
    /**
     * The Tetris game the Tetromino is in
     */
    private TetrisPanel TP;
    /**
     * The thread handling the downward movement of the Tetro
     */
    private transient Thread moveThread;

    
    /**
     * Starts a thread to manage the movement of the Tetromino.
     * The thread continuously moves the Tetromino down while active
     * This way the downward movement is desynchronized from the main loop
     */
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

     /**
     * Constructs a new Tetromino with the specified shape ({@link TetroShape}), initial x-position, and Tetris panel ({@link TetrisPanel}).
     * 
     * @param i Index of the shape type.
     * @param x Initial x-coordinate of the Tetromino.
     * @param t Reference to the Tetris panel.
     */
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

    /**
     * Checking if the Tetromino is stopped by a locked square ({@link TetrisSquare})
     * @param offset the direction we want to move the Tetro
     * @param start The current origin of the tetro
     * @return True if the Tetro is stopped, False otherwise
     */
    public boolean isStopped(int[] offset, Coord start){
        for(Coord cord : this.shape.getRelative()){
            if(TP.getSquares()[start.getY()+cord.getY()+offset[1]][start.getX()+cord.getX()+offset[0]].isLocked()){ //if it hits a locked tetro it stops
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the Tetromino can be rotated 90 degrees clockwise
     * Copies the shape of the Tetromino and checks if the rotated shape with the same origin would be out of bounds or collide with a locked square {@link TetrisSquare} 
     * @return True if rotating the Tetro can be done without illegal behaviour, False if not
     */
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

    /**
     * Stops the thread handling the downward movement of the Tetromino
     * Sets the active flag to false
     */
    public void stopMovementThread() {
        active = false;
        if (moveThread != null) {
            moveThread.interrupt();
        }
    }

    /**
     * Sets the locked flag of the squares ({@link TetrisSquare}) occupied by the Tetromini
     * This makes it so its a locked in place element of the Board
     */
    public void lockTetro(){
        for(TetrisSquare square : this.squares){
            square.lock();
        }
        this.active = false;
        this.stopMovementThread();
        TP.changeTetro();
    }

    /**
     * Calculate the width of the Tetromino to the left relative to its origin (topleft)
     * @param shape
     * @return the leftward width of the Tetromino as an integer
     */
    private int calcLwidth(Coord[] shape){
        int min = 0;
        for(Coord cord : shape){
            min = (cord.getX()<min) ? cord.getX():min;
        }
        return -min;
    } 

    /**
     * Calculate the width of the Tetromino to the right relative to its origin (topleft)
     * @param shape
     * @return the rightward with of the Tetromino as an integer
     */
    private int calcRwidth(Coord[] shape){
        int max = 0;
        for(Coord cord : shape){
            max = (cord.getX()>max) ? cord.getX():max;
        }
        return max;
    }

    /**
     * Calculate how "tall" a Tetromino is
     * @param shape The shape of the Tetromino 
     * @return the height in squares of the Tetro as an integer
     */
    private int calcheight(Coord[] shape){
        int max = 0;
        for(Coord cord : shape){
            max = (cord.getY()>max) ? cord.getY():max;
        }
        return max;
    }

    /**
     * Activates the Tetro
     * Allows for storing Tetrominos without them being moved down/in the game
     */
    public void activate(){
        this.active = true;
        this.setSquares();
        startMovementThread();
    }

    /**
     * Assigns the squares ({@link TetrisSquare}) occupied by this Tetromino
     * Called at initialization and whenever the Tetromino placement is changed
     */
    private void setSquares(){
        for(Coord cord:shape.getRelative()){
            TetrisSquare TS = TP.getSquares()[topleft.getY()+cord.getY()][topleft.getX()+cord.getX()];
            TS.setColor(this.color);
            squares.add(TS);
        }
    }

    /**
     * 
     * @param i the int corresponding to the Tetromino
     * @return the Color of the shape from the {@link TetrisUtils} helper class
     */
    private Color colorPicker(int i){
        return TetrisUtils.getColorForShape(ShapeType.values()[i]);
    }

    /**
     * Creates a new shape ({@link TetroShape})
     * @param i the integer corresponding to the shape/kind of the Tetromino (7 possible)
     */
    private void shapePicker(int i){
        this.shape = new TetroShape(i); 
    }
    
    /**
     * Clears the Squares ({@link TetrisSquare}) occupied by the Tetromino
     * Called whenever the placing of the Tetro is changed, moved/rotated
     */
    private void clearSquares(){
        for(TetrisSquare T:squares){
            T.setColor(TP.getBase());
        }
        this.squares.clear();
    }   

    /**
     * Moves the current {@link Tetromino} down by one
     * First check if moving down is in bounds and whether it collides with a locked square
     * If can move down push the origin (topleft) {@link Coord} of the Tetro down by one
     * Reassign the squares {@link TetrisSquare} occupied by the Tetro according to the new origin
     * If can't be moved further down, lock the Squares of the Tetro
     */
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
    /** 
     * Moves the Tetromino to the right by one column
     * Wrapped in invokelater
     * Checks if moving right is in bounds or stopped by a locked square
     * If moving left is possible do it, reassign sqaures {@link TetrisSquare}
     */
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

    /**
     * Moves the Tetromino to the left by one column
     * Wrapped in invokelater
     * Checks if moving left is in bounds or stopped by a locked square
     * If moving left is possible do it, reassign sqaures {@link TetrisSquare}
     */
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


    /**
     * Pushes the Tetro down as far as possible
     * First we copy the the origin coordinate, and then simulate dropping it down by 1 row while as long as it doesnt hit the bottom or a locked tetro
     * After we find the lowest possible coordinate we set this for the Tetro
     * Height, width and occupied squares need to be recalculated aswell.
     * The part of the method that modifies data is wrapped in invokLater to keep it thread safe
     */
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

    /**
     * Rotates the Tetromino if possible, determined by canRotate() method
     * Wrapped in swingutilites.invokelater to ensure that its thread safe with the gameloop
     * With rotation the occupied squares, width and height of the Tetromino also changes, these are reassigned after rotation
     */
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

  /**
     * Checks whether the Tetromino is currently active.
     *
     * @return True if the Tetromino is active, false otherwise.
     */
    public boolean getActive() {
        return this.active;
        }

    /**
     * Retrieves the top-left coordinate of the Tetromino.
     *
     * @return A Coord object representing the top-left position.
     */
    public Coord getTopleft(){
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
    public Color getColor(){
        return this.color;
    }

    /**
     * Retrieves the squares that make up the Tetromino.
     *
     * @return A list of TetrisSquare objects representing the Tetromino's squares.
     */
    public java.util.List<TetrisSquare> getSquares(){
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

    /**
     * Sets whether the Tetromino is active.
     *
     * @param flag True to activate the Tetromino, false to deactivate it.
     */
    public void setActive(boolean flag){
        this.active = flag;
    }
}
