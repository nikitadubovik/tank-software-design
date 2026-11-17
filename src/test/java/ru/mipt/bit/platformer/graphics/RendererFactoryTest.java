package ru.mipt.bit.platformer.graphics;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GridPosition;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RendererFactoryTest {

    private static final Renderable TREE_RENDERER = batch -> { };
    private static final Renderable TANK_RENDERER = batch -> { };

    @Test
    void usesCreatorRegisteredForObjectType() {
        RendererFactory factory = new RendererFactory();
        factory.register(Tree.class, tree -> TREE_RENDERER);
        factory.register(Tank.class, tank -> TANK_RENDERER);

        assertSame(TREE_RENDERER, factory.create(new Tree(new GridPosition(0, 0))));
        assertSame(TANK_RENDERER, factory.create(new Tank(new GridPosition(0, 0), 0.4f)));
    }

    @Test
    void passesTheObjectToCreator() {
        RendererFactory factory = new RendererFactory();
        Tree[] received = new Tree[1];
        factory.register(Tree.class, tree -> {
            received[0] = tree;
            return TREE_RENDERER;
        });
        Tree tree = new Tree(new GridPosition(1, 1));

        factory.create(tree);

        assertSame(tree, received[0]);
    }

    @Test
    void failsForUnregisteredType() {
        RendererFactory factory = new RendererFactory();

        assertThrows(IllegalArgumentException.class, () -> factory.create(new Tree(new GridPosition(0, 0))));
    }
}
