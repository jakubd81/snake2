package com.example.jakub.snake2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;

/**
 * Created by Jakub on 12.02.2016.
 */
public class BodySegment extends SnakeSegment
{
    @Nullable
    protected IFrameAnimationHandler m_animationHandler;

    public BodySegment(IFrameAnimationHandler animationHandlerBase, int xPosition, int yPosition, Direction direction)
    {
        super(xPosition, yPosition, direction);
        m_animationHandler = animationHandlerBase;
    }

    @Override
    public void draw(Canvas canvas)
    {
        if (null != m_animationHandler)
        {
            Bitmap currentFrame = m_animationHandler.getCurrentFrameGfx(Direction.NO_DIRECTION);

            if (null != currentFrame)
            {
                canvas.drawBitmap(currentFrame, m_xPosition, m_yPosition, null);
            }
        }
    }

    @Override
    public int getCurrentFrame()
    {
        int currentFrame = SnakeSettings.NO_FRAME;

        if (null != m_animationHandler)
        {
            currentFrame = m_animationHandler.getCurrentFrame();
        }

        return currentFrame;
    }
}
