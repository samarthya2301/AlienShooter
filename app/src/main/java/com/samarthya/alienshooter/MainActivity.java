package com.samarthya.alienshooter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    private boolean isMute;
    ImageView settings, ivExit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        findViewById(R.id.play).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, GameActivity.class)));

        TextView highScoreTxt = findViewById(R.id.highScoreTxt);

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);

        String sam = "HighScore: " + prefs.getInt("highscore", 0);
        highScoreTxt.setText(sam);

        isMute = prefs.getBoolean("isMute", false);

        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);

        if (isMute)
            volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
        else
            volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);

        volumeCtrl.setOnClickListener(view -> {

            isMute = !isMute;
            if (isMute)
                volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
            else
                volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isMute", isMute);
            editor.apply();

        });

        settings = findViewById(R.id.settings);
        ivExit = findViewById(R.id.ivExit);

        TextView tvSettings = findViewById(R.id.tvSettings);
        tvSettings.setVisibility(View.GONE);

        settings.setOnClickListener(v -> tvSettings.setVisibility(View.VISIBLE));
        ivExit.setOnClickListener(v -> finish());
    }
}
