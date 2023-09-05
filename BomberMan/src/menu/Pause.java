package menu;

import game.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Klasa z napisem "Game Paused".
 */
public class Pause {
    Game game;

    public Pause(Game game) {
        this.game = game;
    }
    public void draw(Graphics2D g2d)
    {
        g2d.setColor(Color.white);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 60F));
        
        g2d.drawString("GAME PAUSED", Game.SCREEN_WIDTH/2-200, Game.SCREEN_HEIGHT/2);
    }
}
