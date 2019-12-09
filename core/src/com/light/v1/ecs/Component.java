package com.light.v1.ecs;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Json;

public interface Component {
    Json json=new Json();
    SystemManager systemManager=SystemManager.getInstance();

    void dispose();
    void receiveMessage(ECSEvent.EVENT event, String message);
    void update(Batch batch);
    void render(Batch batch);
}
