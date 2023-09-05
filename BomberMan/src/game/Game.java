package game;

import blocks.Block;
import menu.Menu;
import blocks.BlockProcessor;
import blocks.Tile;
import blocks.TileChecker;
import com.google.gson.Gson;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import main.Engine;
import menu.Pause;
import multi.Server;
import objects.Bomb;
import objects.Explosion;
import objects.Monster;
import objects.Player;

/**
 * Klasa główna programu - łączy funkcje i zasoby
 */
public class Game extends JPanel{

    /**
     * Do obsługi CardLayout i przenoszenia pomiedzy kartami
     */
    AbstractAction action = new action();

    /**
     * Bazowy rozmiar kratki - 16 px
     */
    private static final int BASIC_TILE_SIZE  = 16;

    /**
     * skala kratki dla widoczności  = 48 px.
     */
    private static final int SCALE = 3;

    /**
     * Prawdziwy rozmiar kratki
     */
    public static final int TILE_SIZE = BASIC_TILE_SIZE * SCALE;

    /**
     * ile maksymalnie kolumn z kafelków
     */
    public static final int NR_OF_HORIZONTAL_TILES = 17;

    /**
     * Ile maks. rzędów kafelków
     */
    public static final int NR_OF_VERTICAL_TILES = 13;

    /**
     * Wymiar ekranu - szerokość
     */
    public static int SCREEN_WIDTH = TILE_SIZE * NR_OF_HORIZONTAL_TILES;

    /**
     * wymiar ekranu - wysokość
     */
    public static int SCREEN_HEIGHT = TILE_SIZE * NR_OF_VERTICAL_TILES;

    /**
     * Czas wyswietlania eksplozji.
     */
    public final static long EXPLOSION_TIME = 800;

    /**
     * ilość przeciwników
     */
    final static int NR_OF_MONSTERS = 10;

    /**
     * Maksymalna ilośc obiektów
     */
    public final static int MAX_OBJECTS = 999;

    /**
     * Wątek głównmy gry
     */
    public static Engine thread;

    /**
     * aktualna ilośc bomb postawionych
     */
    public static int currentExplosions = 0;

    /**
     * parametr , czy gra jest online.
     */
    public static boolean multi = false;
    
    /**
     * Sprawdzacz zderzeń
     */
    private CollisionChecker cc = new CollisionChecker(this);

    /**
     * generator mapy
     */
    private BlockProcessor bProc;

    /**
     * sprawdzacz pustych pól 
     */
    private TileChecker tc;

    /**
     * Obiekt czytajacy eventy klawiatury
     */
    private final Driver keyDriver = new Driver(this);
    
    /**
     * Stan gry
     */
    public static int GAME_STATUS;

    /**
     * Stała stanu gry - graj
     */
    public static final int PLAY = 1;

    /**
     * stała stanu gry - stop
     */
    public static final int PAUSE = 2;
    
    /**
     * do muttiplayet- serializowany JSON
     */
    private String sMap = null;

    /**
     *do muttiplayet- serializowany JSON
     */
    private String sMonster = null;
    
    /**
     * obiekt - gracz nr 1
     */
    private Player player = new Player(this, keyDriver);

    /**
     * obiekt - gracz nr 2.
     */
    private Player player2;

    /**
     * tryb uruchomieni gry - 0-single, 1-mutti master - 3-multi-host
     */
    private int mode;

    /**
     * Pozycja i stan bomby, jako obiekt klasy
     */
    private Bomb bomb;

    /**
     * Pozycja i stan eksplozji jako obiekt tablicowy.
     */
    private final Explosion explosion[] = new Explosion[10];

    /**
     * Wygenerowanie naspisu przykrywajacego gre w przypadku pauzy - przycick "p"
     */
    private final Pause pauseScr = new Pause(this);
    
    /**
     * obiekt - tablica przeciwników
     */
    private final Monster monster[] = new Monster[NR_OF_MONSTERS];

    /**
     * obiekt serializowany Json postacji przeciwników.
     */
    private int [] XYs =  new int[NR_OF_MONSTERS*3];
    
    /**
     * Konstruktor klasy - domyslny - predefinicja
     */
    public Game()
    {
        addKeyListener(keyDriver);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        //System.out.println(SCREEN_WIDTH + " " + SCREEN_HEIGHT);
        setBackground(Color.black);
        setDoubleBuffered(true);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char)KeyEvent.VK_ESCAPE), "Esc");
        getActionMap().put("Esc", action);
        player2 = null;
        bProc  = new BlockProcessor(this);
    }

    /**
     * Konstruktor klasy, relatywnie do stanu
     * @param mode - stan gry - single/multi + host/server
     */
    public Game(int mode)
    {
        this();
        this.mode = mode;
        if(mode > 0)
        {
            multi = true;
            player2 = new Player(this, multi);
        }
        else
            Game.GAME_STATUS = Game.PLAY;
        bProc.createMap();
        this.tc = new TileChecker(this);
        for(int i = 0 ; i < NR_OF_MONSTERS; i++)
        {
            int[] tmp = new Tile(tc.getNextEmptyTile()).getXY();
            monster[i] = new Monster(this, tmp[0], tmp[1]);
        }
        thread = new Engine(this);
    }

    /**
     * pobierz główny wątek gry
     * @return zwraca wątek głowny gry
     */
    public static Engine getThread()
    {
        return thread;
    }

    /**
     * pobierz zarządce eventów
     * @return zwraca zarządcę eventów.
     */
    public Driver getDriver()
    {
        return keyDriver;
    }

    /**
     * Rysownik elementów
     * @param g parametr obiektu rysującego grafike.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        if(bomb!=null)
        {
            bomb.draw(g2d);
        }
        bProc.draw(g2d);
        for(Monster ac : monster)
        {
            ac.draw(g2d);
        }
        player.draw(g2d);
        if(mode >0)
            player2.draw(g2d);
        if(GAME_STATUS == PAUSE)
        {
            pauseScr.draw(g2d);
        }
        g2d.dispose();
    }

    /**
     * metoda aktualizujaca stan gry w petli gry (25FPS
     */
    public void update()
    {
        if(GAME_STATUS == PLAY)
        {
            player.update();
            if(mode >0)
                player2.update();
            for(int i = 0 ; i < NR_OF_MONSTERS ; i++)
            {
                monster[i].update();
            }
            if(bomb!=null)
            {
                bomb.update();
            }
        }
    }

    /**
     * Metoda do postawienia bomby
     * @param x - parametr współrzędnych x - px
     * @param y parametr współrzędnych y - px
     */
    public void plantBomb(int x, int y)
    {
        bomb = new Bomb(this, x, y, System.currentTimeMillis());
        explosion[currentExplosions] = bomb.getExplosion();
    }

    /**
     * Metoda do postawienia bomby w multiplayer.
     * @param x - parametr współrzędnych x - px
     * @param y parametr współrzędnych y - px
     */
    public void plantNetworkBomb(int x, int y)
    {
        if(mode == 1)
            Server.sendBomb(x, y, true);
        else if(mode == 2)
            Server.sendBomb(x, y, false);
    }

    /**
     * Metoda do zwrócenia sprawdzacza kolizji
     * @return
     */
    public CollisionChecker getCc() {
        return cc;
    }

    /**
     * Metoda do zmienienia sprawdzacza
     * @param cc
     */
    public void setCc(CollisionChecker cc) {
        this.cc = cc;
    }

    /**
     * Metoda do zwrócenia Zarządcy kafelków.
     * @return
     */
    public BlockProcessor getbProc() {
        return bProc;
    }

    /**
     * metoda do zmiany zarządcy kafelków.
     * @param bProc
     */
    public void setbProc(BlockProcessor bProc) {
        this.bProc = bProc;
    }

    /**
     * Metoda do zwrócenia zeserializowanego obiektu mapy.
     * @return
     */
    public String getsMap() {
        return sMap;
    }

    /**
     *
     * @param sMap
     */
    public void setsMap(String sMap) {
        this.sMap = sMap;
    }

    public String getsMonster() {
        return sMonster;
    }

    public void setsMonster(String sMonster) {
        this.sMonster = sMonster;
    }

    public Player getPlayer() {
        return player;
    }
    public Player getPlayer2() {
        return player2;
    }

    public Bomb getBomb() {
        return bomb;
    }
    public int getMode() {
        return mode;
    }

    public Explosion[] getExplosion() {
        return explosion;
    }

    public Monster[] getMonster() {
        return monster;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
    public class action extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            thread.interrupt();
            Menu.goToPause();
        }       
    }
    public String loadMap()
    {
        return sMap;
    }
    public void saveMap(String sMap)
    {
        this.sMap = sMap;
    }

    /**
     * Metoda do przygotowaniu multiplayer
     * @param mode tryb multiplayer - 1/2
     */
    public void prepareMulti(int mode)
    {
        if(mode == 2)
        {
            player.setX(Game.SCREEN_WIDTH - (int)(Game.TILE_SIZE * 1.5));
            player.setY(Game.SCREEN_HEIGHT - (int)(Game.TILE_SIZE * 1.5));
        }
        //this.tc = new TileChecker(this);
        if(mode == 2)
        {
            Gson gson = new Gson();
            Block[][] newMap = new Block[Game.NR_OF_HORIZONTAL_TILES][Game.NR_OF_VERTICAL_TILES];
            newMap = gson.fromJson(sMap,Block[][].class);
            System.out.println("sMap:"+sMap);
            bProc.setMap(newMap);
            XYs = gson.fromJson(sMonster,int[].class);
            System.out.println("sMonster:"+sMonster);
        }
        
        for(int i = 0 ; i < NR_OF_MONSTERS; i++)
        {
            int[] tmp = new Tile(tc.getNextEmptyTile()).getXY();
            if(mode < 2)
            {
                monster[i] = new Monster(this, tmp[0], tmp[1]);
            }
            if(mode == 1)
            {
                XYs[i*3] = tmp[0];
                XYs[(i*3)+1] = tmp[1];
                switch(monster[i].getVector())
                {
                    case "up" -> XYs[(i*3)+2] = 0;
                    case "right" -> XYs[(i*3)+2] = 1;
                    case "down" -> XYs[(i*3)+2] = 2;
                    case "left" -> XYs[(i*3)+2] = 3;
                }
            }
            if(mode == 2)
            {
                monster[i] = new Monster(this, XYs[i*3],XYs[(i*3)+1],XYs[(i*3)+2]);
            }
        }
        if(mode == 1)
        {
            Gson gson = new Gson();
            sMap = gson.toJson(bProc.map);
            sMonster = gson.toJson(XYs);
        }
        if(mode > 0)
        {
            this.bProc.map[Game.NR_OF_HORIZONTAL_TILES-2][Game.NR_OF_VERTICAL_TILES-2].setType(0);
            this.bProc.map[Game.NR_OF_HORIZONTAL_TILES-2][Game.NR_OF_VERTICAL_TILES-2].setHit(false);
            this.bProc.map[Game.NR_OF_HORIZONTAL_TILES-2][Game.NR_OF_VERTICAL_TILES-3].setType(0);
            this.bProc.map[Game.NR_OF_HORIZONTAL_TILES-2][Game.NR_OF_VERTICAL_TILES-3].setHit(false);
            this.bProc.map[Game.NR_OF_HORIZONTAL_TILES-3][Game.NR_OF_VERTICAL_TILES-2].setType(0);
            this.bProc.map[Game.NR_OF_HORIZONTAL_TILES-3][Game.NR_OF_VERTICAL_TILES-2].setHit(false);
        }
    }
}