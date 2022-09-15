package com.cs321.team1.framework.map;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.crates.IntegerCrate;
import com.cs321.team1.framework.objects.tiles.Door;
import com.cs321.team1.framework.objects.tiles.PassableTile;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.textures.Textures;
import com.cs321.team1.util.Direction;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO add random generation
public class Room {
    public final Set<GameObject> objs = new HashSet<>();
    private final Set<GameObject> objsToUpdate = new HashSet<>();
    private boolean refresh = true;
    
    private Room() {
    }
    
    public void refresh() {
        refresh = true;
    }
    
    public void update() {
        if (refresh) {
            objsToUpdate.addAll(objs);
            objs.forEach(it -> it.getTexture().reset());
        }
        else {
            objs.stream().filter(Runnable.class::isInstance).forEach(it -> {
                objsToUpdate.add(it);
                objsToUpdate.addAll(it.getCollisions());
            });
            objsToUpdate.addAll(Game.getPlayer().getCollisions());
        }
    }
    
    public void paint(Graphics2D g) {
        List<GameObject> list = new ArrayList<>(objsToUpdate);
        objsToUpdate.clear();
        list.sort(Comparator.comparingInt(it -> it.getTexture().getTexture().priority));
        list.forEach(it -> it.getTexture().paint(it.getLocation(), g));
    }
    
    /**
     * Generates a room with doors in the given directions
     *
     * @param dir Directions that a door must exist in
     * @return A randomly generated room with the required doors
     */
    public static Room generateRoom(Direction... dir) {
        Room room = new Room();
        new PassableTile(room, 0, 0, Textures.BACKGROUND);
        for (int i = 1; i < 15; i++) {
            new UnpassableTile(room, i, 1, Textures.NOTHING);
            new UnpassableTile(room, i, 10, Textures.NOTHING);
        }
        for (int j = 2; j < 10; j++) {
            new UnpassableTile(room, 1, j, Textures.NOTHING);
            new UnpassableTile(room, 14, j, Textures.NOTHING);
        }
        for (int i = 2; i < 14; i++)
            for (int j = 2; j < 10; j++)
                new PassableTile(room, i, j, Textures.FLOOR_TILE);
        Arrays.stream(dir).forEach(it -> new Door(room, it));
        new IntegerCrate(room, 5, 5, 1);
        return room;
    }
    
    /**
     * Generates a room with no door requirements
     *
     * @return A randomly generated room with at least one door
     */
    public static Room generateRoom() {
        return generateRoom(Direction.NORTH);
    }
}
