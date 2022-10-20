package com.cs321.team1.assets;

import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {
    public static InputStream loadStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public static URL loadURL(String path) {
        return Thread.currentThread().getContextClassLoader().getResource(path);
    }
}
