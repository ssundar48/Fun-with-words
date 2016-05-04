package com.example.ssundar.funwithwords;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ssundar on 5/3/2016.
 */
public class file_display extends Activity{

    Button start;
    EditText number;
    private String rootDir = "/sdcard/";
    private String text = null;
    private int stop = 0;
    TextToSpeech tts;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);
        start = (Button)this.findViewById(R.id.button);
        number = (EditText)this.findViewById(R.id.wordNumber);
        Intent intent = getIntent();
        String message = intent.getStringExtra("txtFile");
        LinearLayout lView = new LinearLayout(this);
        File file = new File(rootDir + "/" + message);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        String lines = "";
        String lineSplit[];
        String printLines = "";
        int lineNumber = 0;
        try {
            while ((line = br.readLine()) != null) {
                lineNumber++;
                lineSplit = line.split(",", 2);
                lines += lineSplit[0] + ",means," + lineSplit[1] + "\n";
                printLines += lineNumber + ". " + lineSplit[0] + "\t-\t" + lineSplit[1] + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView myText = (TextView) findViewById(R.id.myText);
        myText.setTextSize((float) 22.0);
        myText.setMovementMethod(new ScrollingMovementMethod());
        myText.setText(printLines);
        text = lines;

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textSplit[] = text.split("\n");
                int wordNumber = 1;
                try {
                    wordNumber = new Integer(number.getText().toString()).intValue();
                } catch (Exception e) {
                }
                System.out.println(wordNumber);
                for (int i = wordNumber - 1;i < textSplit.length && i >= 0;i++) {
                    tts.speak(textSplit[i], TextToSpeech.QUEUE_ADD, null);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }
}
