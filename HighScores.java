package com.example.jakub.snake2;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Jakub on 09.12.2015.
 */
public class HighScores extends SurfaceView implements SurfaceHolder.Callback
{
    public static final short X_ENTRY_POSITION = 100;
    public static final short Y_ENTRY_START_POSITION = 450;
    public static final short ENTRY_VERTICAL_OFFSET = 60;
    public static final short TEXT_SIZE = 55;

    protected final Paint m_paint;

    public HighScores(Context context)
    {
        super(context);
        m_paint = new Paint();
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
         initPaint();
         Canvas canvas = getHolder().lockCanvas();

         if (null != canvas)
         {
             onDraw(canvas);
             getHolder().unlockCanvasAndPost(canvas);
         }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    @Override
    protected void onDraw(Canvas canvas)
    {
        Bitmap background = ResourceManager.getInstance().getImage("menu_background");
        canvas.drawBitmap(background, 0, 0, null);
        LinkedHashMap<String, Integer> sortedHsTable = ScoresTable.retrieveSortedEntries();

        if (null != sortedHsTable)
        {
            String entryToPrint;
            String name;
            int score;
            short yPosition = Y_ENTRY_START_POSITION;
            Map.Entry entry;
            Iterator it = sortedHsTable.entrySet().iterator();

            while (it.hasNext())
            {
                yPosition += ENTRY_VERTICAL_OFFSET;
                entry = (Map.Entry)it.next();
                name = (String) entry.getKey();
                score = (int) entry.getValue();
                entryToPrint = name + "       " + Integer.toString(score);

                canvas.drawText(entryToPrint, X_ENTRY_POSITION, yPosition, m_paint);
            }
        }
    }

    protected void initPaint()
    {
        m_paint.setColor(Color.GREEN);
        m_paint.setTextSize(TEXT_SIZE);
        Context context = ResourceManager.getInstance().getApplicationContext();
        AssetManager assetManager = context.getAssets();
        Typeface typeFace = Typeface.createFromAsset(assetManager, "ComickBook_Simple.ttf");
        m_paint.setTypeface(typeFace);
    }
}
