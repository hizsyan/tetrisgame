package tetrisgame;

import java.io.Serializable;

/**
 * Represents the shape of a Tetromino in the Tetris game.
 * A TetroShape defines the relative coordinates of the blocks
 * and provides utilities for rotating and managing the shape.
 */
public class TetroShape implements Serializable {
    private Coord[] relative; // Array of relative coordinates defining the shape
    private boolean[][] absolute; // Absolute representation of the shape (optional usage)

    /**
     * Constructs a TetroShape using the index of a predefined shape.
     *
     * @param i The index of the shape (corresponding to TetrisUtils.ShapeType).
     */
    public TetroShape(int i) {
        int[][] shapeCoords = TetrisUtils.getShapeCoords(TetrisUtils.ShapeType.values()[i]);
        this.relative = new Coord[4];
        for (int j = 0; j < 4; j++) {
            try {
                this.relative[j] = new Coord(shapeCoords[j]);
            } catch (Exception e) {
                // Handle exception gracefully (shouldn't occur with valid shape definitions)
            }
        }
    }

    /**
     * Copy constructor that creates a new TetroShape from an existing one.
     *
     * @param copied The TetroShape to copy.
     */
    public TetroShape(TetroShape copied) {
        this.relative = new Coord[copied.relative.length];
        for (int j = 0; j < copied.relative.length; j++) {
            this.relative[j] = new Coord(copied.relative[j].getX(), copied.relative[j].getY());
        }
    }

    /**
     * Calculates the top-left corner of the absolute representation of the shape.
     *
     * @return A Coord object representing the top-left position.
     */
    private Coord calcTopLeft() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (this.absolute[row][col]) {
                    return new Coord(row, col);
                }
            }
        }
        return new Coord(-1, -1); // Default if no valid top-left is found
    }

    /**
     * Recalculates the relative coordinates of the shape based on the top-left corner.
     */
    private void calcRelative() {
        Coord topleft = calcTopLeft();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                Coord current = new Coord(row, col);
                if (absolute[row][col] && !topleft.equals(current)) {
                    this.relative[this.relative.length - 1] = current;
                }
            }
        }
    }

    /**
     * Rotates the shape clockwise by transforming its relative coordinates.
     */
    public void rotateClock() {
        Coord[] newcoords = new Coord[4];
        for (int curr = 0; curr < 4; curr++) {
            Coord c = this.relative[curr];
            Coord make = new Coord(-c.getY(), c.getX());
            newcoords[curr] = make;
        }
        this.relative = newcoords;
    }

    /**
     * Retrieves the relative coordinates of the shape.
     *
     * @return An array of Coord objects representing the relative positions.
     */
    public Coord[] getRelative() {
        return relative;
    }

    /**
     * Retrieves the absolute representation of the shape.
     *
     * @return A 2D boolean array where true indicates a filled block.
     */
    public boolean[][] getAbsolute() {
        return absolute;
    }
}
