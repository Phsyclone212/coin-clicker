package com.forgedinpixl.coinclicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CoinClicker extends Game {

	public Skin skin;

	public SpriteBatch batch;
	public BitmapFont titleFont;
	public BitmapFont bodyFont;
	public BitmapFont statsFont;
	public StatsTracker statsTracker;
	public CoinController coinController;

	public CoinRegistry coinRegistry;
	public CoinInventory coinInventory;
	public CoinUnlockManager coinUnlockManager;

	public RollManager rollManager;

	public Preferences prefs;
	public AssetStore assetStore;

	public OrthographicCamera camera;
	public Viewport viewport;

	@Override
	public void create () {
		batch = new SpriteBatch();

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pixel-font.ttf"));
		FreeTypeFontParameter bodyParam = new FreeTypeFontParameter();
		bodyParam.size = 120;
		FreeTypeFontParameter titleParam = new FreeTypeFontParameter();
		titleParam.size = 180;
		FreeTypeFontParameter statsParam = new FreeTypeFontParameter();
		statsParam.size = 80;
		bodyFont = generator.generateFont(bodyParam);
		titleFont = generator.generateFont(titleParam);
		statsFont = generator.generateFont(statsParam);
		generator.dispose();

		buildSkin();

		// data layer first
		coinRegistry = new CoinRegistry();
		statsTracker = new StatsTracker();
		coinInventory = new CoinInventory(coinRegistry);
		coinUnlockManager = new CoinUnlockManager(coinInventory, coinRegistry, statsTracker);

		// prefs loaded after all objects exist
		prefs = Gdx.app.getPreferences("coinclicker_stats");
		statsTracker.loadFromPrefs(prefs);
		coinInventory.loadFromPrefs(prefs);
		coinUnlockManager.loadFromPrefs(prefs);
		rollManager = new RollManager(coinInventory, coinRegistry, statsTracker);

		// assets loaded after registry exists
		assetStore = new AssetStore();
		assetStore.loadCoins(coinRegistry);

		coinController = new CoinController(statsTracker);
		coinController.setAnimatedMode(prefs.getBoolean("animatedMode", false));

		camera = new OrthographicCamera();
		viewport = new FitViewport(1080, 1920, camera);
		viewport.apply();
		camera.position.set(540, 960, 0);
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		setScreen(new MainScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}


	private void buildSkin() {
		skin = new Skin();

		// register fonts
		skin.add("title", titleFont, BitmapFont.class);
		skin.add("body", bodyFont, BitmapFont.class);
		skin.add("stats", statsFont, BitmapFont.class);

		// colors
		Color bgColor       = new Color(0.176f, 0.102f, 0.102f, 1f); // #2D1A1A
		Color goldColor     = new Color(0.831f, 0.627f, 0.090f, 1f); // #D4A017
		Color darkColor     = new Color(0.176f, 0.102f, 0.102f, 1f); // #2D1A1A
		Color rustColor     = new Color(0.545f, 0.227f, 0.165f, 1f); // #8B3A2A
		Color creamColor    = new Color(0.961f, 0.902f, 0.784f, 1f); // #F5E6C8
		Color mauveColor    = new Color(0.769f, 0.537f, 0.604f, 1f); // #C4899A

		skin.add("gold", goldColor, Color.class);
		skin.add("dark", darkColor, Color.class);
		skin.add("rust", rustColor, Color.class);
		skin.add("cream", creamColor, Color.class);
		skin.add("mauve", mauveColor, Color.class);

		// drawables — solid color pixmaps for now, replaced with ninepatch art later
		skin.add("button-up",       newDrawable(goldColor),  Drawable.class);
		skin.add("button-down",     newDrawable(rustColor),  Drawable.class);
		skin.add("button-disabled", newDrawable(darkColor),  Drawable.class);

		// label style — cream text, stats font
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = statsFont;
		labelStyle.fontColor = creamColor;
		skin.add("default", labelStyle, LabelStyle.class);

		// title label style
		LabelStyle titleStyle = new LabelStyle();
		titleStyle.font = titleFont;
		titleStyle.fontColor = goldColor;
		skin.add("title", titleStyle, LabelStyle.class);

		// body label style
		LabelStyle bodyStyle = new LabelStyle();
		bodyStyle.font = bodyFont;
		bodyStyle.fontColor = creamColor;
		skin.add("body", bodyStyle, LabelStyle.class);

		// button style
		TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.up       = skin.getDrawable("button-up");
		buttonStyle.down     = skin.getDrawable("button-down");
		buttonStyle.disabled = skin.getDrawable("button-disabled");
		buttonStyle.font     = bodyFont;
		buttonStyle.fontColor         = creamColor;
		buttonStyle.downFontColor     = darkColor;
		buttonStyle.disabledFontColor = mauveColor;
		skin.add("default", buttonStyle, TextButton.TextButtonStyle.class);

		// scroll pane style — no background, invisible scrollbar for clean look
		ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
		skin.add("default", scrollStyle, ScrollPane.ScrollPaneStyle.class);
	}

	private Drawable newDrawable(Color color) {
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		Drawable drawable = new TextureRegionDrawable(new Texture(pixmap));
		pixmap.dispose();
		return drawable;
	}
	@Override
	public void pause() {
		statsTracker.saveToPrefs(prefs);
		coinInventory.saveToPrefs(prefs);
		coinUnlockManager.saveToPrefs(prefs);
		prefs.putBoolean("animatedMode", coinController.isAnimatedMode());
		prefs.flush();
	}
	@Override
	public void dispose () {
		statsTracker.saveToPrefs(prefs);
		coinInventory.saveToPrefs(prefs);
		coinUnlockManager.saveToPrefs(prefs);
		prefs.putBoolean("animatedMode", coinController.isAnimatedMode());
		prefs.flush();
		assetStore.dispose();
		batch.dispose();
		titleFont.dispose();
		bodyFont.dispose();
		statsFont.dispose();
		skin.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}
}
