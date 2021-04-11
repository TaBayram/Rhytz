package tusba.rhytz.models;


import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.SnapshotMetadata;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FirebaseClass {

    StorageTask mUploadTast;
    StorageReference firebaseStorage;
    FirebaseFirestore firestore;

    Context context;


    public FirebaseClass(Context context){
        this.context = context;
        firebaseStorage = FirebaseStorage.getInstance().getReference().child("musics");

        firestore = FirebaseFirestore.getInstance();
    }

    public void GetCategories(FirebaseInterface object) {
        List<String> categories = new ArrayList<String>();

        firestore.collection("database").document("category")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for(Object item :document.getData().values()){
                            categories.add(String.valueOf(item));
                        }

                    } else {
                        Toast.makeText(context,"hata",Toast.LENGTH_LONG).show();
                    }

                    object.GetCategoriesResult(categories);

                } else {
                }
            }
        });


    }

    public void AddAudioToFirebase(FirebaseInterface object,Uri audioUri,String fileExtension,int songDuration, String durationFromMilli){

        if(audioUri != null){
            StorageReference storageReference = firebaseStorage.child(System.currentTimeMillis() + "." + fileExtension);
            int millisDuration = songDuration;
            String duration = durationFromMilli;

            mUploadTast = storageReference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            AddMusicToMusician(uri.getPath(),"scream and shot","3QHgRiL1A1V2WcJXr9Kd","1","2",duration);
                            object.AddAudioToFirebaseResult(true);
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {object.AddAudioToFirebaseResult(false);}
                    });
                }
            });
        }
        else{
            object.AddAudioToFirebaseResult(false);
        }

    }

    public void AddMusicToMusician(String source, String name, String musicianId, String albumId, String categoryId, String duration) {
        //sample source : v0/b/rhytz-1821f.appspot.com/o/musics/1616841811107.mp3
        //String source = "v0/b/rhytz-1821f.appspot.com/o/musics/" + musicId + ".mp3";
        String musicId = source.substring(source.indexOf(".mp3") - 13, source.indexOf(".mp3"));
        source = "https://firebasestorage.googleapis.com" + source + "?alt=media";
        DocumentReference documentReference = firestore.collection("database").document("musicians");
        Music music = new Music(musicId, albumId, categoryId, duration, musicianId, name, source);

        documentReference.collection(musicianId).document("musics").collection(musicId).document("info").set(music, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(getApplicationContext(),"Veri Başarıyla Yazıldı",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    public void AddMusicianToFirabase(FirebaseInterface object){
        //Map<String, Object> city = new HashMap<>();
        //city.put("1", "rock");

        String id = GetRandomId();
        DocumentReference documentReference = firestore.collection("database").document("musicians");
        MusicianInfo musicianInfo = new MusicianInfo(id,"Abdullah Turgut","0");

        documentReference.collection(id).document("info").set(musicianInfo, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        object.AddMusicianToFirebaseResult(true);
                        //Toast.makeText(getApplicationContext(),"Veri Başarıyla Yazıldı",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        object.AddMusicianToFirebaseResult(false);
                    }
                });


        //collectionReference.document("albums").set(null);
        //collectionReference.document("songs").set(null);
    }

    public void AddUserToFirebase(FirebaseInterface object ,String username, String mail, String password, String gender){
        String id = GetRandomId();
        User user = new User(id,username,mail,password,gender);

        DocumentReference documentReference = firestore.collection("database").document("users");

        documentReference.collection(id).document("info").set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        object.AddUserToFirebaseResult(true);
                        //Toast.makeText(getApplicationContext(),"Veri Başarıyla Yazıldı",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        object.AddUserToFirebaseResult(false);
                    }
                });
    }

    public void GetAllMusic(FirebaseInterface object){
        List<Music> allMusic = new ArrayList<Music>();



        firestore.collection("database").document("musicians").collection("3QHgRiL1A1V2WcJXr9Kd")
                .document("musics").collection("1618136891573")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(getApplicationContext(),String.valueOf(task.getResult().size()),Toast.LENGTH_LONG).show();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Music music = document.toObject(Music.class);
                                //Toast.makeText(getApplicationContext(),String.valueOf(music.getSource()),Toast.LENGTH_LONG).show();
                                allMusic.add(music);
                            }

                            object.GetAllMusicResult(allMusic);

                        } else {
                            Toast.makeText(context,String.valueOf(task.getException()),Toast.LENGTH_LONG).show();
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });


    }

    public String GetRandomId(){
        DocumentReference documentReference=firestore.collection("database").document();
        String key = documentReference.getId();
        documentReference.delete();
        return key;
    }























    public void TestSetData(){
        DocumentReference docRef = firestore.collection("database").document("category");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(context,document.getData().toString(),Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context,"hata",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
