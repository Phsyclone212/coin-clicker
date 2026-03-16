package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class StatsScreen extends BaseScreen {

    public StatsScreen(CoinClicker game){
        super(game);
    }

    @Override
    public void render(float delta) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float titleY = screenHeight*0.80f;
        float statsStartY = screenHeight*0.65f;
        float lineSpacing = screenHeight*0.10f;
        float backY = screenHeight * .15f;

        String titleText = "Statistics";
        String totalText = "Total Flips: " + statsTracker.getTotalFlips();
        String headsText = "Heads count: " + statsTracker.getHeadsCount();
        String tailsText = "Tails count: " + statsTracker.getTailsCount();
        String backText = "Back to Main";

        GlyphLayout titleLayout = new GlyphLayout(font, titleText);
        GlyphLayout totalLayout = new GlyphLayout(font, totalText);
        GlyphLayout headsLayout = new GlyphLayout(font, headsText);
        GlyphLayout tailsLayout = new GlyphLayout(font, tailsText);
        GlyphLayout backLayout = new GlyphLayout(font, backText);

        ScreenUtils.clear(0,0,0,1);

        handleInput(screenWidth, screenHeight, backY);

        batch.begin();

        font.draw(batch, titleText, screenWidth / 2f - titleLayout.width / 2f, titleY);
        font.draw(batch, totalText, screenWidth / 2f - totalLayout.width / 2f, statsStartY);
        font.draw(batch, headsText, screenWidth / 2f - headsLayout.width / 2f, statsStartY-lineSpacing);
        font.draw(batch, tailsText, screenWidth / 2f - tailsLayout.width / 2f, statsStartY - lineSpacing*2);
        font.draw(batch, backText, screenWidth / 2f - backLayout.width / 2f, backY);

        batch.end();
    }

    private void handleInput(float screenWidth, float screenHeight, float backY) {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = screenHeight - Gdx.input.getY();

            float paddingX = 30f;
            float paddingY = 30f;

            String backText = "Back to Main";
            GlyphLayout backLayout = new GlyphLayout(font, backText);
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
