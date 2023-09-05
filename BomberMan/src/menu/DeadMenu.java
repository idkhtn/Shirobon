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
 * Zawartość karty menu końca gry w CardLayout
 */
public class DeadMenu extends JPanel implements ActionListener {
    
    private JButton exit;
    private Main mainWindow;
    
    public DeadMenu(Main mainWindow)
    {
        setLayout(new GridLayout(2,1));
        exit = new JButton("Exit");
        exit.addActionListener(this);
        exit.setForeground(Color.white);
        exit.setBackground(Color.decode("#fc3a67"));
        JPanel cp1 = new JPanel();
        JPanel cp2 = new JPanel();
        cp1.setLayout(new BorderLayout());
        
        JLabel dead = new JLabel("YOU ARE DEAD!");
        dead.setHorizontalAlignment(JLabel.CENTER);
        dead.setVerticalAlignment(JLabel.CENTER);
        dead.setForeground(Color.decode("#fc3a67"));
        dead.setFont(this.getFont().deriveFont(Font.BOLD, 90F));
        cp1.add(dead, BorderLayout.CENTER);
        
        JLabel info = new JLabel("Click button to exit.");
        info.setHorizontalAlignment(JLabel.CENTER);
        info.setVerticalAlignment(JLabel.CENTER);
        info.setFont(this.getFont().deriveFont(Font.CENTER_BASELINE, 40F));
        cp1.add(info, BorderLayout.SOUTH);
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