package com.forgedinpixl.coinclicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinRegistry {

    private final Map<String, CoinDefinition> coins = new HashMap<>();

    public CoinRegistry() {
        register(new CoinDefinition(
                "flips_1c_a", "1c Coin",
                CoinDefinition.Rarity.A, CoinDefinition.Set.FLIPS,
                "coins/flips/flips_1c_heads.png", "coins/flips/flips_tails_a.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "flips_1c_b", "1c Coin (B)",
                CoinDefinition.Rarity.B, CoinDefinition.Set.FLIPS,
                "coins/flips/flips_1c_heads.png", "coins/flips/flips_tails_b.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "flips_2c_a", "2c Coin",
                CoinDefinition.Rarity.A, CoinDefinition.Set.FLIPS,
                "coins/flips/flips_2c_heads.png", "coins/flips/flips_tails_a.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "flips_2c_b", "2c Coin (B)",
                CoinDefinition.Rarity.B, CoinDefinition.Set.FLIPS,
                "coins/flips/flips_2c_heads.png", "coins/flips/flips_tails_b.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "banana_a", "Banana Coin",
                CoinDefinition.Rarity.A, CoinDefinition.Set.SPECIAL,
                "coins/donor/donor_heads.png", "coins/donor/donor_tails.png",
                CoinDefinition.UnlockType.DONOR, 0, true
        ));
    }

    private void register(CoinDefinition def) {
        coins.put(def.id, def);
    }

    public CoinDefinition get(String id) {
        return coins.get(id);
    }

    public List<CoinDefinition> getAll() {
        return new ArrayList<>(coins.values());
    }

    public List<CoinDefinition> getBySet(CoinDefinition.Set set) {
        List<CoinDefinition> result = new ArrayList<>();
        for (CoinDefinition def : coins.values()) {
            if (def.set == set) result.add(def);
        }
        return result;
    }

    public List<CoinDefinition> getByRarity(CoinDefinition.Rarity rarity) {
        List<CoinDefinition> result = new ArrayList<>();
        for (CoinDefinition def : coins.values()) {
            if (def.rarity == rarity) result.add(def);
        }
        return result;
    }
}