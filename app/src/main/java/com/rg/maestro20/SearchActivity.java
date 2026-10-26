package com.rg.maestro20;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class SearchActivity extends Activity {

    Button btnGo;

    WebView webSearch;
    WebView webTutorial;

    Boolean userLeft = false;
    String tutorialID;
    String tutorialUrl;
    String name;
    String artist;
    String queryUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent f = getIntent();
        name = f.getStringExtra("name");
        artist = f.getStringExtra("artist");
        queryUrl = "https://www.youtube.com/results?search_query=" + name + " " + artist + " " + " tutorial";

        webSearch = findViewById(R.id.webSearch);
        webTutorial = findViewById(R.id.webTutorial);
        btnGo = findViewById(R.id.btnGo);

        webSearch.setVisibility(View.GONE);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoYoutube(queryUrl);
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (userLeft){
            userLeft = false;
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            String pasteData = "";

            // If it does contain data, decide if you can handle the data.
            if (!(clipboard.hasPrimaryClip())) {

            } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {

                // since the clipboard has data but it is not plain text

            } else {

                //since the clipboard contains plain text.
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                // Gets the clipboard as text.
                pasteData = item.getText().toString();
            }
            tutorialID = pasteData.length() > 11 ? pasteData.substring(pasteData.length() - 11) : pasteData;
            tutorialUrl = "https://www.youtube.com/embed/" + tutorialID;
            gotoDuration();
        }

    }

    public void gotoYoutube(String url) {
        userLeft = true;
        webSearch.loadUrl(url);
    }

    private void loadTutorial(String tutorialUrl) {
        WebSettings webSettings = webTutorial.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webTutorial.loadUrl(tutorialUrl);
    }

    private void gotoDuration() {
        Intent r = new Intent(SearchActivity.this, DurationActivity.class);
        r.putExtra("name", name);
        r.putExtra("artist", artist);
        r.putExtra("tutorialUrl", tutorialUrl);
        startActivity(r);
    }


}
