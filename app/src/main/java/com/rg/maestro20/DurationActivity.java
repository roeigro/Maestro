package com.rg.maestro20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Vector;

public class DurationActivity extends Activity {

    WebView webTutorial;
    Button btnNext;
    Button btnTryagain;

    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration);

        btnNext = findViewById(R.id.btnNext);
        btnTryagain = findViewById(R.id.btnTryagain);


        mReference = FirebaseDatabase.getInstance().getReference("songs");

        Intent r = getIntent();
        final String name = r.getStringExtra("name");
        final String artist = r.getStringExtra("artist");
        final String tutorialUrl = r.getStringExtra("tutorialUrl");

        webTutorial = findViewById(R.id.webTutorial);
        WebSettings webSettings = webTutorial.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webTutorial.loadUrl(tutorialUrl);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSong(name, artist, tutorialUrl);
            }
        });
    }

    public void gobackSearch(View view) {
        finish();
    }


    public void saveSong(String name, String artist, String tutorialUrl) {
        String id = mReference.push().getKey();
        Song mSong = new Song(id, name, artist, tutorialUrl, 0);
        mReference.child(id).setValue(mSong);
        //Song Added Successfully!
        Toast.makeText(this, "Song Added Successfully!", Toast.LENGTH_SHORT).show();
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
