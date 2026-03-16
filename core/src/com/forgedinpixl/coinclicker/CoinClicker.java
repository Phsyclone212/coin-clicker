package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CoinClicker extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	public StatsTracker statsTracker;
	public CoinController coinController;
	public AssetStore assetStore;

	public OrthographicCamera camera;
	public Viewport viewport;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.getData().setScale(3f);
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
		font.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}
}
