package com.example.jakub.snake2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Jakub on 11.12.2015.
 */
public class HighScoresActivity extends Activity
{
    public static final String GAME_FINISHED_ID = "gameFinishedID";
    public static final String PLAYER_NAME_ID = "playerNameID";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        HighScores highScoresTable = retrieveHScoresTable();

        if (null != highScoresTable)
        {
            setContentView(highScoresTable);
        }
    }

    private HighScores retrieveHScoresTable()
    {
        Intent highScoresTableIntent = getIntent();
        boolean gameFinished = highScoresTableIntent.getBooleanExtra(GAME_FINISHED_ID, false);
        HighScores highScoresTable;

        if (gameFinished)
        {
            String playerName = highScoresTableIntent.getStringExtra(PLAYER_NAME_ID);
            highScoresTable = new HighScoresPlayer(this, playerName);
        }
        else
        {
            highScoresTable = new HighScores(this);
        }

        return highScoresTable;
    }
}
