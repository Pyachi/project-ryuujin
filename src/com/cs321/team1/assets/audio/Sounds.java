package com.cs321.team1.assets.audio;

import com.cs321.team1.assets.ResourceLoader;
import com.cs321.team1.assets.audio.filters.Filters;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;

/**
 * Enum class for all in-game sound-effects and utility commands to play and filter them
 */
public enum Sounds {
    SELECT("resources/sounds/select.wav"),
    DESELECT("resources/sounds/deselect.wav"),
    ERROR("resources/sounds/error.wav"),
    PICKUP("resources/sounds/pickup.wav"),
    MERGE("resources/sounds/merge.wav");
    private static final AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
            44100F,
            16,
            2,
            4,
            44100F,
            false);
    private static int volume = 50;
    private static boolean initialized = false;
    private final String path;
    private final Map<Filters, byte[]> filteredData = new HashMap<>();
    private byte[] data;
    
    Sounds(String path) {
        this.path = path;
    }
    
    /**
     * Gets the global sound effect volume
     *
     * @return The sound effect volume from 0 (silence) to 100 (max)
     */
    public static int getVolume() {
        return volume;
    }
    
    /**
     * Sets the global sound effect volume
     *
     * @param vol The sound effect volume from 0 (silence) to 100 (max).
     */
    public static void setVolume(int vol) {
        volume = vol;
    }
    
    /**
     * Sets up audio environment for playing sound effects
     * Does nothing if invoked any more than once
     */
    public static void init() {
        if (initialized) return;
        initialized = true;
        try {
            for (Sounds sound : values()) {
                try {
                    var stream = ResourceLoader.loadStream(sound.path);
                    sound.data = AudioSystem.getAudioInputStream(stream).readAllBytes();
                    for (Filters filter : Filters.values())
                        sound.filteredData.put(filter, filter.filter.filter(sound.data));
                } catch (Exception e) { e.printStackTrace(); }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    /**
     * Plays unfiltered sound effect
     */
    public void play() {
        try {
            var clip = AudioSystem.getClip();
            clip.open(new AudioInputStream(new ByteArrayInputStream(data), FORMAT, data.length));
            ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (20 *
                    Math.log10(volume / 100.0)));
            clip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    /**
     * Plays filtered sound effect
     *
     * @param filter The filter to apply to the sound effect
     */
    public void playFiltered(Filters filter) {
        try {
            var clip = AudioSystem.getClip();
            byte[] data = filteredData.get(filter);
            clip.open(new AudioInputStream(new ByteArrayInputStream(data), FORMAT, data.length));
            ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (20 *
                    Math.log10(volume / 100.0)));
            clip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
