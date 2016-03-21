package com.example.jakub.snake2;

/**
 * Created by Jakub on 14.02.2016.
 */
public final class EngineHardSettings implements IEngineHardSettings
{
    public static final int SCORE_REDUCTION = 7;
    public static final int MIN_PROBABILITY_RANGE = 0;
    public static final int MAX_PROBABILITY_RANGE = 100;
    public static final int PROBABILITY_THRESHOLD = 1;

    private final int m_scoreReduction;
    private final int m_minProbabilityRange;
    private final int m_maxProbabilityRange;
    private final int m_probabilityThreshold;

    public EngineHardSettings(int scoreReduction, int minProbabilityRange, int maxProbabilityRange, int probabilityThreshold)
    {
        m_scoreReduction = scoreReduction;
        m_minProbabilityRange = minProbabilityRange;
        m_maxProbabilityRange = maxProbabilityRange;
        m_probabilityThreshold = probabilityThreshold;
    }

    public int getScoreReduction()
    {
        return m_scoreReduction;
    }

    public int getMinProbabilityRange()
    {
        return m_minProbabilityRange;
    }

    public int getMaxProbabilityRange()
    {
        return m_maxProbabilityRange;
    }

    public int getProbabilityThreshold()
    {
        return m_probabilityThreshold;
    }
}
