package com.example.jakub.snake2;


/**
 * Created by Jakub on 2015-08-08.
 */
class BreakingPoint extends Field
{
    private int m_assignedCounter;

    public BreakingPoint(int xPosition, int yPosition, Direction direction)
    {
        super(xPosition, yPosition, direction);
    }

    public int getAssignedCounter()
    {
        return m_assignedCounter;
    }

    public void increaseAssignCounter()
    {
        m_assignedCounter++;
    }
}
