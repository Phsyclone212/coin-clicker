package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainScreen implements Screen {

    CoinClicker game;

    public MainScreen(CoinClicker game){
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta){

        ScreenUtils.clear(0, 0, 0, 1);

        game.batch.begin();

        game.font.draw(game.batch, "Coin Clicker", 100, 200);

        game.batch.end();
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
