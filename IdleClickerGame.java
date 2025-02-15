import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Idea: An Idle Game where the world was destroyed and now you need to rebuild it, by clicking on the screen.

public class IdleClickerGame {

    public static void main (String [] args){

        JFrame frame = new JFrame("Idle Armageddon");
        frame.setSize(360,360);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //  UI Basics, Layout: ==========================================================================================================================================

        int points = 0;
        int autoClickerLevel = 0;
        int autoClickerCost = 10;
        int clickerLevel = 0;
        int clickerLevelCost = 10;

        JLabel pointsLabel = new JLabel("Points: " + points);
        pointsLabel.setBorder (new EmptyBorder(0,10,0,0));//(top, left, bottom right)

        JLabel autoClickerLabel = new JLabel("Autoclicker Level:");
        autoClickerLabel.setBorder (new EmptyBorder(0,10,0,0));//(top, left, bottom right)

        JLabel clickerLabel = new JLabel("Clicker Level:");
        clickerLabel.setBorder (new EmptyBorder(0,10,0,0));//(top, left, bottom right)

        JButton clickerUpgradeButton = new JButton("Upgrade Clicker:");
       


        JButton clickButton = new JButton ("Clicke me!");
        JButton autoUpgradeButton = new JButton("Upgarde Autoclicker:");



    // Panel ========================================================================================================================================================

        JPanel panel = new JPanel();

        panel.add(pointsLabel);
        panel.add(clickButton);
        panel.add(clickerLabel);
        panel.add(clickerUpgradeButton);
        panel.add(autoClickerLabel);
        panel.add(autoUpgradeButton);

        
        panel.setLayout(new GridLayout(3,2,0,50)); //Spalten und Zeilen und horizentalen sowie vertikalen Abstand. (Mit Abstand ist der Abstand zwischen den Elementen gemeint)
        frame.add(panel);
        frame.setVisible(true);
    }
}
