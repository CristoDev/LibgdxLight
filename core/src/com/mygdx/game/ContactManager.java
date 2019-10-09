package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class ContactManager implements ContactListener {
    private static final String TAG = "ContactManager";

    @Override
    public void beginContact(Contact contact) {
        Gdx.app.debug(TAG, "beginContact "+contact.getFixtureA().getBody().getUserData().toString());
        Fixture b=contact.getFixtureB();
        //contact.getFixtureB().getBody().setLinearVelocity(new Vector2(0, 0));
        //contact.getFixtureB().getBody().applyForceToCenter(-10, 0, true);
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.debug(TAG, "endContact "+contact.getFixtureA().getBody().getUserData().toString());
        Gdx.app.debug(TAG, "*****************************************");
        //contact.getFixtureB().getBody().setLinearVelocity(new Vector2(0, 0));
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //Gdx.app.debug(TAG, "preSolve");
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //Gdx.app.debug(TAG, "postSolve");
    }
}
