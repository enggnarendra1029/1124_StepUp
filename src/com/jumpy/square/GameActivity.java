package com.jumpy.square;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.jumpy.square.SceneManager.SceneType;

public class GameActivity extends SimpleBaseGameActivity {

	public static int CAMERA_WIDTH = 480;
	public static int CAMERA_HEIGHT = 800;

	public SmoothCamera smoothCamera;
	private ResourceManager mResourceManager;
	private SceneManager mSceneManager;
	Scene scene;
	// ads
	public AdView adView;
	public static String fulladid = "ca-app-pub-1241088682012841/8668109010";
	public static String adId = "ca-app-pub-1241088682012841/5714642614";
	private InterstitialAd interstitialAd;

	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		smoothCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 0,
				500, 1.0f);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(),
				smoothCamera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub
		mResourceManager = ResourceManager.getInstance();
		mResourceManager.prepare(this);
		mResourceManager.loadSplashResources();
		
		mSceneManager = SceneManager.getInstance();
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub

		mEngine.registerUpdateHandler(new TimerHandler(2f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						mResourceManager.loadGameResources();
						mSceneManager.setScene(SceneType.SCENE_MENU);
						mResourceManager.unloadSplashResources();
					}
				}));

		return mSceneManager.createSplashScene();
		

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.exit(0);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mSceneManager.getCurrentScene() != null) {
			mSceneManager.getCurrentScene().onBackKeyPressed();
			return;
		}
		super.onBackPressed();
	}

	/*
	 * @Override protected void onPause() { // TODO Auto-generated method stub
	 * if (mResourceManager.mMusic!=null && mResourceManager.mMusic.isPlaying())
	 * { mResourceManager.mMusic.pause(); } super.onPause(); }
	 */
	// Score
	SharedPreferences sharedPreferences;

	public void setHighScore(int score) {
		sharedPreferences = getSharedPreferences("MyGame",
				Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor settingsEditor = sharedPreferences.edit();
		if (score > getHighScore()) {
			settingsEditor.putInt("score", score);
			settingsEditor.commit();
		}
	}

	public int getHighScore() {

		sharedPreferences = getSharedPreferences("MyGame",
				Context.MODE_WORLD_WRITEABLE);
		return sharedPreferences.getInt("score", 0);
	}

	// display ads
  
	  @Override 
	  protected void onSetContentView() {
		  // CREATING the parent FrameLayout // 
	  final FrameLayout frameLayout = new FrameLayout(this);
	  
	  // CREATING the layout parameters, fill the screen // final
	  FrameLayout.LayoutParams frameLayoutLayoutParams = new
	  FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
	  FrameLayout.LayoutParams.MATCH_PARENT);
	  
	  // CREATING a Smart Banner View // 
	  adView = new
	  AdView(GameActivity.this); adView.setAdSize(AdSize.BANNER);
	  adView.setAdUnitId(adId);
	  
	  // Doing something  //
	  adView.refreshDrawableState(); 
	  adView.setVisibility(AdView.VISIBLE);
	  
	  // ADVIEW layout, show at the bottom of the screen // 
	  FrameLayout.LayoutParams adViewLayoutParams = new
	  FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
	  FrameLayout.LayoutParams.WRAP_CONTENT,
	  Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
	  
	  // REQUEST an ad (Test ad) // 
	  
	  
	  // RENDER the add on top of the scene // 
	  this.mRenderSurfaceView = new
	  RenderSurfaceView(this); mRenderSurfaceView.setRenderer(mEngine, this);
	    
	  final android.widget.RelativeLayout.LayoutParams surfaceViewLayoutParams = new RelativeLayout.LayoutParams(
			    BaseGameActivity.createSurfaceViewLayoutParams());
			  surfaceViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	  
	  // ADD the surface view and adView to the frame //
	  frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
	  frameLayout.addView(adView, adViewLayoutParams);
	  
	  //Interstitial ad 
	  loadInterstitialAds();
	  
	  // SHOW AD // 
	  this.setContentView(frameLayout, frameLayoutLayoutParams);
	  
	  }
	
	//
	public void showBannerAds() {
		  runOnUiThread(new Runnable() {
		   @Override
		   public void run() {
		    adView.setVisibility(View.VISIBLE);
		    AdRequest adRequest = new AdRequest.Builder().addTestDevice(
		    		  "4F0145887175BCA1D5DF8E6F3FC99552").build();
		    adView.loadAd(adRequest);
		   }
		  });
	 }
	
	
	public void loadInterstitialAds() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					// fullscreen ads
					interstitialAd = new InterstitialAd(GameActivity.this);
					interstitialAd.setAdUnitId(fulladid);
					AdRequest adRequestfull = new AdRequest.Builder().build();
					interstitialAd.loadAd(adRequestfull);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void adIntShow() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {

					interstitialAd.setAdListener(new AdListener() {
						@Override
						public void onAdLoaded() {
							if (interstitialAd.isLoaded()) {
								interstitialAd.show();
							}
						}

						@Override
						public void onAdFailedToLoad(int errorCode) {
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
