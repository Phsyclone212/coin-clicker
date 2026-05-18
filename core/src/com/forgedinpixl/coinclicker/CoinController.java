package com.forgedinpixl.coinclicker;

import java.util.Random;

public class CoinController {

    private final StatsTracker statsTracker;
    private final Random random;
    private boolean animatedMode;
    private boolean flipInProgress;
    private String currentResultText;
    private boolean lastFlipWasHeads;
    private boolean showingHeads;

    private float cycleProgress = 0f;
    private int cyclesRemaining = 0;

    private static final float FLIP_DURATION = 0.12f;
    private static final int FLIP_CYCLES = 6;

    public CoinController(StatsTracker statsTracker){
        this.statsTracker = statsTracker;
        this.random = new Random();
        this.animatedMode = true;
        this.flipInProgress = false;
        this.lastFlipWasHeads = false;
        this.showingHeads = true;
        this.currentResultText = "Tap coin to flip";
    }

    public void requestFlip() {
        if (flipInProgress) return;

        lastFlipWasHeads = random.nextBoolean();
        currentResultText = "";

        if (animatedMode) {
            flipInProgress = true;
            cyclesRemaining = FLIP_CYCLES;
            cycleProgress = 0f;
            showingHeads = true;
        } else {
            // no animation — resolve immediately
            showingHeads = lastFlipWasHeads;
            currentResultText = lastFlipWasHeads ? "Heads" : "Tails";
            statsTracker.recordFlip(lastFlipWasHeads, false);
        }
    }

    public void update(float delta) {
        if (!flipInProgress) return;

        cycleProgress += delta / FLIP_DURATION;

        if (cycleProgress >= 1f) {
            cycleProgress = 0f;
            cyclesRemaining--;
            showingHeads = !showingHeads;

            if (cyclesRemaining <= 0) {
                flipInProgress = false;
                showingHeads = lastFlipWasHeads;
                currentResultText = lastFlipWasHeads ? "Heads" : "Tails";
                statsTracker.recordFlip(lastFlipWasHeads, true);
            }
        }
    }

    public float getCurrentXScale() {
        if (!flipInProgress) return 1f;
        return Math.abs((float) Math.cos(cycleProgress * Math.PI));
    }

    public boolean isShowingHeads() {
        return showingHeads;
    }

    public boolean isFlipInProgress() {
        return flipInProgress;
    }

    public boolean isAnimatedMode() {
        return animatedMode;
    }

    public void setAnimatedMode(boolean animatedMode) {
        this.animatedMode = animatedMode;
    }

    public boolean wasLastFlipHeads() {
        return lastFlipWasHeads;
    }

    public String getCurrentResultText() {
        return currentResultText;
    }
}