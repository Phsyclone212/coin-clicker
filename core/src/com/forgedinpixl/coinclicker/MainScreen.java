package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainScreen extends BaseScreen {

    public MainScreen(CoinClicker game){
        super(game);
    }

    public void show() {

    }

    @Override
    public void render(float delta){

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float x = 100;
        float y = screenHeight-80;
        float lineSpacing = 50;

        ScreenUtils.clear(0, 0, 0, 1);

        handleInput();

        batch.begin();

        font.draw(batch, "Coin Clicker", screenWidth/2, y);
        font.draw(batch, "Stats", screenWidth/2, 120);

        batch.end();
    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            float statsX = Gdx.graphics.getWidth() /2f;
            float statsY = 120;

            float left = statsX - 40;
            float right = statsX + 80;
            float bottom = statsY - 30;
            float top = statsY +20;

            if(touchX >= left && touchX <= right && touchY >= bottom && touchY <= top){
                game.setScreen(new StatsScreen(game));
            }
        }
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
