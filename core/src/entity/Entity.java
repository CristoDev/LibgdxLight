package entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.light.v1.LightMap;
import com.light.v1.LightPlayer;
import components.ComponentGraphics;
import components.ComponentInput;
import components.ComponentReceiver;

import java.util.ArrayList;

public class Entity {
    protected Vector2 position;
    protected Vector2 direction;

    protected ArrayList<ComponentReceiver> components;

    protected ComponentInput input;
    protected ComponentGraphics graphics;

    public Entity(ComponentInput _input, ComponentGraphics _graphics) {
        input=_input;
        graphics=_graphics;

        components.add(input);
        components.add(graphics);
    }

    public void sendMessage(String event, String message) {
        for (ComponentReceiver component : components) {
            component.receiveMessage(event, message);
        }
    }

    public void update(LightMap lightMap, Batch batch, float delta){
        //input.update(this, delta);
        //graphics.update(this, lightMap, batch, delta);
    }
}
