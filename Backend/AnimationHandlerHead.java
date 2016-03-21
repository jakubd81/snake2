package com.example.jakub.snake2;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Jakub on 12.03.2016.
 */
public class AnimationHandlerHead implements IFrameAnimationHandler
{
    protected int m_currentFrame;
    protected int m_sameFrameCounter;
    protected AnimationResource m_headUP;
    protected AnimationResource m_headDOWN;
    protected AnimationResource m_headRIGHT;
    protected AnimationResource m_headLEFT;

    public AnimationHandlerHead(ArrayList<AnimationResource> resources)
    {
        for (AnimationResource headResource : resources)
        {
            switch(headResource.getName())
            {
                case "snake_head_animated_up":
                {
                    m_headUP = headResource;
                    break;
                }
                case "snake_head_animated_down":
                {
                    m_headDOWN = headResource;
                    break;
                }
                case "snake_head_animated_right":
                {
                    m_headRIGHT = headResource;
                    break;
                }
                case "snake_head_animated_left":
                {
                    m_headLEFT = headResource;
                    break;
                }
            }
        }
    }

    @Override
    public int getCurrentFrame()
    {
        return m_currentFrame;
    }

    @Override
    public Bitmap getCurrentFrameGfx(Field.Direction headDirection)
    {
        Bitmap currentFrame = null;

        if ((null != m_headUP) && (null != m_headDOWN) && (null != m_headRIGHT) && (null != m_headLEFT))
        {
            switch (headDirection)
            {
                case UP:
                {
                    setNextFrame(m_headUP);
                    currentFrame = m_headUP.getFrameGfx(m_currentFrame);
                    break;
                }
                case DOWN:
                {
                    setNextFrame(m_headDOWN);
                    currentFrame = m_headDOWN.getFrameGfx(m_currentFrame);
                    break;
                }
                case RIGHT:
                {
                    setNextFrame(m_headRIGHT);
                    currentFrame = m_headRIGHT.getFrameGfx(m_currentFrame);
                    break;
                }
                case LEFT:
                {
                    setNextFrame(m_headLEFT);
                    currentFrame = m_headLEFT.getFrameGfx(m_currentFrame);
                    break;
                }
            }
        }

        return currentFrame;
    }

    protected void setNextFrame(AnimationResource animationResource)
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
