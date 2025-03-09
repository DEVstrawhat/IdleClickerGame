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
    int points = 0;
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
    double costbaseAuto1 = 50;
    double rategrowthAuto1 = 1.22;
    int ownedAuto1 = 0;
    int maxHP;
    int nextCost = calculateNextCost();
    int nextProduction = calculateNextProduction();
    


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


    //connecting the labels in game logic wiht the labels in class main ==================================================================

    public GameLogic(JLabel pointsLabel, JLabel autoClickerLabel, JLabel clickerLabel, JLabel upgradeCostLabel,
            JLabel upgradeAutoLabel, JLabel bossHealthLabel, JLabel bossLevel, JLabel gifLabel, ArrayList<ImageIcon> monsterPngs, JLabel zoneLabel, 
            JLabel dmgPerSecond, JProgressBar healthBar, int maxHP, JLabel bossCountdownLabel, ArrayList<ImageIcon> bossPngs, JLabel hintLabel, JLabel pointsPerMonster) {
        
                this.pointsLabel = pointsLabel;
        this.autoClickerLabel = autoClickerLabel;
        this.clickerLabel = clickerLabel;
        this.upgradeCostLabel = upgradeCostLabel;
        this.upgradeAutoLabel = upgradeAutoLabel;
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
        healthBar.setMaximum(maxHP);
        healthBar.setValue(bossHealth);

        updateUpgradeCostLabel();
        updateUpgradeAutoLabel();


        // Timer =================================================================================================================

        autoClickTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                if (isMonsteralive) {
                bossHealth = bossHealth - (5 * ownedAuto1);
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
    int calculateNextAutoCost1(){
        return (int) (costbaseAuto1 *Math.pow(rategrowthAuto1, ownedAuto1));
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
             // still working on it 
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

    void updatePointsPerMonster(){
        pointsPerMonster.setText("Money/Monster: " + calculateNextProduction() +"$");
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
        dmgPerSecond.setText("Automated Damage: " + (5*ownedAuto1) +"/s");
    }
    void updateClickerLabel() {
        clickerLabel.setText("Clicker Damage: " + clickerLevel);
    }

    void updateAutoClickerLabel() {
        autoClickerLabel.setText("Auto Clicker level: " + autoClickerLevel);
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
    
    private void updateUpgradeAutoLabel() {
        int nextAutoCost = calculateNextAutoCost1();
        upgradeAutoLabel.setText("Upgrade Cost: " + nextAutoCost);
    }


    //start Boss Level method ========================================================================================================

    private void startBossLevel() {
        isBossLevel = true;
        bossCountdownTime = 30; 
        bossCountdownLabel.setVisible(true);
        bossCountdownLabel.setText("Time left: " + bossCountdownTime + "s");
       
        hintLabel.setText("Boss Battle! Defeat him or you loose 30$ and you will repeat the current zone");
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
                        
                        int nextProduction = calculateNextProduction();
                        
                        if (bossLevelInt % 10 == 0){
                            nextProduction*=5;
                            hintLabel.setText("Boss Defeated! You got: " + (nextProduction) + "$" );
                            hintLabel.setVisible(true);
                            hintLabelTimer.restart();
                        }

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

