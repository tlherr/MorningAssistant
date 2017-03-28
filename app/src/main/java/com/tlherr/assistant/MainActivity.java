package com.tlherr.assistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tlherr.assistant.Service.Radio;

/**
 * This android program is designed to keep mornings on track. It will do the following things
 *
 * Set an alarm based on the time I went to sleep at to try and get maxiumum amount of sleep time
 * (Eventually this will get a message from an arduino that can tell when I am in bed)
 * Detect the proximity of my fitbit to tell when I am in the room
 * Time events when I wake up, such as brushing teeth to keep me on schedule
 *
 * While performing morning functions will read news headlines, remind me of my schedule for the day
 * and play music while in the shower, read weather for the day.
 *
 * http://dd.weather.gc.ca/citypage_weather/xml/ON/s0000415_e.xml
 *
 * Will take a picture of face for progression picture
 *
 * At night will remind to enter in caloric intake into fitbit app
 *
 */
public class MainActivity extends AppCompatActivity {

    private Button startMorningButton;
    private Button startRadioButton;
    private Button stopRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startMorningButton = (Button) this.findViewById(R.id.startMorningBtn);
        startMorningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MorningActivity.class);
                startActivity(intent);
            }
        });

        startRadioButton = (Button) this.findViewById(R.id.startRadioButton);
        startRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Radio radio = Radio.getInstance();
                radio.start();
            }
        });

        stopRadioButton = (Button) this.findViewById(R.id.stopRadioButton);
        stopRadioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Radio radio = Radio.getInstance();
                radio.stop();
            }
        });


    }
}
