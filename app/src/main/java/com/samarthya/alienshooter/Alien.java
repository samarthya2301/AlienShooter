package com.samarthya.alienshooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.samarthya.alienshooter.GameView.screenRatioX;
import static com.samarthya.alienshooter.GameView.screenRatioY;

public class Alien
{

    public int speed = 30;
    public boolean wasShot = true;
    int x = 0, y, width, height, alienCounter = 1;
    Bitmap alien1, alien2, alien3, alien4;

    Alien(Resources res)
    {
        alien1 = BitmapFactory.decodeResource(res, R.drawable.alien1);  //extracting and placing the alien spaceships in view
        alien2 = BitmapFactory.decodeResource(res, R.drawable.alien2);  //BitmapFactory is a class that is used to load large bitmaps efficiently
        alien3 = BitmapFactory.decodeResource(res, R.drawable.alien3);      //into the application
        alien4 = BitmapFactory.decodeResource(res, R.drawable.alien4);  //the method that is used is also the best in terms of efficiency

        width = alien1.getWidth();
        height = alien1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        alien1 = Bitmap.createScaledBitmap(alien1, width, height, false);//this method creates a new bitmap, when possible scales from the existing
        alien2 = Bitmap.createScaledBitmap(alien2, width, height, false);
        alien3 = Bitmap.createScaledBitmap(alien3, width, height, false);
        alien4 = Bitmap.createScaledBitmap(alien4, width, height, false);

        y = -height;
    }

    Bitmap getAlien ()
    {             //this method keeps on creating new alien spaceships, and puts an animation into them

        if (alienCounter == 1)
        {
            alienCounter++;
            return alien1;
        }

        if (alienCounter == 2)
        {
            alienCounter++;
            return alien2;
        }

        if (alienCounter == 3)
        {
            alienCounter++;
            return alien3;
        }

        alienCounter = 1;

        return alien4;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}