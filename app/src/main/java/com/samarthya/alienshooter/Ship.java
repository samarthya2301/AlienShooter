package com.samarthya.alienshooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.samarthya.alienshooter.GameView.screenRatioX;
import static com.samarthya.alienshooter.GameView.screenRatioY;

public class Ship
{
    int toShoot = 0;
    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 0, shootCounter = 1;
    Bitmap Ship1, Ship2, shoot1, shoot2, shoot3, shoot4, shoot5, dead;
    private final GameView gameView;

    Ship (GameView gameView, int screenY, Resources res)
    {

        this.gameView = gameView;

        Ship1 = BitmapFactory.decodeResource(res, R.drawable.fly1);
        Ship2 = BitmapFactory.decodeResource(res, R.drawable.fly2);

        width = Ship1.getWidth();
        height = Ship1.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        Ship1 = Bitmap.createScaledBitmap(Ship1, width, height, false);
        Ship2 = Bitmap.createScaledBitmap(Ship2, width, height, false);

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1);
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5);

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false);

        dead = BitmapFactory.decodeResource(res, R.drawable.dead);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        y = screenY / 2;
        x = (int) (64 * screenRatioX);

    }

    Bitmap getShip () {

        if (toShoot != 0) {

            if (shootCounter == 1) {
                shootCounter++;
                return shoot1;
            }

            if (shootCounter == 2) {
                shootCounter++;
                return shoot2;
            }

            if (shootCounter == 3) {
                shootCounter++;
                return shoot3;
            }

            if (shootCounter == 4) {
                shootCounter++;
                return shoot4;
            }

            shootCounter = 1;
            toShoot--;
            gameView.newBullet();

            return shoot5;
        }

        if (wingCounter == 0) {
            wingCounter++;
            return Ship1;
        }
        wingCounter--;

        return Ship2;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead () {
        return dead;
    }

}
