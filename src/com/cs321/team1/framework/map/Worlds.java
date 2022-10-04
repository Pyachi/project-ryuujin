package com.cs321.team1.framework.map;

import com.cs321.team1.framework.Controls;
import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.Trigger;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.textures.Textures;

public class Worlds {
    public static Level worldOne() {
        var world = Level.emptyLevel(9, 5);
        world.addObject(new UnpassableTile(Location.atTile(3,3), Textures.BOULDER.get()));
        world.addObject(new Trigger(Location.atTile(5, 3), Textures.CRATE.get(), () -> {
            if (Controls.SELECT.isPressed()) Game.get().pushSegment(Level.loadLevel("one"));
        }));
        world.addObject(new Player(Location.atTile(1, 2)));
        world.addObject(new Player(Location.atTile(1, 3)));
        world.addObject(new Player(Location.atTile(1, 4)));
        return world;
    }
}
