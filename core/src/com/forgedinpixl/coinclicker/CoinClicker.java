package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class CoinClicker extends Game {

	public SpriteBatch batch;
	public BitmapFont titleFont;
	public BitmapFont bodyFont;
	public BitmapFont statsFont;
	public StatsTracker statsTracker;
	public CoinController coinController;

	public CoinRegistry coinRegistry;
	public CoinInventory coinInventory;
	public CoinUnlockManager coinUnlockManager;

	public RollManager rollManager;

	public Preferences prefs;
	public AssetStore assetStore;

	public OrthographicCamera camera;
	public Viewport viewport;

	@Override
	public void create () {
		batch = new SpriteBatch();

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pixel-font.ttf"));
		FreeTypeFontParameter bodyParam = new FreeTypeFontParameter();
		bodyParam.size = 120;
		FreeTypeFontParameter titleParam = new FreeTypeFontParameter();
		titleParam.size = 180;
		FreeTypeFontParameter statsParam = new FreeTypeFontParameter();
		statsParam.size = 80;
		bodyFont = generator.generateFont(bodyParam);
		titleFont = generator.generateFont(titleParam);
		statsFont = generator.generateFont(statsParam);
		generator.dispose();

		// data layer first
		coinRegistry = new CoinRegistry();
		statsTracker = new StatsTracker();
		coinInventory = new CoinInventory(coinRegistry);
		coinUnlockManager = new CoinUnlockManager(coinInventory, coinRegistry, statsTracker);

		// prefs loaded after all objects exist
		prefs = Gdx.app.getPreferences("coinclicker_stats");
		statsTracker.loadFromPrefs(prefs);
		coinInventory.loadFromPrefs(prefs);
		coinUnlockManager.loadFromPrefs(prefs);
		rollManager = new RollManager(coinInventory, coinRegistry, statsTracker);

		// assets loaded after registry exists
		assetStore = new AssetStore();
		assetStore.loadCoins(coinRegistry);

		coinController = new CoinController(statsTracker);
		coinController.setAnimatedMode(prefs.getBoolean("animatedMode", false));

		camera = new OrthographicCamera();
		viewport = new FitViewport(1080, 1920, camera);
		viewport.apply();
		camera.position.set(540, 960, 0);
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		setScreen(new MainScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void pause() {
		statsTracker.saveToPrefs(prefs);
		coinInventory.saveToPrefs(prefs);
		coinUnlockManager.saveToPrefs(prefs);
		prefs.putBoolean("animatedMode", coinController.isAnimatedMode());
		prefs.flush();
	}
	@Override
	public void dispose () {
		statsTracker.saveToPrefs(prefs);
		coinInventory.saveToPrefs(prefs);
		coinUnlockManager.saveToPrefs(prefs);
		prefs.putBoolean("animatedMode", coinController.isAnimatedMode());
		prefs.flush();
		assetStore.dispose();
		batch.dispose();
		titleFont.dispose();
		bodyFont.dispose();
		statsFont.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}
}
