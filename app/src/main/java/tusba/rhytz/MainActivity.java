package tusba.rhytz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button logout;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tusba.rhytz.models.FirebaseClass;
import tusba.rhytz.models.FirebaseInterface;
import tusba.rhytz.models.Music;
import tusba.rhytz.models.MusicianInfo;

public class MainActivity extends AppCompatActivity implements FirebaseInterface {

    Uri audioUri;
    FirebaseClass firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.btnLogout);

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember","false");
                editor.apply();

                finish();
            }
        });
        firebase = new FirebaseClass(this.getApplicationContext());

    }

    //
    // RESULTS FUNCTIONS
    //

    @Override
    public void AddAudioToFirebaseResult(boolean result) {
        if(result){Log.i("","Müzik başarılı bir şekilde yüklendi");}
        else{Log.i("","Müzik yüklenemedi !");}
    }

    @Override
    public void AddMusicianToFirebaseResult(boolean result) {
        if(result){Log.i("","Müzisyen başarılı bir şekilde eklendi");}
        else{Log.i("","Müzisyen eklenemedi !");}
    }

    @Override
    public void AddUserToFirebaseResult(boolean result) {
        if(result){Log.i("","Kullanıcı başarılı bir şekilde eklendi");}
        else{Log.i("","Kullanıcı eklenemedi !");}
    }

    @Override
    public void GetCategoriesResult(List<String> list) {
        for(String item : list){
            Log.i("",item);
        }
    }

    @Override
    public void GetAllMusicResult(List<Music> list) {
        Log.i("","liste boyuyu : " + String.valueOf(list.size()));
        Intent intent = new Intent(this, ShowMusics.class);
        intent.putExtra("musicList",(Serializable) list);
        startActivity(intent);
    }

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
        firebase.AddMusicianToFirabase(this);
    }
    public void AddUserToFirebase(View view){
        firebase.AddUserToFirebase(this,"ysf.sl","uslu.yusuf@std.izu.edu.tr","password123","male");
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
    public void GoToShowMusics(View view){
        firebase.GetAllMusic(this);
    }



}