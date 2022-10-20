package com.cs321.team1.map;

import com.cs321.team1.Game;
import com.cs321.team1.assets.ResourceLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LevelLoader {
    public static void loadLevel(String name) {
        var file = String.join("\n", new BufferedReader(new InputStreamReader(ResourceLoader.loadStream("resources/levels/" + name + ".ryu"))).lines().toArray(String[]::new));
        var lvl = Level.fromString(file);
        Game.pushSegment(lvl);
        Game.pushSegment(new LevelEntrance(lvl));
    }
}
