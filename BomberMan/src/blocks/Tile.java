package blocks;

/**
 *Klasa przedstawiająca położenie kafelka na mapie - jako uproszczenie.
 */
public class Tile {

    /**
     *współrzędna x na mapie - w pikselach
     */
    private final int x;

    /**
     * współrzędna y na mapie - w pikselach
     */
    private final int y;

    /**
     *Konstruktor obiektu z dwóch współrzędnych.
     * @param x współrzędna x na mapie - w pikselach
     * @param y współrzędna y na mapie - w pikselach
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Konstruktor kafelka,, jako parametr przyjmuje inny kafelek.
     * @param t parametr typu własnego - Tile. do "kopiowania obiektu"
     */
    public Tile(Tile t)
    {
        this.x = t.x;
        this.y = t.y;
    }

    /**
     *Metoda do pobrania współrzednych X oraz Y jako tablicę int o 2-ch elementach.
     * @return Zwraca tablicę elementów x i y.
     */
    public int[] getXY()
    {
        int [] tmp = new int[2];
        tmp[0] = this.x;
        tmp[1] = this.y;
        return tmp;
    }

    /**
     *Metoda nadpisana - na potrzeby własnej prezentacji obiektu - do przesłania do klienta lub na konsolę out.
     * @return
     */
    @Override
    public String toString()
    {
        return "["+this.x+"]["+this.y+"]";
    }
}
