package game;

import objects.AbstCharacter;
import objects.Explosion;

/**
 * Klasa detekcji zderzeń obiektem z innym obiektem.
 */
public class CollisionChecker 
{

    /**
     * Klasa zawiera obiekt gra, aby zyskać dostęp do zasobów współdzielonych.
     */
    Game game;

    /**
     *
     * @param game parametrem jest gra, aby zyskać dostęp do zasobów współdzielonych.
     */
    public CollisionChecker(Game game) {
        this.game = game;
    }

    /**
     * Metoda detekcji kolizji ze scianami.
     * @param ac parametrem jest postać abstrakcyjna.
     */
    public void checkSquare( AbstCharacter ac)
    {
        int leftX = ac.getX() + ac.getHard().x;
        int rightX = ac.getX() + ac.getHard().x + ac.getHard().width;
        int topY = ac.getY() + ac.getHard().y;
        int bottomY = ac.getY() + ac.getHard().y + ac.getHard().height;
        
        int leftCol = leftX/Game.TILE_SIZE;
        int rightCol = rightX/Game.TILE_SIZE;
        int topRow = topY/Game.TILE_SIZE;
        int bottomRow = bottomY/Game.TILE_SIZE;
        
        switch(ac.getVector())
        {
            case "up" -> 
                        {
                            topRow = (topY - ac.getS())/Game.TILE_SIZE;
                            if(game.getbProc().map[leftCol][topRow].isHit() || game.getbProc().map[rightCol][topRow].isHit())
                            {
                                ac.setHitted(true);
                            }
                        }
            case "down" -> 
                        {
                            bottomRow = (bottomY + ac.getS())/Game.TILE_SIZE;
                            if(game.getbProc().map[leftCol][bottomRow].isHit() || game.getbProc().map[rightCol][bottomRow].isHit())
                            {
                                ac.setHitted(true);
                            }
                        }
            case "left" -> 
                        {
                            leftCol = (leftX - ac.getS())/Game.TILE_SIZE;
                            if(game.getbProc().map[leftCol][topRow].isHit() || game.getbProc().map[leftCol][bottomRow].isHit())
                            {
                                ac.setHitted(true);
                            }
                        }
            case "right" -> 
                        {
                            rightCol = (rightX + ac.getS())/Game.TILE_SIZE;
                            if(game.getbProc().map[rightCol][topRow].isHit() || game.getbProc().map[rightCol][bottomRow].isHit())
                            {
                                ac.setHitted(true);
                            }
                        }
        }
    }

    /**
     * Metoda detekcji kolizji postaci
     * @param character gracz - postać abstrakcyjna.
     * @param abstCharList lista innych abstrakcyjnych postacji.
     * @return Zwraca identyfikator postaci, z którą gracz się zderzył - domyslna warość = 1000, czyli o 1 wiecej niż maks. liczba obiektów.
     */
    public int checkCharacter( AbstCharacter character, AbstCharacter[] abstCharList)
    {
        int index = Game.MAX_OBJECTS+1;
        for(int i = 0 ; i < abstCharList.length ; i++)
        {
            if(abstCharList[i] != null)
            {
                character.hard.x = character.getX() + character.getHard().x;
                character.hard.y = character.getY() + character.getHard().y;
                
                abstCharList[i].hard.x = abstCharList[i].getX() + abstCharList[i].getHard().x;
                abstCharList[i].hard.y = abstCharList[i].getY() + abstCharList[i].getHard().y;
                
                switch (character.getVector())
                {
                    case "up" -> {
                        character.hard.y -= character.getS();
                        if(character.getHard().intersects(abstCharList[i].getHard()) && abstCharList[i].isAlive())
                        {
                            character.setHitted(true);
                            index = i;
                        }
                    }
                    case "down" -> {
                        character.hard.y += character.getS();
                        if(character.getHard().intersects(abstCharList[i].getHard()) && abstCharList[i].isAlive())
                        {
                            character.setHitted(true);
                            index = i;
                        }
                    }
                    case "left" -> {
                        character.hard.x -= character.getS();
                        if(character.getHard().intersects(abstCharList[i].getHard()) && abstCharList[i].isAlive())
                        {
                            character.setHitted(true);
                            index = i;
                        }
                    }
                    case "right" -> {
                        character.hard.x += character.getS();
                        if(character.getHard().intersects(abstCharList[i].getHard()) && abstCharList[i].isAlive())
                        {
                            character.setHitted(true);
                            index = i;
                        }
                    }
                }
                character.hard.x = character.getDefaultHardX();
                character.hard.y = character.getDefaultHardY();
                abstCharList[i].hard.x = abstCharList[i].getDefaultHardX();
                abstCharList[i].hard.y = abstCharList[i].getDefaultHardY();
            }
        }
        return index;
    }

    /**
     * Detekcja obecności w polu rażenia wybuchu.
     * @param character postać abstrakcyjna (gracz, przeciwnik).
     * @param explosions Tablica kafelków objętych eksplozją - wypełniane w petli for i size of explosion..
     * @return Zwraca identyfikator postaci, z którą gracz się zderzył - domyslna warość = 1000, czyli o 1 wiecej niż maks. liczba obiektów.
     */
    public int checkBomb( AbstCharacter character, Explosion[] explosions)
    {
        int index = Game.MAX_OBJECTS+1;
        if(explosions!=null)
            for(int i = 0 ; i < explosions.length ; i++)
            {
                if(explosions[i] != null)
                {
                    character.hard.x = character.getX() + character.getHard().x;
                    character.hard.y = character.getY() + character.getHard().y;
                    //character.hard.y -= character.s;
                    if(squareCircleInstersect(explosions[i].getX()/Game.TILE_SIZE,explosions[i].getY()/Game.TILE_SIZE,character)
                        && explosions[i].isVisible()
                        && ((character.getX() >= explosions[i].getX()-32 && character.getX() <= explosions[i].getX()+32)
                                ||(character.getY() >= explosions[i].getY()-32 && character.getY() <= explosions[i].getY()+32))
                    )
                    {
                        character.setHitted(true);
                        character.setAlive(false);
                        index = i;
                    }
                    character.hard.x = character.getDefaultHardX();
                    character.hard.y = character.getDefaultHardY();
                    /*
                    DEBUG:
                    System.out.println("["+character.x/Game.TILE_SIZE+"]["+character.y/Game.TILE_SIZE+"] ["+explosions[i].x/Game.TILE_SIZE+"]["+explosions[i].y/Game.TILE_SIZE+"] Visible: " + explosions[i].visible 
                            + " X: " + explosions[i].x + " Y: " + explosions[i].y
                            + " Colision: " + squareCircleInstersect(explosions[i].x/Game.TILE_SIZE,explosions[i].y/Game.TILE_SIZE,character));
                
                    if(character.x >= (explosions[i].x-40))
                    {
                        System.out.print(" true");
                    }
                    else
                    {
                        System.out.print("false");
                    }
                    System.out.print(" ");
                    if(character.x <= (explosions[i].x+40))
                    {
                        System.out.print(" true");
                    }
                    else
                    {
                        System.out.print("false");
                    }
                    System.out.println();
                    */
                    //System.out.println(character.x + " >= " + (explosions[i].x-22) + "   " + character.x + " <= " + (explosions[i].x+22));
                }
            }
        return index;
    }

    /**
     * Metoda dodatkowa do CheckBomb - oblicza, czy promień wybuchu zawiera w sobie położenie gracza.
     * @param row Parametr środka rzędu
     * @param col Parametr środka kolumny.
     * @param abstractCharacter parametr postaci, na którą działa wybuch.
     * @return Zwraca identyfikator obiektu, z którym gracz się zderzył - domyslna warość = 1000, czyli o 1 wiecej niż maks. liczba obiektów.
     */
    private boolean squareCircleInstersect(int row, int col, AbstCharacter abstractCharacter) {
	//Inspiracja z : http://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
	int characterX = abstractCharacter.getX();
	int characterY = abstractCharacter.getY();

	int circleRadius = (Explosion.getSize() * Game.TILE_SIZE)+9;
	int squareSize = Game.TILE_SIZE;
	int squareCenterX = (row*squareSize)+(squareSize/2);
	int squareCenterY = (col*squareSize)+(squareSize/2);
        //DEBUG
        //System.out.println("cR: " + circleRadius + " sS: "
        //+ squareSize + " sCX: " + squareCenterX + " sCY: " + squareCenterY );
        
	int circleDistanceX = Math.abs(characterX - squareCenterX);
	int circleDistanceY = Math.abs(characterY - squareCenterY);

	if (circleDistanceX > (squareSize/2 + circleRadius)) { return false; }
	if (circleDistanceY > (squareSize/2 + circleRadius)) { return false; }

	if (circleDistanceX <= (squareSize/2)) { return true; }
	if (circleDistanceY <= (squareSize/2)) { return true; }

	int cornerDistance = (circleDistanceX - squareSize/2)^2 +
							      (circleDistanceY - squareSize/2)^2;

	return (cornerDistance <= (circleRadius^2));
    }

    /**
     * Metoda detekcji rozbicia kafelka
     * @param e parametr z eksplozją.
     */
    public void checkBreak(Explosion e)
    {        
        for (int i = 1; i <= Explosion.getSize(); i++) 
        {
            if(game.getbProc().map[(e.getX()/Game.TILE_SIZE)-i][(e.getY()/Game.TILE_SIZE)].getType() == 2)
            {
                game.getbProc().map[(e.getX()/Game.TILE_SIZE)-1][(e.getY()/Game.TILE_SIZE)].setType(3);
                game.getbProc().map[(e.getX()/Game.TILE_SIZE)-1][(e.getY()/Game.TILE_SIZE)].setHit(false);
            }
            if(game.getbProc().map[i+(e.getX()/Game.TILE_SIZE)][(e.getY()/Game.TILE_SIZE)].getType() == 2)
            {
                game.getbProc().map[i+(e.getX()/Game.TILE_SIZE)][(e.getY()/Game.TILE_SIZE)].setType(3);
                game.getbProc().map[i+(e.getX()/Game.TILE_SIZE)][(e.getY()/Game.TILE_SIZE)].setHit(false);
            }
            if(game.getbProc().map[e.getX()/Game.TILE_SIZE][(e.getY()/Game.TILE_SIZE)-i].getType() == 2)
            {
                game.getbProc().map[e.getX()/Game.TILE_SIZE][(e.getY()/Game.TILE_SIZE)-i].setType(3);
                game.getbProc().map[e.getX()/Game.TILE_SIZE][(e.getY()/Game.TILE_SIZE)-i].setHit(false);
            }
            if(game.getbProc().map[e.getX()/Game.TILE_SIZE][i+(e.getY()/Game.TILE_SIZE)].getType() == 2)
            {
                game.getbProc().map[e.getX()/Game.TILE_SIZE][i+(e.getY()/Game.TILE_SIZE)].setType(3);
                game.getbProc().map[e.getX()/Game.TILE_SIZE][i+(e.getY()/Game.TILE_SIZE)].setHit(false);
            }
        }
        //DEBUG
        /*
        System.out.println(
                "row: " + (e.getY()/Game.TILE_SIZE)+
                " col: " + (e.getX()/Game.TILE_SIZE)+ ""+
                game.bProc.map[e.getX()/Game.TILE_SIZE][1+(e.getY()/Game.TILE_SIZE)].type
                + " "
                + game.bProc.map[e.getX()/Game.TILE_SIZE][1+(e.getY()/Game.TILE_SIZE)].hit
        );
       */
    }
}
