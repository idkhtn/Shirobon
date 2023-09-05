package objects;

import game.Driver;
import game.Game;
import java.awt.Color;
import java.awt.Graphics2D;
import static java.lang.Thread.sleep;
import menu.Menu;

/**
 * Klasa postaci - rozwija klasę abstrakcyjną AbstCharacter.
 */
public class Player extends AbstCharacter 
{
    private boolean shadow = false;
    public Player( Game game )
    {
        super(game);
    }
    public Player( Game game , boolean shadow)
    {
        super(game);
        this.shadow=shadow;
    }
    public Player( Game game, Driver keyDriver )
    {
        super(game, keyDriver);
    }
    /**
     *
     * @param tmp
     */
    @Override
    public void Left(Graphics2D tmp)
    {
        
	tmp.setColor(Color.RED);
	tmp.fillOval(this.getX()-getMargin()+11, this.getY()-getMargin()-3, 24, 24);
        Body(tmp);
        
	tmp.setColor(Color.BLACK);
	tmp.drawLine(this.getX()-getMargin()+7, this.getY()-getMargin()+10, this.getX()-getMargin()+7, this.getY()-getMargin()+18);
	tmp.drawLine(this.getX()-getMargin()+14, this.getY()-getMargin()+10, this.getX()-getMargin()+14, this.getY()-getMargin()+18);
    }

    /**
     *
     * @param tmp 
     */
    @Override
    public void Down(Graphics2D tmp)
    {
        
	tmp.setColor(Color.RED);
	tmp.fillOval(this.getX()-getMargin()+9, this.getY()-getMargin()-3, 24, 24);
        
        Body(tmp);
        
	tmp.setColor(Color.BLACK);
	tmp.drawLine(this.getX()-getMargin()+11, this.getY()-getMargin()+10, this.getX()-getMargin()+11, this.getY()-getMargin()+18);
	tmp.drawLine(this.getX()-getMargin()+21, this.getY()-getMargin()+10, this.getX()-getMargin()+21, this.getY()-getMargin()+18);
    }

    /**
     *
     * @param tmp
     */
    @Override
    public void Up(Graphics2D tmp)
    {
        Body(tmp);
        
	tmp.setColor(Color.RED);
	tmp.fillOval(this.getX()-getMargin()+7, this.getY()-getMargin(), 18, 18);
    }

    /**
     *
     * @param tmp
     */
    @Override
    public void Right(Graphics2D tmp)
    {
        
	tmp.setColor(Color.RED);
	tmp.fillOval(this.getX()-getMargin()+16-16, this.getY()-getMargin()-3, 24, 24);
        
        Body(tmp);
        
	tmp.setColor(Color.BLACK);
	tmp.drawLine(this.getX()-getMargin()+17, this.getY()-getMargin()+10, this.getX()-getMargin()+17, this.getY()-getMargin()+18);
	tmp.drawLine(this.getX()-getMargin()+24, this.getY()-getMargin()+10, this.getX()-getMargin()+24, this.getY()-getMargin()+18);
    }

    /**
     *
     * @param tmp
     */
    public void Body(Graphics2D tmp)
    {
        
	tmp.setColor(Color.GRAY);
	tmp.fillOval(this.getX()-getMargin(), this.getY()-getMargin()+2, Game.TILE_SIZE-getMargin(), Game.TILE_SIZE-getMargin());
        
	tmp.setColor(Color.PINK);
	tmp.fillOval(this.getX()-getMargin()+3, this.getY()-getMargin()+5, Game.TILE_SIZE-getMargin()-6, Game.TILE_SIZE-getMargin()-6);
    }
    @Override
    public void update()
    {
        setHitted(false);
        if(!shadow)
        {
            if(keyDriver.up == true)
            {
                setVector("up");
            }
            else
            {
                if(keyDriver.down == true)
                {
                    setVector("down");
                }
                else
                {
                    if(keyDriver.left == true)
                    {
                    setVector("left");
                    }
                    else
                    {
                        if(keyDriver.right == true)
                        {
                            setVector("right");
                        }
                    }
                }
            }
            int monsterIndex = game.getCc().checkCharacter(this, game.getMonster());
            int bombIndex = game.getCc().checkBomb(this, game.getExplosion());
            game.getCc().checkSquare(this);
            meetGrimReaper(monsterIndex);
            meetGrimReaper(bombIndex);
        }
        if((!isHitted() && Driver.active)&&!shadow)
        {
            switch(getVector())
            {
                case "up" -> setY(getY() - getS());
                case "down" -> setY(getY() + getS());
                case "left" -> setX(getX() - getS());
                case "right" -> setX(getX() + getS()); 
            }
        }
    }

    private void meetGrimReaper(int index) {
        if(index != Game.MAX_OBJECTS+1)
        {
            Game.getThread().setThread();
            try {
                sleep(2000);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
            Game.getThread().interrupt();
            Menu.goToHell();
        }
    }
}
