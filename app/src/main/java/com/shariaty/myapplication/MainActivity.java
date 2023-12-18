package com.shariaty.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView playerPosition, playerDuration;
    SeekBar seekBar;
    ImageView btnRew,btnPlay,btnPause,btnFf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnRew = findViewById(R.id.btnRew);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnFf = findViewById(R.id.btnFf);
        seekBar = findViewById(R.id.seekBar);
        playerPosition = findViewById(R.id.playerPosition);
        playerDuration = findViewById(R.id.playerDuration);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.music);
        Handler handler = new Handler();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };

        int duration = mediaPlayer.getDuration();
        String sDuration = convertFormat(duration);
        playerDuration.setText(sDuration);
        seekBar.setMax(mediaPlayer.getDuration());

        //play and pause buttons
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPlay.setVisibility(view.GONE);
                btnPause.setVisibility(view.VISIBLE);
                mediaPlayer.start();
                handler.postDelayed(runnable, 0);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPause.setVisibility(view.GONE);
                btnPlay.setVisibility(view.VISIBLE);
                mediaPlayer.pause();
                handler.removeCallbacks(runnable);
            }
        });

        //ff and rew buttons
        btnFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currenrPosition=mediaPlayer.getCurrentPosition();
                int duration=mediaPlayer.getDuration();
                if(mediaPlayer.isPlaying() && currenrPosition != duration)
                {
                    currenrPosition+=5000;
                    mediaPlayer.seekTo(currenrPosition);
                    playerPosition.setText(convertFormat(currenrPosition));
                }
            }
        });

        btnRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currenrPosition=mediaPlayer.getCurrentPosition();
                if(mediaPlayer.isPlaying() && currenrPosition >= 5000)
                {
                    currenrPosition-=5000;
                    mediaPlayer.seekTo(currenrPosition);
                    playerPosition.setText(convertFormat(currenrPosition));
                }
            }
        });

        //seekbar adjustment
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    mediaPlayer.seekTo(progress);
                }
                playerPosition.setText(convertFormat(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnPause.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(0);
            }
        });
    }

    private String convertFormat(int duration) {
            return String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));


    }
}

