package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.List;

public class ShopScreen extends BaseScreen {

    private Label balanceLabel;
    private Label feedbackLabel;
    private TextButton commonBuyButton;
    private TextButton rareBuyButton;
    private TextButton donorButton;

    private float feedbackTimer = 0f;
    private static final float FEEDBACK_DURATION = 2f;

    public ShopScreen(CoinClicker game) {
        super(game);
        buildUI();
    }

    private void buildUI() {
        balanceLabel  = new Label("", game.skin, "default");
        feedbackLabel = new Label("", game.skin, "default");

        // section headers and descriptions as labels
        Label titleLabel       = new Label("Shop", game.skin, "title");
        Label rollSectionLabel = new Label("-- Coin Rolls --", game.skin, "default");
        Label commonDescLabel  = new Label("Common Roll  (10 coins)  |  50 Points", game.skin, "default");
        Label rareDescLabel    = new Label("Rare Roll  (10 coins)  |  150 Points", game.skin, "default");
        Label donorSectionLabel = new Label("-- Support Development --", game.skin, "default");
        Label donorDescLabel   = new Label("", game.skin, "default");

        // update donor desc based on status
        donorDescLabel.setText(game.coinUnlockManager.isDonor()
                ? "Thank you for your support!"
                : "Donate $1+ to unlock donor coins");

        // buttons
        commonBuyButton = new TextButton("Buy Common Roll", game.skin);
        rareBuyButton   = new TextButton("Buy Rare Roll", game.skin);
        donorButton     = new TextButton(
                game.coinUnlockManager.isDonor() ? "Donor - Thank You!" : "Donate $1+",
                game.skin);

        TextButton backButton = new TextButton("Back", game.skin);

        // listeners
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainScreen(game));
            }
        });

        commonBuyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.rollManager.canAffordCommon()) {
                    List<CoinDefinition> results = game.rollManager.performCommonRoll();
                    if (results != null) {
                        game.coinInventory.saveToPrefs(game.prefs);
                        game.setScreen(new RollScreen(game, results));
                    }
                } else {
                    feedbackLabel.setText("Not enough Points!");
                    feedbackTimer = FEEDBACK_DURATION;
                }
            }
        });

        rareBuyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.rollManager.canAffordRare()) {
                    List<CoinDefinition> results = game.rollManager.performRareRoll();
                    if (results != null) {
                        game.coinInventory.saveToPrefs(game.prefs);
                        game.setScreen(new RollScreen(game, results));
                    }
                } else {
                    feedbackLabel.setText("Not enough Points!");
                    feedbackTimer = FEEDBACK_DURATION;
                }
            }
        });

        donorButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!game.coinUnlockManager.isDonor()) {
                    game.coinUnlockManager.processDonation(true);
                    game.coinUnlockManager.saveToPrefs(game.prefs);
                    game.coinInventory.saveToPrefs(game.prefs);
                    feedbackLabel.setText("Thank you for your support!");
                    feedbackTimer = FEEDBACK_DURATION;
                    donorButton.setText("Donor - Thank You!");
                }
            }
        });

        // root table
        Table root = new Table();
        root.setFillParent(true);
        root.pad(40f);
        root.defaults().center().padBottom(30f);

        root.add(titleLabel).row();
        root.add(balanceLabel).row();
        root.add(rollSectionLabel).row();
        root.add(commonDescLabel).row();
        root.add(commonBuyButton).width(600f).height(130f).row();
        root.add(rareDescLabel).row();
        root.add(rareBuyButton).width(600f).height(130f).row();
        root.add(donorSectionLabel).padTop(40f).row();
        root.add(donorDescLabel).row();
        root.add(donorButton).width(500f).height(130f).row();
        root.add(feedbackLabel).row();
        root.add(backButton).width(280f).height(120f)
                .expandY().bottom().padBottom(10f);

        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.176f, 0.102f, 0.102f, 1f);

        // feedback timer
        if (feedbackTimer > 0) {
            feedbackTimer -= delta;
            if (feedbackTimer <= 0) feedbackLabel.setText("");
        }

        // update live labels
        balanceLabel.setText("Points: " + statsTracker.getPoints());
        commonBuyButton.setText(game.rollManager.canAffordCommon()
                ? "Buy Common Roll"
                : "Need 50 Points");
        rareBuyButton.setText(game.rollManager.canAffordRare()
                ? "Buy Rare Roll"
                : "Need 150 Points");

        super.render(delta);
    }
}