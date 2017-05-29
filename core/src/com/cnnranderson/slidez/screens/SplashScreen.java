package com.cnnranderson.slidez.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cnnranderson.slidez.Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen implements Screen {

    private Texture[] textureArray;
    private Texture title;
    private Texture tap;
    private final Application app;
    private Stage stage;
    private SpriteBatch batch;
    private int index = 0;
    private Image splashImg;

    public SplashScreen(final Application app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(Application.V_WIDTH, Application.V_HEIGHT, app.camera));
    }
    
    @Override
    public void show() {
        System.out.println("SPLASH");
        Gdx.input.setInputProcessor(stage);

        title = new Texture("img\\title.png");
        tap = new Texture("img\\tap.png");

        textureArray = new Texture[80];
        for(int i=0;i<80;i++){
            String path = String.format("icon\\icon_%04d.png", i);
            textureArray[i] = new Texture(path);
        }
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();
        batch.begin();
        batch.draw(tap, (720-462)/2, (1280-86)/2 - 500);
        batch.draw(title, (720-640)/2, (1280-288)/2 + 350);
        batch.draw(textureArray[(index/5)%80], (720-228)/2, (1280-330)/2 - 150);
        batch.end();
        index++;
        if (Gdx.input.justTouched()) {
            app.setScreen(app.mainMenuScreen);
        }
    }

    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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
        for(int i=0;i<80;i++){
            textureArray[i].dispose();
        }
        title.dispose();
        tap.dispose();
    }
}
