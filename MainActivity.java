package com.example.jakub.snake2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;


/**
 * Created by Jakub on 18.10.2015.
 */
public class MainActivity extends Activity
{
    private static final String HIGH_SCORES_SHARED_ID = "highScores";

    @Nullable
    private ScoresTable m_scoresTable;
    private boolean m_gameActivityLaunched;
    private int m_finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        m_scoresTable = new ScoresTable(ScoresTable.MAX_ENTRIES_SIZE);
        setWindowSettings();
        initResourceManager();
        getSharedPreferences(HIGH_SCORES_SHARED_ID, Activity.MODE_PRIVATE);
        setContentView(R.layout.main_menu);
        initMenuButtons();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        if (m_gameActivityLaunched && (m_finalScore > 0) && (null != m_scoresTable))
        {
            boolean addNewScore = m_scoresTable.checkIfAddNewScore(m_finalScore);

            if (addNewScore)
            {
                launchEnterNameActivity();
            }

            m_gameActivityLaunched = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ((EnterNameActivity.GET_NAME_DONE == resultCode) && (EnterNameActivity.GET_NAME == requestCode) && (null != m_scoresTable))
        {
            String playerName = data.getStringExtra(EnterNameActivity.NAME_ID);
            m_scoresTable.addPlayerScore(playerName, m_finalScore);
            launchHStableActivity(playerName);
        }
        else if ((GameActivity.GET_FINAL_SCORE_DONE == resultCode) && (GameActivity.GET_FINAL_SCORE == requestCode))
        {
            m_finalScore = data.getIntExtra(GameActivity.SCORE_ID, 0);
        }
    }

    private void setWindowSettings()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void launchEnterNameActivity()
    {
        Intent enterNameIntent = new Intent(getApplicationContext(), EnterNameActivity.class);
        startActivityForResult(enterNameIntent, EnterNameActivity.GET_NAME);
    }

    private void launchHStableActivity(String playerName)
    {
        Intent highScoresTableIntent = new Intent(getApplicationContext(), HighScoresActivity.class);
        highScoresTableIntent.putExtra(HighScoresActivity.GAME_FINISHED_ID, true);
        highScoresTableIntent.putExtra(HighScoresActivity.PLAYER_NAME_ID, playerName);
        startActivity(highScoresTableIntent);
    }

    private void launchHStableActivity()
    {
        Intent highScoresTableIntent = new Intent(getApplicationContext(), HighScoresActivity.class);
        startActivity(highScoresTableIntent);
    }

    private void initResourceManager()
    {
        ResourceManager resourceManager = ResourceManager.getInstance();
        Context applicationContext = getApplicationContext();
        resourceManager.init(applicationContext);
    }

    private void initMenuButtons()
    {
        ImageButton easyButton = (ImageButton) findViewById(R.id.easyButton);
        ImageButton hardButton = (ImageButton) findViewById(R.id.hardButton);
        ImageButton highScoresButton = (ImageButton) findViewById(R.id.highScoreButton);
        ImageButton exitButton = (ImageButton) findViewById(R.id.exitButton);

        if ((null != easyButton) && (null != hardButton) && (null != exitButton) && (null != m_scoresTable) && (null != highScoresButton))
        {
            // lambda expression aren't supported by Android Studio :(
            easyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    ResourceManager.getInstance().playAudioSample("button_click");
                    launchGameActivity(GameEngineFactory.GameMode.normal);
                }
            });

            hardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    ResourceManager.getInstance().playAudioSample("button_click");
                    launchGameActivity(GameEngineFactory.GameMode.hard);
                }
            });

            highScoresButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    ResourceManager.getInstance().playAudioSample("button_click");
                    launchHStableActivity();
                }
            });

            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    ResourceManager.getInstance().playAudioSample("button_click");
                    finish();
                }
            });
        }
    }

    private void launchGameActivity(GameEngineFactory.GameMode gameMode)
    {
        m_gameActivityLaunched = true;
        m_finalScore = 0;
        Intent gameActivityIntent = new Intent(getApplicationContext(), GameActivity.class);
        gameActivityIntent.putExtra(GameActivity.GAME_MODE_ID, gameMode);
        startActivityForResult(gameActivityIntent, GameActivity.GET_FINAL_SCORE);
    }
}
