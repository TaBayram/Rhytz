package tusba.rhytz;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import tusba.rhytz.helpers.GenericAdapter;
import tusba.rhytz.helpers.MediaPlayerHelper;
import tusba.rhytz.models.Music;
import tusba.rhytz.models.ThemePreference;

public class GenericSmallLibraryActivity extends SlideMenu {

    RecyclerView recyclerViewMusic;
    ArrayList<Music> music;
    TextView textViewTitle;
    Button buttonBack;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemePreference themePreference = new ThemePreference(this);
        setTheme(themePreference.GetThemePreferenceInt());

        LayoutInflater inflater= LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_generic_smal_library,null,false);
        drawer.addView(v,0);

        context = this;
        textViewTitle = findViewById(R.id.textViewSTTitle);
        buttonBack = findViewById(R.id.buttonSTRhytz);

        Bundle bundle = getIntent().getExtras();
        music =(ArrayList<Music>)bundle.getSerializable("music");
        int type = bundle.getInt("type");

        if(type == 2){
            textViewTitle.setText("Playlist");
        }
        else if(type == 3){
            textViewTitle.setText(music.get(0).getGenre());
        }
        else if(type == 4){
            textViewTitle.setText(music.get(0).getArtist());
        }
        else if (type == 5){
            textViewTitle.setText(music.get(0).getAlbum());
        }
        else if (type == 6){
            textViewTitle.setText("Current List");
            MediaPlayerHelper.getInstance().setContextPlaylist(this);
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });


        recyclerViewMusic = findViewById(R.id.recyclerViewGSL);
        GenericAdapter musicAdapter = new GenericAdapter(this,music,1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewMusic.setLayoutManager(layoutManager);
        recyclerViewMusic.setAdapter(musicAdapter);
    }
}