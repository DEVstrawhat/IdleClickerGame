import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Idea: An Idle Game where the world was destroyed and now you need to rebuild it, by clicking on the screen.
// Some reccources: https://machinations.io/articles/idle-games-and-how-to-design-them

public class IdleClickerGame {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Idle Armageddon");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // UI Basics, Defining all Labels, Buttons and Timers:
        // ==========================================================================================================================================

        JLabel pointsLabel = new JLabel("Points: 0");
        pointsLabel.setBorder(new EmptyBorder(0, 10, 0, 0));// (top, left, bottom right)

        JLabel autoClickerLabel = new JLabel("Autoclicker Level: 0");
        autoClickerLabel.setBorder(new EmptyBorder(0, 10, 0, 0));// (top, left, bottom right)

        JLabel clickerLabel = new JLabel("Clicker Level: 0");
        clickerLabel.setBorder(new EmptyBorder(0, 10, 0, 0));// (top, left, bottom right)

        JLabel upgradeCostLabel = new JLabel("Clicker Upgrade Cost: 10");
        upgradeCostLabel.setBorder(new EmptyBorder(0, 10, 0, 0));// (top, left, bottom right)

        JLabel upgradeAutoLabel = new JLabel("Auto Clicker Upgrade Cost: 100");
        upgradeAutoLabel.setBorder(new EmptyBorder(0, 10, 0, 0));// (top, left, bottom right)

        GameLogic gameLogic = new GameLogic(pointsLabel, autoClickerLabel, clickerLabel, upgradeCostLabel,
                upgradeAutoLabel);

        // Button for pointsLabel
        JButton clickButton = new JButton("Clicke me!");
        clickButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.incrementPoints();
            }
        });

        // Button for autoClickerLabel
        JButton autoUpgradeButton = new JButton("Upgrade Autoclicker:");
        autoUpgradeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.incrementAuto();
            }
        });

        // Button for clickerLabel
        JButton clickerUpgradeButton = new JButton("Upgrade Clicker");
        clickerUpgradeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.upgradeClicker();
            }
        });

        // Panel
        // ========================================================================================================================================================

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(pointsLabel);
        panel.add(clickButton);
        panel.add(clickerLabel);
        panel.add(upgradeCostLabel);
        panel.add(clickerUpgradeButton);
        panel.add(autoClickerLabel);
        panel.add(upgradeAutoLabel);
        panel.add(autoUpgradeButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}

// Game Logic
// =============================================================================================================================

class GameLogic { // not public class, because per file there only can be one public class.

    int points = 0;
    int autoClickerLevel = 1;
    int autoClickerCost = 100;
    int clickerLevel = 1;
    int upgradeCost = 10 * clickerLevel;

    private JLabel pointsLabel;
    private JLabel autoClickerLabel;
    private JLabel clickerLabel;
    private JLabel upgradeCostLabel;
    private JLabel upgradeAutoLabel;

    private Timer autoClickTimer;
    private boolean isAutoClickerRunning = false;

    // Constructor with all lables. As I understood it, it says that the lables we
    // are using in this class GameLogic equals the labels form main
    public GameLogic(JLabel pointsLabel, JLabel autoClickerLabel, JLabel clickerLabel, JLabel upgradeCostLabel,
            JLabel upgradeAutoLabel) {
        this.pointsLabel = pointsLabel;
        this.autoClickerLabel = autoClickerLabel;
        this.clickerLabel = clickerLabel;
        this.upgradeCostLabel = upgradeCostLabel;
        this.upgradeAutoLabel = upgradeAutoLabel;

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
                               // many points
        updatePointsLabel();
    }

    // Function Nr.2: Upgrade Clicker Button
    // ==============================================================================================

    void upgradeClicker() {
        if (points >= upgradeCost) {
            points -= upgradeCost;
            clickerLevel++;

            upgradeCost = 10 * clickerLevel;

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
        upgradeCostLabel.setText("Clicker Upgrade Cost: " + upgradeCost);
    }

    private void updateUpgradeAutoLabel() {
        upgradeAutoLabel.setText("Auto Clicker Upgrade Cost: " + autoClickerCost);
    }
}
