package objects;
import java.awt.Color;
import java.awt.Graphics2D;
import game.Game;

/**
 *
 * Klasa potwora - jest rozwinięciem abstrakcyjnej klasy AbstCharacter.
 */
public class Monster extends AbstCharacter
{
    public Monster(Game game, int x, int y) {
        super(game);
        this.setVector( Vector.getRandom().toString());
        this.setS(1);
        this.setX((int) (Game.TILE_SIZE*0.5+(Game.TILE_SIZE*x)));
        this.setY((int) (Game.TILE_SIZE*0.5+(Game.TILE_SIZE*y)));
    }
    public Monster(Game game, int x, int y, int vector) 
    {
        this(game,x,y);
        this.setVector( Vector.values()[vector].toString());
    }
    /**
     *
     * @param tmp
     */
    @Override
    public void Left(Graphics2D tmp)
    {
        Body(tmp); 
        
        tmp.setColor(Color.RED);
	tmp.fillOval(this.getX()-getMargin()+4, this.getY()-getMargin()+10, 5, 5);
	tmp.fillOval(this.getX()-getMargin()+17, this.getY()-getMargin()+10, 5, 5);
        
	tmp.setColor(Color.BLACK);
        tmp.drawLine(this.getX()-getMargin()+8, this.getY()-getMargin()+23, this.getX()-getMargin()+20, this.getY()-getMargin()+23);
    }

    /**
     *
     * @param tmp
     */
    @Override
    public void Down(Graphics2D tmp)
    {
        Body(tmp);
            
        tmp.setColor(Color.RED);
	tmp.fillOval(this.getX()-getMargin()+7, this.getY()-getMargin()+10, 5, 5);
	tmp.fillOval(this.getX()-getMargin()+20, this.getY()-getMargin()+10, 5, 5);
        
	tmp.setColor(Color.BLACK);
        tmp.drawLine(this.getX()-getMargin()+8, this.getY()-getMargin()+23, this.getX()-getMargin()+24, this.getY()-getMargin()+23);
    }

    /**
     *
     * @param tmp
     */
    @Override
    public void Up(Graphics2D tmp)
    {
        Body(tmp);
    }

    /**
     *
     * @param tmp
     */
    @Override
    public void Right(Graphics2D tmp)
    {
        Body(tmp);
            
        tmp.setColor(Color.RED);
	tmp.fillOval(this.getX()-getMargin()+10, this.getY()-getMargin()+10, 5, 5);
	tmp.fillOval(this.getX()-getMargin()+23, this.getY()-getMargin()+10, 5, 5);
        
	tmp.setColor(Color.BLACK);
        tmp.drawLine(this.getX()-getMargin()+12, this.getY()-getMargin()+23, this.getX()-getMargin()+24, this.getY()-getMargin()+23);
    }
    public void Body(Graphics2D tmp)
    {
        
	tmp.setColor(Color.YELLOW);
	tmp.fillOval(this.getX()-getMargin(), this.getY()-getMargin(), Game.TILE_SIZE-getMargin(), Game.TILE_SIZE-getMargin());
    }
    @Override
    public void update()
    {
        if(isAlive())
        {
            setHitted(false);
            int bombIndex = game.getCc().checkBomb(this, game.getExplosion());
            if(bombIndex != Game.MAX_OBJECTS+1)
            {
                this.setHitted(true);
            }
            game.getCc().checkSquare(this);
            if(!isHitted())
            {
                switch(getVector())
                {
                    case "up" -> setY(getY() - getS());
                    case "down" -> setY(getY() + getS());
                    case "left" -> setX(getX() - getS());
                    case "right" -> setX(getX() + getS());
                }
            }
            else if(!Game.multi)
                setVector(Vector.getRandom().toString());
            else
            {
                int tmp = Vector.valueOf(getVector()).ordinal();
                int tmp2 = ((tmp+1)%4);
                setVector(Vector.values()[tmp2].toString());
            }
        }
    }
    
}
