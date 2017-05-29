package com.cnnranderson.slidez.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cnnranderson.slidez.Application;
import com.cnnranderson.slidez.actors.SlideButton;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Puzzle implements Screen {

    private final int buttonSize = 155;
    private final String labelScoreStr = "your score is %dpt.";
    private final Application app;
    private Stage stage;
    private Skin skin;
    private float color = 1.0f;
    private int countMove = 200;
    private int boardSize = 4;
    private int holeX, holeY;
    private int ansStatus = 0;
    private SlideButton[][] buttonGrid;
    private TextButton buttonBack;
    private Label labelAnsStatus;
    private Label labelScore;
    private boolean isFinished = false;
    private boolean isGameOver = false;

    public Puzzle(final Application app) {
        this.app = app;
        this.stage = new Stage(
                new FitViewport(Application.V_WIDTH, Application.V_HEIGHT, app.camera)
            );
    }

    @Override
    public void show() {
        System.out.println("PLAY");
        Gdx.input.setInputProcessor(stage);
        stage.clear();
        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", app.font24);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));
        ansStatus = 0;
        isFinished = false;
        isGameOver = false;
        countMove = 200;
        initNavigationButtons();
        initInfoLabel();
        initAnsStatus();
        initGrid();
        shuffle();
    }

    private void shuffle(){
        int swaps = 0;
        int shuffles;
        for(shuffles = 0; shuffles < 99; shuffles++){
            int posX = MathUtils.random(0, boardSize - 1);
            int posY = MathUtils.random(0, boardSize - 1);
            if (holeX == posX || holeY == posX) {
                moveButtons(posX,posY);
                swaps++;
            }
        }
        System.out.println("Tried: " + shuffles + ", actual moves made: " + swaps);
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
    }

    private void initInfoLabel() {
        labelAnsStatus = new Label("Tap to start!", skin, "default");
        labelAnsStatus.setPosition(30, 1050);
        labelAnsStatus.setAlignment(Align.left);
        labelAnsStatus.addAction(sequence(alpha(0f), delay(.5f), fadeIn(.5f),delay(.5f)));
        stage.addActor(labelAnsStatus);
    }

    private void initAnsStatus() {
        labelScore = new Label(String.format(labelScoreStr,countMove * 100 / 200), skin, "default");
        labelScore.setPosition(30, 970);
        labelScore.setAlignment(Align.left);
        labelScore.addAction(sequence(alpha(0f), delay(.5f), fadeIn(.5f),delay(.5f)));
        stage.addActor(labelScore);
    }

    private boolean checkGameOver(){
        if( getScore() <= 0 )return true;
        else return false;
    }

    private void setClearAction(){
        labelAnsStatus.clearActions();
        labelAnsStatus.setText("Congratulations!!");
        labelAnsStatus.addAction(sequence(alpha(0f), fadeIn(1f, Interpolation.pow5Out)));
        isFinished = true;
        ansStatus = 1;
        color = 0.0f;
    }

    private void setGameOverAction(){
        labelAnsStatus.clearActions();
        labelAnsStatus.setText("Game Over...");
        labelAnsStatus.addAction(sequence(alpha(0f), fadeIn(1f, Interpolation.pow5Out)));
        labelScore.clearActions();
        labelScore.setText(String.format(labelScoreStr,getScore()));
        labelScore.addAction(sequence(alpha(0f), fadeIn(1f, Interpolation.pow5Out)));
        isGameOver = true;
    }

    private void setInvalidAction(){
        labelAnsStatus.clearActions();
        labelAnsStatus.setText("Invalid Move. -2pt");
        labelAnsStatus.addAction(sequence(alpha(0f), fadeIn(1f, Interpolation.pow5Out),fadeOut(1f, Interpolation.pow5Out)));
        labelScore.clearActions();
        labelScore.setText(String.format(labelScoreStr,  getScore()));
        labelScore.addAction(sequence(alpha(0f), fadeIn(1f, Interpolation.pow5Out)));
        ansStatus = 2;
        color = 0.0f;
    }

    private void setMoveAction(){
        labelScore.clearActions();
        labelScore.setText(String.format(labelScoreStr,  getScore()));
        labelScore.addAction(sequence(alpha(0f), fadeIn(1f, Interpolation.pow5Out)));
        labelAnsStatus.clearActions();
        labelAnsStatus.setText("Moved. -0.5pt");
        labelAnsStatus.addAction(sequence(alpha(0f), fadeIn(1f, Interpolation.pow5Out),fadeOut(1f, Interpolation.pow5Out)));
    }

    private int getScore(){
        if(countMove * 100 / 200 < 0)return 0;
        else return countMove * 100 / 200;
    }

    private void initGrid() {
        Array<Integer> nums = new Array<Integer>();
        buttonGrid = new SlideButton[boardSize][boardSize];
        for(int i = 1; i < boardSize * boardSize; i++) {
            nums.add(i);
        }
        holeX = boardSize-1;
        holeY = boardSize-1;

        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {

                if(i != holeY || j != holeX) {
                    int id = nums.removeIndex(0);
                    buttonGrid[i][j] = new SlideButton(id + "", skin, "default", id);
                    buttonGrid[i][j].setPosition((app.camera.viewportWidth / 7) * 2 + ((buttonSize+6)) * j - 170,
                            (app.camera.viewportHeight / 5) * 3 - ((buttonSize+6)) * i);
                    buttonGrid[i][j].setSize(buttonSize, buttonSize);
                    buttonGrid[i][j].addAction(sequence(alpha(0), delay((j + 1 + (i * boardSize)) / 60f),
                            parallel(fadeIn(.5f), moveBy(0, -10, .25f, Interpolation.pow5Out))));
                    buttonGrid[i][j].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            if(isFinished){
                                ansStatus = 1;
                                color = 0.0f;
                                return;
                            }
                            if(isGameOver){
                                ansStatus = 2;
                                color = 0.0f;
                                return;
                            }

                            int buttonX = 0, buttonY = 0;
                            boolean buttonFound = false;
                            SlideButton selectedButton = (SlideButton) event.getListenerActor();

                            for(int i = 0; i < boardSize && !buttonFound; i++) {
                                for(int j = 0; j < boardSize && !buttonFound; j++) {
                                    if(buttonGrid[i][j] != null && selectedButton == buttonGrid[i][j]) {
                                        buttonX = j;
                                        buttonY = i;
                                        buttonFound = true;
                                    }
                                }
                            }

                            if(holeX == buttonX || holeY == buttonY) {
                                moveButtons(buttonX, buttonY);
                                countMove--;
                                setMoveAction();

                                if(checkGameOver()){
                                    setGameOverAction();
                                }
                                else if(solutionFound()) {
                                    setClearAction();
                                }
                            } else {
                                countMove -= 4;
                                if(checkGameOver())setGameOverAction();
                                else setInvalidAction();
                            }
                        }
                    });
                    stage.addActor(buttonGrid[i][j]);
                }
            }
        }
    }

    private void moveButtons(int x, int y) {
        SlideButton button;
        if(x < holeX) {
            for(; holeX > x; holeX--) {
                button = buttonGrid[holeY][holeX - 1];
                button.addAction(moveBy(((buttonSize+6)), 0, .5f, Interpolation.pow5Out));
                buttonGrid[holeY][holeX] = button;
                buttonGrid[holeY][holeX - 1] = null;
            }
        } else {
            for(; holeX < x; holeX++) {
                button = buttonGrid[holeY][holeX + 1];
                button.addAction(moveBy(-((buttonSize+6)), 0, .5f, Interpolation.pow5Out));
                buttonGrid[holeY][holeX] = button;
                buttonGrid[holeY][holeX + 1] = null;
            }
        }
        if(y < holeY) {
            for(; holeY > y; holeY--) {
                button = buttonGrid[holeY - 1][holeX];
                button.addAction(moveBy(0, -((buttonSize+6)), .5f, Interpolation.pow5Out));
                buttonGrid[holeY][holeX] = button;
                buttonGrid[holeY - 1][holeX] = null;
            }
        } else {
            for(; holeY < y; holeY++) {
                button = buttonGrid[holeY + 1][holeX];
                button.addAction(moveBy(0, ((buttonSize+6)), .5f, Interpolation.pow5Out));
                buttonGrid[holeY][holeX] = button;
                buttonGrid[holeY + 1][holeX] = null;
            }
        }
    }

    private boolean solutionFound() {
        int idCheck = 1;
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                if(buttonGrid[i][j] != null) {
                    if(buttonGrid[i][j].getId() == idCheck++) {
                        if(idCheck == 16) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
