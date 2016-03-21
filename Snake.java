package com.example.jakub.snake2;

import android.graphics.Canvas;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jakub on 2015-06-05.
 */
class Snake extends Observable implements IDrawable
{
    public enum SegmentAction
    {
        add,
        remove
    }

    @Nullable
    private final ArrayList<SnakeSegment> m_body;
    @Nullable
    private final SnakeSettings m_settings;

    public Snake(SnakeSettings settings)
    {
        m_body = new ArrayList<>();
        m_settings = settings;
    }

    public void init(Observer observer)
    {
        addObserver(observer);
        buildSnake();
    }

    public boolean checkSuicide()
    {
        boolean suicide = false;

        if (null != m_body)
        {
            int headXCompensated = 0;
            int headYCompensated = 0;
            Field.Direction headDirection = getHeadDirection();

            // adjust head to field position
            switch (headDirection)
            {
                case UP:
                {
                    headXCompensated = getHeadXposition();
                    headYCompensated = m_body.get(0).getCompensatedValue(Field.Direction.UP);
                    break;
                }
                case DOWN:
                {
                    headXCompensated = getHeadXposition();
                    headYCompensated = m_body.get(0).getCompensatedValue(Field.Direction.DOWN);
                    break;
                }
                case RIGHT:
                {
                    headXCompensated = m_body.get(0).getCompensatedValue(Field.Direction.RIGHT);
                    headYCompensated = getHeadYposition();
                    break;
                }
                case LEFT:
                {
                    headXCompensated = m_body.get(0).getCompensatedValue(Field.Direction.LEFT);
                    headYCompensated = getHeadYposition();
                    break;
                }
            }

            int segmentCompYPosition;
            int segmentCompXPosition;
            Field.Direction segmentDirection;

            // adjust segment to field position and check with head if they have the same positions = suicide
            for (SnakeSegment segment : m_body.subList(1, m_body.size()))
            {
                segmentDirection = segment.getDirection();

                switch (segmentDirection)
                {
                    case UP:
                    {
                        segmentCompYPosition = segment.getCompensatedValue(Field.Direction.DOWN);
                        suicide = checkVerticalCollision(segment, headXCompensated, headYCompensated, segmentCompYPosition);
                        break;
                    }
                    case DOWN:
                    {
                        segmentCompYPosition = segment.getCompensatedValue(Field.Direction.UP);
                        suicide = checkVerticalCollision(segment, headXCompensated, headYCompensated, segmentCompYPosition);
                        break;
                    }
                    case RIGHT:
                    {
                        segmentCompXPosition = segment.getCompensatedValue(Field.Direction.LEFT);
                        suicide = checkHorizontalCollision(segment, headXCompensated, headYCompensated, segmentCompXPosition);
                        break;
                    }
                    case LEFT:
                    {
                        segmentCompXPosition = segment.getCompensatedValue(Field.Direction.RIGHT);
                        suicide = checkHorizontalCollision(segment, headXCompensated, headYCompensated, segmentCompXPosition);
                        break;
                    }
                }

                if (suicide)
                {
                    // snake ate himself
                    break;
                }
            }
        }
        else
        {
            System.out.println("ERROR: Snake::checkSuicide()");
        }

        return suicide;
    }

    public List<SnakeSegment> getSnakeBody()
    {
        return Collections.unmodifiableList(m_body);
    }

    public void assignBreakingPoints(@Nullable List<BreakingPoint> breakingPoints)
    {
        if ((null != m_body) && (null != breakingPoints) && (0 < m_body.size()))
        {
            SnakeSegment segment;
            BreakingPoint breakingPoint;
            Field.Direction newDirection;

            for (int i = 0; i < m_body.size(); i++)
            {
                for (int j = 0; j < breakingPoints.size(); j++)
                {
                    segment = m_body.get(i);
                    breakingPoint = breakingPoints.get(j);
                    boolean samePosition = checkIfTheSameFields(segment, breakingPoint);

                    if (samePosition)
                    {
                        // BP assigned
                        newDirection = breakingPoint.getDirection();
                        segment.setDirection(newDirection);
                        break;
                    }
                }
            }
        }
        else
        {
            System.out.println("ERROR: Snake::assignBreakingPoints()");
        }
    }

    public boolean isFoodEaten(int foodXposition, int foodYposition)
    {
        boolean isFoodEaten = false;

        if (null != m_body)
        {
            isFoodEaten = ((getHeadXposition() == foodXposition) && (getHeadYposition() == foodYposition));
        }
        else
        {
            System.out.println("ERROR: Snake::isFoodEaten()");
        }

        return isFoodEaten;
    }

    public Field.Direction getHeadDirection()
    {
        Field.Direction direction = Field.Direction.NO_DIRECTION;

        if (null != m_body)
        {
            direction = m_body.get(0).getDirection();
        }
        else
        {
            System.out.println("ERROR: Snake::getHeadDirection()");
        }

        return direction;
    }

    public int getHeadXposition()
    {
        int headXposition = -1;

        if (null != m_body)
        {
            headXposition = m_body.get(0).getXposition();
        }
        else
        {
            System.out.println("ERROR: Snake::getHeadXposition()");
        }

        return headXposition;
    }

    public int getHeadYposition()
    {
        int headYposition = -1;

        if (null != m_body)
        {
            headYposition = m_body.get(0).getYposition();
        }
        else
        {
            System.out.println("ERROR: Snake::getHeadYposition()");
        }

        return headYposition;
    }

    // always add at the end of the snake's tail
    public void addBodySegment()
    {
        SnakeSegment lastSegment = getLastSegment();
        int lastSegmentXPosition = 0;
        int lastSegmentYPosition = 0;

        switch(lastSegment.getDirection())
        {
            case DOWN:
            {
                lastSegmentXPosition = lastSegment.getXposition();
                lastSegmentYPosition = lastSegment.getYposition() - BodySegment.HEIGHT;
                break;
            }
            case UP:
            {
                lastSegmentXPosition = lastSegment.getXposition();
                lastSegmentYPosition = lastSegment.getYposition() + BodySegment.HEIGHT;
                break;
            }
            case RIGHT:
            {
                lastSegmentXPosition = lastSegment.getXposition() - BodySegment.WIDTH;
                lastSegmentYPosition = lastSegment.getYposition();
                break;
            }
            case LEFT:
            {
                lastSegmentXPosition = lastSegment.getXposition() + BodySegment.WIDTH;
                lastSegmentYPosition = lastSegment.getYposition();
                break;
            }
        }

        if (null != m_body)
        {
            AnimationResource bodySegmentResource = ResourceManager.getInstance().getAnimationResource("snake_segment_animated");
            AnimationResource tailSegmentResource = ResourceManager.getInstance().getAnimationResource("snake_tail_segment");
            ArrayList<AnimationResource> bodySegmentResources = new ArrayList<>();
            bodySegmentResources.add(bodySegmentResource);
            bodySegmentResources.add(tailSegmentResource);
            AnimationHandlerBody animationHandlerBody = new AnimationHandlerBody(bodySegmentResources, getCurrentBodyFrame());
            BodySegment newSegment = new BodySegment(animationHandlerBody, lastSegmentXPosition, lastSegmentYPosition, lastSegment.getDirection());

            m_body.add(newSegment);
        }
        else
        {
            System.out.println("ERROR: Snake::addBodySegment()");
        }

        notifyBPpool(SegmentAction.add);
    }

    // removes segments from snake's body and returns detached segments
    public List<DetachedSegment> removeSegments()
    {
        ArrayList<DetachedSegment> detachedSegments = new ArrayList<>();

        if ((null != m_body) && (null != m_settings))
        {
            int lastSegmentPosition;
            DetachedSegment detachedSegment;

            for (int i = 0; i < m_settings.getSegmentsToDetach(); i++)
            {
                detachedSegment = createDetachedSegment();
                detachedSegments.add(detachedSegment);
                lastSegmentPosition = m_body.size() - 1;
                m_body.remove(lastSegmentPosition);
                notifyBPpool(SegmentAction.remove);
            }
        }

        return Collections.unmodifiableList(detachedSegments);
    }

    public boolean checkIfDetachSegments()
    {
        boolean detach = false;

        if ((null != m_body) && (null != m_settings) && (m_body.size() >= m_settings.getMinimumBodyLength()))
        {
            detach = true;
        }

        return detach;
    }

    public void move(int velocity)
    {
        if (null != m_body)
        {
            Field.Direction segmentDirection;
            int segmentYPosition;
            int segmentXPosition;
            int newYPosition;
            int newXPosition;

            for (SnakeSegment bodySegment : m_body)
            {
                segmentDirection = bodySegment.getDirection();

                switch (segmentDirection)
                {
                    case UP:
                    {
                        segmentYPosition = bodySegment.getYposition();
                        newYPosition = segmentYPosition - velocity;
                        bodySegment.setYposition(newYPosition);
                        break;
                    }
                    case DOWN:
                    {
                        segmentYPosition = bodySegment.getYposition();
                        newYPosition = segmentYPosition + velocity;
                        bodySegment.setYposition(newYPosition);
                        break;
                    }
                    case RIGHT:
                    {
                        segmentXPosition = bodySegment.getXposition();
                        newXPosition = segmentXPosition + velocity;
                        bodySegment.setXposition(newXPosition);
                        break;
                    }
                    case LEFT:
                    {
                        segmentXPosition = bodySegment.getXposition();
                        newXPosition = segmentXPosition - velocity;
                        bodySegment.setXposition(newXPosition);
                        break;
                    }
                }
            }
        }
        else
        {
            System.out.println("ERROR: Snake::move()");
        }
    }

    public void draw(Canvas canvas)
    {
        if (null != m_body)
        {
            for (SnakeSegment bodySegment : m_body)
            {
                if (null != bodySegment)
                {
                    bodySegment.draw(canvas);
                }
                else
                {
                    System.out.println("ERROR: Snake::draw() bodySegment");
                }
            }
        }
        else
        {
            System.out.println("ERROR: Snake::draw()");
        }
    }

    private DetachedSegment createDetachedSegment()
    {
        AnimationResource detachedSegmentGfx = ResourceManager.getInstance().getAnimationResource("snake_segment_detached");
        ArrayList<AnimationResource> headAnimations = new ArrayList<>();
        headAnimations.add(detachedSegmentGfx);
        AnimationHandlerDetached animationHandlerDetached = new AnimationHandlerDetached(headAnimations, IFrameAnimationHandler.FIRST_FRAME);
        SnakeSegment lastSegment = getLastSegment();

        return new DetachedSegment(animationHandlerDetached, lastSegment.getXposition(), lastSegment.getYposition(), Field.Direction.NO_DIRECTION);
    }

    private SnakeSegment getLastSegment()
    {
        SnakeSegment bodySegment = null;

        if ((null != m_body) && (0 < m_body.size()))
        {
            bodySegment = m_body.get(m_body.size() - 1);
        }
        else
        {
            System.out.println("ERROR: Snake::getLastSegment()");
        }

        return bodySegment;
    }

    private void buildSnake()
    {
        addHead();
        addBody();
    }

    private void addBody()
    {
        if (null != m_settings)
        {
            for (int i = 0; i < m_settings.getInitialSegments(); i++)
            {
                addBodySegment();
            }
        }
    }

    private void addHead()
    {
        if ((null != m_body) && (null != m_settings))
        {
            AnimationResource headAnimationUP = ResourceManager.getInstance().getAnimationResource("snake_head_animated_up");
            AnimationResource headAnimationDOWN = ResourceManager.getInstance().getAnimationResource("snake_head_animated_down");
            AnimationResource headAnimationRIGHT = ResourceManager.getInstance().getAnimationResource("snake_head_animated_right");
            AnimationResource headAnimationLEFT = ResourceManager.getInstance().getAnimationResource("snake_head_animated_left");
            ArrayList<AnimationResource> headAnimationResources = new ArrayList<>();
            headAnimationResources.add(headAnimationUP);
            headAnimationResources.add(headAnimationDOWN);
            headAnimationResources.add(headAnimationRIGHT);
            headAnimationResources.add(headAnimationLEFT);
            AnimationHandlerHead animationHandlerHead = new AnimationHandlerHead(headAnimationResources);
            HeadSegment headSegment = new HeadSegment(animationHandlerHead, m_settings.getXStartPosition(), m_settings.getYStartPosition(), Field.Direction.UP);

            m_body.add(headSegment);
            notifyBPpool(SegmentAction.add);
        }
        else
        {
            System.out.println("ERROR: Snake::buildSnake()");
        }
    }

    private boolean checkIfTheSameFields(@Nullable SnakeSegment bodySegment, @Nullable BreakingPoint breakingPoint)
    {
        boolean theSameFields = false;

        if ((null != bodySegment) && (null != breakingPoint))
        {
            theSameFields = (bodySegment.getXposition() == breakingPoint.getXposition()) && (bodySegment.getYposition() == breakingPoint.getYposition());
        }
        else
        {
            System.out.println("ERROR: Snake::checkIfTheSameFields()");
        }

        return theSameFields;
    }

    private boolean checkVerticalCollision(@Nullable SnakeSegment bodySegment, int headXCompensated, int headYCompensated, int segmentCompYpos)
    {
        boolean verticalCollision = false;

        if (null != bodySegment)
        {
            verticalCollision = ((bodySegment.getXposition() == headXCompensated) && (segmentCompYpos == headYCompensated));
        }
        else
        {
            System.out.println("ERROR: Snake::checkVerticalCollision()");
        }

        return verticalCollision;
    }

    private boolean checkHorizontalCollision(@Nullable SnakeSegment bodySegment, int headXCompensated, int headYCompensated, int segmentCompXpos)
    {
        boolean horizontalCollision = false;

        if (null != bodySegment)
        {
            horizontalCollision = ((segmentCompXpos == headXCompensated) && (bodySegment.getYposition() == headYCompensated));
        }
        else
        {
            System.out.println("ERROR: Snake::checkHorizontalCollision()");
        }

        return horizontalCollision;
    }

    private void notifyBPpool(SegmentAction action)
    {
        setChanged();
        notifyObservers(action);
        clearChanged();
    }

    private int getCurrentBodyFrame()
    {
        int currentFrame = SnakeSettings.NO_FRAME;

        if ((null != m_body) &&
                (m_body.size() >= (SnakeSettings.FIRST_BODY_SEGM_POSITION + 1)) )
        {
            // get frame of first body segment (one behind head)
            currentFrame = m_body.get(SnakeSettings.FIRST_BODY_SEGM_POSITION).getCurrentFrame();
        }

        return currentFrame;
    }
}
