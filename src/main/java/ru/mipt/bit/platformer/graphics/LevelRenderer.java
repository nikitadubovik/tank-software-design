package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GridPosition;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class LevelRenderer implements TileGrid, Disposable {

    private final TiledMap tiledMap;
    private final MapRenderer mapRenderer;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;

    public LevelRenderer(String mapPath, Batch batch, Interpolation movementInterpolation) {
        this.tiledMap = new TmxMapLoader().load(mapPath);
        this.mapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        this.groundLayer = getSingleLayer(tiledMap);
        this.tileMovement = new TileMovement(groundLayer, movementInterpolation);
    }

    public void render() {
        mapRenderer.render();
    }

    @Override
    public void placeAtTileCenter(Rectangle rectangle, GridPosition cell) {
        moveRectangleAtTileCenter(groundLayer, rectangle, toGridPoint(cell));
    }

    @Override
    public void placeBetweenTileCenters(Rectangle rectangle, GridPosition from, GridPosition to, float progress) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, toGridPoint(from), toGridPoint(to), progress);
    }

    private static GridPoint2 toGridPoint(GridPosition cell) {
        return new GridPoint2(cell.getX(), cell.getY());
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
    }
}
