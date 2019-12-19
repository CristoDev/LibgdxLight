package com.light.v1.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.light.v1.tools.AnimationEntity;
import com.light.v1.tools.AnimationManager;

public class ImageScreen {
    protected SpriteBatch batch = null;
    int x = 0, y = 0, dx = 1, dy = 2, nb = 30;

    Array<Vector2> positions = new Array<Vector2>();
    Array<Vector2> deltas = new Array<Vector2>();

    AnimationEntity p = null;
    AnimationEntity o = null;

    public ImageScreen() {
        create();
    }

    private void updatePosition() {
        for (int i = 0; i < nb; i++) {
            float dx = deltas.get(i).x, dy = deltas.get(i).y;

            if (positions.get(i).x >= 200) {
                dx = -1;
            }

            if (positions.get(i).y >= 400) {
                dy = -1;
            }

            if (positions.get(i).x <= 0) {
                dx = 1;
            }

            if (positions.get(i).y <= 0) {
                dy = 1;
            }

            positions.set(i, new Vector2(positions.get(i).x + dx, positions.get(i).y + dy));
            deltas.set(i, new Vector2(dx, dy));
        }

    }

    public void update(float delta) {
        if (p != null)
            p.update(delta);

        if (o != null)
            o.update(delta);

    }

    public void render(float delta) {
        updatePosition();
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0.6f, 0.6f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        if (p != null)
            p.render(batch);

        if (o != null)
            o.render(batch);

        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }

    public void create() {
        batch = new SpriteBatch();

        p = new AnimationEntity();
        o = new AnimationEntity();

        if (p != null) {
            p.createHuman();
            p.init();
            p.setPosition(200, 50);
            p.addEquipment("torso/chain/mail_male.png");
            p.addEquipment("hands/gloves/male/metal_gloves_male.png");
            p.addEquipment("weapons/right hand/male/dagger_male.png");
            p.setAnimationDirection(AnimationManager.AnimationDirection.RIGHT);
            p.loadAllAnimations();
        }

        if (o != null) {
            o.createOrc();
            o.init();
            o.setPosition(50, 50);
            o.addEquipment("weapons/right hand/male/spear_male.png");
            o.setAnimationDirection(AnimationManager.AnimationDirection.UP);
            o.setAnimationState(AnimationManager.AnimationState.HURT);
            o.loadAllAnimations();
        }

    }
}