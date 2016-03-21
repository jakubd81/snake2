package com.example.jakub.snake2;


import android.graphics.Canvas;

/**
 * Created by Jakub on 08.01.2016.
 */
public interface IGameClient
{
    void init();
    void startGame();
    void pauseGame();
    void unPauseGame();
    void stopGame();
    void drawFrame(Canvas canvas);
    void setSelectedDirection(Field.Direction selectedDirection);
}

