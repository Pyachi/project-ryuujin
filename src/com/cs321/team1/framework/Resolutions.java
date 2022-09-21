package com.cs321.team1.framework;

import java.awt.Dimension;

public enum Resolutions {
    _640x480(new Dimension(640,480),0),
    _800x600(new Dimension(800,600), 1),
    _1024x768(new Dimension(1024,768), 2),
    _1280x720(new Dimension(1280,720), 3),
    _1280x800(new Dimension(1280,800), 4),
    _1366x768(new Dimension(1366,768), 5),
    _1440x900(new Dimension(1440,900), 6),
    _1680x1050(new Dimension(1680,1050), 7),
    _1920x1080(new Dimension(1920,1080), 8),
    _1920x1200(new Dimension(1920,1200), 9),
    _2560x1440(new Dimension(2560,1440), 10);
    
    public final Dimension size;
    public final int index;
    
    Resolutions(Dimension size, int index) {
        this.size = size;
        this.index = index;
    }
}
