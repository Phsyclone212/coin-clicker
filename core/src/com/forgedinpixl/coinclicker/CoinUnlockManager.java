package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.List;

public class CoinUnlockManager {

    private static final String DONOR_KEY = "isDonor";

    private final CoinInventory inventory;
    private final CoinRegistry registry;
    private final StatsTracker statsTracker;
    private boolean isDonor;

    public CoinUnlockManager(CoinInventory inventory, CoinRegistry registry,
                             StatsTracker statsTracker) {
        this.inventory = inventory;
        this.registry = registry;
        this.statsTracker = statsTracker;
        this.isDonor = false;
    }

    // call this after every flip to check milestone unlocks
    public List<String> checkMilestoneUnlocks() {
        List<String> newlyUnlocked = new ArrayList<>();
        int totalFlips = statsTracker.getTotalFlips();

        for (CoinDefinition def : registry.getAll()) {
            if (def.unlockType == CoinDefinition.UnlockType.MILESTONE
                    && def.milestoneFlips <= totalFlips
                    && !inventory.owns(def.id)) {
                inventory.add(def.id);
                newlyUnlocked.add(def.id);
            }
        }
        return newlyUnlocked;
    }

    // call this when donation is confirmed
    public void unlockDonorRewards() {
        isDonor = true;
        for (CoinDefinition def : registry.getAll()) {
            if (def.isDonorOnly && !inventory.owns(def.id)) {
                inventory.add(def.id);
            }
        }
    }

    public boolean isDonor() {
        return isDonor;
    }

    // placeholder — swap out for real Play Billing result later
    public void processDonation(boolean paymentSuccessful) {
        if (paymentSuccessful) {
            unlockDonorRewards();
        }
    }

    public void saveToPrefs(Preferences prefs) {
        prefs.putBoolean(DONOR_KEY, isDonor);
        prefs.flush();
    }

    public void loadFromPrefs(Preferences prefs) {
        isDonor = prefs.getBoolean(DONOR_KEY, false);
    }
}