package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseScreen implements Screen {

    protected CoinClicker game;
    protected SpriteBatch batch;
    protected BitmapFont font;
    protected StatsTracker statsTracker;

    public BaseScreen(CoinClicker game){
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.statsTracker = new StatsTracker();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
