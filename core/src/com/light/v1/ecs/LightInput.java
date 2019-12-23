package com.light.v1.ecs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class LightInput implements Component {
    public static Vector2 mousePosition=new Vector2(0, 0);
    public static Map<ECSEventInput.Keys, ECSEventInput.States> keyDirections = new HashMap<ECSEventInput.Keys, ECSEventInput.States>();
    private boolean idle=true;
    protected LinkedList<ECSEvent.AnimationDirection> directionQueue=new LinkedList<>();

    static {
        keyDirections.put(ECSEventInput.Keys.LEFT, ECSEventInput.States.IDLE);
        keyDirections.put(ECSEventInput.Keys.RIGHT, ECSEventInput.States.IDLE);
        keyDirections.put(ECSEventInput.Keys.UP, ECSEventInput.States.IDLE);
        keyDirections.put(ECSEventInput.Keys.DOWN, ECSEventInput.States.IDLE);
    };

    public static Map<ECSEventInput.Keys, ECSEventInput.States> keyActions = new HashMap<ECSEventInput.Keys, ECSEventInput.States>();

    static {
        keyActions.put(ECSEventInput.Keys.SPACE, ECSEventInput.States.IDLE);
        keyActions.put(ECSEventInput.Keys.QUIT, ECSEventInput.States.IDLE);
        keyActions.put(ECSEventInput.Keys.PAUSE, ECSEventInput.States.IDLE);
    }

    public static Map<ECSEventInput.Buttons, ECSEventInput.States> mouseButtons = new HashMap<ECSEventInput.Buttons, ECSEventInput.States>();

    static {
        mouseButtons.put(ECSEventInput.Buttons.LEFT, ECSEventInput.States.IDLE);
        mouseButtons.put(ECSEventInput.Buttons.RIGHT, ECSEventInput.States.IDLE);
    };

    protected void addQueueDirection(ECSEvent.AnimationDirection direction) {
        if (!directionQueue.contains(direction)) {
            directionQueue.add(direction);
        }
    }

    protected ECSEvent.AnimationDirection removeQueueDirection(ECSEvent.AnimationDirection direction) {
        directionQueue.remove(direction);

        if (directionQueue.size() > 0) {
            return directionQueue.getLast();
        }
        else {
            return null;
        }
    }

    protected ECSEvent.AnimationDirection keyPressed(int keycode, ECSEventInput.States state) {
        ECSEvent.AnimationDirection direction=null;
        switch (keycode) {
            case Input.Keys.LEFT:
                keyDirections.put(ECSEventInput.Keys.LEFT, state);
                direction=ECSEvent.AnimationDirection.LEFT;
                break;
            case Input.Keys.RIGHT:
                keyDirections.put(ECSEventInput.Keys.RIGHT, state);
                direction=ECSEvent.AnimationDirection.RIGHT;
                break;
            case Input.Keys.UP:
                keyDirections.put(ECSEventInput.Keys.UP, state);
                direction=ECSEvent.AnimationDirection.UP;
                break;
            case Input.Keys.DOWN:
                keyDirections.put(ECSEventInput.Keys.DOWN, state);
                direction=ECSEvent.AnimationDirection.DOWN;
                break;
            case Input.Keys.SPACE:
                keyActions.put(ECSEventInput.Keys.SPACE, state);
                break;
            default:
                // nothing to do
        }

        return direction;
    }

    protected void updateKeys(LightEntity lightEntity, Map<ECSEventInput.Keys, ECSEventInput.States> entries, ECSEvent.Event event) {
        LightPlayerEntity lightPlayerEntity=(LightPlayerEntity)lightEntity;
        idle=true;

        for (Map.Entry<ECSEventInput.Keys, ECSEventInput.States> entry : entries.entrySet()) {
            if (entry.getValue() != ECSEventInput.States.IDLE) {
                idle=false;
                sendMessage(lightPlayerEntity, event, buildMessageKey(entry));
            }

            if (entry.getValue() == ECSEventInput.States.DOWN) {
                idle=false;
                entries.put(entry.getKey(), ECSEventInput.States.PRESSED);
            }

            if (entry.getValue() == ECSEventInput.States.UP) {
                entries.put(entry.getKey(), ECSEventInput.States.IDLE);
            }
        }
    }

    protected void updateKeyDirections(LightEntity lightEntity) {
        updateKeys(lightEntity, keyDirections, ECSEvent.Event.KEY_DIRECTION);

        if (idle) {
            sendMessage((LightPlayerEntity)lightEntity, ECSEvent.Event.SET_STATE, ECSEvent.AnimationState.IDLE.toString());
        }
        else {
            sendMessage((LightPlayerEntity)lightEntity, ECSEvent.Event.SET_STATE, ECSEvent.AnimationState.WALK.toString());
        }
    }

    protected void updateKeyActions(LightEntity lightEntity) {
        updateKeys(lightEntity, keyActions, ECSEvent.Event.KEY_ACTION);
    }

    protected void updateButtons(LightEntity lightEntity) {
        LightPlayerEntity lightPlayerEntity=(LightPlayerEntity)lightEntity;

        for (Map.Entry<ECSEventInput.Buttons, ECSEventInput.States> entry : mouseButtons.entrySet()) {
            if (entry.getValue() != ECSEventInput.States.IDLE) {
                sendMessage(lightPlayerEntity, ECSEvent.Event.MOUSE_ACTION, buildMessageMouseButtons(entry));
            }

            if (entry.getValue() == ECSEventInput.States.DOWN) {
                mouseButtons.put(entry.getKey(), ECSEventInput.States.PRESSED);
            }

            if (entry.getValue() == ECSEventInput.States.UP) {
                mouseButtons.put(entry.getKey(), ECSEventInput.States.IDLE);
            }
        }

    }

    protected void sendMessage(LightPlayerEntity lightPlayerEntity, ECSEvent.Event event, String message) {
        SystemManager.getInstance().sendMessage(lightPlayerEntity, event, message);
    }

    protected String buildMessageKey(Map.Entry<ECSEventInput.Keys, ECSEventInput.States> entry) {
        return json.toJson(entry.getKey()+ECSEvent.MESSAGE_TOKEN+entry.getValue()+ECSEvent.MESSAGE_TOKEN+mousePosition.x+ECSEvent.MESSAGE_TOKEN+mousePosition.y);
    }

    protected String buildMessageMouseButtons(Map.Entry<ECSEventInput.Buttons, ECSEventInput.States> entry) {
        return json.toJson(entry.getKey()+ECSEvent.MESSAGE_TOKEN+entry.getValue()+ECSEvent.MESSAGE_TOKEN+mousePosition.x+ECSEvent.MESSAGE_TOKEN+mousePosition.y);
    }
}
