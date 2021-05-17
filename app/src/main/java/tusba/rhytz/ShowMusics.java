package tusba.rhytz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tusba.rhytz.models.Music;
import tusba.rhytz.models.MusicAdapter;

public class ShowMusics extends SlideMenu {

    ListView musicLV;
    MediaPlayer mediaPlayer;
    List<Music> musicList;

    public void ShowMusics(List<Music> musicList){
        this.musicList = musicList;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater= LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_show_musics,null,false);
        drawer.addView(v,0);

        musicLV = findViewById(R.id.musicLV);

        Intent i = getIntent();
        musicList = (List<Music>) i.getSerializableExtra("musicList");

        Log.i("","music source : " + musicList.get(0).source);
        SetMusicAdapter();

    }

    public void SetMusicAdapter(){
        //musicList.clear();
        //musicList.addAll(musicList);
        SetAdapter(musicList);
    }

    public void SetAdapter(List<Music> list){
        //Toast.makeText(getApplicationContext(),String.valueOf(list.size()),Toast.LENGTH_LONG).show();
        MusicAdapter adapter = new MusicAdapter(this,R.layout.music_layout,list);
        musicLV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void PlayMusic(Music music) throws IOException {

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setDataSource(music.getSource());

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        mediaPlayer.prepareAsync();
    }

}