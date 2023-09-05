package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import game.Game;
import static game.Game.GAME_STATUS;
import static game.Game.PLAY;

/**
 * Klasa eksplozji do obsługi wybuchów.
 */
public class Explosion {
    private static int size = 1;

    public static int getSize() {
        return size;
    }

    public static void setSize(int aSize) {
        size = aSize;
    }
    private long start;
    private final Game game;
    private int x;
    private int y;
    private boolean visible = false;
    public Explosion(Game game, int x, int y, long start) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.start = start;
    }
    public void update()
    {
        if(GAME_STATUS == PLAY)
        {
            if(System.currentTimeMillis() > start + Game.EXPLOSION_TIME)
                this.visible = false;
        }
    }
    public void draw(Graphics2D g2d)
    {
        game.getCc().checkBreak(this);
        if(System.currentTimeMillis() < start + Game.EXPLOSION_TIME)
        {
            g2d.setColor(Color.ORANGE);
            g2d.fillOval(this.x-AbstCharacter.getMargin(), this.y-AbstCharacter.getMargin(), Game.TILE_SIZE-AbstCharacter.getMargin(), Game.TILE_SIZE-AbstCharacter.getMargin());
            for (int i = 1; i <= size; i++) 
            {
                g2d.fillOval(this.x-AbstCharacter.getMargin()+(Game.TILE_SIZE*i), this.y-AbstCharacter.getMargin(), Game.TILE_SIZE-AbstCharacter.getMargin(), Game.TILE_SIZE-AbstCharacter.getMargin());
                g2d.fillOval(this.x-AbstCharacter.getMargin()-(Game.TILE_SIZE*i), this.y-AbstCharacter.getMargin(), Game.TILE_SIZE-AbstCharacter.getMargin(), Game.TILE_SIZE-AbstCharacter.getMargin());
                g2d.fillOval(this.x-AbstCharacter.getMargin(), this.y-AbstCharacter.getMargin()+(Game.TILE_SIZE*i), Game.TILE_SIZE-AbstCharacter.getMargin(), Game.TILE_SIZE-AbstCharacter.getMargin());
                g2d.fillOval(this.x-AbstCharacter.getMargin(), this.y-AbstCharacter.getMargin()-(Game.TILE_SIZE*i), Game.TILE_SIZE-AbstCharacter.getMargin(), Game.TILE_SIZE-AbstCharacter.getMargin());
            }
        }
    } 

    public long getStart() {
        return start;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
