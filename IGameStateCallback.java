package com.example.jakub.snake2;

/**
 * Created by Jakub on 07.02.2016.
 */
public interface IGameStateCallback
{
    void frameUpdated();
    void gameOver(int finalScore, boolean finishedByUser);
}

