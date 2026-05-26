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
import java.util.ArrayList;
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
    private static final int COLS = 2;
    private static final float HEADER_HEIGHT = 300f;
    private static final float GRID_START_Y_FACTOR = 0.85f;

    private List<Object> renderList = new ArrayList<>();

    private final Comparator<CoinDefinition> coinComparator = new Comparator<CoinDefinition>() {
        @Override
        public int compare(CoinDefinition a, CoinDefinition b) {
            int setCompare = a.set.compareTo(b.set);
            if (setCompare != 0) return setCompare;
            int rarityCompare = a.rarity.compareTo(b.rarity);
            if (rarityCompare != 0) return rarityCompare;
            return a.id.compareTo(b.id);
        }
    };

    public CollectionScreen(CoinClicker game) {
        super(game);
        gestureDetector = new GestureDetector(this);
        shapeRenderer = new ShapeRenderer();
        Gdx.input.setInputProcessor(gestureDetector);
        buildRenderList();
        recalculateMaxScroll();
    }

    private void buildRenderList() {
        renderList.clear();

        List<CoinDefinition> pixlCoins = game.coinRegistry.getBySet(CoinDefinition.Set.PIXL);
        if (!pixlCoins.isEmpty()) {
            Collections.sort(pixlCoins, coinComparator);
            renderList.add("Pixl Set");
            renderList.addAll(pixlCoins);
        }

        List<CoinDefinition> specialCoins = game.coinRegistry.getBySet(CoinDefinition.Set.SPECIAL);
        if (!specialCoins.isEmpty()) {
            Collections.sort(specialCoins, coinComparator);
            renderList.add("Special");
            renderList.addAll(specialCoins);
        }

        List<CoinDefinition> milestoneCoins = game.coinRegistry.getBySet(CoinDefinition.Set.MILESTONE);
        if (!milestoneCoins.isEmpty()) {
            Collections.sort(milestoneCoins, coinComparator);
            renderList.add("Milestone");
            renderList.addAll(milestoneCoins);
        }
    }

    private void recalculateMaxScroll() {
        float screenHeight = game.viewport.getWorldHeight();
        float slotH = COIN_SIZE + PADDING;
        float totalHeight = 0f;
        int col = 0;

        for (Object item : renderList) {
            if (item instanceof String) {
                if (col != 0) { totalHeight += slotH; col = 0; }
                totalHeight += HEADER_HEIGHT;
            } else {
                if (col == 0) totalHeight += slotH;
                col++;
                if (col >= COLS) col = 0;
            }
        }

        maxScroll = Math.max(0, totalHeight - screenHeight * GRID_START_Y_FACTOR);
    }

    @Override
    public void render(float delta) {
        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();

        float margin = 100f;
        float titleY  = screenHeight * 0.95f;
        float backY   = screenHeight * 0.05f;
        float statsY  = screenHeight * 0.05f;

        String titleText = "Collection";
        String backText  = "[ Back ]";
        String statsText = "[ Stats ]";

        GlyphLayout titleLayout = new GlyphLayout(titleFont, titleText);
        GlyphLayout backLayout  = new GlyphLayout(bodyFont, backText);
        GlyphLayout statsLayout = new GlyphLayout(bodyFont, statsText);

        ScreenUtils.clear(0, 0, 0, 1);

        handleInput(screenWidth, screenHeight, backY, statsY,
                backLayout, statsLayout, margin);

        float slotW      = COIN_SIZE + PADDING;
        float slotH      = COIN_SIZE + PADDING;
        float gridWidth  = COLS * slotW - PADDING;
        float gridStartX = screenWidth / 2f - gridWidth / 2f;
        float gridStartY = screenHeight * GRID_START_Y_FACTOR + scrollY;

        // active indicator pass
        shapeRenderer.setProjectionMatrix(game.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(1f, 0.84f, 0f, 0.4f));

        int col = 0;
        float curY = gridStartY;

        for (Object item : renderList) {
            if (item instanceof String) {
                if (col != 0) { curY -= slotH; col = 0; }
                curY -= HEADER_HEIGHT;
            } else {
                CoinDefinition def = (CoinDefinition) item;
                boolean isActive = def.id.equals(game.coinInventory.getActiveCoinId());
                float x = gridStartX + col * slotW;

                if (isActive) {
                    float border = 12f;
                    shapeRenderer.rect(x - border, curY - border,
                            COIN_SIZE + border * 2, COIN_SIZE + border * 2);
                }

                col++;
                if (col >= COLS) { col = 0; curY -= slotH; }
            }
        }

        shapeRenderer.end();

        // main draw pass
        batch.begin();

        titleFont.draw(batch, titleText,
                screenWidth / 2f - titleLayout.width / 2f, titleY);

        col = 0;
        curY = gridStartY;

        for (Object item : renderList) {
            if (item instanceof String) {
                if (col != 0) { curY -= slotH; col = 0; }
                // draw header at current position
                String header = (String) item;
                bodyFont.draw(batch, header, gridStartX, curY);
                // then move down past the header
                curY -= HEADER_HEIGHT;
            } else {
                CoinDefinition def = (CoinDefinition) item;
                float x = gridStartX + col * slotW;

                if (curY + COIN_SIZE > 0 && curY < screenHeight + COIN_SIZE) {
                    boolean owned = game.coinInventory.owns(def.id);
                    Texture texture = owned
                            ? game.assetStore.getHeads(def)
                            : game.assetStore.lockedCoin;

                    batch.draw(texture, x, curY, COIN_SIZE, COIN_SIZE);

                    String label = owned ? def.displayName : "???";
                    GlyphLayout labelLayout = new GlyphLayout(statsFont, label);
                    statsFont.draw(batch, label,
                            x + COIN_SIZE / 2f - labelLayout.width / 2f,
                            curY - 20f);
                }

                col++;
                if (col >= COLS) { col = 0; curY -= slotH; }
            }
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

            float backLeft   = margin - padding;
            float backRight  = margin + backLayout.width + padding;
            float backBottom = backY - backLayout.height - padding;
            float backTop    = backY + padding;

            float statsX      = screenWidth - margin - statsLayout.width;
            float statsLeft   = statsX - padding;
            float statsRight  = statsX + statsLayout.width + padding;
            float statsBottom = statsY - statsLayout.height - padding;
            float statsTop    = statsY + padding;

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

            // coin tap — replicate render list walk
            float slotW      = COIN_SIZE + PADDING;
            float slotH      = COIN_SIZE + PADDING;
            float gridWidth  = COLS * slotW - PADDING;
            float gridStartX = screenWidth / 2f - gridWidth / 2f;
            float gridStartY = screenHeight * GRID_START_Y_FACTOR + scrollY;

            int col = 0;
            float curY = gridStartY;

            for (Object item : renderList) {
                if (item instanceof String) {
                    if (col != 0) { curY -= slotH; col = 0; }
                    curY -= 60f;
                    curY -= HEADER_HEIGHT;
                } else {
                    CoinDefinition def = (CoinDefinition) item;
                    float x = gridStartX + col * slotW;

                    if (touchX >= x && touchX <= x + COIN_SIZE
                            && touchY >= curY && touchY <= curY + COIN_SIZE) {
                        if (game.coinInventory.owns(def.id)) {
                            game.coinInventory.setActiveCoin(def.id);
                            game.coinInventory.saveToPrefs(game.prefs);
                        }
                        return;
                    }

                    col++;
                    if (col >= COLS) { col = 0; curY -= slotH; }
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