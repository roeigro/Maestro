package com.rg.maestro20;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SessionActivity extends YouTubeBaseActivity {

    String tutorialUrl;
    Integer duration = 0, current = 0, progress = 0;

    Boolean pauseFlag = false;

    Song mSong;

    TextView txtWelcome, txtArtist, txtProgress, txtEdit, txtDelete;
    EditText etName, etArtist;
    SeekBar sbProgress;
    ImageView imgEdit, imgDelete;
    Button btnSaveChanges;

    DatabaseReference mReference;

    YouTubePlayerView mYoutubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    YouTubePlayer.OnFullscreenListener mOnFullscreenListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        mReference = FirebaseDatabase.getInstance().getReference("songs");

        mYoutubePlayerView = findViewById(R.id.youtubePlay);
        txtWelcome = findViewById(R.id.txtWelcome);
        etName = findViewById(R.id.etName);
        etArtist = findViewById(R.id.etArtist);
        txtArtist = findViewById(R.id.txtArtist);
        txtProgress = findViewById(R.id.txtProgress);
        sbProgress = findViewById(R.id.sbProgress);
        imgEdit = findViewById(R.id.imgEdit);
        imgDelete = findViewById(R.id.imgDelete);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        txtEdit = findViewById(R.id.txtEdit);
        txtDelete = findViewById(R.id.txtDelete);

        etName.setEnabled(false);
        etArtist.setEnabled(false);
        sbProgress.setEnabled(false);
        btnSaveChanges.setVisibility(View.GONE);

        Intent i = getIntent();
        final String id = i.getStringExtra("chosenID");

        imgEdit.setOnClickListener(imgEditListener);
        imgDelete.setOnClickListener(imgDeleteListener);

        //Firebase
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    if (songSnapshot.getValue(Song.class).getId().equals(id)) {
                        Song mySong = songSnapshot.getValue(Song.class);
                        mSong = mySong;

                        loadSongDetails(mSong);
                        mYoutubePlayerView.initialize(YoutubeConfig.getApiKey(), mOnInitializedListener);//PLAY
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Youtube
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(tutorialUrl.length() > 11 ? tutorialUrl.substring(tutorialUrl.length() - 11) : tutorialUrl);

                youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(String s) {
                        duration = (int)(youTubePlayer.getDurationMillis()*0.001);
                        mSong.setDuration(duration);
                        youTubePlayer.seekToMillis((int)((mSong.getDuration() * mSong.getProgress() * 0.01)*1000));
                        youTubePlayer.pause();
                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {

                    }

                    @Override
                    public void onVideoEnded() {

                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {

                    }
                });
                youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPlaying() {
                        pauseFlag = true;
                    }

                    @Override
                    public void onPaused() {
                        if (pauseFlag) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(SessionActivity.this);
                            builder.setMessage("Have you finished your session?");
                            builder.setIcon(R.drawable.question);
                            builder.setPositiveButton("Yes, Save Progress", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    current = (int) (youTubePlayer.getCurrentTimeMillis() * 0.001);
                                    updateProgress(current);
                                    finish();
                                }
                            });
                            builder.setNegativeButton("No, Not Yet", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog mDialog = builder.create();
                            mDialog.show();
                        }
                    }

                    @Override
                    public void onStopped() {

                    }

                    @Override
                    public void onBuffering(boolean b) {

                    }

                    @Override
                    public void onSeekTo(int i) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) { }
        };
        mOnFullscreenListener = new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                txtWelcome.setVisibility(View.GONE);
                txtDelete.setVisibility(View.GONE);
                txtEdit.setVisibility(View.GONE);
                imgDelete.setVisibility(View.GONE);
                imgEdit.setVisibility(View.GONE);
            }
        };



    }

    private void updateProgress(final Integer current) {
        double temp = ((double)current / (double)duration * 100);
        progress = (int)(temp);
        mSong.setProgress(progress);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    if (songSnapshot.getValue(Song.class).getId().equals(mSong.getId())) {
                        mReference.child(mSong.getId()).setValue(mSong);
                        sbProgress.setProgress(progress);
                        txtProgress.setText(progress + " %");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void convertDuration(Integer dur) {
        txtArtist.setText(dur.toString());
    }

    private void loadSongDetails(Song mSong) {
        etName.setText(mSong.getName());
        etArtist.setText(mSong.getArtist());
        tutorialUrl = mSong.getTutorialUrl();
        sbProgress.setProgress(mSong.getProgress());
        txtProgress.setText(mSong.getProgress() + " %");
    }

    //Edit Song Details
    private View.OnClickListener imgEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            etName.setEnabled(true);
            etArtist.setEnabled(true);
            imgEdit.setVisibility(View.GONE);
            txtEdit.setVisibility(View.GONE);
            imgDelete.setVisibility(View.GONE);
            txtDelete.setVisibility(View.GONE);
            btnSaveChanges.setVisibility(View.VISIBLE);
            btnSaveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!etName.getText().toString().equals("") && !etArtist.getText().toString().equals("")) {
                        etName.setEnabled(false);
                        etArtist.setEnabled(false);
                        mSong.setName(etName.getText().toString());
                        mSong.setArtist(etArtist.getText().toString());
                        mReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                                    if (songSnapshot.getValue(Song.class).getId().equals(mSong.getId())) {
                                        mReference.child(mSong.getId()).setValue(mSong);
                                        Toast.makeText(SessionActivity.this, "Song Updated Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                        Toast.makeText(SessionActivity.this, "Please don't skip fields. Thank you!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    //Delete Song
    private View.OnClickListener imgDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SessionActivity.this);
            builder.setMessage("Are you sure you want to delete this song including your progress?");
            builder.setIcon(R.drawable.icontrash);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mReference.child(mSong.getId()).removeValue();
                    Toast.makeText(SessionActivity.this, "Song Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            AlertDialog mDialog = builder.create();
            mDialog.show();
        }
    };
}