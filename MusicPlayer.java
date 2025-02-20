import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicPlayer {
    void playMusic(String musicLoc){
             try {
                     File musicPath = new File(musicLoc);
                      if(musicPath.exists()){ 
                              AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                              Clip clip = AudioSystem.getClip();
                              clip.open(audioInput);
                              clip.loop(Clip.LOOP_CONTINUOUSLY);

                       }
            }
            catch (Exception ex){
                       ex.printStackTrace();
                 }
       }
}