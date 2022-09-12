package com.cs321.team1.framework;

public enum Textures {
    PLAYER_UP("src/resources/player_up.png"),
    PLAYER_DOWN("src/resources/player_down.png"),
    PLAYER_LEFT("src/resources/player_left.png"),
    PLAYER_RIGHT("src/resources/player_right.png"),

    WALL_TILE("src/resources/tile.png"),
    FLOOR_TILE("src/resources/floor_tile.png"),
    CRATE_ONE("src/resources/crate_one.png"),
    CRATE_TWO("src/resources/crate_two.png"),
    CRATE_THREE("src/resources/crate_three.png");

    public final String path;

    Textures(String path) {
        this.path = path;
    }
}
