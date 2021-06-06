package tusba.rhytz.models;

import android.net.Uri;

import java.io.Serializable;

public class Music implements Serializable {

    public String id = "undefined";
    public String albumId = "undefined";
    public String categoryId = "undefined";
    public String musicianId = "undefined";
    private String uri = "undefined";
    private String name = "undefined";
    private int duration = 0;
    private int size = 0;
    private String album = "undefined";
    private String artist = "undefined";
    private String title = "undefined";
    private String genre = "undefined";
    private String albumArt = "undefined";
    private boolean isFavorite = false;


    public Music(){}
    /**
     * DATABASE CONSTRUCTOR
     * */
    public Music(String id, String albumId, String categoryId, int duration, String musicianId, String title, String source, String musicianName) {
        this.id = id;
        this.albumId = albumId;
        this.categoryId = categoryId;
        this.duration = duration;
        this.musicianId = musicianId;
        this.title = title;
        this.uri = source;
        this.artist = musicianName;
    }

    /**
    * LOCAL CONSTRUCTOR
    * */
    public Music(Uri uri, String name, int duration, int size,String album, String artist, String title, String genre) {
        this.uri = uri.toString();
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.album = album;
        this.artist = artist;
        this.title = title;
        this.genre = genre;
        GenerateLocalID();
        CheckNulls();
    }

    public String getId() {        return id;    }

    public void setId(String id) {        this.id = id;    }

    public String getAlbumId() {        return albumId;    }

    public void setAlbumId(String albumId) {        this.albumId = albumId;    }

    public String getCategoryId() {        return categoryId;    }

    public void setCategoryId(String categoryId) {  this.categoryId = categoryId;   }

    public String getMusicianId() {    return musicianId;   }

    public void setMusicianId(String musicianId) {  this.musicianId = musicianId;   }

    public String getSource() {  return uri;   }

    public void setSource(String source) {   this.uri = source;  }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
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
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    /*---------------------------------------------------------------*/

    private void GenerateLocalID(){
        long longID = 0;
        for(int i = 0; i < this.name.length(); i++){
            char ch = this.name.charAt(i);
            longID += (int)ch;
        }
        this.id = ""+Long.toString(longID+this.size,36);
    }

    public String getDurationFormatted(){
        String time = "";
        int second = duration/1000;
        int min = second/60;
        int hour = min/60;
        second = second%60;
        min = min%60;


        if(hour > 0){
            if(hour < 10){
                time += "0"+hour;
            }
            else{
                time+=hour;
            }
            time += ":";
        }
        if(min < 10){
            time += "0"+min;
        }
        else{
            time += min;
        }
        time += ":";
        if(second < 10){
            time += "0"+second;
        }
        else{
            time += second;
        }
        return  time;
    }

    public void CheckNulls(){
        if(this.title == null){
            this.title = "undefined";
        }
        if(this.album == null){
            this.album = "undefined";
        }
        if(this.artist == null){
            this.artist = "undefined";
        }
        if(this.genre == null){
            this.genre = "undefined";
        }
        if(this.albumArt == null){
            this.albumArt = "undefined";
        }
        if(this.name == null){
            this.name = "undefined";
        }
    }

    public Uri GetUri() {
        return Uri.parse(uri);
    }

    public void SetUri(Uri uri) {
        this.uri = uri.toString();
    }

}
