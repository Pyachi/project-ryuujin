package com.cs321.team1;

import com.cs321.team1.framework.Game;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
       JFrame window = new JFrame();
       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       window.setResizable(false);
       window.setTitle("Project 龍神");
       Game game = new Game();
       window.add(game);
       window.pack();
       window.setLocationRelativeTo(null);
       window.setVisible(true);
    }
}
