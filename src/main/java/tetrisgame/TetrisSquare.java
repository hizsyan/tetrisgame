package tetrisgame;

import java.awt.*;
import java.io.Serializable;

/**
 * Represents a single square in the Tetris game grid.
 * Each square has a position, a color, and a bool lock state indicating if it is locked in place
 */
public class TetrisSquare implements Serializable {
    private Coord position; // The position of the square on the grid
    private Color color; // The color of the square
    private boolean locked; // Whether the square is locked (part of a completed line or Tetromino)

    /**
     * Constructs a TetrisSquare at the specified position with the base color.
     * The default state is unlocked, becuase the square is not occupied by a Tetromino when initialized
     *
     * @param xX The x-coordinate of the square.
     * @param yY The y-coordinate of the square.
     */
    public TetrisSquare(int xX, int yY) {
        this.position = new Coord(xX, yY);
        color = TetrisUtils.BASE_COLOR;
        locked = false;
    }

    /**
     * Constructs a copy of another TetrisSquare.
     *
     * @param copy The TetrisSquare to copy.
     */
    public TetrisSquare(TetrisSquare copy) {
        this.position = new Coord(copy.getX(), copy.getY());
        color = copy.getColor();
    }

    /**
     * Sets the y-coordinate of the square.
     * We define seperate setters and getters for better readability, instead of calling square.getPosition().setY()
     * @param y The new y-coordinate.
     */
    public void setY(int y) {
        this.position.setY(y);
    }

    /**
     * Gets the y-coordinate of the square.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return this.position.getY();
    }

    /**
     * Sets the x-coordinate of the square.
     *
     * @param x The new x-coordinate.
     */
    public void setX(int x) {
        this.position.setX(x);
    }

    /**
     * Gets the x-coordinate of the square.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return this.position.getX();
    }

    /**
     * Gets the color of the square. 
     * 
     * @return The current color of the square.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the square.
     *
     * @param c The new color. {@link Color}
     */
    public void setColor(Color c) {
        color = c;
    }

    /**
     * Gets the position of the square.
     *
     * @return The position as a Coord object. {@link Coord}
     */
    public Coord getPosition() {
        return this.position;
    }

    /**
     * Sets the position of the square.
     *
     * @param co The new position as a Coord object.
     */
    public void setPosition(Coord co) {
        this.position = co;
    }

    /**
     * Locks the square, indicating it is locked in place, has been placed down
     */
    public void lock() {
        this.locked = true;
    }

    /**
     * Unlocks the square
     */
    public void unlock() {
        this.locked = false;
    }

    /**
     * Checks if the square is locked.
     *
     * @return True if the square is locked, false otherwise.
     */
    public boolean isLocked() {
        return locked;
    }
}
