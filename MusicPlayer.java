import java.io.File;
import javax.sound.sampled.*;

public class MusicPlayer {
    private Clip clip;
    
    void playMusic(String musicLoc) {
        try {
            File musicPath = new File(musicLoc);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                AudioFormat baseFormat = audioInput.getFormat();
                
                // Determine a sample rate.
                // If baseFormat returns an unknown sample rate (<= 0), set it to a default value.
                float sampleRate = baseFormat.getSampleRate();
                if (sampleRate <= 0) {
                    sampleRate = 44100f; // common sample rate
                }
                
                // Create a new format: PCM_SIGNED, 16-bit, using the determined sample rate,
                // number of channels as in the original, frame size = channels * 2,
                // frame rate same as sample rate, and set big-endian to true.
                AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    sampleRate,
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    sampleRate,
                    true  // use big-endian since the error indicates that is supported
                );
                
                AudioInputStream decodedInput = AudioSystem.getAudioInputStream(decodeFormat, audioInput);
                
                clip = AudioSystem.getClip();
                clip.open(decodedInput);
                clip.start();
                // Optionally, loop continuously:
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.out.println("Couldn't find Music file at " + musicLoc);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
