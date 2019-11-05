package com.light.v1.ECS;

public interface ECSEvent {
    String MESSAGE_TOKEN = ":::::";

    enum EVENT {
        CURRENT_POSITION,
        INIT_START_POSITION,
        CURRENT_DIRECTION,
        CURRENT_STATE,
        COLLISION_WITH_MAP,
        COLLISION_WITH_ENTITY,
        LOAD_ANIMATIONS,
        INIT_DIRECTION,
        INIT_STATE,
        INIT_SELECT_ENTITY,
        ENTITY_SELECTED,
        ENTITY_DESELECTED,
        CURRENT_ACTION
    }

    enum DIRECTION {
        UP,
        DOWN,
        LEFT,
        RIGHT
    };

    enum COMPAS {
        NORTH,
        SOUTH,
        EAST,
        WEST
    };
}
