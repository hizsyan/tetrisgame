package tetrisgame;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TetroShapeTest {
    private TetroShape tShape = new TetroShape(0);
    @Test
    void testConstructor(){
        assertEquals(new Coord(0,0), tShape.getRelative()[0]);
        assertEquals(new Coord(0,1), tShape.getRelative()[1]);
        assertEquals(new Coord(1,0), tShape.getRelative()[2]);
        assertEquals(new Coord(1,1), tShape.getRelative()[3]);
    }

    @Test
    void testCopy(){
        TetroShape copy = new TetroShape(tShape);
        for(int i = 0; i<4;i++){
            assertEquals(tShape.getRelative()[i], copy.getRelative()[i]);
        }
    }

    @Test
    void testRotate(){
        
    }
}
