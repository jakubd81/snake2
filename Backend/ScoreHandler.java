package com.example.jakub.snake2;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;

/**
 * Created by Jakub on 03.10.2015.
 */
class ScoreHandler implements IDrawable
{
    @Nullable
    IScoreHandlerSettings m_settings;
    @Nullable
    protected final Paint m_paint;
    protected int m_scores;
    protected int m_scoreCounter;

    public ScoreHandler(IScoreHandlerSettings scoreHandlerSettings)
    {
        m_settings = scoreHandlerSettings;
        m_paint = new Paint();
    }

    public void init()
    {
        if (null != m_paint)
        {
            m_paint.setColor(Color.GREEN);
            Context context = ResourceManager.getInstance().getApplicationContext();
            AssetManager assetManager = context.getAssets();
            Typeface typeFace = Typeface.createFromAsset(assetManager, "ComickBook_Simple.ttf");
            m_paint.setTypeface(typeFace);
        }
        else
        {
            System.out.println("ERROR: ScoreHandler::init()");
        }
    }

    public int getScores()
    {
        return m_scores;
    }

    public void update(int stepVelocity)
    {
        if (null != m_settings)
        {
            if (m_scoreCounter > m_settings.getMinimumScore())
            {
                m_scoreCounter -= stepVelocity;
            }
            else
            {
                m_scoreCounter = m_settings.getMinimumScore();
            }
        }
    }

    public void addMinimumScore()
    {
        if (null != m_settings)
        {
            m_scores += m_settings.getMinimumScore();
        }
    }

    public void addScores()
    {
        m_scores += m_scoreCounter;
    }

    public void setNewScoreCounter(int headXposition, int headYposition, int foodXposition, int foodYposition)
    {
        int shortestPath = Math.abs(headXposition - foodXposition) + Math.abs(headYposition - foodYposition);

        if (null != m_settings)
        {
            m_scoreCounter = shortestPath * m_settings.getScoreCounterRatio();
        }
    }

    public void draw(Canvas canvas)
    {
        if ((null != m_paint) && (null != m_settings))
        {
            m_paint.setTextSize(m_settings.getScoreFontSize());
            canvas.drawText("SCORE: " + Integer.toString(m_scores), m_settings.getScoreXposition(), m_settings.getScoreYposition(), m_paint);
            m_paint.setTextSize(m_settings.getCounterFontSize());
            canvas.drawText("COUNTER: " + Integer.toString(m_scoreCounter), m_settings.getCounterXposition(), m_settings.getCounterYposition(), m_paint);
        }
        else
        {
            System.out.println("ERROR: ScoreHandler::draw()");
        }
    }
}
