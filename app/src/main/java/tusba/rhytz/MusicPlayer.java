package tusba.rhytz;



import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import tusba.rhytz.helpers.MediaPlayerHelper;
import tusba.rhytz.helpers.PlayerListenerHelper;
import tusba.rhytz.models.Music;


public class MusicPlayer extends SlideMenu implements PlayerListenerHelper {

    TextView textViewSongName ;
    TextView textViewSingerName ;
    TextView textViewCurrentPlace ;
    TextView textViewDuration ;
    Button buttonPausePlay;
    Button buttonPlayList;
    Button buttonNext;
    Button buttonPrevious;
    Button buttonRepeat;
    MediaPlayerHelper mediaPlayerHelper;
    SeekBar seekBarMusic;
    public Music music;
    Button equBtn;
    Context context;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater= LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_music_player,null,false);
        drawer.addView(v,0);
        Bundle bundle = getIntent().getExtras();
        music =(Music)bundle.getSerializable("song");

        this.context = this;




        textViewSongName = findViewById(R.id.textViewSongName);
        textViewSingerName = findViewById(R.id.textViewSingerName);
        textViewCurrentPlace = findViewById(R.id.textViewCurrentPlace);
        textViewDuration = findViewById(R.id.textViewDuration);
        buttonPausePlay = findViewById(R.id.buttonPausePlay);
        buttonPlayList = findViewById(R.id.buttonPlayList);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonRepeat = findViewById(R.id.buttonRepeat);
        seekBarMusic = findViewById(R.id.seekBarMusic);

        Button buttonDrawer = findViewById(R.id.buttonMPRhytz);

        buttonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);

            }
        });


        mediaPlayerHelper = MediaPlayerHelper.getInstance();
        if(music == null){
            music = mediaPlayerHelper.getMusic();
            seekBarMusic.setProgress(mediaPlayerHelper.getMediaPlayer().getCurrentPosition()*100/ mediaPlayerHelper.getMediaPlayer().getDuration());
            mediaPlayerHelper.setContextMusicPlayer(this);
        }
        else{
            mediaPlayerHelper.setPlaying(true);
            mediaPlayerHelper.setMediaPlayer(this,music);

        }


        textViewSongName.setText(music.getTitle());
        textViewSingerName.setText(music.getArtist());



        if(mediaPlayerHelper.getMediaPlayer() != null && mediaPlayerHelper.isPlaying()){
            buttonPausePlay.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
        }



        //mediaPlayer = MediaPlayer.create(this, music.getUri());
        equBtn = (Button) findViewById(R.id.buttonSleepTimer);
        AttachListeners();

    }

    private void AttachListeners(){

        buttonPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GenericSmallLibraryActivity.class);
                intent.putExtra("music", MediaPlayerHelper.getInstance().getPlayList());
                intent.putExtra("type", 6);
                context.startActivity(intent);
            }
        });

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    int seekedMili = progress* mediaPlayerHelper.getMediaPlayer().getDuration()/100;
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
                    Toast.makeText(context, "Not Prepared", Toast.LENGTH_SHORT).show();
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

        buttonRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayerHelper.isOnRepeat()){
                    mediaPlayerHelper.setOnRepeat(false);
                    buttonRepeat.setBackground(getResources().getDrawable(android.R.drawable.stat_notify_sync));
                }
                else{
                    mediaPlayerHelper.setOnRepeat(true);
                    buttonRepeat.setBackground(getResources().getDrawable(android.R.drawable.ic_popup_sync));
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerHelper.NextSong(context);
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerHelper.PreviousSong(context);
            }
        });

        equBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* sessionId = mediaPlayerHelper.getMediaPlayer().getAudioSessionId();
                Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);

                startActivityForResult(intent,123);*/

                Intent intent = new Intent(context, SleepActivity.class);
                context.startActivity(intent);
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
        runOnUiThread(() -> textViewDuration.setText(duration));
    }

    @Override
    public void SetCurrent(String duration) {
        runOnUiThread(() -> textViewCurrentPlace.setText(duration));
    }

    @Override
    public void SetInformation(Music music) {
        runOnUiThread(() -> {
            textViewSongName.setText(music.getTitle());
            textViewSingerName.setText(music.getArtist());
        });

    }

    @Override
    public void SetSeekBar(int percentage) {
        runOnUiThread(() -> seekBarMusic.setProgress(percentage));
    }
}
