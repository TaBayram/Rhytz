package tusba.rhytz.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tusba.rhytz.DAO.FavoriteListDao;
import tusba.rhytz.Entity.FavoriteList;
import tusba.rhytz.RoomDB.FavoriteRoomDB;

public class FavoriteRepository {

    private FavoriteListDao favoriteDao;
    private LiveData<List<FavoriteList>> liveDataFavorite;

    public FavoriteRepository(Application application){
        FavoriteRoomDB db = FavoriteRoomDB.getDatabase(application);
        favoriteDao = db.getFavoritesDao();
        liveDataFavorite = favoriteDao.getAllFavorites();
    }

    LiveData<List<FavoriteList>> getAllFavorites(){
        return liveDataFavorite;
    }

    LiveData<List<FavoriteList>> FindFavoriteById(int id){
        return favoriteDao.FindFavoriteById(id);
    }

    void InsertFavorite(FavoriteList favorite){
        FavoriteRoomDB.databaseWriteExecutor.execute(() -> {
            favoriteDao.InsertFavorite(favorite);
        });
    }


    void DeleteFavorite(FavoriteList favorite){
        FavoriteRoomDB.databaseWriteExecutor.execute(() -> {
            favoriteDao.DeleteFavorite(favorite);
        });
    }


    void UpdateFavorite(FavoriteList favorite){
        FavoriteRoomDB.databaseWriteExecutor.execute(() -> {
            favoriteDao.UpdateFavorite(favorite);
        });
    }
}
