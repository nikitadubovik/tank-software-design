package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class TankRenderer implements Disposable {

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TankRenderer(String texturePath) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(graphics);
    }

    public void update(Level level, Tank tank) {
        level.moveBetweenTiles(rectangle, tank.getPosition(), tank.getDestination(), tank.getMovementProgress());
    }

    public void render(Batch batch, Tank tank) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, tank.getAngle());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}