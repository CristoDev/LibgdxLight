package com.light.v1.element;

import com.badlogic.gdx.utils.TimeUtils;

public interface WorldElement {
    long startTime= TimeUtils.millis();
    String name="WorldElement";

    void update();

}
