package com.jumpy.square;

import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLES20;

import com.jumpy.square.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {

	public MenuScene mMenuScene;
    public MenuScene mSubMenuScene;
	public TiledSprite soundSprite;
 
    protected static final int MENU_PLAY = 0;
    protected static final int MENU_QUIT = 1;
    
    public SharedPreferences sp;
    
    public int SOUND_PLAY = 0;
    public int SOUND_PAUSE = 1;
                 
    private boolean checkSound;
	private Sprite bgImage;
    
    @Override
    public void createScene() {
                 
        mMenuScene = createMenuScene();
        mSubMenuScene = createSubMenuScene();
        
       
        soundSprite = new TiledSprite(175, 50, mResourceManager.mGameSoundTextureRegion, mVertexBufferObjectManager){
        	

			@Override
        	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
        			float pTouchAreaLocalX, float pTouchAreaLocalY) {
        		// TODO Auto-generated method stub
				int spriteIndex = soundSprite.getCurrentTileIndex();
        		if(pSceneTouchEvent.isActionDown()){
        			
        			soundSetting();
           		}
        		
        		return true;
        	}
        };
        soundSprite.setScale(0.5f);
        mMenuScene.registerTouchArea(soundSprite);
        //mMenuScene.attachChild(soundSprite);//exp to remove sound icon
        
                
        /* Attach the menu. */
        this.setChildScene(mMenuScene, false, true, true);
         
      /*  if (!mResourceManager.mMusic.isPlaying()) {
            mResourceManager.mMusic.play();
        }
        */
        
        //ads
       ResourceManager.getInstance().mActivity.adIntShow();
        
    }
     
    @Override
    public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
    	switch(pMenuItem.getID()) {
        case MENU_PLAY:
           /* if (mResourceManager.mMusic.isPlaying()) {
                mResourceManager.mMusic.pause();
            }
            */
        	mResourceManager.mSound.play();
            mMenuScene.closeMenuScene();
            //mActivity.smoothCamera.setBounds(0, 0, mActivity.CAMERA_WIDTH, mActivity.CAMERA_HEIGHT);
            
            mSceneManager.setScene(SceneType.SCENE_GAME);
            
            return true;
             
        case MENU_QUIT:
            /* End Activity. */
        	mResourceManager.mSound.play();
            mActivity.finish();
            return true;               
             
        default:
            return false;
    }
    }  
     
    public MenuScene createMenuScene() {
    	final MenuScene menuScene = new MenuScene(mCamera);
    	 
        final IMenuItem playMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_PLAY, mResourceManager.font, "Play", mVertexBufferObjectManager), new Color(1,1,1), new Color(1.0f, 1.0f, 1.0f));
        playMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(playMenuItem);
         
        final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_QUIT, mResourceManager.font, "Quit", mVertexBufferObjectManager), new Color(1,1,1), new Color(1.0f, 1.0f, 1.0f));
        quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(quitMenuItem);       
     
        menuScene.buildAnimations();
     
        menuScene.setBackgroundEnabled(false);
     
        playMenuItem.setPosition(mActivity.CAMERA_WIDTH-380, mActivity.CAMERA_HEIGHT-100);
        quitMenuItem.setPosition(mActivity.CAMERA_WIDTH-180, mActivity.CAMERA_HEIGHT-100);
        menuScene.setOnMenuItemClickListener(this);
        return menuScene;
    }
     
    public MenuScene createSubMenuScene() {
        //TODO implement
        return null;
    }  
     
    @Override
    public void onBackKeyPressed() {
        if (mMenuScene.hasChildScene())
            mSubMenuScene.back();
        else
            mActivity.finish();
    }
 
    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_MENU;
    }
 
    @Override
    public void disposeScene() {
        //TODO
    }
    //sound Shared Preference
    
    SharedPreferences mSoundPreferences;
	   
    public void soundSetting(){
    	mSoundPreferences = mActivity.getSharedPreferences("MySound",
				Context.MODE_WORLD_WRITEABLE);
    	SharedPreferences.Editor settingsEditor = mSoundPreferences.edit();
		
    	if(SOUND_PLAY==getSound()){
    		settingsEditor.putInt("sound", SOUND_PAUSE);
			settingsEditor.commit();
			mActivity.getEngine().getMusicManager().setMasterVolume(0);
			mActivity.getEngine().getSoundManager().setMasterVolume(0);
			soundSprite.setCurrentTileIndex(1);
			
    	}else{
    		settingsEditor.putInt("sound", SOUND_PLAY);
			settingsEditor.commit();
			mActivity.getEngine().getMusicManager().setMasterVolume(10);
			mActivity.getEngine().getSoundManager().setMasterVolume(10);
			soundSprite.setCurrentTileIndex(0);
    	}
 	
   }
    public int getSound(){
    	mSoundPreferences = mActivity.getSharedPreferences("MySound",
				Context.MODE_WORLD_WRITEABLE);
		return mSoundPreferences.getInt("sound", SOUND_PLAY);
	}
    
    
}
