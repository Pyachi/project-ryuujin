package com.cs321.team1.map;

import com.cs321.team1.Game;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LevelLoader {
    public static void loadLevel(String name) {
        try {
            var file = Files.readString(new File("src/resources/levels/" + name + ".ryu").toPath());
            var lvl = Level.fromString(file);
            Game.pushSegment(lvl);
            Game.pushSegment(new LevelEntrance(lvl));
        } catch (IOException ignored) {
        }
    }
}
