package edu.cmu.lyricfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = findViewById(R.id.searchButton);
        EditText songName = findViewById(R.id.songName);
        EditText artistName = findViewById(R.id.artistName);

        songName.requestFocus();

        songName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i < s.length(); i++) {
                    if (!Character.isLetterOrDigit(s.charAt(i))) {
                        songName.setError("Invalid Song Name");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        artistName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i < s.length(); i++) {
                    if (!Character.isLetterOrDigit(s.charAt(i))) {
                        artistName.setError("Invalid Artist Name");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songName.length() == 0) {
                    songName.setError("Song Name Cannot Be Empty");
                }
                if (artistName.length() == 0) {
                    artistName.setError("Artist Name Cannot Be Empty");
                }
                if (songName.getError() == null && artistName.getError() == null && songName.length() != 0 && artistName.length() != 0) {
                    String song = songName.getText().toString();
                    String artist = artistName.getText().toString();
                    searchLyrics(song, artist);
                }
            }
        });
    }

    private void searchLyrics(String songName, String artistName){
        String apiKey = "4794d71e3256361ba03a768912556bc9";
        String url = "https://api.vagalume.com.br/search.php?art="+artistName+"&mus="+songName+"&apikey="+apiKey;
//        String url = "http://localhost:8080/getLyrics?artistName="+artistName+"&songName="+songName+"&phoneBrand="+Build.BRAND+"&phoneDevice="+Build.DEVICE;

        RequestQueue volleyQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                // we are using GET HTTP request method
                Request.Method.GET,
                // url we want to send the HTTP request to
                url,
                // this parameter is used to send a JSON object to the
                // server, since this is not required in our case,
                // we are keeping it `null`
                null,

                // lambda function for handling the case
                // when the HTTP request succeeds
                (Response.Listener<JSONObject>) response -> {
                    // get the image url from the JSON object
                    String lyrics;
                    String song;
                    String artist;
                    try {
                        JSONArray music = response.getJSONArray("mus");
                        JSONObject artistInfo = response.getJSONObject("art");
                        JSONObject musicInfo = music.getJSONObject(0);
                        lyrics = musicInfo.getString("text");
                        song = musicInfo.getString("name");
                        artist = artistInfo.getString("name");
                        Intent i = new Intent(MainActivity.this, MainActivity2.class);
                        i.putExtra("lyrics",lyrics);
                        i.putExtra("song",song);
                        i.putExtra("artist",artist);
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                // lambda function for handling the case
                // when the HTTP request fails
                (Response.ErrorListener) error -> {
                    // make a Toast telling the user
                    // that something went wrong
                    Toast.makeText(MainActivity.this, "Some error occurred! Cannot fetch song lyrics", Toast.LENGTH_LONG).show();
                    // log the error message in the error stream
                    Log.e("MainActivity", "searchLyrics error: ${error.localizedMessage}");
                }
        );

        volleyQueue.add(jsonObjectRequest);

    }


}