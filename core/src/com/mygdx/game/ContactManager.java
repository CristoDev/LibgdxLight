package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class ContactManager implements ContactListener {
    private static final String TAG = "ContactManager";

    @Override
    public void beginContact(Contact contact) {
        Gdx.app.debug("beginContact", contact.getFixtureA().getBody().getUserData().toString());
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.debug("endContact", contact.getFixtureA().getBody().getUserData().toString());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
