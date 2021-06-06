package tusba.rhytz.models;

<<<<<<< HEAD:app/src/main/java/tusba/rhytz/models/Musician.java
public class Musician {
    public String id,name;

    public Musician(){}
    public Musician(String id, String name) {
=======
public class MusicianInfo {
    String id,name,musicCount;

    public MusicianInfo(String id, String name, String musicCount) {
>>>>>>> parent of 014f814 (v 0.1.3):app/src/main/java/tusba/rhytz/models/MusicianInfo.java
        this.id = id;
        this.name = name;
        this.musicCount = musicCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(String musicCount) {
        this.musicCount = musicCount;
    }
}
