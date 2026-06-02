package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    private static final float PADDING_Y = 110f;
    private static final int COLS = 2;

    private Label titleLabel;
    private TextButton actionButton;

    public RollScreen(CoinClicker game, List<CoinDefinition> results) {
        super(game);
        this.results = results;
        buildUI();
    }

    private void buildUI() {
        titleLabel   = new Label("Opening...", game.skin, "title");
        actionButton = new TextButton("Skip", game.skin);

        actionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!allRevealed) {
                    revealedCount = results.size();
                    allRevealed = true;
                    autoRevealing = false;
                } else {
                    game.setScreen(new ShopScreen(game));
                }
            }
        });

        Table root = new Table();
        root.setFillParent(true);
        root.pad(40f);
        root.top();

        root.add(titleLabel).center().expandX().padBottom(20f).row();

        stage.addActor(root);
        actionButton.setSize(400f, 130f);
        actionButton.setPosition(
                (1080f - 400f) / 2f,  // centered horizontally
                40f                     // fixed distance from bottom
        );
        stage.addActor(actionButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.176f, 0.102f, 0.102f, 1f);

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

        // update UI
        titleLabel.setText(allRevealed ? "Roll Complete!" : "Opening...");
        actionButton.setText(allRevealed ? "Back to Shop" : "Skip");

        super.render(delta);

        // draw coin grid manually
        batch.begin();
        batch.setProjectionMatrix(stage.getCamera().combined);

        float screenWidth  = 1080f;
        float gridStartY   = 1920f * 0.90f;
        float slotW        = COIN_SIZE + PADDING_X;
        float slotH        = COIN_SIZE + PADDING_Y;
        float gridWidth    = COLS * slotW - PADDING_X;
        float gridStartX   = screenWidth / 2f - gridWidth / 2f;

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
                statsFont.draw(batch, rarityText,
                        x + COIN_SIZE / 2f - statsFont.getSpaceXadvance() * rarityText.length() / 2f,
                        y - COIN_SIZE - 24f);
            } else {
                batch.draw(game.assetStore.lockedCoin,
                        x, y - COIN_SIZE, COIN_SIZE, COIN_SIZE);
            }
        }

        batch.end();
    }
}