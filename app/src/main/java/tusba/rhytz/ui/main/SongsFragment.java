package tusba.rhytz.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tusba.rhytz.R;
import tusba.rhytz.model.GenericAdapter;
import tusba.rhytz.model.Music;
import tusba.rhytz.model.MusicAdapter;


public class SongsFragment extends Fragment {

    private static final String ARG_PARAM_MUSIC = "music";
    private static final String ARG_PARAM_TYPE = "type";
    RecyclerView recyclerViewMusic;

    // TODO: Rename and change types of parameters
    private ArrayList<Music> music;
    private  int type;

    public SongsFragment() {
        // Required empty public constructor
    }

    public static SongsFragment newInstance(ArrayList<Music> music, int type) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_MUSIC, music);
        args.putInt(ARG_PARAM_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            music = (ArrayList<Music>) getArguments().getSerializable(ARG_PARAM_MUSIC);
            type = getArguments().getInt(ARG_PARAM_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_songs, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerViewMusic = getView().findViewById(R.id.recyclerViewFragmentSongs);
        GenericAdapter musicAdapter = new GenericAdapter(getContext(),music,type);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewMusic.setLayoutManager(layoutManager);
        recyclerViewMusic.setAdapter(musicAdapter);
    }
}