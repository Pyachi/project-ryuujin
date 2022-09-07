package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Textures;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.objects.tiles.tags.Collision;
import com.cs321.team1.framework.objects.tiles.tags.Movement;
import com.cs321.team1.util.Keyboard;

import java.util.List;

public class Player extends GameObject implements Movement, Collision {
    private Direction dir = Direction.UP;

    public Player() {
        setTilePosition(9, 9);
        locZ = 3;
    }

    @Override
    public void calculateMovement() {
        int dX = 0, dY = 0;

        if (Keyboard.isKeyPressed('w')) dY -= 2;
        if (Keyboard.isKeyPressed('s')) dY += 2;
        if (Keyboard.isKeyPressed('a')) dX -= 2;
        if (Keyboard.isKeyPressed('d')) dX += 2;

        if (dY < 0) dir = Direction.UP;
        else if (dY > 0) dir = Direction.DOWN;
        else if (dX < 0) dir = Direction.LEFT;
        else if (dX > 0) dir = Direction.RIGHT;
        texture = dir.texture;

        if (dX != 0 || dY != 0) move(dX, dY);
    }

    @Override
    public void calculateCollision() {
        List<UnpassableTile> collisions = getCollisionsOfType(UnpassableTile.class);
        while (!collisions.isEmpty()) {
            GameObject closestTile = collisions.get(0);
            int closestDis = getDistanceSqr(closestTile);
            for (GameObject tile : collisions) {
                if (getDistanceSqr(tile) < closestDis) {
                    closestTile = tile;
                    closestDis = getDistanceSqr(tile);
                }
            }
            int dX = getX() - closestTile.getX();
            int dY = getY() - closestTile.getY();
            if (Math.abs(dX) >= Math.abs(dY)) {
                if (dX >= 0) move(1, 0);
                else move(-1, 0);
            } else {
                if (dY >= 0) move(0, 1);
                else move(0, -1);
            }
            collisions = getCollisionsOfType(UnpassableTile.class);
        }
    }

    private enum Direction {
        UP(Textures.PLAYER_UP), DOWN(Textures.PLAYER_DOWN), LEFT(Textures.PLAYER_LEFT), RIGHT(Textures.PLAYER_RIGHT);

        public final Textures texture;

        Direction(Textures texture) {
            this.texture = texture;
        }
    }
}
