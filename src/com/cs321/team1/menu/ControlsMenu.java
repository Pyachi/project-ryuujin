package com.cs321.team1.menu;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.game.Game;
import com.cs321.team1.menu.elements.MenuButton;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/**
 * Menu for viewing control scheme and adjusting controls
 */
public class ControlsMenu extends LevelMenu {
    private final Map<Controls, MenuButton> buttons = new EnumMap<>(Controls.class);
    private final Map<Controls, Integer> oldKeys = new EnumMap<>(Controls.class);
    private final Map<Controls, Integer> newKeys = new EnumMap<>(Controls.class);
    private MenuButton applyButton;
    
    @Override
    public void finish() { }
    
    @Override
    public void start() {
        Arrays.stream(Controls.values()).forEach(it -> {
            newKeys.put(it, it.getKey());
            var button = new MenuButton("", () -> {
                Sounds.SELECT.play();
                Game.get().pushSegment(new Controls.ControlChanger(it, this));
            });
            buttons.put(it, button);
            elements.add(button);
        });
        var applyButton = new MenuButton("Apply Settings", () -> {
            Sounds.SELECT.play();
            newKeys.forEach(Controls::setKey);
            oldKeys.clear();
            oldKeys.putAll(newKeys);
            refresh();
        });
        this.applyButton = applyButton;
        elements.add(applyButton);
        elements.add(new MenuButton("Back", () -> {
            Sounds.DESELECT.play();
            Game.get().popSegment();
        }));
        oldKeys.putAll(newKeys);
        refresh();
    }
    
    /**
     * Gets the associated MenuElement for a given control
     *
     * @param control The control to get the associated MenuElement
     * @return The associated MenuElement
     */
    public MenuButton getButton(Controls control) { return buttons.get(control); }
    
    /**
     * Gets the not-yet-mapped new key for the given control
     *
     * @param control The control to get the new key of
     * @return The integer keymap of the control to be set to
     */
    public int getNewKey(Controls control) { return newKeys.get(control); }
    
    @Override
    public void refresh() {
        buttons.forEach((control, button) -> button.setText(20,
                control.name() + ":",
                Controls.keyNameFromInt(newKeys.get(control))));
        applyButton.setDisabled(newKeys.containsValue(-1) || newKeys.equals(oldKeys));
    }
    
    /**
     * Sets the not-yet-mapped new key for the given control
     *
     * @param control The control to get the new key of
     * @param key     The integer keymap of the control to be set to
     */
    public void setNewKey(Controls control, int key) { newKeys.put(control, key); }
}
