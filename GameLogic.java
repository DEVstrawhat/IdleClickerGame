import javax.swing.*;
import javafx.scene.layout.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

// Game Logic ========================================================================================================================

public class GameLogic {
    

    // ints =======================================================================================================================
    int points = 1000;
    int autoClickerLevel = 0;
    int autoClickerCost = 100;
    int clickerLevel = 1;
    int bossHealth = 10;
    int bossLevelInt = 1;
    int currentPngIndex = 0;
    double costbase = 5;
    double rategrowth = 1.07;
    int owned = 0;
    int multipliers = 1;
    double productionBase = 1;
    double rateMonster = 1.55;
    int currrentZone = 1;
    int defeatedMonsterInCurrentZone = 0;
    final int MONSTER_PER_ZONE = 10;
    double costbaseWarrior = 50;
    double costbaseArcher = 50;
    double costbasePriest = 50;
    double rategrowthAutoWarrior = 1.22;
    double rategrowthAutoArcher = 1.22;
    double rategrowthAutoPriest = 1.22;
    int ownedWarrior = 0;
    int ownedArcher = 0;
    int ownedPriest = 0;
    int maxHP;
    int nextCost = calculateNextCost();
    int nextProduction = calculateNextProduction();
    


    // Labels, Timer and booleans ==================================================================================================

    private JLabel pointsLabel;
    private JLabel autoClickerWarriorLabel;
    private JLabel autoClickerArcherLabel;
    private JLabel autoClickerPriestLabel;
    private JLabel upgradeAutoWarriorLabel;
    private JLabel upgradeAutoArcherLabel;
    private JLabel upgradeAutoPriestLabel;
    private JLabel clickerLabel;
    private JLabel upgradeCostLabel;
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
    ArrayList<ImageIcon> monsterPngs;
    private Timer autoClickTimer;
    private boolean isAutoClickerRunning = false;
    ArrayList<ImageIcon> bossPngs;
    private boolean isMonsteralive = true;
    private JLabel hintLabel;
    private Timer hintLabelTimer;
    private boolean hintAt5PointsShown = false;
    private boolean hintAt50PointsShown = false;
    private JLabel pointsPerMonster;
    private JButton autoUpgradeWarrior;
    private JButton autoUpgradeArcher;
    private JButton autoUpgradePriest;
    public enum BossType {
        PHYSICAL,
        MAGICAL,
        AGILE
    }
    private BossType currentBossType;
    

    //connecting the labels in game logic wiht the labels in class main ==================================================================

    public GameLogic(JLabel pointsLabel, JLabel autoClickerWarriorLabel, JLabel autoClickerArcherLabel, JLabel autoClickerPriestLabel, JLabel clickerLabel, JLabel upgradeCostLabel,
            JLabel upgradeAutoWarriorLabel, JLabel upgradeAutoArcherLabel, JLabel upgradeAutoPriestLabel,JLabel bossHealthLabel, JLabel bossLevel, JLabel gifLabel, ArrayList<ImageIcon> monsterPngs, JLabel zoneLabel, 
            JLabel dmgPerSecond, JProgressBar healthBar, int maxHP, JLabel bossCountdownLabel, ArrayList<ImageIcon> bossPngs, JLabel hintLabel, JLabel pointsPerMonster, JButton autoUpgradeWarrior, JButton autoUpgradeArcher, JButton autoUpgradePriest ) {
        
                this.pointsLabel = pointsLabel;
        this.autoClickerWarriorLabel = autoClickerWarriorLabel;
        this.autoClickerArcherLabel = autoClickerArcherLabel;
        this.autoClickerPriestLabel = autoClickerPriestLabel;
        this.clickerLabel = clickerLabel;
        this.upgradeCostLabel = upgradeCostLabel;
        this.upgradeAutoWarriorLabel = upgradeAutoWarriorLabel;
        this.upgradeAutoArcherLabel = upgradeAutoArcherLabel;
        this.upgradeAutoPriestLabel = upgradeAutoPriestLabel;
        this.bossHealthLabel = bossHealthLabel;
        this.bossLevel = bossLevel;
        this.gifLabel = gifLabel;
        this.monsterPngs = monsterPngs;
        this.zoneLabel = zoneLabel;
        this.dmgPerSecond = dmgPerSecond;
        this.healthBar = healthBar;
        this.maxHP = maxHP;
        this.bossCountdownLabel = bossCountdownLabel;
        this.bossPngs = bossPngs;
        this.hintLabel = hintLabel;
        this.pointsPerMonster = pointsPerMonster;
        this.autoUpgradeWarrior = autoUpgradeWarrior;
        this.autoUpgradeArcher = autoUpgradeArcher;
        this.autoUpgradePriest = autoUpgradePriest;
        healthBar.setMaximum(maxHP);
        healthBar.setValue(bossHealth);

        updateUpgradeCostLabel();
        updateUpgradeAutoLabel("Warrior");
        updateUpgradeAutoLabel("Archer");
        updateUpgradeAutoLabel("Priest");
        
        BossType[] bossTypes = BossType.values();
    currentBossType = bossTypes[random.nextInt(bossTypes.length)];

        // Timer =================================================================================================================

        autoClickTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                if (isMonsteralive) {
                int totalDamage = calculateAutoDamage();
                System.out.println("AutoClicker Damage: " + totalDamage); 
                bossHealth = bossHealth - totalDamage;
                updateBossHealth();
                checkBossHealth();
                }
            }
        });

        hintLabelTimer = new Timer (3500, new ActionListener() {
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

    int calculateBossProduction() {
        return (int) ((productionBase * currrentZone * multipliers) * 5); // 5x Multiplikator für den Boss
    }

    // How much does the next upgrade costs
    int calculateNextCost(){
        return (int) (((costbase + owned) *Math.pow(rategrowth, owned)));
    }

    // How much life has the next monster 
    int calculateNextMonster(){
        if (bossLevelInt % 10 == 0){
            return (int) (100* (currrentZone -1 + Math.pow(rateMonster, currrentZone-1)));
        }else 
         return (int) (10*(currrentZone- 1 + Math.pow(rateMonster, currrentZone-1)));
    }

    // How much cost for autoclicker 
    int calculateNextAutoCostWarrior(){
        return (int) (costbaseWarrior *Math.pow(rategrowthAutoWarrior, ownedWarrior));
    }

    int calculateNextAutoCostArcher(){
        return (int) (costbaseArcher *Math.pow(rategrowthAutoArcher, ownedArcher));
    }

    int calculateNextAutoCostPriest(){
        return (int) (costbasePriest *Math.pow(rategrowthAutoPriest, ownedPriest));
    }


    // Function Nr.1:Figth
    // ===================================================================================================

    void incrementPoints() { // when clicking the monster
        if (isMonsteralive && bossHealth > 0) { 
        bossHealth = bossHealth - clickerLevel;
        checkBossHealth();
        updateBossHealth();        
        //EffectPlayer effectPlayer = new EffectPlayer();
        //effectPlayer.playEffect("slash.wav");                       
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
            
        } else {
            hintLabelTimer.start();
            hintLabel.setText("You dont have enough money to buy the next clicker upgrade.");
            hintLabel.setVisible(true);
            }  
    }

    // Function Nr.3: Autoclicker Warrior
    // ==========================================================================================================

    void incrementAutoClicker(String autoClickerType) {
        int nextAutoCost = 0;
        switch (autoClickerType) {
            case "Warrior":
            nextAutoCost = calculateNextAutoCostWarrior();
                break;
            case "Archer":
            nextAutoCost = calculateNextAutoCostArcher();
                break;
            case "Priest":
            nextAutoCost = calculateNextAutoCostPriest();
                break;
        }
        if (points >= nextAutoCost) {
            points -= nextAutoCost;
            
            switch (autoClickerType) {
                case "Warrior":
                ownedWarrior++;
                break;
            case "Archer":
                ownedArcher++;
                break;
            case "Priest":
                ownedPriest++;
                break;
        }


            updatePointsLabel();
            updateAutoClickerLabel(autoClickerType);
            updateUpgradeAutoLabel(autoClickerType);
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
    
        if (ownedPriest >= 1) {
        updatePriestButton();
        }
        if (ownedArcher >= 1) {
            updateArcherButton();
            }
        if (ownedWarrior >= 1) {
            updateWarriorButton();
            }
        }
        
    
    

    void startAutoclicker() {
        autoClickTimer.start();
    }


    

    // Function 4: Settings ====================================================================================================

     void openSettingsLabel(){
             // still working on it 
     }



    // Methods to update the Labels
    // =========================================================================================================
    void updateBossLevel(){
        if (isBossLevel){
            bossLevel.setText("Boss Monster Level: " + currrentZone + " Type: "+ currentBossType);
        }else {
        bossLevel.setText("Monster Level: " + bossLevelInt);
    }
}



    void updatePointsPerMonster() {
    if (isBossLevel) {
        int bossProduction = calculateBossProduction();
        pointsPerMonster.setText("Money/Boss: " + bossProduction + "$");
    } else {
        int monsterProduction = calculateNextProduction();
        pointsPerMonster.setText("Money/Monster: " + monsterProduction + "$");
    }
}
       

    void updateBossHealth(){
        bossHealthLabel.setText("Health: " + bossHealth);
        healthBar.setValue(bossHealth);
    }

    void updatePointsLabel() {
        pointsLabel.setText( "Money: " + points + "$");

        if  (points >= 5 && !hintAt5PointsShown){
            hintLabelTimer.start();
            hintLabel.setText("You earned 5$. You can buy your first clickerupgrade.");
            hintLabel.setVisible(true); 
            hintAt5PointsShown = true; // Mark this hint as shown
        }
        if  (points >= 50 && !hintAt50PointsShown){
            hintLabelTimer.start();
            hintLabel.setText("You earned 50$. You can buy your first autouclicker.");
            hintLabel.setVisible(true); 
            hintAt50PointsShown = true; // Mark this hint as shown
        }
    }

    void updateDmgPerSecond(){
        int totalDamage = calculateAutoDamage();
        dmgPerSecond.setText("Automated Damage: " + totalDamage +"/s");
    }
    void updateClickerLabel() {
        clickerLabel.setText("Clicker Damage: " + clickerLevel);
    }

    void updateWarriorButton(){
        autoUpgradeWarrior.setText("Upgrade Warrior");
        
    }
    void updateArcherButton(){
        autoUpgradeArcher.setText("Upgrade Archer");
        
    }
    void updatePriestButton(){
        autoUpgradePriest.setText("Upgrade Priest");
        
    }

    int calculateAutoDamage() {
            int warriorDamage = 5 * ownedWarrior;
            int archerDamage = 5 * ownedArcher;
            int priestDamage = 5 * ownedPriest;
        
            if (isBossLevel){
            switch (currentBossType) {
                case PHYSICAL:
                    warriorDamage *= 2;
                    archerDamage /= 2;
                    break;
                case MAGICAL:
                    priestDamage *= 2;
                    warriorDamage /= 2;
                    break;
                case AGILE:
                    archerDamage *= 2;
                    priestDamage /= 2;
                    break;
            }
        }
        
            return warriorDamage + archerDamage + priestDamage;
            }
    void updateAutoClickerLabel(String autoClickerType) {
        switch (autoClickerType) {
            case "Warrior":
            autoClickerWarriorLabel.setText("Warrior Level: " + ownedWarrior);
            break;
        case "Archer":
            autoClickerArcherLabel.setText("Archer Level: " + ownedArcher);
            break;
        case "Priest":
            autoClickerPriestLabel.setText("Priest Level: " + ownedPriest);
            break;
    }
}

    void updateZoneLabel(){
        zoneLabel.setText("Zone: " + currrentZone);
    }

    void updateUpgradeCostLabel() {
        upgradeCostLabel.setText("Upgrade Cost: " + calculateNextCost());
    }

    public String getClickerUpgradeInfo() {
        return "Clicker Level: " + clickerLevel + " | Cost: " + calculateNextCost();
    }
    
    private void updateUpgradeAutoLabel(String autoClickerType) {
        int nextAutoCost = 0;
    switch (autoClickerType) {
        case "Warrior":
            nextAutoCost = calculateNextAutoCostWarrior();
            upgradeAutoWarriorLabel.setText("Upgrade Warrior Cost: " + nextAutoCost);
            break;
        case "Archer":
            nextAutoCost = calculateNextAutoCostArcher();
            upgradeAutoArcherLabel.setText("Upgrade Archer Cost: " + nextAutoCost);
            break;
        case "Priest":
            nextAutoCost = calculateNextAutoCostPriest();
            upgradeAutoPriestLabel.setText("Upgrade Priest Cost: " + nextAutoCost);
            break;
    }
}
    //start Boss Level method ========================================================================================================

    private void startBossLevel() {
        isBossLevel = true;
        bossCountdownTime = 30; 
        bossCountdownLabel.setVisible(true);
        bossCountdownLabel.setText("Time left: " + bossCountdownTime + "s");
       
        BossType[] bossTypes = BossType.values();
        currentBossType = bossTypes[random.nextInt(bossTypes.length)];



        hintLabel.setText("Boss Battle! Boss Type: " +currentBossType +  " Defeat him or you loose 30$ and you will repeat the current zone");
        hintLabel.setVisible(true);
        hintLabelTimer.restart(); 


        updateBossLevel();

        bossCountdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (bossCountdownTime <= 0) {
                    bossCountdownTimer.stop();
                    bossCountdownLabel.setVisible(false);

                    hintLabel.setText("You are defeated! You lost 30 points and continue battling in zone: " + currrentZone );
                    hintLabel.setVisible(true);
                    hintLabelTimer.restart();

                  // Bestrafung, wenn der Boss nicht rechtzeitig besiegt wurde
                  points = Math.max(0, points -30); 
                  updatePointsLabel();

                  isBossLevel = false;
                  
                  bossLevelInt =1;
                  updateBossLevel();

                currentPngIndex = random.nextInt(monsterPngs.size());
                gifLabel.setIcon(monsterPngs.get(currentPngIndex));
                  
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

        if (bossPngs != null && bossPngs.size() > 0) {
            int randomBossIndex = random.nextInt(bossPngs.size());
            gifLabel.setIcon(bossPngs.get(randomBossIndex));
        } else {
        ImageIcon bossIcon = new ImageIcon("resources/boss/boss_image.png");
        gifLabel.setIcon(bossIcon);
        }
    }
    
    // check Boss Health Method ====================================================================================================

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
                        
                        int nextProduction;
                        
                        if (bossLevelInt % 10 == 0){
                            nextProduction = calculateBossProduction();
                            hintLabel.setText("Boss Defeated! You got: " + nextProduction + "$");
                        }else{
                            nextProduction = calculateNextProduction();
                            hintLabel.setText("Monster Defeated! You got: " + nextProduction + "$" );
                          
                        }

                        hintLabel.setVisible(true);
                        hintLabelTimer.restart();

                        points = points + nextProduction;
                        updatePointsPerMonster();

                       


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
                        
                        currentPngIndex = random.nextInt(monsterPngs.size());
                        gifLabel.setIcon(monsterPngs.get(currentPngIndex));
                        }
                        isMonsteralive = true;
                    }
                });
                deathTimer.setRepeats(false); // Timer soll nur einmal ausgeführt werden
                deathTimer.start();
            }
          

            
        }

        
    }

