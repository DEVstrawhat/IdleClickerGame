import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class EffectPlayer {
    void playEffect(String musicLoc){
             try {
                     File effectPath = new File(musicLoc);
                      if(effectPath.exists()){ 
                              AudioInputStream audioInput = AudioSystem.getAudioInputStream(effectPath);
                              Clip clip = AudioSystem.getClip();
                              clip.open(audioInput);
                              clip.start();;

                       }
            }
            catch (Exception ex){
                       ex.printStackTrace();
                 }
       }
}