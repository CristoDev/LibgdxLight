package com.light.v1.ecs;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.element.WorldLightTest;

public class LightPlayerEntity extends LightEntity {
    //private static final String TAG = "LightPlayerEntity";
    private WorldLightTest elementLight;

    public LightPlayerEntity(World world, RayHandler rayHandler, OrthographicCamera camera) {
        super(world, rayHandler, camera);
        createAnimation();
    }

    public void createAnimation() {
        /*
        animationEntity=new AnimationEntity();

        animationEntity.createHuman();
        animationEntity.init();
        animationEntity.setPosition(200, 50);
        animationEntity.addEquipment("torso/chain/mail_male.png");
        animationEntity.addEquipment("hands/gloves/male/metal_gloves_male.png");
        animationEntity.addEquipment("weapons/right hand/male/dagger_male.png");
        animationEntity.setAnimationDirection(AnimationManager.AnimationDirection.RIGHT);
        animationEntity.loadAllAnimations();

         */
    }


    public void createLights() {
        elementLight=new WorldLightTest(rayHandler);
    }

    public void update(Batch batch) {
        elementLight.update();
    }

    public void elementLightActivate(float screenX, float screenY) {
        Vector3 point=new Vector3(0, 0, 0);
        camera.unproject(point.set(screenX, screenY, 0));
        elementLight.activate(point);
    }

    public void elementLightSetActive(boolean active) {
        elementLight.setActive(active);
    }
}
