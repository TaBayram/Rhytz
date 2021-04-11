package tusba.rhytz.models;

public class MusicianInfo {
    String id,name,musicCount;

    public MusicianInfo(String id, String name, String musicCount) {
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
