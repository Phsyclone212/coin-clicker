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

        float coinSize = 600f;
        float margin = 80f;

        // anchors
        float titleY        = screenHeight * 0.95f;
        float headerY       = titleY - 160f;        // Flip$ and Anim toggle row
        float coinY         = screenHeight * 0.68f;
        float subtextY      = coinY - coinSize - 80f;
        float streakY       = subtextY - 100f;
        float multiplierY = streakY - 100f;
        float bottomRowY    = screenHeight * 0.08f; // Shop and Stats row

        // strings
        String titleText        = "Coin Clicker";
        String flipDollarsText  = "Flip$: " + statsTracker.getFlipDollars();
        String animText         = coinController.isAnimatedMode() ? "[ Anim: ON ]" : "[ Anim: OFF ]";
        String resultText       = coinController.getCurrentResultText();
        String currentStreakText = "Current Streak: " + statsTracker.getCurrentStreak();
        String multiplierText = coinController.isAnimatedMode() ? "x2 Flip$ per flip" : "";
        String collectionText        = "[ Collection ]";
        String shopText         = "[ Shop ]";

        // layouts
        GlyphLayout titleLayout         = new GlyphLayout(titleFont, titleText);
        GlyphLayout flipDollarsLayout   = new GlyphLayout(statsFont, flipDollarsText);
        GlyphLayout animLayout          = new GlyphLayout(statsFont, animText);
        GlyphLayout resultLayout        = new GlyphLayout(bodyFont, resultText);
        GlyphLayout streakLayout        = new GlyphLayout(statsFont, currentStreakText);
        GlyphLayout multiplierLayout = new GlyphLayout(statsFont, multiplierText);
        GlyphLayout statsLayout         = new GlyphLayout(bodyFont, collectionText);
        GlyphLayout shopLayout          = new GlyphLayout(bodyFont, shopText);

        ScreenUtils.clear(0, 0, 0, 1);

        handleInput(screenWidth, screenHeight, coinY, coinSize, headerY, bottomRowY,
                animLayout, statsLayout, shopLayout, margin);

        coinController.update(delta);
        game.coinUnlockManager.checkMilestoneUnlocks();

        batch.begin();

        // title — centered
        titleFont.draw(batch, titleText,
                screenWidth / 2f - titleLayout.width / 2f, titleY);

        // Flip$ — top left
        statsFont.draw(batch, flipDollarsText, margin, headerY);

        // Anim toggle — top right
        statsFont.draw(batch, animText,
                screenWidth - margin - animLayout.width, headerY);

        // coin
        CoinDefinition activeCoin = game.coinInventory.getActiveCoin();
        Texture coinTexture = coinController.isShowingHeads()
                ? game.assetStore.getHeads(activeCoin)
                : game.assetStore.getTails(activeCoin);

        float coinX = screenWidth / 2f - coinSize / 2f;
        float xScale = coinController.getCurrentXScale();
        float scaledWidth = coinSize * xScale;
        float offsetX = (coinSize - scaledWidth) / 2f;

        batch.draw(coinTexture, coinX + offsetX, coinY - coinSize, scaledWidth, coinSize);

        // result and streak — centered
        bodyFont.draw(batch, resultText,
                screenWidth / 2f - resultLayout.width / 2f, subtextY);
        statsFont.draw(batch, currentStreakText,
                screenWidth / 2f - streakLayout.width / 2f, streakY);

        // bonus rate for animation toggled on
        statsFont.draw(batch, multiplierText,
                screenWidth / 2f - multiplierLayout.width / 2f, multiplierY);

        // Shop — bottom left (greyed out via naming, dead button)
        bodyFont.draw(batch, shopText, margin, bottomRowY);

        // Stats — bottom right
        bodyFont.draw(batch, collectionText,
                screenWidth - margin - statsLayout.width, bottomRowY);

        batch.end();
    }

    private void handleInput(float screenWidth, float screenHeight,
                             float coinY, float coinSize,
                             float headerY, float bottomRowY,
                             GlyphLayout animLayout, GlyphLayout statsLayout,
                             GlyphLayout shopLayout, float margin) {

        if (Gdx.input.justTouched()) {

            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touchPos);
            float touchX = touchPos.x;
            float touchY = touchPos.y;

            float padding = 30f;

            // coin hitbox
            float coinX      = screenWidth / 2f - coinSize / 2f;
            float coinLeft   = coinX;
            float coinRight  = coinX + coinSize;
            float coinBottom = coinY - coinSize;
            float coinTop    = coinY;

            // anim toggle hitbox — top right
            float animX      = screenWidth - margin - animLayout.width;
            float animLeft   = animX - padding;
            float animRight  = animX + animLayout.width + padding;
            float animBottom = headerY - animLayout.height - padding;
            float animTop    = headerY + padding;

            // stats hitbox — bottom right
            float collectionX      = screenWidth - margin - statsLayout.width;
            float collectionLeft   = collectionX - padding;
            float collectionRight  = collectionX + statsLayout.width + padding;
            float collectionBottom = bottomRowY - statsLayout.height - padding;
            float collectionTop    = bottomRowY + padding;

            // coin tap
            if (touchX >= coinLeft && touchX <= coinRight
                    && touchY >= coinBottom && touchY <= coinTop) {
                coinController.requestFlip();
            }

            // anim toggle tap
            if (touchX >= animLeft && touchX <= animRight
                    && touchY >= animBottom && touchY <= animTop) {
                coinController.setAnimatedMode(!coinController.isAnimatedMode());
            }

            // collection tap
            if (touchX >= collectionLeft && touchX <= collectionRight
                    && touchY >= collectionBottom && touchY <= collectionTop) {
                game.setScreen(new CollectionScreen(game));
            }

            // shop hitbox — bottom left
            float shopLeft   = margin - padding;
            float shopRight  = margin + shopLayout.width + padding;
            float shopBottom = bottomRowY - shopLayout.height - padding;
            float shopTop    = bottomRowY + padding;

            if (touchX >= shopLeft && touchX <= shopRight
                    && touchY >= shopBottom && touchY <= shopTop) {
                game.setScreen(new ShopScreen(game));
            }
        }
    }
}