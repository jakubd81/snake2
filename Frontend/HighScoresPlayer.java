package com.example.jakub.snake2;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceHolder;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Jakub on 13.01.2016.
 */
public class HighScoresPlayer extends HighScores
{
    private static final short X_INFO_POSITION = 50;
    private static final short Y_INFO_POSITION = 400;

    private String m_playerName;
    private Paint m_highlightPaint;

    public HighScoresPlayer(Context context, String playerName)
    {
        super(context);
        m_playerName = playerName;
        m_highlightPaint = new Paint();
    }

    public HighScoresPlayer(Context context)
    {
        super(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        initPaint();
        initHighlightPaint();
        Canvas canvas = getHolder().lockCanvas();

        if (null != canvas)
        {
            onDraw(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Bitmap background = ResourceManager.getInstance().getImage("menu_background");
        canvas.drawBitmap(background, 0, 0, null);
        LinkedHashMap<String, Integer> sortedHsTable = ScoresTable.retrieveSortedEntries();
        canvas.drawText("Your highest score is: ", X_INFO_POSITION, Y_INFO_POSITION, m_highlightPaint);

        if (null != sortedHsTable)
        {
            String entryToPrint;
            String name;
            int score;
            short yPosition = Y_ENTRY_START_POSITION;
            Map.Entry entry;
            Paint currentPaint;
            Iterator it = sortedHsTable.entrySet().iterator();

            while (it.hasNext())
            {
                yPosition += ENTRY_VERTICAL_OFFSET;
                entry = (Map.Entry)it.next();
                name = (String) entry.getKey();
                score = (int) entry.getValue();
                entryToPrint = name + "       " + Integer.toString(score);
                currentPaint = m_paint;

                // highlight player score entry
                if (name.equals(m_playerName))
                {
                    currentPaint = m_highlightPaint;
                }

                canvas.drawText(entryToPrint, X_ENTRY_POSITION, yPosition, currentPaint);
            }
        }
    }

    private void initHighlightPaint()
    {
        Context context = ResourceManager.getInstance().getApplicationContext();
        AssetManager assetManager = context.getAssets();
        Typeface typeFace = Typeface.createFromAsset(assetManager, "ComickBook_Simple.ttf");
        m_highlightPaint.setTypeface(typeFace);
        m_highlightPaint.setColor(Color.RED);
        m_highlightPaint.setTextSize(TEXT_SIZE);
    }
}
