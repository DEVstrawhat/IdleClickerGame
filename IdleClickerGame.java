import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


// Idea: An Idle Game where you fight monsters to get stronger and buq more upgrades

public class IdleClickerGame {


    private static ArrayList<ImageIcon> monsterGifs = new ArrayList<>();
    private static ArrayList<ImageIcon> monsterDeathGifs = new ArrayList<>();
    private static int currentGifIndex = 0;

    private static void loadMonsterGifs() {
    for (int i = 1; i <= 9; i++) {  // Falls deine Dateien "monster1.gif", "monster2.gif" usw. heißen
        String path = "resources/gifs/Monster" +i +".gif";
        String deathpath = "resources/deathgifs/Monster" +i +".gif";

        monsterGifs.add(new ImageIcon((path)));
        monsterDeathGifs.add(new ImageIcon((deathpath)));
    }

    }

    public static void main(String[] args) throws FontFormatException, IOException {
        
        // Frame creation 
        JFrame frame = new JFrame("Idle MonsterHunter");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Background music
    MusicPlayer musicPlayer = new MusicPlayer();
    musicPlayer.playMusic("dungeonBeat.wav");
        //Font
        Font pixelifyFont = Font.createFont(Font.TRUETYPE_FONT, new File ("resources/font/PixelifySans-VariableFont_wght.ttf")).deriveFont(24f);  


    // UI Basics, Defining all Labels, Buttons and Timers:
    // ==========================================================================================================================================
       
    /* structure of the UI 

    mainPanel (BorderLayout)
    │
    ├── topPanel (Upper left corner) → gifLabel, bosslevel and bosshealthlabel
    │
    └── upgradePanel (CENTER) → Click Button & Money
    │ 
    └── bottomPanel (CENTER) → contains two parts:
    │   ├── centerPanel → Clicker-Upgrade
    │   ├── rightPanel → AutoClicker-Upgrade
    */
        
        // mainPanel======================================================================================================================================
        JPanel mainPanel = new BackgroundPanel( "resources/background/backgroundimage.jpg");
        mainPanel.setLayout(new BorderLayout());

        //topPanel=======================================================================================================================================
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 10)); // Abstand vom Rand
        topPanel.setOpaque(false); // Transparent

        loadMonsterGifs();
        ImageIcon gifIcon = monsterGifs.get(currentGifIndex);
        JLabel gifLabel = new JLabel(gifIcon);
        ImageIcon deathGifIcon = monsterDeathGifs.get(currentGifIndex);
        JLabel deathGifLabel = new JLabel(deathGifIcon);
        JLabel bossLevel = new JLabel("Monster level: 1");
        bossLevel.setFont(pixelifyFont);
        bossLevel.setForeground(Color.WHITE);
        JLabel bossHealthLabel = new JLabel("Health: 10");
        bossHealthLabel.setForeground(Color.WHITE);
        bossHealthLabel.setFont(pixelifyFont);
        JLabel zoneLabel = new JLabel("Zone: 1");
        zoneLabel.setForeground(Color.WHITE);
        zoneLabel.setFont(pixelifyFont);

        topPanel.add(zoneLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(bossLevel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(bossHealthLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(gifLabel);
        
        
        // LeftPanel======================================================================================================================================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 50, 10)); // Padding
        leftPanel.setOpaque(false); // Transparent

        
        JLabel pointsLabel = new JLabel("Money: 0");
        pointsLabel.setFont(pixelifyFont);
        pointsLabel.setForeground(Color.WHITE);
        JButton clickButton = new JButton("Fight!");
        clickButton.setMargin(new Insets(50, 500, 50, 425)); // Oben, Links, Unten, Rechts
        clickButton.setFont(pixelifyFont);
        
        leftPanel.add(clickButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 20px vertikaler Abstand
        leftPanel.add(pointsLabel);


        // upgrade Panel ===============================================================================================================================
        JPanel upgradeComponent = new JPanel(new BorderLayout());
        upgradeComponent.setLayout(new BoxLayout(upgradeComponent, BoxLayout.Y_AXIS));
        upgradeComponent.setBorder(BorderFactory.createEmptyBorder(100, 10, 10, 85)); 
        upgradeComponent.setOpaque(false); // Transparent


        // centerPanel ==================================================================================================================================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false); // Transparent

        JLabel clickerLabel = new JLabel("Clicker Level: 1");
        clickerLabel.setFont(pixelifyFont);
        clickerLabel.setForeground(Color.WHITE);
        JLabel upgradeOptionsLabel = new JLabel("Upgrade your skills!");
        upgradeOptionsLabel.setFont(pixelifyFont);
        upgradeOptionsLabel.setForeground(Color.WHITE);
        upgradeOptionsLabel.setFont(upgradeOptionsLabel.getFont().deriveFont(Font.BOLD));
        JLabel upgradeCostLabel = new JLabel("Upgrade Cost: 10");
        upgradeCostLabel.setFont(pixelifyFont);
        upgradeCostLabel.setForeground(Color.WHITE);
        JButton clickerUpgradeButton = new JButton("Upgrade Clicker");
        clickerUpgradeButton.setMargin(new Insets(50, 175, 50, 175)); 
        clickerUpgradeButton.setFont(pixelifyFont);

        centerPanel.add(upgradeOptionsLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 50))); // 50px vertical distance
        centerPanel.add(clickerUpgradeButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25))); 
        centerPanel.add(clickerLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25))); 
        centerPanel.add(upgradeCostLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25))); 


        // RightPanel =================================================================================================================================
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false); // Transparent

        JLabel autoClickerLabel = new JLabel("Autoclicker Level: 0");
        autoClickerLabel.setFont(pixelifyFont);
        autoClickerLabel.setForeground(Color.WHITE);
        JLabel upgradeAutoLabel = new JLabel("Upgrade Cost: 100");
        upgradeAutoLabel.setFont(pixelifyFont);
        
        upgradeAutoLabel.setForeground(Color.WHITE);
        JButton autoUpgradeButton = new JButton("Upgrade Autoclicker");
        autoUpgradeButton.setMargin(new Insets(50, 175, 50, 175)); // Oben, Links, Unten, Rechts
        autoUpgradeButton.setFont(pixelifyFont);

        rightPanel.add(autoUpgradeButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand
        rightPanel.add(autoClickerLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand
        rightPanel.add(upgradeAutoLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand

        

        //gluing everything together  ===================================================================================================================
        upgradeComponent.add(centerPanel, BorderLayout.CENTER);
        upgradeComponent.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.WEST);
        mainPanel.add(leftPanel, BorderLayout.SOUTH);
        mainPanel.add(upgradeComponent, BorderLayout.EAST);
    
        frame.add(mainPanel);
        frame.setVisible(true);

        GameLogic gameLogic = new GameLogic(pointsLabel, autoClickerLabel, clickerLabel, upgradeCostLabel,
        upgradeAutoLabel, bossHealthLabel, bossLevel, gifLabel, deathGifLabel, monsterGifs, monsterDeathGifs, zoneLabel);


        // Buttons =====================================================================================================================================
        clickButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.incrementPoints();
            }
        });

        autoUpgradeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.incrementAuto();
            }
        });

        clickerUpgradeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.upgradeClicker();
            }
        });
    }
}


class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        this.backgroundImage = new ImageIcon(imagePath).getImage();
    }
//test
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Hintergrund zeichnen
    }
}

// Game Logic
// =============================================================================================================================

class GameLogic { // not public class, because per file there only can be one public class.

    int points = 0;
    int autoClickerLevel = 0;
    int autoClickerCost = 100;
    int clickerLevel = 1;
    int bossHealth = 10;
    int bossLevelInt = 1;
    int currentGifIndex =0;
    double costbase = 5;
    double rategrowth = 1.07;
    int owned = 0;
    int multipliers = 1;
    double productionBase = 1;
    double rateMonster = 1.55;
    int currrentZone = 1;
    int defeatedMonsterInCurrentZone = 0;
    final int MonsterInZone = 10;
    double costbaseAuto1 = 50;
    double rategrowthAuto1 = 1.22;
    int ownedAuto1 = 0;


    private JLabel pointsLabel;
    private JLabel autoClickerLabel;
    private JLabel clickerLabel;
    private JLabel upgradeCostLabel;
    private JLabel upgradeAutoLabel;
    private JLabel bossHealthLabel;
    private JLabel bossLevel;
    private JLabel gifLabel;
    private JLabel deathGifLabel;
    private boolean isDeathAnimationActive = false; 
    private JLabel zoneLabel;


    ArrayList <ImageIcon> monsterGifs;
    ArrayList <ImageIcon> monsterDeathGifs;
    

    private Timer autoClickTimer;
    private boolean isAutoClickerRunning = false;

    // Constructor with all lables. As I understood it, it says that the lables we
    // are using in this class GameLogic equals the labels form main
    public GameLogic(JLabel pointsLabel, JLabel autoClickerLabel, JLabel clickerLabel, JLabel upgradeCostLabel,
            JLabel upgradeAutoLabel, JLabel bossHealthLabel, JLabel bossLevel, JLabel gifLabel, JLabel deathGifLabel, ArrayList<ImageIcon> monsterGifs, ArrayList<ImageIcon> monsterDeathGifs, JLabel zoneLabel) {
        this.pointsLabel = pointsLabel;
        this.autoClickerLabel = autoClickerLabel;
        this.clickerLabel = clickerLabel;
        this.upgradeCostLabel = upgradeCostLabel;
        this.upgradeAutoLabel = upgradeAutoLabel;
        this.bossHealthLabel = bossHealthLabel;
        this.bossLevel = bossLevel;
        this.gifLabel = gifLabel;
        this.deathGifLabel = deathGifLabel;
        this.monsterGifs = monsterGifs;
        this.monsterDeathGifs = monsterDeathGifs;
        this.zoneLabel = zoneLabel;

        updateUpgradeCostLabel();
        updateUpgradeAutoLabel(); 

        autoClickTimer = new Timer(1000 /* 1000 ms equals 1 second */ , new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                points += autoClickerLevel;
                bossHealth = bossHealth - (5* ownedAuto1);
                updateBossHealth();
                checkBossHealth();
            }
        });
    }


    // Calculation formulas ======================================================================================================
    // How much Gold shoulod drop after a monster is dead
    int calculateNextProduction(){
        return (int) ((productionBase * currrentZone *multipliers)); //I will add multipliers later in the game 
    }
    // How much does the next upgrade costs
    int calculateNextCost(){
        return (int) (((costbase + owned) *Math.pow(rategrowth, owned)));
    }
//test
    // How much life has the next monster 
    int calculateNextMonster(){
        return (int) (10*(currrentZone- 1 + Math.pow(rateMonster, currrentZone-1)));
    }

    // How much cost for autoclicker 
    int calculateNextAutoCost1(){
        return (int) (costbaseAuto1 *Math.pow(rategrowthAuto1, ownedAuto1));
    }

    // Function Nr.1: Click Button
    // ===================================================================================================

    void incrementPoints() {
        if (bossHealth > 0) { 
        bossHealth = bossHealth - clickerLevel;
        checkBossHealth();
        updateBossHealth();                        
    }
}


    // Function Nr.2: Upgrade Clicker Button
    // ==============================================================================================

    void upgradeClicker() {
        int nextCost = calculateNextCost();
        if (points >= nextCost) {
            points -= nextCost;
            clickerLevel++;
            owned++;
            updatePointsLabel();
            updateClickerLabel();
            updateUpgradeCostLabel();
            
        }
    }

    // Function Nr.3: Autoclicker
    // ==========================================================================================================

    void incrementAuto() {
        int nextAutoCost = calculateNextAutoCost1();
        
        if (points >= nextAutoCost) {
            points -= nextAutoCost;
            
            autoClickerLevel++;
            ownedAuto1++;
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

    void updateZoneLabel(){
        zoneLabel.setText("Zone: " + currrentZone);
    }

    private void updateUpgradeCostLabel() {
    int nextCost = calculateNextCost();
        upgradeCostLabel.setText("Upgrade Cost: " + nextCost);
      

    
    }

    private void updateUpgradeAutoLabel() {
        int nextAutoCost = calculateNextAutoCost1();
        upgradeAutoLabel.setText("Upgrade Cost: " + nextAutoCost);
    }
    
    private void checkBossHealth(){

        if (bossHealth <= 0 && !isDeathAnimationActive) {
                bossHealth = 0;
                updateBossHealth();
                isDeathAnimationActive = true;

                gifLabel.setIcon(monsterDeathGifs.get(currentGifIndex));
        
                Timer deathTimer = new Timer(700, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        defeatedMonsterInCurrentZone++;

                        if (defeatedMonsterInCurrentZone >= MonsterInZone){
                            currrentZone++;
                            defeatedMonsterInCurrentZone =0;

                        }
                        int nextProduction = calculateNextProduction();
                        int nextMonster = calculateNextMonster();
                        bossLevelInt++;
                        bossHealth = bossHealth + nextMonster;
                        points = points + nextProduction;
                        updateBossHealth();
                        updateBossLevel();
                        updatePointsLabel();
                        updateZoneLabel();
                        
        
                        currentGifIndex = (currentGifIndex + 1) % monsterGifs.size();
                        gifLabel.setIcon(monsterGifs.get(currentGifIndex));
                        isDeathAnimationActive = false;

                    }
                });
                deathTimer.setRepeats(false); // Timer soll nur einmal ausgeführt werden
                deathTimer.start();
            }
        }
        
    }


