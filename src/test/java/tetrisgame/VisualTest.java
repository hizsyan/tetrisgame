package tetrisgame;
import org.junit.jupiter.api.Test;
import java.awt.event.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import javax.swing.*;

import org.junit.jupiter.api.BeforeAll;

public class VisualTest{

    public void waitForEDT() {
        try {
            SwingUtilities.invokeAndWait(() -> {}); // Submit a no-op task to the EDT
        } catch (Exception e) {
            throw new RuntimeException("Failed to synchronize with EDT", e);
        }
    }

    @BeforeAll
    public static void setupHeadlessMode() {
        System.setProperty("java.awt.headless", "true");
    }

    void testTetrisPanelInitialization() {
        TetrisPanel panel = new TetrisPanel(new GameFrame());

        // Check dimensions
        assertEquals(300, panel.getWidth());
        assertEquals(600, panel.getHeight());

        // Check square initialization
        TetrisSquare[][] squares = panel.getSquares();
        assertNotNull(squares);
        assertEquals(20, squares.length);
        assertEquals(10, squares[0].length);
        assertNotNull(squares[0][0]);

        // Check base color
        assertEquals(TetrisUtils.BASE_COLOR, panel.getBase());
    }

    @Test
    void testPauseMenuResumeButton() {
        GameFrame gameFrame = new GameFrame();
        PauseMenu menu = new PauseMenu(gameFrame);
        gameFrame.runGame();
        gameFrame.getBoard().pauseGame();
        JPanel sPanel = new JPanel();
        for(Component P : menu.getContentPane().getComponents()){
            if(P instanceof JPanel){
                sPanel = (JPanel) P;
            }
        }
        for (Component C:sPanel.getComponents()){
            if(C instanceof JButton){
                JButton JB = (JButton) C;
                if(JB.getText().equals("Resume")){
                    JB.doClick();
                    System.out.println("RESUMECLICKED");
                }
            }
        }
        // Verify that the game has resumed
        assertFalse(gameFrame.getBoard().isPaused());
    }

    @Test
    void testTetrominoMoveDown() {
        GameFrame G = new GameFrame();
        TetrisPanel panel = new TetrisPanel(G);
        Tetromino tetromino = panel.getCurrTetro();

        // Initial position
        int initialY = tetromino.getTopleft().getY();

        // Simulate move down
        tetromino.moveDown();

        this.waitForEDT();
        
        // Verify position has updated
        assertEquals(initialY + 1, tetromino.getTopleft().getY());
    }

    @Test
    void testMoveRight(){
        GameFrame gameFrame = new GameFrame();
        TetrisPanel panel = gameFrame.getBoard();
        Tetromino tetromino = panel.getCurrTetro();

        // Get initial position
        int initialX = tetromino.getTopleft().getX();

        // Simulate pressing the right arrow key
        tetromino.moveRight();

        // Wait for the event to be processed
        this.waitForEDT();

        // Verify tetromino moved right
        assertEquals(initialX + 1, tetromino.getTopleft().getX());
    }

    @Test
    void testMoveLeft(){
        GameFrame gameFrame = new GameFrame();
        TetrisPanel panel = gameFrame.getBoard();
        Tetromino tetromino = panel.getCurrTetro();

        // Get initial position
        int initialX = tetromino.getTopleft().getX();

        // Simulate pressing the right arrow key
        tetromino.moveLeft();

        // Wait for the event to be processed
        this.waitForEDT();

        // Verify tetromino moved right
        assertEquals(initialX - 1, tetromino.getTopleft().getX());
    }


}

