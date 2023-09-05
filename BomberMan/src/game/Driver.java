package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *Klasa do obsługi zdarzeń. Przycisku klawiatury.
 */
public class Driver implements KeyListener {
    
    /**
     *kierunki poruszania się postaci
     */
    public boolean up,down,left,right;

    /**
     * Czy ruch postaci ma być wykonany
     */
    public static boolean active = false;

    /**
     * obiektem jest gra, aby zyskać dostęp do zasobów współdzielonych.
     */
    private final Game game;

    /**
     * konstruktor klasy Sterownika.
     * @param game parametrem jest gra, aby zyskać dostęp do zasobów współdzielonych.
     */
    public Driver(Game game) {
        this.game = game;
    }
    
    /**
     * Nie używany
     * @param e Nie używany
     */
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    /**
     * Zdarzenie do poruszania się postacią, pauzy i wyjścia, a także stawiania bomby.
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_W)
        {
            active = true;
            up = true;
        }
        if(key == KeyEvent.VK_S)
        {
            active = true;
            down = true;
        }
        if(key == KeyEvent.VK_A)
        {
            active = true;
            left = true;
        }
        if(key == KeyEvent.VK_D)
        {
            active = true;
            right = true;
        }
        if(key == KeyEvent.VK_P)
        {
            if(Game.GAME_STATUS == Game.PLAY)
                Game.GAME_STATUS = Game.PAUSE;
            else if(Game.GAME_STATUS == Game.PAUSE)
                Game.GAME_STATUS = Game.PLAY;
        }
        if(key == KeyEvent.VK_SPACE)
        {
            //DEBUG System.out.println("x:" + Game.player.x + "y:" + Game.player.y + " Col:" + (Game.player.x)/Game.tileSize );
            //new Bomb(game,Game.player.x,Game.player.y);
            game.plantBomb(((int)(game.getPlayer().getX()/Game.TILE_SIZE)*Game.TILE_SIZE + 24),((int)(game.getPlayer().getY()/Game.TILE_SIZE)*Game.TILE_SIZE +24));
            if(Game.multi)
                game.plantNetworkBomb(((int)(game.getPlayer().getX()/Game.TILE_SIZE)*Game.TILE_SIZE + 24),((int)(game.getPlayer().getY()/Game.TILE_SIZE)*Game.TILE_SIZE +24));
        }
    }

    /**
     * Metoda zwalniajaca wiiśniecie 
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        active = false;
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_W)
        {
            up = false;
        }
        if(key == KeyEvent.VK_S)
        {
            down = false;
        }
        if(key == KeyEvent.VK_A)
        {
            left = false;
        }
        if(key == KeyEvent.VK_D)
        {
            right = false;
        }
    }
    
}
