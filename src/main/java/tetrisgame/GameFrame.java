package tetrisgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class GameFrame extends JFrame{
    private TetrisPanel TetrisBoard;
    private TetrisHUD HUD;
    private Random random;
    private javax.swing.Timer gameTimer;
    private transient String savePath;

    class MyKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent k){
            switch (k.getKeyCode()){
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

    public Random getRandom(){
        return random;
    }

    public GameFrame(){
        random = new Random();
        setTitle("Active Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450,800);
        setResizable(false);
        this.TetrisBoard = new TetrisPanel(this);
        this.addKeyListener(new MyKeyListener());
        this.setLayout(new BorderLayout());
        this.add(TetrisBoard, BorderLayout.CENTER); // Add the Tetris game board to the center
        this.HUD = new TetrisHUD(TetrisBoard);
        this.add(HUD, BorderLayout.EAST); // Add the HUD to the right side (EAST)
        this.drawHUD();
        this.setVisible(true);
        this.savePath = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "saves";
    }

    public void resume(){
        this.gameTimer.start();
        this.TetrisBoard.resumeGame();
    }

    private void gameOver(){
     // Stop the game timer
        this.gameTimer.stop();

     // Create Game Over message
        JLabel overLabel = new JLabel("Game Over! Score: " + this.TetrisBoard.getScore(), SwingConstants.CENTER);
        overLabel.setFont(new Font("Arial", Font.BOLD, 20));
        overLabel.setOpaque(true);
        overLabel.setBackground(Color.BLACK);
        overLabel.setForeground(Color.GREEN);
 
     // Clear existing components and set layout
        this.getContentPane().removeAll(); // Remove all components from the JFrame
        this.setLayout(new BorderLayout()); // Set layout for new components
 
     // Add the Game Over label
        this.add(overLabel, BorderLayout.CENTER);
 
     // Revalidate and repaint to refresh the frame
        this.revalidate();
        this.repaint();
 }

    public void drawPauseMenu(){
        pauseMenu PM = new pauseMenu(this);
        PM.display();
    }

    public void drawHUD(){
        this.HUD.setVisible(true);
    }

    public void updateHUD() {
        if(TetrisBoard.isUpdated()){
            this.HUD.repaint();  // Repaint the HUD to display the updates
            TetrisBoard.update();
        }
    }

    public void Pause(){
        this.gameTimer.stop();
        this.TetrisBoard.pauseGame();
        drawPauseMenu();
    }

    public void runGame(){
        this.setVisible(true);
        gameTimer = new javax.swing.Timer(10, e -> {
            if(!this.TetrisBoard.isGameOn()){
                this.gameOver();
                return;
            }
            TetrisBoard.doFrame(); // Update game logic
            TetrisBoard.repaint(); // Redraw the game board
            updateHUD();
        });
        gameTimer.start();
    }

    public void saveGame(String filename) {
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs(); // Create the folder if it doesn't exist
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(TetrisBoard); // Serialize the game board
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            TetrisPanel loadedBoard = (TetrisPanel) ois.readObject();
            this.TetrisBoard = loadedBoard;
            this.TetrisBoard.resumeGame(); // Restart the game thread
            this.TetrisBoard.repaint(); // Redraw the board
            this.add(TetrisBoard, BorderLayout.CENTER); // Reattach the board
            System.out.println("Game loaded successfully.");
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TetrisPanel getBoard(){
        return this.TetrisBoard;
    }

}
