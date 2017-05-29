package com.cnnranderson.slidez;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.cnnranderson.slidez.screens.LoadingScreen;
import com.cnnranderson.slidez.screens.MainMenuScreen;
import com.cnnranderson.slidez.screens.Puzzle;
import com.cnnranderson.slidez.screens.SplashScreen;
import com.cnnranderson.slidez.screens.MentalArithmetic;
import com.cnnranderson.slidez.screens.Janken;


public class Application extends Game {

    public static final String TITLE = "Slidez";
    public static final float VERSION = .8f;
    public static final int V_WIDTH = 720;
    public static final int V_HEIGHT = 1280;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public BitmapFont font24;
    public AssetManager assets;
    public LoadingScreen loadingScreen;
    public SplashScreen splashScreen;
    public MainMenuScreen mainMenuScreen;
    public Puzzle puzzle;
    public Janken janken;
    public MentalArithmetic mentalArithmetic;

    @Override
    public void create() {
        assets = new AssetManager();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
        batch = new SpriteBatch();
        initFonts();
        loadingScreen = new LoadingScreen(this);
        splashScreen = new SplashScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        puzzle = new Puzzle(this);
        janken = new Janken(this);
        mentalArithmetic = new MentalArithmetic(this);
        this.setScreen(loadingScreen);
    }

    @Override
    public void render() {
        super.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font24.dispose();
        assets.dispose();
        loadingScreen.dispose();
        splashScreen.dispose();
        mainMenuScreen.dispose();
        puzzle.dispose();
        janken.dispose();
        mentalArithmetic.dispose();
    }   

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 48;
        params.color = Color.BLACK;
        font24 = generator.generateFont(params);
    }
}
