package main;

import game.Game;
import multi.Server;

/**
 * Klasa silnika gry - głównej pętli.
 */
public class Engine extends Thread
{
    private final Game frame;
    private static volatile boolean exit = false;
    public final static int FPS = 25;
    public Engine(Game frame) {
        this.frame = frame;
    }
    @Override
    public void run() {
    exit = false;
    double paintTime = 1000000000/FPS;
    double delta = 0;
    long last = System.nanoTime();
    long now;
    long i = 0;
    int frames = 0;
    //DEBUG:     long i = 0;
    while(!exit && !this.isInterrupted())
    { 
        now = System.nanoTime();
        delta += (now - last) / paintTime;
        i += (now - last);
        last = now;
        // Debug: System.out.println("running!" + i++);
        if(delta>=1)
        {
            frame.update();
            
            if(Game.multi)
            {
                if(frame.getMode() > 1 )
                    Server.sendXY(frame.getPlayer().getX(), frame.getPlayer().getY(), frame.getPlayer().getVector(), frame.getPlayer().isHitted());
                else
                    Server.sendXY(frame.getPlayer().getX(), frame.getPlayer().getY(), frame.getPlayer().getVector(), frame.getPlayer().isHitted());
               //Server.sendXY(frame.bomb., FPS, vector, exit);
            }
            frame.repaint();
            delta--;
            frames++;
        }
        if(i >=1000000000)
        {
            //Debug: System.out.println("FPS: " + frames);
            i = 0;
            frames = 0;
        }
        // DEBUG: System.out.println(this.getName() + " " + this.getState());
        //try
        //{
            //sleep(20);
        //}
        //catch (InterruptedException e) {
        //System.out.println(e);
        //exit = true;
        }
    //}
    }
    public void setThread()
    {
        exit = !exit;
    }
}
