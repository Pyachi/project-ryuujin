package com.cs321.team1.util.audio.filters;

public abstract class Filter {

  private byte[] buffer;

  public byte[] filter(byte[] data) {
    buffer = data.clone();
    applyFilter();
    return buffer;
  }

  protected abstract void applyFilter();

  protected void extendBuffer(double multiple) {
    byte[] data = new byte[(int) (buffer.length * multiple)];
    System.arraycopy(buffer, 0, data, 0, buffer.length);
    buffer = data;
  }

  protected short[] getSamples(int channel) {
    short[] dbBuffer = new short[buffer.length / 4];
    for (int i = 0; i < buffer.length / 4; i++) {
      dbBuffer[i] = getSample(i, channel);
    }
    return dbBuffer;
  }

  protected void setSamples(int channel, short[] dbBuffer) {
    for (int i = 0; i < buffer.length / 4; i++) {
      setSample(i, channel, dbBuffer[i]);
    }
  }

  private short getSample(int index, int channel) {
    if (index * 4 + channel * 2 < 0) {
      return 0;
    }
    if (index * 4 + channel * 2 + 1 >= buffer.length) {
      return 0;
    }
    return (short) (((buffer[index * 4 + channel * 2 + 1] & 0xFF) << 8) | (
        buffer[index * 4 + channel * 2] & 0xFF));
  }

  private void setSample(int index, int channel, short data) {
    if (index * 4 + channel * 2 < 0) {
      return;
    }
    if (index * 4 + channel * 2 + 1 >= buffer.length) {
      return;
    }
    this.buffer[index * 4 + channel * 2] = (byte) (data & 0xFF);
    this.buffer[index * 4 + channel * 2 + 1] = (byte) ((data >> 8) & 0xFF);
  }
}
