package com.cs321.team1.assets;

import java.io.InputStream;


public class ResourceLoader {

  public static InputStream loadStream(String path) throws NullPointerException {
    var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (stream == null) {
      throw new NullPointerException("Input stream cannot be found");
    }
    return stream;
  }
}
