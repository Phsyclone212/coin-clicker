package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainScreen extends BaseScreen {

    private Label titleLabel;
    private Label pointsLabel;
    private Label resultLabel;
    private Label streakLabel;
    private Label multiplierLabel;
    private TextButton animButton;
    private TextButton shopButton;
    private TextButton collectionButton;
    private TextButton statsButton;
    private Image coinImage;

    // coin image is drawn manually for animation — kept outside Scene2D
    private float coinSize = 600f;

    public MainScreen(CoinClicker game) {
        super(game);
        buildUI();
    }

    private void buildUI() {
        // labels
        titleLabel      = new Label("Coin Clicker", game.skin, "title");
        pointsLabel     = new Label("Points: 0", game.skin, "default");
        resultLabel     = new Label("Tap coin to flip", game.skin, "body");
        streakLabel     = new Label("Current Streak: 0", game.skin, "default");
        multiplierLabel = new Label("", game.skin, "default");

        // buttons
        animButton       = new TextButton("Anim: OFF", game.skin);
        shopButton       = new TextButton("Shop", game.skin);
        collectionButton = new TextButton("Coinbook", game.skin);
        statsButton      = new TextButton("Stats", game.skin);

        // button listeners
        animButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                coinController.setAnimatedMode(!coinController.isAnimatedMode());
            }
        });

        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ShopScreen(game));
            }
        });

        collectionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new CollectionScreen(game));
            }
        });

        statsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new StatsScreen(game));
            }
        });

        // coin tap area — invisible button sized to coin
        TextButton coinButton = new TextButton("", game.skin);
        coinButton.setSize(coinSize, coinSize);
        coinButton.setPosition(
                (1080f - coinSize) / 2f,
                1920f * 0.68f - coinSize);
        coinButton.getStyle().up = null;
        coinButton.getStyle().down = null;
        coinButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                coinController.requestFlip();
            }
        });
        stage.addActor(coinButton);

        // main layout table
        Table root = new Table();
        root.setFillParent(true);
        root.pad(40f);

        // top row — points left, anim button right
        Table topRow = new Table();
        topRow.add(pointsLabel).expandX().left();
        topRow.add(animButton).right().width(350f).height(120f).pad(10f);

        // bottom row — shop left, stats middle, collection right
        Table bottomRow = new Table();
        bottomRow.add(shopButton).expandX().left().width(280f).height(150f).pad(5f);
        bottomRow.add(statsButton).expandX().center().width(300f).height(150f).pad(15f);
        bottomRow.add(collectionButton).expandX().right().width(260f).height(150f).pad(10f);

        // assemble root table
        root.add(titleLabel).colspan(2).center().padBottom(20f).row();
        root.add(topRow).colspan(2).fillX().padBottom(20f).row();

        // spacer for coin area
        root.add(new Label("", game.skin)).colspan(2)
                .height(1920f * 0.55f).row();

        root.add(resultLabel).colspan(2).center().padBottom(10f).row();
        root.add(streakLabel).colspan(2).center().padBottom(10f).row();
        root.add(multiplierLabel).colspan(2).center().expandY().top().row();
        root.add(bottomRow).colspan(2).fillX().bottom().padBottom(10f);

        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.176f, 0.102f, 0.102f, 1f);

        coinController.update(delta);
        game.coinUnlockManager.checkMilestoneUnlocks();

        // update labels
        pointsLabel.setText("Points: " + statsTracker.getPoints());
        resultLabel.setText(coinController.getCurrentResultText());
        streakLabel.setText("Current Streak: " + statsTracker.getCurrentStreak());
        multiplierLabel.setText(coinController.isAnimatedMode() ? "x2 Points per flip" : "");
        animButton.setText(coinController.isAnimatedMode() ? "Anim: ON" : "Anim: OFF");

        super.render(delta);

        // draw coin manually for animation — Scene2D can't handle x-scale squish
        batch.begin();
        batch.setProjectionMatrix(stage.getCamera().combined);

        CoinDefinition activeCoin = game.coinInventory.getActiveCoin();
        Texture coinTexture = coinController.isShowingHeads()
                ? game.assetStore.getHeads(activeCoin)
                : game.assetStore.getTails(activeCoin);

        float coinX = (1080f - coinSize) / 2f;
        float coinY = 1920f * 0.68f - coinSize;
        float xScale = coinController.getCurrentXScale();
        float scaledWidth = coinSize * xScale;
        float offsetX = (coinSize - scaledWidth) / 2f;

        batch.draw(coinTexture, coinX + offsetX, coinY, scaledWidth, coinSize);
        batch.end();
    }
}