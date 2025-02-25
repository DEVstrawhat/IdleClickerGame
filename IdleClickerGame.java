
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

// Idea: An Idle Game where you fight monsters to get stronger and buq more upgrades
public class IdleClickerGame {

    private static ArrayList<ImageIcon> monsterGifs = new ArrayList<>();
    private static int currentGifIndex = 0;
    private static Random random = new Random();

    
    private static ImageIcon resizeGif(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(image);
    }

    private static void loadMonsterGifs() {
        int gifWidth = 700;  // Breite des GIFs
        int gifHeight = 500;  // Höhe des GIFs
        
        File folder = new File("resources/png");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));
    
        if (files != null && files.length > 0) {
            for (File file : files) {
                ImageIcon resizedIcon = resizeGif(file.getAbsolutePath(), gifWidth, gifHeight);
                monsterGifs.add(resizedIcon);
    
    }
}
}



    public static void main(String[] args) throws FontFormatException, IOException {

        // Frame creation 
        JFrame frame = new JFrame("Idle MonsterHunter");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Background music
        //MusicPlayer musicPlayer = new MusicPlayer();
        //musicPlayer.playMusic("dungeonBeat.wav");
        //Font
        Font pixelifyFont = Font.createFont(Font.TRUETYPE_FONT, new File ("resources/font/PixelifySans-VariableFont_wght.ttf")).deriveFont(24f);  

        JProgressBar healthBar = new JProgressBar(0, 10);

        healthBar.setStringPainted(true); // Prozentanzeige aktivieren
        healthBar.setForeground(Color.RED); // Farbe der Leiste (Lebensanzeige)
        healthBar.setBackground(Color.DARK_GRAY); 
        int maxHP = healthBar.getMaximum();




    // UI Basics, Defining all Labels, Buttons and Timers:
    // ==========================================================================================================================================
       
    /* structure of the UI 

    mainPanel (BorderLayout)
    │
    ├── left Panel
    │
    └── rightPanel 
    │
  
    */
        
        // mainPanel======================================================================================================================================
        JPanel mainPanel = new BackgroundPanel( "resources/background/backgroundimage.jpg");
        mainPanel.setLayout(new BorderLayout());

        //leftPanel=======================================================================================================================================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 0)); // Abstand vom Rand
        leftPanel.setOpaque(false); // Transparent

        loadMonsterGifs();
        currentGifIndex = random.nextInt(monsterGifs.size());
        ImageIcon gifIcon = monsterGifs.get(currentGifIndex);
        JLabel gifLabel = new JLabel(gifIcon);
        JLabel bossLevel = new JLabel("Monster level: 1");
        bossLevel.setFont(pixelifyFont);
        bossLevel.setForeground(Color.WHITE);
        JLabel bossHealthLabel = new JLabel("Health: 10");
        bossHealthLabel.setForeground(Color.WHITE);
        bossHealthLabel.setFont(pixelifyFont);
        JLabel zoneLabel = new JLabel("Zone: 1");
        zoneLabel.setForeground(Color.WHITE);
        zoneLabel.setFont(pixelifyFont);
        JLabel dmgPerSecond = new JLabel("Automated DMG: 0/s");
        dmgPerSecond.setForeground(Color.WHITE);
        dmgPerSecond.setFont(pixelifyFont);
    
        leftPanel.add(zoneLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(bossLevel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(dmgPerSecond);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 100)));
        leftPanel.add(gifLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(healthBar);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(bossHealthLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    
    
        
       

        
        // RightPanel======================================================================================================================================
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 50, 10)); // Padding
        rightPanel.setOpaque(false); // Transparent

        
        JLabel pointsLabel = new JLabel("Money: 0");
        pointsLabel.setFont(pixelifyFont);
        pointsLabel.setForeground(Color.WHITE);
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
        JLabel autoClickerLabel = new JLabel("Autoclicker Level: 0");
        autoClickerLabel.setFont(pixelifyFont);
        autoClickerLabel.setForeground(Color.WHITE);
        JLabel upgradeAutoLabel = new JLabel("Upgrade Cost: 100");
        upgradeAutoLabel.setFont(pixelifyFont);
        upgradeAutoLabel.setForeground(Color.WHITE);
        JButton autoUpgradeButton = new JButton("Upgrade Autoclicker");
        autoUpgradeButton.setMargin(new Insets(50, 175, 50, 175)); // Oben, Links, Unten, Rechts
        autoUpgradeButton.setFont(pixelifyFont);
       
        rightPanel.add(upgradeOptionsLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50))); // 50px vertical distance
        rightPanel.add(pointsLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 50px vertical distance
        rightPanel.add(clickerUpgradeButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); 
        rightPanel.add(clickerLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); 
        rightPanel.add(upgradeCostLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); 
        rightPanel.add(autoUpgradeButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand
        rightPanel.add(autoClickerLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand
        rightPanel.add(upgradeAutoLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25))); // 20px vertikaler Abstand

        

        //gluing everything together  ===================================================================================================================


        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);
    
        frame.add(mainPanel);
        frame.setVisible(true);

        GameLogic gameLogic = new GameLogic(pointsLabel, autoClickerLabel, clickerLabel, upgradeCostLabel,
        upgradeAutoLabel, bossHealthLabel, bossLevel, gifLabel, monsterGifs, zoneLabel, dmgPerSecond, healthBar, maxHP);

        clickerUpgradeButton.setToolTipText(gameLogic.getClickerUpgradeInfo());

        // Buttons =====================================================================================================================================

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

        gifLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gameLogic.incrementPoints(); // Simuliert den Schaden, wenn auf das Bild geklickt wird
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
    int maxHP;


    private JLabel pointsLabel;
    private JLabel autoClickerLabel;
    private JLabel clickerLabel;
    private JLabel upgradeCostLabel;
    private JLabel upgradeAutoLabel;
    private JLabel bossHealthLabel;
    private JLabel bossLevel;
    private JLabel gifLabel;
    private JLabel zoneLabel;
    private JLabel dmgPerSecond;
    private static Random random = new Random();
    private JProgressBar healthBar;



    ArrayList <ImageIcon> monsterGifs;
    ArrayList <ImageIcon> monsterDeathGifs;
    

    private Timer autoClickTimer;
    private boolean isAutoClickerRunning = false;

    // Constructor with all lables. As I understood it, it says that the lables we
    // are using in this class GameLogic equals the labels form main
    public GameLogic(JLabel pointsLabel, JLabel autoClickerLabel, JLabel clickerLabel, JLabel upgradeCostLabel,
            JLabel upgradeAutoLabel, JLabel bossHealthLabel, JLabel bossLevel, JLabel gifLabel, ArrayList<ImageIcon> monsterGifs, JLabel zoneLabel, JLabel dmgPerSecond, JProgressBar healthBar, int maxHP) {
        this.pointsLabel = pointsLabel;
        this.autoClickerLabel = autoClickerLabel;
        this.clickerLabel = clickerLabel;
        this.upgradeCostLabel = upgradeCostLabel;
        this.upgradeAutoLabel = upgradeAutoLabel;
        this.bossHealthLabel = bossHealthLabel;
        this.bossLevel = bossLevel;
        this.gifLabel = gifLabel;
        this.monsterGifs = monsterGifs;
        this.zoneLabel = zoneLabel;
        this.dmgPerSecond = dmgPerSecond;
        this.healthBar = healthBar;
        this.maxHP = maxHP;

        healthBar.setMaximum(maxHP);
        healthBar.setValue(bossHealth);

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
            updateDmgPerSecond();

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
        healthBar.setValue(bossHealth);
    }

    void updatePointsLabel() {
        pointsLabel.setText("Money: " + points);
    }

    void updateDmgPerSecond(){
        dmgPerSecond.setText("Automated DMG: " + (5*ownedAuto1) +"/s");
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
    public String getClickerUpgradeInfo() {
        return "Clicker Level: " + clickerLevel + " | Cost: " + calculateNextCost();
    }
    
    private void updateUpgradeAutoLabel() {
        int nextAutoCost = calculateNextAutoCost1();
        upgradeAutoLabel.setText("Upgrade Cost: " + nextAutoCost);
    }
    
    private void checkBossHealth(){

        if (bossHealth <= 0) {
                bossHealth = 0;
                updateBossHealth();

        
                Timer deathTimer = new Timer(300, new ActionListener() {
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
                        maxHP = nextMonster;
                        bossHealth = bossHealth + nextMonster;
                        healthBar.setMaximum(maxHP);
                        healthBar.setValue(Math.max(0, bossHealth));
                        points = points + nextProduction;
                        updateBossHealth();
                        updateBossLevel();
                        updatePointsLabel();
                        updateZoneLabel();
                        
                        
                        currentGifIndex = random.nextInt(monsterGifs.size());
                        gifLabel.setIcon(monsterGifs.get(currentGifIndex));
                       

                    }
                });
                deathTimer.setRepeats(false); // Timer soll nur einmal ausgeführt werden
                deathTimer.start();
            }
        }
        
    }


