package com.forgedinpixl.coinclicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RollManager {

    private static final int ROLL_SIZE = 10;
    private static final int COMMON_ROLL_COST = 50;
    private static final int RARE_ROLL_COST = 150;

    private final CoinInventory coinInventory;
    private final CoinRegistry coinRegistry;
    private final StatsTracker statsTracker;
    private final Random random;

    public RollManager(CoinInventory coinInventory, CoinRegistry coinRegistry,
                       StatsTracker statsTracker) {
        this.coinInventory = coinInventory;
        this.coinRegistry = coinRegistry;
        this.statsTracker = statsTracker;
        this.random = new Random();
    }

    public boolean canAffordCommon() {
        return statsTracker.getFlipDollars() >= COMMON_ROLL_COST;
    }

    public boolean canAffordRare() {
        return statsTracker.getFlipDollars() >= RARE_ROLL_COST;
    }

    public int getCommonRollCost() { return COMMON_ROLL_COST; }
    public int getRareRollCost() { return RARE_ROLL_COST; }

    public List<CoinDefinition> performCommonRoll() {
        if (!canAffordCommon()) return null;
        statsTracker.spendFlipDollars(COMMON_ROLL_COST);
        return generateRoll(RollType.COMMON);
    }

    public List<CoinDefinition> performRareRoll() {
        if (!canAffordRare()) return null;
        statsTracker.spendFlipDollars(RARE_ROLL_COST);
        return generateRoll(RollType.RARE);
    }

    private enum RollType { COMMON, RARE }

    private List<CoinDefinition> generateRoll(RollType type) {
        List<CoinDefinition> results = new ArrayList<>();
        for (int i = 0; i < ROLL_SIZE; i++) {
            CoinDefinition.Rarity rarity = rollRarity(type);
            CoinDefinition coin = getRandomCoinOfRarity(rarity);
            if (coin != null) {
                results.add(coin);
                coinInventory.add(coin.id);
            }
        }
        return results;
    }

    private CoinDefinition.Rarity rollRarity(RollType type) {
        double roll = random.nextDouble() * 100;

        if (type == RollType.COMMON) {
            if (roll < 0.01) return CoinDefinition.Rarity.GOLD;
            if (roll < 9.99) return CoinDefinition.Rarity.RARE;
            if (roll < 44.99) return CoinDefinition.Rarity.B;
            return CoinDefinition.Rarity.A;
        } else {
            if (roll < 7.5)  return CoinDefinition.Rarity.GOLD;
            if (roll < 32.5) return CoinDefinition.Rarity.RARE;
            if (roll < 60.0) return CoinDefinition.Rarity.B;
            if (roll < 95.0) return CoinDefinition.Rarity.A;
            // 2.5% whimsical — substitute rare until implemented
            return CoinDefinition.Rarity.RARE;
        }
    }

    private CoinDefinition getRandomCoinOfRarity(CoinDefinition.Rarity rarity) {
        List<CoinDefinition> pool = new ArrayList<>();
        for (CoinDefinition def : coinRegistry.getAll()) {
            if (def.rarity == rarity && !def.isDonorOnly) {
                pool.add(def);
            }
        }
        if (pool.isEmpty()) {
            // fallback to A if no coins of this rarity exist yet
            return getRandomCoinOfRarity(CoinDefinition.Rarity.A);
        }
        return pool.get(random.nextInt(pool.size()));
    }
}