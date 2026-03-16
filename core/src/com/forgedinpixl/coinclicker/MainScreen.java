package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainScreen extends BaseScreen {

    public MainScreen(CoinClicker game){
        super(game);
    }

    @Override
    public void render(float delta){

        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();

        //anchors
        float titleY = screenHeight * 0.85f;
        float coinY = screenHeight * 0.55f;
        float subtextY = screenHeight * 0.42f;
        float statsY = screenHeight * 0.15f;

        String titleText = "Coin Clicker";
        String coinText = "( COIN )";
        String resultText = coinController.getCurrentResultText();
        String statsText = "Stats";

        GlyphLayout titleLayout = new GlyphLayout(font, titleText);
        GlyphLayout coinLayout = new GlyphLayout(font, coinText);
        GlyphLayout resultLayout = new GlyphLayout(font, resultText);
        GlyphLayout statsLayout = new GlyphLayout(font, statsText);

        ScreenUtils.clear(0, 0, 0, 1);

        handleInput(screenWidth, screenHeight, coinY, statsY);

        coinController.update(delta);

        batch.begin();

        font.draw(batch, titleText, screenWidth / 2f - titleLayout.width / 2f, titleY);
        //draw coin
        font.draw(batch, coinText, screenWidth / 2f - coinLayout.width / 2f, coinY);
        //draw subtext
        font.draw(batch, resultText, screenWidth / 2f - resultLayout.width / 2f, subtextY); // getResult default text is "Tap coin to flip" so it starting this way works.
        font.draw(batch, statsText, screenWidth / 2f - statsLayout.width / 2f, statsY);

        batch.end();
    }

    private void handleInput(float screenWidth, float screenHeight, float coinY, float statsY) {
        if (Gdx.input.justTouched()) {

            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touchPos);
            // Where did we just get tapped?
            float touchX = touchPos.x;
            float touchY = touchPos.y;

            float paddingX = 30f;
            float paddingY = 30f;

            String coinText = "( COIN )";
            GlyphLayout coinLayout = new GlyphLayout(font, coinText);

            // determine coin location
            float coinX = screenWidth/2f - coinLayout.width / 2f;

            // hitbox for coin
            float coinLeft = coinX - paddingX;
            float coinRight = coinX + coinLayout.width + paddingX;
            float coinBottom = coinY - coinLayout.height - paddingY;
            float coinTop = coinY + paddingY;

            // stats button
            String statsText = "Stats";
            GlyphLayout statsLayout = new GlyphLayout(font, statsText);

            float statsX = screenWidth / 2f - statsLayout.width / 2f;

            // Creating hitbox for stats button
            float left = statsX - paddingX;
            float right = statsX + statsLayout.width + paddingX;
            float bottom = statsY - statsLayout.height - paddingY;
            float top = statsY + paddingY;

            // did the coin get tapped?
            if(touchX >= coinLeft && touchX <= coinRight && touchY >= coinBottom && touchY <= coinTop){
                //flip a coin
                coinController.requestFlip();
            }

            // did stats get tapped?
            if(touchX >= left && touchX <= right && touchY >= bottom && touchY <= top){
                game.setScreen(new StatsScreen(game));
            }
        }
    }
}
