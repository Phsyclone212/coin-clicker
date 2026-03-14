package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CoinClicker extends Game {

	SpriteBatch batch;
	BitmapFont font;
	StatsTracker statsTracker;
	AssetStore assetStore;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		statsTracker = new StatsTracker();

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
