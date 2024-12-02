package tetrisgame;

import java.awt.Color;
import java.util.HashMap;

/**
 * Utility class for managing Tetris game constants and utility methods.
 * This class includes color mappings for shapes, shape definitions, and other game constants.
 */
public class TetrisUtils {

    /**
     * Enum representing the types of Tetris shapes.
     */
    public enum ShapeType {
        O, I, S, L, J, T, Z
    }

    /**
     * HashMap for storing colors corresponding to each Tetris shape type.
     */
    public static final HashMap<ShapeType, Color> SHAPE_COLORS = new HashMap<>();

    // Static initializer block for setting up shape colors
    static {
        SHAPE_COLORS.put(ShapeType.O, Color.YELLOW);
        SHAPE_COLORS.put(ShapeType.I, new Color(0, 100, 150)); // Teal
        SHAPE_COLORS.put(ShapeType.S, new Color(50, 250, 0));  // Green
        SHAPE_COLORS.put(ShapeType.L, new Color(255, 160, 30)); // Orange
        SHAPE_COLORS.put(ShapeType.J, new Color(200, 20, 100)); // Pink
        SHAPE_COLORS.put(ShapeType.T, new Color(250, 30, 0));   // Red
        SHAPE_COLORS.put(ShapeType.Z, new Color(220, 20, 170)); // Purple
    }

    /**
     * Array containing the relative coordinates of each shape type.
     * Each shape is defined as an array of 2D coordinates relative to its origin.
     */
    public static final int[][][] shapes = {
        { {0, 0}, {0, 1}, {1, 0}, {1, 1} },  // O
        { {0, 0}, {0, 1}, {0, 2}, {0, 3} },  // I
        { {0, 0}, {1, 0}, {-1, 1}, {0, 1} }, // S
        { {0, 0}, {0, 1}, {0, 2}, {1, 2} },  // L
        { {0, 0}, {0, 1}, {0, 2}, {-1, 2} }, // J
        { {0, 0}, {1, 0}, {2, 0}, {1, 1} },  // T
        { {0, 0}, {1, 0}, {1, 1}, {1, 2} }   // Z
    };

    /**
     * Retrieves the color associated with a given Tetris shape type.
     *
     * @param shape The shape type whose color is to be retrieved.
     * @return The color associated with the specified shape type.
     */
    public static Color getColorForShape(ShapeType shape) {
        return SHAPE_COLORS.get(shape);
    }

    /**
     * Retrieves the coordinates of the blocks forming a given Tetris shape.
     *
     * @param shape The shape type whose coordinates are to be retrieved.
     * @return A 2D array representing the relative coordinates of the shape's blocks.
     */
    public static int[][] getShapeCoords(ShapeType shape) {
        return shapes[shape.ordinal()];
    }

    /**
     * The default base/background color for the Tetris grid.
     */
    public static final Color BASE_COLOR = Color.BLACK;

    /**
     * The color of the grid lines on the Tetris board.
     */
    public static final Color GRID_LINE_COLOR = Color.WHITE;
}
