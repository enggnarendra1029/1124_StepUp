package com.jumpy.square;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;

import com.jumpy.square.SceneManager.SceneType;

public class SplashScene extends BaseScene {

	 private Text splashText;
	private Text text;

	@Override
	    public void createScene() {
	 
		text = new Text(0, 15, mResourceManager.font, "Loading...",
				mVertexBufferObjectManager){
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				// TODO Auto-generated method stub
				super.preDraw(pGLState, pCamera);
			}
		};
		text.setScale(0.7f);
		text.setPosition(mActivity.CAMERA_WIDTH/2-text.getWidth()/2,mActivity.CAMERA_HEIGHT/2-text.getHeight()/2);
		attachChild(text);
		
		 	
	    }
	     
	    @Override
	    public void onBackKeyPressed() {
	        mActivity.finish();
	    }
	 
	    @Override
	    public SceneType getSceneType() {
	        return SceneType.SCENE_SPLASH;
	    }
	 
	    @Override
	    public void disposeScene() {
	    }

}
