package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Bullet;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class BulletRenderer implements Renderable {

    private final Bullet bullet;
    private final TileGrid grid;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public BulletRenderer(Bullet bullet, TileGrid grid, TextureRegion graphics) {
        this.bullet = bullet;
        this.grid = grid;
        this.graphics = graphics;
        this.rectangle = createBoundingRectangle(graphics);
    }

    @Override
    public void render(Batch batch) {
        grid.placeBetweenTileCenters(rectangle, bullet.getPosition(), bullet.getDestination(), bullet.getMovementProgress());
        drawTextureRegionUnscaled(batch, graphics, rectangle, bullet.getDirection().getAngle());
    }
}
