package tetrisgame;

import java.io.Serializable;

/**
 * Represents a coordinate point in a 2D space with x and y values.
 */
public class Coord implements Serializable {
    private int x;
    private int y;

    /**
     * Creates a coordinate point with specified x and y values.
     *
     * @param xX The x-coordinate.
     * @param yY The y-coordinate.
     */
    public Coord(int xX, int yY) {
        x = xX;
        y = yY;
    }

    /**
     * Creates a coordinate point from an array of two integers.
     *
     * @param vals An array containing x and y values.
     * @throws IllegalArgumentException If the array does not contain exactly two values.
    */
    public Coord(int[] vals) {
        if (vals == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
        if (vals.length != 2) {
            throw new IllegalArgumentException("Input array must contain exactly two values");
        }
        x = vals[0];
        y = vals[1];
    }
    
    /**
     * Creates a coordinate point with default values (0, 0).
     */
    public Coord() {
        x = 0;
        y = 0;
    }

    /**
     * Gets the x-coordinate.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the x-coordinate.
     *
     * @param X The new x-coordinate value.
     */
    public void setX(int X) {
        x = X;
    }

    /**
     * Sets the y-coordinate.
     *
     * @param Y The new y-coordinate value.
     */
    public void setY(int Y) {
        y = Y;
    }

    /**
     * Adjusts this coordinate to be relative to another coordinate.
     *
     * @param origin The origin coordinate to use as a reference.
     */
    public void makeRelative(Coord origin) {
        this.x -= origin.x;
        this.y -= origin.y;
    }

    /**
     * Swaps the x and y values of this coordinate.
     */
    public void flip() {
        int buffy = this.y;
        this.y = this.x;
        this.x = buffy;
    }

    /**
     * Converts this coordinate to an array representation.
     *
     * @return An array with the x and y values.
     */
    public int[] toArray() {
        return new int[] { this.x, this.y };
    }

    /**
     * Adds another coordinate's values to this coordinate.
     *
     * @param xy The coordinate to add.
     */
    public void add(Coord xy) {
        this.x += xy.getX();
        this.y += xy.getY();
    }

    /**
     * Increases the x-coordinate by the specified offset.
     *
     * @param off The amount to increase the x-coordinate.
     */
    public void pushX(int off) {
        this.x += off;
    }

    /**
     * Increases the y-coordinate by the specified offset.
     *
     * @param off The amount to increase the y-coordinate.
     */
    public void pushY(int off) {
        this.y += off;
    }

    /**
     * Compares this coordinate with another object for equality.
     *
     * @param o The object to compare.
     * @return True if the object is a Coord with the same x and y values; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coord otherCoord = (Coord) o;
        return x == otherCoord.getX() && y == otherCoord.getY();
    }
}
