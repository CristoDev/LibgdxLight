package com.light.v1.ecs;

public interface ECSEvent {
    String MESSAGE_TOKEN = ":::::";

    enum Event {
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
        CURRENT_ACTION,
        MOUSE_ACTION,
        KEY_ACTION,
        KEY_DIRECTION,
        SPEED_MODIFIER,
        SPEED_MODIFIER_REVERSE
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    enum Compas {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }
}
