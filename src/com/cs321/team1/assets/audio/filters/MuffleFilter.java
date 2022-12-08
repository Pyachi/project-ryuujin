package com.cs321.team1.assets.audio.filters;

class MuffleFilter extends Filter {

  @Override
  protected void applyFilter() {
    for (int channel = 0; channel < 2; channel++) {
      short[] buff = getSamples(channel);
      for (int t = 0; t < buff.length; t++) {
        short sum = 0;
        for (int i = Math.max(0, t - 19); i <= Math.min(buff.length - 1, t + 19); i++) {
          sum += buff[i] * 0.025;
        }
        buff[t] = sum;
      }
      setSamples(channel, buff);
    }
  }
}
