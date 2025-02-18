import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Idea: An Idle Game where the world was destroyed and now you need to rebuild it, by clicking on the screen.
// Some reccources: https://machinations.io/articles/idle-games-and-how-to-design-them

public class IdleClickerGame {

    public static void main(String[] args) {
        
        // Frame creation 
        JFrame frame = new JFrame("Idle Armageddon");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // UI Basics, Defining all Labels, Buttons and Timers:
        // ==========================================================================================================================================
       
    /* structure of the UI 

    mainPanel (BorderLayout)
    │
    ├── gifLabel (NORTH) → picture above 
    │
    └── bottomPanel (CENTER) → contains three parts:
        ├── leftPanel → Click-Button & Points
        ├── centerPanel → Clicker-Upgrade
        ├── rightPanel → AutoClicker-Upgrade
    */
        
        // mainPanel
        JPanel mainPanel = new JPanel(new BorderLayout());

            //gifLabel
        ImageIcon gifIcon = new ImageIcon("c:\\Users\\Christian Schellhorn\\Dropbox\\Mein PC (DESKTOP-0JAOGE8)\\Desktop\\SideProjectIdleGame\\original-b89427a424892a34512fe8249396c0f8-ezgif.com-speed.gif");
        JLabel gifLabel = new JLabel(gifIcon);
        mainPanel.add(gifLabel, BorderLayout.NORTH);

        
            // bottomPanel
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

                // LeftPanel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JLabel pointsLabel = new JLabel("Points: 0");
        JButton clickButton = new JButton("Click me!");
        JLabel bossHealthLabel = new JLabel("Health: ");

        leftPanel.add(clickButton);
        leftPanel.add(pointsLabel);
        leftPanel.add(bossHealthLabel);

                // centerPanel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel clickerLabel = new JLabel("Clicker Level: 1");
        JLabel upgradeCostLabel = new JLabel("Upgrade Cost: 10");
        JButton clickerUpgradeButton = new JButton("Upgrade Clicker");
        centerPanel.add(clickerUpgradeButton);
        centerPanel.add(clickerLabel);
        centerPanel.add(upgradeCostLabel);

                // RightPanel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JLabel autoClickerLabel = new JLabel("Autoclicker Level: 0");
        JLabel upgradeAutoLabel = new JLabel("Upgrade Cost: 100");
        JButton autoUpgradeButton = new JButton("Upgrade Autoclicker");
        rightPanel.add(autoUpgradeButton);
        rightPanel.add(autoClickerLabel);
        rightPanel.add(upgradeAutoLabel);


        // Add panels to the bottom panel
        gbc.gridx = 0; // first position left
        gbc.gridy = 0;
        bottomPanel.add(leftPanel, gbc);

        gbc.gridx = 1; //center position
        gbc.gridy = 0;
        bottomPanel.add(centerPanel, gbc);

        gbc.gridx = 2; // right position
        gbc.gridy = 0;
        bottomPanel.add(rightPanel, gbc);

        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);


        GameLogic gameLogic = new GameLogic(pointsLabel, autoClickerLabel, clickerLabel, upgradeCostLabel,
                upgradeAutoLabel, bossHealthLabel);

        // Button for pointsLabel
        clickButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.incrementPoints();
            }
        });

        // Button for autoClickerLabel
        autoUpgradeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.incrementAuto();
            }
        });

        // Button for clickerLabel
        clickerUpgradeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.upgradeClicker();
            }
        });



    }
}

// Game Logic
// =============================================================================================================================

class GameLogic { // not public class, because per file there only can be one public class.

    int points = 0;
    int autoClickerLevel = 0;
    int autoClickerCost = 100;
    int clickerLevel = 1;
    int upgradeCost = 10 * clickerLevel;
    int bossHealth = 200;
    int bossLevel =1;

    private JLabel pointsLabel;
    private JLabel autoClickerLabel;
    private JLabel clickerLabel;
    private JLabel upgradeCostLabel;
    private JLabel upgradeAutoLabel;
    private JLabel bossHealthLabel;
    

    private Timer autoClickTimer;
    private boolean isAutoClickerRunning = false;

    // Constructor with all lables. As I understood it, it says that the lables we
    // are using in this class GameLogic equals the labels form main
    public GameLogic(JLabel pointsLabel, JLabel autoClickerLabel, JLabel clickerLabel, JLabel upgradeCostLabel,
            JLabel upgradeAutoLabel, JLabel bossHealthLabel) {
        this.pointsLabel = pointsLabel;
        this.autoClickerLabel = autoClickerLabel;
        this.clickerLabel = clickerLabel;
        this.upgradeCostLabel = upgradeCostLabel;
        this.upgradeAutoLabel = upgradeAutoLabel;
        this.bossHealthLabel = bossHealthLabel;

        updateUpgradeCostLabel();
        updateUpgradeAutoLabel();

        autoClickTimer = new Timer(1000 /* 1000 ms equals 1 second */ , new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                points += autoClickerLevel;
                updatePointsLabel();
            }
        });
    }

    // Function Nr.1: Click Button
    // ===================================================================================================

    void incrementPoints() {
        points += clickerLevel;// + clicker level, so that if the clicker level is upgraded, you get twice as
        bossHealth = bossHealth - clickerLevel;
        updateBossHealth();                        // many points
        updatePointsLabel();
    }

    // Function Nr.2: Upgrade Clicker Button
    // ==============================================================================================

    void upgradeClicker() {
        if (points >= upgradeCost) {
            points -= upgradeCost;
            clickerLevel++;

            upgradeCost *= 2 ;

            updatePointsLabel();
            updateClickerLabel();
            updateUpgradeCostLabel();
        }
    }

    // Function Nr.3: Autoclicker
    // ==========================================================================================================

    void incrementAuto() {
        if (points >= autoClickerCost) {
            points -= autoClickerCost;
            autoClickerLevel++;
            autoClickerCost *= 2;
            updatePointsLabel();
            updateAutoClickerLabel();
            updateUpgradeAutoLabel();

            if (!isAutoClickerRunning) {
                startAutoclicker();
                isAutoClickerRunning = true;
            }
        }
    }

    void startAutoclicker() {
        autoClickTimer.start();
    }










    // Methods to update the Labels
    // =========================================================================================================

    void updateBossHealth(){
        bossHealthLabel.setText("Health: " + (bossHealth- points));
    }

    void updatePointsLabel() {
        pointsLabel.setText("Points: " + points);
    }

    void updateClickerLabel() {
        clickerLabel.setText("Clicker level: " + clickerLevel);
    }

    void updateAutoClickerLabel() {
        autoClickerLabel.setText("Auto Clicker level: " + autoClickerLevel);
    }

    private void updateUpgradeCostLabel() {
        upgradeCostLabel.setText("Upgrade Cost: " + upgradeCost);
    }

    private void updateUpgradeAutoLabel() {
        upgradeAutoLabel.setText("Upgrade Cost: " + autoClickerCost);
    }
}
