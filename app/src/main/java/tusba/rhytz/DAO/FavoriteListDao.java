package tusba.rhytz.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tusba.rhytz.Entity.FavoriteList;

@Dao
public interface FavoriteListDao {

    @Insert
    void InsertFavorite(FavoriteList favorite);

    @Delete
    void DeleteFavorite(FavoriteList favorite);

    @Update
    void UpdateFavorite(FavoriteList favorite);

    @Query("Select *From FavoriteList")
    LiveData<List<FavoriteList>> getAllFavorites();

    @Query("Select *From FavoriteList Where FavoriteId = :id")
    LiveData<List<FavoriteList>> FindFavoriteById(int id);

}
