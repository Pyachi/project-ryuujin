package com.cs321.team1.map;

import com.cs321.team1.Game;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Textures;
import com.cs321.team1.objects.PassableTile;
import com.cs321.team1.objects.Player;
import com.cs321.team1.objects.Trigger;
import com.cs321.team1.objects.UnpassableTile;

public class World extends Level {
    protected World(int width, int height) {
        super(width, height);
    }
    
    private static World EmptyWorld(int width, int height) {
        var world = new World(width, height);
        world.addObject(new UnpassableTile(Location.Tile(1, 0), Textures.NULL.get(width, 1)));
        world.addObject(new UnpassableTile(Location.Tile(1, height + 1), Textures.NULL.get(width, 1)));
        world.addObject(new UnpassableTile(Location.Tile(0, 1), Textures.NULL.get(1, height)));
        world.addObject(new UnpassableTile(Location.Tile(width + 1, 1), Textures.NULL.get(1, height)));
        world.addObject(new PassableTile(Location.Tile(1, 1), Textures.BASE.get(width, height)));
        return world;
    }
    
    public static World One() {
        var world = EmptyWorld(9, 5);
        world.addObject(new Trigger(Location.Tile(5, 3), Textures.CRATE.get(), () -> {
            if (Controls.SELECT.isPressed()) Game.pushSegment(Level.loadLevel("one"));
        }));
        world.addObject(new Player(Location.Tile(5, 1)));
        return world;
    }
}
