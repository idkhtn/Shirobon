package multi;

import game.Game;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import main.Engine;

/**
 * Klasa wątków dla serwera.
 */
class Session extends Thread
{
    private final Game game;
    private boolean initiated = false;
    private boolean mapLoaded = false;
    private boolean monsterLoaded = false;
    private Socket socket = null;
    Move pOut;
    Move lastPOut = new Move(0,0,"up",false);
    BombSender bOut;
    BombSender lastBOut = new BombSender(0,0,false);
    public Session(Socket socket, Game game) 
    {
        this.socket = socket;
        this.game = game;
    }
    @Override
    public void run()
    {
    PrintWriter out = null;
        try
        {
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            if(!initiated)
            {  
                out.println(":INITIATE:");
                initiated = true;
            }
            double paintTime = 1000000000/Engine.FPS;
            double delta = 0;
            long last = System.nanoTime();
            long now;
            long i = 0;
            int frames = 0;
            while(true) 
            {   
                now = System.nanoTime();
                delta += (now - last) / paintTime;
                i += (now - last);
                last = now;
                // Debug: System.out.println("running!" + i++);
                if(delta>=1)
                {
                    
                 if(!mapLoaded)
                 {
                     out.println(":MAP:"+game.getsMap());
                     mapLoaded = true;
                     try 
                     {
                         sleep(100);
                     } 
                     catch (InterruptedException ex) {
                         System.out.println(ex);
                     }
                     continue;
                 }
                 if(!monsterLoaded)
                 {
                     out.println(":MONSTER:"+game.getsMonster());
                     monsterLoaded = true;
                 }
                 if(!pOut.toString().equals(lastPOut.toString()))
                 {
                     out.println(pOut.toString());
                     lastPOut = pOut;
                 }
                 if(bOut!=null &&(!bOut.toString().equals(lastBOut.toString())))
                 {
                     out.println(bOut.toString());
                     lastBOut = bOut;
                 }
                delta--;
                    frames++;
                }
                if(i >=1000000000)
                {
                    //Debug: System.out.println("FPS: " + frames);
                    i = 0;
                    frames = 0;
                }
            }
        } 
        catch (IOException e) 
        {
            System.out.println("To-Client communication error occured!");
        } 
        finally 
        {
            try 
            {
                out.close();
                socket.close();
            } 
            catch (IOException e) 
            {
                System.out.println("Closing connection exception occured!");
            }
        }
    }

    public void setbOut(BombSender bOut) {
        this.bOut = bOut;
    }

    public void setLastBOut(BombSender lastBOut) {
        this.lastBOut = lastBOut;
    }
}
