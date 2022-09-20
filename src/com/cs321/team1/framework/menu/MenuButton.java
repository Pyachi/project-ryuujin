package com.cs321.team1.framework.menu;

public class MenuButton {
    private final String text;
    private final Runnable run;
    
    public MenuButton(String text, Runnable run) {
        this.text = text;
        this.run = run;
    }
    
    public String getText() {
        return text;
    }
    
    public void run() {
        run.run();
    }
}
