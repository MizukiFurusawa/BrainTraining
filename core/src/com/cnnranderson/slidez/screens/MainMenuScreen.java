package com.cnnranderson.slidez.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cnnranderson.slidez.Application;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenuScreen implements Screen {
    private final Application app;
    private Stage stage;
    private Skin skin;
    private TextButton buttonAnzan, buttonJanken, button15, buttonExit;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Texture texImgWelcome;
    private Sprite sprImgWelcome;

    public MainMenuScreen(final Application app) {
        this.app = app;
        this.stage = new Stage(
            new FitViewport(Application.V_WIDTH, Application.V_HEIGHT, app.camera));
        this.shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        texImgWelcome = new Texture("img/welcome.png");
        sprImgWelcome = new Sprite(texImgWelcome);
        sprImgWelcome.setPosition((720-sprImgWelcome.getWidth())/2,840);
    }

    @Override
    public void show() {
        System.out.println("MAIN MENU");
        Gdx.input.setInputProcessor(stage);
        stage.clear();
        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", app.font24);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));
        initButtons();
    }

    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        sprImgWelcome.draw(batch);
        batch.end();

        update(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
        batch.dispose();
        texImgWelcome.dispose();
    }

    private void initButtons() {
        button15 = new TextButton("15puzzle", skin, "default");
        button15.setPosition(0, 675);
        button15.setSize(720, 200);
        button15.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        button15.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.puzzle);
            }
        });
        buttonAnzan = new TextButton("Mental arithmetic", skin, "default");
        buttonAnzan.setPosition(0, 450);
        buttonAnzan.setSize(720, 200);
        buttonAnzan.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonAnzan.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.mentalArithmetic);
            }
        });
        buttonJanken = new TextButton("rock-paper-scissors", skin, "default");
        buttonJanken.setPosition(0, 225);
        buttonJanken.setSize(720, 200);
        buttonJanken.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonJanken.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.janken);
            }
        });
        buttonExit = new TextButton("Exit", skin, "default");
        buttonExit.setPosition(0, 100);
        buttonExit.setSize(720, 100);
        buttonExit.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(button15);
        stage.addActor(buttonAnzan);
        stage.addActor(buttonJanken);
        stage.addActor(buttonExit);
    }
}
