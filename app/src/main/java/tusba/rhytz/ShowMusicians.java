package tusba.rhytz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

import tusba.rhytz.models.FirebaseClass;
import tusba.rhytz.models.FirebaseInterface;
import tusba.rhytz.models.Music;
import tusba.rhytz.models.MusicAdapter;
import tusba.rhytz.models.Musician;
import tusba.rhytz.models.MusicianAdapter;
import tusba.rhytz.models.User;

public class ShowMusicians extends AppCompatActivity implements FirebaseInterface {

    ListView musicianLV;
    FirebaseClass firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_musicians);

        musicianLV = findViewById(R.id.musicianLV);

        firebase = new FirebaseClass(this.getApplicationContext());

        GetAllMusician();
    }

    public void SetAdapter(List<Musician> list){
        if(list == null){musicianLV.setAdapter(null); Toast.makeText(this,"Müzisyen bulunamadı !", Toast.LENGTH_SHORT).show(); return;}
        else{
            MusicianAdapter adapter = new MusicianAdapter(this,R.layout.musician_layout,list);
            musicianLV.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void ShowMusiciansMusic(Musician musician) throws IOException {
        Intent intent = new Intent(this, ShowMusiciansMusic.class);
        intent.putExtra("musicianId",musician.getId());
        startActivity(intent);
    }

    public void GetAllMusician(){
        firebase.GetAllMusician(this);
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

    }

    @Override
    public void GetAllMusicianResult(List<Musician> list) {

        SetAdapter(list);
    }

    @Override
    public void GetMusicianWithIdResult(List<Musician> list) {

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
    public void UpdateUser(boolean result) {

    }

    @Override
    public void GetUserResult(User user) {

    }

    @Override
    public void TESTINT(List<String> list) {

    }
}