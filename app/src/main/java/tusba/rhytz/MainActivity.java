package tusba.rhytz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tusba.rhytz.DAO.FavoriteListDao;
import tusba.rhytz.Entity.FavoriteList;
import tusba.rhytz.models.Comment;
import tusba.rhytz.models.FirebaseClass;
import tusba.rhytz.models.FirebaseInterface;
import tusba.rhytz.models.LocalDatabase;
import tusba.rhytz.models.Music;
import tusba.rhytz.models.Musician;
import tusba.rhytz.models.User;

public class MainActivity extends AppCompatActivity implements FirebaseInterface {

    Uri audioUri;
    FirebaseClass firebase;
    LocalDatabase localDatabase;
    EditText musicianNameTxt;
    EditText etName,etSurname,etMail,etUsername,etPassword,etGender,etLoginMail,etLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicianNameTxt = findViewById(R.id.singerNameTXT);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etMail = findViewById(R.id.etMail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etGender = findViewById(R.id.etGender);
        etLoginMail = findViewById(R.id.etLoginMail);
        etLoginPassword = findViewById(R.id.etLoginPassword);

        firebase = new FirebaseClass(this.getApplicationContext());
        localDatabase = new LocalDatabase(this.getApplicationContext());

    }

    ///////////////////////////////////////////
    ///////////////////////////////////////////
    // Firebase Interface
    ///////////////////////////////////////////
    ///////////////////////////////////////////

    @Override
    public void AddAudioToFirebaseResult(boolean result) {
        if(result){Log.i("","Müzik başarılı bir şekilde yüklendi");}
        else{Log.i("","Müzik yüklenemedi !");}
    }

    @Override
    public void AddMusicianToFirebaseResult(boolean result) {
        if(result){Log.i("","Müzisyen başarılı bir şekilde eklendi"); ShowToast("Müzisyen başarılı bir şekilde eklendi");}
        else{Log.i("","Müzisyen eklenemedi !");}
    }

    @Override
    public void AddUserToFirebaseResult(boolean result) {
        if(result){Log.i("","Kullanıcı başarılı bir şekilde eklendi"); ShowToast("Kullanıcı başarılı bir şekilde eklendi");}
        else{Log.i("","Kullanıcı eklenemedi !");}
    }

    @Override
    public void GetCategoriesResult(Hashtable<String, String> list) {
        for(Map.Entry<String, String> item : list.entrySet())
        {
            Log.i("category",item.getKey() + " : " + item.getValue());
        }
    }

    @Override
    public void GetAllMusicResult(List<Music> list) {
        Log.i("","liste boyutu : " + String.valueOf(list.size()));
        try{
            Intent intent = new Intent(this, ShowMusics.class);
            intent.putExtra("musicList",(Serializable) list);
            startActivity(intent);
        }
        catch (Exception ex){
            Log.i("hataaa ","hata : " + ex.getMessage());
        }
    }

    @Override
    public void GetMusicWithGenreResult(List<Music> list) {
        Log.i("","liste boyuyu : " + String.valueOf(list.size()));
        try{
            Intent intent = new Intent(this, ShowMusics.class);
            intent.putExtra("musicList",(Serializable) list);
            startActivity(intent);
        }
        catch (Exception ex){
            Log.i("hataaa ","hata : " + ex.getMessage());
        }
    }

    @Override
    public void GetMusicWithMusicianIdResult(List<Music> list) {

    }

    @Override
    public void GetAllMusicianResult(List<Musician> list) {

    }

    @Override
    public void GetMusicianWithIdResult(List<Musician> list) {
        for (Musician musician : list){
            Log.i("Test",musician.getName());
        }
    }

    @Override
    public void GetUserInfoWithMailResult(User user) {
        Log.i("test",user.getName() + " : " + user.getPassword());
    }

    @Override
    public void CheckMailExistResult(boolean result) {
        if(result){ShowToast("Aynı mail kullanımda !");}
        else{
            String username = etUsername.getText().toString();
            firebase.CheckUsernamelExist(this,username);
        }
    }

    @Override
    public void CheckUsernameExistResult(boolean result) {
        if(result){ShowToast("Aynı kullanıcı adı kullanımda !");}
        else{
            String name = etName.getText().toString();
            String surname = etSurname.getText().toString();
            String mail = etMail.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String gender = etGender.getText().toString();

            User user = new User("0",name,surname,mail,username,password,gender);
            firebase.AddUserToFirebase(this,user);
        }
    }

    @Override
    public void LoginToAppResult(boolean result) {
        if(result){ShowToast("Giriş Başarılı");}
        else{ShowToast("Mail veya Şifre yanlış !");}
    }

    @Override
    public void TESTINT(List<String> list) {
        Log.i("","laylaaylalallaa");
        for(String item : list){
            Log.i("",item);
        }
    }

    ///////////////////////////////////////////////////////////////////////////



    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == RESULT_OK && data.getData() != null ){
            audioUri = data.getData();
            String fileName = GetFileName(audioUri);
        }
    }
    public void GetAllCategories(View view){
        firebase.GetCategories(this);
    }
    public void AddAudioToFirebase(View view){
        String fileExtension = GetFileExtension(audioUri);
        int songDuration = GetSongDuration(audioUri);
        String durationFromMilli = GetDurationFromMilli(songDuration);
        firebase.AddAudioToFirebase(this,audioUri,fileExtension,songDuration,durationFromMilli);
    }
    public void AddMusicianToFirebase(View view){
        String musicianName = String.valueOf(musicianNameTxt.getText());
        firebase.AddMusicianToFirabase(this,musicianName);
    }
    public void AddUserToFirebase(View view){
        String mail = etMail.getText().toString();
        firebase.CheckMailExist(this,mail);
    }

    public void LoginToApp(View view){
        String mail = etLoginMail.getText().toString();
        String password = etLoginPassword.getText().toString();
        firebase.LoginToApp(this,mail,password);
    }


    public void OpenAudioFile(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent,101);
    }
    public String GetFileName(Uri uri){

        String result = null;
        if(uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri,null,null,null,null);

            try {
                    if(cursor!=null && cursor.moveToFirst()){
                        result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
            }
            finally {
                cursor.close();
            }
        }

        if(result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut!=-1){
                result = result.substring(cut + 1);
            }
        }

        return  result;
    }
    public int GetSongDuration(Uri audioUri){
        int duration = 0;

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this,audioUri);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            duration = Integer.parseInt(time);

            retriever.release();
            return duration;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }
    public String GetDurationFromMilli(int milliSec){
        Date date = new Date(milliSec);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        String time = simpleDateFormat.format(date);
        return time;
    }
    private String GetFileExtension(Uri audioUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(audioUri));
    }

    ///////////////////////////////////////////
    ///////////////////////////////////////////
            // Click Events
    ///////////////////////////////////////////
    ///////////////////////////////////////////


    public void GoToShowMusics(View view){
        //firebase.GetAllMusic(this);

        Intent intent = new Intent(this, ShowMusics.class);
        startActivity(intent);
    }

    public void GoToShowMusician(View view){
        //firebase.GetAllMusic(this);

        Intent intent = new Intent(this, ShowMusicians
                .class);
        startActivity(intent);
    }

    public void ShowToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void SetThemePreference(View view){
        boolean result = localDatabase.SetThemePreference("red");
        if(result){ShowToast("Tema tercihi başarıyla ayarlandı");}
        else {ShowToast("Tema tercihi ayarlanamadı");}
    }

    public void GetThemePreference(View view){
        String result = localDatabase.GetThemePreference();
        ShowToast("Tema tercihi : " + result);
    }

}