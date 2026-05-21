package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Preferences;
import java.util.HashMap;
import java.util.Map;

public class CoinInventory {

    private static final String ACTIVE_COIN_KEY = "activeCoinId";
    private static final String QUANTITY_PREFIX = "coin_qty_";
    private static final String DEFAULT_COIN_ID = "flips_1c_a";

    private final Map<String, Integer> owned = new HashMap<>();
    private String activeCoinId;
    private final CoinRegistry registry;

    public CoinInventory(CoinRegistry registry) {
        this.registry = registry;
        this.activeCoinId = DEFAULT_COIN_ID;
    }

    public void add(String coinId, int quantity) {
        int current = owned.getOrDefault(coinId, 0);
        owned.put(coinId, current + quantity);
    }

    public void add(String coinId) {
        add(coinId, 1);
    }

    public boolean owns(String coinId) {
        return owned.getOrDefault(coinId, 0) > 0;
    }

    public int getQuantity(String coinId) {
        return owned.getOrDefault(coinId, 0);
    }

    public String getActiveCoinId() {
        return activeCoinId;
    }

    public CoinDefinition getActiveCoin() {
        return registry.get(activeCoinId);
    }

    public boolean setActiveCoin(String coinId) {
        if (!owns(coinId)) return false;
        activeCoinId = coinId;
        return true;
    }

    public void saveToPrefs(Preferences prefs) {
        prefs.putString(ACTIVE_COIN_KEY, activeCoinId);
        for (Map.Entry<String, Integer> entry : owned.entrySet()) {
            prefs.putInteger(QUANTITY_PREFIX + entry.getKey(), entry.getValue());
        }
        prefs.flush();
    }

    public void loadFromPrefs(Preferences prefs) {
        activeCoinId = prefs.getString(ACTIVE_COIN_KEY, DEFAULT_COIN_ID);
        owned.clear();
        for (CoinDefinition def : registry.getAll()) {
            String key = QUANTITY_PREFIX + def.id;
            if (prefs.contains(key)) {
                owned.put(def.id, prefs.getInteger(key, 0));
            }
        }
        if (!owns(DEFAULT_COIN_ID)) {
            add(DEFAULT_COIN_ID);
        }
    }
}