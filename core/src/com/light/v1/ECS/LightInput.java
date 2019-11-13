package com.light.v1.ECS;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public abstract class LightInput implements Component {
    static Vector2 mousePosition=new Vector2(0, 0);
    static Map<ECSEventInput.Keys, ECSEventInput.States> keyDirections = new HashMap<ECSEventInput.Keys, ECSEventInput.States>();

    static {
        keyDirections.put(ECSEventInput.Keys.LEFT, ECSEventInput.States.IDLE);
        keyDirections.put(ECSEventInput.Keys.RIGHT, ECSEventInput.States.IDLE);
        keyDirections.put(ECSEventInput.Keys.UP, ECSEventInput.States.IDLE);
        keyDirections.put(ECSEventInput.Keys.DOWN, ECSEventInput.States.IDLE);
    };

    static Map<ECSEventInput.Keys, ECSEventInput.States> keyActions = new HashMap<ECSEventInput.Keys, ECSEventInput.States>();

    static {
        keyActions.put(ECSEventInput.Keys.SPACE, ECSEventInput.States.IDLE);
        keyActions.put(ECSEventInput.Keys.QUIT, ECSEventInput.States.IDLE);
        keyActions.put(ECSEventInput.Keys.PAUSE, ECSEventInput.States.IDLE);
    }

    static Map<ECSEventInput.Buttons, ECSEventInput.States> mouseButtons = new HashMap<ECSEventInput.Buttons, ECSEventInput.States>();

    static {
        mouseButtons.put(ECSEventInput.Buttons.LEFT, ECSEventInput.States.IDLE);
        mouseButtons.put(ECSEventInput.Buttons.RIGHT, ECSEventInput.States.IDLE);
    };

    void keyPressed(int keycode, ECSEventInput.States state) {
        switch (keycode) {
            case Input.Keys.LEFT:
                keyDirections.put(ECSEventInput.Keys.LEFT, state);
                break;
            case Input.Keys.RIGHT:
                keyDirections.put(ECSEventInput.Keys.RIGHT, state);
                break;
            case Input.Keys.UP:
                keyDirections.put(ECSEventInput.Keys.UP, state);
                break;
            case Input.Keys.DOWN:
                keyDirections.put(ECSEventInput.Keys.DOWN, state);
                break;
            case Input.Keys.SPACE:
                keyActions.put(ECSEventInput.Keys.SPACE, state);

        }
    }

    protected void updateKeys(LightEntity lightEntity, Map<ECSEventInput.Keys, ECSEventInput.States> entries, ECSEvent.EVENT event) {
        LightPlayerEntity lightPlayerEntity=(LightPlayerEntity)lightEntity;

        for (Map.Entry<ECSEventInput.Keys, ECSEventInput.States> entry : entries.entrySet()) {
            if (entry.getValue() != ECSEventInput.States.IDLE) {
                sendMessage(lightPlayerEntity, event, buildMessageKey(entry));
            }

            if (entry.getValue() == ECSEventInput.States.DOWN) {
                entries.put(entry.getKey(), ECSEventInput.States.PRESSED);
            }

            if (entry.getValue() == ECSEventInput.States.UP) {
                entries.put(entry.getKey(), ECSEventInput.States.IDLE);
            }
        }
    }

    protected void updateKeyDirections(LightEntity lightEntity) {
        updateKeys(lightEntity, keyDirections, ECSEvent.EVENT.KEY_DIRECTION);
    }

    protected void updateKeyActions(LightEntity lightEntity) {
        updateKeys(lightEntity, keyActions, ECSEvent.EVENT.KEY_ACTION);
    }

    protected void updateButtons(LightEntity lightEntity) {
        LightPlayerEntity lightPlayerEntity=(LightPlayerEntity)lightEntity;

        for (Map.Entry<ECSEventInput.Buttons, ECSEventInput.States> entry : mouseButtons.entrySet()) {
            if (entry.getValue() != ECSEventInput.States.IDLE) {
                sendMessage(lightPlayerEntity, ECSEvent.EVENT.MOUSE_ACTION, buildMessageMouseButtons(entry));
            }

            if (entry.getValue() == ECSEventInput.States.DOWN) {
                mouseButtons.put(entry.getKey(), ECSEventInput.States.PRESSED);
            }

            if (entry.getValue() == ECSEventInput.States.UP) {
                mouseButtons.put(entry.getKey(), ECSEventInput.States.IDLE);
            }
        }

    }


    protected void sendMessage(LightPlayerEntity lightPlayerEntity, ECSEvent.EVENT event, String message) {
        SystemManager.getInstance().sendMessage(lightPlayerEntity, event, message);
    }

    protected String buildMessageKey(Map.Entry<ECSEventInput.Keys, ECSEventInput.States> entry) {
        return json.toJson(entry.getKey()+ECSEvent.MESSAGE_TOKEN+entry.getValue()+ECSEvent.MESSAGE_TOKEN+mousePosition.x+ECSEvent.MESSAGE_TOKEN+mousePosition.y);
    }

    protected String buildMessageMouseButtons(Map.Entry<ECSEventInput.Buttons, ECSEventInput.States> entry) {
        return json.toJson(entry.getKey()+ECSEvent.MESSAGE_TOKEN+entry.getValue()+ECSEvent.MESSAGE_TOKEN+mousePosition.x+ECSEvent.MESSAGE_TOKEN+mousePosition.y);
    }
}
