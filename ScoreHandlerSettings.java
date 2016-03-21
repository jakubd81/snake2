package com.example.jakub.snake2;

/**
 * Created by Jakub on 29.02.2016.
 */
public final class ScoreHandlerSettings implements IScoreHandlerSettings
{
    public static final short MINIMUM_SCORE = 10;
    public static final short SCORE_FONT_SIZE = 40;
    public static final short COUNTER_FONT_SIZE = 30;
    public static final short SCORE_X_POSITION = 20;
    public static final short SCORE_Y_POSITION = 1030;
    public static final short COUNTER_Y_POSITION = 1090;
    public static final short COUNTER_X_POSITION = 20;
    public static final short SCORE_COUNTER_RATIO = 2;

    private final int m_minimumScore;
    private final int m_scoreFontSize;
    private final int m_counterFontSize;
    private final int m_scoreXposition;
    private final int m_scoreYposition;
    private final int m_counterXposition;
    private final int m_counterYposition;
    private final int m_scoreCounterRatio;

    public ScoreHandlerSettings(int minimumScore, int scoreFontSize, int counterFontSize, int scoreXposition, int scoreYposition,
                                int counterXposition, int counterYposition, int scoreCounterRatio)

    {
        m_minimumScore = minimumScore;
        m_scoreFontSize = scoreFontSize;
        m_counterFontSize = counterFontSize;
        m_scoreXposition = scoreXposition;
        m_scoreYposition = scoreYposition;
        m_counterXposition = counterXposition;
        m_counterYposition = counterYposition;
        m_scoreCounterRatio = scoreCounterRatio;
    }

    public int getMinimumScore()
    {
        return m_minimumScore;
    }

    public int getScoreFontSize()
    {
        return m_scoreFontSize;
    }

    public int getCounterFontSize()
    {
        return m_counterFontSize;
    }

    public int getScoreXposition()
    {
        return m_scoreXposition;
    }

    public int getScoreYposition()
    {
        return m_scoreYposition;
    }

    public int getCounterXposition()
    {
        return m_counterXposition;
    }

    public int getCounterYposition()
    {
        return m_counterYposition;
    }

    public int getScoreCounterRatio()
    {
        return m_scoreCounterRatio;
    }
}
