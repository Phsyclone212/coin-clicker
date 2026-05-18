package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

        float coinSize = 400f;

        //anchors
        float titleY = screenHeight * 0.88f;
        float coinY = screenHeight * 0.70f;         // top edge of coin
        float subtextY = coinY - coinSize - 40f;    // just below coin
        float streakY = subtextY - 80f;             // below subtext
        float statsY = screenHeight * 0.10f;

        String titleText = "Coin Clicker";
        String resultText = coinController.getCurrentResultText();
        String currentStreakText = "Current Streak: " + statsTracker.getCurrentStreak();
        String statsText = "Stats";

        GlyphLayout titleLayout = new GlyphLayout(titleFont, titleText);
        GlyphLayout resultLayout = new GlyphLayout(bodyFont, resultText);
        GlyphLayout currentStreakLayout = new GlyphLayout(statsFont, currentStreakText);
        GlyphLayout statsLayout = new GlyphLayout(bodyFont, statsText);

        ScreenUtils.clear(0, 0, 0, 1);

        handleInput(screenWidth, screenHeight, coinY, coinSize, statsY);

        coinController.update(delta);

        batch.begin();

        titleFont.draw(batch, titleText, screenWidth / 2f - titleLayout.width / 2f, titleY);

        Texture coinTexture = coinController.wasLastFlipHeads()
                ? game.assetStore.coinHeads
                : game.assetStore.coinTails;

        float coinX = screenWidth / 2f - coinSize / 2f;
        batch.draw(coinTexture, coinX, coinY - coinSize, coinSize, coinSize);

        bodyFont.draw(batch, resultText, screenWidth / 2f - resultLayout.width / 2f, subtextY);
        statsFont.draw(batch, currentStreakText, screenWidth / 2f - currentStreakLayout.width / 2f, streakY);
        bodyFont.draw(batch, statsText, screenWidth / 2f - statsLayout.width / 2f, statsY);

        batch.end();
    }

    private void handleInput(float screenWidth, float screenHeight, float coinY, float coinSize, float statsY) {
        if (Gdx.input.justTouched()) {

            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touchPos);
            float touchX = touchPos.x;
            float touchY = touchPos.y;

            float coinX = screenWidth / 2f - coinSize / 2f;

            // hitbox derived from actual coin position and size
            float coinLeft   = coinX;
            float coinRight  = coinX + coinSize;
            float coinBottom = coinY - coinSize;
            float coinTop    = coinY;

            // stats button
            String statsText = "Stats";
            GlyphLayout statsLayout = new GlyphLayout(bodyFont, statsText);
            float statsX = screenWidth / 2f - statsLayout.width / 2f;
            float padding = 30f;

            float left   = statsX - padding;
            float right  = statsX + statsLayout.width + padding;
            float bottom = statsY - statsLayout.height - padding;
            float top    = statsY + padding;

            if(touchX >= coinLeft && touchX <= coinRight && touchY >= coinBottom && touchY <= coinTop){
                coinController.requestFlip();
            }

            if(touchX >= left && touchX <= right && touchY >= bottom && touchY <= top){
                game.setScreen(new StatsScreen(game));
            }
        }
    }
}