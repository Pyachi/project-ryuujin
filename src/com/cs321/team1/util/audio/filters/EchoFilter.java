package com.cs321.team1.util.audio.filters;

class EchoFilter extends Filter {

  @Override
  protected void applyFilter() {
    extendBuffer(3);
    for (int channel = 0; channel < 2; channel++) {
      short[] buff = getSamples(channel);
      for (int t = 22050; t < buff.length; t++) {
        short echo = 0;
        for (int i = -1; i <= 1; i++) {
          echo += buff[t - i - 22049] / 3;
        }
        buff[t] = (short) (buff[t] + echo * 0.4);
      }
      setSamples(channel, buff);
    }
  }
}
