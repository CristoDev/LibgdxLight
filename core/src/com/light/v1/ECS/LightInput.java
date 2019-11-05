package com.light.v1.ECS;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

abstract class LightInput implements Component {
    static Map<ECSEventInput.Keys, ECSEventInput.States> keys = new HashMap<ECSEventInput.Keys, ECSEventInput.States>();

    static {
        keys.put(ECSEventInput.Keys.LEFT, ECSEventInput.States.IDLE);
        keys.put(ECSEventInput.Keys.RIGHT, ECSEventInput.States.IDLE);
        keys.put(ECSEventInput.Keys.UP, ECSEventInput.States.IDLE);
        keys.put(ECSEventInput.Keys.DOWN, ECSEventInput.States.IDLE);
        keys.put(ECSEventInput.Keys.QUIT, ECSEventInput.States.IDLE);
        keys.put(ECSEventInput.Keys.PAUSE, ECSEventInput.States.IDLE);
    };

    static Map<ECSEventInput.Buttons, ECSEventInput.States> buttons = new HashMap<ECSEventInput.Buttons, ECSEventInput.States>();

    static {
        buttons.put(ECSEventInput.Buttons.LEFT, ECSEventInput.States.IDLE);
        buttons.put(ECSEventInput.Buttons.RIGHT, ECSEventInput.States.IDLE);
    };

    void keyPressed(int keycode, ECSEventInput.States state) {
        switch (keycode) {
            case Input.Keys.LEFT:
                keys.put(ECSEventInput.Keys.LEFT, state);
                break;
            case Input.Keys.RIGHT:
                keys.put(ECSEventInput.Keys.RIGHT, state);
                break;
            case Input.Keys.UP:
                keys.put(ECSEventInput.Keys.UP, state);
                break;
            case Input.Keys.DOWN:
                keys.put(ECSEventInput.Keys.DOWN, state);
                break;
        }
    }

    static Vector2 mousePosition=new Vector2(0, 0);
}
