package com.light.v1;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Json;

public interface Component {
    String MESSAGE_TOKEN = ":::::";
    Json json=new Json();

    void dispose();
    void receiveMessage(String event, String message);
    void update(LightPlayer lightPlayer, Batch batch, float delta);
}
