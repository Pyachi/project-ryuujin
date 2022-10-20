package com.cs321.team1.assets.audio;

import com.cs321.team1.assets.ResourceLoader;
import com.cs321.team1.assets.audio.filters.MuffleFilter;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum Music {
    DAY("resources/music/day.wav"),
    OVERWORLD("resources/music/overworld.wav");

    private final String path;
    private byte[] data;
    private byte[] muffledData;

    private static int volume = 50;
    private static int selected = -1;
    private static AudioFormat format;
    private static InputStream stream;
    private static SourceDataLine line = null;
    private static int distance;
    private static boolean muffled = false;

    Music(String path) {
        this.path = path;
    }

    public void init() {
        try {
            data = AudioSystem.getAudioInputStream(new BufferedInputStream(ResourceLoader.loadStream(path))).readAllBytes();
            muffledData = new MuffleFilter(new ByteArrayInputStream(data)).stream().readAllBytes();
        } catch (IOException | UnsupportedAudioFileException ignored) {}
    }

    public void play() {
        try {
            selected = ordinal();
            format = AudioSystem.getAudioFileFormat(ResourceLoader.loadStream(path)).getFormat();
            stream = new ByteArrayInputStream(muffled ? muffledData : data);
            distance = 0;
            if (line == null) initialize();
        } catch (UnsupportedAudioFileException | IOException ignored) {
        }
    }

    public static void setMuffled(boolean flag) {
        try {
            muffled = flag;
            var song = values()[selected];
            stream = new ByteArrayInputStream(muffled ? song.muffledData : song.data);
            stream.readNBytes(distance);
            if (line == null) initialize();
        } catch (IOException ignored) {}
    }

    private static void initialize() {
        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine) AudioSystem.getLine(info);
            Thread thread = new Thread(Music::run);
            thread.start();
        } catch (LineUnavailableException ignored) {}
    }

    private static void run() {
        try {
            line.open(format, 11025);
            line.start();
            ((FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (20 *
                    Math.log10(volume / 100.0)));
            byte[] buffer = new byte[4096];
            int count;
            while (true) {
                while ((count = stream.read(buffer, 0, 4096)) != -1) {
                    distance += count;
                    line.write(buffer, 0, count);
                }
                values()[selected].play();
            }
        } catch (LineUnavailableException | IOException ignored) {}
    }

    public static int getVolume() {
        return volume;
    }

    public static void setVolume(int vol) {
        volume = vol;
        if (line != null) ((FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (20 *
                Math.log10(volume / 100.0)));
    }
}
