package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CoinClicker extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	public StatsTracker statsTracker;
	public CoinController coinController;
	public AssetStore assetStore;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.getData().setScale(4f);
		statsTracker = new StatsTracker();
		coinController = new CoinController(statsTracker);

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
}
