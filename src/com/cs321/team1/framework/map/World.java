package com.cs321.team1.framework.map;

import com.cs321.team1.framework.Controls;
import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.Trigger;
import com.cs321.team1.framework.objects.tiles.PassableTile;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.textures.Texture;
import com.cs321.team1.framework.textures.Textures;

public class World extends Level {
    public World(int width, int height) {
        super(width, height);
    }
    
    //TODO fill worlds with actual levels
    public static World worldOne() {
        int width = 16;
        int height = 12;
        var level = new World(width, height);
        for (int i = 0; i <= width + 1; i++) {
            new UnpassableTile(level, Location.atTile(i, 0), Textures.NOTHING);
            new UnpassableTile(level, Location.atTile(i, height + 1), Textures.NOTHING);
        }
        for (int j = 1; j <= height; j++) {
            new UnpassableTile(level, Location.atTile(0, j), Textures.NOTHING);
            new UnpassableTile(level, Location.atTile(width + 1, j), Textures.NOTHING);
        }
        for (int i = 1; i <= width; i++)
            for (int j = 1; j <= height; j++)
                new PassableTile(level, Location.atTile(i, j), Textures.FLOOR);
        new Trigger(level, Location.atTile(4, 4), new Texture(Textures.WALL), () -> {
            if (Controls.SELECT.isPressed()) Game.get().pushSegment(Level.emptyLevel(5, 10));
        });
        new Trigger(level, Location.atTile(6, 4), new Texture(Textures.WALL), () -> {
            if (Controls.SELECT.isPressed()) Game.get().pushSegment(Level.emptyLevel(5, 3));
        });
        new Trigger(level, Location.atTile(8, 4), new Texture(Textures.WALL), () -> {
            if (Controls.SELECT.isPressed()) Game.get().pushSegment(Level.emptyLevel(10, 4));
        });
        new Player(level, Location.atTile(width / 2, height / 2));
        return level;
    }
}
