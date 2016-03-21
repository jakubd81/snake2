package com.example.jakub.snake2;

import android.graphics.Bitmap;

/**
 * Created by Jakub on 13.03.2016.
 */
public interface IFrameAnimationHandler
{
    // returns null if no frames left
    Bitmap getCurrentFrameGfx(Field.Direction headDirection);
    int getCurrentFrame();

    int FIRST_FRAME = 0;
}
