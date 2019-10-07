package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

public class MyContact extends Contact {
    protected MyContact(World world, long addr) {
        super(world, addr);
    }
}
