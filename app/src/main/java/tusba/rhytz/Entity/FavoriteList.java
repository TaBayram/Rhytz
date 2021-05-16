package tusba.rhytz.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import tusba.rhytz.RoomDB.FavoriteRoomDB;

@Entity(tableName = "FavoriteList")
public class FavoriteList {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    int FavoriteId;

    @NonNull
    @ColumnInfo(name = "Musician_Id")
    String MusicianId;

    @NonNull
    @ColumnInfo(name = "Music_Id")
    String Music_Id;

    public FavoriteList(){}
    public FavoriteList(int favoriteId, @NonNull String musicianId, @NonNull String music_Id) {
        FavoriteId = favoriteId;
        MusicianId = musicianId;
        Music_Id = music_Id;
    }

    public int getFavoriteId() {
        return FavoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        FavoriteId = favoriteId;
    }

    @NonNull
    public String getMusicianId() {
        return MusicianId;
    }

    public void setMusicianId(@NonNull String musicianId) {
        MusicianId = musicianId;
    }

    @NonNull
    public String getMusic_Id() {
        return Music_Id;
    }

    public void setMusic_Id(@NonNull String music_Id) {
        Music_Id = music_Id;
    }
}
