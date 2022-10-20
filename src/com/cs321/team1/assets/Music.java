package com.cs321.team1.assets;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum Music {
    DAY("src/resources/music/day.wav"),
    OVERWORLD("src/resources/music/overworld.wav");
    
    private final File path;
    private static int volume = 50;
    private static AudioFormat format;
    private static AudioInputStream stream;
    private static SourceDataLine line = null;
    
    Music(String path) {
        this.path = new File(path).getAbsoluteFile();
    }
    
    public void play() {
        try {
            format = AudioSystem.getAudioFileFormat(path.toURI().toURL()).getFormat();
            stream = AudioSystem.getAudioInputStream(path);
            if (line == null) initialize();
        } catch (UnsupportedAudioFileException | IOException ignored) {}
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
            line.open(format,2205);
            line.start();
            ((FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (20 *
                    Math.log10(volume / 100.0)));
            byte[] data = new byte[4096];
            int count;
            while ((count = stream.read(data, 0, 4096)) != -1) line.write(data, 0, count);
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
