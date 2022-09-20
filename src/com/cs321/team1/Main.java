package com.cs321.team1;

import com.cs321.team1.framework.Game;
import com.cs321.team1.util.Keyboard;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Project 龍神");
        window.add(Game.get());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        Keyboard.init(window);
    }
}
