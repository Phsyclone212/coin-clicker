package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseScreen implements Screen {

    protected CoinClicker game;
    protected SpriteBatch batch;
    protected BitmapFont font;
    protected StatsTracker statsTracker;
    protected CoinController coinController;

    public BaseScreen(CoinClicker game){
        this.game = game;
        this.batch = game.batch;
        this.font = game.font;
        this.statsTracker = game.statsTracker;
        this.coinController = game.coinController;
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
