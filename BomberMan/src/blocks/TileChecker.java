package blocks;

import java.util.Collections;
import java.util.Stack;
import game.Game;

/**
 *Sprawdzacz kafelków - do generowania położenia przeciwników - wyszukiwacz wolnych pól.
 */
public class TileChecker {

    /**
     * Obiekt Zarządzający kafelkami - potrzebny do wczytania i zapisania mapy po zmianie.
     */
    private final BlockProcessor bp;

    /**
     * Obiekt łączący program.
     */
    private final Game game;

    /**
     * Stos pustych pól na mapie
     */
    public Stack<Tile> emptyTiles;

    /**
     *Konstruktor klasy sprawdzacza.
     * @param game parametrem jest gra, aby zyskać dostęp do zasobów współdzielonych.
     */
    public TileChecker(Game game) {
        this.game = game;
        bp = game.getbProc();
        emptyTiles = new Stack<>();
        emptyTiles = this.getEmptyTiles();
    }

    /**
     * Metoda zwracajaca liczbę pustych kafelków.
     * @return Jako parametr zwraca ilość pustych pól na mapie.
     */
    public int emptyTilesCount()
    {
        Block[][] map; 
        map = bp.getMap();
        int count = 0;
        for (Block[] row : map) {
            for (Block item : row) {
                if (item.getType() > 2) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Metoda zwracajaca stos złozony z obiektów Tile ze współrzędnymi pustych kafelków
     * @return Zwrócony stos jest używany do ustalania pozycji nowych przeciwników.
     */
    public final Stack<Tile> getEmptyTiles()
    {
        //int x = 0;
        Block[][] tmp = bp.getMap();
        for(int i = 0 ; i < tmp.length ; i++)
        {
            for(int j = 0; j < tmp[i].length ; j++)
            {
                if(tmp[i][j].getType()>2)
                {
                    emptyTiles.push(new Tile(i,j));
                }
            }
        }
        Collections.shuffle(emptyTiles);
        return emptyTiles;
    }

    /**
     * Metoda zwracajaca nastepny pusty kafelek.
     * @return zwraca obiekt Tile - złożony z dwuelementowej tablicy int[2].
     */
    public Tile getNextEmptyTile()
    {
        return emptyTiles.pop();
    }
}
