package com.light.v1.ecs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;

public interface Component {
    Json json=new Json();
    SystemManager systemManager=SystemManager.getInstance();

    void dispose();
    void receiveMessage(ECSEvent.Event event, String message);
    void update(float delta);
    void render(SpriteBatch batch);
}
