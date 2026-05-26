package com.forgedinpixl.coinclicker;

public class CoinDefinition {

    public enum UnlockType {
        ROLL,
        MILESTONE,
        DONOR,
        DEFAULT
    }

    public enum Rarity {
        A, B, RARE, GOLD
    }

    public enum Set {
        PIXL, MILESTONE, SPECIAL
    }

    public final String id;
    public final String displayName;
    public final Rarity rarity;
    public final Set set;
    public final String headsPath;
    public final String tailsPath;
    public final UnlockType unlockType;
    public final int milestoneFlips;
    public final boolean isDonorOnly;

    public CoinDefinition(String id, String displayName, Rarity rarity, Set set,
                          String headsPath, String tailsPath, UnlockType unlockType,
                          int milestoneFlips, boolean isDonorOnly) {
        this.id = id;
        this.displayName = displayName;
        this.rarity = rarity;
        this.set = set;
        this.headsPath = headsPath;
        this.tailsPath = tailsPath;
        this.unlockType = unlockType;
        this.milestoneFlips = milestoneFlips;
        this.isDonorOnly = isDonorOnly;
    }
}