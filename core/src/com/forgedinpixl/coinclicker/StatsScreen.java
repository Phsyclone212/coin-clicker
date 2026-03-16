package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class StatsScreen extends BaseScreen {

    public StatsScreen(CoinClicker game){
        super(game);
    }

    @Override
    public void render(float delta) {

        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();

        float titleY = screenHeight*0.80f;
        float statsStartY = screenHeight*0.65f;
        float lineSpacing = screenHeight*0.06f;
        float backY = screenHeight * .15f;

        String titleText = "Statistics";
        String totalText = "Total Flips: " + statsTracker.getTotalFlips();
        String headsText = "Heads count: " + statsTracker.getHeadsCount();
        String tailsText = "Tails count: " + statsTracker.getTailsCount();
        String currentStreakText = "Current Streak: "+ statsTracker.getCurrentStreak()+" "+statsTracker.getSide();
        String longestStreakText = "Longest Streak: "+ statsTracker.getLongestStreak();
        String backText = "Back to Main";

        GlyphLayout titleLayout = new GlyphLayout(titleFont, titleText);
        GlyphLayout totalLayout = new GlyphLayout(statsFont, totalText);
        GlyphLayout headsLayout = new GlyphLayout(statsFont, headsText);
        GlyphLayout tailsLayout = new GlyphLayout(statsFont, tailsText);
        GlyphLayout currentStreakLayout = new GlyphLayout(statsFont, currentStreakText);
        GlyphLayout longestStreakLayout = new GlyphLayout(statsFont, longestStreakText);
        GlyphLayout backLayout = new GlyphLayout(bodyFont, backText);

        ScreenUtils.clear(0,0,0,1);

        handleInput(screenWidth, screenHeight, backY);

        batch.begin();

        titleFont.draw(batch, titleText, screenWidth / 2f - titleLayout.width / 2f, titleY);
        statsFont.draw(batch, totalText, screenWidth / 2f - totalLayout.width / 2f, statsStartY);
        statsFont.draw(batch, headsText, screenWidth / 2f - headsLayout.width / 2f, statsStartY-lineSpacing);
        statsFont.draw(batch, tailsText, screenWidth / 2f - tailsLayout.width / 2f, statsStartY - lineSpacing*2);
        statsFont.draw(batch, currentStreakText, screenWidth / 2f - currentStreakLayout.width / 2f, statsStartY - lineSpacing*3);
        statsFont.draw(batch, longestStreakText, screenWidth / 2f - longestStreakLayout.width / 2f, statsStartY - lineSpacing*4);
        bodyFont.draw(batch, backText, screenWidth / 2f - backLayout.width / 2f, backY);

        batch.end();
    }

    private void handleInput(float screenWidth, float screenHeight, float backY) {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touchPos);
            // Where did we just get tapped?
            float touchX = touchPos.x;
            float touchY = touchPos.y;

            float paddingX = 30f;
            float paddingY = 30f;

            String backText = "Back to Main";
            GlyphLayout backLayout = new GlyphLayout(bodyFont, backText);
            float backX = screenWidth / 2f - backLayout.width / 2f;

            float left = backX - paddingX;
            float right = backX + backLayout.width + paddingX;
            float bottom = backY - backLayout.height - paddingY;
            float top = backY + paddingY;

            if(touchX >= left && touchX <= right && touchY >= bottom && touchY <= top){
                game.setScreen(new MainScreen(game));
            }
        }
    }
}
