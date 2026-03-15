package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

public class StatsScreen extends BaseScreen {

    public StatsScreen(CoinClicker game){
        super(game);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float x = 100;
        float y = screenHeight-80;
        float lineSpacing = 50;

        handleInput();

        batch.begin();

        font.draw(batch, "Statistics", x, y);
        font.draw(batch, "Total Flips: " + statsTracker.getTotalFlips(), x, y-100);
        font.draw(batch, "Heads: "+ statsTracker.getHeadsCount(), x, y-100-lineSpacing);
        font.draw(batch, "Tails: "+ statsTracker.getTailsCount(), x, y-100 - lineSpacing*2);
        font.draw(batch, "Back to Main", x, 120);

        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            float backX = 100;
            float backY = 120;

            float left = backX - 40;
            float right = backX + 80;
            float bottom = backY - 30;
            float top = backY +20;

            if(touchX >= left && touchX <= right && touchY >= bottom && touchY <= top){
                game.setScreen(new MainScreen(game));
            }
        }
    }
}
