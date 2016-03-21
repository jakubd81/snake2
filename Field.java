package com.example.jakub.snake2;

/**
 * Created by Jakub on 2015-07-21.
 */
class Field
{
    static
    {
        WIDTH = 50;
        HEIGHT = 50;
    }

    public static final int WIDTH;
    public static final int HEIGHT;

    public enum Direction
    {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NO_DIRECTION
    }

    protected int m_xPosition;
    protected int m_yPosition;
    protected Direction m_direction;

    public Field(int xPosition, int yPosition, Direction direction)
    {
        m_xPosition = xPosition;
        m_yPosition = yPosition;
        m_direction = direction;
    }

    public Direction getDirection()
    {
        return m_direction;
    }

    public void setDirection(Direction direction)
    {
        m_direction = direction;
    }

    public int getXposition()
    {
        return m_xPosition ;
    }

    public int getYposition()
    {
        return m_yPosition;
    }

    public void setXposition(int xPosition)
    {
        m_xPosition = xPosition;
    }

    public void setYposition(int yPosition)
    {
        m_yPosition = yPosition;
    }

    public int getCompensatedValue(Field.Direction direction)
    {
        int retCompensatedVal = 0;
        int rest = getRest(direction);

        switch (direction)
        {
            case UP:
            {
                retCompensatedVal = m_yPosition - rest;
                break;
            }
            case DOWN:
            {
                retCompensatedVal = m_yPosition + rest;
                break;
            }
            case LEFT:
            {
                retCompensatedVal =  m_xPosition - rest;
                break;
            }
            case RIGHT:
            {
                retCompensatedVal =  m_xPosition + rest;
                break;
            }
        }

        return retCompensatedVal;
    }

    private int getRest(Field.Direction direction)
    {
        int rest = 0;

        switch (direction)
        {
            case UP:
            {
                rest = m_yPosition % HEIGHT;
                break;
            }
            case DOWN:
            {
                rest = HEIGHT - (m_yPosition % HEIGHT);
                break;
            }
            case LEFT:
            {
                rest = m_xPosition % WIDTH;
                break;
            }
            case RIGHT:
            {
                rest = WIDTH - (m_xPosition % WIDTH);
                break;
            }
        }

        if (WIDTH == rest)
        {
            rest = 0;
        }

        return rest;
    }
}
