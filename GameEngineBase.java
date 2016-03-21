package com.example.jakub.snake2;

import android.graphics.Canvas;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jakub on 2015-06-04.
 */
class GameEngineBase implements IGameEngineBase, IDrawable
{
    @Nullable
    protected IVelocitySettings m_velocitySettings;
    @Nullable
    protected final BreakingPointsPool m_breakingPointsPool;
    @Nullable
    protected final Food m_food;
    @Nullable
    protected final Snake m_snake;
    @Nullable
    protected final ScoreHandler m_scoreHandler;
    protected int m_stepCounter;
    protected ArrayList<IDrawable> m_drawables;

    public GameEngineBase(IVelocitySettings velocitySettings, IScoreHandlerSettings scoreHandlerSettings)
    {
        m_velocitySettings = velocitySettings;
        m_scoreHandler = new ScoreHandler(scoreHandlerSettings);
        m_breakingPointsPool = new BreakingPointsPool();
        SnakeSettings snakeSettings = new SnakeSettings(
                SnakeSettings.INITIAL_BODY_SEGMENTS_NO,
                SnakeSettings.SEGMENTS_TO_DETACH,
                SnakeSettings.MINIMUM_BODY_LENGTH,
                SnakeSettings.X_START_POSITION,
                SnakeSettings.Y_START_POSITION);
        m_snake = new Snake(snakeSettings);
        AnimationResource foodGfx = ResourceManager.getInstance().getAnimationResource("food_animated");
        ArrayList<AnimationResource> foodResources = new ArrayList<>();
        foodResources.add(foodGfx);
        AnimationHandlerBase foodAnimationHandler = new AnimationHandlerBase(foodResources, IFrameAnimationHandler.FIRST_FRAME);
        m_food = new Food(foodAnimationHandler, Food.GENERATE_TRIALS, 0, 0, Field.Direction.NO_DIRECTION);
        m_drawables = new ArrayList<>();
        m_drawables.add(m_food);
        m_drawables.add(m_snake);
        m_drawables.add(m_scoreHandler);
    }

    public void init()
    {
        if ((null != m_scoreHandler) && (null != m_snake) && (null != m_food) && (null != m_breakingPointsPool))
        {
            m_snake.init(m_breakingPointsPool);
            m_scoreHandler.init();
        }
        else
        {
            System.out.println("ERROR: GameEngineBase::init()");
        }
    }

    public void draw(Canvas canvas)
    {
        if (null != m_drawables)
        {
            for (IDrawable drawable: m_drawables)
            {
                drawable.draw(canvas);
            }
        }
        else
        {
            System.out.println("ERROR: GameEngineBase::render()");
        }
    }

    public int getFinalScore()
    {
        int finalScore = 0;

        if (null != m_scoreHandler)
        {
            finalScore = m_scoreHandler.getScores();
        }

        return finalScore;
    }

    public boolean update(Field.Direction selectedDirection)
    {
        boolean gameOver = false;

        if ((null != m_snake) && (null != m_scoreHandler) && (null != m_velocitySettings))
        {
            m_stepCounter++;
            moveSnake();

            if (m_velocitySettings.getFirstStep() == m_stepCounter)
            {
                boolean collision = checkCollisions();

                if (collision)
                {
                    ResourceManager.getInstance().playAudioSample("game_over");
                    gameOver = true;
                }
            }
            else if (m_velocitySettings.getLastStep() == m_stepCounter)
            {
                updateFood();
                updatePlayerInput(selectedDirection);
                updateBreakingPoints();
                m_stepCounter = 0;
            }
        }
        else
        {
            System.out.println("ERROR: GameEngineBase::updateBoard()");
        }

        return gameOver;
    }

    protected void moveSnake()
    {
        if ((null != m_snake) && (null != m_velocitySettings))
        {
            if (m_stepCounter != m_velocitySettings.getLastStep())
            {
                m_snake.move(m_velocitySettings.getSnakeVelocity());
            }
            else
            {
                m_snake.move(m_velocitySettings.getLastStepVelocity());
            }
        }
        else
        {
            System.out.println("ERROR: GameEngineBase::moveSnake()");
        }
    }

    protected void updatePlayerInput(Field.Direction selectedDirection)
    {
        if ((null != m_snake) && (null != m_breakingPointsPool))
        {
            Field.Direction lastDirection = m_snake.getHeadDirection();
            boolean changeDirection = checkIfChangeDirection(selectedDirection, lastDirection);

            if (changeDirection)
            {
                m_breakingPointsPool.addBreakingPoint(m_snake.getHeadXposition(), m_snake.getHeadYposition(), selectedDirection);
            }
        }
        else
        {
            System.out.println("ERROR: GameEngineBase::updatePlayerInput()");
        }
    }

    protected void updateFood()
    {
        if ((null != m_food) && (null != m_snake) && (null != m_scoreHandler))
        {
            if (!m_food.isGenerated())
            {
                boolean foodGenerated = generateNewFood();
                // if it's not generated then next generating trials will be performed in next last step
                if (foodGenerated)
                {
                    m_food.setGenerated(true);
                }
            }
            else
            {
                boolean foodEaten = m_snake.isFoodEaten(m_food.getXposition(), m_food.getYposition());

                if (foodEaten)
                {
                    m_food.setGenerated(false);
                    m_scoreHandler.addMinimumScore();
                    m_snake.addBodySegment();
                    ResourceManager.getInstance().playAudioSample("food_eaten");
                }
            }
        }
        else
        {
            System.out.println("ERROR: GameEngineBase::updateFood()");
        }
    }

    protected boolean checkIfChangeDirection(Field.Direction newDirection, Field.Direction lastDirection)
    {
        boolean changeDirection = false;

        if ((newDirection == Field.Direction.RIGHT || newDirection == Field.Direction.LEFT) &&
                (lastDirection == Field.Direction.UP || lastDirection == Field.Direction.DOWN))
        {
            changeDirection = true;
        }
        else if ((newDirection == Field.Direction.UP || newDirection == Field.Direction.DOWN) &&
                (lastDirection == Field.Direction.RIGHT || lastDirection == Field.Direction.LEFT))
        {
            changeDirection = true;
        }

        return changeDirection;
    }

    protected void updateBreakingPoints()
    {
        if ((null != m_breakingPointsPool) && (null != m_snake))
        {
            try
            {
                List<BreakingPoint> breakingPoints = m_breakingPointsPool.getBreakingPoints();

                if (null != breakingPoints)
                {
                    m_snake.assignBreakingPoints(breakingPoints);
                }

                m_breakingPointsPool.removedUsedBrkPts();
            }
            catch (NullPointerException e)
            {
                System.out.println("ERROR: GameEngineBase::updateBreakingPoints() NullPointerException: " + e.getMessage());
            }
        }
        else
        {
            System.out.println("ERROR: GameEngineBase::updateBreakingPoints()");
        }
    }

    protected boolean checkCollisions()
    {
        boolean collision = false;

        if (null != m_snake)
        {
            int snakeHeadXPosition = m_snake.getHeadXposition();
            int snakeHeadYPosition = m_snake.getHeadYposition();
            boolean suicide = m_snake.checkSuicide();

            if (suicide ||
                    (snakeHeadXPosition < 0) ||
                    (snakeHeadYPosition < 0) ||
                    (snakeHeadXPosition > GameViewConstants.PLAYGROUND_WIDTH - Field.WIDTH) ||
                    (snakeHeadYPosition > (GameViewConstants.PLAYGROUND_HEIGHT - Field.HEIGHT)))
            {
                collision = true;
            }
        }
        else
        {
            System.out.println("ERROR: GameEngineBase::checkColli   sions()");
        }

        return collision;
    }

    protected boolean generateNewFood()
    {
        boolean noSnakeCollision = false;

        if ((null != m_snake) && (null != m_food) && (null != m_scoreHandler))
        {
            try
            {
                List<SnakeSegment> snakeBody = m_snake.getSnakeBody();

                if ((null != snakeBody) && (snakeBody.size() > 0))
                {
                    for (short i = 0; (i < m_food.getGenerateTrials()) && !noSnakeCollision; i++)
                    {
                        m_food.generate();
                        noSnakeCollision = m_food.noSnakeCollision(snakeBody);
                    }
                }
            }
            catch (NullPointerException e)
            {
                System.out.println("ERROR: GameEngineBase::generateNewFood() NullPointerException: " + e.getMessage());
            }
        }
        else
        {
            System.out.println("ERROR: GameEngineBase::generateNewFood()");
        }

        return noSnakeCollision;
    }
}
