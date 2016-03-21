package com.example.jakub.snake2;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jakub on 2015-07-26.
 */
class BreakingPointsPool implements Observer
{
    @Nullable
    private final ArrayList<BreakingPoint> m_brkPtsMatrix;
    private int m_snakeSize;

    public BreakingPointsPool()
    {
        m_brkPtsMatrix = new ArrayList<>();
    }

    public List<BreakingPoint> getBreakingPoints()
    {
        if (null != m_brkPtsMatrix)
        {
            if (m_brkPtsMatrix.size() > 0)
            {
                for (BreakingPoint breakingPoint: m_brkPtsMatrix)
                {
                    breakingPoint.increaseAssignCounter();
                }
            }
        }
        else
        {
            System.out.println("ERROR: BreakingPointsPool::getBreakingPoints()");
        }

        return Collections.unmodifiableList(m_brkPtsMatrix);
    }

    public void removedUsedBrkPts()
    {
        if (null != m_brkPtsMatrix)
        {
            Iterator<BreakingPoint> i = m_brkPtsMatrix.iterator();
            BreakingPoint breakingPoint;

            while (i.hasNext())
            {
                breakingPoint = i.next();

                if (breakingPoint.getAssignedCounter() >= m_snakeSize)
                {
                    i.remove();
                }
            }
        }
        else
        {
            System.out.println("ERROR: BreakingPointsPool::removedUsedBrkPts()");
        }
    }

    public void addBreakingPoint(int xPosition, int yPosition, Field.Direction direction)
    {
        if (null != m_brkPtsMatrix)
        {
            BreakingPoint breakingPoint = new BreakingPoint(xPosition, yPosition, direction);
            m_brkPtsMatrix.add(breakingPoint);
        }
        else
        {
            System.out.println("ERROR: BreakingPointsPool::addBreakingPoint()");
        }
    }

    @Override
    public void update(Observable observable, Object data)
    {
        Snake.SegmentAction segmentAction = (Snake.SegmentAction)data;

        switch(segmentAction)
        {
            case add:
            {
                m_snakeSize++;
                break;
            }
            case remove:
            {
                m_snakeSize--;
                break;
            }
        }
    }
}
