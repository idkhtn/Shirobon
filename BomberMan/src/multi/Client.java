package multi;

import game.Game;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * Klasa klienta danych do multiPlayer
 */
public class Client 
{
    private boolean error = false;
    private boolean initiated = false;
    private Processor processor = null;
    private Socket socket = null;
    public static String smap;
    public static String smon = null;
    private static boolean isResponder = false;
    public Game game;
    
    public Client(Game game, int port)
    {
        this.game = game;
        try 
        {
            socket = new Socket("localhost", port);
            if(port%2==0)
                System.out.println("Client has been connected.");
            else
            {
                isResponder = true;
                System.out.println("Client has been connected.");
            }
        }
        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(this.game.getRootPane(),"Error connecting server.");
            System.out.println("DEBUG: To-Server communication failed!");
            kill();
            this.error = true;
        }
        if(!this.error)
        {
            processor = new Processor(socket,this,isResponder);
        }
    }
    public void init()
    {
        if(!this.error)
        {
            processor.start();
        }
    }
    public final void kill()
    {
        if(processor!=null)
        {
            processor.interrupt();
        }
    }
    public Socket getSocket()
    {
        return this.socket;
    }
    public void setInitiated(boolean init)
    {
        this.initiated = init;
    }
    public boolean getInitiated()
    {
        return this.initiated;
    }
    public void changeGame(Game game)
    {
        this.game = game;
    }
}
