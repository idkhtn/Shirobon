package multi;

/**
 * Klasa potrzebna do transmisji postawionej bomby.
 */
public class BombSender 
{
    private int x;
    private int y;
    private boolean mode;

    public BombSender(int x, int y, boolean mode) {
        this.x = x;
        this.y = y;
        this.mode = mode;
    }
    @Override
    public String toString()
    {
        String tmp;
        if(mode)
            tmp = ":B:";
        else
            tmp = ":B2:";
        tmp+= String.format("%03d", x);
        tmp+= ":";
        tmp+= String.format("%03d", y);
        return tmp;
    }
}
