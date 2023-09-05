package menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import game.Game;
import static java.lang.Thread.sleep;
import multi.Client;
import main.Main;
import multi.Server;

/**
 * Klasa definiująca główne menu aplikacji.
 */
public class Menu extends JFrame {
 
    private static int currentCard = 1;
    public static CardLayout cardLayout;
    private static JPanel cardMenuPanel;
    private static JPanel gamePanel;
    private final JPanel pausePanel;
    private final JPanel deadPanel;
    private static Main mainWindow;
    private Client client = null;
    private Client Serverclient = null;
    private Server server;
    private Server clientServer;
    public Menu(Main mainWindow)
    {
        cardMenuPanel = new JPanel();
        cardLayout = new CardLayout();
        cardMenuPanel.setLayout(cardLayout);
        JPanel gameMenuPanel = new JPanel();
        gameMenuPanel.setLayout(new GridLayout(0,2,0,-500));
        pausePanel = new ExitMenu(mainWindow);
        deadPanel = new DeadMenu(mainWindow);
        gamePanel = new Game();
        gamePanel.setFocusable(true);
        gamePanel.setEnabled(true);
        gamePanel.setVisible(true);
        JLabel jl1 = new JLabel("BOMBERMAN!");
        jl1.setHorizontalAlignment(JLabel.CENTER);
        jl1.setVerticalAlignment(JLabel.CENTER);
        jl1.setForeground(Color.decode("#fc3a67"));
        Font f = new Font("Monospaced", Font.BOLD | Font.ITALIC,/*25*/60);
        jl1.setFont(f);
        gameMenuPanel.add(jl1);
        
        JLabel image = new JLabel();
        image.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/bomberman.png")));
        gameMenuPanel.add(image);
        validate();
        
        cardMenuPanel.add(gameMenuPanel, "1");
        cardMenuPanel.add(gamePanel, "2");
        cardMenuPanel.add(gamePanel, "3");
        cardMenuPanel.add(pausePanel, "4");
        cardMenuPanel.add(deadPanel, "5");
        JPanel buttonPanel = new JPanel();
        JButton singlePlayer = new JButton("Single Player");
        JButton multiPlayer = new JButton("Multi Player");
        JButton exit = new JButton("Exit");
        singlePlayer.setBackground(Color.decode("#fc3a67"));
        exit.setBackground(Color.decode("#fc3a67"));
        multiPlayer.setForeground(Color.white);
        multiPlayer.setBackground(Color.decode("#fc3a67"));
        singlePlayer.setForeground(Color.white);
        exit.setForeground(Color.white);
        buttonPanel.add(singlePlayer);
        buttonPanel.add(multiPlayer);
        singlePlayer.addActionListener((ActionEvent arg0) -> {
            gamePanel = new Game(0);
            cardMenuPanel.add(gamePanel, "2");
            Game.getThread().start();
            currentCard = 2;
            cardLayout.show(cardMenuPanel, "" + (currentCard));
            gamePanel.setRequestFocusEnabled(true);
            gamePanel.requestFocusInWindow();
        });
        multiPlayer.addActionListener((ActionEvent arg0) -> {
            gamePanel = new Game(1);
            ((Game)gamePanel).prepareMulti(1);
            server = new Server(((Game)gamePanel),2020);
            if(Server.isIsMaster())
            {
                server.process();
                cardMenuPanel.add(gamePanel, "3");
                if(server.getSession()!=null)
                {
                    //System.out.println(((Game)gamePanel).getsMap());
                    //System.out.println(((Game)gamePanel).getsMonster());
                    client = new Client(((Game)gamePanel),2021);
                    client.init();
                    Game.getThread().start();
                    currentCard = 3;
                    cardLayout.show(cardMenuPanel, "" + (currentCard));
                    gamePanel.setRequestFocusEnabled(true);
                    gamePanel.requestFocusInWindow();
                }
            }
            else
            {
                gamePanel = new Game(2);
                clientServer = new Server(((Game)gamePanel),2021);
                client = new Client(((Game)gamePanel),2020);
                client.init();
                while(Client.smon == null)
                {
                    try {
                        sleep(100);
                    } catch (InterruptedException ex) {
                        System.out.println(ex);
                    }
                }
                ((Game)gamePanel).setsMap(Client.smap);
                ((Game)gamePanel).setsMonster(Client.smon);
                clientServer.process();
                ((Game)gamePanel).prepareMulti(2);
                cardMenuPanel.add(gamePanel, "3");
                if(client.getSocket()!=null)
                {
                    Game.getThread().start();
                    currentCard = 3;
                    cardLayout.show(cardMenuPanel, "" + (currentCard));
                    gamePanel.setRequestFocusEnabled(true);
                    gamePanel.requestFocusInWindow();
                }
            }
            Game.GAME_STATUS = Game.PLAY;
        });
        buttonPanel.add(exit);
        exit.addActionListener((ActionEvent arg0) -> {
            dispose();
        });
        getContentPane().add(cardMenuPanel, BorderLayout.NORTH);
        gameMenuPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    public static void goToPause()
    {
        currentCard = 4;
        cardLayout.show(cardMenuPanel, "" + (currentCard));
    }
    public static void goToHell()
    {
        currentCard = 5;
        cardLayout.show(cardMenuPanel, "" + (currentCard));
    }
    public static void goToMenu()
    {
        gamePanel = new Game();
        cardLayout.first(cardMenuPanel);
        currentCard = 1;
    }
    private final Action quit = new AbstractAction()
    {
        @Override
	public void actionPerformed(ActionEvent e) {
            dispose();
	}
    };
    public static Main getMainWindow()
    {
        return mainWindow;
    }
    @Override
    public void dispose()
    {
        if(client != null)
        {
            client.kill();
        }
        super.dispose();
    }
}