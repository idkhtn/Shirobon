package main;

/**
 * klasa zamykacza kona i wątków.
 */
public class Closer{

    /**
     *pole głownego okna aplikacji
     */
    private final Main mainWindow;

    public Closer(){
        mainWindow = new Main();
    }
    public Closer(Main mainWindow){
        this.mainWindow = mainWindow;
    }

    public void diposeMainFrame(){
        mainWindow.getMainFrame().dispose();
    }
}
