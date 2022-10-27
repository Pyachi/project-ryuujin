package com.cs321.team1.objects;

import com.cs321.team1.GameObject;
import com.cs321.team1.Tick;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Vec2;

public class Navigator extends GameObject {
    
    public Navigator(Vec2 loc) {
        setTexture(new Texture("player/nav", 5));
        setSize(new Vec2(16, 16));
        setLocation(loc);
    }
    
    boolean moving = false;
    int moveTick = 0;
    int moveX = 0;
    int moveY = 0;
    
    @Tick
    public void checkMovement() {
        if (moving) {
            moveTick--;
            super.move(moveX,moveY);
            if (moveTick == 0) moving = false;
        } else {
            if (Controls.UP.isPressed() && canMove(0, -1)) startMoving(0,-1);
            else if (Controls.DOWN.isPressed() && canMove(0, 1)) startMoving(0,1);
            else if (Controls.LEFT.isPressed() && canMove(-1, 0)) startMoving(-1,0);
            else if (Controls.RIGHT.isPressed() && canMove(1, 0)) startMoving(1,0);
        }
    }
    
    private boolean canMove(int x, int y) {
        super.move(x * 16, y * 16);
        boolean collision = collidesWith(LevelObject.class);
        super.move(-x * 16, -y * 16);
        return collision;
    }
    
    private void startMoving(int x, int y) {
        moveX = x*2;
        moveY = y*2;
        moving = true;
        moveTick = 8;
    }
    
    @Override
    public String toString() {
        return "NAV|" + getLocation().toString();
    }
}
