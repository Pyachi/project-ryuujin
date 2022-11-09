package com.cs321.team1.objects;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.game.Game;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.LevelTransition;
import com.cs321.team1.map.Vec2;

/**
 * GameObject for running scripts in-level
 */
public class Trigger extends GameObject {
    private final Runnable run;
    private final String command;
    
    /**
     * Creates a Trigger with the given characteristics
     *
     * @param loc     Location of trigger
     * @param size    Size of trigger
     * @param tex     Texture of trigger
     * @param command Command of trigger
     */
    public Trigger(Vec2 loc, Vec2 size, Texture tex, String command) {
        setLocation(loc);
        setSize(size);
        setTexture(tex);
        this.command = command;
        run = getCommand();
    }
    
    /**
     * Creates a Trigger with the given characteristics
     *
     * @param loc     Location of trigger
     * @param tex     Texture of trigger
     * @param command Command of trigger
     */
    public Trigger(Vec2 loc, Texture tex, String command) {
        setTexture(tex);
        setSize(tex.size);
        setLocation(loc);
        this.command = command;
        run = getCommand();
    }
    
    /**
     * Creates a Trigger with the given characteristics
     *
     * @param size    Size of trigger
     * @param command Command of trigger
     */
    public Trigger(Vec2 location, Vec2 size, String command) {
        setLocation(location);
        setSize(size);
        this.command = command;
        run = getCommand();
    }
    
    /**
     * Handles interaction and running of trigger
     */
    @Tick(priority = 3)
    public void run() {
        if (collidesWith(Player.class)) run.run();
    }
    
    @Override
    public String toString() {
        return "TGR|" + getLocation() + "|" + getSize() + "|" + getTexture() + "->" + command;
    }
    
    private Runnable getCommand() {
        var cmd = command.split("\\|");
        return switch (cmd[0]) {
            case "LVL" -> () -> {
                if (Controls.SELECT.isPressed()) {
                    var lvl = Level.load(cmd[1]);
                    if (lvl != null) Game.get().pushSegments(new LevelTransition(Game.get().getHighestSegment(), lvl),
                            lvl);
                }
            };
            default -> () -> { };
        };
    }
}
