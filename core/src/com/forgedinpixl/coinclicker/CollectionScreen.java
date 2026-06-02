package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.List;

public class CollectionScreen extends BaseScreen {

    private static class CoinSlot {
        public final String expectedId;
        public final String displayName;
        public final CoinDefinition def;

        public CoinSlot(String expectedId, String displayName, CoinDefinition def) {
            this.expectedId = expectedId;
            this.displayName = displayName;
            this.def = def;
        }
    }

    private static final float COIN_SIZE = 220f;
    private static final float PADDING   = 60f;
    private static final int   COLS      = 2;

    private List<CoinSlot> allSlots = new ArrayList<>();
    private Table coinGrid;
    private ScrollPane scrollPane;

    public CollectionScreen(CoinClicker game) {
        super(game);
        buildSlots();
        buildUI();
    }

    private void buildSlots() {
        allSlots.clear();

        String[] pixlDenoms = {"1c", "2c", "5c", "10c", "25c", "50c", "1", "2"};
        String[] pixlNames  = {"1c", "2c", "5c", "10c", "25c", "50c", "$1", "$2"};
        CoinDefinition.Rarity[] rarities = {
                CoinDefinition.Rarity.A,
                CoinDefinition.Rarity.B,
                CoinDefinition.Rarity.RARE,
                CoinDefinition.Rarity.GOLD
        };

        for (int i = 0; i < pixlDenoms.length; i++) {
            for (CoinDefinition.Rarity rarity : rarities) {
                String suffix = rarity.name().toLowerCase();
                String id     = "pixl_" + pixlDenoms[i] + "_" + suffix;
                String name   = pixlNames[i] + " (" + rarity.name() + ")";
                allSlots.add(new CoinSlot(id, name, game.coinRegistry.get(id)));
            }
        }

        List<CoinDefinition> special = game.coinRegistry.getBySet(CoinDefinition.Set.SPECIAL);
        for (CoinDefinition def : special) {
            allSlots.add(new CoinSlot(def.id, def.displayName, def));
        }

        List<CoinDefinition> milestone = game.coinRegistry.getBySet(CoinDefinition.Set.MILESTONE);
        for (CoinDefinition def : milestone) {
            allSlots.add(new CoinSlot(def.id, def.displayName, def));
        }
    }

    private void buildUI() {
        // scrollable content table
        coinGrid = new Table();
        coinGrid.top().pad(20f);

        // Pixl Set header
        coinGrid.add(new Label("Pixl Set", game.skin, "body"))
                .colspan(COLS).left().padBottom(20f).padTop(20f).row();

        // add all pixl slots (first 32 = 8 denoms x 4 rarities)
        int pixlCount = 8 * 4;
        for (int i = 0; i < pixlCount; i++) {
            addCoinSlot(allSlots.get(i));
            if ((i + 1) % COLS == 0) coinGrid.row();
        }

        // Special header if needed
        List<CoinDefinition> special = game.coinRegistry.getBySet(CoinDefinition.Set.SPECIAL);
        if (!special.isEmpty()) {
            if (pixlCount % COLS != 0) coinGrid.row();
            coinGrid.add(new Label("Special", game.skin, "body"))
                    .colspan(COLS).left().padBottom(20f).padTop(40f).row();
            for (int i = pixlCount; i < pixlCount + special.size(); i++) {
                addCoinSlot(allSlots.get(i));
                if ((i - pixlCount + 1) % COLS == 0) coinGrid.row();
            }
        }

        // Milestone header if needed
        List<CoinDefinition> milestone = game.coinRegistry.getBySet(CoinDefinition.Set.MILESTONE);
        if (!milestone.isEmpty()) {
            coinGrid.add(new Label("Milestone", game.skin, "body"))
                    .colspan(COLS).left().padBottom(20f).padTop(40f).row();
            int startIdx = pixlCount + special.size();
            for (int i = startIdx; i < allSlots.size(); i++) {
                addCoinSlot(allSlots.get(i));
                if ((i - startIdx + 1) % COLS == 0) coinGrid.row();
            }
        }

        scrollPane = new ScrollPane(coinGrid, game.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, false);

        // buttons
        TextButton backButton = new TextButton("Back", game.skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainScreen(game));
            }
        });

        TextButton statsButton = new TextButton("Stats", game.skin);
        statsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new StatsScreen(game));
            }
        });

        Table bottomRow = new Table();
        bottomRow.add(backButton).width(280f).height(120f).expandX().left();
        bottomRow.add(statsButton).width(280f).height(120f).expandX().right();

        Table root = new Table();
        root.setFillParent(true);
        root.pad(40f);
        root.top();

        root.add(new Label("Coinbook", game.skin, "title"))
                .center().padBottom(20f).row();
        root.add(scrollPane).expandX().fillX().expandY().fillY().row();
        root.add(bottomRow).fillX().padBottom(10f);

        stage.addActor(root);
    }

    private void addCoinSlot(final CoinSlot slot) {
        // each slot is a table cell containing coin image area + label
        Table slotTable = new Table();
        slotTable.pad(PADDING / 2f);

        // coin image actor — draws texture manually
        Actor coinActor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                boolean owned = slot.def != null
                        && game.coinInventory.owns(slot.expectedId);
                boolean isActive = slot.expectedId.equals(
                        game.coinInventory.getActiveCoinId());

                Texture texture = owned
                        ? game.assetStore.getHeads(slot.def)
                        : game.assetStore.lockedCoin;

                // draw highlight border
                if (isActive) {
                    batch.draw(game.assetStore.coinHighlight,
                            getX() - 12f, getY() - 12f,
                            COIN_SIZE + 24f, COIN_SIZE + 24f);
                }

                batch.draw(texture, getX(), getY(), COIN_SIZE, COIN_SIZE);
            }
        };
        coinActor.setSize(COIN_SIZE, COIN_SIZE);

        // tap to select
        coinActor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (slot.def != null && game.coinInventory.owns(slot.expectedId)) {
                    game.coinInventory.setActiveCoin(slot.expectedId);
                    game.coinInventory.saveToPrefs(game.prefs);
                }
            }
        });

        boolean owned = slot.def != null && game.coinInventory.owns(slot.expectedId);
        String labelText = owned
                ? slot.displayName + " x" + game.coinInventory.getQuantity(slot.expectedId)
                : "???";
        Label nameLabel = new Label(labelText, game.skin, "default");
        nameLabel.setWrap(false);

        slotTable.add(coinActor).size(COIN_SIZE).row();
        slotTable.add(nameLabel).center().padTop(8f);

        coinGrid.add(slotTable).pad(PADDING / 2f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.176f, 0.102f, 0.102f, 1f);

        // redraw coin grid to reflect ownership changes
        refreshCoinGrid();

        super.render(delta);
    }

    private void refreshCoinGrid() {
        // update labels for owned coins — walk slot tables in coinGrid
        int slotIndex = 0;
        for (com.badlogic.gdx.scenes.scene2d.Actor actor : coinGrid.getChildren()) {
            if (actor instanceof Table) {
                Table slotTable = (Table) actor;
                if (slotTable.getCells().size >= 2) {
                    com.badlogic.gdx.scenes.scene2d.ui.Cell labelCell =
                            slotTable.getCells().get(1);
                    if (labelCell.getActor() instanceof Label && slotIndex < allSlots.size()) {
                        CoinSlot slot = allSlots.get(slotIndex);
                        Label lbl = (Label) labelCell.getActor();
                        boolean owned = slot.def != null
                                && game.coinInventory.owns(slot.expectedId);
                        lbl.setText(owned
                                ? slot.displayName + " x" + game.coinInventory.getQuantity(slot.expectedId)
                                : "???");
                        slotIndex++;
                    }
                }
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}