package tetrisgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class firstTry extends JFrame{
    private GameFrame CurrentGame;
    JButton newgame = new JButton("New Game");
    JButton loadgame = new JButton("Load Game");
    JButton quit = new JButton("Quit");
    JPanel panel = new JPanel();

    public firstTry(){
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,110);
        setResizable(false);
        panel.add(newgame);
        panel.add(loadgame);
        panel.add(quit);
        this.add(panel);
        newgame.addActionListener(new StartButton());
    }

    public class StartButton implements ActionListener{
        public void actionPerformed(java.awt.event.ActionEvent e) {
            CurrentGame = new GameFrame();
            CurrentGame.runGame();
        }
    }
    public static void main(String[] args) {
        firstTry frame = new firstTry();

        // Make the window visible
        frame.setVisible(true);
    }
}
