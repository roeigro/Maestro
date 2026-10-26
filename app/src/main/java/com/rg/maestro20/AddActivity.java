package com.rg.maestro20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends Activity {

    EditText etName;
    EditText etArtist;
    Button btnNext;

    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mReference = FirebaseDatabase.getInstance().getReference("songs");

        etName = findViewById(R.id.etName);
        etArtist = findViewById(R.id.etArtist);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearch();
            }
        });
    }

    private void gotoSearch() {
        String name = etName.getText().toString();
        String artist = etArtist.getText().toString();
        if (!name.equals("") && !artist.equals("")) {
            Intent f = new Intent(AddActivity.this, SearchActivity.class);
            f.putExtra("name", name);
            f.putExtra("artist", artist);
            startActivity(f);
        }
        else
            Toast.makeText(this, "Please don't skip fields. Thank you!", Toast.LENGTH_SHORT).show();
    }
}
