package com.cs321.team1.framework.menu.menus;

import com.cs321.team1.framework.Controls;
import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.GameComponent;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.menu.Menu;
import com.cs321.team1.framework.menu.elements.MenuButton;
import com.cs321.team1.util.Keyboard;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class ControlsMenu extends Menu {
    private final Level level;
    
    public ControlsMenu(Level level) {
        this.level = level;
        Arrays.stream(Controls.values())
                .forEach(it -> elements.add(new MenuButton(it.name() + ": " + KeyEvent.getKeyText(it.getKey()), () -> {
                    Game.get().pushSegment(new GameComponent() {
                        int tick = 0;
                        
                        @Override
                        public void update() {
                            tick++;
                            if (tick % 60 < 30) elements.get(it.ordinal()).setText(it.name() + ": _");
                            else elements.get(it.ordinal()).setText(it.name() + ":  ");
                            if (Keyboard.getPressedKeys().isEmpty()) return;
                            it.setKey(Keyboard.getPressedKeys().stream().findAny().orElse(it.getKey()));
                            elements.get(it.ordinal()).setText(it.name() + ": " + KeyEvent.getKeyText(it.getKey()));
                            Keyboard.getPressedKeys().clear();
                            Game.get().popSegment();
                        }
                        
                        @Override
                        public void render(Graphics2D g) {
                            ControlsMenu.this.render(g);
                        }
                        
                        @Override
                        public void refresh() {
                        }
                        
                        @Override
                        public void onClose() {
                        }
                    });
                })));
        elements.add(new MenuButton("Back", () -> Game.get().popSegment()));
    }
    
    @Override
    public void render(Graphics2D g) {
        if (level != null) level.render(g);
        super.render(g);
    }
    
    @Override
    public void onClose() {
    }
}
