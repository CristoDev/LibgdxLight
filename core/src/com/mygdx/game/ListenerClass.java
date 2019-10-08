package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class ListenerClass implements ContactListener {
    private Box2DLightsSample parent;


    public ListenerClass(Box2DLightsSample parent) {
        this.parent=parent;
        Gdx.app.debug("contact", "constructor");
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void beginContact(Contact contact) {
        Gdx.app.debug("contact", "begin contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        Gdx.app.debug("contact", fa.getBody().getType()+" has hit "+ fb.getBody().getType());
        // Check to see if the collision is between the second sprite and the bottom of the screen
        // If so apply a random amount of upward force to both objects... just because
        /*
        if ((contact.getFixtureA().getBody() == bodyEdgeScreen &&
                contact.getFixtureB().getBody() == body2)
                ||
                (contact.getFixtureA().getBody() == body2 &&
                        contact.getFixtureB().getBody() == bodyEdgeScreen)) {

            body.applyForceToCenter(0, MathUtils.random(20, 50), true);
            body2.applyForceToCenter(0, MathUtils.random(20, 50), true);
        }*/


    }
}