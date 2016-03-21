package com.example.jakub.snake2;

/**
 * Created by Jakub on 14.02.2016.
 */
public final class VelocitySettings implements IVelocitySettings
{
    public static final int FIRST_STEP = 1;
    public static final int NORMAL_LAST_STEP = 10;
    public static final int NORMAL_SNAKE_VELOCITY = 5;
    public static final int NORMAL_SNAKE_LAST_STEP_VELOCITY = 5;
    public static final int HARD_SNAKE_VELOCITY = 7;
    public static final int HARD_SNAKE_LAST_STEP_VELOCITY = 8;
    public static final int HARD_LAST_STEP = 7;

    private final int m_firstStep;
    private final int m_lastStep;
    private final int m_snakeVelocity;
    private final int m_lastStepVelocity;

    public VelocitySettings(int firstStep, int lastStep, int snakeVelocity, int lastStepVelocity)
    {
        m_firstStep = firstStep;
        m_lastStep = lastStep;
        m_snakeVelocity = snakeVelocity;
        m_lastStepVelocity = lastStepVelocity;
    }

    public int getFirstStep()
    {
        return m_firstStep;
    }

    public int getLastStep()
    {
        return m_lastStep;
    }

    public int getSnakeVelocity()
    {
        return m_snakeVelocity;
    }

    public int getLastStepVelocity()
    {
        return m_lastStepVelocity;
    }

}
