package tetrisgame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

public class TetrisSquareTest {
    private TetrisSquare T = new TetrisSquare(5, 10);

    @Test
    void testConstructor() {
        assertEquals(5,T.getX());
        assertEquals(10, T.getY());
        assertEquals(false, T.isLocked());
    }

    @Test
    void testLock(){
        T.lock();
        assertEquals(true,T.isLocked());
    }

    @Test 
    void testCopy(){
        TetrisSquare copyT = new TetrisSquare(T);
        assertEquals(T.getPosition(), copyT.getPosition());
        assertEquals(T.getColor(), copyT.getColor());
        assertEquals(T.isLocked(), copyT.isLocked());
    }

    @Test
    void setterTest(){
        T.setX(100);
        T.setY(100);
        T.setColor(Color.PINK);
        assertEquals(100, T.getX());
        assertEquals(100, T.getY());
        assertEquals(Color.PINK, T.getColor());
    }
}
