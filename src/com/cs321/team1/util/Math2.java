package com.cs321.team1.util;

public class Math2 {
    public static int clamp(int val, int min, int max) {
        return java.lang.Math.max(min, java.lang.Math.min(max, val));
    }
}
