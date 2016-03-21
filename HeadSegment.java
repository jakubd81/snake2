package com.example.jakub.snake2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;

/**
 * Created by Jakub on 12.03.2016.
 */
public class HeadSegment extends SnakeSegment
{
    @Nullable
    protected IFrameAnimationHandler m_animationHandler;

    public HeadSegment(IFrameAnimationHandler animationHandlerHead, int xPosition, int yPosition, Field.Direction direction)
    {
        super(xPosition, yPosition, direction);
        m_animationHandler = animationHandlerHead;
    }

    @Override
    public void draw(Canvas canvas)
    {
        if (null != m_animationHandler)
        {
            Bitmap currentFrame = m_animationHandler.getCurrentFrameGfx(m_direction);

            if (null != currentFrame)
            {
                canvas.drawBitmap(currentFrame, m_xPosition, m_yPosition, null);
            }
        }
    }

    @Override
    public int getCurrentFrame()
    {
        int currentFrame = -1;

        if (null != m_animationHandler)
        {
            currentFrame = m_animationHandler.getCurrentFrame();
        }

        return currentFrame;
    }
}
