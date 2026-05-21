package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.List;

public class CollectionScreen extends BaseScreen implements GestureDetector.GestureListener {

    private float scrollY = 0f;
    private float maxScroll = 0f;
    private GestureDetector gestureDetector;

    private static final float COIN_SIZE = 220f;
    private static final float PADDING = 70f;
    private static final float COLS = 3f;

    private List<CoinDefinition> allCoins;

    public CollectionScreen(CoinClicker game) {
        super(game);
        gestureDetector = new GestureDetector(this);
        Gdx.input.setInputProcessor(gestureDetector);
        allCoins = game.coinRegistry.getAll();

        // calculate max scroll based on content height
        float rows = (float) Math.ceil(allCoins.size() / COLS);
        float contentHeight = rows * (COIN_SIZE + PADDING) + PADDING + 200f; // 200f for header
        float screenHeight = game.viewport.getWorldHeight();
        maxScroll = Math.max(0, contentHeight - screenHeight);
    }

    @Override
    public void render(float delta) {
        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();

        float margin = 100f;
        float headerY = screenHeight * 0.95f;
        float backY = screenHeight * 0.05f;
        float statsY = screenHeight * 0.05f;

        String titleText = "Collection";
        String backText = "[ Back ]";
        String statsText = "[ Stats ]";

        GlyphLayout titleLayout = new GlyphLayout(titleFont, titleText);
        GlyphLayout backLayout = new GlyphLayout(bodyFont, backText);
        GlyphLayout statsLayout = new GlyphLayout(bodyFont, statsText);

        ScreenUtils.clear(0, 0, 0, 1);

        handleInput(screenWidth, screenHeight, backY, statsY,
                backLayout, statsLayout, margin);

        batch.begin();

        // header
        titleFont.draw(batch, titleText,
                screenWidth / 2f - titleLayout.width / 2f, headerY);

        // coin grid
        float slotSize = COIN_SIZE + PADDING;
        float gridWidth = COLS * slotSize - PADDING;
        float gridStartX = screenWidth / 2f - gridWidth / 2f;
        float gridStartY = screenHeight * 0.75f + scrollY;

        for (int i = 0; i < allCoins.size(); i++) {
            CoinDefinition def = allCoins.get(i);
            int col = i % (int) COLS;
            int row = i / (int) COLS;

            float x = gridStartX + col * slotSize;
            float y = gridStartY - row * slotSize;

            // skip if offscreen
            if (y + COIN_SIZE < 0 || y > screenHeight + COIN_SIZE) continue;

            boolean owned = game.coinInventory.owns(def.id);
            boolean isActive = def.id.equals(game.coinInventory.getActiveCoinId());

            Texture texture = owned
                    ? game.assetStore.getHeads(def)
                    : game.assetStore.lockedCoin;

            // active coin indicator — draw a border effect by drawing slightly larger
            if (isActive) {
                batch.draw(game.assetStore.lockedCoin, x - 8f, y - 8f,
                        COIN_SIZE + 16f, COIN_SIZE + 16f);
            }

            batch.draw(texture, x, y, COIN_SIZE, COIN_SIZE);

            // coin name below slot
            String label = owned ? def.displayName : "???";
            GlyphLayout labelLayout = new GlyphLayout(statsFont, label);
            statsFont.draw(batch, label,
                    x + COIN_SIZE / 2f - labelLayout.width / 2f,
                    y - 20f);
        }

        // bottom buttons
        bodyFont.draw(batch, backText, margin, backY);
        bodyFont.draw(batch, statsText,
                screenWidth - margin - statsLayout.width, statsY);

        batch.end();
    }

    private void handleInput(float screenWidth, float screenHeight,
                             float backY, float statsY,
                             GlyphLayout backLayout, GlyphLayout statsLayout,
                             float margin) {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touchPos);
            float touchX = touchPos.x;
            float touchY = touchPos.y;
            float padding = 30f;

            // back button
            float backLeft = margin - padding;
            float backRight = margin + backLayout.width + padding;
            float backBottom = backY - backLayout.height - padding;
            float backTop = backY + padding;

            // stats button
            float statsX = screenWidth - margin - statsLayout.width;
            float statsLeft = statsX - padding;
            float statsRight = statsX + statsLayout.width + padding;
            float statsBottom = statsY - statsLayout.height - padding;
            float statsTop = statsY + padding;

            if (touchX >= backLeft && touchX <= backRight
                    && touchY >= backBottom && touchY <= backTop) {
                Gdx.input.setInputProcessor(null);
                game.setScreen(new MainScreen(game));
            }

            if (touchX >= statsLeft && touchX <= statsRight
                    && touchY >= statsBottom && touchY <= statsTop) {
                Gdx.input.setInputProcessor(null);
                game.setScreen(new StatsScreen(game));
            }

            // coin tap — check grid
            float slotSize = COIN_SIZE + PADDING;
            float gridWidth = COLS * slotSize - PADDING;
            float gridStartX = screenWidth / 2f - gridWidth / 2f;
            float gridStartY = screenHeight * 0.88f + scrollY;

            for (int i = 0; i < allCoins.size(); i++) {
                CoinDefinition def = allCoins.get(i);
                int col = i % (int) COLS;
                int row = i / (int) COLS;

                float x = gridStartX + col * slotSize;
                float y = gridStartY - row * slotSize;

                if (touchX >= x && touchX <= x + COIN_SIZE
                        && touchY >= y && touchY <= y + COIN_SIZE) {
                    if (game.coinInventory.owns(def.id)) {
                        game.coinInventory.setActiveCoin(def.id);
                        game.coinInventory.saveToPrefs(game.prefs);
                    }
                    break;
                }
            }
        }
    }

    // GestureListener — fling and pan for scrolling
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        float worldDeltaY = deltaY * (game.viewport.getWorldHeight() / Gdx.graphics.getHeight());
        scrollY -= worldDeltaY;
        scrollY = Math.max(-maxScroll, Math.min(0, scrollY));
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override public boolean touchDown(float x, float y, int pointer, int button) { return false; }
    @Override public boolean tap(float x, float y, int count, int button) { return false; }
    @Override public boolean longPress(float x, float y) { return false; }
    @Override public boolean zoom(float initialDistance, float distance) { return false; }
    @Override public boolean pinch(Vector2 i1, Vector2 i2, Vector2 p1, Vector2 p2) { return false; }
    @Override public void pinchStop() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
    @Override public boolean panStop(float x, float y, int pointer, int button) { return false; }
}