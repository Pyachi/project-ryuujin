package com.cs321.team1.framework.textures;

public enum Textures {
    NOTHING("src/resources/nothing.png", -1),
    
    //Room textures
    BACKGROUND("src/resources/background.png", -1),
    DOOR_NORTH("src/resources/door_north.png", 0),
    DOOR_SOUTH("src/resources/door_south.png", 0),
    DOOR_EAST("src/resources/door_east.png", 0),
    DOOR_WEST("src/resources/door_west.png", 0),
    
    //Player textures
    PLAYER_UP("src/resources/player_up.png", 3),
    PLAYER_DOWN("src/resources/player_down.png", 3),
    PLAYER_LEFT("src/resources/player_left.png", 3),
    PLAYER_RIGHT("src/resources/player_right.png", 3),
    
    //Object textures
    WALL_TILE("src/resources/tile.png", 1),
    FLOOR_TILE("src/resources/floor_tile.png", 0),
    CRATE("src/resources/crate.png", 2),
    CRATE_GRABBED("src/resources/crate_grabbed.png",2),
    CRATE_INTERACTABLE("src/resources/crate_interactable.png", 2);
    
    public final String path;
    public final int priority;
    
    Textures(String path, int priority) {
        this.path = path;
        this.priority = priority;
    }
}
