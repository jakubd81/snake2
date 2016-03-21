package com.example.jakub.snake2;

import android.graphics.Canvas;

/**
 * Created by Jakub on 01.02.2016.
 */
public class GameClientBase implements Runnable, IGameClient
{
    private static final double AMOUNT_OF_TICKS = 60.0;
    private static final double NS = 1000000000 / AMOUNT_OF_TICKS;
    private static final int TIMER_UPDATE = 1000;

    protected final GameEngineBase m_gameEngine;
    protected final IGameStateCallback m_gameStateCallback;
    protected Field.Direction m_selectedDirection;
    protected long m_timer;
    protected int m_updates;
    protected int m_framesGfx;
    protected final Object m_monitor;
    protected boolean m_gameIsRunning;
    protected boolean m_threadPaused;

    public GameClientBase(GameEngineBase gameEngine, IGameStateCallback gameStateCallback)
    {
        m_gameEngine = gameEngine;
        m_gameStateCallback = gameStateCallback;
        m_monitor = new Object();
    }

    public void init()
    {
        m_gameEngine.init();
    }

    public void run()
    {
        while (m_gameIsRunning)
        {
            checkIfPause();
            updateGame();
        }
    }

    public void setSelectedDirection(Field.Direction selectedDirection)
    {
        m_selectedDirection = selectedDirection;
    }

    public void startGame()
    {
        m_gameIsRunning = true;
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public void pauseGame()
    {
        m_threadPaused = true;
    }

    public void unPauseGame()
    {
        m_threadPaused = false;

        synchronized (m_monitor)
        {
            m_monitor.notify();
        }
    }

    public void stopGame()
    {
        m_gameIsRunning = false;
    }

    public void drawFrame(Canvas canvas)
    {
        m_gameEngine.draw(canvas);
    }

    private void updateGame()
    {
        long lastTime = System.nanoTime();
        double delta = 0;
        m_timer = System.currentTimeMillis();
        m_updates  = 0;
        m_framesGfx = 0;
        long now;
        now = System.nanoTime();
        delta += (now - lastTime) / NS;
        lastTime = now;

        while(delta >= 1)
        {
            //tick;
            m_updates++;
            delta--;
        }

        boolean gameOver = m_gameEngine.update(m_selectedDirection);

        if (!gameOver)
        {
            m_gameStateCallback.frameUpdated();
            updateTimer();
        }
        else
        {
            // game over
            int finalScore = m_gameEngine.getFinalScore();
            m_gameIsRunning = false;
            m_gameStateCallback.gameOver(finalScore, false);
        }

        m_framesGfx++;
    }

    private void updateTimer()
    {
        if(System.currentTimeMillis() - m_timer > TIMER_UPDATE)
        {
            m_timer += TIMER_UPDATE;
            m_framesGfx = 0;
            m_updates = 0;
        }
    }

    private void checkIfPause()
    {
        while (m_threadPaused)
        {
            try
            {
                synchronized (m_monitor)
                {
                    m_monitor.wait();
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
