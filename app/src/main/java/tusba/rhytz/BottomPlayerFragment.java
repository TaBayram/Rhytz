package tusba.rhytz;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import tusba.rhytz.helpers.MediaPlayerHelper;
import tusba.rhytz.helpers.PlayerListenerHelper;
import tusba.rhytz.models.Music;


public class BottomPlayerFragment extends Fragment implements PlayerListenerHelper {

    Button buttonPausePlay,buttonNext,buttonPrevious;
    SeekBar seekBarMusic;
    TextView textViewTitle,textViewDuration,textViewCurrent;
    MediaPlayerHelper mediaPlayerHelper;
    Fragment fragment;

    public BottomPlayerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BottomPlayerFragment newInstance(String param1, String param2) {
        BottomPlayerFragment fragment = new BottomPlayerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_player, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        buttonPausePlay = getView().findViewById(R.id.buttonBottomPlayPause);
        buttonNext = getView().findViewById(R.id.buttonBottomNext);
        buttonPrevious = getView().findViewById(R.id.buttonBottomPrevious);
        textViewDuration = getView().findViewById(R.id.textViewBottomDuration);
        textViewCurrent = getView().findViewById(R.id.textViewBottomCurrent);
        textViewTitle = getView().findViewById(R.id.textViewBottomTitle);
        seekBarMusic = getView().findViewById(R.id.seekBarBottomMusic);
        mediaPlayerHelper = MediaPlayerHelper.getInstance();
        View layout = getView().findViewById(R.id.linearLayoutBottomMain);
        fragment = this;

        if(mediaPlayerHelper.isPlaying()){
            buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
        }
        else{
            buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_play));
        }

         layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayerHelper.getMusic() == null) return;
                Intent intent = new Intent(view.getContext(), MusicPlayer.class);
                intent.putExtra("song", (Bundle) null);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                view.getContext().startActivity(intent);
            }
        });

        mediaPlayerHelper.setFragmentBottomPlayer(this);

        AttachListeners();
    }


    private void AttachListeners(){
        if(mediaPlayerHelper.getMediaPlayer() == null) {
            return;
        }

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    int seekedMili = progress*mediaPlayerHelper.getMediaPlayer().getDuration()/100;
                    mediaPlayerHelper.getMediaPlayer().seekTo(seekedMili);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonPausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayerHelper.isReady()){
                    Toast.makeText(getContext(), "Not Prepared", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mediaPlayerHelper.isPlaying()){
                    mediaPlayerHelper.setPlaying(false);
                }
                else{
                    mediaPlayerHelper.setPlaying(true);
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerHelper.NextSong(getContext());
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerHelper.PreviousSong(getContext());
            }
        });


    }


    @Override
    public void SetState(boolean isPlaying) {
        if(isPlaying){
            buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
        }
        else{
            buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_play));
        }
    }

    @Override
    public void SetDuration(String duration) {
        if(this.getContext() == null || getActivity() == null){
            mediaPlayerHelper.getFragmentBottomPlayer().remove(this);
            return;
        }
        getActivity().runOnUiThread(() -> textViewDuration.setText(duration));
    }

    @Override
    public void SetCurrent(String duration) {
        if(this.getContext() == null || getActivity() == null){
            mediaPlayerHelper.getFragmentBottomPlayer().remove(this);
            return;
        }
        getActivity().runOnUiThread(() -> textViewCurrent.setText(duration));
    }

    @Override
    public void SetInformation(Music music) {
        if(this.getContext() == null || getActivity() == null){
            mediaPlayerHelper.getFragmentBottomPlayer().remove(this);
            return;
        }
        getActivity().runOnUiThread(() -> {
            textViewTitle.setText(music.getTitle());
            //textViewSingerName.setText(music.getArtist());
        });

    }

    @Override
    public void SetSeekBar(int percentage) {
        if(this.getContext() == null || getActivity() == null){
            mediaPlayerHelper.getFragmentBottomPlayer().remove(this);
            return;
        }
        getActivity().runOnUiThread(() -> seekBarMusic.setProgress(percentage));
    }
}