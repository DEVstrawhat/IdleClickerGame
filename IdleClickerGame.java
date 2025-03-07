import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class IdleClickerGame {

    private static ArrayList<ImageIcon> monsterGifs = new ArrayList<>();
    private static ArrayList<ImageIcon> bossGifs = new ArrayList<>();
    private static int currentGifIndex = 0;
    private static Random random = new Random();

    private static ImageIcon resizeGif(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(image);
    }

    private static void loadMonsterGifs() {
        int gifWidth = 700;
        int gifHeight = 500;

        File folder = new File("resources/png");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));

        if (files != null && files.length > 0) {
            for (File file : files) {
                ImageIcon resizedIcon = resizeGif(file.getAbsolutePath(), gifWidth, gifHeight);
                monsterGifs.add(resizedIcon);
            }
        }
    }

    private static void loadBossGifs() {
        int gifWidth = 700;
        int gifHeight = 500;
    
        File folder = new File("resources/boss");
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
    
        if (files != null && files.length > 0) {
            for (File file : files) {
                ImageIcon resizedIcon = resizeGif(file.getAbsolutePath(), gifWidth, gifHeight);
                bossGifs.add(resizedIcon);
            }
        }
    }

   

    public static void main(String[] args) throws FontFormatException, IOException {
        JFrame frame = new JFrame("Idle MonsterHunter");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //MusicPlayer musicPlayer = new MusicPlayer();
        //musicPlayer.playMusic("dungeonBeat.wav");

        Font pixelifyFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/BitPotionExt.ttf")).deriveFont(35f);


        // BAckground for UI UpgradePanel
        class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String filePath) {
        backgroundImage = new ImageIcon(filePath).getImage();
            }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}


        // UI Layout =====================================================================================================

        /*
         * 1. Main Panel 
         *  1.1 left Panel 
         *      1.1.1 topleft Panel (including zone label, monster level label, automated dmg label)
         *      1.1.2  center Panel (including monster- and boss png, health progressbar and health label )
         *      1.1.3 topright Panel (including hint label)
         * 1.2 right Panel (including all upgarde labels)
         */

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(new BorderLayout());

        // 1.1 left Panel ===================================================================================================

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        leftPanel.setOpaque(false);

            // 1.1.1 topleft Panel ============================================================================================

        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        topWrapper.setOpaque(false);

        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setLayout(new BoxLayout(topLeftPanel, BoxLayout.Y_AXIS));
        topLeftPanel.setOpaque(false);

        JLabel bossLevel = new JLabel("Monster Level: 1");
        bossLevel.setFont(pixelifyFont);
        bossLevel.setForeground(Color.WHITE);

        JLabel zoneLabel = new JLabel("Zone: 1");
        zoneLabel.setForeground(Color.WHITE);
        zoneLabel.setFont(pixelifyFont);

        JLabel dmgPerSecond = new JLabel("Automated DMG: 0/s");
        dmgPerSecond.setForeground(Color.WHITE);
        dmgPerSecond.setFont(pixelifyFont);

        topLeftPanel.add(zoneLabel);
        topLeftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topLeftPanel.add(bossLevel);
        topLeftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topLeftPanel.add(dmgPerSecond);

        topWrapper.add(topLeftPanel);

        // 1.1.2. center Panel =======================================================================================================

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JProgressBar healthBar = new JProgressBar(0, 10);
        healthBar.setStringPainted(true);
        healthBar.setForeground(Color.WHITE);
        healthBar.setBackground(Color.BLACK);
        healthBar.setPreferredSize(new Dimension(500, 20));
        int maxHP = healthBar.getMaximum();

        JPanel healthBarPanel = new JPanel();
        healthBarPanel.setLayout(new BoxLayout(healthBarPanel, BoxLayout.X_AXIS));
        healthBarPanel.setOpaque(false);
        healthBarPanel.add(Box.createHorizontalGlue());
        healthBar.setMaximumSize(new Dimension(500, 20));
        healthBarPanel.add(healthBar);
        healthBarPanel.add(Box.createHorizontalGlue());

        loadMonsterGifs();
        loadBossGifs();
        currentGifIndex = random.nextInt(monsterGifs.size());
        ImageIcon gifIcon = monsterGifs.get(currentGifIndex);
        JLabel gifLabel = new JLabel(gifIcon);

        JPanel gifLabelPanel = new JPanel();
        gifLabelPanel.setLayout(new BoxLayout(gifLabelPanel, BoxLayout.X_AXIS));
        gifLabelPanel.setOpaque(false);
        gifLabelPanel.add(Box.createHorizontalGlue());
        gifLabelPanel.setMaximumSize(new Dimension(700, 500));
        gifLabelPanel.add(gifLabel);
        gifLabelPanel.add(Box.createHorizontalGlue());

        
        JLabel hintLabel = new JLabel(" This is a test message, to see if the hint panel is working properly!");
        hintLabel.setFont(pixelifyFont);
        hintLabel.setBorder(BorderFactory.createEmptyBorder(25, 0,0 ,0 ));
        hintLabel.setForeground(Color.WHITE);
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintLabel.setVisible(false);

        JLabel bossHealthLabel = new JLabel("Health: 10");
        bossHealthLabel.setForeground(Color.WHITE);
        bossHealthLabel.setFont(pixelifyFont);

        JPanel bossHealthPanel = new JPanel();
        bossHealthPanel.setLayout(new BoxLayout(bossHealthPanel, BoxLayout.X_AXIS));
        bossHealthPanel.setOpaque(false);
        bossHealthPanel.add(Box.createHorizontalGlue());
        bossHealthPanel.add(bossHealthLabel);
        bossHealthPanel.add(Box.createHorizontalGlue());

        JLabel bossCountdownLabel = new JLabel("Time left: 30s");
        bossCountdownLabel.setForeground(Color.WHITE);
        bossCountdownLabel.setFont(pixelifyFont);
        bossCountdownLabel.setVisible(false);

        centerPanel.add(hintLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(gifLabelPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(healthBarPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(bossHealthPanel);
        centerPanel.add(bossCountdownLabel);

        centerWrapper.add(centerPanel);

    


        leftPanel.add(topWrapper, BorderLayout.WEST);
        leftPanel.add(centerWrapper, BorderLayout.CENTER);



       
        // 1.2 right Panel =======================================================================================================

        JPanel rightPanel = new BackgroundPanel("resources/ui/uipanelupgrades.png");
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        rightPanel.setForeground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(700, 300));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        buttonPanel.setPreferredSize(new Dimension(1075, 75));
        buttonPanel.setMaximumSize(new Dimension(1075,75));

        buttonPanel.setOpaque(false);

        JLabel pointsLabel = new JLabel("Money: 0$");
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
        autoUpgradeButton.setMargin(new Insets(50, 175, 50, 175));
        autoUpgradeButton.setFont(pixelifyFont);

        JScrollPane scrollPane = new JScrollPane(rightPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setOpaque(false);
        
        ImageIcon settings = new ImageIcon("resources/ui/settings.png");
        settings.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH); // Passe die Größe an

        JButton settingsButton = new JButton(settings);
        settingsButton.setPreferredSize(new Dimension(60, 60)); // Gleiche Größe wie das Bild
        settingsButton.setMinimumSize(new Dimension(60, 60));
        settingsButton.setMaximumSize(new Dimension(60, 60));
        settingsButton.setFont(pixelifyFont);
        settingsButton.setForeground(Color.BLACK);

        ImageIcon achievement = new ImageIcon("resources/ui/achievements.png");
        achievement.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH); // Passe die Größe an

        JButton achievementButton = new JButton(achievement);
        achievementButton.setPreferredSize(new Dimension(60, 60)); // Gleiche Größe wie das Bild
        achievementButton.setMinimumSize(new Dimension(60, 60));
        achievementButton.setMaximumSize(new Dimension(60, 60));
        achievementButton.setFont(pixelifyFont);
        achievementButton.setForeground(Color.BLACK);

        buttonPanel.add(achievementButton);
        buttonPanel.add(settingsButton);

        rightPanel.add(buttonPanel);
        rightPanel.add(upgradeOptionsLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        rightPanel.add(pointsLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(clickerUpgradeButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(clickerLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(upgradeCostLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(autoUpgradeButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(autoClickerLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(upgradeAutoLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel effectLabel = new JLabel(new ImageIcon("resources/effects/clickeffect.gif"));
        effectLabel.setVisible(false);

        mainPanel.add(effectLabel);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.EAST);

        frame.add(mainPanel);
        frame.setVisible(true);

        GameLogic gameLogic = new GameLogic(pointsLabel, autoClickerLabel, clickerLabel, upgradeCostLabel,
                upgradeAutoLabel, bossHealthLabel, bossLevel, gifLabel, monsterGifs, zoneLabel, dmgPerSecond, healthBar, maxHP, bossCountdownLabel, bossGifs, hintLabel);

        clickerUpgradeButton.setToolTipText(gameLogic.getClickerUpgradeInfo());


        // Action performed when clicking a button or clicking with the mouse =========================================================

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


        settingsButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                gameLogic.openSettingsLabel();
            }
        });





        Timer timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                effectLabel.setVisible(false);
            }
        });


        timer.setRepeats(false);

        mainPanel.setLayout(null);
        gifLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                gameLogic.incrementPoints();
                Point clickPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), mainPanel);

                if (timer.isRunning()) {
                    timer.stop();
                }

                effectLabel.setVisible(false);
                effectLabel.setBounds(clickPoint.x - effectLabel.getWidth() / 2,
                        clickPoint.y - effectLabel.getHeight() / 2, 256, 264);

                effectLabel.setVisible(true);
                timer.start();
            }
        });
    }
}




// Game Logic ========================================================================================================================

class GameLogic {

    // ints =======================================================================================================================
    int points = 0;
    int autoClickerLevel = 0;
    int autoClickerCost = 100;
    int clickerLevel = 1;
    int bossHealth = 10;
    int bossLevelInt = 1;
    int currentGifIndex = 0;
    double costbase = 5;
    double rategrowth = 1.07;
    int owned = 0;
    int multipliers = 1;
    double productionBase = 1;
    double rateMonster = 1.55;
    int currrentZone = 1;
    int defeatedMonsterInCurrentZone = 0;
    final int MONSTER_PER_ZONE = 10;
    double costbaseAuto1 = 50;
    double rategrowthAuto1 = 1.22;
    int ownedAuto1 = 0;
    int maxHP;
    int nextCost = calculateNextCost();


    // Labels, Timer and booleans ==================================================================================================

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
    private boolean isBossLevel = false;
    private Timer bossCountdownTimer;
    private int bossCountdownTime = 30;
    private JLabel bossCountdownLabel;
    ArrayList<ImageIcon> monsterGifs;
    private Timer autoClickTimer;
    private boolean isAutoClickerRunning = false;
    ArrayList<ImageIcon> bossGifs;
    private boolean isMonsteralive = true;
    private JLabel hintLabel;
    private Timer hintLabelTimer;


    //connecting the labels in game logic wiht the labels in class main ==================================================================

    public GameLogic(JLabel pointsLabel, JLabel autoClickerLabel, JLabel clickerLabel, JLabel upgradeCostLabel,
            JLabel upgradeAutoLabel, JLabel bossHealthLabel, JLabel bossLevel, JLabel gifLabel, ArrayList<ImageIcon> monsterGifs, JLabel zoneLabel, JLabel dmgPerSecond, JProgressBar healthBar, int maxHP, JLabel bossCountdownLabel, ArrayList<ImageIcon> bossGifs, JLabel hintLabel) {
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
        this.bossCountdownLabel = bossCountdownLabel;
        this.bossGifs = bossGifs;
        this.hintLabel = hintLabel;
        healthBar.setMaximum(maxHP);
        healthBar.setValue(bossHealth);

        updateUpgradeCostLabel();
        updateUpgradeAutoLabel();

        autoClickTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                if (isMonsteralive) {
                points += autoClickerLevel;
                bossHealth = bossHealth - (5 * ownedAuto1);
                updateBossHealth();
                checkBossHealth();
                }
            }
        });

        hintLabelTimer = new Timer (3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hintLabel.setVisible(false);
                hintLabelTimer.stop();
            }
        });
        hintLabelTimer.setRepeats(false);

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
        if (bossLevelInt % 10 == 0){
            return (int) (100* (currrentZone -1 + Math.pow(rateMonster, currrentZone-1)));
        }else 
         return (int) (10*(currrentZone- 1 + Math.pow(rateMonster, currrentZone-1)));
    }

    // How much cost for autoclicker 
    int calculateNextAutoCost1(){
        return (int) (costbaseAuto1 *Math.pow(rategrowthAuto1, ownedAuto1));
    }

    // Function Nr.1:Figth
    // ===================================================================================================

    void incrementPoints() {
        if (isMonsteralive && bossHealth > 0) { 
        bossHealth = bossHealth - clickerLevel;
        checkBossHealth();
        updateBossHealth();        
        EffectPlayer effectPlayer = new EffectPlayer();
        effectPlayer.playEffect("slash.wav");                       
    }
    if  (points == 5 ){
        hintLabelTimer.start();
        hintLabel.setText("You have gained 5$. Click the Upgrade Clicker Button to get stronger.");
        hintLabel.setVisible(true); 
    }
}


    // Function Nr.2: Upgrade Clicker Button
    // ==============================================================================================

    void upgradeClicker() {
        if (points >= nextCost) {
            points -= nextCost;
            clickerLevel++;
            owned++;
            updatePointsLabel();
            updateClickerLabel();
            updateUpgradeCostLabel();
            
        } else {
            hintLabelTimer.start();
            hintLabel.setText("You dont have enough money to buy the next clicker upgrade.");
            hintLabel.setVisible(true);
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
        } else {
            hintLabelTimer.start();
            hintLabel.setText("You dont have enough money to buy the next auto upgrade.");
            hintLabel.setVisible(true);
            }  

    
    }

    void startAutoclicker() {
        autoClickTimer.start();
    }

    // Function 4: Settings ====================================================================================================

     void openSettingsLabel(){
             
     }



    // Methods to update the Labels
    // =========================================================================================================
    void updateBossLevel(){
        if (isBossLevel){
            bossLevel.setText("Boss Monster Level: " + currrentZone);
        }else {
        bossLevel.setText("Monster Level: " + bossLevelInt);
    }
}

       

    void updateBossHealth(){
        bossHealthLabel.setText("Health: " + bossHealth);
        healthBar.setValue(bossHealth);
    }

    void updatePointsLabel() {
        pointsLabel.setText( "Money: " + points + "$");
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

    private void startBossLevel() {
        isBossLevel = true;
        bossCountdownTime = 30; // Reset des Countdowns
        bossCountdownLabel.setVisible(true);
        bossCountdownLabel.setText("Time left: " + bossCountdownTime + "s");

        updateBossLevel();

        bossCountdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (bossCountdownTime <= 0) {
                    bossCountdownTimer.stop();
                    bossCountdownLabel.setVisible(false);

                  // Bestrafung, wenn der Boss nicht rechtzeitig besiegt wurde
                  points = Math.max(0, points -30); 
                  updatePointsLabel();

                  isBossLevel = false;
                  
                  bossLevelInt =1;
                  updateBossLevel();

                currentGifIndex = random.nextInt(monsterGifs.size());
                gifLabel.setIcon(monsterGifs.get(currentGifIndex));
                  
                  int nextMonster = calculateNextMonster();
                  maxHP = nextMonster;
                  bossHealth = nextMonster;
                  healthBar.setMaximum(maxHP);
                  healthBar.setValue(bossHealth);
                  updateBossHealth();

                  defeatedMonsterInCurrentZone = 0;
                  
              
            
                } else {
                bossCountdownTime--;
                bossCountdownLabel.setText("Time left: " + bossCountdownTime + "s");
                
                }                                
            }
        });

        bossCountdownTimer.start();

        if (bossGifs != null && bossGifs.size() > 0) {
            int randomBossIndex = random.nextInt(bossGifs.size());
            gifLabel.setIcon(bossGifs.get(randomBossIndex));
        } else {
        // Hier kannst du ein spezielles Boss-Bild laden
        ImageIcon bossIcon = new ImageIcon("resources/boss/boss_image.png");
        gifLabel.setIcon(bossIcon);
        }
    }
    
    private void checkBossHealth(){

        if (bossHealth <= 0) {
            bossHealth = 0;
            updateBossHealth();
            isMonsteralive = false;

                
            if (isBossLevel) {
                bossCountdownTimer.stop();
                bossCountdownLabel.setVisible(false);
                isBossLevel = false;
            }

        
                Timer deathTimer = new Timer(400, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        defeatedMonsterInCurrentZone++;
                        
                        int nextProduction = calculateNextProduction();
                        if (bossLevelInt % 10 == 0){
                            nextProduction*=5;
                        }

                        points = points + nextProduction;


                        if (defeatedMonsterInCurrentZone >= MONSTER_PER_ZONE){
                            currrentZone++;
                            defeatedMonsterInCurrentZone =0;
                            bossLevelInt =1;

                        }else if (bossLevelInt % 10==0){
                            bossLevelInt =1;
                        }else {
                            bossLevelInt++;
                        }

                        boolean nextIsBoss = (bossLevelInt == 10 && defeatedMonsterInCurrentZone == MONSTER_PER_ZONE - 1);

                        int nextMonster = calculateNextMonster();
                        
                        maxHP = nextMonster;
                        bossHealth = bossHealth + nextMonster;
                        healthBar.setMaximum(maxHP);
                        healthBar.setValue(Math.max(0, bossHealth));
                        
                        updateBossHealth();
                        updateBossLevel();
                        updatePointsLabel();
                        updateZoneLabel();
                        
                        if (nextIsBoss) {
                            // Starte Boss-Level, wenn es das 10. Monster ist und alle Monster in der Zone besiegt wurden
                            startBossLevel();
                        } else {
                        
                        currentGifIndex = random.nextInt(monsterGifs.size());
                        gifLabel.setIcon(monsterGifs.get(currentGifIndex));
                        }
                        isMonsteralive = true;
                    }
                });
                deathTimer.setRepeats(false); // Timer soll nur einmal ausgeführt werden
                deathTimer.start();
            }
          

            
        }
        
    }


