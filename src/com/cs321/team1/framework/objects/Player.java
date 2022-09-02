package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.Textures;
import com.cs321.team1.framework.Tickable;
import com.cs321.team1.util.Keyboard;

import java.util.List;

public class Player extends GameObject implements Tickable {
    private Direction dir = Direction.UP;

    public Player() {
        setTilePosition(9, 9);
    }

    @Override
    protected Textures getTexture() {
        return dir.texture;
    }

    @Override
    public void tick() {
        Game.i.getObjects().stream().filter(this::collidesWith).forEach(GameObject::update);
        calculateMovement();
        calculateCollision();
    }

    private void calculateMovement() {
        int dX = 0, dY = 0;

        if (Keyboard.isKeyPressed('w')) dY -= 2;
        if (Keyboard.isKeyPressed('s')) dY += 2;
        if (Keyboard.isKeyPressed('a')) dX -= 2;
        if (Keyboard.isKeyPressed('d')) dX += 2;

        if (dY < 0) dir = Direction.UP;
        else if (dY > 0) dir = Direction.DOWN;
        else if (dX < 0) dir = Direction.LEFT;
        else if (dX > 0) dir = Direction.RIGHT;

        if (dX != 0 || dY != 0) move(dX, dY);
    }

    private void calculateCollision() {
        List<GameObject> collisions = Game.i.getObjects().stream().filter(it -> it instanceof UnpassableTile && collidesWith(it)).toList();
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
            collisions = Game.i.getObjects().stream().filter(it -> it instanceof UnpassableTile && collidesWith(it)).toList();
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
