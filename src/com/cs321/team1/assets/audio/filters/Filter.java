package com.cs321.team1.assets.audio.filters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class Filter {
    protected final byte[] buffer;
    
    protected Filter(InputStream stream) {
        byte[] temp;
        try {
            temp = stream.readAllBytes();
        } catch (IOException e) {
            temp = new byte[0];
        }
        this.buffer = temp;
        filter();
    }
    
    public InputStream stream() {
        return new ByteArrayInputStream(buffer);
    }
    
    protected short[] getSamples(int channel) {
        short[] dbBuffer = new short[buffer.length / 4];
        for (int i = 0; i < buffer.length / 4; i++)
            dbBuffer[i] = getSample(i, channel);
        return dbBuffer;
    }
    
    protected void setSamples(int channel, short[] dbBuffer) {
        for (int i = 0; i < buffer.length / 4; i++)
            setSample(i, channel, dbBuffer[i]);
    }
    
    protected short[] conv(short[] a, short[] b) {
        int n = a.length;
        int m = b.length;
    
        short[] c = new short[n + m - 1];
        for (int i = 0 ;i < n; ++i)
            for (int j = 0; j < m; ++j)
                c[i+j] += a[i]*b[j];
        return c;
    }
    
    private short getSample(int index, int channel) {
        if (index * 4 + channel * 2 < 0) return 0;
        if (index * 4 + channel * 2 + 1 >= buffer.length) return 0;
        return (short) (((buffer[index * 4 + channel * 2 + 1] & 0xFF) << 8) | (buffer[index * 4 + channel * 2] & 0xFF));
    }
    
    private void setSample(int index, int channel, short data) {
        if (index * 4 + channel * 2 < 0) return;
        if (index * 4 + channel * 2 + 1 >= buffer.length) return;
        this.buffer[index * 4 + channel * 2] = (byte) (data & 0xFF);
        this.buffer[index * 4 + channel * 2 + 1] = (byte) ((data >> 8) & 0xFF);
    }
    
    protected abstract void filter();
}
