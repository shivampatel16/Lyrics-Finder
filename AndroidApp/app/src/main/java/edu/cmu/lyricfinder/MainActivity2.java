package edu.cmu.lyricfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;




public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView titleView = findViewById(R.id.title);
        Intent i = getIntent();
        String titleStr = i.getStringExtra("song")+" by "+i.getStringExtra("artist");
        titleView.setText(titleStr);
        TextView lyricsView = findViewById(R.id.lyrics);
        lyricsView.setText(i.getStringExtra("lyrics"));
    }
}