package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.Trigger;
import com.cs321.team1.framework.textures.Textures;
import com.cs321.team1.util.Direction;

public class Door extends GameObject {
    public Door(Room room, Direction dir) {
        super(room);
        switch (dir) {
            case NORTH -> {
                setTexture(Textures.DOOR_NORTH);
                getLocation().setTile(7, 0);
                getCollisions(UnpassableTile.class).stream()
                        .filter(it -> it.collidesWith(getLocation().clone().centralize().moveTiles(1, 1)))
                        .findFirst()
                        .ifPresent(room::removeObject);
                room.addObject(new Trigger(room, 8, 0, 1, 1, () -> {
                    Game.getDungeon().move(0, -1);
                    Game.getPlayer().getLocation().set(128, 144);
                }));
            }
            case SOUTH -> {
                setTexture(Textures.DOOR_SOUTH);
                getLocation().setTile(7, 9);
                getCollisions(UnpassableTile.class).stream()
                        .filter(it -> it.collidesWith(getLocation().clone().centralize().moveTiles(1, 0)))
                        .findFirst()
                        .ifPresent(room::removeObject);
                room.addObject(new Trigger(room, 8, 10, 1, 1, () -> {
                    Game.getDungeon().move(0, 1);
                    Game.getPlayer().getLocation().set(128, 16);
                }));
            }
            case EAST -> {
                setTexture(Textures.DOOR_EAST);
                getLocation().setTile(15, 4);
                getCollisions(UnpassableTile.class).stream()
                        .filter(it -> it.collidesWith(getLocation().clone().centralize().moveTiles(0, 1)))
                        .findFirst()
                        .ifPresent(room::removeObject);
                room.addObject(new Trigger(room, 16, 5, 1, 1, () -> {
                    Game.getDungeon().move(1, 0);
                    Game.getPlayer().getLocation().set(16, 80);
                }));
            }
            case WEST -> {
                setTexture(Textures.DOOR_WEST);
                getLocation().setTile(0, 4);
                getCollisions(UnpassableTile.class).stream()
                        .filter(it -> it.collidesWith(getLocation().clone().centralize().moveTiles(1, 1)))
                        .findFirst()
                        .ifPresent(room::removeObject);
                room.addObject(new Trigger(room, 0, 5, 1, 1, () -> {
                    Game.getDungeon().move(-1, 0);
                    Game.getPlayer().getLocation().set(240, 80);
                }));
            }
        }
    }
}
