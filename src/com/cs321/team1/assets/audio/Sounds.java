package com.cs321.team1.assets.audio;

import com.cs321.team1.assets.ResourceLoader;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum Sounds {
    SELECT("resources/sounds/select.wav"),
    DESELECT("resources/sounds/deselect.wav"),
    ERROR("resources/sounds/error.wav"),
    PICKUP("resources/sounds/pickup.wav"),
    MERGE("resources/sounds/merge.wav");
    
    private final String path;
    private static int volume = 50;
    
    Sounds(String path) {
        this.path = path;
    }

    public void play() {
        try {
            var clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new BufferedInputStream(ResourceLoader.loadStream(path))));
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
