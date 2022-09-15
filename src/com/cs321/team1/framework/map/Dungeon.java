package com.cs321.team1.framework.map;

import com.cs321.team1.util.Direction;

//TODO add random generation
public class Dungeon {
    public static final int maxDungeonWidth = 8;
    public static final int maxDungeonHeight = 5;
    
    public Room[][] layout = new Room[maxDungeonWidth][maxDungeonHeight];
    private int x = 0;
    private int y = 1;
    
    public static Dungeon generateDungeon() {
        Dungeon dungeon = new Dungeon();
        dungeon.layout[0][0] = Room.generateRoom(Direction.EAST, Direction.SOUTH);
        dungeon.layout[1][0] = Room.generateRoom(Direction.WEST, Direction.SOUTH);
        dungeon.layout[0][1] = Room.generateRoom(Direction.EAST, Direction.NORTH);
        dungeon.layout[1][1] = Room.generateRoom(Direction.WEST, Direction.NORTH);
        return dungeon;
    }
    
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
        getActiveRoom().refresh();
    }
    
    public Room getActiveRoom() {
        return layout[x][y];
    }
    
    public int getRoomX() {
        return x;
    }
    
    public int getRoomY() {
        return y;
    }
}
