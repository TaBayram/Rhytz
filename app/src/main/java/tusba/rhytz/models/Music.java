package tusba.rhytz.models;

import java.io.Serializable;

public class Music implements Serializable {

    public String id,albumId,categoryId,duration,musicianId,name,source;

    public Music(){}
    public Music(String id, String albumId, String categoryId, String duration, String musicianId, String name, String source) {
        this.id = id;
        this.albumId = albumId;
        this.categoryId = categoryId;
        this.duration = duration;
        this.musicianId = musicianId;
        this.name = name;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMusicianId() {
        return musicianId;
    }

    public void setMusicianId(String musicianId) {
        this.musicianId = musicianId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }




}
