package tusba.rhytz.model;


import android.net.Uri;

import java.io.Serializable;

public class Music implements Serializable {
    public Uri getUri() {
        return Uri.parse(uri);
    }

    public void setUri(Uri uri) {
        this.uri = uri.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    private String uri;
    private String name;
    private int duration;
    private int size;
    private String album;
    private String artist;
    private String title;
    private String Genre;

    public Music(Uri uri, String name, int duration, int size,String album, String artist, String title, String Genre) {
        this.uri = uri.toString();
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.album = album;
        this.artist = artist;
        this.title = title;
    }
}


