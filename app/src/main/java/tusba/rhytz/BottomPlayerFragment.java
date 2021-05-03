package tusba.rhytz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import tusba.rhytz.model.MediaPlayerHelper;
import tusba.rhytz.model.MusicAdapter;


public class BottomPlayerFragment extends Fragment {

    Button buttonPausePlay;
    SeekBar seekBarMusic;
    TextView textViewTitle,textViewDuration;
    MediaPlayerHelper mediaPlayerHelper;


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
        textViewDuration = getView().findViewById(R.id.textViewBottomDuration);
        textViewTitle = getView().findViewById(R.id.textViewBottomTitle);
        seekBarMusic = getView().findViewById(R.id.seekBarBottomMusic);
        mediaPlayerHelper = MediaPlayerHelper.getInstance();
        View layout = getView().findViewById(R.id.linearLayoutBottomMain);




        buttonPausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayerHelper.isReady()){
                    Toast.makeText(view.getContext(), "Not Prepared", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mediaPlayerHelper.getMediaPlayer().isPlaying()){
                    mediaPlayerHelper.getMediaPlayer().pause();

                    buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_play));
                }
                else{
                    mediaPlayerHelper.getMediaPlayer().start();

                    buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
                }
            }
        });
        AttachMediaPlayerListeners();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayerHelper.getMusic() == null) return;
                Intent intent = new Intent(view.getContext(), MusicPlayer.class);
                intent.putExtra("song", (Bundle) null);
                view.getContext().startActivity(intent);
            }
        });


    }


    private void AttachMediaPlayerListeners(){
        if(mediaPlayerHelper.getMediaPlayer() == null) {
            return;
        }




         Timer timer = new Timer();
         timer.schedule(new TimerTask() {
             @Override
             public void run() {
                 getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         if(!mediaPlayerHelper.isReady()) return;
                         textViewTitle.setText(mediaPlayerHelper.getMusic().getTitle());
                         textViewDuration.setText(mediaPlayerHelper.GetTimeWithMiliSecond(mediaPlayerHelper.getMediaPlayer().getCurrentPosition())+"/"+mediaPlayerHelper.GetTimeWithMiliSecond(mediaPlayerHelper.getMediaPlayer().getDuration()));
                         seekBarMusic.setProgress(mediaPlayerHelper.getMediaPlayer().getCurrentPosition()*100/mediaPlayerHelper.getMediaPlayer().getDuration());
                     }
                 });

             }
         },50,500);
;

        mediaPlayerHelper.getMediaPlayer().setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBarMusic.setProgress(percent);
               // textViewCurrentPlace.setText(""+mp.getDuration()*percent/100);
            }
        });

        mediaPlayerHelper.getMediaPlayer().setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(MediaPlayer mp, TimedText text) {
                System.out.println("hmm");
            }
        });

        mediaPlayerHelper.getMediaPlayer().setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                textViewDuration.setText(mediaPlayerHelper.GetTimeWithMiliSecond(mediaPlayerHelper.getMediaPlayer().getCurrentPosition())+"/"+mediaPlayerHelper.GetTimeWithMiliSecond(mediaPlayerHelper.getMediaPlayer().getDuration()));
            }
        });
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

    }
}