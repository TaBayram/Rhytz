package tusba.rhytz;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import tusba.rhytz.model.Music;


public class MusicPlayer extends AppCompatActivity {

    TextView textViewSongName ;
    TextView textViewSingerName ;
    TextView textViewCurrentPlace ;
    TextView textViewDuration ;
    Button buttonPausePlay;
    MediaPlayer mediaPlayer;
    SeekBar seekBarMusic;
    boolean isMediaPlayerReady = false;
    Music music;



    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        Bundle bundle = getIntent().getExtras();
        music =(Music)bundle.getSerializable("song");

        /*
        Intent intent = new Intent(this, MusicPlayer.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList();*/
        

        textViewSongName = findViewById(R.id.textViewSongName);
        textViewSingerName = findViewById(R.id.textViewSingerName);
        textViewCurrentPlace = findViewById(R.id.textViewCurrentPlace);
        textViewDuration = findViewById(R.id.textViewDuration);
        buttonPausePlay = findViewById(R.id.buttonPausePlay);
        seekBarMusic = findViewById(R.id.seekBarMusic);

        textViewSongName.setText(music.getTitle());
        textViewSingerName.setText(music.getArtist());

        mediaPlayer = MediaPlayer.create(this, music.getUri());

       AttachListeners();

    }

    private void AttachListeners(){
        AttachMediaPlayerListeners();
        AttachViewListeners();
    }

    private void AttachMediaPlayerListeners(){
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                SetDetails();
                textViewDuration.setText(GetTimeWithMiliSecond(mediaPlayer.getDuration()));
                isMediaPlayerReady = true;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                textViewCurrentPlace.setText(GetTimeWithMiliSecond(mp.getCurrentPosition()));
                                seekBarMusic.setProgress(mp.getCurrentPosition()*100/mp.getDuration());
                            }
                        });

                    }
                },50,500);
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Toast.makeText(getParent(), "nh", Toast.LENGTH_SHORT).show();
                seekBarMusic.setProgress(percent);
                textViewCurrentPlace.setText(""+mp.getDuration()*percent/100);
            }
        });

        mediaPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(MediaPlayer mp, TimedText text) {
                System.out.println("hmm");
            }
        });

        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                textViewCurrentPlace.setText(GetTimeWithMiliSecond(mp.getCurrentPosition()));
            }
        });
    }

    private void AttachViewListeners(){
        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    int seekedMili = progress*mediaPlayer.getDuration()/100;
                    mediaPlayer.seekTo(seekedMili);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        buttonPausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMediaPlayerReady){
                    Toast.makeText(getParent(), "Not Prepared", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();

                    buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_play));
                }
                else{
                    mediaPlayer.start();

                    buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
                }
            }
        });

    }

    private String GetTimeWithMiliSecond(int milisecond){
        String time = "";
        int second = milisecond/1000;
        int min = second/60;
        int hour = min/60;
        second = second%60;
        min = min%60;


        if(hour > 0){
            if(hour < 10){
                time += "0"+hour;
            }
            else{
                time+=hour;
            }
            time += ":";
        }
        if(min < 10){
            time += "0"+min;
        }
        else{
            time += min;
        }
        time += ":";
        if(second < 10){
            time += "0"+second;
        }
        else{
            time += second;
        }





        return  time;

    }

    private void SetDetails() {
        /*
        File file = new File("D:\\Users\\Documents\\GitHub\\Rhytz\\app\\src\\main\\res\\raw\\fearinoculum.mp3");
        Uri uri = Uri.fromFile(file);
        System.out.println(uri.getPath());

        Cursor returnCursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        System.out.println(returnCursor);

        returnCursor.moveToFirst();


        for (int i = 0; i < 6; i++) {
            try {
                System.out.println(returnCursor.getString(i));

            } catch (Exception e) {
                throw e;
            }
            ;
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void Test() {


        List<Music> AudioList = new ArrayList<Music>();
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
       // collection = Environment.getExternalStorageDirectory().getAbsolutePath();


        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE
        };
        String selection = MediaStore.Audio.Media.DATA + " like ? ";
        String[] selectionArgs = new String[]{
                String.valueOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS))
        };
        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

        Cursor cursor = getApplicationContext().getContentResolver().query(
                collection,
                projection,
                selection,
                null,
                sortOrder
        );
        System.out.println(cursor.getCount());
        {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ARTIST);
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int genreColumn = cursor.getColumnIndex(MediaStore.Audio.Media.GENRE);


            while (cursor.moveToNext()) {
                // Get values of columns for a given Audio.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                int size = cursor.getInt(sizeColumn);
                String album = cursor.getString(albumColumn);
                String artist = cursor.getString(artistColumn);
                String title = cursor.getString(titleColumn);
                String genre = cursor.getString(genreColumn);


                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                AudioList.add(new Music(contentUri, name, duration, size, album, artist, title,genre));
            }
        }

        for (Music music: AudioList ) {
            System.out.println(music.getAlbum());
        }

    }


  /*  public ArrayList<File> Song(File file){
        ArrayList<File> filesArray = new ArrayList<>();

        File[] files = file.listFiles();

        for(File singleFile: files){

            if(singleFile.isDirectory()){
                filesArray.addAll(Song(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    filesArray.add(singleFile);
                }
            }

        }
        return  filesArray;
    }*/
}