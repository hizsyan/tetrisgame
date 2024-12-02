package tetrisgame;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CoordTest {
    private Coord defC = new Coord();
    @Test
    void testDefault(){
        assertEquals(0, defC.getX());
        assertEquals(0, defC.getY());
    }

    @Test
    void testConstructor(){
        Coord C = new Coord(10, -5);
        assertEquals(10, C.getX());
        assertEquals(-5, C.getY());
    }

    @Test
    void testArrConstructor()throws Exception{
        Coord C = new Coord(new int[]{10,60});
        assertEquals(10, C.getX());
        assertEquals(60, C.getY());
    }

    @Test
    void testSetters(){
        defC.setX(-100);
        defC.setY(50);
        assertEquals(-100, defC.getX());
        assertEquals(50, defC.getY());
    }

}
