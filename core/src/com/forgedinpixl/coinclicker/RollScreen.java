package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.List;

public class RollScreen extends BaseScreen {

    private final List<CoinDefinition> results;
    private int revealedCount = 0;
    private float revealTimer = 0f;
    private static final float REVEAL_INTERVAL = 0.6f;
    private boolean autoRevealing = true;
    private boolean allRevealed = false;

    private static final float COIN_SIZE = 200f;
    private static final float PADDING_X = 40f;
    private static final float PADDING_Y = 80f;
    private static final int COLS = 2;

    public RollScreen(CoinClicker game, List<CoinDefinition> results) {
        super(game);
        this.results = results;
    }

    @Override
    public void render(float delta) {
        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();
        float margin = 100f;

        // auto reveal timer
        if (autoRevealing && !allRevealed) {
            revealTimer += delta;
            if (revealTimer >= REVEAL_INTERVAL) {
                revealTimer = 0f;
                revealedCount++;
                if (revealedCount >= results.size()) {
                    revealedCount = results.size();
                    allRevealed = true;
                    autoRevealing = false;
                }
            }
        }

        float titleY    = screenHeight * 0.95f;
        float gridStartY = screenHeight * 0.85f;
        float backY     = screenHeight * 0.05f;

        String titleText = allRevealed ? "Roll Complete!" : "Rolling...";
        String backText  = allRevealed ? "[ Back to Shop ]" : "[ Skip ]";

        GlyphLayout titleLayout = new GlyphLayout(titleFont, titleText);
        GlyphLayout backLayout  = new GlyphLayout(bodyFont, backText);

        ScreenUtils.clear(0, 0, 0, 1);

        handleInput(screenWidth, screenHeight, backY, backLayout, margin);

        batch.begin();

        titleFont.draw(batch, titleText,
                screenWidth / 2f - titleLayout.width / 2f, titleY);

        // coin grid
        float slotW = COIN_SIZE + PADDING_X;
        float slotH = COIN_SIZE + PADDING_Y;
        float gridWidth  = COLS * slotW - PADDING_X;
        float gridStartX = screenWidth / 2f - gridWidth / 2f;

        for (int i = 0; i < results.size(); i++) {
            int col = i % COLS;
            int row = i / COLS;

            float x = gridStartX + col * slotW;
            float y = gridStartY - row * slotH;

            if (i < revealedCount) {
                CoinDefinition def = results.get(i);
                Texture texture = game.assetStore.getHeads(def);
                batch.draw(texture, x, y - COIN_SIZE, COIN_SIZE, COIN_SIZE);

                String rarityText = def.rarity.name();
                GlyphLayout rarityLayout = new GlyphLayout(statsFont, rarityText);
                statsFont.draw(batch, rarityText,
                        x + COIN_SIZE / 2f - rarityLayout.width / 2f,
                        y - COIN_SIZE - 24f);
            } else {
                batch.draw(game.assetStore.lockedCoin,
                        x, y - COIN_SIZE, COIN_SIZE, COIN_SIZE);
            }
        }

        bodyFont.draw(batch, backText, margin, backY);

        batch.end();
    }

    private void handleInput(float screenWidth, float screenHeight,
                             float backY, GlyphLayout backLayout, float margin) {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touchPos);
            float touchX = touchPos.x;
            float touchY = touchPos.y;
            float padding = 30f;

            float backLeft   = margin - padding;
            float backRight  = margin + backLayout.width + padding;
            float backBottom = backY - backLayout.height - padding;
            float backTop    = backY + padding;

            if (touchX >= backLeft && touchX <= backRight
                    && touchY >= backBottom && touchY <= backTop) {
                if (!allRevealed) {
                    // skip — reveal all immediately
                    revealedCount = results.size();
                    allRevealed = true;
                    autoRevealing = false;
                } else {
                    // done — back to shop
                    game.setScreen(new ShopScreen(game));
                }
            }
        }
    }
}