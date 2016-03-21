package com.example.jakub.snake2;

/**
 * Created by Jakub on 13.03.2016.
 */
public abstract class SnakeSegment extends Field implements IDrawable
{
    public SnakeSegment(int xPosition, int yPosition, Direction direction)
    {
        super(xPosition, yPosition, direction);
    }

    public abstract int getCurrentFrame();
}
