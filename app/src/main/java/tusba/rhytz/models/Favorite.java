package tusba.rhytz.models;

public class Favorite {
    private int id;
    private String musician;
    private String music;

    public Favorite(){};
    public Favorite(int id, String musician, String music) {
        this.id = id;
        this.musician = musician;
        this.music = music;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMusician() {
        return musician;
    }

    public void setMusician(String musician) {
        this.musician = musician;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }
}
