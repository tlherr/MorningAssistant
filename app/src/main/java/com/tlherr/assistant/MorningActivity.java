package com.tlherr.assistant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tlherr.assistant.Service.Radio;
import com.tlherr.assistant.Task.TimedTask;
import com.tlherr.assistant.Task.TimedTaskCallbackInterface;
import com.tlherr.assistant.View.UI.CircularProgressBar;

import java.util.ArrayList;
import java.util.Locale;

/**
 * When started this activity will create sequential timers
 *
 * Timers will go as follows to create a ~15 min morning routine
 *
 * Wash Face (20s)
 * Clean Mouthguard(25s)
 * Brush Teeth (120s)
 * Floss Teeth (60s)
 * Shower (5 mins)
 * Deodorant (20s)
 * Shave (2 mins)
 * Weigh Self (25s)
 * Take Vitamins (20s)
 *
 * Complete
 */
public class MorningActivity extends AppCompatActivity {

    private ArrayList<TimedTask> tasks;
    private CircularProgressBar currentTaskTimer;
    private TextView txtStepNumber;
    private TextView txtTask;
    private ProgressBar progressBar;
    private int step = 0;
    private TextToSpeech ttsService;
    private Intent radioServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morning);

        this.currentTaskTimer = (CircularProgressBar) this.findViewById(R.id.currentTaskTimer);
        this.txtStepNumber = (TextView) this.findViewById(R.id.txtStepNumber);
        this.txtTask = (TextView) this.findViewById(R.id.txtTask);
        this.progressBar = (ProgressBar) this.findViewById(R.id.taskProgressBar);

        //Populate our tasks
        tasks = new ArrayList<>();

        tasks.add(new TimedTask("Wash Face", 20, currentTaskTimer));
        tasks.add(new TimedTask("Step on Scale", 25, currentTaskTimer));
        tasks.add(new TimedTask("Clean Mouthguard", 60, currentTaskTimer));
        tasks.add(new TimedTask("Brush Teeth", 120, currentTaskTimer));
        tasks.add(new TimedTask("Floss Teeth", 60, currentTaskTimer));
        tasks.add(new TimedTask("Enter Shower", 330, currentTaskTimer));
        tasks.add(new TimedTask("Apply Deodorant", 20, currentTaskTimer));
        tasks.add(new TimedTask("Shave", 120, currentTaskTimer));
        tasks.add(new TimedTask("Take Vitamins", 20, currentTaskTimer));

        this.progressBar.setMax(tasks.size()+1);

        Radio radio = Radio.getInstance();
        radio.start();

        this.ttsService = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    System.out.println("TTS Started");
                    ttsService.setLanguage(Locale.ENGLISH);
                    //Start the radio stream
                    doTask(0);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(ttsService != null){
            ttsService.stop();
            ttsService.shutdown();
        }
    }

    @Override
    protected void onPause() {

        if(ttsService != null){
            ttsService.stop();
            ttsService.shutdown();
        }
        super.onPause();
    }

    private void doTask(final int index) {
        this.step = index+1;
        this.txtStepNumber.setText(String.valueOf(this.step));

        //Update ProgressBar
        try {
            final TimedTask currentTask = tasks.get(index);
            this.txtTask.setText(currentTask.getName());
            this.ttsService.speak(currentTask.getName(), TextToSpeech.QUEUE_FLUSH, null);
            currentTask.start(new TimedTaskCallbackInterface() {
                @Override
                public void onTaskComplete() {
                    //Start the second task
                    System.out.println("Task Complete");
                    progressBar.setProgress(step);
                    currentTask.cancel();
                    doTask(index+1);
                }
            });
        } catch(IndexOutOfBoundsException boundsEx) {
            this.txtTask.setText("");
            this.txtStepNumber.setText("");
            this.step = 0;
            System.out.println("Ran out of tasks");
            this.ttsService.speak("Completed", TextToSpeech.QUEUE_FLUSH, null);
            this.ttsService.speak("Remember to track calories and take suppliments.", TextToSpeech.QUEUE_FLUSH, null);

            try {
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Radio radio = Radio.getInstance();
            radio.stop();

            Intent result = new Intent("com.tlherr.assistant.MORNING_COMPLETE", Uri.parse("content://result_uri"));
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

}
