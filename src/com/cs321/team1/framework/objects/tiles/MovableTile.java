package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.Textures;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.tiles.tags.Collision;

public class MovableTile extends GameObject implements Collision {
    public MovableTile(int locX, int locY, Textures texture) {
        this.texture = texture;
        setTilePosition(locX, locY);
    }

    @Override
    public void calculateCollision() {
        Player player = Game.i.getPlayer();
        int dx = 0, dy = 0;
        while (collidesWith(player)) {
            int x = getX() - player.getX();
            int y = getY() - player.getY();
            if (Math.abs(x) >= Math.abs(y)) {
                if (x >= 0) dx = 1;
                else dx = -1;
            } else {
                if (y >= 0) dy = 1;
                else dy = -1;
            }
            move(dx, dy);
            if (!getCollisionsOfType(UnpassableTile.class).isEmpty()) {
                move(-dx, -dy);
                while (collidesWith(player))
                    player.move(-dx, -dy);
            }
        }
    }
}
