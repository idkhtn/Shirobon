package main;
import game.Game;
import menu.Menu;
import javax.swing.JFrame;

/**
 * Główna klasa Main aplikacji
 */
public class Main {
    private final JFrame mainFrame;
    public Main()
    {
        mainFrame = new Menu(this);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setSize(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        mainFrame.setTitle("BOMBERMAN");
        mainFrame.pack();
    }
    public final JFrame getMainFrame(){
        return mainFrame;
    }
    public static void main(String[] args) 
    {
        Closer main = new Closer();
    }
}