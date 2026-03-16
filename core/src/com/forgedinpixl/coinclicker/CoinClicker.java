package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
	public StatsTracker statsTracker;
	public CoinController coinController;
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
		bodyFont = generator.generateFont(bodyParam);
		titleFont = generator.generateFont(titleParam);
		generator.dispose();

		statsTracker = new StatsTracker();
		coinController = new CoinController(statsTracker);

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
	public void dispose () {
		batch.dispose();
		titleFont.dispose();
		bodyFont.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}
}
