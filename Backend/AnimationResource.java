package com.example.jakub.snake2;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

/**
 * Created by Jakub on 10.02.2016.
 */
public class AnimationResource
{
    @Nullable
    private final Bitmap m_image;
    @Nullable
    private Bitmap[] m_framesGfx;
    private final String m_name;
    private final int m_framesGfxNumber;
    private final int m_sameFrameThreshold;
    private final int m_height;
    private final int m_width;

    public AnimationResource(Bitmap image, String ID, int framesNumber, int sameFrameThreshold, int height, int width)
    {
        m_image = image;
        m_name = ID;
        m_framesGfxNumber = framesNumber;
        m_sameFrameThreshold = sameFrameThreshold;
        m_height = height;
        m_width = width;
        m_framesGfx = new Bitmap[m_framesGfxNumber];
    }

    public Bitmap getImage() { return m_image; }
    public String getName() { return m_name; }
    public int getFramesNumber() { return m_framesGfxNumber; }
    public int getSameFrameThreshold() { return m_sameFrameThreshold; }
    public int getHeight() { return m_height; }
    public int getWidth() { return m_width; }
    public Bitmap[] getFrames() { return m_framesGfx; }

    public Bitmap getFrameGfx(int frameNumber)
    {
        Bitmap frame = null;

        if ((null != m_framesGfx) && (frameNumber <= m_framesGfxNumber))
        {
            frame = m_framesGfx[frameNumber];
        }

        return frame;
    }

    public void init()
    {
        if ((null != m_framesGfx) && (null != m_image))
        {
            for (int i = 0; i < m_framesGfxNumber; i++)
            {
                m_framesGfx[i] = Bitmap.createBitmap(m_image, i * m_width, 0, m_width, m_height);
            }
        }
    }
}
