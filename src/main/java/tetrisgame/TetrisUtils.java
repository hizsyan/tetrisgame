package tetrisgame;

import java.awt.Color;
import java.util.HashMap;

public class TetrisUtils {

    // Enum for Tetris shapes
    public enum ShapeType {
        O, I, S, L, J, T, Z
    }

    // HashMap for Colors
    public static final HashMap<ShapeType, Color> SHAPE_COLORS = new HashMap<>();

    static {
        SHAPE_COLORS.put(ShapeType.O, Color.YELLOW);
        SHAPE_COLORS.put(ShapeType.I, new Color(0, 100, 150)); // Teal
        SHAPE_COLORS.put(ShapeType.S, new Color(50, 250, 0));  // Green
        SHAPE_COLORS.put(ShapeType.L, new Color(255, 160, 30)); // Orange
        SHAPE_COLORS.put(ShapeType.J, new Color(200, 20, 100)); // Pink
        SHAPE_COLORS.put(ShapeType.T, new Color(250, 30, 0));   // Red
        SHAPE_COLORS.put(ShapeType.Z, new Color(220, 20, 170)); // Purple
    }

    // Example Shapes Definition
    public static final int[][][] shapes = {
        { {0, 0}, {0, 1}, {1, 0}, {1, 1} },  // O
        { {0, 0}, {0, 1}, {0, 2}, {0, 3} },  // I
        { {0, 0}, {1, 0}, {-1, 1}, {0, 1} }, // S
        { {0, 0}, {0, 1}, {0, 2}, {1, 2} },  // L
        { {0, 0}, {0, 1}, {0, 2}, {-1, 2} }, // J
        { {0, 0}, {1, 0}, {2, 0}, {1, 1} },  // T
        { {0, 0}, {1, 0}, {1, 1}, {1, 2} }   // Z
    };

    // Utility method for retrieving colors
    public static Color getColorForShape(ShapeType shape) {
        return SHAPE_COLORS.get(shape);
    }

    // Utility method for retrieving shapes
    public static int[][] getShapeCoords(ShapeType shape) {
        return shapes[shape.ordinal()];
    }

    public static final Color BASE_COLOR = Color.BLACK; // Default base/background color
    public static final Color GRID_LINE_COLOR = Color.WHITE; // Grid line color
}
