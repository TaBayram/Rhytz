package tusba.rhytz.RoomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tusba.rhytz.DAO.FavoriteListDao;
import tusba.rhytz.Entity.FavoriteList;

@Database(entities = {FavoriteList.class}, version = 1, exportSchema = false)
public abstract class FavoriteRoomDB extends RoomDatabase {

    public abstract FavoriteListDao getFavoritesDao();

    private static FavoriteRoomDB INSTANCE;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(5);

    public static FavoriteRoomDB getDatabase(final Context context) {
        //singleton yapısı
        if (INSTANCE == null) {
            synchronized (FavoriteRoomDB.class){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteRoomDB.class, "frd")
                        //.allowMainThreadQueries() //yapılması uygun değil
                        .build();
            }
        }
        return INSTANCE;
    }
}
