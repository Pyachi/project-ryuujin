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
                getCollisions(UnpassableTile.class).forEach(room.objs::remove);
                room.objs.add(new Trigger(room, 7, 0, 2, 1, () -> {
                    Game.getDungeon().move(0, -1);
                    Game.getPlayer().getLocation().set(120,160);
                }));
            }
            case SOUTH -> {
                setTexture(Textures.DOOR_SOUTH);
                getLocation().setTile(7, 10);
                getCollisions(UnpassableTile.class).forEach(room.objs::remove);
                room.objs.add(new Trigger(room, 7, 11, 2, 1, () -> {
                    Game.getDungeon().move(0, 1);
                    Game.getPlayer().getLocation().set(120,16);
                }));
            }
            case EAST -> {
                setTexture(Textures.DOOR_EAST);
                getLocation().setTile(14, 5);
                getCollisions(UnpassableTile.class).forEach(room.objs::remove);
                room.objs.add(new Trigger(room, 15, 5, 1, 2, () -> {
                    Game.getDungeon().move(1, 0);
                    Game.getPlayer().getLocation().set(16,88);
                }));
            }
            case WEST -> {
                setTexture(Textures.DOOR_WEST);
                getLocation().setTile(0, 5);
                getCollisions(UnpassableTile.class).forEach(room.objs::remove);
                room.objs.add(new Trigger(room, 0, 5, 1, 2, () -> {
                    Game.getDungeon().move(-1, 0);
                    Game.getPlayer().getLocation().set(224,88);
                }));
            }
        }
    }
}
