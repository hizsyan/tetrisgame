package tetrisgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


/**
 * The TetrisPanel class represents the game board for a Tetris game.
 * It handles the rendering of the board, the current and next Tetromino {@link Tetromino},
 * as well as game logic like clearing rows and detecting game over conditions.
 * It also stores boolean flags representing whether it's paused or has been updated
 */
public class TetrisPanel extends JPanel implements Serializable {
   /**
 * A 2D array of squares representing the Tetris game board.
 */
protected TetrisSquare[][] Squares;

/**
 * The base color for unoccupied squares on the board.
 * Transient to avoid being serialized.
 */
protected transient Color base;

/**
 * The currently active Tetromino being controlled by the player.
 */
protected Tetromino currTetro;

/**
 * The next Tetromino that will appear on the board.
 */
protected Tetromino nextTetro;

/**
 * Random number generator for Tetromino selection.
 * Transient to avoid being serialized.
 */
private transient Random R;

/**
 * The width of the Tetris board in terms of the number of squares.
 */
private int squareWidth;

/**
 * The height of the Tetris board in terms of the number of squares.
 */
private int squareHeight;

/**
 * The player's current score in the game.
 */
private int score;

/**
 * Flag indicating whether the game is currently active.
 */
private boolean gameOn;

/**
 * Flag indicating whether the game is currently paused.
 * Transient to avoid being serialized.
 */
private transient boolean paused;

/**
 * Flag indicating whether the game state has been updated.
 * Transient to avoid being serialized.
 */
private transient boolean updated;


    /**
     * Initializes the 2D array of Tetris squares. {@link TetrisSquare}
     * 
     */
    private void initSquares() {
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 10; col++) {
                Squares[row][col] = new TetrisSquare(row * 30, col * 30);
            }
        }
    }

    /**
     * Constructs a TetrisPanel with the specified game frame.
     *
     * @param G The GameFrame instance associated with this panel.
     */
    public TetrisPanel(GameFrame G) {
        this.setSize(300, 600);
        this.squareHeight = 19;
        this.squareWidth = 9;
        this.Squares = new TetrisSquare[squareHeight + 1][squareWidth + 1];
        initSquares();
        initTrans();
        currTetro = new Tetromino(R.nextInt(7), 5, this);
        nextTetro = new Tetromino(R.nextInt(7), 5, this);
        this.gameOn = true;
        this.score = 0;
    }

    /**
     * Initializes transient fields after deserialization.
     */
    public void initTrans() {
        R = new Random();
        this.paused = false;
        this.updated = true;
        this.base = TetrisUtils.BASE_COLOR;
    }

    /**
     * Overriding the painCompononent method of the JPanel class
     * We paint a filled square for every Tetris square {@link TetrisSquare} stored in the Board
     * The squares are coloured to the {@link Color} of the corresponding Square
     * The method also draws the lines of the grid after having drawn the squares.
     * 
     * @param g The Graphics object used to render the board.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 10; col++) {
                Color currcolor = Squares[row][col].getColor();
                g.setColor(currcolor);
                g.fillRect(30 * col, 30 * row, 30, 30);
            }
        }
        g.setColor(Color.white);
        for (int col = 1; col < 10; col++) {
            g.drawLine(col * 30, 0, col * 30, 600);
        }
        for (int row = 1; row < 20; row++) {
            g.drawLine(0, row * 30, 300, row * 30);
        }
    }

    /**
     * Checks if the game is lost (when a locked square is in the top row).
     *
     * @return True if the game is lost, false otherwise.
     */
    public boolean isLost() {
        for (TetrisSquare square : this.Squares[0]) {
            if (square.isLocked()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks and clears full rows.
     * Uses the locked flag of the Squares {@link TetrisSquare} to identify the ones that are fixed in place
     * If a row is full of locked squares, it calls the deleteRow method and updates the score
     */
    public void checkRows() {
        int counter = 0;
        for (TetrisSquare[] row : this.Squares) {
            boolean full = true;
            for (TetrisSquare currSq : row) {
                if (!currSq.isLocked()) {
                    full = false;
                }
            }
            if (full) {
                deleteRow(counter);
            }
            counter++;
        }
    }

    /**
     * Deletes a full row and shifts rows above it down.
     * 
     * @param i The index of the row to delete.
     */
    public void deleteRow(int i) {
        for (int row = i; row > 0; row--) {
            for (int col = 0; col < this.squareWidth + 1; col++) {
                Squares[row][col] = Squares[row - 1][col];
                Squares[row][col].getPosition().setY(row * 30); // Update y-coordinate
            }
        }

        for (int col = 0; col < this.squareWidth + 1; col++) {
            Squares[0][col] = new TetrisSquare(0, col * 30);
        }

        score += 100; // Increase score
        this.updated = true;
    }

    /**
     * Pauses the game and stops the current Tetromino's movement.
     */
    public void pauseGame() {
        this.paused = true;
        this.currTetro.stopMovementThread();
    }

    /**
     * Resumes the game and restarts the current Tetromino's movement.
     */
    public void resumeGame() {
        this.paused = false;
        this.currTetro.startMovementThread();
    }

    /**
     * Replaces the current Tetromino with the next one and generates a new next Tetromino {@link Tetromino}.
     * It sets the updated flag to true to indicate that the game window {@link GameFrame} the {@link TetrisHUD} needs updating
     */
    public void changeTetro() {
        currTetro = nextTetro;
        nextTetro = new Tetromino(R.nextInt(7), 5, this);
        this.updated = true;
    }

    /**
     * Advances the game frame 
     * Checks for full rows every frame
     * Checks for losing condition every frame
     * Repaints the Board 
     */
    public void doFrame() {
        if (isLost()) {
            this.gameOn = false;
            return;
        }
        if (!currTetro.getActive()) {
            currTetro.activate();
        }
        checkRows();
        this.repaint();
    }

    /**
     * Moves the current Tetromino {@link Tetromino} to the left.
     */
    public void moveLeft() {
        this.currTetro.moveLeft();
    }

    /**
     * Moves the current Tetromino to the right.
     */
    public void moveRight() {
        this.currTetro.moveRight();
    }

    /**
     * Rotates the current Tetromino clockwise.
     */
    public void rotateTetro() {
        this.currTetro.rotate();
    }

    /**
     * The getter for the {@link TetrisSquare} grid of the Tetris game
     * @return The 2D array of Tetris squares.
     */
    public TetrisSquare[][] getSquares() {
        return Squares;
    }

    /**
     * Getter for the currently in play Tetro of the game
     * @return The currently active Tetromino.
     */
    public Tetromino getCurrTetro() {
        return currTetro;
    }

    /**
     * Sets the current Tetromino.
     *
     * @param T The Tetromino to set as current.
     */
    public void setCurrTetro(Tetromino T) {
        this.currTetro = T;
    }

    /**
     * Getter for the next in line Tetromino
     * @return The next Tetromino to be dropped.
     */
    public Tetromino getNextTetro() {
        return nextTetro;
    }

    /**
     * Sets the next Tetromino to be dropped.
     *
     * @param T The Tetromino to set as next.
     */
    public void setNextTetro(Tetromino T) {
        this.nextTetro = T;
    }

    /**
     * The getter for the base color for the Tetris board
     * @return The base color for unoccupied squares.
     */
    public Color getBase() {
        return base;
    }

    /**
     * The getter for the width of the Game board in squares as an integer
     * @return The width of the board in squares.
     */
    public int getSquareWidth() {
        return squareWidth;
    }

    /**
     * Getter for the height of the Game board in squares as an integer
     * @return The height of the board in squares.
     */
    public int getSquareHeight() {
        return squareHeight;
    }

    /**
     * Getter for the score of the tetris game
     * @return The player's current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter for the on-state of the game
     * @return True if the game is active, false otherwise.
     */
    public boolean isGameOn() {
        return gameOn;
    }

    /**
     * Getter for the pause-state of the game
     * @return True if the game is paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Getter for the updated flag 
     * @return True if the game state has been updated, false otherwise.
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * Marks the game state as no longer updated.
     */
    public void update() {
        this.updated = false;
    }
}
