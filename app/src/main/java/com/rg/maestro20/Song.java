package com.rg.maestro20;

public class Song {

    public String id;
    public String name;
    public String artist;
    public String tutorialUrl;
    public Integer duration;
    public Integer progress;

    public Song() {
    }



    public Song(String id, String name, String artist, String tutorialUrl, Integer duration) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.tutorialUrl = tutorialUrl;
        this.duration = duration;
        this.progress = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getTutorialUrl() {
        return tutorialUrl;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getProgress() { return progress; }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTutorialUrl(String tutorialUrl) {
        this.tutorialUrl = tutorialUrl;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setProgress(Integer progress) { this.progress = progress; }

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", tutorialUrl='" + tutorialUrl + '\'' +
                ", duration=" + duration +
                ", progress=" + progress +
                '}';
    }
}