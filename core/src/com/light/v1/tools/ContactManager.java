package com.light.v1.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;

public class ContactManager implements ContactListener {
    private static final String TAG = "ContactManager";

    @Override
    public void beginContact(Contact contact) {
        Gdx.app.debug("beginContact", TimeUtils.millis()+" // "+contact.getFixtureA().getBody().getUserData().toString()+" --> "+contact.getFixtureB().getBody().getUserData().toString());
    }

    @Override
    public void endContact(Contact contact) {
        //Gdx.app.debug("endContact  ", TimeUtils.millis()+" // "+contact.getFixtureA().getBody().getUserData().toString()+" --> "+contact.getFixtureB().getBody().getUserData().toString());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
