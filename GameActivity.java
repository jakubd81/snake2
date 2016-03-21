package com.example.jakub.snake2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


/**
 * Created by Jakub on 2015-05-27.
 */
public class GameActivity extends Activity
{
    public static final String SCORE_ID = "scoreID";
    public static final String GAME_MODE_ID = "gameModeID";
    public static final int GET_FINAL_SCORE = 3;
    public static final int GET_FINAL_SCORE_DONE = 4;

    @Nullable
    private View m_gameInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setWindowSettings();
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_gameInterface = layoutInflater.inflate(R.layout.game_interface_layout, null);
        GameEngineFactory gameEngineFactory = new GameEngineFactory();
        GameEngineFactory.GameMode gameMode = retrieveGameMode();
        GameEngineBase gameEngine = gameEngineFactory.produceGameEngine(gameMode);
        GameView gameView = new GameView(this, gameEngine);
        FrameLayout gameFrameLayout = new FrameLayout(this);

        if (null != m_gameInterface)
        {
            gameFrameLayout.addView(gameView);
            gameFrameLayout.addView(m_gameInterface);
            setContentView(gameFrameLayout);
        }
        else
        {
            System.out.println("ERROR: GameActivity::onCreate()");
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    public void setFinalScoreForResult(int finalScore)
    {
        if (finalScore > 0)
        {
            Intent retrieveScoreIntent = new Intent();
            retrieveScoreIntent.putExtra(GameActivity.SCORE_ID, finalScore);
            setResult(GameActivity.GET_FINAL_SCORE_DONE, retrieveScoreIntent);
        }
        else
        {
            setResult(Activity.RESULT_CANCELED);
        }
    }

    public View getGameInterface()
    {
        return m_gameInterface;
    }

    private void setWindowSettings()
    {
        //turn off the title at the top of the screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //turn on full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private GameEngineFactory.GameMode retrieveGameMode()
    {
        Intent gameIntent = getIntent();

        return (GameEngineFactory.GameMode)gameIntent.getSerializableExtra(GAME_MODE_ID);
    }
}
