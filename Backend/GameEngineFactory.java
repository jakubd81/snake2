package com.example.jakub.snake2;

/**
 * Created by Jakub on 05.02.2016.
 */
public class GameEngineFactory
{
    public enum GameMode
    {
        normal,
        hard
    }

    public GameEngineBase produceGameEngine(GameMode gameMode)
    {
        GameEngineBase gameEngine = null;
        ScoreHandlerSettings scoreHandlerSettings =  new ScoreHandlerSettings(
                ScoreHandlerSettings.MINIMUM_SCORE,
                ScoreHandlerSettings.SCORE_FONT_SIZE,
                ScoreHandlerSettings.COUNTER_FONT_SIZE,
                ScoreHandlerSettings.SCORE_X_POSITION,
                ScoreHandlerSettings.SCORE_Y_POSITION,
                ScoreHandlerSettings.COUNTER_X_POSITION,
                ScoreHandlerSettings.COUNTER_Y_POSITION,
                ScoreHandlerSettings.SCORE_COUNTER_RATIO);

        switch (gameMode)
        {
            case normal:
            {
                VelocitySettings velocitySettings = new VelocitySettings(
                        VelocitySettings.FIRST_STEP,
                        VelocitySettings.NORMAL_LAST_STEP,
                        VelocitySettings.NORMAL_SNAKE_VELOCITY,
                        VelocitySettings.NORMAL_SNAKE_LAST_STEP_VELOCITY);

                gameEngine = new GameEngineBase(velocitySettings, scoreHandlerSettings);
                break;
            }
            case hard:
            {
                VelocitySettings velocitySettings = new VelocitySettings(
                    VelocitySettings.FIRST_STEP,
                    VelocitySettings.HARD_LAST_STEP,
                    VelocitySettings.HARD_SNAKE_VELOCITY,
                    VelocitySettings.HARD_SNAKE_LAST_STEP_VELOCITY);

                EngineHardSettings engineHardSettings = new EngineHardSettings(
                        EngineHardSettings.SCORE_REDUCTION,
                        EngineHardSettings.MIN_PROBABILITY_RANGE,
                        EngineHardSettings.MAX_PROBABILITY_RANGE,
                        EngineHardSettings.PROBABILITY_THRESHOLD);

                gameEngine = new GameEngineHard(velocitySettings, scoreHandlerSettings, engineHardSettings);
                break;
            }
        }

        return gameEngine;
    }
}
