package com.rg.maestro20;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {

    private Activity context;
    private List<Song> songsList;

    public SongAdapter(Activity context, List<Song> songsList) {
        super(context, R.layout.song, songsList);
        this.context = context;
        this.songsList = songsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.song, null, true);

        TextView txtName = listView.findViewById(R.id.txtName);
        TextView txtArtist = listView.findViewById(R.id.txtArtist);
        SeekBar sbProgress = listView.findViewById(R.id.sbProgress);
        TextView txtProgress = listView.findViewById(R.id.txtProgress);

        Song mSong = songsList.get(position);
        txtName.setText(mSong.getName());
        txtArtist.setText(mSong.getArtist());
        sbProgress.setEnabled(false);
        sbProgress.setProgress(mSong.getProgress());
        txtProgress.setText(mSong.getProgress() + " %");

        return listView;
    }
}
