import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Idea: An Idle Game where the world was destroyed and now you need to rebuild it, by clicking on the screen.

public class IdleClickerGame {

<<<<<<< HEAD
    //test123
=======
    //test
>>>>>>> 25fcbb9eb1cd3afc3e56724fbe7034b19cd409c8


    private static ArrayList<ImageIcon> monsterGifs = new ArrayList<>();
    private static int currentGifIndex = 0;

    private static void loadMonsterGifs() {
    for (int i = 1; i <= 12; i++) {  // Falls deine Dateien "monster1.gif", "monster2.gif" usw. heißen
        String path = "resources/gifs/Monster" +i +".gif";
        monsterGifs.add(new ImageIcon((path)));
    }

    }

    public static void main(String[] args) throws FontFormatException, IOException {
        
        // Frame creation 
        JFrame frame = new JFrame("Idle MonsterHunter");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       
        Font pixelifyFont = Font.createFont(Font.TRUETYPE_FONT, new File ("resources/font/PixelifySans-VariableFont_wght.ttf")).deriveFont(24f);

        

    


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

        //topPanel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 10)); // Abstand vom Rand



            //gifLabel
        loadMonsterGifs();
        ImageIcon gifIcon = monsterGifs.get(currentGifIndex);
        JLabel gifLabel = new JLabel(gifIcon);


        JLabel bossLevel = new JLabel("Boss level: 1");
        bossLevel.setFont(pixelifyFont);


        JLabel bossHealthLabel = new JLabel("Health: 20");
        bossHealthLabel.setFont(pixelifyFont);



        topPanel.add(bossLevel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(bossHealthLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(gifLabel);
        
            // upgrade Panel
        JPanel upgradeComponent = new JPanel(new BorderLayout());
        upgradeComponent.setLayout(new BoxLayout(upgradeComponent, BoxLayout.Y_AXIS));

        upgradeComponent.setBorder(BorderFactory.createEmptyBorder(100, 10, 10, 100)); // Padding

                // LeftPanel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 50, 10)); // Padding

        JLabel pointsLabel = new JLabel("Money: 0");
        pointsLabel.setFont(pixelifyFont);

        JButton clickButton = new JButton("Fight!");
        clickButton.setMargin(new Insets(50, 500, 50, 425)); // Oben, Links, Unten, Rechts
        clickButton.setFont(pixelifyFont);




        leftPanel.add(clickButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 20px vertikaler Abstand

        leftPanel.add(pointsLabel);

                // centerPanel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel clickerLabel = new JLabel("Clicker Level: 1");
        clickerLabel.setFont(pixelifyFont);
        JLabel upgradeOptionsLabel = new JLabel("Upgrade your skills!");
        upgradeOptionsLabel.setFont(pixelifyFont);
        JLabel upgradeCostLabel = new JLabel("Upgrade Cost: 10");
        upgradeCostLabel.setFont(pixelifyFont);
        JButton clickerUpgradeButton = new JButton("Upgrade Clicker");
        clickerUpgradeButton.setMargin(new Insets(50, 200, 50, 200)); // Oben, Links, Unten, Rechts
        clickerUpgradeButton.setFont(pixelifyFont);


        centerPanel.add(upgradeOptionsLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 50))); // 20px vertikaler Abstand

        centerPanel.add(clickerUpgradeButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand

        centerPanel.add(clickerLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand

        centerPanel.add(upgradeCostLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand


                // RightPanel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JLabel autoClickerLabel = new JLabel("Autoclicker Level: 0");
        autoClickerLabel.setFont(pixelifyFont);
        JLabel upgradeAutoLabel = new JLabel("Upgrade Cost: 100");
        upgradeAutoLabel.setFont(pixelifyFont);
        JButton autoUpgradeButton = new JButton("Upgrade Autoclicker");
        autoUpgradeButton.setMargin(new Insets(50, 200, 50, 150)); // Oben, Links, Unten, Rechts
        autoUpgradeButton.setFont(pixelifyFont);



        rightPanel.add(autoUpgradeButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand

        rightPanel.add(autoClickerLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand

        rightPanel.add(upgradeAutoLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand

        
        upgradeComponent.add(centerPanel, BorderLayout.CENTER);
        upgradeComponent.add(rightPanel, BorderLayout.EAST);
       

        mainPanel.add(topPanel, BorderLayout.WEST);
        mainPanel.add(leftPanel, BorderLayout.SOUTH);
        mainPanel.add(upgradeComponent, BorderLayout.EAST);
    


        frame.add(mainPanel);
        frame.setVisible(true);


        GameLogic gameLogic = new GameLogic(pointsLabel, autoClickerLabel, clickerLabel, upgradeCostLabel,
                upgradeAutoLabel, bossHealthLabel, bossLevel, gifLabel, monsterGifs);

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
    int bossHealth = 20;
    int bossLevelInt = 1;
    int currentGifIndex =1;
    private JLabel pointsLabel;
    private JLabel autoClickerLabel;
    private JLabel clickerLabel;
    private JLabel upgradeCostLabel;
    private JLabel upgradeAutoLabel;
    private JLabel bossHealthLabel;
    private JLabel bossLevel;
    private JLabel gifLabel;

    ArrayList <ImageIcon> monsterGifs;
    

    private Timer autoClickTimer;
    private boolean isAutoClickerRunning = false;

    // Constructor with all lables. As I understood it, it says that the lables we
    // are using in this class GameLogic equals the labels form main
    public GameLogic(JLabel pointsLabel, JLabel autoClickerLabel, JLabel clickerLabel, JLabel upgradeCostLabel,
            JLabel upgradeAutoLabel, JLabel bossHealthLabel, JLabel bossLevel, JLabel gifLabel, ArrayList<ImageIcon> monsterGifs) {
        this.pointsLabel = pointsLabel;
        this.autoClickerLabel = autoClickerLabel;
        this.clickerLabel = clickerLabel;
        this.upgradeCostLabel = upgradeCostLabel;
        this.upgradeAutoLabel = upgradeAutoLabel;
        this.bossHealthLabel = bossHealthLabel;
        this.bossLevel = bossLevel;
        this.gifLabel = gifLabel;
        this.monsterGifs = monsterGifs;

        updateUpgradeCostLabel();
        updateUpgradeAutoLabel();

        autoClickTimer = new Timer(1000 /* 1000 ms equals 1 second */ , new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                points += autoClickerLevel;
                bossHealth -= autoClickerLevel;
                updateBossHealth();
                checkBossHealth();
            }
        });
    }

    // Function Nr.1: Click Button
    // ===================================================================================================

    void incrementPoints() {
        bossHealth = bossHealth - clickerLevel;
        checkBossHealth();
        updateBossHealth();                        
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
    void updateBossLevel(){
        bossLevel.setText("Boss Level: " + bossLevelInt);
    }


    void updateBossHealth(){
        bossHealthLabel.setText("Health: " + bossHealth);
    }

    void updatePointsLabel() {
        pointsLabel.setText("Money: " + points);
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
    
    private void checkBossHealth(){
    if (bossHealth <= 0){
        bossHealth =0;
        updateBossHealth();
        bossLevelInt++;
        bossHealth = 20 * bossLevelInt;
        updateBossLevel();
        points += 10 * bossLevelInt;
        updatePointsLabel();

        // loop for the monstergifs 
        if (currentGifIndex < monsterGifs.size() - 1){
            currentGifIndex ++;
        }else {
            currentGifIndex = 0;
        }
        gifLabel.setIcon(monsterGifs.get(currentGifIndex));

    }
}
}
