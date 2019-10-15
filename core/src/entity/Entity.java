package entity;

import com.badlogic.gdx.math.Vector2;
import components.ComponentGraphics;
import components.ComponentInput;

public class Entity {
    protected Vector2 position;
    protected Vector2 direction;

    protected ComponentInput input;
    protected ComponentGraphics graphics;

    public Entity(ComponentInput _input, ComponentGraphics _graphics) {
        input=_input;
        graphics=_graphics;
    }

}
