package com.cs321.team1.assets;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

//TODO Allow changing of music & dynamic volume adjustment
public enum Music {
    DAY("src/resources/music/day.wav");
    
    private final File path;
    private static int volume = 50;
    
    Music(String path) {
        this.path = new File(path).getAbsoluteFile();
    }
    
    public void play() {
//        try {
//            var clip = AudioSystem.getClip();
//            clip.open(AudioSystem.getAudioInputStream(path));
//            ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (20 * Math.log10(volume
//                                                                                                                      / 100.0)));
//            clip.start();
//        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//            e.printStackTrace();
//        }
    }
    
    public static int getVolume() {
        return volume;
    }
    
    public static void setVolume(int vol) {
        volume = vol;
    }
}
