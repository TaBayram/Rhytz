package tusba.rhytz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;

import tusba.rhytz.model.Music;
import tusba.rhytz.model.MusicAdapter;

public class MusicLibrary extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 1;
    RecyclerView recyclerViewSongs;
    ArrayList<Music> music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_library);

        recyclerViewSongs = findViewById(R.id.recyclerViewFragmentSongs);



        if(ContextCompat.checkSelfPermission(MusicLibrary.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MusicLibrary.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MusicLibrary.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
            else{
                ActivityCompat.requestPermissions(MusicLibrary.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        }
        else{
            Music();
        }

        MusicAdapter musicAdapter = new MusicAdapter(this,music);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewSongs.setLayoutManager(layoutManager);
        recyclerViewSongs.setAdapter(musicAdapter);


    }
    private void Music(){
        ArrayList<Music> musics = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(songUri,null,null,null,null);

        if(songCursor != null && songCursor.moveToFirst()){
            int idColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int albumColumn = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int artistColumn = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ARTIST);
            int titleColumn = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int genreColumn = songCursor.getColumnIndex(MediaStore.Audio.Media.GENRE);

            do{
                long id = songCursor.getLong(idColumn);
                String name = songCursor.getString(nameColumn);
                int duration = songCursor.getInt(durationColumn);
                int size = songCursor.getInt(sizeColumn);
                String album = songCursor.getString(albumColumn);
                String artist = songCursor.getString(artistColumn);
                String title = songCursor.getString(titleColumn);
                String genre = songCursor.getString(genreColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);


                musics.add(new Music(contentUri, name, duration, size, album, artist, title,genre));
                System.out.println(name);
            }while(songCursor.moveToNext());

            music = musics;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MusicLibrary.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                        Music();
                    }
                    else{
                        Toast.makeText(this,"Permission Couldn't be Granted",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return;
                }
            }
        }
    }
}