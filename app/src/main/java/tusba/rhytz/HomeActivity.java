package tusba.rhytz;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import tusba.rhytz.model.Music;
import tusba.rhytz.model.MusicAdapter;
import tusba.rhytz.ui.main.SectionsPagerAdapter;

public class HomeActivity extends SlideMenu {

    private static final int PERMISSION_REQUEST = 1;
    public ArrayList<Music> music;
    public View bottomPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
            else{
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        }
        else{
            Music();
        }




    }

    private void Music(){
        ArrayList<Music> musics = new ArrayList<Music>();
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;


        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";


        Cursor songCursor = contentResolver.query(songUri,null,selection,null,sortOrder);

        if(songCursor != null && songCursor.moveToFirst()){
            int idColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int albumColumn = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int artistColumn = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ARTIST);
            int artist2Column = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
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
                if(artist == null){
                    artist = songCursor.getString(artist2Column);
                }

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);


                musics.add(new Music(contentUri, name, duration, size, album, artist, title,genre));
            }while(songCursor.moveToNext());
            music = musics;
        }

        songUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        songCursor = contentResolver.query(songUri,null,null,null,null);

        if(songCursor != null && songCursor.moveToFirst()) {

            int albumColumn = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int artistColumn = songCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int artColumn = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

            do {
                String album = songCursor.getString(albumColumn);
                String artist = songCursor.getString(artistColumn);
                String albumArt = songCursor.getString(artColumn);

                for (Music song : musics) {
                    if (song.getAlbum().equals(album) && song.getArtist().equals(artist)) {
                        song.setAlbumArt(albumArt);
                    }
                }


            } while (songCursor.moveToNext());
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();

                        Music();
                    }
                    else{
                        Toast.makeText(this,"Permission Couldn't be Granted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return;
                }
            }
        }
    }
}
