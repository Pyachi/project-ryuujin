package com.cs321.team1.assets.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum Sounds {
    SELECT("src/resources/sounds/select.wav"),
    DESELECT("src/resources/sounds/deselect.wav"),
    ERROR("src/resources/sounds/error.wav"),
    PICKUP("src/resources/sounds/pickup.wav"),
    MERGE("src/resources/sounds/merge.wav");
    
    private final File path;
    private static int volume = 50;
    
    Sounds(String path) {
        this.path = new File(path).getAbsoluteFile();
    }
    
    public void play() {
        try {
            var clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(path));
            ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (20 *
                    Math.log10(volume / 100.0)));
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ignored) {}
    }
    
    public static int getVolume() {
        return volume;
    }
    
    public static void setVolume(int vol) {
        volume = vol;
    }
}
