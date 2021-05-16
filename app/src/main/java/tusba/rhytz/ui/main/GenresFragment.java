package tusba.rhytz.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tusba.rhytz.R;
import tusba.rhytz.model.GenreAdapter;
import tusba.rhytz.model.Music;
import tusba.rhytz.model.MusicAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenresFragment extends Fragment {

    private static final String ARG_PARAM_MUSIC = "music";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerViewGenre;

    // TODO: Rename and change types of parameters
    private ArrayList<Music> music;

    public GenresFragment() {
        // Required empty public constructor
    }

    public static GenresFragment newInstance(ArrayList<Music> music) {
        GenresFragment fragment = new GenresFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_MUSIC, music);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            music = (ArrayList<Music>) getArguments().getSerializable(ARG_PARAM_MUSIC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_genres, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerViewGenre = getView().findViewById(R.id.recyclerViewGenres);
        GenreAdapter genreAdapter = new GenreAdapter(getContext(),music);
        recyclerViewGenre.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewGenre.setAdapter(genreAdapter);
    }
}