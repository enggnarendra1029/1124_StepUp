package com.jumpy.square;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

public class ResourceManager {

	private BitmapTextureAtlas mSplashTextureAtlas;
	public ITextureRegion mSplashTextureRegion;
	public Font font;
	//
	private static final ResourceManager INSTANCE = new ResourceManager();

	public GameActivity mActivity;
	Music mMusic;
	Sound mSound;
	Sound mJumpSound;
	Sound mCollisionSound;

	private BitmapTextureAtlas mGameSoundTextureAtlas;
	public TiledTextureRegion mGameSoundTextureRegion;
	private BitmapTextureAtlas mRestartTextureAtlas;
	public TextureRegion mRestartTextureRegion;
	private BitmapTextureAtlas mTileTextureAtlas;
	public TextureRegion mTileTextureRegion;
	public BitmapTextureAtlas gameTextureAtlas;
	public TextureRegion gameBGTextureRegion;
	public BitmapTextureAtlas scoreBoardTextureAtlas;
	public TextureRegion scoreBoardTextureRegion;

	private ResourceManager() {
	}

	public static ResourceManager getInstance() {
		return INSTANCE;
	}

	public void prepare(GameActivity activity) {
		INSTANCE.mActivity = activity;
	}

	public void loadSplashResources() {
		// TODO implement
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mSplashTextureAtlas = new BitmapTextureAtlas(
				mActivity.getTextureManager(), 512, 512,
				TextureOptions.BILINEAR);// 64x64
		mSplashTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mSplashTextureAtlas, mActivity, "loading.png",
						0, 0);// 447x51

		mSplashTextureAtlas.load();
		//
		FontFactory.setAssetBasePath("font/");
		ITexture fontTexture3 = new BitmapTextureAtlas(
				mActivity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR);
		font = FontFactory.createFromAsset(mActivity.getFontManager(),
				fontTexture3, mActivity.getAssets(), "OpenSans-Bold.ttf", 40,
				true, Color.WHITE);
		font.load();
	}

	public void unloadSplashResources() {
		// TODO implement
		mSplashTextureAtlas.unload();
		mSplashTextureRegion = null;
	}

	public void loadGameResources() {
		// TODO implement

		// sound
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mGameSoundTextureAtlas = new BitmapTextureAtlas(
				mActivity.getTextureManager(), 512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mGameSoundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mGameSoundTextureAtlas, mActivity,
						"sound.png", 0, 0, 2, 1);// 271x133

		mGameSoundTextureAtlas.load();

		// restart icon
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		mRestartTextureAtlas = new BitmapTextureAtlas(
				mActivity.getTextureManager(), 64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mRestartTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mRestartTextureAtlas, mActivity,
						"icon_restart.png", 0, 0);
		mRestartTextureAtlas.load();

		// background
		gameTextureAtlas = new BitmapTextureAtlas(
				mActivity.getTextureManager(), 1024, 1024);
		gameBGTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, this.mActivity,
						"background.png",0,0);
		gameTextureAtlas.load();
		
		// score_board
		scoreBoardTextureAtlas = new BitmapTextureAtlas(
						mActivity.getTextureManager(), 512, 512);
		scoreBoardTextureRegion = BitmapTextureAtlasTextureRegionFactory
						.createFromAsset(scoreBoardTextureAtlas, this.mActivity,
								"score_board.png",0,0);//343x340
		scoreBoardTextureAtlas.load();
		
		// tile
		mTileTextureAtlas = new BitmapTextureAtlas(
				mActivity.getTextureManager(), 64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTileTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mTileTextureAtlas, mActivity, "icon_tile.png",
						0, 0);// 24x24
		mTileTextureAtlas.load();
		
		// Font
	
		// Sound and Music
		SoundFactory.setAssetBasePath("sfx/");
		try {
			mSound = SoundFactory.createSoundFromAsset(mActivity.getEngine()
					.getSoundManager(), mActivity, "button_sound.mp3");
			mJumpSound = SoundFactory.createSoundFromAsset(mActivity
					.getEngine().getSoundManager(), mActivity, "jump.mp3");
			mCollisionSound = SoundFactory.createSoundFromAsset(mActivity
					.getEngine().getSoundManager(), mActivity, "collision.mp3");

		} catch (final IOException e) {
			Debug.e(e);
		}

		MusicFactory.setAssetBasePath("sfx/");
		try {
			mMusic = MusicFactory.createMusicFromAsset(mActivity.getEngine()
					.getMusicManager(), mActivity, "piano_sounds_6.mp3");
			mMusic.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}
	}

	public void unloadGameResources() {
		// TODO implement

		//sprite unload
		gameTextureAtlas.unload();
		gameBGTextureRegion = null;
		mTileTextureAtlas.unload();
		mTileTextureRegion = null;
		mGameSoundTextureAtlas.unload();
		mGameSoundTextureRegion = null;
		scoreBoardTextureAtlas.unload();
		scoreBoardTextureRegion = null;
		
		// Font
		font.unload();
		font = null;
		// Sound and Music
		mSound.release();
		mSound = null;

		mMusic.stop();
		mMusic.release();
		mMusic = null;
	}
}
