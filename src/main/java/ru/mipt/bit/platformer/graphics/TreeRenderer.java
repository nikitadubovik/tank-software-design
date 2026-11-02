package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tree;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class TreeRenderer implements Renderable {

    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TreeRenderer(Tree tree, TileGrid grid, TextureRegion graphics) {
        this.graphics = graphics;
        this.rectangle = createBoundingRectangle(graphics);
        grid.placeAtTileCenter(rectangle, tree.getPosition());
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }
}
