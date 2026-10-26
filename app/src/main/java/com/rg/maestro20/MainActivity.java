package com.rg.maestro20;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    ListView listSongs;
    ImageView imgAdd;
    ImageView imgDelete;
    SeekBar sbProgress;

    DatabaseReference mReference;
    List<Song> songList;
    Song mSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listSongs = findViewById(R.id.listSongs);
        imgAdd = findViewById(R.id.imgAdd);
        imgDelete = findViewById(R.id.imgDelete);
        sbProgress = findViewById(R.id.sbProgress);

        imgAdd.setOnClickListener(imgAddListener);
        listSongs.setOnItemClickListener(itemClickListener);

        mReference = FirebaseDatabase.getInstance().getReference("songs");
        songList = new ArrayList<>();
        mSong = new Song();
    }

    @Override
    protected void onResume() {
        super.onResume();
        songList.clear();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()){
                    mSong = songSnapshot.getValue(Song.class);
                    songList.add(mSong);
                }
                SongAdapter songAdapter = new SongAdapter(MainActivity.this, songList);
                listSongs.setAdapter(songAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent m = new Intent(MainActivity.this, SessionActivity.class);
            m.putExtra("chosenID", songList.get(position).getId());
            startActivity(m);
        }
    };

    private View.OnClickListener imgAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gotoAdd();
        }
    };

    public void gotoAdd() {
        Intent i= new Intent(this, AddActivity.class);
        startActivity(i);
    }


}
