package tusba.rhytz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import java.util.Hashtable;
import java.util.List;

import tusba.rhytz.models.FirebaseClass;
import tusba.rhytz.models.FirebaseInterface;
import tusba.rhytz.models.Music;
import tusba.rhytz.models.MusicAdapter;
import tusba.rhytz.models.Musician;
import tusba.rhytz.models.User;

public class ShowMusics extends SlideMenu implements FirebaseInterface {

    FirebaseClass firebase;
    ListView musicLV;
    MediaPlayer mediaPlayer;
    List<Music> musicList;
    List<Musician> musicianList;
    Button btnGenre_All,btnGenre_1,btnGenre_2,btnGenre_3,btnGenre_4,btnGenre_5,btnGenre_6,btnGenre_7;

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

        firebase = new FirebaseClass(this.getApplicationContext());

        musicianList = new ArrayList<>();

        btnGenre_All = findViewById(R.id.btnGenre_All);
        btnGenre_1 = findViewById(R.id.btnGenre_1);
        btnGenre_2 = findViewById(R.id.btnGenre_2);
        btnGenre_3 = findViewById(R.id.btnGenre_3);
        btnGenre_4 = findViewById(R.id.btnGenre_4);
        btnGenre_5 = findViewById(R.id.btnGenre_5);
        btnGenre_6 = findViewById(R.id.btnGenre_6);
        btnGenre_7 = findViewById(R.id.btnGenre_7);

        firebase.GetAllMusic(this);


        btnGenre_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAllMusic();
            }
        });

        btnGenre_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMusicWithCategory("1");
            }
        });

        btnGenre_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMusicWithCategory("2");
            }
        });

        btnGenre_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMusicWithCategory("3");
            }
        });

        btnGenre_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMusicWithCategory("4");
            }
        });

        btnGenre_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMusicWithCategory("5");
            }
        });

        btnGenre_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMusicWithCategory("6");
            }
        });

        btnGenre_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMusicWithCategory("7");
            }
        });

    }

    public void SetMusicAdapter(){
        //musicList.clear();
        //musicList.addAll(musicList);
        SetAdapter(musicList);
    }

    public void SetAdapter(List<Music> list){
        if(list == null){musicLV.setAdapter(null); Toast.makeText(this,"Bu kategoriye ait müzik yok !", Toast.LENGTH_SHORT).show(); return;}
        else{
            MusicAdapter adapter = new MusicAdapter(this,R.layout.music_layout,list);
            musicLV.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
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

    public void GetAllMusic(){
        firebase.GetAllMusic(this);
    }

    public void GetMusicWithCategory(String genre){
        firebase.GetMusicWithGenre(this,genre);
    }





    ///////////////////////////////////////////
    ///////////////////////////////////////////
            //Interface Functions
    ///////////////////////////////////////////
    ///////////////////////////////////////////

    @Override
    public void AddAudioToFirebaseResult(boolean result) {

    }

    @Override
    public void AddMusicianToFirebaseResult(boolean result) {

    }

    @Override
    public void AddUserToFirebaseResult(boolean result) {

    }

    @Override
    public void GetCategoriesResult(Hashtable<String, String> list) {

    }

    @Override
    public void GetAllMusicResult(List<Music> list) {
        musicList = list;
        if(list != null){
            if(list.size() < 1){SetAdapter(null); return;}
            ArrayList<String> idList = new ArrayList<>();
            for(Music music:list){idList.add(music.getMusicianId());}

            firebase.GetMusicianWithId(this,idList);
        }
        else{ SetAdapter(null); return;}
    }

    @Override
    public void GetMusicWithGenreResult(List<Music> list) {
        musicList = list;
        if(list != null){
            if(list.size() < 1){SetAdapter(null); return;}
            ArrayList<String> idList = new ArrayList<>();
            for(Music music:list){idList.add(music.getMusicianId());}

            firebase.GetMusicianWithId(this,idList);
        }
        else{ SetAdapter(null); return;}
    }

    @Override
    public void GetMusicWithMusicianIdResult(List<Music> list) {

    }

    @Override
    public void GetAllMusicianResult(List<Musician> list) {

    }

    @Override
    public void GetMusicianWithIdResult(List<Musician> list) {
        for(int i = 0; i<list.size(); i++){
            musicList.get(i).setArtist(list.get(i).getName());
        }

        SetAdapter(musicList);
    }

    @Override
    public void GetUserInfoWithMailResult(User user) {

    }

    @Override
    public void CheckMailExistResult(boolean result) {

    }

    @Override
    public void CheckUsernameExistResult(boolean result) {

    }

    @Override
    public void LoginToAppResult(boolean result) {

    }

    @Override
    public void TESTINT(List<String> list) {

    }
}