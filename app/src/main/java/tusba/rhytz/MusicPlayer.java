package tusba.rhytz;



import androidx.annotation.Nullable;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import tusba.rhytz.helpers.MediaPlayerHelper;
import tusba.rhytz.models.Music;


public class MusicPlayer extends SlideMenu {

    TextView textViewSongName ;
    TextView textViewSingerName ;
    TextView textViewCurrentPlace ;
    TextView textViewDuration ;
    Button buttonPausePlay;
    MediaPlayerHelper mediaPlayer;
    SeekBar seekBarMusic;
    boolean isMediaPlayerReady = false;
    Music music;
    Button equBtn;
    int sessionId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater= LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_music_player,null,false);
        drawer.addView(v,0);
        Bundle bundle = getIntent().getExtras();
        music =(Music)bundle.getSerializable("song");


        textViewSongName = findViewById(R.id.textViewSongName);
        textViewSingerName = findViewById(R.id.textViewSingerName);
        textViewCurrentPlace = findViewById(R.id.textViewCurrentPlace);
        textViewDuration = findViewById(R.id.textViewDuration);
        buttonPausePlay = findViewById(R.id.buttonPausePlay);
        seekBarMusic = findViewById(R.id.seekBarMusic);

        mediaPlayer = MediaPlayerHelper.getInstance();
        if(music == null){
            music = mediaPlayer.getMusic();
            seekBarMusic.setProgress(mediaPlayer.getMediaPlayer().getCurrentPosition()*100/mediaPlayer.getMediaPlayer().getDuration());
        }
        else{
            mediaPlayer.setMediaPlayer(this,music);
        }

        textViewSongName.setText(music.getTitle());
        textViewSingerName.setText(music.getArtist());



        boolean isPlaying = false;
        if(mediaPlayer.getMediaPlayer() != null)
            isPlaying = mediaPlayer.getMediaPlayer().isPlaying();
        if(isPlaying) {
            //mediaPlayer.getMediaPlayer().start();
            buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
        }


        //mediaPlayer = MediaPlayer.create(this, music.getUri());
        equBtn = (Button) findViewById(R.id.buttonEqualizer);
        AttachListeners();

    }

    private void AttachListeners(){
        AttachMediaPlayerListeners();
    }

    private void AttachMediaPlayerListeners(){
        mediaPlayer.getMediaPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                textViewDuration.setText(mediaPlayer.GetTimeWithMiliSecond(mediaPlayer.getMediaPlayer().getDuration()));
                mediaPlayer.setReady(true);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewCurrentPlace.setText(mediaPlayer.GetTimeWithMiliSecond(mp.getCurrentPosition()));
                                seekBarMusic.setProgress(mp.getCurrentPosition()*100/mp.getDuration());
                            }
                        });

                    }
                },50,500);
            }
        });

        if(mediaPlayer.isReady()){
            textViewDuration.setText(mediaPlayer.GetTimeWithMiliSecond(mediaPlayer.getMediaPlayer().getDuration()));
            mediaPlayer.setReady(true);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewCurrentPlace.setText(mediaPlayer.GetTimeWithMiliSecond(mediaPlayer.getMediaPlayer().getCurrentPosition()));
                            seekBarMusic.setProgress(mediaPlayer.getMediaPlayer().getCurrentPosition()*100/mediaPlayer.getMediaPlayer().getDuration());
                        }
                    });

                }
            },50,500);



        }


        mediaPlayer.getMediaPlayer().setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBarMusic.setProgress(percent);
                textViewCurrentPlace.setText(""+mp.getDuration()*percent/100);
            }
        });

        mediaPlayer.getMediaPlayer().setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(MediaPlayer mp, TimedText text) {
                System.out.println("hmm");
            }
        });

        mediaPlayer.getMediaPlayer().setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                textViewCurrentPlace.setText(mediaPlayer.GetTimeWithMiliSecond(mp.getCurrentPosition()));
            }
        });
        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    int seekedMili = progress*mediaPlayer.getMediaPlayer().getDuration()/100;
                    mediaPlayer.getMediaPlayer().seekTo(seekedMili);
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
                if(!mediaPlayer.isReady()){
                    Toast.makeText(getParent(), "Not Prepared", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mediaPlayer.getMediaPlayer().isPlaying()){
                    mediaPlayer.getMediaPlayer().pause();

                    buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_play));
                }
                else{
                    mediaPlayer.getMediaPlayer().start();

                    buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
                }
            }
        });

        equBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionId = mediaPlayer.getMediaPlayer().getAudioSessionId();
                Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);
                startActivityForResult(intent,123);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123 && resultCode==RESULT_OK){
            return;
        }
    }

    private String GetTimeWithMiliSecond(int milisecond){
        String time = "";
        int second = milisecond/1000;
        int min = second/60;
        int hour = min/60;
        second = second%60;
        min = min%60;


        if(hour > 0){
            if(hour < 10){
                time += "0"+hour;
            }
            else{
                time+=hour;
            }
            time += ":";
        }
        if(min < 10){
            time += "0"+min;
        }
        else{
            time += min;
        }
        time += ":";
        if(second < 10){
            time += "0"+second;
        }
        else{
            time += second;
        }

        return  time;

    }
}
