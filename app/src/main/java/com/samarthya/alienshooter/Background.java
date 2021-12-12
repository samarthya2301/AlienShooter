package com.samarthya.alienshooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background
{
    int x = 0, y = 0;
    Bitmap background;

    Background (int screenX, int screenY, Resources res)
    {
        background = BitmapFactory.decodeResource(res, R.drawable.background);      //extract from drawable
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);    //resize the background to fit in the entire screen
    }
}