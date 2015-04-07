package com.jumpy.square;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.jumpy.square.SceneManager.SceneType;



public abstract class BaseScene extends Scene {
	protected final int SCREEN_WIDTH = GameActivity.CAMERA_WIDTH;
    protected final int SCREEN_HEIGHT = GameActivity.CAMERA_HEIGHT;
     
    protected GameActivity mActivity;
    protected Engine mEngine;
    protected Camera mCamera;
    VertexBufferObjectManager mVertexBufferObjectManager;
    protected ResourceManager mResourceManager;
    protected SceneManager mSceneManager;
 
    public BaseScene() {
        mResourceManager = ResourceManager.getInstance();
        mActivity = mResourceManager.mActivity;
        mVertexBufferObjectManager = mActivity.getVertexBufferObjectManager();
        mEngine = mActivity.getEngine();
        mCamera = mEngine.getCamera();
        mSceneManager = SceneManager.getInstance();
        createScene();
    }
     
    public abstract void createScene();
    public abstract void onBackKeyPressed();
    public abstract SceneType getSceneType();
    public abstract void disposeScene();    
}
