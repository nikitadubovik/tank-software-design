package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

public class RendererFactory implements Disposable {

    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final String BULLET_TEXTURE = "images/bullet.png";
    private static final Color PLAYER_TINT = Color.WHITE;
    private static final Color ENEMY_TINT = new Color(1f, 0.45f, 0.45f, 1f);

    private final TileGrid grid;
    private final Tank player;
    private final Texture tankTexture = new Texture(TANK_TEXTURE);
    private final Texture treeTexture = new Texture(TREE_TEXTURE);
    private final Texture bulletTexture = new Texture(BULLET_TEXTURE);

    public RendererFactory(TileGrid grid, Tank player) {
        this.grid = grid;
        this.player = player;
    }

    public Renderable create(GameObject object) {
        if (object instanceof Tank) {
            Color tint = object == player ? PLAYER_TINT : ENEMY_TINT;
            return new TankRenderer((Tank) object, grid, new TextureRegion(tankTexture), tint);
        }
        if (object instanceof Tree) {
            return new TreeRenderer((Tree) object, grid, new TextureRegion(treeTexture));
        }
        if (object instanceof Bullet) {
            return new BulletRenderer((Bullet) object, grid, new TextureRegion(bulletTexture));
        }
        throw new IllegalArgumentException("Не знаю, как нарисовать " + object.getClass().getSimpleName());
    }

    @Override
    public void dispose() {
        tankTexture.dispose();
        treeTexture.dispose();
        bulletTexture.dispose();
    }
}
