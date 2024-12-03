package tetrisgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Random;

/**
 * The main frame of the Tetris game. Displays the game board {@link TetrisPanel} and the HUD {@link TetrisHUD}.
 * Handles the main game thread using {@link javax.swing.Timer}, and  user input.
 * Also has a {@link Random} random number generator to randomize the {@link Tetromino} coming in
 */
public class GameFrame extends JFrame {
    private TetrisPanel TetrisBoard;
    private TetrisHUD HUD;
    private Random random;
    private javax.swing.Timer gameTimer;
    private String savePath;

    /**
     * Key listener for handling user input during the game.
     * Arrows for controlling the Tetromino, Space to push it down and Esc to pause the game.
     */
    class MyKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent k) {
            switch (k.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    TetrisBoard.currTetro.moveRight();
                    break;
                case KeyEvent.VK_LEFT:
                    TetrisBoard.currTetro.moveLeft();
                    break;
                case KeyEvent.VK_SPACE:
                    TetrisBoard.currTetro.pushDown();
                    break;
                case KeyEvent.VK_UP:
                    TetrisBoard.rotateTetro();
                    break;
                case KeyEvent.VK_ESCAPE:
                    Pause();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Gets the random number generator used by the game.
     * 
     * @return A {@link Random} instance.
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Initializes basic configurations for the game frame.
     * The basics inits are in a method of their own so they can just be called when using multiple constructors.
     */
    public void initBasic() {
        random = new Random();
        setTitle("Active Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450, 800);
        setResizable(false);
        this.savePath = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "saves";
        this.setLayout(new BorderLayout());
    }

    /**
     * Initializes and attaches the HUD to the game frame.
     * Has to be called after TetrisBoard has been initialized.
     */
    public void initHUD() {
        this.HUD = new TetrisHUD(TetrisBoard);
        this.add(HUD, BorderLayout.EAST);
        this.drawHUD();
    }

    /**
     * Constructs a new game frame with a fresh game board. {@link TetrisPanel}
     */
    public GameFrame() {
        initBasic();
        this.TetrisBoard = new TetrisPanel(this);
        this.addKeyListener(new MyKeyListener());
        this.add(TetrisBoard, BorderLayout.CENTER);
        initHUD();
    }

    /**
     * Constructs a game frame by loading a previously saved game state.
     * 
     * @param filename The name of the file containing the saved game state.
     */
    public GameFrame(String filename) {
        initBasic();
        loadGame(filename);
        this.addKeyListener(new MyKeyListener());
        this.add(TetrisBoard, BorderLayout.CENTER);
        initHUD();
    }

    /**
     * Resumes the game after being paused.
     * Restarts the gameloop controlling swing Timer.
     * Sets the board's state to active and restarts the Tetromino controlling thread.
     */
    public void resume() {
        this.gameTimer.start();
        this.TetrisBoard.resumeGame();
    }

    /**
     * Ends the game and displays a Game Over message.
     * Displays the score that the game ended on.
     */
    private void gameOver() {
        this.gameTimer.stop();

        JLabel overLabel = new JLabel("Game Over! Score: " + this.TetrisBoard.getScore(), SwingConstants.CENTER);
        overLabel.setFont(new Font("Arial", Font.BOLD, 20));
        overLabel.setOpaque(true);
        overLabel.setBackground(Color.BLACK);
        overLabel.setForeground(Color.GREEN);

        this.getContentPane().removeAll();
        this.setLayout(new BorderLayout());
        this.add(overLabel, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

    /**
     * Displays a pause menu (called from Pause after Esc key)
     * Pause menu is a seperate window {@link PauseMenu}
     */
    public void drawPauseMenu() {
        PauseMenu PM = new PauseMenu(this);
        PM.display();
    }

    /**
     * Ensures the HUD is visible and properly displayed.
     */
    public void drawHUD() {
        this.HUD.setVisible(true);
    }

    /**
     * Updates the HUD based on changes in the game state.
     * The Board {@link TetrisPanel} has an updated flag which helps us keep track
     * If there has been a change to the Score or the next Tetromino in line we need to update the HUD {@link TetrisHUD}
     * This is uself so we dont redraw the HUD on every frame.
     */
    public void updateHUD() {
        if (TetrisBoard.isUpdated()) {
            this.HUD.repaint();
            TetrisBoard.update();
        }
    }

    /**
     * Pauses the game and displays the pause menu.{@link PauseMenu}
     * The menu has options to quit, save and quit or resume
     */
    public void Pause() {
        this.gameTimer.stop();
        this.TetrisBoard.pauseGame();
        drawPauseMenu();
    }

    /**
     * Starts the game and initializes the game loop.
     * The game loop is a swing Timer {@link javax.swing.Timer} which is perfect for a swing game with real time movement
     * Using Timer makes sure everything is thread safe
     */
    public void runGame() {
        this.setVisible(true);
        gameTimer = new javax.swing.Timer(10, e -> {
            if (!this.TetrisBoard.isGameOn()) {
                this.gameOver();
                return;
            }
            TetrisBoard.doFrame();
            TetrisBoard.repaint();
            updateHUD();
        });
        gameTimer.start();
    }

    /**
     * Saves the current game state to a file.
     * Called from the Pause menu's {@link PauseMenu} save menu
     * @param filename The name of the file to save the game state to.
     */
    public void saveGame(String filename) {
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savePath + File.separator + filename))) {
            oos.writeObject(TetrisBoard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a gamestate from a file.  {@link TetrisPanel} 
     * Called in the start window's {@link StartWindow} load game button 
     * @param filename The name of the file containing the saved game state.
     */
    public void loadGame(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            TetrisPanel loadedBoard = (TetrisPanel) ois.readObject();
            this.TetrisBoard = loadedBoard;
            TetrisBoard.initTrans();
            this.TetrisBoard.resumeGame();
            this.TetrisBoard.repaint();
            this.add(TetrisBoard, BorderLayout.CENTER);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the current game board.
     * @return The {@link TetrisPanel} representing the game board.
     */
    public TetrisPanel getBoard() {
        return this.TetrisBoard;
    }

    /**
     * Gets the timer managing the game loop.
     *
     * @return The {@link javax.swing.Timer} managing the game loop.
     */
    public javax.swing.Timer getTimer() {
        return gameTimer;
    }
}
