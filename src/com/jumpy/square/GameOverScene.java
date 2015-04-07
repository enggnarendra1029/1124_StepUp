package com.jumpy.square;

import com.jumpy.square.SceneManager.SceneType;

public class GameOverScene extends BaseScene {

	@Override
	public void createScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		SceneManager.getInstance().createMenuScene();
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

}
