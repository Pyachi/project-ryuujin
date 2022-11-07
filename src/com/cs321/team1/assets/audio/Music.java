package com.cs321.team1.assets.audio;

import com.cs321.team1.assets.ResourceLoader;
import com.cs321.team1.assets.audio.filters.Filters;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

/**
 * Enum class for all in-game music and utility commands to play and filter them.
 */
public enum Music {
    SOMBIENCE("resources/music/sombience.wav"),
    DAY("resources/music/day.wav"),
    OVERWORLD("resources/music/overworld.wav");
    private static final AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
            44100F,
            16,
            2,
            4,
            44100F,
            false);
    private static int volume = 50;
    private static int selected = -1;
    private static volatile InputStream stream;
    private static boolean initialized = false;
    private static int distance;
    private static SourceDataLine line = null;
    private final String path;
    private final Map<Filters, byte[]> filteredData = new HashMap<>();
    private byte[] data;
    
    Music(String path) {
        this.path = path;
    }
    
    /**
     * Applies a filter to the song, seamlessly starting where the unfiltered song left off at
     *
     * @param filter The filter to apply to the song
     */
    public static void applyFilter(Filters filter) {
        try {
            var song = values()[selected];
            stream = new ByteArrayInputStream(filter == null ? song.data : song.filteredData.get(filter));
            stream.readNBytes(distance);
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    /**
     * Gets the global music volume
     *
     * @return The music volume from 0 (silence) to 100 (max)
     */
    public static int getVolume() {
        return volume;
    }
    
    /**
     * Sets the global music volume
     *
     * @param vol The music volume from 0 (silence) to 100 (max).
     */
    public static void setVolume(int vol) {
        volume = vol;
        if (line != null) ((FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (20 *
                Math.log10(volume / 100.0)));
    }
    
    /**
     * Sets up audio environment for playing music
     * Does nothing if invoked any more than once
     */
    public static void init() {
        if (initialized) return;
        initialized = true;
        try {
            for (Music song : values()) {
                try {
                    var stream = ResourceLoader.loadStream(song.path);
                    song.data = AudioSystem.getAudioInputStream(stream).readAllBytes();
                    for (Filters filter : Filters.values())
                        song.filteredData.put(filter, filter.filter.filter(song.data));
                } catch (Exception e) { e.printStackTrace(); }
            }
            new Thread(() -> {
                try {
                    var info = new DataLine.Info(SourceDataLine.class, FORMAT);
                    line = (SourceDataLine) AudioSystem.getLine(info);
                    line.open(FORMAT, 11025);
                    line.start();
                    ((FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (20 *
                            Math.log10(volume / 100.0)));
                    byte[] buffer = new byte[4096];
                    int count;
                    while (true) {
                        while (stream == null) Thread.onSpinWait();
                        while ((count = stream.read(buffer, 0, 4096)) != -1) {
                            distance += count;
                            line.write(buffer, 0, count);
                        }
                        values()[selected].play();
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }).start();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    /**
     * Plays song, stopping any other song if already playing
     * If this song is already playing, this method does nothing
     */
    public void play() {
        try {
            if (selected != -1 && Music.values()[selected] == this) return;
            selected = ordinal();
            stream = new ByteArrayInputStream(data);
            distance = 0;
        } catch (Exception e) { e.printStackTrace(); }
    }
}
