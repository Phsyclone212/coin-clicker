package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

public class AssetStore {

    private final Map<String, Texture> textures = new HashMap<>();

    public Texture lockedCoin;

    public void loadCoins(CoinRegistry registry) {
        for (CoinDefinition def : registry.getAll()) {
            loadIfAbsent(def.headsPath);
            loadIfAbsent(def.tailsPath);
        }
        lockedCoin = new Texture(Gdx.files.internal("coins/coin_locked.png"));
    }

    private void loadIfAbsent(String path) {
        if (!textures.containsKey(path)) {
            textures.put(path, new Texture(Gdx.files.internal(path)));
        }
    }

    public Texture get(String path) {
        return textures.get(path);
    }

    public Texture getHeads(CoinDefinition def) {
        return textures.get(def.headsPath);
    }

    public Texture getTails(CoinDefinition def) {
        return textures.get(def.tailsPath);
    }

    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        if (lockedCoin != null) lockedCoin.dispose();
        textures.clear();
    }
}