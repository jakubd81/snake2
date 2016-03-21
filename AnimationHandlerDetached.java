package com.example.jakub.snake2;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Jakub on 13.02.2016.
 */
public class AnimationHandlerDetached extends AnimationHandlerBase
{
    public AnimationHandlerDetached(ArrayList<AnimationResource> arrayList, int currentFrame)
    {
        super(arrayList, currentFrame);
    }

    @Override
    public Bitmap getCurrentFrameGfx(Field.Direction headDirection)
    {
        Bitmap nextFrame = null;

        if (null != m_resources)
        {
            setNextFrame();
            AnimationResource firstResource = getFirstResource();

            if (m_currentFrame != (firstResource.getFramesNumber() - 1))
            {
                nextFrame = firstResource.getFrameGfx(m_currentFrame);
            }
        }

        return nextFrame;
    }
}
