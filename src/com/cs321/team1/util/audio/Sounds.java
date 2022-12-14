package com.cs321.team1.util.audio;

import com.cs321.team1.game.Game;
import com.cs321.team1.util.ResourceUtil;
import com.cs321.team1.util.audio.filters.Filters;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
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
    Game.getLogger().info("Initializing sound effects...");
    try {
      for (Sounds sound : values()) {
        var stream = ResourceUtil.loadStream(sound.path);
        sound.data = AudioSystem.getAudioInputStream(stream).readAllBytes();
        for (Filters filter : Filters.values()) {
          sound.filteredData.put(filter, filter.filter.filter(sound.data));
        }
      }
      Game.getLogger().info("Sound effects initialized!");
    } catch (Exception e) {
      Game.getLogger().info("Could not initialize sound effects!");
    }
  }

  public void play() {
    try {
      var clip = AudioSystem.getClip();
      clip.open(new AudioInputStream(new ByteArrayInputStream(data), FORMAT, data.length));
      ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(
          (float) (20 * Math.log10(volume / 100.0)));
      clip.start();
    } catch (Exception e) {
      Game.getLogger().warning("Could not play sound: " + name());
    }
  }

  public void playFiltered(Filters filter) {
    try {
      var clip = AudioSystem.getClip();
      byte[] data = filteredData.get(filter);
      clip.open(new AudioInputStream(new ByteArrayInputStream(data), FORMAT, data.length));
      ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(
          (float) (20 * Math.log10(volume / 100.0)));
      clip.start();
    } catch (Exception e) {
      Game.getLogger().warning("Could not play filtered sound: " + name());
    }
  }
}
