package com.light.v1.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.*;

public class AnimationManager {
    protected static final String TAG="AnimationManager";
    protected static final AssetManager assetManager = new AssetManager();
    protected static final String ROOTPATH="Universal-LPC-spritesheet/";
    protected String defaultSpritePath = "body/male/red_orc.png";
    protected static InternalFileHandleResolver filePathResolver =  new InternalFileHandleResolver();

    protected Pixmap character=null;
    protected List<String> elements=new ArrayList<String>();
    protected float frameDuration=0.1f;
    protected TextureRegion currentFrame = null;
    protected int FRAME_WIDTH = 64;
    protected int FRAME_HEIGHT = 64;
    protected float FRAME_WIDTH_SCALE = 1/32f;
    protected float FRAME_HEIGHT_SCALE = 1/32f;

    protected Hashtable<AnimationState, Animation<TextureRegion>> animations;
    protected HashMap<AnimationState, HashMap<String, Animation<TextureRegion>>> animationsFull;

    protected Animation currentAnimation;
    protected AnimationState currentAnimationState;
    protected AnimationDirection currentAnimationDirection;
    protected int left=9;
    protected int up=8;
    protected int down=10;
    protected int right=11;
    protected float _frameTime = 0f;
    private boolean debug=false;
    protected boolean idle=true;

    // petu etre utiliser une classe avec une variable reprenant l'enum?
    public enum AnimationState {
        SPELLCAST(0, 3, 7),
        THRUST(4, 7, 8),
        WALK(8, 11, 9),
        SLASH(12, 15, 6),
        SHOOT(16,19,13),
        HURT(20,20,6);

        private int rowStart;
        private int rowEnd;
        private int framesCount;

        AnimationState(int start, int end, int count) {
            rowStart=start;
            rowEnd=end;
            framesCount=count;
        }

        public int getRowStart() {
            return rowStart;
        }

        public int getRowEnd() {
            return rowEnd;
        }

        public int getFramesCount() {
            return framesCount;
        }
    }

    public static enum AnimationDirection {
        UP(0),
        LEFT(1),
        DOWN(2),
        RIGHT(3);

        private int index;

        AnimationDirection(int i) {
            index=i;
        }

        public int getIndex() {
            return index;
        }
    }

    public AnimationManager() {
        animations = new Hashtable<>();
        animationsFull =new HashMap<>();
        currentAnimationState =AnimationState.WALK;
        currentAnimationDirection =AnimationDirection.LEFT;
    }

    private Texture buildTexture() {
        if (elements.size() > 0) {
            character = new Pixmap(Gdx.files.internal(ROOTPATH + defaultSpritePath));

            for (String element : elements) {
                character.drawPixmap(new Pixmap(Gdx.files.internal(element)), 0, 0);
            }

            return new Texture(character);
        }
        else {
            return getTextureAsset(defaultSpritePath);
        }
    }

    public void addElement(String filename) {
        elements.add(ROOTPATH+filename);
    }

    protected void loadAnimation(Texture texture, AnimationState animationState) {
        TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        HashMap<String, Animation<TextureRegion>> animation=new HashMap<>();

        for (AnimationDirection direction : AnimationDirection.values()) {
            Array<TextureRegion> frames = new Array<>(animationState.getFramesCount());

            for (int i = 0; i < animationState.getFramesCount(); i++) {
                TextureRegion region = textureFrames[animationState.getRowStart()+direction.getIndex()][i];
                if (region == null) {
                    Gdx.app.debug(TAG,"loadAnimation::Got null animation frame " + i);
                }
                frames.insert(i, region);
            }

            animation.put(direction.name(), new Animation(frameDuration, frames, Animation.PlayMode.LOOP));
            animationsFull.put(animationState, animation);

            if (animationState.getRowStart() == animationState.getRowEnd()) {
                break ;
            }
        }
    }

    protected void loadAllAnimations(){
        Texture texture = buildTexture();

        for (AnimationState state : AnimationState.values()) {
            loadAnimation(texture, state);
        }

        HashMap<String, Animation<TextureRegion>> animation= animationsFull.get(currentAnimationState);
        currentAnimation =animation.get(currentAnimationDirection.name());
        currentFrame = (TextureRegion) currentAnimation.getKeyFrame(_frameTime);
    }

    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    protected void loadAnimationFromDefaultFile(TextureAtlas textureAtlas, AnimationState animationState) {
        for (AnimationDirection direction : AnimationDirection.values()) {
            Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(animationState.name()+"_"+direction.name());
            HashMap<String, Animation<TextureRegion>> animation=new HashMap<>();
            animation.put(direction.name(), new Animation(frameDuration, regions, Animation.PlayMode.LOOP));
            animationsFull.put(animationState, animation);

            if (animationState.getRowStart() == animationState.getRowEnd()) {
                break ;
            }
        }
    }

    // utile mais l'écriture sur disque prend du temps...
    protected void loadAllAnimationsFromDefaultFile() {
        buildTexture();

        FileHandle destFile=new FileHandle("images/default_character.png");

        if (destFile.exists()) {
            Gdx.app.debug(TAG, "suppression du fichier");
            destFile.delete();
        }

        try {
            PixmapIO.writePNG(destFile, character);
        }
        catch (Exception exception) {
            Gdx.app.debug(TAG, "EXCEPTION!!!!!! "+exception.getMessage());
        }

        TextureAtlas textureAtlas=new TextureAtlas("images/default_character.atlas");
        for (AnimationState state : AnimationState.values()) {
            loadAnimationFromDefaultFile(textureAtlas, state);
        }

        HashMap<String, Animation<TextureRegion>> animation= animationsFull.get(currentAnimationState);
        currentAnimation =animation.get(currentAnimationDirection.name());
        currentFrame = (TextureRegion) currentAnimation.getKeyFrame(_frameTime);
    }

    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    public AnimationState getCurrentAnimationState() {
        return currentAnimationState;
    }

    public void setAnimationState(AnimationState _currentAnimationState) {
        this.currentAnimationState = _currentAnimationState;
    }

    public void setAnimationState(String state) {
        if (state.compareTo("IDLE") == 0) {
            setAnimationIdle();
            return ;
        }

        for (AnimationState animation : AnimationState.values()) {
            if (state.compareTo(animation.toString()) == 0) {
                Gdx.app.debug("ANIMATION", "changement d'état trouvé! "+state);
                setAnimationState(animation);
                return;
            }
        }
    }

    public void setAnimationIdle() {
        idle=true;
        setAnimationState(AnimationState.WALK);
    }

    public void unsetAnimationIdle() {
        idle=false;
    }

    public AnimationDirection getCurrentAnimationDirection() {
        return currentAnimationDirection;
    }

    public void setAnimationDirection(AnimationDirection _currentAnimationDirection) {
        this.currentAnimationDirection = _currentAnimationDirection;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    public void loadTextureAsset(String textureFilenamePath){
        if( textureFilenamePath == null || textureFilenamePath.isEmpty() ){
            return;
        }

        String textureFilenameRootPath=ROOTPATH+textureFilenamePath;

        if( assetManager.isLoaded(textureFilenameRootPath) ){
            return;
        }

        if( filePathResolver.resolve(textureFilenameRootPath).exists() ){
            assetManager.setLoader(Texture.class, new TextureLoader(filePathResolver));
            assetManager.load(textureFilenameRootPath, Texture.class);
            assetManager.finishLoadingAsset(textureFilenameRootPath);
        }
        else{
            Gdx.app.debug(TAG, "loadTextureAsset::Texture doesn't exist!: " + textureFilenameRootPath );
        }
    }

    private Texture getTextureAsset(String textureFilenamePath) {
        Texture texture=null;
        String textureFilenameRootPath=ROOTPATH+textureFilenamePath;

        if (assetManager.isLoaded(textureFilenameRootPath)) {
            texture= assetManager.get(textureFilenameRootPath, Texture.class);
        }
        else {
            Gdx.app.debug(TAG, "getTextureAsset::Texture is not loaded "+ textureFilenameRootPath);
        }

        return texture;
    }

    public void update(float delta) {
        _frameTime = (_frameTime + delta)%5; //Want to avoid overflow
        currentFrame = getCurrentFrame(_frameTime);
    }

    public TextureRegion getCurrentFrame(float _frameTime) {
        return (TextureRegion) currentAnimation.getKeyFrame(_frameTime);
    }

    public void setDefaultSpritePath(String _defaultSpritePath) {
        this.defaultSpritePath = _defaultSpritePath;
    }

    public void render (SpriteBatch batch, Vector2 position) {
        if (debug) {
            Texture bgTest = new Texture(Gdx.files.internal("background_test_64.png"));
            batch.draw(bgTest, position.x, position.y, FRAME_WIDTH * FRAME_WIDTH_SCALE, FRAME_HEIGHT * FRAME_HEIGHT_SCALE);
        }
        batch.draw(currentFrame, position.x, position.y, FRAME_WIDTH*FRAME_WIDTH_SCALE, FRAME_HEIGHT*FRAME_HEIGHT_SCALE);
    }
}
