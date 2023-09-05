
package objects;

import java.util.Random;

/**
 * Klasa wyliczeniowa do kierunkow postaci.
 */
public enum Vector {
    up,
    right,
    down,
    left;
    Vector()
    {}
    public static Vector getRandom()
    {
        return Vector.values()[new Random().nextInt(4)];
    }
}
