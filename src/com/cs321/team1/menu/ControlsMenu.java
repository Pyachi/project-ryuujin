package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.GameSegment;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.menu.elements.MenuButton;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class ControlsMenu extends LevelMenu {
    
    @Override
    public void start() {
        Arrays.stream(Controls.values()).forEach(it -> elements.add(new MenuButton(it.name() + ":" +
                String.format("%" + (19 - it.name().length()) + "s",
                        it.getKey() == -1 ? "UNBOUND" : KeyEvent.getKeyText(it.getKey())), () -> {
            Sounds.SELECT.play();
            Game.pushSegment(new GameSegment() {
                int tick = 0;
                
                @Override
                public void update() {
                    tick++;
                    if (tick % 60 < 30) elements.get(it.ordinal()).setText(20, it.name() + ":", "_");
                    else elements.get(it.ordinal()).setText(20, it.name() + ":", " ");
                    if (Controls.getPressedKeys().isEmpty()) return;
                    int key = Controls.getPressedKeys().stream().findAny().orElse(it.getKey());
                    for (Controls control : Controls.values())
                        if (control.getKey() == key && control != it) {
                            control.setKey(-1);
                            elements.get(control.ordinal()).setText(20, control.name() + ":", "UNBOUND");
                        }
                    it.setKey(key);
                    elements.get(it.ordinal()).setText(20, it.name() + ":", KeyEvent.getKeyText(it.getKey()));
                    Controls.clearCache();
                    Game.popSegment();
                }
                
                @Override
                public void render(Graphics2D g) {
                    ControlsMenu.this.render(g);
                }
                
                @Override
                public void finish() {
                }
            });
        })));
        elements.add(new MenuButton("Back", () -> {
            Sounds.DESELECT.play();
            Game.popSegment();
        }));
    }
    
    @Override
    public void finish() {}
}
