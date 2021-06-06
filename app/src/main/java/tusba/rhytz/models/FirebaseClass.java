package tusba.rhytz.models;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class FirebaseClass {

    StorageTask mUploadTast;
    StorageReference firebaseStorage;
    FirebaseFirestore firestore;

    public Context context;
    RealDoc realDoc = new RealDoc();

    String globalMusicianId = "vfbOjdVF5u7cYQJ4N2d9";

    Hashtable<String, String> categoryList = new Hashtable<String, String>();

    public FirebaseClass(Context context){
        this.context = context;
        firebaseStorage = FirebaseStorage.getInstance().getReference().child("musics");

        firestore = FirebaseFirestore.getInstance();
    }

    public void GetCategories(FirebaseInterface object) {
        categoryList = new Hashtable<String, String>();

        firestore.collection("categories").document("documents")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for(Object item :document.getData().values()){
                            categoryList.put(String.valueOf(i),String.valueOf(item));
                            i++;
                        }

                    } else {
                        Toast.makeText(context,"hata",Toast.LENGTH_LONG).show();
                    }

                    object.GetCategoriesResult(categoryList);

                } else {
                }
            }
        });


    }

    public void AddAudioToFirebase(FirebaseInterface object,Uri audioUri,String fileExtension,int songDuration, int durationFromMilli){

        if(audioUri != null){
            StorageReference storageReference = firebaseStorage.child(System.currentTimeMillis() + "." + fileExtension);
            int millisDuration = songDuration;
            int duration = durationFromMilli;

            mUploadTast = storageReference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String path = "/v0/b/rhytz-1821f.appspot.com/o/musics%2F" + uri.getPath().substring(39,56);

                            AddMusicToMusician(path,"şarkı adı",globalMusicianId,"1","2",duration);
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

    public void AddMusicToMusician(String source, String name, String musicianId, String albumId, String categoryId, int duration) {
        String musicId = source.substring(source.indexOf(".mp3") - 13, source.indexOf(".mp3"));
        source = "https://firebasestorage.googleapis.com" + source + "?alt=media";
        CollectionReference collectionReference = firestore.collection("musicians").document(musicianId).collection("musics");
        Music music = new Music(musicId, albumId, categoryId, duration, musicianId, name, source, "");

        collectionReference.document(musicId).set(realDoc, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        collectionReference.document(musicId).collection("documents").document("info").set(music, SetOptions.merge())
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });



    }

    public void AddMusicianToFirabase(FirebaseInterface object,String name){
        //Map<String, Object> city = new HashMap<>();
        //city.put("1", "rock");

        String id = GetRandomId();
        DocumentReference documentReference = firestore.collection("musicians").document(id);
        documentReference.set(realDoc, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        Musician musician = new Musician(id,name);

                        documentReference.collection("info").document("documents").set(musician, SetOptions.merge())
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

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                    }
                });


    }

    public void AddUserToFirebase(FirebaseInterface object ,User user){
        String id = GetRandomId();
        user.setId(id);

        CollectionReference collectionReference = firestore.collection("users");

        collectionReference.document(id).set(user, SetOptions.merge())
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


    public void CheckMailExist(FirebaseInterface object, String mail){
        firestore.collection("users").whereEqualTo("email",mail) //get all musician id
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i("test",task.getResult().size()+"");
                            if(task.getResult().size() >= 1){object.CheckMailExistResult(true);}
                            else{object.CheckMailExistResult(false);}

                        } else { Toast.makeText(context,String.valueOf("hata"),Toast.LENGTH_LONG).show();}


                    }
                });
    }

    public void CheckUsernamelExist(FirebaseInterface object, String username){
        firestore.collection("users").whereEqualTo("username",username) //get all musician id
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if(task.getResult().size() >= 1){object.CheckUsernameExistResult(true);}
                            else{object.CheckUsernameExistResult(false);}

                        } else { Toast.makeText(context,String.valueOf("hata"),Toast.LENGTH_LONG).show();}


                    }
                });
    }

    public void GetAllMusic(FirebaseInterface object){
        List<Music> allMusic = new ArrayList<Music>();
        final boolean[] flag1 = {true};
        final int[] musicCount = {0};
        final int[] foundMusicCount = {0};
        firestore.collection("musicians") //get all musician id
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            //Toast.makeText(context,String.valueOf(task.getResult().size()),Toast.LENGTH_LONG).show();
                            for (QueryDocumentSnapshot document1 : task.getResult()) {

                                firestore.collection("musicians").document(document1.getId()).collection("musics") // get all musician's music
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    musicCount[0] += task.getResult().size();
                                                    for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                        firestore.collection("musicians").document(document1.getId()).collection("musics").document(document2.getId()).
                                                                collection("documents").document("info").
                                                                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                Music music = documentSnapshot.toObject(Music.class);
                                                                allMusic.add(music);
                                                                foundMusicCount[0]++;
                                                                if(foundMusicCount[0] >= musicCount[0]) { object.GetAllMusicResult(allMusic);}

                                                            }
                                                        });


                                                    }

                                                }
                                            }
                                        });

                            }


                        } else { Toast.makeText(context,String.valueOf("hata"),Toast.LENGTH_LONG).show();}


                    }
                });


    }

    public void GetMusicWithGenre(FirebaseInterface object,String categoryId){
        List<Music> allMusic = new ArrayList<Music>();
        final boolean[] flag1 = {false};
        final boolean[] flag2 = {false};
        final int[] musicCount = {0};
        final int[] foundMusicCount = {0};
        firestore.collection("musicians") //get all musician id
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if(task.getResult().size() >= 1){object.CheckUsernameExistResult(true);}
                            else{object.CheckUsernameExistResult(false);}

                        } else { Toast.makeText(context,String.valueOf("hata"),Toast.LENGTH_LONG).show();}


                    }
                });
    }

    public void GetAllMusic(FirebaseInterface object){
        List<Music> allMusic = new ArrayList<Music>();
        final boolean[] flag1 = {true};
        final int[] musicCount = {0};
        final int[] foundMusicCount = {0};
        firestore.collection("musicians") //get all musician id
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            //Toast.makeText(context,String.valueOf(task.getResult().size()),Toast.LENGTH_LONG).show();
                            for (QueryDocumentSnapshot document1 : task.getResult()) {

                                firestore.collection("musicians").document(document1.getId()).collection("musics") // get all musician's music
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    musicCount[0] += task.getResult().size();
                                                    for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                        firestore.collection("musicians").document(document1.getId()).collection("musics").document(document2.getId()).
                                                                collection("documents").document("info").
                                                                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                Music music = documentSnapshot.toObject(Music.class);
                                                                allMusic.add(music);
                                                                foundMusicCount[0]++;
                                                                if(foundMusicCount[0] >= musicCount[0]) { object.GetAllMusicResult(allMusic);}

                                                            }
                                                        });


                                                    }

                                                }
                                            }
                                        });

                            }


                        } else { Toast.makeText(context,String.valueOf("hata"),Toast.LENGTH_LONG).show();}


                    }
                });
    }

    public void GetMusicWithMusicianId(FirebaseInterface object,String musicianId){
        List<Music> allMusic = new ArrayList<Music>();
        final int[] musicCount = {0};
        final int[] foundMusicCount = {0};


    public void GetMusicWithGenre(FirebaseInterface object,String categoryId){
        List<Music> allMusic = new ArrayList<Music>();
        final boolean[] flag1 = {false};
        final boolean[] flag2 = {false};
        final int[] musicCount = {0};
        final int[] foundMusicCount = {0};
        firestore.collection("musicians") //get all musician id
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            //Toast.makeText(context,String.valueOf(task.getResult().size()),Toast.LENGTH_LONG).show();
                            for (QueryDocumentSnapshot document1 : task.getResult()) {

                                firestore.collection("musicians").document(document1.getId()).collection("musics") // get all musician's music
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    musicCount[0] += task.getResult().size();
                                                    for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                        firestore.collection("musicians").document(document1.getId()).collection("musics").document(document2.getId()).
                                                                collection("documents").document("info").
                                                                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                Music music = documentSnapshot.toObject(Music.class);
                                                                if(!music.getCategoryId().equals(categoryId)){foundMusicCount[0]++; }
                                                                else{
                                                                    allMusic.add(music);
                                                                    foundMusicCount[0]++;
                                                                }

                                                                if(foundMusicCount[0] >= musicCount[0]) { object.GetMusicWithGenreResult(allMusic);}
                                                            }
                                                        });

                                                    }
                                                }
                                            }
                                        });


                            }

                        } else { Toast.makeText(context,String.valueOf("hata"),Toast.LENGTH_LONG).show();}


                    }
                });
    }

    public void GetMusicWithMusicianId(FirebaseInterface object,String musicianId){
        List<Music> allMusic = new ArrayList<Music>();
        final int[] musicCount = {0};
        final int[] foundMusicCount = {0};

                            }

        firestore.collection("musicians").document(musicianId).collection("musics") // get all musician's music
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            musicCount[0] += task.getResult().size();
                            for (QueryDocumentSnapshot document2 : task.getResult()) {
                                firestore.collection("musicians").document(musicianId).collection("musics").document(document2.getId()).
                                        collection("documents").document("info").
                                        get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        Music music = documentSnapshot.toObject(Music.class);
                                        allMusic.add(music);
                                        foundMusicCount[0]++;

                                        if(foundMusicCount[0] >= musicCount[0]) { object.GetMusicWithMusicianIdResult(allMusic);}
                                    }
                                });

                            }
                        }
                    }
                });

    }

    public void GetAllMusician(FirebaseInterface object){
        List<Musician> allMusician = new ArrayList<Musician>();
        final boolean[] flag1 = {true};
        final int[] musicCount = {0};
        final int[] foundMusicCount = {0};
        firestore.collection("musicians") //get all musician id
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            musicCount[0] = task.getResult().size();
                            //Toast.makeText(context,String.valueOf(task.getResult().size()),Toast.LENGTH_LONG).show();
                            for (QueryDocumentSnapshot document1 : task.getResult()) {

                                firestore.collection("musicians").document(document1.getId()).collection("info").document("documents") // get all musician's music
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Musician musician = documentSnapshot.toObject(Musician.class);
                                                allMusician.add(musician);
                                                foundMusicCount[0]++;
                                                if(foundMusicCount[0] >= musicCount[0]) { object.GetAllMusicianResult(allMusician);}
                                            }
                                        });

                            }

                                //Toast.makeText(context,String.valueOf(task.getResult().size()),Toast.LENGTH_LONG).show();
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Musician musician = documentSnapshot.toObject(Musician.class);
                                Log.i("test",musician.getName());
                                musicianList.add(musician);
                                foundMusician[0]++;
                                if(foundMusician[0] >= idList.size()) { object.GetMusicianWithIdResult(musicianList);}

                        } else { Toast.makeText(context,String.valueOf("hata"),Toast.LENGTH_LONG).show();}

                            } else { Toast.makeText(context,String.valueOf("hata"),Toast.LENGTH_LONG).show();}

                    }
                });
    }

    public void GetMusicianWithId(FirebaseInterface object, ArrayList<String> idList){
        ArrayList<Musician> musicianList = new ArrayList<>();
        final int[] foundMusician = {0};
        for (String id : idList) {
            firestore.collection("musicians").document(id).collection("info").document("documents") //get all musician id
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                //Toast.makeText(context,String.valueOf(task.getResult().size()),Toast.LENGTH_LONG).show();
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Musician musician = documentSnapshot.toObject(Musician.class);
                                Log.i("test",musician.getName());
                                musicianList.add(musician);
                                foundMusician[0]++;
                                if(foundMusician[0] >= idList.size()) { object.GetMusicianWithIdResult(musicianList);}

    public void LoginToApp(FirebaseInterface object, String mail, String password){
        firestore.collection("users").whereEqualTo("email",mail) //get all musician id
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            } else { Toast.makeText(context,String.valueOf("hata"),Toast.LENGTH_LONG).show();}

                                User user = documentSnapshot.toObject(User.class);

                        }
                    });
        }
    }

    public void GetUserInfoWithMail(FirebaseInterface object, String mail){
        firestore.collection("users").whereEqualTo("email",mail) //get all musician id
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                User user = documentSnapshot.toObject(User.class);
                                object.GetUserInfoWithMailResult(user);
                            }

                        } else { Toast.makeText(context,String.valueOf("hata"),Toast.LENGTH_LONG).show();}
                    }
                });
    }

                    }
                });
    }

    public void GetUser(FirebaseInterface object,String userId){
        firestore.collection("users").document(userId) // get all musician's music
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = task.getResult().toObject(User.class);
                            object.GetUserResult(user);
                        }
                    }
                });

    }

    public void UpdateUser(FirebaseInterface object,User user){
        DocumentReference documentReference = firestore.collection("users").document(user.getId());

        documentReference.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        object.UpdateUser(true);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        object.UpdateUser(false);
                    }
                });
    }

    public String GetRandomId(){
        DocumentReference documentReference=firestore.collection("database").document();
        String key = documentReference.getId();
        documentReference.delete();
        return key;
    }


}
