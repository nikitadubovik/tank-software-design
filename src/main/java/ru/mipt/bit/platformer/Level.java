package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class Level implements Disposable {

    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;
    private final Obstacles obstacles = new Obstacles();
    private final List<TreeRenderer> treeRenderers = new ArrayList<>();

    public Level(String mapPath, Batch batch) {
        this.tiledMap = new TmxMapLoader().load(mapPath);
        this.tiledMapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        this.groundLayer = getSingleLayer(tiledMap);
        this.tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
    }

    public void addObstacle(String texturePath, GridPosition position) {
        Tree tree = new Tree(position);
        obstacles.add(tree);
        treeRenderers.add(new TreeRenderer(texturePath, tree, groundLayer));
    }

    public ObstacleMap getObstacles() {
        return obstacles;
    }
    public void moveBetweenTiles(Rectangle rectangle, GridPosition from, GridPosition to, float progress) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, from.toGridPoint2(), to.toGridPoint2(), progress);
    }

    public void render() {
        tiledMapRenderer.render();
    }

    public void renderObstacles(Batch batch) {
        for (TreeRenderer renderer : treeRenderers) {
            renderer.render(batch);
        }
    }

    @Override
    public void dispose() {
        for (TreeRenderer renderer : treeRenderers) {
            renderer.dispose();
        }
        tiledMap.dispose();
    }
}