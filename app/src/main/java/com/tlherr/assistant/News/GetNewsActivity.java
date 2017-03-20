package com.tlherr.assistant.News;

import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;
import com.tlherr.assistant.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GetNewsActivity extends AppCompatActivity {

    private StoryCallback storyCallback = new StoryCallback();
    private ArrayList<String> headlines = new ArrayList<>();
    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_get_news);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        loadStories();
    }

    private void loadStories() {

        PkRSS.with(GetNewsActivity.this)
                .load("http://www.cbc.ca/cmlink/rss-topstories")
                .callback(storyCallback).async();
    }

    private void readNews() {
        this.tts = new TextToSpeech(GetNewsActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                    int i = 0;
                    String timeStamp = new SimpleDateFormat("E M w").format(new Date());
                    tts.speak("Top Headlines For "+timeStamp, TextToSpeech.QUEUE_FLUSH, null);

                    for (String headline : headlines) {
                        System.out.println(headline);
                        tts.speak(headline, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                finish();
            }
        });
    }


    private class StoryCallback implements Callback {

        private int randomStory(int min, int max ) {
            Random random = new Random();
            return random.nextInt(max - min + 1) + min;
        }

        @Override
        public void onPreload() {

        }

        @Override
        public void onLoaded(List<Article> articles) {
            System.out.println("Loaded Articles: "+articles.size());

            headlines.add(articles.get(randomStory(0, articles.size())).getTitle());
            headlines.add(articles.get(randomStory(0, articles.size())).getTitle());
            headlines.add(articles.get(randomStory(0, articles.size())).getTitle());
            readNews();
        }

        @Override
        public void onLoadFailed() {

        }
    }

}
