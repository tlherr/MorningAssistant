package com.tlherr.assistant.Service;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;


public class Radio {
    private MediaPlayer mp;
    private static Radio ourInstance = new Radio();

    public static Radio getInstance() {
        return ourInstance;
    }

    private Radio() {
        mp = new MediaPlayer();
        mp.setLooping(false);
        mp.setAudioStreamType(AudioManager.STREAM_RING);
    }

    public void start() {
        try {
            mp.setDataSource("http://cbc_r1_tor.akacast.akamaistream.net/7/632/451661/v1/rc.akacast.akamaistream.net/cbc_r1_tor");
            mp.prepare();
            mp.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        mp.stop();
        mp.reset();
    }
}
