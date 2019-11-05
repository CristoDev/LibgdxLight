package com.light.v1.ECS;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Json;
import com.light.v1.LightPlayer;

public interface Component {

    Json json=new Json();

    void dispose();
    void receiveMessage(ECSEvent.EVENT event, String message);
    void update(LightPlayer lightPlayer, float delta, Batch batch);
}
