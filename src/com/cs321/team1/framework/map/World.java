package com.cs321.team1.framework.map;

import com.cs321.team1.framework.Controls;
import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.Trigger;
import com.cs321.team1.framework.textures.Texture;
import com.cs321.team1.framework.textures.Textures;

public class World {
    public static Level worldOne() {
        var world = Level.emptyLevel(9, 5);
        new Trigger(world, Location.atTile(5, 3), new Texture(Textures.CRATE), () -> {
            if (Controls.SELECT.isPressed()) Game.get().pushSegment(Level.loadLevel("one"));
        });
        new Player(world, Location.atTile(1,1));
        return world;
    }
}
