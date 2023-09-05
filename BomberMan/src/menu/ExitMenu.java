package menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import main.Closer;
import main.Main;

/**
 * Klasa z zawartością menu wyjścia.
 */
public class ExitMenu extends JPanel implements ActionListener {
    
    private JButton exit;
    
    private Main mainWindow;
    
    public ExitMenu(Main mainWindow)
    {
        
        setLayout(new GridLayout(2,1));
        exit = new JButton("Exit");
        exit.addActionListener(this);
        exit.setForeground(Color.white);
        exit.setBackground(Color.decode("#fc3a67"));
        JPanel cp1 = new JPanel();
        JPanel cp2 = new JPanel();
        cp1.setLayout(new BorderLayout());
        
        JLabel dead = new JLabel("Click the button to exit.");
        dead.setHorizontalAlignment(JLabel.CENTER);
        dead.setVerticalAlignment(JLabel.CENTER);
        dead.setFont(this.getFont().deriveFont(Font.BOLD, 50F));
        cp1.add(dead, BorderLayout.CENTER);
        
        exit.setPreferredSize(new Dimension(200, 80));
        exit.setFont(this.getFont().deriveFont(Font.CENTER_BASELINE, 40F));
        cp2.add(exit);
        add(cp1);
        add(cp2);
        this.mainWindow = mainWindow;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == exit)
        {
            new Closer(mainWindow).diposeMainFrame();
        }
    }
}