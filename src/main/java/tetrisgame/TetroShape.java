package tetrisgame;

import java.io.Serializable;

/**
 * Represents the shape of a Tetromino in the Tetris game.
 * A TetroShape defines the relative coordinates of the blocks
 * and provides utilities for rotating and managing the shape.
 * The shape is stored as a {@link Coord} array where the coordinates represent where the Tetromino's squares are relative to its topleft square
 */
public class TetroShape implements Serializable {
    /**
     * Array of relative coordinates defining the shape
     */
    private Coord[] relative; // Array of relative coordinates defining the shape

    /**
     * Constructs a TetroShape using the index of a predefined shape.
     *
     * @param i The index of the shape (corresponding to TetrisUtils.ShapeType).
     */
    public TetroShape(int i) {
        int[][] shapeCoords = TetrisUtils.getShapeCoords(TetrisUtils.ShapeType.values()[i]);
        this.relative = new Coord[4];
        for (int j = 0; j < 4; j++) {
            this.relative[j] = new Coord(shapeCoords[j]);
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
     * Rotates the shape clockwise by transforming its relative coordinates.
     * When rotating clockwise by 90 degrees offest on the x axis becomes the same offset on the y
     * Offset on the y axis becomes the same magnitude but opposite direction offset on the x axis
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
}


