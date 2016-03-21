package com.example.jakub.snake2;


import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import java.util.ArrayList;


/**
 * Created by Jakub on 10.02.2016.
 */
public class AnimationHandlerBase implements IFrameAnimationHandler
{
    @Nullable
    protected final ArrayList<AnimationResource> m_resources;
    protected int m_sameFrameCounter;
    protected int m_currentFrame;

    public AnimationHandlerBase(ArrayList<AnimationResource> resources, int startFrame)
    {
        if (SnakeSettings.NO_FRAME != startFrame)
        {
            m_currentFrame = startFrame;
        }

        m_resources = resources;
    }

    public int getCurrentFrame()
    {
        return m_currentFrame;
    }

    @Override
    public Bitmap getCurrentFrameGfx(Field.Direction headDirection)
    {
        setNextFrame();
        Bitmap nextFrame = null;

        if (m_resources != null)
        {
            AnimationResource firstResource = getFirstResource();
            nextFrame = firstResource.getFrameGfx(m_currentFrame);
        }

        return nextFrame;
    }

    protected AnimationResource getFirstResource()
    {
        return m_resources.get(0);
    }

    // base implementation for single graphic resource
    protected void setNextFrame()
    {
        m_sameFrameCounter++;

        if ((null != m_resources) && (m_resources.size() > 0))
        {
            AnimationResource firstResource = m_resources.get(0);

            // shift to next frame
            if (firstResource.getSameFrameThreshold() == m_sameFrameCounter)
            {
                if ((firstResource.getFramesNumber() - 1) == m_currentFrame)
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
}
