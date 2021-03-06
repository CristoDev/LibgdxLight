package com.light.v1.tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.light.v1.ecs.ECSEvent;

public class AnimationEntity {
    protected String defaultSpritePath = "body/male/red_orc.png";
    protected static String TAG="Character";

    protected Vector2 position=new Vector2(0, 0);
    protected AnimationManager animationManager=new AnimationManager();

    public void init() {
        animationManager.setDefaultSpritePath(defaultSpritePath);
        animationManager.loadTextureAsset(defaultSpritePath);
    }

    public void loadAllAnimations() {
        animationManager.loadAllAnimations();
    }

    public void loadAllAnimationsFromDefaultFile() {
        animationManager.loadAllAnimationsFromDefaultFile();
    }

    public void update(float delta){
        animationManager.update(delta);
    }

    public void render (SpriteBatch batch) {
        animationManager.render(batch, position);
    }

    public void createOrc() {
        setDefaultSpritePath("body/male/red_orc.png");
    }

    public void createHuman() {
        setDefaultSpritePath("body/male/dark.png");
    }

    public void setDefaultSpritePath(String _defaultSpritePath) {
        this.defaultSpritePath = _defaultSpritePath;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        setPosition(new Vector2(x, y));
    }

    public void setPosition(float x, float y) {
        setPosition(new Vector2(x, y));
    }

    public void addEquipment(String filename) {
        animationManager.addElement(filename);
    }

    public void setAnimationState(ECSEvent.AnimationState state) {
        animationManager.setAnimationState(state);
    }

    public void setAnimationState(String state) {
        animationManager.setAnimationState(state);
    }

    public void setAnimationDirection(ECSEvent.AnimationDirection direction) {
        animationManager.setAnimationDirection(direction);
    }

    public void setAnimationDirection(String direction) {
        animationManager.setAnimationDirection(direction);
    }
}
