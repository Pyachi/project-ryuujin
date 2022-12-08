package com.cs321.team1.assets.audio;

import com.cs321.team1.assets.ResourceLoader;
import com.cs321.team1.assets.audio.filters.Filters;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public enum Sounds {
  SELECT("resources/sounds/select.wav"),
  DESELECT("resources/sounds/deselect.wav"),
  ERROR("resources/sounds/error.wav"),
  PICKUP("resources/sounds/pickup.wav"),
  MERGE("resources/sounds/merge.wav");
  private static final AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F,
      16, 2, 4, 44100F, false);
  private static int volume = 50;
  private static boolean initialized = false;
  private final String path;
  private final Map<Filters, byte[]> filteredData = new HashMap<>();
  private byte[] data;

  Sounds(String path) {
    this.path = path;
  }

  public static int getVolume() {
    return volume;
  }

  public static void setVolume(int vol) {
    volume = vol;
  }

  public static void init() {
    if (initialized) {
      return;
    }
    initialized = true;
    try {
      for (Sounds sound : values()) {
        try {
          AudioInputStream audioStream = AudioSystem.getAudioInputStream(
              ResourceLoader.loadStream(sound.path));
          byte[] data = new byte[audioStream.available()];
          audioStream.read(data);
          sound.data = data;
          for (Filters filter : Filters.values()) {
            sound.filteredData.put(filter, filter.filter.filter(sound.data));
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void play() {
    try {
      Clip clip = AudioSystem.getClip();
      clip.open(new AudioInputStream(new ByteArrayInputStream(data), FORMAT, data.length));
      ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(
          (float) (20 * Math.log10(volume / 100.0)));
      clip.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void playFiltered(Filters filter) {
    try {
      Clip clip = AudioSystem.getClip();
      byte[] data = filteredData.get(filter);
      clip.open(new AudioInputStream(new ByteArrayInputStream(data), FORMAT, data.length));
      ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(
          (float) (20 * Math.log10(volume / 100.0)));
      clip.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
