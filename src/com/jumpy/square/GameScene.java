package com.jumpy.square;

import java.util.Random;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.hardware.SensorManager;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jumpy.square.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener {

	private PhysicsWorld mPhysicsWorld;
	private FixtureDef wallFixtureDef;
	// private Rectangle tile;
	private Body tileBody;
	private Rectangle leftWall;
	private Body leftWallBody;
	private Rectangle rightWall;
	private Body rightWallBody;
	private int rectpostomove;

	private Rectangle[][] rect;
	private Rectangle[] smallRect;
	private Body[] smallRectBody;
	private Rectangle[] smallRect2;
	private Body[] smallRectBody2;
	Random rand;

	private int vertical_gap;
	private int widthRect;
	private int heightRect = 20;
	private FixtureDef tileFixtureDef;
	private Body[][] rectBody;
	private int moveRectafter = -450;
	private HUD gameHUD;
	private Text scoreText;
	private int index;
	private final int MUSIC_JUMP = 0;
	private final int MUSIC_FALL = 1;
	private int rectPosition;
	private int tempi;
	private int[][] randomTile;
	int score = 0;
	int lastScoreUpdate = 600;
	private Text highScoreText;
	private Text gameOverText;
	private Sprite restartSprite;
	private Body restartSpriteBody;
	private Sprite tile;
	private Sprite bgImage;
	private Sprite scoreBoardSprite;

	public void init() {
		vertical_gap = 300;
		widthRect = 120;
		heightRect = 20;
		moveRectafter = -450;
		int[][] randomTile = { { 0, 240, 360 }, { 0, 120, 240 },
				{ 0, 120, 360 }, { 120, 240, 360 } };
		this.randomTile = randomTile;
	}

	@Override
	public void createScene() {
		// TODO Auto-generated method stub
		mActivity.showBannerAds();
		mActivity.adIntShow();
	//	setBackground(new Background(0.2f,0.5f,2f));
	//	setBackground(new Background(0,0,0));
		// backgournd
		
				
		bgImage = new Sprite(0, 0, mResourceManager.gameBGTextureRegion,
				mVertexBufferObjectManager);
		
		// AutoParallax background
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(
				0, 0, 0, 0);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f,
				bgImage));

		setBackground(autoParallaxBackground);
	
		init();
		mPhysicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH + 20), true);
		tileFixtureDef = PhysicsFactory.createFixtureDef(10f, 0f, 0f);

		createsidewall();
		// Tile
		// createTile();

		//
		tile = new Sprite(240, 450, mResourceManager.mTileTextureRegion,
				mVertexBufferObjectManager);
		tileBody = PhysicsFactory.createBoxBody(mPhysicsWorld, tile,
				BodyType.StaticBody, tileFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(tile,
				tileBody, true, true) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				// TODO Auto-generated method stub
				leftWallBody.setTransform(0 / 32, tile.getY() / 32, 0);
				rightWallBody.setTransform(GameActivity.CAMERA_WIDTH / 32,
						tile.getY() / 32, 0);
				super.onUpdate(pSecondsElapsed);
			}
		});
		tileBody.setUserData("tile");

		attachChild(tile);

		//for trail run commented below line
		mPhysicsWorld.setContactListener(createContactListener());
		registerUpdateHandler(mPhysicsWorld);

		rect = new Rectangle[4][3];
		smallRect = new Rectangle[4];
		smallRect2 = new Rectangle[4];

		createRectangle();
		createSmallRectangle2();

		setOnSceneTouchListener(this);

		mActivity.smoothCamera.setChaseEntity(tile);

		registerUpdateHandler(updateScene());

		createHUD();
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		SceneManager.getInstance().createMenuScene();
		mActivity.getEngine().getMusicManager().setMasterVolume(10);
		mActivity.getEngine().getSoundManager().setMasterVolume(10);
		mActivity.smoothCamera.setHUD(null);
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}

	private void createsidewall() {
		// TODO Auto-generated method stub
		wallFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);

		leftWall = new Rectangle(0, 0, 5, GameActivity.CAMERA_HEIGHT,
				mVertexBufferObjectManager);
		attachChild(leftWall);

		leftWallBody = PhysicsFactory.createBoxBody(mPhysicsWorld, leftWall,
				BodyType.StaticBody, wallFixtureDef);

		rightWall = new Rectangle(-5, 0, 5, GameActivity.CAMERA_HEIGHT,
				mVertexBufferObjectManager);
		attachChild(rightWall);
		rightWallBody = PhysicsFactory.createBoxBody(mPhysicsWorld, rightWall,
				BodyType.StaticBody, wallFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(leftWall,
				leftWallBody, true, true));
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rightWall,
				rightWallBody, true, true));

		leftWallBody.setUserData("leftWall");
		rightWallBody.setUserData("rightWall");

	}

	
	private void createHUD() {
		// TODO Auto-generated method stub

		gameHUD = new HUD();

		scoreBoardSprite = new Sprite(0, 0, mResourceManager.scoreBoardTextureRegion, mVertexBufferObjectManager);
		gameHUD.attachChild(scoreBoardSprite);
		scoreBoardSprite.setVisible(false);
		
		scoreText = new Text(10, 15, mResourceManager.font, "          ",
				mVertexBufferObjectManager);
		scoreText.setScale(.75f);
		gameHUD.attachChild(scoreText);

		highScoreText = new Text(0, 0, mResourceManager.font,
				"HighScore:     ", mVertexBufferObjectManager);
		gameHUD.attachChild(highScoreText);
		highScoreText.setVisible(false);

		gameOverText = new Text(0, 0, mResourceManager.font, "GAME OVER",
				mVertexBufferObjectManager);
		gameHUD.attachChild(gameOverText);
		gameOverText.setVisible(false);

		restartSprite = new Sprite(mActivity.CAMERA_WIDTH / 2 - 12, 500,
				mResourceManager.mRestartTextureRegion,
				mVertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				mSceneManager.setScene(SceneType.SCENE_MENU);
				
				// sound option
				mActivity.getEngine().getMusicManager().setMasterVolume(10);
				mActivity.getEngine().getSoundManager().setMasterVolume(10);
				//
				scoreBoardSprite.setVisible(false);
				gameOverText.setVisible(false);
				scoreText.setVisible(false);
				highScoreText.setVisible(false);
				restartSprite.setVisible(false);
				
				return true;
			}
		};
		restartSprite.setVisible(false);
		gameHUD.registerTouchArea(restartSprite);
		gameHUD.attachChild(restartSprite);

		mActivity.smoothCamera.setHUD(gameHUD);
	}

	private void createRectangle() {

		wallFixtureDef = PhysicsFactory.createFixtureDef(2f, 0f, 0f);

		rectBody = new Body[4][3];

		for (int i = 0; i < rect.length; i++) {

			for (int j = 0; j < 3; j++) {

				rect[i][j] = new Rectangle(randomTile[i][j], vertical_gap,
						widthRect, heightRect, mVertexBufferObjectManager);

				rect[i][j].setColor(128, 128, 0);
				
				attachChild(rect[i][j]);

				rectBody[i][j] = PhysicsFactory.createBoxBody(mPhysicsWorld,
						rect[i][j], BodyType.StaticBody, wallFixtureDef);
				rectBody[i][j].setUserData("rectangle");
				mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
						rect[i][j], rectBody[i][j], true, true));

			}

			vertical_gap -= 300;
		}

	}

	private void createSmallRectangle2() {

		wallFixtureDef = PhysicsFactory.createFixtureDef(0f, 0f, 0f);
		rand = new Random();

		smallRectBody2 = new Body[4];
		int rY = (int) (rect[0][0].getY() - 150);
		for (int i = 0; i < smallRect2.length; i++) {

			int rX = rand.nextInt((int) (GameActivity.CAMERA_WIDTH - 40)) + 20;
			int rS = rand.nextInt(20) + 20;
			//rs = 40
			smallRect2[i] = new Rectangle(rX, rY, 30, 30,
					mVertexBufferObjectManager);
			smallRect2[i].setColor(139, 69, 19);
			attachChild(smallRect2[i]);
			rY -= 300;
			smallRectBody2[i] = PhysicsFactory.createBoxBody(mPhysicsWorld,
					smallRect2[i], BodyType.StaticBody, wallFixtureDef);
			smallRectBody2[i].setUserData("rectangle");
			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
					smallRect2[i], smallRectBody2[i], true, true));
		}

	}

	private IUpdateHandler updateScene() {

		IUpdateHandler up = new IUpdateHandler() {

			@Override
			public void reset() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUpdate(float pSecondsElapsed) {
				// TODO Auto-generated method stub
				// gameOver if falling
				if (tile.getY() > mActivity.CAMERA_HEIGHT) {
					gameOver();
				}
				// score
				if (tile.getY() < lastScoreUpdate - 300) {
					score++;
					scoreText.setText("   " + score);
					mActivity.setHighScore(score);
					lastScoreUpdate = (int) tile.getY();
				}
				//
				if (rectBody != null) {
					rectpostomove = (int) rect[tempi][0].getY();
					// claculate highest getY

					if (tile.getY() < moveRectafter) {

						// scoreText.setText("Score:"+ score++);

						for (int i = 0; i < rect.length; i++) {

							if (rect[i][0].getY() > rectpostomove) {
								rectpostomove = (int) rect[i][0].getY();
								tempi = i;
							}

						}

						for (int i = 0; i < 3; i++) {
							Vector2 v = Vector2Pool
									.obtain((rect[tempi][i].getX() + rect[tempi][i]
											.getWidth() / 2) / 32,
											(vertical_gap + rect[tempi][i]
													.getHeight() / 2) / 32);

							rectBody[tempi][i].setTransform(v,
									rectBody[tempi][i].getAngle());

							smallRectBody2[tempi].setTransform(
									smallRect2[tempi].getX() / 32,
									(smallRect2[tempi].getY() - 1200) / 32, 0);

							Vector2Pool.recycle(v);

						}
						vertical_gap -= 300;
						moveRectafter -= 300;

					}
				}
			}
		};
		return up;
	}

	private void jumpRight() {
		// TODO Auto-generated method stub
		tileBody.setLinearVelocity(5, -15);
	}

	private void jumpLeft() {
		// TODO Auto-generated method stub

		tileBody.setLinearVelocity(-5, -15);
	}

	private ContactListener createContactListener() {
		ContactListener contactListener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData().equals("tile")
						&& x2.getBody().getUserData().equals("rectangle")) {
					// tileBody.setType(BodyType.StaticBody);
					mResourceManager.mCollisionSound.play();
					gameOver();
									
				}
			}

			@Override
			public void endContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData().equals("tile")
						&& x2.getBody().getUserData().equals("rectangle")) {
					// tileBody.setType(BodyType.StaticBody);

				}
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		};
		return contactListener;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		clearEntityModifiers();
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
			index = MUSIC_JUMP;
			// sound();
			mResourceManager.mJumpSound.play();
			tileBody.setType(BodyType.DynamicBody);
			if (pSceneTouchEvent.getX() < tile.getX()) {

				jumpLeft();
			}
			if (pSceneTouchEvent.getX() > tile.getX()) {

				jumpRight();
			}
		}

		clearEntityModifiers();
		return true;
	}

	public void soundOption() {

	}

	//to be called on collision for gameover
	public void gameOver() {
		mActivity.smoothCamera.setChaseEntity(null);
		setOnSceneTouchListener(null);
		
		scoreBoardSprite.setPosition(mActivity.CAMERA_WIDTH/2 - scoreBoardSprite.getWidth()/2, 250);
		scoreBoardSprite.setVisible(true);
		
		scoreText.setScale(.8f);
		scoreText.setText("" + score);
		scoreText.setPosition(mActivity.CAMERA_WIDTH / 2 - scoreText.getWidth()
				/ 2, 400);
		highScoreText.setVisible(true);
		highScoreText.setScale(0.8f);
		highScoreText.setText("HighScore: " + mActivity.getHighScore());
		highScoreText.setPosition(
				mActivity.CAMERA_WIDTH / 2 - highScoreText.getWidth() / 2, 340);
		gameOverText.setVisible(true);
		gameOverText.setScale(0.8f);
		gameOverText.setPosition(
				mActivity.CAMERA_WIDTH / 2 - gameOverText.getWidth() / 2, 280);
		restartSprite.setVisible(true);
	}

}
