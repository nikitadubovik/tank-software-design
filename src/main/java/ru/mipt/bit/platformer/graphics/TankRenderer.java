package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class TankRenderer implements Renderable {

    private final Tank tank;
    private final TileGrid grid;
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final Color tint;

    public TankRenderer(String texturePath, Tank tank, TileGrid grid, Color tint) {
        this.tank = tank;
        this.grid = grid;
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(graphics);
        this.tint = tint;
    }

    @Override
    public void render(Batch batch) {
        grid.placeBetweenTileCenters(rectangle, tank.getPosition(), tank.getDestination(), tank.getMovementProgress());
        batch.setColor(tint);
        drawTextureRegionUnscaled(batch, graphics, rectangle, tank.getDirection().getAngle());
        batch.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
