package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class StatsScreen extends BaseScreen {

    private boolean confirmingReset = false;

    private Label totalLabel;
    private Label pointsLabel;
    private Label headsLabel;
    private Label tailsLabel;
    private Label headPercentLabel;
    private Label tailsPercentLabel;
    private Label currentStreakLabel;
    private Label historyLabel;
    private Label longestStreakLabel;
    private Label streakOddsLabel;
    private TextButton resetButton;

    public StatsScreen(CoinClicker game) {
        super(game);
        buildUI();
    }

    private void buildUI() {
        // stat labels
        totalLabel        = new Label("", game.skin, "default");
        pointsLabel       = new Label("", game.skin, "default");
        headsLabel        = new Label("", game.skin, "default");
        tailsLabel        = new Label("", game.skin, "default");
        headPercentLabel  = new Label("", game.skin, "default");
        tailsPercentLabel = new Label("", game.skin, "default");
        currentStreakLabel = new Label("", game.skin, "default");
        historyLabel      = new Label("", game.skin, "default");
        longestStreakLabel = new Label("", game.skin, "default");
        streakOddsLabel   = new Label("", game.skin, "default");

        resetButton = new TextButton("Reset Streaks", game.skin);
        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!confirmingReset) {
                    confirmingReset = true;
                    resetButton.setText("Confirm Reset");
                } else {
                    statsTracker.resetStreaks();
                    statsTracker.saveToPrefs(game.prefs);
                    confirmingReset = false;
                    resetButton.setText("Reset Streaks");
                }
            }
        });

        TextButton backButton = new TextButton("Back", game.skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmingReset = false;
                game.setScreen(new MainScreen(game));
            }
        });

        // scrollable stats content
        Table statsTable = new Table();
        statsTable.pad(20f);
        statsTable.defaults().center().padBottom(20f).row();
        statsTable.add(totalLabel).row();
        statsTable.add(pointsLabel).row();
        statsTable.add(headsLabel).row();
        statsTable.add(tailsLabel).row();
        statsTable.add(headPercentLabel).row();
        statsTable.add(tailsPercentLabel).row();
        statsTable.add(currentStreakLabel).row();
        statsTable.add(historyLabel).row();
        statsTable.add(longestStreakLabel).row();
        statsTable.add(streakOddsLabel).row();

        ScrollPane scrollPane = new ScrollPane(statsTable, game.skin);
        scrollPane.setFadeScrollBars(true);
        scrollPane.setScrollingDisabled(true, false);

        // root table
        Table root = new Table();
        root.setFillParent(true);
        root.pad(40f);

        root.add(new Label("Statistics", game.skin, "title"))
                .center().padBottom(30f).row();
        root.add(scrollPane).expandX().fillX().expandY().fillY().row();

        // bottom row
        Table bottomRow = new Table();
        bottomRow.add(backButton).width(200f).height(120f).padRight(80f);
        bottomRow.add(resetButton).width(400f).height(120f).padLeft(60f);

        root.add(bottomRow).fillX().padBottom(10f);

        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.176f, 0.102f, 0.102f, 1f);

        // update labels every frame
        totalLabel.setText("Total Flips: " + statsTracker.getTotalFlips());
        pointsLabel.setText("Points: " + statsTracker.getPoints());
        headsLabel.setText("Heads count: " + statsTracker.getHeadsCount());
        tailsLabel.setText("Tails count: " + statsTracker.getTailsCount());
        headPercentLabel.setText("Heads: " + String.format("%.3f", statsTracker.getHeadsPercentage()) + "%");
        tailsPercentLabel.setText("Tails: " + String.format("%.3f", statsTracker.getTailsPercentage()) + "%");
        currentStreakLabel.setText("Current Streak: " + statsTracker.getCurrentStreak() + " " + statsTracker.getSide());
        historyLabel.setText(statsTracker.getHistoryText());
        longestStreakLabel.setText("Longest Streak: " + statsTracker.getLongestStreak() + " " + statsTracker.getLongestStreakSide());
        streakOddsLabel.setText("Streak Chance: " + String.format("%.3f", statsTracker.getOddsPercent()) + "% (1 in " + statsTracker.getOddsNum() + ")");

        super.render(delta);
    }
}