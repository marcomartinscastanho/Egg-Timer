package com.martinscastanho.marco.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Integer maxTimer = 10*60*1000; // in milliseconds
    Integer minTimer = 10*1000; //in milliseconds
    Boolean isRunning = false;
    Integer countdownStartTime = 30 * 1000;
    CountDownTimer timer;
    TextView timerText;
    SeekBar timerSetBar;
    Button startStopButton;

    public void updateTimerText(Long time){
        Long timeMinutes = (time/1000) / 60;
        Long timeSeconds = (time/1000) % 60;
        timerText.setText(String.format("%02d:%02d", timeMinutes, timeSeconds));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startStopButton = findViewById(R.id.startStopButton);

        timerText = findViewById(R.id.timerText);
        updateTimerText(Long.valueOf(countdownStartTime));

        timerSetBar = findViewById(R.id.timerSetBar);
        timerSetBar.setMax(maxTimer);
        timerSetBar.setProgress(countdownStartTime);
        timerSetBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Integer timerSetTime;
                if(i < minTimer){
                    timerSetTime = minTimer;
                    timerSetBar.setProgress(minTimer);
                }
                else {
                    timerSetTime = i;
                }
                countdownStartTime=timerSetTime;
                updateTimerText(Long.valueOf(countdownStartTime));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void stopTimer(){
        timer.cancel();
        startStopButton.setText("Go!");

        countdownStartTime = minTimer;
        updateTimerText(Long.valueOf(countdownStartTime));
        timerSetBar.setProgress(countdownStartTime);

        timerSetBar.setEnabled(true);
        isRunning = false;
    }

    public void startTimer(){
        timer = new CountDownTimer(countdownStartTime, 1000) {
            @Override
            public void onTick(long millisecondsUntilDone) {
                updateTimerText(Long.valueOf(millisecondsUntilDone));
            }

            @Override
            public void onFinish() {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.horn);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        stopTimer();
                    }
                });
            }
        }.start();
        startStopButton.setText("Stop");
        timerSetBar.setEnabled(false);
        isRunning = true;
    }

    public void startStop(View view){
        if(isRunning){
            stopTimer();
        }
        else {
            startTimer();
        }
    }
}
