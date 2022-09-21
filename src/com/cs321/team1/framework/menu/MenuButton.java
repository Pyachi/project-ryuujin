package com.cs321.team1.framework.menu;

public class MenuButton {
    private String text;
    private final Runnable run;
    
    public MenuButton(String text, Runnable run) {
        this.text = text;
        this.run = run;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public void run() {
        run.run();
    }
}
