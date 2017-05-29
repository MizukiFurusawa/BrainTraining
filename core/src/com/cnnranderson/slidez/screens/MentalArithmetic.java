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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MentalArithmetic implements Screen {
    private final Application app;
    private final int movePixcel = 40;
    private Label labelInfo;
    private Label ansStatusLabel;
    private Stage stage;
    private Skin skin;
    private float color = 0.0f;
    private int ansStatus = 0;
    private TextButton[] ans;
    private TextButton buttonBack;
    private ShapeRenderer shapeRenderer;
    private int probNum = 1;
    private int collectNum = 0;
    private int num1 = 0;
    private int num2 = 0;
    private int answerIndex = 0;
    private int selectIndex = 0;

    public MentalArithmetic(final Application app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(Application.V_WIDTH, Application.V_HEIGHT, app.camera));
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        System.out.println("MentalArithmetic");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        probNum = 0;
        collectNum = 0;
        num1 = 0;
        num2 = 0;
        answerIndex = 0;
        selectIndex = 0;
        color = 0.0f;
        ansStatus = 0;

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", app.font24);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));

        labelInfo = new Label("", skin, "default");
        labelInfo.setAlignment(Align.center);
        labelInfo.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -movePixcel, .5f, Interpolation.pow5Out))));
        stage.addActor(labelInfo);

        ansStatusLabel = new Label("your score 0 /10", skin, "default");
        ansStatusLabel.setAlignment(Align.center);
        ansStatusLabel.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -movePixcel, .5f, Interpolation.pow5Out))));
        ansStatusLabel.setPosition((720 - ansStatusLabel.getWidth())/2, 1100 + movePixcel);
        stage.addActor(ansStatusLabel);

        initButtons();
        setNextProblem();
    }

    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        switch(ansStatus){
            case 0:default:Gdx.gl.glClearColor(1f, 1f, 1f, 0.5f);break;
            case 1:Gdx.gl.glClearColor(color, 1f, color, 0.5f);break;
            case 2:Gdx.gl.glClearColor(1f, color, color, 0.5f);break;
        }
        if(color < 1.0f){
            color += 0.02;
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    }

    private void checkAnswer(int index){
        if(probNum > 10)return;
        color = 0.0f;
        if(index == answerIndex){
            ansStatus = 1;
            collectNum++;
            ansStatusLabel.setText(String.format("your score is %2d/10", collectNum));
        }else{
            ansStatus = 2;
        }
        ansStatusLabel.setPosition((720 - ansStatusLabel.getWidth())/2, 1100 + movePixcel);
        ansStatusLabel.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -movePixcel, .5f, Interpolation.pow5Out))));
    }

    private void setNextProblem(){
        probNum++;
        if(probNum > 10){
            labelInfo.setText("FINISH!!");
            labelInfo.setPosition((720 - labelInfo.getWidth())/2, 1050 + movePixcel);
            labelInfo.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -movePixcel, .5f, Interpolation.pow5Out))));
            ans[0].setText("");
            ans[1].setText("Please press the back button.");
            ans[2].setText("");
        }
        else{
            num1 = MathUtils.random(1, 100);
            num2 = MathUtils.random(1, 100);
            ans[0].setPosition(0, 750 + movePixcel);
            ans[1].setPosition(0, 525 + movePixcel);
            ans[2].setPosition(0, 300 + movePixcel);
            labelInfo.clearActions();
            labelInfo.setText(String.format("(%d) %3d + %3d = ???", probNum,num1,num2));
            labelInfo.setPosition((720 - labelInfo.getWidth())/2, 1050 + movePixcel);
            labelInfo.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -movePixcel, .5f, Interpolation.pow5Out))));
            answerIndex = MathUtils.random(0, 2);
            for(int i=0; i<3; i++){
                ans[i].clearActions();
                ans[i].addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -movePixcel, .5f, Interpolation.pow5Out))));
                if(i == answerIndex){
                    ans[i].setText(String.format("%s", num1 + num2));
                }else{
                    ans[i].setText(String.format("%s",MathUtils.random(2, 200)));
                }
            }
        }
    }

    private void initButtons() {
        ans = new TextButton[3];
        ans[0] = new TextButton("", skin, "default");
        ans[0].setPosition(0, 750 + movePixcel);
        ans[0].setSize(720, 200);
        ans[0].addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -movePixcel, .5f, Interpolation.pow5Out))));
        ans[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                checkAnswer(0);
                setNextProblem();
            }
        });
        ans[1] = new TextButton("", skin, "default");
        ans[1].setPosition(0, 525 + movePixcel);
        ans[1].setSize(720, 200);
        ans[1].addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -movePixcel, .5f, Interpolation.pow5Out))));
        ans[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                checkAnswer(1);
                setNextProblem();
            }
        });
        ans[2] = new TextButton("", skin, "default");
        ans[2].setPosition(0, 300 + movePixcel);
        ans[2].setSize(720, 200);
        ans[2].addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -movePixcel, .5f, Interpolation.pow5Out))));
        ans[2].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                checkAnswer(2);
                setNextProblem();
            }
        });
        buttonBack = new TextButton("Back", skin, "default");
        buttonBack.setPosition(30, 30 + movePixcel);
        buttonBack.setSize(150 , 70);
        buttonBack.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -movePixcel, .5f, Interpolation.pow5Out))));
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.mainMenuScreen);
            }
        });
        stage.addActor(ans[0]);
        stage.addActor(ans[1]);
        stage.addActor(ans[2]);
        stage.addActor(buttonBack);
    }
}
