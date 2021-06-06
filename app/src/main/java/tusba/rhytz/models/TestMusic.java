package tusba.rhytz.models;

import android.net.Uri;

import java.io.Serializable;

public class TestMusic implements Serializable {

    public String id;
    public String albumId;
    public String categoryId;
    public String musicianId;
    public String source;
    public String name;
    public int duration;
    public int size;
    public String album;
    public String artist;
    public String title;
    public String genre;
    public String albumArt;


    public TestMusic(){}


    public TestMusic(String id, String albumId, String categoryId, int duration, String musicianId, String title, String source, String musicianName) {
        this.id = id;
        this.albumId = albumId;
        this.categoryId = categoryId;
        this.duration = duration;
        this.musicianId = musicianId;
        this.title = title;
        this.source = source;
        this.artist = musicianName;
    }




}
