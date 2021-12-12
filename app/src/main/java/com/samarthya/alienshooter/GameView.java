package com.samarthya.alienshooter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable
{

	private Thread thread;
	private boolean isPlaying, isGameOver = false;
	private final int screenX;
	private final int screenY;
	private int score = 0;
	public static float screenRatioX, screenRatioY;
	private final Paint paint;
	private final Alien[] aliens;
	private final SharedPreferences prefs;
	private final Random random;
	private final SoundPool soundPool;
	private final SoundPool explosion;
	private final List<Bullet> bullets;
	private final int sound;
	private final int explosion_sound;
	private final Ship Ship;
	private final GameActivity activity;
	private final Background background1;
	private final Background background2;

	public GameView(GameActivity activity, int screenX, int screenY)
	{
		super(activity);

		this.activity = activity;

		prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{

			AudioAttributes audioAttributes = new AudioAttributes.Builder()
					.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
					.setUsage(AudioAttributes.USAGE_GAME)
					.build();

			soundPool = new SoundPool.Builder()
					.setAudioAttributes(audioAttributes)
					.build();
			explosion = new SoundPool.Builder()
					.setAudioAttributes(audioAttributes)
					.build();

		}
		else {
			soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			explosion = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		}
		sound = soundPool.load(activity, R.raw.shoot, 1);
		explosion_sound = explosion.load(activity, R.raw.explosion, 1);

		this.screenX = screenX;
		this.screenY = screenY;
		screenRatioX = 1920f / screenX;
		screenRatioY = 1080f / screenY;

		background1 = new Background(screenX, screenY, getResources());
		background2 = new Background(screenX, screenY, getResources());

		Ship = new Ship(this, screenY, getResources());

		bullets = new ArrayList<>();

		background2.x = screenX;

		paint = new Paint();
		paint.setTextSize(128);
		paint.setColor(Color.WHITE);

		aliens = new Alien[4];

		for (int i = 0;i < 4;i++)
		{
			Alien alien = new Alien(getResources());
			aliens[i] = alien;
		}

		random = new Random();
	}

	@Override
	public void run()
	{
		while (isPlaying)
		{
			update ();
			draw ();
			sleep ();
		}
	}

	private void update ()
	{

		background1.x -= 8;// * screenRatioX;
		background2.x -= 8;// * screenRatioX;

		if (background1.x + background1.background.getWidth() < 0)
			background1.x = screenX;

		if (background2.x + background2.background.getWidth() < 0)
			background2.x = screenX;

		if (Ship.isGoingUp)
			Ship.y -= 30 * screenRatioY;
		else
			Ship.y += 30 * screenRatioY;

		if (Ship.y < 0)
			Ship.y = 0;

		if (Ship.y >= screenY - Ship.height)
			Ship.y = screenY - Ship.height;

		List<Bullet> trash = new ArrayList<>();

		for (Bullet bullet : bullets)
		{

			if (bullet.x > screenX)
				trash.add(bullet);

			bullet.x += 50 * screenRatioX;

			for (Alien alien : aliens)
			{
				if (Rect.intersects(alien.getCollisionShape(), bullet.getCollisionShape()))
				{
					score++;
					alien.x = -500;
					bullet.x = screenX + 500;
					alien.wasShot = true;
				}
			}
		}

		for (Bullet bullet : trash)
			bullets.remove(bullet);

		for (Alien alien : aliens)
		{
			alien.x -= alien.speed;

			if (alien.x + alien.width < 0)
			{

				if (!alien.wasShot)
				{
					isGameOver = true;
					return;
				}

				int bound = (int) (40 * screenRatioX);
				alien.speed = random.nextInt(bound);

				if (alien.speed < 20 * screenRatioX)
					alien.speed = (int) (20 * screenRatioX);

				alien.x = screenX;
				alien.y = random.nextInt(screenY - alien.height);

				alien.wasShot = false;
			}

			if (Rect.intersects(alien.getCollisionShape(), Ship.getCollisionShape()))
			{
				isGameOver = true;
				return;
			}
		}
	}

	private void draw ()
	{

		if (getHolder().getSurface().isValid()) //is the surfaceview instanstiated or not
		{
			Canvas canvas = getHolder().lockCanvas();	//returns the current canvas on the screen
			canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
			canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

			for (Alien alien : aliens)
				canvas.drawBitmap(alien.getAlien(), alien.x, alien.y, paint);

			canvas.drawText(score + "", screenX / 2f, 164, paint);


			if (isGameOver)
			{
				isPlaying = false;
				canvas.drawBitmap(Ship.getDead(), Ship.x, Ship.y, paint);
				getHolder().unlockCanvasAndPost(canvas);		//showing the obtained canvas on the screen

				if (!prefs.getBoolean("isMute", false))
					explosion.play(explosion_sound, 1, 1, 0, 0, 1);

				saveIfHighScore();
				waitBeforeExiting ();
				return;
			}

			canvas.drawBitmap(Ship.getShip(), Ship.x, Ship.y, paint);

			for (Bullet bullet : bullets)
				canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

			getHolder().unlockCanvasAndPost(canvas);
		}
	}

	private void waitBeforeExiting()
	{
		try
		{
			Thread.sleep(2000   );
			activity.startActivity(new Intent(activity, MainActivity.class));
			activity.finish();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	private void saveIfHighScore()
	{

		if (prefs.getInt("highscore", 0) < score)
		{
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("highscore", score);
			editor.apply();
		}
	}

	private void sleep ()
	{
		try
		{
			Thread.sleep(17);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void resume ()   //udf
	{
		isPlaying = true;
		thread = new Thread(this);
		thread.start();
	}

	public void pause ()
	{
		try
		{
			isPlaying = false;
			thread.join();	//terminates the thread
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if (event.getX() < screenX / 2.0)
					Ship.isGoingUp = true;
				break;

			case MotionEvent.ACTION_UP:
				Ship.isGoingUp = false;
				if (event.getX() > screenX / 2.0)
					Ship.toShoot++;
				break;
		}

		return true;
	}

	public void newBullet()
	{
		if (!prefs.getBoolean("isMute", false))
			soundPool.play(sound, 1, 1, 0, 0, 1);

		Bullet bullet = new Bullet(getResources());
		bullet.x = Ship.x + 2 * Ship.width/3;
		bullet.y = Ship.y + (Ship.height / 3);
		bullets.add(bullet);
	}
}