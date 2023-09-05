package multi;

import game.Game;
import java.io.IOException;
import java.net.ServerSocket;  
import java.net.Socket;

/**
 * Klasa serwera - master lub Slave - w zależności kto pierwszy się podłączy.
 */
public class Server
{
    private ServerSocket s = null;
    private static Session session = null;
    private static boolean isMaster = true;
    private static boolean isResponder = false;
    public int port;
    Game game;
    public Server(Game game, int port) 
    {
        this.game = game;
        this.port = port;
        try 
        {
            
            s = new ServerSocket(port);
            if(port % 2 == 1)
            {
                isResponder=true;
                System.out.println("Client-Responder is ready to go.");
            }
            else
                System.out.println("Server is ready to go.");
        } 
        catch (IOException e) 
        {
            System.out.println("Error in creating socket section.");
            isMaster = false;
        }
    }

    public void process() {
        Socket socket;
        try 
        {
            while(session==null)
            {
                socket = s.accept();
                session = new Session(socket, this.game);
                session.start();
                System.out.println("not connected");
            }
        }
        catch (IOException e) 
        {
            System.out.println("To-Client connection failed.");
        }
        finally
        {
            try
            {
                s.close();
            }
            catch (IOException e)
            {
                System.out.println("Error in server stopping section has already occured!");
            }
        }
    }
    public Session getSession()
    {
        return this.session;
    }
    public static void sendXY(int x, int y, String vector, boolean hitted) 
    {
        if(session!=null)
        {
            (session.pOut) = new Move(x,y,vector,hitted);
            //System.out.println("server.jar->sendxy()" + x + " ");
        }
        
            //System.out.println("server.jar->sendxy()" + x + " ");
    }
    public static boolean isIsMaster() {
        return isMaster;
    }

    public static void setIsMaster(boolean aIsMaster) {
        isMaster = aIsMaster;
    }

    public int getPort() {
        return port;
    }
    public static void sendBomb(int x, int y, boolean mode) 
    {
        session.setbOut( new BombSender(x,y, mode));
    }
}
