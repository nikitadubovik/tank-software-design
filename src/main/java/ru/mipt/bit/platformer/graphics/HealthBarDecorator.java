package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Damageable;

public class HealthBarDecorator implements BoundedRenderable {

    private static final float BAR_WIDTH = 80f;
    private static final float BAR_HEIGHT = 8f;
    private static final float GAP = 6f;
    private static final Color BACKGROUND = new Color(0.15f, 0.15f, 0.15f, 0.85f);
    private static final Color HIGH = new Color(0.3f, 0.85f, 0.3f, 1f);
    private static final Color MEDIUM = new Color(0.95f, 0.8f, 0.2f, 1f);
    private static final Color LOW = new Color(0.9f, 0.25f, 0.2f, 1f);

    private final BoundedRenderable renderable;
    private final Damageable target;
    private final HealthBarVisibility visibility;
    private final Texture pixel;

    public HealthBarDecorator(BoundedRenderable renderable, Damageable target,
                              HealthBarVisibility visibility, Texture pixel) {
        this.renderable = renderable;
        this.target = target;
        this.visibility = visibility;
        this.pixel = pixel;
    }

    @Override
    public Rectangle getBounds() {
        return renderable.getBounds();
    }

    @Override
    public void render(Batch batch) {
        renderable.render(batch);
        if (visibility.isVisible()) {
            renderHealthBar(batch);
        }
    }

    private void renderHealthBar(Batch batch) {
        Rectangle bounds = renderable.getBounds();
        float x = bounds.x + (bounds.width - BAR_WIDTH) / 2f;
        float y = bounds.y + bounds.height + GAP;
        float share = healthShare();

        batch.setColor(BACKGROUND);
        batch.draw(pixel, x, y, BAR_WIDTH, BAR_HEIGHT);
        batch.setColor(colorFor(share));
        batch.draw(pixel, x, y, BAR_WIDTH * share, BAR_HEIGHT);
        batch.setColor(Color.WHITE);
    }

    private float healthShare() {
        if (target.getMaxHealth() <= 0) {
            return 0f;
        }
        float share = (float) target.getHealth() / target.getMaxHealth();
        return Math.max(0f, Math.min(1f, share));
    }

    private static Color colorFor(float share) {
        if (share > 0.5f) {
            return HIGH;
        }
        if (share > 0.25f) {
            return MEDIUM;
        }
        return LOW;
    }
}
