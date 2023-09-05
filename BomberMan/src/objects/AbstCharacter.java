package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import game.Driver;
import game.Game;

/**
 * Klasa abstrakcyjna postaci (gracz, potwór).
 */
public abstract class AbstCharacter {

    private boolean alive = true;
    private static int margin = 16;
    private int x;
    private int y;
    private int s; 
    private String vector;
    private boolean hitted;
    public Rectangle hard;
    private int defaultHardX;
    private int defaultHardY;
    protected Game game;
    protected Driver keyDriver;

    public AbstCharacter(Game game) {
        // kalibracja rysowania postaci
        this.hard = new Rectangle(-18,-18,33,35);
        defaultHardX = hard.x;
        defaultHardY = hard.y;
        this.game = game;
        s = 3;
        vector = "down";
        x = (int) (Game.TILE_SIZE*1.5);
        y = (int) (Game.TILE_SIZE*1.5);
    }
    public AbstCharacter(Game game, Driver keyDriver) {
        this(game);
        this.hard = new Rectangle(-18,-18,33,35);
        this.keyDriver = keyDriver;
    }
    
    public void draw(Graphics2D g2d)
    {
        if(isAlive())
        {
            switch(getVector())
            {

                case "up" -> Up(g2d);
                case "down" -> Down(g2d);
                case "left" -> Left(g2d);
                case "right" -> Right(g2d);
            }
        }
    }
    
    /**
     * @return the alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * @param alive the alive to set
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * @return the margin
     */
    public static int getMargin() {
        return margin;
    }

    /**
     * @param aMargin the margin to set
     */
    public static void setMargin(int aMargin) {
        margin = aMargin;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the s
     */
    public int getS() {
        return s;
    }

    /**
     * @param s the s to set
     */
    public void setS(int s) {
        this.s = s;
    }

    /**
     * @return the vector
     */
    public String getVector() {
        return vector;
    }

    /**
     * @param vector the vector to set
     */
    public void setVector(String vector) {
        this.vector = vector;
    }

    /**
     * @return the hitted
     */
    public boolean isHitted() {
        return hitted;
    }

    /**
     * @param hitted the hitted to set
     */
    public void setHitted(boolean hitted) {
        this.hitted = hitted;
    }

    /**
     * @return the hard
     */
    public Rectangle getHard() {
        return hard;
    }

    /**
     * @param hard the hard to set
     */
    public void setHard(Rectangle hard) {
        this.hard = hard;
    }

    /**
     * @return the defaultHardX
     */
    public int getDefaultHardX() {
        return defaultHardX;
    }

    /**
     * @param defaultHardX the defaultHardX to set
     */
    public void setDefaultHardX(int defaultHardX) {
        this.defaultHardX = defaultHardX;
    }

    /**
     * @return the defaultHardY
     */
    public int getDefaultHardY() {
        return defaultHardY;
    }

    /**
     * @param defaultHardY the defaultHardY to set
     */
    public void setDefaultHardY(int defaultHardY) {
        this.defaultHardY = defaultHardY;
    }
    abstract void update();
    
    abstract void Up(Graphics2D g2d);

    abstract void Down(Graphics2D g2d);

    abstract void Left(Graphics2D g2d);

    abstract void Right(Graphics2D g2d);
}
