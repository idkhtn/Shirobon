package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import game.Game;

/**
 * Klasa postawionej bomby - zawiera w sobie pośrednio eksplozję.
 */
public class Bomb 
{
    private long start;
    private long hideTime;
    private Explosion[] explosion = new Explosion[10];
    private Game game;
    private int x;
    private int y;
    private boolean visible = true;
    public Bomb(Game game, int x, int y, long startTime) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.start = startTime;
        if(Game.multi)
            this.hideTime = start+3000;
        else
            this.hideTime = start+2000+new Random().nextInt(2001);
        explosion[Game.currentExplosions] = new Explosion(game,x,y,hideTime);
    }
    public Explosion getExplosion()
    {
        return this.explosion[Game.currentExplosions];
    }
    public void update()
    {
        if(System.currentTimeMillis() > hideTime)
        {
            this.visible = false;
        }
    }
    public void draw(Graphics2D g2d)
    {
        if(System.currentTimeMillis() < hideTime)
        {
            g2d.setColor(Color.RED);
            g2d.fillOval(this.x-AbstCharacter.getMargin(), this.y-AbstCharacter.getMargin(), Game.TILE_SIZE-AbstCharacter.getMargin(), Game.TILE_SIZE-AbstCharacter.getMargin());
        }
        else
        {
            if(System.currentTimeMillis() < hideTime + Game.EXPLOSION_TIME)
            {
                explosion[Game.currentExplosions].setVisible(true);
                explosion[Game.currentExplosions].draw(g2d);
            }
            else
                explosion[Game.currentExplosions].setVisible(false);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public long getHideTime() {
        return hideTime;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setHideTime(long hideTime) {
        this.hideTime = hideTime;
    }

    public void setExplosion(Explosion[] explosion) {
        this.explosion = explosion;
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
