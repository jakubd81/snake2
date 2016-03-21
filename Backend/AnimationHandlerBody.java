package com.example.jakub.snake2;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Jakub on 12.02.2016.
 */
public class AnimationHandlerBody extends AnimationHandlerBase
{
    private static final short TAIL_LIFE_CYCLE_FRAMES = 42;
    private int m_tailLifeCycleCount;
    private AnimationResource m_bodySegmentResource;
    private AnimationResource m_tailSegmentResource;

    public AnimationHandlerBody(ArrayList<AnimationResource> arrayList, int currentFrame)
    {
        super(arrayList, currentFrame);

        if (null != m_resources)
        {
            for (AnimationResource animationResource : m_resources)
            {
                if (animationResource.getName().equals("snake_segment_animated"))
                {
                    m_bodySegmentResource = animationResource;
                }
                else if (animationResource.getName().equals("snake_tail_segment"))
                {
                    m_tailSegmentResource = animationResource;
                }
            }
        }
    }

    @Override
    public Bitmap getCurrentFrameGfx(Field.Direction headDirection)
    {
        Bitmap currentFrame = null;

        // segments animation consists of two different graphics
        if (null != m_resources)
        {
            if (m_tailLifeCycleCount < TAIL_LIFE_CYCLE_FRAMES)
            {
                // set tail frame
                m_tailLifeCycleCount++;
                setNextFrame(m_tailSegmentResource);
                currentFrame = m_tailSegmentResource.getFrameGfx(m_currentFrame);
            }
            else
            {
                // set common frame
                setNextFrame(m_bodySegmentResource);
                currentFrame = m_bodySegmentResource.getFrameGfx(m_currentFrame);
            }
        }
        else
        {
            System.out.println("ERROR: AnimationHandlerBody::getFrameGfx()");
        }

        return currentFrame;
    }

    private void setNextFrame(AnimationResource animationResource)
    {
        m_sameFrameCounter++;

        if (animationResource.getSameFrameThreshold() == m_sameFrameCounter)
        {
            if ((animationResource.getFramesNumber() - 1) == m_currentFrame)
            {
                m_currentFrame = 0;
            }
            else
            {
                m_currentFrame++;
            }

            m_sameFrameCounter = 0;
        }
    }
}
