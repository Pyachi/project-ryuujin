package com.cs321.team1.framework.textures;

import java.io.File;

public enum Textures {
    NULL("src/resources/textures/nothing.png", -1, 1),
    
    //Player textures
    PLAYER_UP("src/resources/textures/player/player_up.png", 3, 1),
    PLAYER_DOWN("src/resources/textures/player/player_down.png", 3, 1),
    PLAYER_LEFT("src/resources/textures/player/player_left.png", 3, 1),
    PLAYER_RIGHT("src/resources/textures/player/player_right.png", 3, 1),
    
    //Tile Textures
    BOULDER("src/resources/textures/map/tile.png", 1, 1),
    TILE("src/resources/textures/map/floor_tile.png", 1, 1),
    BASE("src/resources/textures/map/gray_floor.png", 0, 1),
    
    //Crate Textures
    CRATE("src/resources/textures/crates/crate.png", 2, 1),
    CRATE_GRABBED("src/resources/textures/crates/crate_grabbed.png", 2, 1),
    CRATE_INTERACTABLE("src/resources/textures/crates/crate_interactable.png", 2, 1),
    
    //Particle Textures
    EXPLOSION("src/resources/textures/crates/explosion.png", 4, 7);
    
    public final File path;
    public final int priority;
    public final int frames;
    
    Textures(String path, int priority, int frames) {
        this.path = new File(path);
        this.priority = priority;
        this.frames = frames;
    }
    
    public Texture get() {
        return new Texture(this, frames);
    }
    
    public Texture get(int width, int height) {
        return new Texture(this, frames, width, height);
    }
}
