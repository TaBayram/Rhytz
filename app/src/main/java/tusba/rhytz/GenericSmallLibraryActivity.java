package tusba.rhytz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import tusba.rhytz.helpers.GenericAdapter;
import tusba.rhytz.models.Music;

public class GenericSmallLibraryActivity extends AppCompatActivity {

    RecyclerView recyclerViewMusic;
    ArrayList<Music> music;
    TextView textViewTitle;
    Button buttonBack;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_smal_library);
        context = this;
        textViewTitle = findViewById(R.id.textViewGSLTitle);
        buttonBack = findViewById(R.id.buttonGSLBack);

        Bundle bundle = getIntent().getExtras();
        music =(ArrayList<Music>)bundle.getSerializable("music");
        int type = bundle.getInt("type");


        if(type == 3){
            textViewTitle.setText(music.get(0).getGenre());
        }
        else if(type == 4){
            textViewTitle.setText(music.get(0).getArtist());
        }
        else if (type == 5){
            textViewTitle.setText(music.get(0).getAlbum());
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });


        recyclerViewMusic = findViewById(R.id.recyclerViewGSL);
        GenericAdapter musicAdapter = new GenericAdapter(this,music,1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewMusic.setLayoutManager(layoutManager);
        recyclerViewMusic.setAdapter(musicAdapter);
    }
}