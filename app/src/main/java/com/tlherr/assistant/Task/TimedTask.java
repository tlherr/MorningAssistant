package com.tlherr.assistant.Task;


import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.ActionMode;

import com.tlherr.assistant.View.UI.CircularProgressBar;

import java.util.Locale;

public class TimedTask {

    private int duration;
    private String name;
    private CircularProgressBar taskTimer;
    private CountDownTimer mTimerCountDown;


    public TimedTask(String name, int duration, CircularProgressBar taskTimer) {
        this.name = name;
        this.duration = duration;
        this.taskTimer = taskTimer;
    }

    public void start(final TimedTaskCallbackInterface callback) {
        System.out.println("Running Start for: "+ this.name);
        this.taskTimer.setMax(duration);
        this.mTimerCountDown = new CountDownTimer(duration * 1000, 1000) {
            @Override
            public void onTick(final long millisUntilFinished) {
                final int secondsRemaining = (int) (millisUntilFinished / 1000);
                //Fire a callback
                taskTimer.setProgress(secondsRemaining);
            }

            @Override
            public void onFinish() {
                taskTimer.setProgress(0);
                callback.onTaskComplete();
            }
        }.start();
    }

    public void cancel() {
        if (this.mTimerCountDown != null)
        {
            this.mTimerCountDown.cancel();
        }
    }

    public String getName() {
        return name;
    }
}
