package tetrisgame;

import java.io.Serializable;

/**
 * Represents a coordinate point on a grid
 * @author Hizsnyan Bálint
 */
public class Coord implements Serializable {
    private int x;
    private int y;

    /**
     * Creates a coordinate point with specified x and y values.
     * Both x and y are integers as we are on a grid, they correspond to column and row
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
     * This allows for increased compatibility with other code that might use a simple integer array to store grid coordinates.
     * We just have to make sure the array has exactly two values.
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
     * Exists so we can use a default constructor and later set the coordinates.
     */
    public Coord() {
        x = 0;
        y = 0;
    }

    /**
     * Makes the private x-coordinate gettable from the outside
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Makes the private y-coordinate gettable from the outside
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the x-coordinate to a specified value
     *
     * @param X The new x-coordinate value.
     */
    public void setX(int X) {
        x = X;
    }

    /**
     * Sets the y-coordinate to a specified value
     *
     * @param Y The new y-coordinate value.
     */
    public void setY(int Y) {
        y = Y;
    }

    /**
     * Adjusts this coordinate to be relative to another coordinate.
     * Could be useful for changing the origin of our grid, calculating distance etc.
     *
     * @param origin The origin coordinate to use as a reference.
     */
    public void makeRelative(Coord origin) {
        this.x -= origin.x;
        this.y -= origin.y;
    }

    /**
     * Swaps the x and y values of this coordinate.
     * Allows for smoother rotation, mirroring etc. operations
     */
    public void flip() {
        int buffy = this.y;
        this.y = this.x;
        this.x = buffy;
    }

    /**
     * Converts this coordinate to an array representation.
     * Exists to improve compatibility with integer array based grid coordinate representation.
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
     * Useful so when moving the point in the given direction, so we don't have to use setX(getX()+offset).
     * @param off The amount to increase the x-coordinate.
     */
    public void pushX(int off) {
        this.x += off;
    }

    /**
     * Increases the y-coordinate by the specified offset.
     * Similiarly to pushX
     * @param off The amount to increase the y-coordinate.
     */
    public void pushY(int off) {
        this.y += off;
    }

    /**
     * Compares this coordinate with another object for equality.
     * Useful for testing Coord equality without manually comparing both x and y.
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
