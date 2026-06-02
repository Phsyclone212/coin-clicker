package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class StatsScreen extends BaseScreen {

    private boolean confirmingReset = false;

    public StatsScreen(CoinClicker game){
        super(game);
    }

    @Override
    public void render(float delta) {

        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();

        float titleY      = screenHeight * 0.95f;
        float statsStartY = screenHeight * 0.85f;
        float lineSpacing = screenHeight * 0.06f;
        float resetY      = screenHeight * 0.12f;
        float backY       = screenHeight * 0.05f;

        String titleText        = "Statistics";
        String totalText        = "Total Flips: " + statsTracker.getTotalFlips();
        String pointsText       = "Points: " + statsTracker.getPoints();
        String headsText        = "Heads count: " + statsTracker.getHeadsCount();
        String tailsText        = "Tails count: " + statsTracker.getTailsCount();
        String headPercent      = "Heads: " + String.format("%.3f", statsTracker.getHeadsPercentage()) + "%";
        String tailsPercent     = "Tails: " + String.format("%.3f", statsTracker.getTailsPercentage()) + "%";
        String currentStreakText = "Current Streak: " + statsTracker.getCurrentStreak() + " " + statsTracker.getSide();
        String historyText      = statsTracker.getHistoryText();
        String longestStreakText = "Longest Streak: " + statsTracker.getLongestStreak() + " " + statsTracker.getLongestStreakSide();
        String streakOddsText   = "Streak Chance: " + String.format("%.3f", statsTracker.getOddsPercent()) + "% (1 in " + statsTracker.getOddsNum() + ")";
        String resetText        = confirmingReset ? "[ Confirm Reset ]" : "[ Reset Streaks ]";
        String backText         = "[ Back ]";

        GlyphLayout titleLayout        = new GlyphLayout(titleFont, titleText);
        GlyphLayout totalLayout        = new GlyphLayout(statsFont, totalText);
        GlyphLayout pointsLayout       = new GlyphLayout(statsFont, pointsText);
        GlyphLayout headsLayout        = new GlyphLayout(statsFont, headsText);
        GlyphLayout tailsLayout        = new GlyphLayout(statsFont, tailsText);
        GlyphLayout hPercentLayout     = new GlyphLayout(statsFont, headPercent);
        GlyphLayout tPercentLayout     = new GlyphLayout(statsFont, tailsPercent);
        GlyphLayout currentStreakLayout = new GlyphLayout(statsFont, currentStreakText);
        GlyphLayout historyLayout      = new GlyphLayout(statsFont, historyText);
        GlyphLayout longestStreakLayout = new GlyphLayout(statsFont, longestStreakText);
        GlyphLayout streaksOddsLayout  = new GlyphLayout(statsFont, streakOddsText);
        GlyphLayout resetLayout        = new GlyphLayout(bodyFont, resetText);
        GlyphLayout backLayout         = new GlyphLayout(bodyFont, backText);

        ScreenUtils.clear(0.176f, 0.102f, 0.102f, 1f);

        handleInput(screenWidth, screenHeight, backY, resetY, resetLayout, backLayout);

        batch.begin();

        titleFont.draw(batch, titleText,
                screenWidth / 2f - titleLayout.width / 2f, titleY);
        statsFont.draw(batch, totalText,
                screenWidth / 2f - totalLayout.width / 2f, statsStartY);
        statsFont.draw(batch, pointsText,
                screenWidth / 2f - pointsLayout.width / 2f, statsStartY - lineSpacing);
        statsFont.draw(batch, headsText,
                screenWidth / 2f - headsLayout.width / 2f, statsStartY - lineSpacing * 2);
        statsFont.draw(batch, tailsText,
                screenWidth / 2f - tailsLayout.width / 2f, statsStartY - lineSpacing * 3);
        statsFont.draw(batch, headPercent,
                screenWidth / 2f - hPercentLayout.width / 2f, statsStartY - lineSpacing * 4);
        statsFont.draw(batch, tailsPercent,
                screenWidth / 2f - tPercentLayout.width / 2f, statsStartY - lineSpacing * 5);
        statsFont.draw(batch, currentStreakText,
                screenWidth / 2f - currentStreakLayout.width / 2f, statsStartY - lineSpacing * 6);
        statsFont.draw(batch, historyText,
                screenWidth / 2f - historyLayout.width / 2f, statsStartY - lineSpacing * 7);
        statsFont.draw(batch, longestStreakText,
                screenWidth / 2f - longestStreakLayout.width / 2f, statsStartY - lineSpacing * 8);
        statsFont.draw(batch, streakOddsText,
                screenWidth / 2f - streaksOddsLayout.width / 2f, statsStartY - lineSpacing * 9);

        // reset button — centered
        bodyFont.draw(batch, resetText,
                screenWidth / 2f - resetLayout.width / 2f, resetY);

        // back — bottom left
        bodyFont.draw(batch, backText, 100f, backY);

        batch.end();
    }

    private void handleInput(float screenWidth, float screenHeight,
                             float backY, float resetY,
                             GlyphLayout resetLayout, GlyphLayout backLayout) {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touchPos);
            float touchX = touchPos.x;
            float touchY = touchPos.y;
            float padding = 30f;

            // back button — bottom left
            float backLeft   = 100f - padding;
            float backRight  = 100f + backLayout.width + padding;
            float backBottom = backY - backLayout.height - padding;
            float backTop    = backY + padding;

            // reset button — centered
            float resetX      = screenWidth / 2f - resetLayout.width / 2f;
            float resetLeft   = resetX - padding;
            float resetRight  = resetX + resetLayout.width + padding;
            float resetBottom = resetY - resetLayout.height - padding;
            float resetTop    = resetY + padding;

            if (touchX >= backLeft && touchX <= backRight
                    && touchY >= backBottom && touchY <= backTop) {
                confirmingReset = false;
                game.setScreen(new MainScreen(game));
                return;
            }

            if (touchX >= resetLeft && touchX <= resetRight
                    && touchY >= resetBottom && touchY <= resetTop) {
                if (!confirmingReset) {
                    confirmingReset = true;
                } else {
                    statsTracker.resetStreaks();
                    statsTracker.saveToPrefs(game.prefs);
                    confirmingReset = false;
                }
                return;
            }

            // tap anywhere else cancels confirm state
            confirmingReset = false;
        }
    }
}