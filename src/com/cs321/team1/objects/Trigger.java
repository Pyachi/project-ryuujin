package com.cs321.team1.objects;

import com.cs321.team1.GameObject;
import com.cs321.team1.Tick;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.Vec2;

public class Trigger extends GameObject {
    private final Runnable run;
    private final String command;
    
    public Trigger(Vec2 loc, Vec2 size, Texture tex, String command) {
        setLocation(loc);
        setSize(size);
        setTexture(tex);
        this.command = command;
        run = getCommand();
    }
    
    public Trigger(Vec2 loc, Texture tex, String command) {
        setTexture(tex);
        setSize(tex.size);
        setLocation(loc);
        this.command = command;
        run = getCommand();
    }
    
    public Trigger(Vec2 location, Vec2 size, String command) {
        setLocation(location);
        setSize(size);
        this.command = command;
        run = getCommand();
    }
    
    private Runnable getCommand() {
        var cmd = command.split("\\|");
        return switch (cmd[0]) {
            case "LVL" -> () -> {
                if (Controls.SELECT.isPressed()) Level.load(cmd[1]);
            };
            default -> () -> {};
        };
    }
    
    @Tick(priority = 3)
    public void run() {
        if (collidesWith(Player.class)) run.run();
    }
    
    @Override
    public String toString() {
        return "TGR|" + getLocation() + "|" + getSize() + "|" + getTexture() + "->" + command;
    }
}
