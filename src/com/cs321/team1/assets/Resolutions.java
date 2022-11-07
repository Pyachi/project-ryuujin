package com.cs321.team1.assets;

import com.cs321.team1.map.Vec2;

import java.awt.Dimension;
import java.util.Arrays;

/**
 * Enum class of some common resolutions
 */
public enum Resolutions {
    _640x480(640, 480),
    _800x600(800, 600),
    _1280x720(1280, 720),
    _1024x768(1024, 768),
    _1366x768(1366, 768),
    _1280x800(1280, 800),
    _1440x900(1440, 900),
    _1680x1050(1680, 1050),
    _1920x1080(1920, 1080),
    _1920x1200(1920, 1200),
    _2560x1440(2560, 1440),
    _3840x2160(3840, 2160);
    /**
     * Underlying 2D vector
     */
    public final Vec2 size;
    
    Resolutions(int width, int height) {
        size = new Vec2(width, height);
    }
    
    /**
     * Gets enum equivalent of dimension size
     *
     * @param size Dimension class equivalent of 2D vector
     * @return Equivalent enum if exists, 640x480 otherwise
     */
    public static Resolutions fromDimension(Dimension size) {
        return Arrays.stream(values()).filter(it -> it.size.equals(Vec2.fromDimension(size))).findFirst().orElse(
                _640x480);
    }
    
    /**
     * Gets enum equivalent of 2D vector
     *
     * @param size 2D vector
     * @return Equivalent enum if exists, 640x480 otherwise
     */
    public static Resolutions fromVec2(Vec2 size) {
        return Arrays.stream(values()).filter(it -> it.size.equals(size)).findFirst().orElse(_640x480);
    }
}
