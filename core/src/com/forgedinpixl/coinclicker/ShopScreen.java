package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.List;

public class ShopScreen extends BaseScreen {

    private String feedbackText = "";
    private float feedbackTimer = 0f;
    private static final float FEEDBACK_DURATION = 2f;

    public ShopScreen(CoinClicker game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();
        float margin = 100f;

        // feedback timer
        if (feedbackTimer > 0) {
            feedbackTimer -= delta;
            if (feedbackTimer <= 0) feedbackText = "";
        }

        // anchors
        float titleY         = screenHeight * 0.95f;
        float balanceY       = titleY - 140f;
        float sectionY       = balanceY - 120f;
        float commonY        = sectionY - 120f;
        float commonBuyY     = commonY - 100f;
        float rareY          = commonBuyY - 160f;
        float rareBuyY       = rareY - 100f;
        float donorSectionY  = rareBuyY - 180f;
        float donorDescY     = donorSectionY - 100f;
        float donorButtonY   = donorDescY - 100f;
        float feedbackY      = donorButtonY - 120f;
        float backY          = screenHeight * 0.05f;

        // strings
        String titleText      = "Shop";
        String balanceText    = "Points: " + statsTracker.getPoints();
        String sectionText    = "-- Coin Rolls --";
        String commonText     = "Common Roll  (10 coins)  |  50 Points";
        String commonBuyText  = game.rollManager.canAffordCommon()
                ? "[ Buy Common Roll ]"
                : "[ Need 50 Points ]";
        String rareText       = "Rare Roll  (10 coins)  |  150 Points";
        String rareBuyText    = game.rollManager.canAffordRare()
                ? "[ Buy Rare Roll ]"
                : "[ Need 150 Points ]";
        String donorSectionText = "-- Support Development --";
        String donorDescText  = game.coinUnlockManager.isDonor()
                ? "Thank you for your support!"
                : "Donate $1+ to unlock donor coins";
        String donorButtonText = game.coinUnlockManager.isDonor()
                ? "[ Donor - Thank You! ]"
                : "[ Donate $1+ ]";
        String backText       = "[ Back ]";

        // layouts
        GlyphLayout titleLayout       = new GlyphLayout(titleFont, titleText);
        GlyphLayout balanceLayout     = new GlyphLayout(statsFont, balanceText);
        GlyphLayout sectionLayout     = new GlyphLayout(statsFont, sectionText);
        GlyphLayout commonLayout      = new GlyphLayout(statsFont, commonText);
        GlyphLayout commonBuyLayout   = new GlyphLayout(bodyFont, commonBuyText);
        GlyphLayout rareLayout        = new GlyphLayout(statsFont, rareText);
        GlyphLayout rareBuyLayout     = new GlyphLayout(bodyFont, rareBuyText);
        GlyphLayout donorSectionLayout = new GlyphLayout(statsFont, donorSectionText);
        GlyphLayout donorDescLayout   = new GlyphLayout(statsFont, donorDescText);
        GlyphLayout donorButtonLayout = new GlyphLayout(bodyFont, donorButtonText);
        GlyphLayout backLayout        = new GlyphLayout(bodyFont, backText);
        GlyphLayout feedbackLayout    = feedbackText.isEmpty()
                ? null
                : new GlyphLayout(statsFont, feedbackText);

        ScreenUtils.clear(0.176f, 0.102f, 0.102f, 1f);

        handleInput(screenWidth, screenHeight, backY, commonBuyY, rareBuyY,
                donorButtonY, commonBuyLayout, rareBuyLayout,
                donorButtonLayout, backLayout, margin);

        batch.begin();

        titleFont.draw(batch, titleText,
                screenWidth / 2f - titleLayout.width / 2f, titleY);
        statsFont.draw(batch, balanceText,
                screenWidth / 2f - balanceLayout.width / 2f, balanceY);
        statsFont.draw(batch, sectionText,
                screenWidth / 2f - sectionLayout.width / 2f, sectionY);

        // common roll
        statsFont.draw(batch, commonText,
                screenWidth / 2f - commonLayout.width / 2f, commonY);
        bodyFont.draw(batch, commonBuyText,
                screenWidth / 2f - commonBuyLayout.width / 2f, commonBuyY);

        // rare roll
        statsFont.draw(batch, rareText,
                screenWidth / 2f - rareLayout.width / 2f, rareY);
        bodyFont.draw(batch, rareBuyText,
                screenWidth / 2f - rareBuyLayout.width / 2f, rareBuyY);

        // donor section
        statsFont.draw(batch, donorSectionText,
                screenWidth / 2f - donorSectionLayout.width / 2f, donorSectionY);
        statsFont.draw(batch, donorDescText,
                screenWidth / 2f - donorDescLayout.width / 2f, donorDescY);
        bodyFont.draw(batch, donorButtonText,
                screenWidth / 2f - donorButtonLayout.width / 2f, donorButtonY);

        // feedback
        if (feedbackLayout != null) {
            statsFont.draw(batch, feedbackText,
                    screenWidth / 2f - feedbackLayout.width / 2f, feedbackY);
        }

        // back
        bodyFont.draw(batch, backText, margin, backY);

        batch.end();
    }

    private void handleInput(float screenWidth, float screenHeight,
                             float backY, float commonBuyY, float rareBuyY,
                             float donorButtonY, GlyphLayout commonBuyLayout,
                             GlyphLayout rareBuyLayout, GlyphLayout donorButtonLayout,
                             GlyphLayout backLayout, float margin) {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touchPos);
            float touchX = touchPos.x;
            float touchY = touchPos.y;
            float padding = 30f;

            // back button
            float backLeft   = margin - padding;
            float backRight  = margin + backLayout.width + padding;
            float backBottom = backY - backLayout.height - padding;
            float backTop    = backY + padding;

            // common buy button
            float commonX      = screenWidth / 2f - commonBuyLayout.width / 2f;
            float commonLeft   = commonX - padding;
            float commonRight  = commonX + commonBuyLayout.width + padding;
            float commonBottom = commonBuyY - commonBuyLayout.height - padding;
            float commonTop    = commonBuyY + padding;

            // rare buy button
            float rareX      = screenWidth / 2f - rareBuyLayout.width / 2f;
            float rareLeft   = rareX - padding;
            float rareRight  = rareX + rareBuyLayout.width + padding;
            float rareBottom = rareBuyY - rareBuyLayout.height - padding;
            float rareTop    = rareBuyY + padding;

            // donor button
            float donorX      = screenWidth / 2f - donorButtonLayout.width / 2f;
            float donorLeft   = donorX - padding;
            float donorRight  = donorX + donorButtonLayout.width + padding;
            float donorBottom = donorButtonY - donorButtonLayout.height - padding;
            float donorTop    = donorButtonY + padding;

            if (touchX >= backLeft && touchX <= backRight
                    && touchY >= backBottom && touchY <= backTop) {
                game.setScreen(new MainScreen(game));
            }

            if (touchX >= commonLeft && touchX <= commonRight
                    && touchY >= commonBottom && touchY <= commonTop) {
                if (game.rollManager.canAffordCommon()) {
                    List<CoinDefinition> results = game.rollManager.performCommonRoll();
                    if (results != null) {
                        game.coinInventory.saveToPrefs(game.prefs);
                        game.setScreen(new RollScreen(game, results));
                    }
                } else {
                    feedbackText = "Not enough Points!";
                    feedbackTimer = FEEDBACK_DURATION;
                }
            }

            if (touchX >= rareLeft && touchX <= rareRight
                    && touchY >= rareBottom && touchY <= rareTop) {
                if (game.rollManager.canAffordRare()) {
                    List<CoinDefinition> results = game.rollManager.performRareRoll();
                    if (results != null) {
                        game.coinInventory.saveToPrefs(game.prefs);
                        game.setScreen(new RollScreen(game, results));
                    }
                } else {
                    feedbackText = "Not enough Points!";
                    feedbackTimer = FEEDBACK_DURATION;
                }
            }

            if (touchX >= donorLeft && touchX <= donorRight
                    && touchY >= donorBottom && touchY <= donorTop) {
                if (!game.coinUnlockManager.isDonor()) {
                    // placeholder — swap for real Play Billing later
                    game.coinUnlockManager.processDonation(true);
                    game.coinUnlockManager.saveToPrefs(game.prefs);
                    game.coinInventory.saveToPrefs(game.prefs);
                    feedbackText = "Thank you for your support!";
                    feedbackTimer = FEEDBACK_DURATION;
                }
            }
        }
    }
}