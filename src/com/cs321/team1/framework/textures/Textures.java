package com.cs321.team1.framework.textures;

import java.io.File;

public enum Textures {
    NOTHING("src/resources/textures/nothing.png", -1),

    //Player textures
    PLAYER_UP("src/resources/textures/player/player_up.png", 3),
    PLAYER_DOWN("src/resources/textures/player/player_down.png", 3),
    PLAYER_LEFT("src/resources/textures/player/player_left.png", 3),
    PLAYER_RIGHT("src/resources/textures/player/player_right.png", 3),

    //Tile Textures
    BOULDER("src/resources/textures/map/tile.png", 1),
    TILE("src/resources/textures/map/floor_tile.png", 1),
    BASE("src/resources/textures/map/gray_floor.png", 0),

    //Crate Textures
    CRATE("src/resources/textures/crates/crate.png", 2),
    CRATE_GRABBED("src/resources/textures/crates/crate_grabbed.png", 2),
    CRATE_INTERACTABLE("src/resources/textures/crates/crate_interactable.png", 2);

    public final File path;
    public final int priority;

    Textures(String path, int priority) {
        this.path = new File(path);
        this.priority = priority;
    }
}
