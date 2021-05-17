package tusba.rhytz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

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

public class ShowMusiciansMusic extends AppCompatActivity implements FirebaseInterface {

    FirebaseClass firebase;
    MediaPlayer mediaPlayer;
    ListView musicianMusicLV;
    List<Music> musicList;
    String musicianId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_musicians_music);

        musicList = new ArrayList<>();

        musicianMusicLV = findViewById(R.id.musicianMusicLV);

        firebase = new FirebaseClass(this.getApplicationContext());

        Intent intent = getIntent();
        musicianId = intent.getStringExtra("musicianId");

        ShowMusicianMusic(musicianId);
    }

    public void ShowMusicianMusic(String musicianId){
        firebase.GetMusicWithMusicianId(this, musicianId);
    }

    public void SetAdapter(List<Music> list){
        if(list == null){musicianMusicLV.setAdapter(null); Toast.makeText(this,"Bu şarkıcıya ait müzik yok !", Toast.LENGTH_SHORT).show(); return;}
        else{
            MusicAdapter adapter = new MusicAdapter(this,R.layout.music_layout,list);
            musicianMusicLV.setAdapter(adapter);
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

    }

    @Override
    public void GetMusicWithGenreResult(List<Music> list) {

    }

    @Override
    public void GetMusicWithMusicianIdResult(List<Music> list) {
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
    public void GetAllMusicianResult(List<Musician> list) {

    }

    @Override
    public void GetMusicianWithIdResult(List<Musician> list) {
        for(int i = 0; i<list.size(); i++){
            musicList.get(i).setMusicianName(list.get(i).getName());
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