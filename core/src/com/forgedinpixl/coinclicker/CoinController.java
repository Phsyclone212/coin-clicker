package com.forgedinpixl.coinclicker;

import java.util.Random;

public class CoinController {
    // Handling gameplay mechanics in here

    private final StatsTracker statsTracker;
    private final Random random;
    boolean animatedMode;
    boolean flipInProgress;
    String currentResultText; // will be replaced by result
    boolean lastFlipWasHeads;

    public CoinController(StatsTracker statsTracker){
        this.statsTracker = statsTracker;
        this.random = new Random();
        this.animatedMode = false;
        this.flipInProgress = false;
        this.lastFlipWasHeads = false;
        this.currentResultText = "Tap coin to flip";
    }

    public void requestFlip() {
        if(flipInProgress){
            return;
        }

        lastFlipWasHeads = random.nextBoolean(); // flip of the coin

        if (lastFlipWasHeads) {
            currentResultText = "Heads";
        } else {
            currentResultText = "Tails";
        }

        statsTracker.recordFlip(lastFlipWasHeads);
    }

    public boolean isFlipInProgress() {
        return flipInProgress;
    }

    public void update(float delta){
        // animation logic later
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
