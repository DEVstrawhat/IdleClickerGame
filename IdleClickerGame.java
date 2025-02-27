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

        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.playMusic("dungeonBeat.wav");

        Font pixelifyFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/PixelifySans-VariableFont_wght.ttf")).deriveFont(24f);

        JPanel mainPanel = new BackgroundPanel("resources/background/backgroundimage.jpg");
        mainPanel.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 0));
        leftPanel.setOpaque(false);

        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        topWrapper.setOpaque(false);

        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setLayout(new BoxLayout(topLeftPanel, BoxLayout.Y_AXIS));
        topLeftPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topLeftPanel.setOpaque(false);

        JLabel bossLevel = new JLabel("Monster Level: 1");
        bossLevel.setFont(pixelifyFont);
        bossLevel.setForeground(Color.WHITE);
        bossLevel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel zoneLabel = new JLabel("Zone: 1");
        zoneLabel.setForeground(Color.WHITE);
        zoneLabel.setFont(pixelifyFont);
        zoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dmgPerSecond = new JLabel("Automated DMG: 0/s");
        dmgPerSecond.setForeground(Color.WHITE);
        dmgPerSecond.setFont(pixelifyFont);
        dmgPerSecond.setAlignmentX(Component.LEFT_ALIGNMENT);

        topLeftPanel.add(zoneLabel);
        topLeftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topLeftPanel.add(bossLevel);
        topLeftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topLeftPanel.add(dmgPerSecond);

        topWrapper.add(topLeftPanel);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 40, 0));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JProgressBar healthBar = new JProgressBar(0, 10);
        healthBar.setStringPainted(true);
        healthBar.setForeground(Color.RED);
        healthBar.setBackground(Color.DARK_GRAY);
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

        //JLabel bosswarning = new JLabel ("BOSS INCOMING! If you loose you will stay in the current zone and loose 30$!");
        //bosswarning.setVisible(false);

        centerPanel.add(gifLabelPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(healthBarPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(bossHealthPanel);
        centerPanel.add(bossCountdownLabel);

        centerWrapper.add(centerPanel);

        leftPanel.add(topWrapper, BorderLayout.NORTH);
        leftPanel.add(centerWrapper, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 50, 10));

        JLabel pointsLabel = new JLabel("Money: 0");
        pointsLabel.setFont(pixelifyFont);
        pointsLabel.setForeground(Color.BLACK);
        JLabel clickerLabel = new JLabel("Clicker Level: 1");
        clickerLabel.setFont(pixelifyFont);
        clickerLabel.setForeground(Color.BLACK);
        JLabel upgradeOptionsLabel = new JLabel("Upgrade your skills!");
        upgradeOptionsLabel.setFont(pixelifyFont);
        upgradeOptionsLabel.setForeground(Color.BLACK);
        upgradeOptionsLabel.setFont(upgradeOptionsLabel.getFont().deriveFont(Font.BOLD));
        JLabel upgradeCostLabel = new JLabel("Upgrade Cost: 10");
        upgradeCostLabel.setFont(pixelifyFont);
        upgradeCostLabel.setForeground(Color.BLACK);
        JButton clickerUpgradeButton = new JButton("Upgrade Clicker");
        clickerUpgradeButton.setMargin(new Insets(50, 175, 50, 175));
        clickerUpgradeButton.setFont(pixelifyFont);
        JLabel autoClickerLabel = new JLabel("Autoclicker Level: 0");
        autoClickerLabel.setFont(pixelifyFont);
        autoClickerLabel.setForeground(Color.BLACK);
        JLabel upgradeAutoLabel = new JLabel("Upgrade Cost: 100");
        upgradeAutoLabel.setFont(pixelifyFont);
        upgradeAutoLabel.setForeground(Color.BLACK);
        JButton autoUpgradeButton = new JButton("Upgrade Autoclicker");
        autoUpgradeButton.setMargin(new Insets(50, 175, 50, 175));
        autoUpgradeButton.setFont(pixelifyFont);
        JScrollPane scrollPane = new JScrollPane(rightPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setOpaque(false);

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
                upgradeAutoLabel, bossHealthLabel, bossLevel, gifLabel, monsterGifs, zoneLabel, dmgPerSecond, healthBar, maxHP, bossCountdownLabel, bossGifs);

        clickerUpgradeButton.setToolTipText(gameLogic.getClickerUpgradeInfo());

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

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        this.backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

class GameLogic {
    int points = 100;
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

    public GameLogic(JLabel pointsLabel, JLabel autoClickerLabel, JLabel clickerLabel, JLabel upgradeCostLabel,
            JLabel upgradeAutoLabel, JLabel bossHealthLabel, JLabel bossLevel, JLabel gifLabel, ArrayList<ImageIcon> monsterGifs, JLabel zoneLabel, JLabel dmgPerSecond, JProgressBar healthBar, int maxHP, JLabel bossCountdownLabel, ArrayList<ImageIcon> bossGifs) {
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
        healthBar.setMaximum(maxHP);
        healthBar.setValue(bossHealth);

        updateUpgradeCostLabel();
        updateUpgradeAutoLabel();

        autoClickTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                points += autoClickerLevel;
                bossHealth = bossHealth - (5 * ownedAuto1);
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
        if (bossHealth > 0) { 
        bossHealth = bossHealth - clickerLevel;
        checkBossHealth();
        updateBossHealth();        
        EffectPlayer effectPlayer = new EffectPlayer();
        effectPlayer.playEffect("slash.wav");                       
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
        if (isBossLevel){
            bossLevel.setText("Boss Monster Level: " + bossLevelInt);
        }else {
        bossLevel.setText("Monster Level: " + bossLevelInt);
    }
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
                    }
                });
                deathTimer.setRepeats(false); // Timer soll nur einmal ausgeführt werden
                deathTimer.start();
            }
          

            
        }
        
    }


