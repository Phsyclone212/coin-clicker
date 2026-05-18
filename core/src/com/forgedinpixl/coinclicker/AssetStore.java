package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AssetStore {

    public Texture coinHeads;
    public Texture coinTails;

    public void load() {
        coinHeads = new Texture(Gdx.files.internal("coin_heads.png"));
        coinTails = new Texture(Gdx.files.internal("coin_tails.png"));
    }

    public void dispose() {
        coinHeads.dispose();
        coinTails.dispose();
    }
}