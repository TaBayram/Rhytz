package tusba.rhytz.helpers;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;

import tusba.rhytz.models.Music;

public class MediaPlayerHelper {
    private static MediaPlayerHelper instance = null;
    public static MediaPlayerHelper getInstance() {
        if (instance == null) {
            instance = new MediaPlayerHelper();
        }
        return instance;
    }
    private MediaPlayerHelper(){
        mediaPlayer = new MediaPlayer();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(Context context, Music music) {
        this.music = music;
        if(this.mediaPlayer != null && this.mediaPlayer.isPlaying()){
            this.mediaPlayer.stop();
        }
        this.mediaPlayer = MediaPlayer.create(context,music.getUri());
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    Music music;
    ArrayList<Music> musicList;
    MediaPlayer mediaPlayer;
    boolean isReady = false;

    public String GetTimeWithMiliSecond(int milisecond){
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
