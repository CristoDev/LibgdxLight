package com.light.v1.ecs;

public interface ECSFilter {
    short VOID =        0x00000;
    // categories
    short FLOOR =       0x00001;
    short WARP =        0x00002;
    short WALL =        0x00004;
    //8, 16, 32, 64
    short PLAYER =      0x00128;
    short ENEMY =       0x00256;
    short NPC =         0x00512;
    short OBSTACLE =    0x01024;
    short LIGHT =       0x02048;
    // 512, 1024, 2048, 4096, 8192, 16384, 32768



    // masques
    short MASK_PLAYER = OBSTACLE | FLOOR | WALL | OBSTACLE | ENEMY;
    short MASK_ENEMY = FLOOR | WALL | PLAYER | OBSTACLE;
    short MASK_LIGHT = WALL | OBSTACLE;
    short MASK_OBSTACLE = ENEMY | PLAYER | LIGHT;
    short MASK_FLOOR = -1;
    short MASK_WALL = -1;
    short MASK_ALL = -1;
    short MASK_VOID = 0;
}
