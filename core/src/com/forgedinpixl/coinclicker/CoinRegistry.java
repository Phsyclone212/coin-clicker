package com.forgedinpixl.coinclicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinRegistry {

    private final Map<String, CoinDefinition> coins = new HashMap<>();

    public CoinRegistry() {
        register(new CoinDefinition(
                "pixl_1c_a", "1c Coin",
                CoinDefinition.Rarity.A, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_1c_heads.png", "coins/pixl/pixl_tails_a.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_1c_b", "1c Coin (B)",
                CoinDefinition.Rarity.B, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_1c_heads.png", "coins/pixl/pixl_tails_b.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_2c_a", "2c Coin",
                CoinDefinition.Rarity.A, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_2c_heads.png", "coins/pixl/pixl_tails_a.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_2c_b", "2c Coin (B)",
                CoinDefinition.Rarity.B, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_2c_heads.png", "coins/pixl/pixl_tails_b.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_5c_a", "5c Coin",
                CoinDefinition.Rarity.A, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_5c_heads.png", "coins/pixl/pixl_tails_a.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_5c_b", "5c Coin (B)",
                CoinDefinition.Rarity.B, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_5c_heads.png", "coins/pixl/pixl_tails_b.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_10c_a", "10c Coin",
                CoinDefinition.Rarity.A, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_10c_heads.png", "coins/pixl/pixl_tails_a.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_10c_b", "10c Coin (B)",
                CoinDefinition.Rarity.B, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_10c_heads.png", "coins/pixl/pixl_tails_b.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_25c_a", "25c Coin",
                CoinDefinition.Rarity.A, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_25c_heads.png", "coins/pixl/pixl_tails_a.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_25c_b", "25c Coin (B)",
                CoinDefinition.Rarity.B, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_25c_heads.png", "coins/pixl/pixl_tails_b.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_50c_a", "50c Coin",
                CoinDefinition.Rarity.A, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_50c_heads.png", "coins/pixl/pixl_tails_a.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_50c_b", "50c Coin (B)",
                CoinDefinition.Rarity.B, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_50c_heads.png", "coins/pixl/pixl_tails_b.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_1_a", "$1 Coin",
                CoinDefinition.Rarity.A, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_1_heads.png", "coins/pixl/pixl_tails_a.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_1_b", "$1 Coin (B)",
                CoinDefinition.Rarity.B, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_1_heads.png", "coins/pixl/pixl_tails_b.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_2_a", "$2 Coin",
                CoinDefinition.Rarity.A, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_2_heads.png", "coins/pixl/pixl_tails_a.png",
                CoinDefinition.UnlockType.ROLL, 0, false
        ));

        register(new CoinDefinition(
                "pixl_2_b", "$2 Coin (B)",
                CoinDefinition.Rarity.B, CoinDefinition.Set.PIXL,
                "coins/pixl/pixl_2_heads.png", "coins/pixl/pixl_tails_b.png",
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