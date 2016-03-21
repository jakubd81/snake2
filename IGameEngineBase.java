package com.example.jakub.snake2;

/**
 * Created by Jakub on 07.02.2016.
 */
public interface IGameEngineBase
{
    void init();
    // returns false if game over
    boolean update(Field.Direction direction);
    int getFinalScore();
}
