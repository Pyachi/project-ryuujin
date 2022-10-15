package com.cs321.team1.map;

import com.cs321.team1.Game;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.objects.*;
import com.cs321.team1.objects.crates.*;

import java.io.File;
import java.util.Scanner;

public class LevelLoader {
    public static void loadLevel(String name, boolean world) {
        try {
            var file = new Scanner(new File("src/resources/levels/" + name + ".ryu"));
            var setup = file.nextLine().split("\t");
            var mapSize = setup[1].split(":");
            var mapWidth = Integer.parseInt(mapSize[0]);
            var mapHeight = Integer.parseInt(mapSize[1]);
            var level = new Level(mapWidth, mapHeight, world);
            for (int i = 2; i < setup.length; i++) {
                var playerLoc = setup[i].split(":");
                var playerX = Integer.parseInt(playerLoc[0]);
                var playerY = Integer.parseInt(playerLoc[1]);
                level.addObject(new Player(Location.Tile(playerX, playerY)));
            }
            while (file.hasNext()) {
                try {
                    var line = file.nextLine().split("\t");
                    var locString = line[1].split(":");
                    var locX = Integer.parseInt(locString[0]);
                    var locY = Integer.parseInt(locString[1]);
                    var loc = Location.Tile(locX, locY);
                    switch (line[0]) {
                        case "FLR" -> {
                            if (line[2].contains("/")) {
                                var textureData = line[2].split(":");
                                var priority = Integer.parseInt(textureData[0]);
                                var path = textureData[1];
                                var texture = new Texture(path, priority);
                                level.addObject(new PassableTile(loc, texture));
                            } else {
                                var size = line[2].split(":");
                                var width = Integer.parseInt(size[0]);
                                var height = Integer.parseInt(size[1]);
                                if (line.length == 4) {
                                    var textureData = line[3].split(":");
                                    var priority = Integer.parseInt(textureData[0]);
                                    var path = textureData[1];
                                    var texture = new Texture(path, priority);
                                    level.addObject(new PassableTile(loc, texture, width, height));
                                } else level.addObject(new PassableTile(loc, width, height));
                            }
                        }
                        case "WAL" -> {
                            if (line[2].contains("/")) {
                                var textureData = line[2].split(":");
                                var priority = Integer.parseInt(textureData[0]);
                                var path = textureData[1];
                                var texture = new Texture(path, priority);
                                level.addObject(new UnpassableTile(loc, texture));
                            } else {
                                var size = line[2].split(":");
                                var width = Integer.parseInt(size[0]);
                                var height = Integer.parseInt(size[1]);
                                if (line.length == 4) {
                                    var textureData = line[3].split(":");
                                    var priority = Integer.parseInt(textureData[0]);
                                    var path = textureData[1];
                                    var texture = new Texture(path, priority);
                                    level.addObject(new UnpassableTile(loc, texture, width, height));
                                } else level.addObject(new UnpassableTile(loc, width, height));
                            }
                        }
                        case "INT" -> {
                            var val = Integer.parseInt(line[2]);
                            level.addObject(new IntegerCrate(loc, val));
                        }
                        case "NEG" -> {
                            level.addObject(new NegateCrate(loc));
                        }
                        case "MOD" -> {
                            var val = Integer.parseInt(line[2]);
                            level.addObject(new ModuloCrate(loc, val));
                        }
                        case "MUL" -> {
                            var val = Integer.parseInt(line[2]);
                            level.addObject(new MultiplyCrate(loc, val));
                        }
                        case "DIV" -> {
                            var val = Integer.parseInt(line[2]);
                            level.addObject(new DivideCrate(loc, val));
                        }
                        case "LCK" -> {
                            var val = Integer.parseInt(line[2]);
                            level.addObject(new LockedCrate(loc, val));
                        }
                        case "PWR" -> {
                            var val = Integer.parseInt(line[2]);
                            level.addObject(new UnpoweredCrate(loc, val));
                        }
                        case "CVR" -> {
                            var move = line[2].split(":");
                            var conv = switch (line[2]) {
                                default -> Conveyor.UP(loc);
                                case "D" -> Conveyor.DOWN(loc);
                                case "L" -> Conveyor.LEFT(loc);
                                case "R" -> Conveyor.RIGHT(loc);
                            };
                            level.addObject(conv);
                        }
                        case "TGR" -> {
                            var command = file.nextLine().split("\t");
                            Runnable run = () -> {};
                            switch (command[0]) {
                                case "LVL" -> run = () -> {
                                    if (Controls.SELECT.isPressed()) loadLevel(command[1], false);
                                };
                            }
                            if (line[2].contains("/")) {
                                var textureData = line[2].split(":");
                                var priority = Integer.parseInt(textureData[0]);
                                var path = textureData[1];
                                var texture = new Texture(path, priority);
                                level.addObject(new Trigger(loc, texture, run));
                            } else {
                                var size = line[2].split(":");
                                var width = Integer.parseInt(size[0]);
                                var height = Integer.parseInt(size[1]);
                                if (line.length == 4) {
                                    var textureData = line[3].split(":");
                                    var priority = Integer.parseInt(textureData[0]);
                                    var path = textureData[1];
                                    var texture = new Texture(path, priority);
                                    level.addObject(new Trigger(loc, texture, width, height, run));
                                } else level.addObject(new Trigger(loc, width, height, run));
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }
            Game.pushSegment(level);
        } catch (Exception e) {
            System.out.println("Error could not load " + name);
        }
    }
    
}
