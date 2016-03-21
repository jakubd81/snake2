package com.example.jakub.snake2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Random;

/**
 * Created by Jakub on 2015-08-12.
 */
public class Food extends Field implements IDrawable
{
    public static final int GENERATE_TRIALS = 15;

    @Nullable
    protected final Random m_random;
    protected boolean m_generated;
    @Nullable
    protected IFrameAnimationHandler m_animationHandler;
    private final int m_generateTrials;

    public Food(IFrameAnimationHandler animationHandler, int generateTrials, int xPosition, int yPosition, Direction direction)
    {
        super(xPosition, yPosition, direction);
        m_generateTrials = generateTrials;
        m_random = new Random();
        m_animationHandler = animationHandler;
    }

    public void draw(Canvas canvas)
    {
        if (m_generated && (null != m_animationHandler))
        {
            Bitmap currentFrame = m_animationHandler.getCurrentFrameGfx(Direction.NO_DIRECTION);

            if (null != currentFrame)
            {
                canvas.drawBitmap(currentFrame, m_xPosition, m_yPosition, null);
            }
        }
    }

    // if food is generated outside snake's body - returns true
    public boolean noSnakeCollision(List<SnakeSegment> snakeBody)
    {
        boolean noCollision = true;
        int snakeSegmentXpos;
        int SnakeSegmentYpos;

        for (SnakeSegment bodySegment : snakeBody)
        {
            snakeSegmentXpos = bodySegment.getXposition();
            SnakeSegmentYpos = bodySegment.getYposition();

            if ((snakeSegmentXpos == m_xPosition) && (SnakeSegmentYpos == m_yPosition))
            {
                noCollision = false;
                break;
            }
        }

        return noCollision;
    }

    public void generate()
    {
        if (null != m_random)
        {
            int minXRange = GameViewConstants.BORDER_WIDTH;
            int maxXRange = minXRange + GameViewConstants.PLAYGROUND_WIDTH - WIDTH;
            int randomX = generateRandomNumber(minXRange, maxXRange);
            m_xPosition = randomX - (randomX % WIDTH);

            int minYRange = GameViewConstants.BORDER_WIDTH;
            int maxYRange = GameViewConstants.PLAYGROUND_HEIGHT - HEIGHT;
            int randomY = generateRandomNumber(minYRange, maxYRange);
            m_yPosition = randomY - (randomY % HEIGHT);
        }
        else
        {
            System.out.println("ERROR: Food::generate()");
        }
    }

    public boolean isGenerated()
    {
        return m_generated;
    }

    public void setGenerated(boolean isEaten)
    {
        m_generated = isEaten;
    }

    public int generateRandomNumber(int minRange, int maxRange)
    {
        int randomNumber = 0;

        if (null != m_random)
        {
            randomNumber = m_random.nextInt(maxRange - minRange + 1) + minRange;
        }

        return randomNumber;
    }

    public int getGenerateTrials()
    {
        return m_generateTrials;
    }
}
