package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollectionScreen extends BaseScreen implements GestureDetector.GestureListener {

    private float scrollY = 0f;
    private float maxScroll = 0f;
    private GestureDetector gestureDetector;
    private ShapeRenderer shapeRenderer;

    private static final float COIN_SIZE = 220f;
    private static final float PADDING = 85f;
    private static final float COLS = 2f;
    private static final float GRID_START_Y_FACTOR = 0.75f;

    private List<CoinDefinition> allCoins;

    public CollectionScreen(CoinClicker game) {
        super(game);
        gestureDetector = new GestureDetector(this);
        shapeRenderer = new ShapeRenderer();
        Gdx.input.setInputProcessor(gestureDetector);
        allCoins = game.coinRegistry.getAll();
        Collections.sort(allCoins, new Comparator<CoinDefinition>() {
            @Override
            public int compare(CoinDefinition a, CoinDefinition b) {
                int setCompare = a.set.compareTo(b.set);
                if (setCompare != 0) return setCompare;
                int rarityCompare = a.rarity.compareTo(b.rarity);
                if (rarityCompare != 0) return rarityCompare;
                return a.id.compareTo(b.id);
            }
        });

        float rows = (float) Math.ceil(allCoins.size() / COLS);
        float contentHeight = rows * (COIN_SIZE + PADDING) + PADDING + 200f;
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

        float slotSize = COIN_SIZE + PADDING;
        float gridWidth = COLS * slotSize - PADDING;
        float gridStartX = screenWidth / 2f - gridWidth / 2f;
        float gridStartY = screenHeight * GRID_START_Y_FACTOR + scrollY;

        // draw active indicator with ShapeRenderer before batch
        shapeRenderer.setProjectionMatrix(game.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(1f, 0.84f, 0f, 0.4f)); // gold tint

        for (int i = 0; i < allCoins.size(); i++) {
            CoinDefinition def = allCoins.get(i);
            boolean isActive = def.id.equals(game.coinInventory.getActiveCoinId());
            if (!isActive) continue;

            int col = i % (int) COLS;
            int row = i / (int) COLS;
            float x = gridStartX + col * slotSize;
            float y = gridStartY - row * slotSize;

            float border = 12f;
            shapeRenderer.rect(x - border, y - border,
                    COIN_SIZE + border * 2, COIN_SIZE + border * 2);
        }

        shapeRenderer.end();

        batch.begin();

        titleFont.draw(batch, titleText,
                screenWidth / 2f - titleLayout.width / 2f, headerY);

        for (int i = 0; i < allCoins.size(); i++) {
            CoinDefinition def = allCoins.get(i);
            int col = i % (int) COLS;
            int row = i / (int) COLS;

            float x = gridStartX + col * slotSize;
            float y = gridStartY - row * slotSize;

            if (y + COIN_SIZE < 0 || y > screenHeight + COIN_SIZE) continue;

            boolean owned = game.coinInventory.owns(def.id);

            Texture texture = owned
                    ? game.assetStore.getHeads(def)
                    : game.assetStore.lockedCoin;

            batch.draw(texture, x, y, COIN_SIZE, COIN_SIZE);

            String label = owned ? def.displayName : "???";
            GlyphLayout labelLayout = new GlyphLayout(statsFont, label);
            statsFont.draw(batch, label,
                    x + COIN_SIZE / 2f - labelLayout.width / 2f,
                    y - 20f);
        }

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

            float backLeft = margin - padding;
            float backRight = margin + backLayout.width + padding;
            float backBottom = backY - backLayout.height - padding;
            float backTop = backY + padding;

            float statsX = screenWidth - margin - statsLayout.width;
            float statsLeft = statsX - padding;
            float statsRight = statsX + statsLayout.width + padding;
            float statsBottom = statsY - statsLayout.height - padding;
            float statsTop = statsY + padding;

            if (touchX >= backLeft && touchX <= backRight
                    && touchY >= backBottom && touchY <= backTop) {
                Gdx.input.setInputProcessor(null);
                game.setScreen(new MainScreen(game));
                return;
            }

            if (touchX >= statsLeft && touchX <= statsRight
                    && touchY >= statsBottom && touchY <= statsTop) {
                Gdx.input.setInputProcessor(null);
                game.setScreen(new StatsScreen(game));
                return;
            }

            // coin tap — gridStartY matches render exactly now
            float slotSize = COIN_SIZE + PADDING;
            float gridWidth = COLS * slotSize - PADDING;
            float gridStartX = screenWidth / 2f - gridWidth / 2f;
            float gridStartY = screenHeight * GRID_START_Y_FACTOR + scrollY;

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

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        float worldDeltaY = deltaY * (game.viewport.getWorldHeight() / Gdx.graphics.getHeight());
        scrollY -= worldDeltaY;
        scrollY = Math.max(-maxScroll, Math.min(0, scrollY));
        return true;
    }

    @Override public boolean fling(float velocityX, float velocityY, int button) { return false; }
    @Override public boolean touchDown(float x, float y, int pointer, int button) { return false; }
    @Override public boolean tap(float x, float y, int count, int button) { return false; }
    @Override public boolean longPress(float x, float y) { return false; }
    @Override public boolean zoom(float initialDistance, float distance) { return false; }
    @Override public boolean pinch(Vector2 i1, Vector2 i2, Vector2 p1, Vector2 p2) { return false; }
    @Override public boolean panStop(float x, float y, int pointer, int button) { return false; }
    @Override public void pinchStop() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}