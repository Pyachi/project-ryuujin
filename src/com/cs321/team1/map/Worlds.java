package com.cs321.team1.map;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.Game;
import com.cs321.team1.objects.Player;
import com.cs321.team1.objects.Trigger;
import com.cs321.team1.assets.Textures;

/**
 * List of global values representing each world map.
 */
public class Worlds {
    public static final Level WORLD_ONE;
    
    static {
        var worldOne = Level.EmptyLevel(9, 5);
        worldOne.addObject(new Trigger(Location.Tile(5, 3), Textures.CRATE.get(), () -> {
            if (Controls.SELECT.isPressed()) Game.get().pushSegment(Level.loadLevel("one"));
        }));
        worldOne.addObject(new Player(Location.Tile(5, 1)));
        WORLD_ONE = worldOne;
    }
}
