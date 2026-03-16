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
        float titleY = screenHeight * 0.85f;
        float coinY = screenHeight * 0.55f;
        float subtextY = screenHeight * 0.42f;
        float statsY = screenHeight * 0.15f;

        ScreenUtils.clear(0, 0, 0, 1);

        handleInput();

        batch.begin();

        font.draw(batch, "Coin Clicker", screenWidth/2, titleY);

        //draw coin
        font.draw(batch, " ( COIN ) ", screenWidth/2, coinY);

        //draw subtext
        font.draw(batch, getResult(), screenWidth*0.45f, subtextY); // getResult default text is "Tap coin to flip" so it starting this way works.

        font.draw(batch, "Stats", screenWidth/2, statsY);

        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            // Where did we just get tapped?
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // determine coin location
            float coinX = Gdx.graphics.getWidth()/2f;
            float coinY = Gdx.graphics.getHeight()*0.55f;

            // hitbox for coin location (large, so using methods, should simplify variables)
            float coinLeft = coinX - Gdx.graphics.getWidth()*0.4f;
            float coinRight = coinX + Gdx.graphics.getWidth()*0.4f;
            float coinBottom = coinY - Gdx.graphics.getHeight()*0.2f;
            float coinTop = coinY + Gdx.graphics.getHeight()*0.2f;

            if(touchX >= coinLeft && touchX <= coinRight && touchY >= coinBottom && touchY <= coinTop){
                //flip a coin
                coinController.requestFlip();
            }

            // determining stats button location
            float statsX = Gdx.graphics.getWidth() /2f;
            float statsY = Gdx.graphics.getHeight() * 0.15f;

            // Creating hitbox for stats button
            float left = statsX - 100;
            float right = statsX + 100;
            float bottom = statsY - 40;
            float top = statsY + 40;

            // did stats get tapped?
            if(touchX >= left && touchX <= right && touchY >= bottom && touchY <= top){
                game.setScreen(new StatsScreen(game));
            }
        }
    }

    public String getResult(){
        return coinController.currentResultText;
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
