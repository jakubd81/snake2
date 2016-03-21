package com.example.jakub.snake2;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Jakub on 13.02.2016.
 */
public class DetachedSegment extends BodySegment
{
    protected boolean m_isActive;

    public DetachedSegment(IFrameAnimationHandler animationHandler, int xPosition, int yPosition, Direction direction)
    {
        super(animationHandler, xPosition, yPosition, direction);
        m_isActive = true;
    }

    public boolean isActive()
    {
        return m_isActive;
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
            else
            {
                // if null is returned it means that all frames are used and detached segment disappeared
                m_isActive = false;
            }
        }
    }
}
