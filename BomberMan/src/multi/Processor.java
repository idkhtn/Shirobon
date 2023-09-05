package multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Klasa wątku dla klienta.
 */
public class Processor extends Thread
{
    private Socket socket;
    private Client client;
    private boolean initiated = false;
    private boolean mapLoaded = false;
    private boolean monsterLoaded = false;
    private boolean isResponder = false;
    
    public Processor(Socket socket, Client client, boolean isResponder) {
        this.client = client;
        //this.mode = mode;
        this.socket = socket;
    }
    @Override
    public void run()
    {
        //PrintWriter out = null;
        BufferedReader in = null;
        if(this.socket != null)
        {
            try
            {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                //out.println("MAP:"+client.game.loadMap());
                //System.out.println("MAP:"+client.game.loadMap());
                while(true)
                {String msg = in.readLine();
                    if (msg == null) 
                    {
                        break;
                    } 
                    System.out.println("DEBUG: " + msg);
                    if(!initiated && msg.equals(":INITIATE:"))
                    {
                        //client.setID(Integer.parseInt(msg.replace("INITIATE:", "")));
                        client.setInitiated(true);
                        initiated = true;
                    }
                    //System.out.println("DEBUG: I'm ID: " + client.getID() + " and i'm is talking.");
                    if(!mapLoaded && msg.startsWith(":MAP:"))
                    {
                        System.out.println("MAP HAS BEEN LOADED");
                        Client.smap = msg.replace(":MAP:", "");
                        mapLoaded = true;
                    }
                    if(!monsterLoaded && msg.startsWith(":MONSTER:"))
                    {
                        System.out.println("MONSTER HAS BEEN LOADED");
                        Client.smon = msg.replace(":MONSTER:", "");
                        mapLoaded = true;
                    }
                    if(msg.startsWith(":P:"))
                    {
                        String setter = (msg.replace(":P:", ""));
                        client.game.getPlayer2().setX(Integer.parseInt(setter.substring(0, 3)));
                        client.game.getPlayer2().setY(Integer.parseInt(setter.substring(4, 7)));
                        switch(setter.substring(8, 9))
                        {
                            case "u" -> client.game.getPlayer2().setVector("up");
                            case "d" -> client.game.getPlayer2().setVector("down");
                            case "l" -> client.game.getPlayer2().setVector("left");
                            case "r" -> client.game.getPlayer2().setVector("right");
                        }
                    }
                    if(msg.startsWith(":P2:"))
                    {
                        String setter = (msg.replace(":P2:", ""));
                        client.game.getPlayer2().setX(Integer.parseInt(setter.substring(0, 3)));
                        client.game.getPlayer2().setY(Integer.parseInt(setter.substring(4, 7)));
                        switch(setter.substring(8, 9))
                        {
                            case "u" -> client.game.getPlayer2().setVector("up");
                            case "d" -> client.game.getPlayer2().setVector("down");
                            case "l" -> client.game.getPlayer2().setVector("left");
                            case "r" -> client.game.getPlayer2().setVector("right");
                        }
                    }
                    if(client.game.getMode()==2)
                        if(msg.startsWith(":B:"))
                        {
                            String setter = (msg.replace(":B:", ""));
                            client.game.plantBomb(Integer.parseInt(setter.substring(0, 3)), Integer.parseInt(setter.substring(4, 7)));
                        }
                    if(client.game.getMode()==1)
                        if(msg.startsWith(":B2:"))
                        {
                            String setter = (msg.replace(":B2:", ""));
                            client.game.plantBomb(Integer.parseInt(setter.substring(0, 3)), Integer.parseInt(setter.substring(4, 7)));
                        }
                }
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
            finally
            {
                try 
                {
                    in.close();
                    socket.close();
                } 
                catch (IOException e) 
                {
                    System.out.println("Closing connection exception occured!");
                }
            }
        }
    }
}
