package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class BaseScreen implements Screen {

    protected CoinClicker game;
    protected SpriteBatch batch;
    protected BitmapFont titleFont;
    protected BitmapFont bodyFont;
    protected BitmapFont statsFont;
    protected StatsTracker statsTracker;
    protected CoinController coinController;
    protected Stage stage;

    public BaseScreen(CoinClicker game){
        this.game = game;
        this.batch = game.batch;
        this.titleFont = game.titleFont;
        this.bodyFont = game.bodyFont;
        this.statsFont = game.statsFont;
        this.statsTracker = game.statsTracker;
        this.coinController = game.coinController;
        this.stage = new Stage(new FitViewport(1080, 1920), game.batch);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}