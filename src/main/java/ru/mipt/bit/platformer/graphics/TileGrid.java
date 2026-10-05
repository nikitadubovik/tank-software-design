package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.GridPosition;

public interface TileGrid {

    void placeAtTileCenter(Rectangle rectangle, GridPosition cell);

    void placeBetweenTileCenters(Rectangle rectangle, GridPosition from, GridPosition to, float progress);
}
