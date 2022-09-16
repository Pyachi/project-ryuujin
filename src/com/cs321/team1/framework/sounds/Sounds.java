package com.cs321.team1.framework.sounds;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum Sounds {
    PICKUP("src/resources/sounds/pickup.wav"), DROP("src/resources/sounds/drop.wav");
    
    private final File path;
    public static float volume = 0.5f;
    
    Sounds(String path) {
        this.path = new File(path).getAbsoluteFile();
    }
    
    public void play() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(path));
            ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (20 * Math.log10(volume)));
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ignored) {
        }
    }
}
