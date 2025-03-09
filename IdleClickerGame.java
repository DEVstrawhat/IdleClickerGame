
import javax.swing.*;
import javafx.scene.layout.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class IdleClickerGame {


    // implementing the different pngs ================================================================================================

    private static ArrayList<ImageIcon> monsterPngs = new ArrayList<>();
    private static ArrayList<ImageIcon> bossPngs = new ArrayList<>();
    private static int currentPngIndex = 0;
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
                monsterPngs.add(resizedIcon);
            }
        }
    }

    private static void loadBossPngs() {
        int gifWidth = 700;
        int gifHeight = 500;
    
        File folder = new File("resources/boss");
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
    
        if (files != null && files.length > 0) {
            for (File file : files) {
                ImageIcon resizedIcon = resizeGif(file.getAbsolutePath(), gifWidth, gifHeight);
                bossPngs.add(resizedIcon);
            }
        }
    }


   
    public static void main(String[] args) throws FontFormatException, IOException {
        
        // frame =================================================================================================================
        JFrame frame = new JFrame("Idle 8-bit Hunter");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.playMusic("Boss Battle.wav");

        Font pixelifyFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/BitPotionExt.ttf")).deriveFont(35f);
        Image cursorImage = new ImageIcon("resources/ui/mouseclicker.png").getImage();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Point hotSpot = new Point(0, 0); // Der "heiße Punkt" des Cursors (z. B. die Spitze eines Pfeils)
        Cursor customCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "CustomCursor");
        // BAckground for UI UpgradePanel======================================================================================
    
        ImageIcon warriorSymbol = new ImageIcon("resources/ui/WarriorClass.png");
        ImageIcon archerSymbol = new ImageIcon("resources/ui/ArcherClass.png");
        ImageIcon priestSymbol = new ImageIcon("resources/ui/PriestClass.png");

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
         *      1.1.1 north Panel (including zone label, monster level label, automated dmg label)
         *      1.1.2  center Panel (including monster- and boss png, health progressbar and health label )
         *      1.1.3 south Panel (including th hint label)
         * 1.2 right Panel (including all upgarde labels)
         */

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);


        // 1.1.1 North Panel ============================================================================================

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 0, 0));
        northPanel.setOpaque(false);

        JLabel bossLevel = new JLabel("Monster Level: 1");
        bossLevel.setFont(pixelifyFont);
        bossLevel.setForeground(Color.WHITE);

        JLabel zoneLabel = new JLabel("Zone: 1");
        zoneLabel.setForeground(Color.WHITE);
        zoneLabel.setFont(pixelifyFont);

        JLabel dmgPerSecond = new JLabel("Automated DMG: 0/s");
        dmgPerSecond.setForeground(Color.WHITE);
        dmgPerSecond.setFont(pixelifyFont);
        
        JLabel pointsPerMonster = new JLabel("Money/Monster: 1$");
        pointsPerMonster.setForeground(Color.WHITE);
        pointsPerMonster.setFont(pixelifyFont);

        northPanel.add(zoneLabel);
        northPanel.add(Box.createRigidArea(new Dimension(50, 25)));
        northPanel.add(bossLevel);
        northPanel.add(Box.createRigidArea(new Dimension(50, 25)));
        northPanel.add(dmgPerSecond);
        northPanel.add(Box.createRigidArea(new Dimension(50,25)));
        northPanel.add(pointsPerMonster);
        northPanel.add(Box.createRigidArea(new Dimension(50,25)));

        leftPanel.add(northPanel, BorderLayout.NORTH);


        // 1.1.2. center Panel =======================================================================================================

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(200, 0, 0, 0));
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
        healthBar.setMaximumSize(new Dimension(700, 20));
        healthBarPanel.add(healthBar);
        healthBarPanel.add(Box.createHorizontalGlue());

        loadMonsterGifs();
        loadBossPngs();
        currentPngIndex = random.nextInt(monsterPngs.size());
        ImageIcon gifIcon = monsterPngs.get(currentPngIndex);
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

        JPanel bossCountdowPanel = new JPanel();
        bossCountdowPanel.setLayout(new BoxLayout(bossCountdowPanel, BoxLayout.X_AXIS));
        bossCountdowPanel.setOpaque(false);
        bossCountdowPanel.add(Box.createHorizontalGlue());
        bossCountdowPanel.add(bossCountdownLabel);
        bossCountdowPanel.add(Box.createHorizontalGlue());


        centerPanel.add(gifLabelPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(healthBarPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(bossHealthPanel);
        centerPanel.add(bossCountdowPanel);

        leftPanel.add(centerPanel, BorderLayout.CENTER);

       
        // 1.1.3 south Panel ==============================================================================================================================
        
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        southPanel.setPreferredSize(new Dimension(frame.getWidth(), 100)); 

        JLabel hintLabel = new JLabel(" This is a test message, to see if the hint panel is working properly!");
        hintLabel.setFont(pixelifyFont);
        hintLabel.setForeground(Color.RED);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hintLabel.setVisible(false);

        JPanel hintPanel = new JPanel();
        hintPanel.setLayout(new BoxLayout(hintPanel, BoxLayout.X_AXIS));
        hintPanel.setOpaque(false);
        hintPanel.add(Box.createHorizontalGlue());
        hintPanel.add(hintLabel);
        hintPanel.add(Box.createHorizontalGlue());

        southPanel.add(hintPanel, BorderLayout.CENTER);

        leftPanel.add(southPanel, BorderLayout.SOUTH);


        // 1.2 right Panel =======================================================================================================

        JPanel rightPanel = new BackgroundPanel("resources/ui/uipanelupgrades.png");
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 0, 50));
        rightPanel.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setPreferredSize(new Dimension(1075, 75));
        buttonPanel.setMaximumSize(new Dimension(1075,75));
        buttonPanel.setOpaque(false);

        JLabel pointsLabel = new JLabel("Money: 0$");
        pointsLabel.setFont(pixelifyFont);
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setFont(pointsLabel.getFont().deriveFont(Font.BOLD));


        JLabel clickerLabel = new JLabel("Clicker Damage: 1");
        clickerLabel.setFont(pixelifyFont);
        clickerLabel.setForeground(Color.WHITE);

        

        JLabel upgradeCostLabel = new JLabel("Upgrade Cost: 10");
        upgradeCostLabel.setFont(pixelifyFont);
        upgradeCostLabel.setForeground(Color.WHITE);

        JButton clickerUpgradeButton = new JButton("Upgrade your clicker strength");
        clickerUpgradeButton.setFont(pixelifyFont);
        clickerUpgradeButton.setPreferredSize(new Dimension(550, 100));
        clickerUpgradeButton.setMaximumSize(new Dimension(550,100));

        // AutoClicker Upgrade Buttons ===============================================================================================
        //1. Warrior =====================================================================================================================
        
        JLabel autoClickerWarriorLabel = new JLabel("Warrior Level: 0");
        autoClickerWarriorLabel.setFont(pixelifyFont);
        autoClickerWarriorLabel.setForeground(Color.WHITE);

        JLabel upgradeAutoWarriorLabel = new JLabel("Upgrade Warrior Cost: 50");
        upgradeAutoWarriorLabel.setFont(pixelifyFont);
        upgradeAutoWarriorLabel.setForeground(Color.WHITE);

        JButton autoUpgradeWarrior = new JButton("Unlock Warrior", warriorSymbol);
        autoUpgradeWarrior.setFont(pixelifyFont);
        autoUpgradeWarrior.setBackground(Color.WHITE);
        autoUpgradeWarrior.setPreferredSize(new Dimension(550, 100));
        autoUpgradeWarrior.setMaximumSize(new Dimension(550,100));
        autoUpgradeWarrior.setHorizontalTextPosition(SwingConstants.LEFT); // Text rechts vom Icon
        autoUpgradeWarrior.setIconTextGap(100); // Abstand in Pixeln
        autoUpgradeWarrior.setHorizontalAlignment(SwingConstants.RIGHT); // Icon und Text linksbündig
        


        // 2. Archer =========================================================================================================================
        
        JButton autoUpgradeArcher = new JButton("Unlock Archer", archerSymbol);
        autoUpgradeArcher.setFont(pixelifyFont);
        autoUpgradeArcher.setBackground(Color.WHITE);
        autoUpgradeArcher.setPreferredSize(new Dimension(550, 100));
        autoUpgradeArcher.setMaximumSize(new Dimension(550,100));
        autoUpgradeArcher.setHorizontalTextPosition(SwingConstants.LEFT); // Text rechts vom Icon
        autoUpgradeArcher.setIconTextGap(100); // Abstand in Pixeln
        autoUpgradeArcher.setHorizontalAlignment(SwingConstants.RIGHT); // Icon und Text linksbündig

        JLabel autoClickerArcherLabel = new JLabel("Archer Level: 0");
        autoClickerArcherLabel.setFont(pixelifyFont);
        autoClickerArcherLabel.setForeground(Color.WHITE);

        JLabel upgradeAutoArcherLabel = new JLabel("Upgrade Archer Cost: 50");
        upgradeAutoArcherLabel.setFont(pixelifyFont);
        upgradeAutoArcherLabel.setForeground(Color.WHITE);


        // 3. Priest ========================================================================================================================
        
        JButton autoUpgradePriest = new JButton("Unlock Priest", priestSymbol);
        autoUpgradePriest.setFont(pixelifyFont);
        autoUpgradePriest.setBackground(Color.WHITE);
        autoUpgradePriest.setPreferredSize(new Dimension(550, 100));
        autoUpgradePriest.setMaximumSize(new Dimension(550,100));
        autoUpgradePriest.setHorizontalTextPosition(SwingConstants.LEFT); // Text rechts vom Icon
        autoUpgradePriest.setIconTextGap(100); // Abstand in Pixeln
        autoUpgradePriest.setHorizontalAlignment(SwingConstants.RIGHT); // Icon und Text linksbündig

        JLabel autoClickerPriestLabel = new JLabel("Priest Level: 0");
        autoClickerPriestLabel.setFont(pixelifyFont);
        autoClickerPriestLabel.setForeground(Color.WHITE);

        JLabel upgradeAutoPriestLabel = new JLabel("Upgrade Priest Cost: 50");
        upgradeAutoPriestLabel.setFont(pixelifyFont);
        upgradeAutoPriestLabel.setForeground(Color.WHITE);

        // Rest  of the rightPanel =================================================================================================================================================
      
        JScrollPane scrollPane = new JScrollPane(rightPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling
        scrollPane.setPreferredSize(new Dimension(750, frame.getHeight())); // Set width, and use frame height
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JLabel difference = new JLabel("==================================================");
        difference.setFont(pixelifyFont);
        difference.setForeground(Color.WHITE);

        JLabel difference2 = new JLabel("=================================================");
        difference2.setFont(pixelifyFont);
        difference2.setForeground(Color.WHITE);

        JLabel getStrongerLabel = new JLabel("Clicker Upgrade");
        getStrongerLabel.setFont(pixelifyFont);
        getStrongerLabel.setForeground(Color.WHITE);

        JLabel getAutoStrongerLabel = new JLabel("Auto Clicker Upgrades");
        getAutoStrongerLabel.setFont(pixelifyFont);
        getAutoStrongerLabel.setForeground(Color.WHITE);
        
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
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(pointsLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        rightPanel.add(getStrongerLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        rightPanel.add(difference);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(clickerUpgradeButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        rightPanel.add(clickerLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        rightPanel.add(upgradeCostLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        rightPanel.add(getAutoStrongerLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        rightPanel.add(difference2);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(autoUpgradeWarrior);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        rightPanel.add(autoClickerWarriorLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        rightPanel.add(upgradeAutoWarriorLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        rightPanel.add(autoUpgradeArcher);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        rightPanel.add(autoClickerArcherLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        rightPanel.add(upgradeAutoArcherLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        rightPanel.add(autoUpgradePriest);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        rightPanel.add(autoClickerPriestLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        rightPanel.add(upgradeAutoPriestLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 75)));


    // gluing everything together =============================================================================================================
        
        JLabel effectLabel = new JLabel(new ImageIcon("resources/effects/clickeffect.gif"));
        effectLabel.setVisible(false);

        mainPanel.add(effectLabel);
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.EAST);
        frame.add(mainPanel);
        frame.setCursor(customCursor);
        frame.setVisible(true);

        GameLogic gameLogic = new GameLogic(pointsLabel, autoClickerWarriorLabel,autoClickerArcherLabel,autoClickerPriestLabel, clickerLabel, upgradeCostLabel, upgradeAutoWarriorLabel, upgradeAutoArcherLabel, upgradeAutoPriestLabel,bossHealthLabel, bossLevel, gifLabel, monsterPngs, zoneLabel, dmgPerSecond, healthBar, maxHP, bossCountdownLabel, bossPngs, hintLabel, pointsPerMonster, autoUpgradeWarrior, autoUpgradeArcher, autoUpgradePriest );


        clickerUpgradeButton.setToolTipText(gameLogic.getClickerUpgradeInfo());


        // Action performed when clicking a button or clicking with the mouse =========================================================

        clickerUpgradeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.upgradeClicker();
            }
        });

        autoUpgradeWarrior.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.incrementAutoClicker("Warrior");
            }
        });

        autoUpgradeArcher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.incrementAutoClicker("Archer");
            }  
        });

        autoUpgradePriest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.incrementAutoClicker("Priest");
            }
        });


        settingsButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                gameLogic.openSettingsLabel();
            }
        });

       


        //
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









