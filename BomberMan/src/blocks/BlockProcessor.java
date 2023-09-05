package blocks;

import game.Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * Klasa do zarządzania zawartością mapy - generator i zmieniacz.
 */
public final class BlockProcessor {

    /**
     * Obiekt głównej klasy gry. Łączy w całość segmenty
     */
    Game game;

    /**
     * Obiekt do przechowania podobiektów Block w dwóch wymiarach tablicy.
     */
    public Block[][] map;
    /**
     * Obiekt klasy do wyszukiwania pustych pól, aby przeciwnicy nie generowali
     * się w ścianach.
     */
    TileChecker tc;

    /**
     * Konstruktor z parametrem obiektu gry, aby zmieniać posrednio jej
     * właściwości.
     *
     * @param game Obiekt głównego łącznika programu.
     */
    public BlockProcessor(Game game) {
        this.game = game;
        map = new Block[Game.NR_OF_HORIZONTAL_TILES][Game.NR_OF_VERTICAL_TILES];
    }

    /**
     * Metoda do rysowania ścian, które można rozbić bombą.
     * @param rowIndex parametr identyfikujący numer rzedu na mapie obiektów Block[][] map.
     * @param colIndex parametr identyfikujący numer kolumny na mapie obiektów Block[][] map.
     * @param g2d parametr okreslający "rysownika" obiektór, czyli Graphics2D
     */
    private void paintObstacle(int rowIndex, int colIndex, Graphics g2d) {
        g2d.setColor(Color.RED);
        g2d.fillRect(colIndex * Game.TILE_SIZE, rowIndex * Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(colIndex * Game.TILE_SIZE + 1, rowIndex * Game.TILE_SIZE + 10, colIndex * Game.TILE_SIZE + Game.TILE_SIZE, rowIndex * Game.TILE_SIZE + 10);
        g2d.drawLine(colIndex * Game.TILE_SIZE + 1, rowIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2), colIndex * Game.TILE_SIZE + Game.TILE_SIZE, rowIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2));
        g2d.drawLine(colIndex * Game.TILE_SIZE + 1, rowIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 12, colIndex * Game.TILE_SIZE + Game.TILE_SIZE, rowIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 12);
        g2d.drawLine(colIndex * Game.TILE_SIZE + 1, rowIndex * Game.TILE_SIZE + Game.TILE_SIZE, colIndex * Game.TILE_SIZE + Game.TILE_SIZE, rowIndex * Game.TILE_SIZE + Game.TILE_SIZE);

        g2d.drawLine(colIndex * Game.TILE_SIZE + 10, rowIndex * Game.TILE_SIZE + 1, colIndex * Game.TILE_SIZE + 10, rowIndex * Game.TILE_SIZE + 10);
        g2d.drawLine(colIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 12, rowIndex * Game.TILE_SIZE + 1, colIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 12, rowIndex * Game.TILE_SIZE + 10);

        g2d.drawLine(colIndex * Game.TILE_SIZE + 1, rowIndex * Game.TILE_SIZE + 10, colIndex * Game.TILE_SIZE + 1, rowIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2));
        g2d.drawLine(colIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 1, rowIndex * Game.TILE_SIZE + 10, colIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 1, rowIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2));

        g2d.drawLine(colIndex * Game.TILE_SIZE + 10, rowIndex * Game.TILE_SIZE + 1 + ((Game.TILE_SIZE) / 2), colIndex * Game.TILE_SIZE + 10, rowIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 12);
        g2d.drawLine(colIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 12, rowIndex * Game.TILE_SIZE + 1 + ((Game.TILE_SIZE) / 2), colIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 12, rowIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 12);

        g2d.drawLine(colIndex * Game.TILE_SIZE + 1, rowIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 12, colIndex * Game.TILE_SIZE + 1, rowIndex * Game.TILE_SIZE + Game.TILE_SIZE);
        g2d.drawLine(colIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 1, rowIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 12, colIndex * Game.TILE_SIZE + ((Game.TILE_SIZE) / 2) + 1, rowIndex * Game.TILE_SIZE + Game.TILE_SIZE);

    }

    /**
     * Metoda do rysowania ścian, których NIE można rozbić bombą - granic mapy.
     * @param rowIndex parametr identyfikujący numer rzedu na mapie obiektów Block[][] map.
     * @param colIndex parametr identyfikujący numer kolumny na mapie obiektów Block[][] map.
     * @param g2d parametr okreslający "rysownika" obiektór, czyli Graphics2D
     */
    private void paintWall(int rowIndex, int colIndex, Graphics g2d) {
        g2d.setColor(Color.lightGray);
        g2d.fillRect(colIndex * Game.TILE_SIZE, rowIndex * Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE);
        g2d.setColor(Color.darkGray);
        g2d.drawLine(colIndex * Game.TILE_SIZE, rowIndex * Game.TILE_SIZE, colIndex * Game.TILE_SIZE + Game.TILE_SIZE, rowIndex * Game.TILE_SIZE);
        g2d.drawLine(colIndex * Game.TILE_SIZE, rowIndex * Game.TILE_SIZE + Game.TILE_SIZE, colIndex * Game.TILE_SIZE + Game.TILE_SIZE, rowIndex * Game.TILE_SIZE + Game.TILE_SIZE);
        g2d.drawLine(colIndex * Game.TILE_SIZE, rowIndex * Game.TILE_SIZE, colIndex * Game.TILE_SIZE, rowIndex * Game.TILE_SIZE + Game.TILE_SIZE);
        g2d.drawLine(colIndex * Game.TILE_SIZE + Game.TILE_SIZE, rowIndex * Game.TILE_SIZE, colIndex * Game.TILE_SIZE + Game.TILE_SIZE, rowIndex * Game.TILE_SIZE + Game.TILE_SIZE);
    }

    /**
     *Metoda zmieniajaca cyfry z typem, na faktyczne metody generujące obiekty.
     * @param type cyfra z typem (0,3-infinity - puste, 1 - ściana, 2-mur.)
     * @param x parametr identyfikujący numer rzedu na mapie obiektów Block[][] map.
     * @param y parametr identyfikujący numer kolumny na mapie obiektów Block[][] map.
     * @param g2d parametr okreslający "rysownika" obiektór, czyli Graphics2D
     */
    public void drawType(int type, int x, int y, Graphics g2d) {
        switch (type) {
            case 1 ->
                paintWall(x, y, g2d);
            case 2 ->
                paintObstacle(x, y, g2d);
        }
    }

    /**
     *Metoda generująca losowo zbudowaną mapę, ale z obramowaniem z bloków, których nie da się rozbijać.
     */
    public void createMap() {
        for (int x = 0; x < Game.NR_OF_HORIZONTAL_TILES; x++) {
            for (int y = 0; y < Game.NR_OF_VERTICAL_TILES; y++) {
                if ((x == 1 && y == 1) || (x == 1 && y == 2) || (y == 1 && x == 2)) {
                    map[x][y] = new Block(0);
                } else {
                    if (x == 0 || y == Game.NR_OF_VERTICAL_TILES - 1 || y == 0 || x == Game.NR_OF_HORIZONTAL_TILES - 1) {
                        map[x][y] = new Block(1);
                    } else {
                        Random rand = new Random();
                        map[x][y] = new Block(rand.nextInt(2, 5));
                    }
                }
            }
        }
    }

    /**
     *Metoda zwracająca aktualnie wygenerowaną mapę.
     * @return zwraca typ obiektu map - Block[][]
     */
    public Block[][] getMap() {
        return map;
    }

    /**
     *Metoda do ustalenia mapy po zmianie - nadpisanej np.
     * @param newMap Parametrem przyjmuje nową nadpisaną mapę.
     */
    public void setMap(Block[][] newMap) {
        this.map = newMap;
    }

    /**
     * Metoda to wyrysowania mapy obiektów
     * @param g2d obiekt klasy Graphics2D - do rysowania obiektów i elementów interfejsu.
     */
    public void draw(Graphics2D g2d) {
        for (int x = 0; x < Game.NR_OF_VERTICAL_TILES; x++) {
            for (int y = 0; y < Game.NR_OF_HORIZONTAL_TILES; y++) {
                int num = map[y][x].getType();
                drawType(num, x, y, g2d);
            }
        }
    }
}
