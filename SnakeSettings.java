package com.example.jakub.snake2;

/**
 * Created by Jakub on 18.02.2016.
 */
public final class SnakeSettings implements ISnakeSettings
{
    public static final int INITIAL_BODY_SEGMENTS_NO = 3;
    public static final int SEGMENTS_TO_DETACH = 3;
    public static final int MINIMUM_BODY_LENGTH = 7;
    public static final int X_START_POSITION = 300;
    public static final int Y_START_POSITION = 550;
    public static final int NO_FRAME = -1;
    public static final int FIRST_BODY_SEGM_POSITION = 1;

    private final int m_initialBodySegments;
    private final int m_segmentsToDetach;
    private final int m_minimumBodyLength;
    private final int m_xStartPosition;
    private final int m_yStartPosition;

    public SnakeSettings(int initialBodySegments, int segmentsToDetach, int minimumBodyLength, int xStartPosition, int yStartPosition)
    {
        m_initialBodySegments = initialBodySegments;
        m_segmentsToDetach = segmentsToDetach;
        m_minimumBodyLength = minimumBodyLength;
        m_xStartPosition = xStartPosition;
        m_yStartPosition = yStartPosition;
    }

    public int getInitialSegments()
    {
        return m_initialBodySegments;
    }

    public int getSegmentsToDetach()
    {
        return m_segmentsToDetach;
    }

    public int getMinimumBodyLength()
    {
        return m_minimumBodyLength;
    }

    public int getXStartPosition()
    {
        return m_xStartPosition;
    }

    public int getYStartPosition()
    {
        return m_yStartPosition;
    }
}
