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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Janken implements Screen {
    private final Application app;
    private Stage stage;
    private Skin skin;

    private Label scoreStatusLabel;

    private TextButton buttonBack;
    private TextButton buttonCheckAns;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    private Texture[] texPrbType;
    private Sprite[] sprPrbType;

    private Texture[] texPrbType1;
    private Sprite[] sprPrbTyp1;

    private Texture[] texJankenType;
    private Sprite[] sprJankenType;

    private Texture[] texSelectJankenType;
    private Sprite[] sprSelectJankenType;

    private boolean[] selectAnsStatus;

    private int probIndex = 0;
    private int probIndex1 = 0;

    private int probNum = 0;
    private int collectNum = 0;

    private boolean isFinished = false;

    private float color = 0.0f;
    private int ansStatus = 0;

    public Janken(final Application app) {
        this.app = app;
        this.stage = new Stage(
            new FitViewport(Application.V_WIDTH, Application.V_HEIGHT, app.camera));
        this.shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        selectAnsStatus = new boolean[3];
        selectAnsStatus[0] = false;
        selectAnsStatus[1] = false;
        selectAnsStatus[2] = false;
        probIndex = 0;
        probIndex1 = 0;

        //問題文
        texPrbType = new Texture[5];
        sprPrbType = new Sprite[5];

        //問題用
        texPrbType1 = new Texture[3];
        sprPrbTyp1 = new Sprite[3];

        //選択前
        texJankenType = new Texture[3];
        sprJankenType = new Sprite[3];

        //選択後
        texSelectJankenType = new Texture[3];
        sprSelectJankenType = new Sprite[3];

        for(int i=0;i<5;i++){
            texPrbType[i] = new Texture(String.format("img/ans%02d.png",i));
            sprPrbType[i] = new Sprite(texPrbType[i]);
        }

        for(int i=0;i<3;i++){
            texJankenType[i] = new Texture(String.format("img/j%02d.png",i));
            sprJankenType[i] = new Sprite(texJankenType[i]);
            sprJankenType[i].setPosition((720-sprJankenType[i].getWidth())/2,550);
        }

        for(int i=0;i<3;i++){
            texSelectJankenType[i] = new Texture(String.format("img/jj%02d.png",i));
            sprSelectJankenType[i] = new Sprite(texSelectJankenType[i]);
            sprSelectJankenType[i].setPosition((720-sprSelectJankenType[i].getWidth())/2,550);
        }

        for(int i=0;i<3;i++){
            texPrbType1[i] = new Texture(String.format("img/j%02d.png",i));
            sprPrbTyp1[i] = new Sprite(texPrbType1[i]);
            sprPrbTyp1[i].setPosition((720-sprPrbTyp1[i].getWidth())/2,730);
        }
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



        scoreStatusLabel = new Label("(No.1) your score 0 /10", skin, "default");
        scoreStatusLabel.setAlignment(Align.center);
        scoreStatusLabel.addAction(sequence(alpha(0), fadeIn(.5f)));
        scoreStatusLabel.setPosition((720 - scoreStatusLabel.getWidth())/2, 650);
        stage.addActor(scoreStatusLabel);


        color = 0.0f;
        ansStatus = 0;
        probNum  = 0;
        collectNum = 0;
        isFinished = false;

        initNavigationButtons();
        setNextProb();
    }

    private boolean checkAnswer(){
        boolean ret = false;

        switch(probIndex){
            
            //あいこ
            case 0:
            switch(probIndex1){
                case 0:
                if(selectAnsStatus[0] && !selectAnsStatus[1] && !selectAnsStatus[2])ret = true;
                else ret = false;
                break;

                case 1:
                if(!selectAnsStatus[0] &&selectAnsStatus[1] && !selectAnsStatus[2])ret = true;
                else ret = false;
                break;

                case 2:
                if(!selectAnsStatus[0] && !selectAnsStatus[1] && selectAnsStatus[2])ret = true;
                else ret = false;
                break;
            }
            break;
            
            //勝ってください
            case 1:
            switch(probIndex1){
                case 0:
                if(!selectAnsStatus[0] && !selectAnsStatus[1] && selectAnsStatus[2])ret = true;
                else ret = false;
                break;

                case 1:
                if(selectAnsStatus[0] && !selectAnsStatus[1] && !selectAnsStatus[2])ret = true;
                else ret = false;
                break;

                case 2:
                if(!selectAnsStatus[0] && selectAnsStatus[1] && !selectAnsStatus[2])ret = true;
                else ret = false;
                break;
            }
            break;
            
            //勝たないでください
            case 2:
            switch(probIndex1){
                case 0:
                if(selectAnsStatus[0] && selectAnsStatus[1] && !selectAnsStatus[2])ret = true;
                else ret = false;
                break;

                case 1:
                if(!selectAnsStatus[0] && selectAnsStatus[1] && selectAnsStatus[2])ret = true;
                else ret = false;
                break;

                case 2:
                if(selectAnsStatus[0] && !selectAnsStatus[1] && selectAnsStatus[2])ret = true;
                else ret = false;
                break;
            }
            break;
            
            //負けてください
            case 3:
            switch(probIndex1){
                case 0:
                if(!selectAnsStatus[0] && selectAnsStatus[1] && !selectAnsStatus[2])ret = true;
                else ret = false;
                break;

                case 1:
                if(!selectAnsStatus[0] && !selectAnsStatus[1] && selectAnsStatus[2])ret = true;
                else ret = false;
                break;

                case 2:
                if(selectAnsStatus[0] && !selectAnsStatus[1] && !selectAnsStatus[2])ret = true;
                else ret = false;
                break;
            }
            break;

            //負けないでください
            case 4:
            switch(probIndex1){
                case 0:
                if(selectAnsStatus[0] && !selectAnsStatus[1] && selectAnsStatus[2])ret = true;
                else ret = false;
                break;

                case 1:
                if(selectAnsStatus[0] && selectAnsStatus[1] && !selectAnsStatus[2])ret = true;
                else ret = false;
                break;

                case 2:
                if(!selectAnsStatus[0] && selectAnsStatus[1] && selectAnsStatus[2])ret = true;
                else ret = false;
                break;
            }
            break;
        }
        return ret;
    }

    private void setNextProb(){
        probNum++;
        if(probNum > 10){
            isFinished = true;
            buttonCheckAns.setText("Please press the back button.");
            buttonCheckAns.setPosition(0, 430);
            buttonCheckAns.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
            scoreStatusLabel.setText(String.format("your score is %d/10",collectNum));
            scoreStatusLabel.setPosition((720 - scoreStatusLabel.getWidth())/2, 650);
            scoreStatusLabel.addAction(sequence(alpha(0), fadeIn(.5f)));
            return;
        }
        scoreStatusLabel.setText(String.format("(No.%d) your score is %d/10", probNum,collectNum));
        scoreStatusLabel.setPosition((720 - scoreStatusLabel.getWidth())/2, 650);
        scoreStatusLabel.addAction(sequence(alpha(0), fadeIn(.5f)));
        selectAnsStatus[0] = false;
        selectAnsStatus[1] = false;
        selectAnsStatus[2] = false;
        probIndex = MathUtils.random(0, 4);
        probIndex1 = MathUtils.random(0, 2);
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

        batch.begin();
        sprPrbTyp1[probIndex1].draw(batch);
        drawProbType(probIndex);

        touchStatusUpdate();
        drawAnsType();
        
        batch.end();

        update(delta);
        stage.draw();

    }

    public void touchStatusUpdate(){
        if(isFinished)return;
        if (Gdx.input.justTouched() && Gdx.input.isTouched(0)) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), 1280 - Gdx.input.getY(), 0);
            System.out.println("px = " + touchPos.x + " , py = " + touchPos.y);

            for(int i=0;i<3;i++){
                int x = (int)sprJankenType[i].getX();
                int y = (int)sprJankenType[i].getY();
                int w = (int)sprJankenType[i].getWidth();
                int h = (int)sprJankenType[i].getHeight();

                System.out.printf("[%d] x = %d, y = %d, w = %d, h = %d%n",i,x,y,w,h);
                if(touchPos.x > x && touchPos.x < x + w && touchPos.y > y && touchPos.y < y + h){
                    if(selectAnsStatus[i])selectAnsStatus[i] = false;
                    else selectAnsStatus[i] = true;
                    System.out.println("selectAnsStatus["+ i +"]");
                }
            }
        }
    }

    public void drawProbType(int type){
        if(isFinished)return;
        sprPrbType[type].setPosition((720-sprPrbType[type].getWidth())/2,840);
        sprPrbType[type].draw(batch);
    }

    public void drawAnsType(){

        int ansPosArray[] = new int[3];
        ansPosArray[0] = 70;
        ansPosArray[1] = (int)((double)(720-sprJankenType[1].getWidth())/2);
        ansPosArray[2] = (int)(720 - sprJankenType[2].getWidth() - 70);

        for(int i=0;i<3;i++){
            if(selectAnsStatus[i]){
                sprSelectJankenType[i].setPosition(ansPosArray[i],150);
                sprSelectJankenType[i].draw(batch);
            }
            else{
                sprJankenType[i].setPosition(ansPosArray[i],150);
                sprJankenType[i].draw(batch);
            }
        }
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
        for(int i=0;i<5;i++){
            texPrbType[i].dispose();
        }
        for(int i=0;i<3;i++){
            texPrbType1[i].dispose();
            texJankenType[i].dispose();
            texSelectJankenType[i].dispose();
        }
    }

    private void initNavigationButtons() {
        buttonBack = new TextButton("Back", skin, "default");
        buttonBack.setPosition(30, 30);
        buttonBack.setSize(150 , 70);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.mainMenuScreen);
            }
        });
        stage.addActor(buttonBack);

        buttonCheckAns = new TextButton("Check the answer.", skin, "default");
        buttonCheckAns.setPosition(0, 430);
        buttonCheckAns.setSize(720, 200);
        buttonCheckAns.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonCheckAns.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(isFinished){
                    ansStatus = 1;
                    color = 0.0f;
                    return;
                }
                if(checkAnswer()){
                    ansStatus = 1;
                    color = 0.0f;
                    collectNum++;
                }else{
                    ansStatus = 2;
                    color = 0.0f;
                }
                setNextProb();
            }
        });
        stage.addActor(buttonCheckAns);
    }
}
