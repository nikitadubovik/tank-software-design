package ru.mipt.bit.platformer.graphics;

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

    public TankRenderer(String texturePath, Tank tank, TileGrid grid) {
        this.tank = tank;
        this.grid = grid;
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(graphics);
    }

    @Override
    public void render(Batch batch) {
        grid.placeBetweenTileCenters(rectangle, tank.getPosition(), tank.getDestination(), tank.getMovementProgress());
        drawTextureRegionUnscaled(batch, graphics, rectangle, tank.getDirection().getAngle());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
