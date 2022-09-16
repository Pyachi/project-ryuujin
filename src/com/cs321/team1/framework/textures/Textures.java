package com.cs321.team1.framework.textures;

import java.io.File;

public enum Textures {
    NOTHING("src/resources/nothing.png", -1),
    
    //Room textures
    BACKGROUND("src/resources/textures/map/background.png", -1),
    DOOR_NORTH("src/resources/textures/map/door_north.png", 0),
    DOOR_SOUTH("src/resources/textures/map/door_south.png", 0),
    DOOR_EAST("src/resources/textures/map/door_east.png", 0),
    DOOR_WEST("src/resources/textures/map/door_west.png", 0),
    
    //Player textures
    PLAYER_UP("src/resources/textures/player/player_up.png", 3),
    PLAYER_DOWN("src/resources/textures/player/player_down.png", 3),
    PLAYER_LEFT("src/resources/textures/player/player_left.png", 3),
    PLAYER_RIGHT("src/resources/textures/player/player_right.png", 3),
    
    //Object textures
    WALL_TILE("src/resources/textures/map/tile.png", 1),
    FLOOR_TILE("src/resources/textures/map/floor_tile.png", 0),
    CRATE("src/resources/textures/crates/crate.png", 2),
    CRATE_GRABBED("src/resources/textures/crates/crate_grabbed.png",2),
    CRATE_INTERACTABLE("src/resources/textures/crates/crate_interactable.png", 2);
    
    public final File path;
    public final int priority;
    
    Textures(String path, int priority) {
        this.path = new File(path);
        this.priority = priority;
    }
}
