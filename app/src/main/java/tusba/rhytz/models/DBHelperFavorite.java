package tusba.rhytz.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import tusba.rhytz.DAO.FavoriteListDao_Impl;

public class DBHelperFavorite extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FavoriteDB.db";

    private static final String TABLE_NAME = "FavoriteTable";
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_MUSICIAN = "Musician";
    private static final String COLUMN_MUSIC = "Music";

    private static final String SQL_CREATE_TABLE_FAVORITE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_MUSICIAN + " TEXT," +
                    COLUMN_MUSIC + " TEXT)";

    private static final String SQL_DELETE_TABLE_FAVORITE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHelperFavorite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {db.execSQL(SQL_CREATE_TABLE_FAVORITE);}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_TABLE_FAVORITE);
        onCreate(db);
    }

    public long FavoriteAdd(Favorite favorite){

        SQLiteDatabase db = this.getWritableDatabase();

        try{
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_MUSICIAN, favorite.getMusician());
            cv.put(COLUMN_MUSIC, favorite.getMusic());

            return db.insert(TABLE_NAME,null,cv);
        }
        catch (Exception ex){  return -1;}
        finally {db.close();}
    }

    public long FavoriteDelete(Favorite favorite){

        String id = String.valueOf(favorite.getId());
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            db.execSQL("delete from " + TABLE_NAME + " where ID = " + id);
            return 1;
        }
        catch (Exception ex){
            Log.i("hata",ex.getMessage()); return -1;}
        finally {db.close();}
    }

    public ArrayList<Favorite> FavoriteGetAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try{
            if(db==null) { throw new Exception("Database is not found");}

            String query = "Select * From " + TABLE_NAME ;
            cursor = db.rawQuery(query,null);

            ArrayList<Favorite> favorites = new ArrayList<>();
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String musician = cursor.getString(cursor.getColumnIndex(COLUMN_MUSICIAN));
                String music = cursor.getString(cursor.getColumnIndex(COLUMN_MUSIC));

                favorites.add(new Favorite(id,musician,music));
            }

            return favorites;
        }
        catch (Exception ex){ return null;}
        finally {db.close();}
    }
}
