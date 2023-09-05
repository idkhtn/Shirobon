/**
 * blocks Package do definicji "kafelków".
 */
package blocks;

/**
 * Klasa pojedynczego bloku - kafelka.
 */
public class Block {
    private boolean hit = false;
    private int type;

    /**
     *
     * @param type konstruktor potrzebny do generowania mapy - w parametrze typ kafelka (1-ściana, 2-mur, 0,3,4,5- puste).
     */
    public Block(int type)
    {
        this.type=type;
        if(type == 1 || type == 2)
        {
            this.hit = true;
        }
    }

    /**
     *
     * @return Zwraca, czy kafelek jest przenikalny
     */
    public boolean isHit() {
        return hit;
    }

    /**
     *
     * @param hit ustala, czy kafelek ma byc przenikalny - do zmiany w mapie po generowaniu lub po rozbiciu kostki bombą.
     */
    public void setHit(boolean hit) {
        this.hit = hit;
    }

    /**
     *
     * @return Zwraca typ kostki (1-ściana, 2-mur, 0,3,4,5- puste).
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @param type Setter do ustalenia lub zmiany typu obiektu. np. przy rozbiciu kafelka bombą.
     */
    public void setType(int type) {
        this.type = type;
    }
}
