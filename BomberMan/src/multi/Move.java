package multi;

/**
 * Klasa do transmizji ruchu przeciwnika;
 */
public class Move 
{
    private int x;
    private int y;
    private String vector;
    private boolean hitted;

    public Move(int x, int y, String vector, boolean hitted) {
        this.x = x;
        this.y = y;
        this.vector = vector;
        this.hitted = hitted;
    }
    @Override
    public String toString()
    {
        String tmp = ":P:";
        tmp+= String.format("%03d", x);
        tmp+= ":";
        tmp+= String.format("%03d", y);
        tmp+= ":";
        tmp+= (vector.substring(0, 1));
        tmp+= ":";
        tmp+= (Boolean.toString(hitted).substring(0, 1));
        return tmp;
    }
}
