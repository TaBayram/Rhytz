package tusba.rhytz.helpers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tusba.rhytz.BottomPlayerFragment;
import tusba.rhytz.HomeActivity;
import tusba.rhytz.MusicPlayer;
import tusba.rhytz.models.FirebaseClass;
import tusba.rhytz.models.FirebaseInterface;
import tusba.rhytz.models.Music;
import tusba.rhytz.models.Musician;
import tusba.rhytz.models.User;

public class MediaPlayerHelper implements FirebaseInterface {
    private static MediaPlayerHelper instance = null;
    public static MediaPlayerHelper getInstance() {
        if (instance == null) {
            instance = new MediaPlayerHelper();
        }
        return instance;
    }

    private Music music;
    private int playIndex = 0;
    private ArrayList<Music> playList;


    private MediaPlayer mediaPlayer;
    private boolean isReady = false;
    private boolean isPlaying = false;
    private boolean isOnRepeat = false;

    private ArrayList<Music> allMusicLocal;
    private ArrayList<Music> allMusicOnline;
    private Context contextMusicPlayer;
    private final ArrayList<BottomPlayerFragment> fragmentBottomPlayer;

    private Timer timer;

    public Context contextPlaylist = null;


    private int filterSetting = 0;


    public int sleepTime;
    public int sleepTimeCountdown;
    public boolean isSleepTimerOn = false;
    private Timer sleepTimer;


    FirebaseClass firebaseHelper;

    public void SetTheme(){
        ((HomeActivity)fragmentBottomPlayer.get(0).getContext()).Theme();
    }

    private MediaPlayerHelper(){
        mediaPlayer = new MediaPlayer();
        allMusicLocal = new ArrayList<Music>();
        allMusicOnline = new ArrayList<Music>();
        playList = new ArrayList<Music>();
        fragmentBottomPlayer = new ArrayList<>();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(Context context, Music music) {
        setReady(false);

        this.music = music;
        if(this.mediaPlayer != null && this.mediaPlayer.isPlaying()){
            this.mediaPlayer.stop();
        }

        this.mediaPlayer = MediaPlayer.create(context,music.GetUri());

        if(context instanceof MusicPlayer)
            setContextMusicPlayer(context);
        else{
            for(int i = 0; i < fragmentBottomPlayer.size(); i++){
                if(fragmentBottomPlayer.get(i).getContext() == context){
                    SetListeners(fragmentBottomPlayer.get(i));
                }
            }
        }


        for (int i = 0; i < this.playList.size(); i++) {
            if(music.getTitle().equals(playList.get(i).getTitle()) && music.GetUri().equals(playList.get(i).GetUri())){
                playIndex = i;
            }
        }
        if(isPlaying) mediaPlayer.start();

        if(contextMusicPlayer != null) {
            ((PlayerListenerHelper) contextMusicPlayer).SetInformation(music);
        }
        for(int i = 0; i < fragmentBottomPlayer.size(); i++) {
            BottomPlayerFragment fragment = fragmentBottomPlayer.get(i);
            ((PlayerListenerHelper) fragment).SetInformation(music);
            ((PlayerListenerHelper) fragment).SetDuration(GetTimeWithMiliSecond(mediaPlayer.getDuration()));
        }

    }

    public Context getContextMusicPlayer() {
        return contextMusicPlayer;
    }

    public void setContextMusicPlayer(Context contextMusicPlayer) {
        this.contextMusicPlayer = contextMusicPlayer;
        SetListeners((PlayerListenerHelper) contextMusicPlayer);
    }

    public ArrayList<BottomPlayerFragment> getFragmentBottomPlayer() {
        return fragmentBottomPlayer;
    }

    public void setFragmentBottomPlayer(BottomPlayerFragment fragmentBottomPlayer) {
        this.fragmentBottomPlayer.add(fragmentBottomPlayer);
        if(this.fragmentBottomPlayer.size() == 1){
            SharedPreferences sharedPref = fragmentBottomPlayer.getActivity().getPreferences(Context.MODE_PRIVATE);
            filterSetting = sharedPref.getInt("filterSetting",0);
        }
        if(music != null) fragmentBottomPlayer.SetInformation(music);
        SetListeners(fragmentBottomPlayer);
    }



    public ArrayList<Music> getPlayList(){ return playList;}

    public void setPlayList(ArrayList<Music> playList){    this.playList = playList;  }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
        if(isPlaying){
            mediaPlayer.start();
        }
        else{
            mediaPlayer.pause();
        }
        if(contextMusicPlayer != null)
            ((PlayerListenerHelper) contextMusicPlayer).SetState(isPlaying);
        for(int i = 0; i < fragmentBottomPlayer.size(); i++) {
            BottomPlayerFragment fragment = fragmentBottomPlayer.get(i);
            ((PlayerListenerHelper) fragment).SetState(isPlaying);
        }

    }

    public boolean isOnRepeat() {
        return isOnRepeat;
    }

    public void setOnRepeat(boolean onRepeat) {
        isOnRepeat = onRepeat;
    }


    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }


    public ArrayList<Music> getAllMusicLocal() {    return allMusicLocal;    }

    public void setAllMusicLocal(ArrayList<Music> allMusicLocal) {
        this.allMusicLocal = allMusicLocal;
    }
    public ArrayList<Music> getAllMusicOnline() {
        return allMusicOnline;
    }

    public void setAllMusicOnline(ArrayList<Music> allMusicOnline) {
        this.allMusicOnline = allMusicOnline;
    }

    public void setContextPlaylist(Context context){
        if(this.contextPlaylist != null) ((Activity)contextPlaylist).finish();
        this.contextPlaylist = context;
    }

    public int getFilterSetting() {
        return filterSetting;
    }

    /**
     * 0 ALL, 1 Local, 2 Stream
     * */
    public void setFilterSetting(int filterSetting) {
        if(this.fragmentBottomPlayer.size() <= 1){
            SharedPreferences sharedPref = this.fragmentBottomPlayer.get(0).getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("filterSetting", filterSetting);
            editor.apply();
        }
        this.filterSetting = filterSetting;
        ((HomeActivity)firebaseHelper.context).Refresh();
    }

    /*--------------------------------------------------------------------*/

    public void StartSleepTimer(int duration){
        if(duration != 0) {
            sleepTimeCountdown = duration;
            sleepTime = duration;
        }
        isSleepTimerOn = true;
        if(sleepTimer != null)
            sleepTimer.cancel();
        sleepTimer = new Timer();
        sleepTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sleepTimeCountdown--;
                if(sleepTimeCountdown <= 0){
                    if(isPlaying()){
                        isSleepTimerOn = false;
                        setPlaying(false);
                        sleepTimer.cancel();
                    }
                }
            }
        }, 000, 1000);
    }

    public void StopSleepTimer(){
        sleepTimer.cancel();
        isSleepTimerOn = false;
    }


    private void SetListeners(PlayerListenerHelper playerListenerHelper){


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setReady(true);
                playerListenerHelper.SetDuration(GetTimeWithMiliSecond(mediaPlayer.getDuration()));
            }
        });

        if(isReady()){
            playerListenerHelper.SetDuration(GetTimeWithMiliSecond(mediaPlayer.getDuration()));
            playerListenerHelper.SetCurrent(GetTimeWithMiliSecond(mediaPlayer.getCurrentPosition()));
        }

        if(timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isReady) {
                        if(contextMusicPlayer != null) {
                            ((PlayerListenerHelper) contextMusicPlayer).SetCurrent(GetTimeWithMiliSecond(mediaPlayer.getCurrentPosition()));
                            ((PlayerListenerHelper) contextMusicPlayer).SetSeekBar(mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration());
                        }
                        for(int i = 0; i < fragmentBottomPlayer.size(); i++) {
                            BottomPlayerFragment fragment = fragmentBottomPlayer.get(i);
                            ((PlayerListenerHelper) fragment).SetCurrent(GetTimeWithMiliSecond(mediaPlayer.getCurrentPosition()));
                            ((PlayerListenerHelper) fragment).SetSeekBar(mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration());
                        }
                    }
                }
            }, 50, 500);

        }
       /* mediaPlayer.getMediaPlayer().setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

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
*/
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                playerListenerHelper.SetCurrent(GetTimeWithMiliSecond(mediaPlayer.getCurrentPosition()));
            }
        });



        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(playList.size() > (playIndex+1)){
                    if(!isOnRepeat)
                        playIndex ++;
                    if(contextMusicPlayer != null)
                        setMediaPlayer(contextMusicPlayer,playList.get(playIndex));
                    else if(fragmentBottomPlayer != null)
                        setMediaPlayer(fragmentBottomPlayer.get(0).getContext(),playList.get(playIndex));
                }
                else{
                    playIndex = 0;
                    if(contextMusicPlayer != null)
                        setMediaPlayer(contextMusicPlayer,playList.get(playIndex));
                    else if(fragmentBottomPlayer != null)
                        setMediaPlayer(fragmentBottomPlayer.get(0).getContext(),playList.get(playIndex));
                    setPlaying(false);
                }
            }
        });

    }


    public ArrayList<Music> GetAllMusic(){
        ArrayList<Music> music = new ArrayList<Music>();
            if(filterSetting == 0 || filterSetting == 2)
        music.addAll(allMusicOnline);
            if(filterSetting == 0 || filterSetting == 1)
        music.addAll(allMusicLocal);
        return music;
    }

    public boolean SetLocalMusic(Context context){
        ArrayList<String> favorites = GetAllPlaylists(context);

        ArrayList<Music> musics = new ArrayList<Music>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;


        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";



        Cursor songCursor = contentResolver.query(songUri,null,selection,null,sortOrder);

        if(songCursor != null && songCursor.moveToFirst()){
            int idColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int albumColumn = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int artistColumn = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ARTIST);
            int artist2Column = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int titleColumn = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int genreColumn = songCursor.getColumnIndex(MediaStore.Audio.Media.GENRE);

            do{
                long id = songCursor.getLong(idColumn);
                String name = songCursor.getString(nameColumn);
                int duration = songCursor.getInt(durationColumn);
                int size = songCursor.getInt(sizeColumn);
                String album = songCursor.getString(albumColumn);
                String artist = songCursor.getString(artistColumn);
                String title = songCursor.getString(titleColumn);
                String genre = songCursor.getString(genreColumn);
                if(artist == null){
                    artist = songCursor.getString(artist2Column);
                }

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                Music song = new Music(contentUri, name, duration, size, album, artist, title,genre);
                musics.add(song);

                if(favorites.get(0).contains(song.getId())){
                    song.setFavorite(true);
                }

            }while(songCursor.moveToNext());

        }

        songUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        songCursor = contentResolver.query(songUri,null,null,null,null);

        if(songCursor != null && songCursor.moveToFirst()) {

            int albumColumn = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int artistColumn = songCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int artColumn = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

            do {
                String album = songCursor.getString(albumColumn);
                String artist = songCursor.getString(artistColumn);
                String albumArt = songCursor.getString(artColumn);

                for (Music song : musics) {
                    if (song.getAlbum().equals(album) && song.getArtist().equals(artist)) {
                        song.setAlbumArt(albumArt);
                    }
                }


            } while (songCursor.moveToNext());
        }

        setAllMusicLocal(musics);
        return true;

    }


    public boolean SetOnlineMusic(Context context){
        firebaseHelper = new FirebaseClass(context);
        firebaseHelper.GetAllMusic(this);
        return  true;
    }

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

    public void NextSong(Context context){
        playIndex = (playIndex+1)%playList.size();
        setMediaPlayer(context,playList.get(playIndex));

    }
    public void  PreviousSong(Context context){
        playIndex = (playIndex-1);
        if(playIndex == -1) playIndex = playList.size()-1;
        setMediaPlayer(context,playList.get(playIndex));
    }

    public void CheckMusicPlayer(Music song){
        if(contextMusicPlayer != null && !((MusicPlayer) contextMusicPlayer).music.getId().equals(song.getId())){
            setMediaPlayer(contextMusicPlayer,song);
        }
    }

    public void PlayNext(Music song,Context context){
        if(playList != null && !playList.isEmpty()){
            playList.add(playIndex+1,song);
            SimpleToast(context,"Will Played Next!");
        }

    }

    public void AddToQueue(Music song,ArrayList<Music> songs, Context context){
        if(playList != null){
            if(song != null)
                playList.add(song);
            if(songs != null)
                playList.addAll(songs);
            SimpleToast(context,"Added To Queue!");
            if(music == null){
                playIndex = 0;
                setMediaPlayer(context,playList.get(0));
            }
        }
    }

    public boolean isPlaylistChanged = false;

    public void ChangeFavorite(Music music, Context context){
        String list = GetAllPlaylists(context).get(0);
        if(music.isFavorite()){
            try {
                SharedPreferences sharedPref = context.getSharedPreferences("playlists", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String id = list.split("!")[0];
                String info = list.split("!")[1];
                info = info.replaceFirst("#"+music.getId(),"");
                editor.putString(id, info);
                editor.apply();

                isPlaylistChanged = true;
                SimpleToast(context,"Removed!");
            }
            catch (Exception e){

                SimpleToast(context,"Error Updating Playlist");
            }

        }
        else{
            try {
                SharedPreferences sharedPref = context.getSharedPreferences("playlists", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String id = list.split("!")[0];
                String info = list.split("!")[1];
                editor.putString(id, info.concat("#" + music.getId()));
               // editor.putString(id, "Favorites:null");
                editor.apply();

                isPlaylistChanged = true;
                SimpleToast(context,"Added!");
            }
            catch (Exception e){
                SimpleToast(context,"Error Updating Playlist");
            }
        }
        music.setFavorite(!music.isFavorite());
    }


    public ArrayList<String> GetAllPlaylists(Context context){
        // ! ID separator, : name separator, # song separator
        SharedPreferences sharedPref = context.getSharedPreferences("playlists",Context.MODE_PRIVATE);
        ArrayList<String> lists = new ArrayList<>();
        lists.add("favorites!"+sharedPref.getString("favorites", "Favorites:null"));
        for(int i = 0; i < 20; i++) {
            String pl = sharedPref.getString("playlist"+i, "null");
            if(!pl.equals("null")){
                lists.add(("playlist"+i)+"!"+sharedPref.getString("favorites", "null"));
            }
        }

        return  lists;
    }


    private void SimpleToast(Context context, String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void AddAudioToFirebaseResult(boolean result) {

    }

    @Override
    public void AddMusicianToFirebaseResult(boolean result) {

    }

    @Override
    public void AddUserToFirebaseResult(boolean result) {

    }

    @Override
    public void GetCategoriesResult(Hashtable<String, String> list) {

    }

    @Override
    public void GetAllMusicResult(List<Music> list) {
        if(list != null){
            ArrayList<String> idList = new ArrayList<>();
            for(Music music:list){idList.add(music.getMusicianId());}
            firebaseHelper.GetMusicianWithId(this,idList);
        }
        else
            return;

        this.allMusicOnline = (ArrayList<Music>) list;


    }

    @Override
    public void GetMusicWithGenreResult(List<Music> list) {

    }

    @Override
    public void GetMusicWithMusicianIdResult(List<Music> list) {


    }

    @Override
    public void GetAllMusicianResult(List<Musician> list) {

    }

    @Override
    public void GetMusicianWithIdResult(List<Musician> list) {
        for(int i = 0; i<list.size(); i++){
            this.allMusicOnline.get(i).setArtist(list.get(i).getName());
        }


        for(Music song: allMusicOnline){
            song.CheckNulls();
        }
        ((HomeActivity)firebaseHelper.context).Refresh();

    }

    @Override
    public void GetUserInfoWithMailResult(User user) {

    }

    @Override
    public void CheckMailExistResult(boolean result) {

    }

    @Override
    public void CheckUsernameExistResult(boolean result) {

    }

    @Override
    public void LoginToAppResult(User user) {

    }

    @Override
    public void UpdateUser(boolean result) {

    }

    @Override
    public void GetUserResult(User user) {

    }



    @Override
    public void TESTINT(List<String> list) {

    }
}
