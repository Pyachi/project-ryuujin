package com.cs321.team1.assets;

import java.io.InputStream;

/**
 * Utility class for loading data from resource folder
 */
public class ResourceLoader {
    /**
     * Loads resource from file system
     *
     * @param path Path to file
     * @return InputStream of file
     * @throws NullPointerException when file does not exist
     */
    public static InputStream loadStream(String path) throws NullPointerException {
        var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (stream == null) throw new NullPointerException("Input stream cannot be found");
        return stream;
    }
}
