package tusba.rhytz.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import tusba.rhytz.GenericSmallLibraryActivity;
import tusba.rhytz.MusicPlayer;
import tusba.rhytz.R;
import tusba.rhytz.models.Music;

public  class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.ViewHolder> {

    Context context;

    int type = 0;
    ArrayList<Music> music;
    TreeMap<String,ArrayList<Music>> treeMusic;
    HashMap<String,ArrayList<Music>> hashMusic;
    Map<Integer,String> keys = new HashMap<Integer, String>();



    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView textViewHeader;
        TextView textViewFooter;
        ImageView imageViewImage;
        ImageButton buttonDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHeader = itemView.findViewById(R.id.textViewItemGenericHeader);
            textViewFooter = itemView.findViewById(R.id.textViewItemGenericFoot);
            imageViewImage = itemView.findViewById(R.id.imageViewItemGeneric);
            buttonDetails = itemView.findViewById(R.id.buttonItemGenericMore);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type == 1) {
                        MediaPlayerHelper.getInstance().setPlayList(music);
                            Intent intent = new Intent(context, MusicPlayer.class);
                            intent.putExtra("song", music.get(Integer.parseInt(itemView.getTag().toString())));
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            MediaPlayerHelper.getInstance().CheckMusicPlayer(music.get(Integer.parseInt(itemView.getTag().toString())));
                            context.startActivity(intent);

                    }
                    else if( type == 2){
                        Intent intent = new Intent(context, GenericSmallLibraryActivity.class);
                        intent.putExtra("music", hashMusic.get(keys.get(itemView.getTag())));
                        intent.putExtra("type", type);
                        context.startActivity(intent);

                    }
                    else if( type == 4 || type == 5){
                        Intent intent = new Intent(context, GenericSmallLibraryActivity.class);
                        intent.putExtra("music", treeMusic.get(keys.get(itemView.getTag())));
                        intent.putExtra("type", type);
                        context.startActivity(intent);

                    }

                }
            });

        }
    }
    /**
     * 1, song | 2, playlist | 3, genre | 4, artist | 5, album
     * */
    public GenericAdapter(Context context, ArrayList<Music> music, int type){
        this.context = context;
        this.music = music;
        this.type = type;

        musicBackup = new ArrayList<>();
        musicBackup.addAll(music);
        if(type == 1){

        }
        else if(type == 2){

            ArrayList<String> lists =  MediaPlayerHelper.getInstance().GetAllPlaylists(context);
            hashMusic = new HashMap<String, ArrayList<Music>>();

            int i = 0;
            for(String list: lists){
                try {

                    String[] name = list.split(":");
                    String[] musicIDS = name[1].split("#");
                    ArrayList<Music> tempSongs = new ArrayList<>();
                    for (String id : musicIDS) {
                        for (Music song : music) {
                            if (id.equals(song.getId())) {
                                tempSongs.add(song);
                            }
                        }
                    }
                    hashMusic.put(name[0], tempSongs);
                    keys.put(i, name[0]);
                    i++;
                }
                catch (Exception e){

                }
            }

            hashMusicBackup = new HashMap<>();
            hashMusicBackup.putAll(hashMusic);

        }
        else if(type == 4){
            Map<String,ArrayList<Music>> mapMusic = new HashMap<String, ArrayList<Music>>();
            for(Music song: music){
                if(mapMusic.get(song.getArtist()) == null){
                    mapMusic.put(song.getArtist(),new ArrayList<Music>());
                }
                mapMusic.get(song.getArtist()).add(song);
            }
            treeMusic = new TreeMap<String, ArrayList<Music>>();
            treeMusic.putAll(mapMusic);
            int i = 0;
            for(Map.Entry<String, ArrayList<Music>> entry : treeMusic.entrySet()){
                keys.put(i,entry.getKey());
                i++;
            }

            treeMusicBackup = new TreeMap<>();
            treeMusicBackup.putAll(treeMusic);
            keysBackup = new HashMap<>();
            keysBackup.putAll(keys);
        }
        else if(type == 5){
            Map<String,ArrayList<Music>> mapMusic = new HashMap<String, ArrayList<Music>>();
            for(Music song: music){
                if(mapMusic.get(song.getAlbum()) == null){
                    mapMusic.put(song.getAlbum(),new ArrayList<Music>());
                }
                mapMusic.get(song.getAlbum()).add(song);
            }
            treeMusic = new TreeMap<String, ArrayList<Music>>();
            treeMusic.putAll(mapMusic);
            int i = 0;
            for(Map.Entry<String, ArrayList<Music>> entry : treeMusic.entrySet()) {
                keys.put(i, entry.getKey());
                i++;
            }
            treeMusicBackup = new TreeMap<>();
            treeMusicBackup.putAll(treeMusic);
            keysBackup = new HashMap<>();
            keysBackup.putAll(keys);
        }

    }

    @NonNull
    @Override
    public GenericAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_generic,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        if(type == 1){
            Music music = this.music.get(position);
            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_song);
            holder.imageViewImage.setImageDrawable(drawable);
            holder.textViewHeader.setText(music.getTitle());
            holder.textViewFooter.setText(music.getArtist());
/*
            MediaPlayerHelper mediaPlayerHelper = MediaPlayerHelper.getInstance();
            if(mediaPlayerHelper.isReady() && mediaPlayerHelper.isPlaying()){
                if(mediaPlayerHelper.getMusic().getTitle().equals(music.getTitle())){
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFAAAA"));
                    holder.textViewFooter.setBackgroundColor(Color.parseColor("#FFAAAA"));
                }
            }*/

            holder.buttonDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context,holder.buttonDetails);
                    popupMenu.inflate(R.menu.menu_genericitem);
                    if(music.isFavorite())
                        popupMenu.getMenu().findItem(R.id.ItemGIFirst).setTitle("Unfavorable");
                    else
                        popupMenu.getMenu().findItem(R.id.ItemGIFirst).setTitle("Favorite");
                    popupMenu.getMenu().findItem(R.id.ItemGISecond).setTitle("Play Next");
                    popupMenu.getMenu().findItem(R.id.ItemGIThird).setTitle("Add To Queue");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.ItemGIFirst:
                                    MediaPlayerHelper.getInstance().ChangeFavorite(music,context);

                                    break;
                                case R.id.ItemGISecond:
                                    MediaPlayerHelper.getInstance().PlayNext(music,context);
                                    break;
                                case R.id.ItemGIThird:
                                    MediaPlayerHelper.getInstance().AddToQueue(music,null,context);
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });



        }
        else if(type == 2){
            if(keys.get(position) == null) return;
            ArrayList<Music> music = this.hashMusic.get(keys.get(position));
            if(music == null) return;
            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_favoritefull);
            holder.imageViewImage.setImageDrawable(drawable);
            holder.textViewHeader.setText(keys.get(position).split("!")[1]);
            holder.textViewFooter.setText(music.size()+" songs");


            holder.buttonDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context,holder.buttonDetails);
                    popupMenu.inflate(R.menu.menu_genericitem);
                    popupMenu.getMenu().removeItem(R.id.ItemGIFirst);
                    popupMenu.getMenu().removeItem(R.id.ItemGISecond);
                    popupMenu.getMenu().findItem(R.id.ItemGIThird).setTitle("Add To Queue");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.ItemGIThird:
                                    MediaPlayerHelper.getInstance().AddToQueue(null,music,context);
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

        }
        else if(type == 4){
            if(keys.get(position) == null) return;
            ArrayList<Music> music = this.treeMusic.get(keys.get(position));
            if(music == null) return;
            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_singer);
            holder.imageViewImage.setImageDrawable(drawable);
            holder.textViewHeader.setText(music.get(0).getArtist());
            holder.textViewFooter.setText(music.size()+" songs");
            

            holder.buttonDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context,holder.buttonDetails);
                    popupMenu.inflate(R.menu.menu_genericitem);
                    popupMenu.getMenu().removeItem(R.id.ItemGIFirst);
                    popupMenu.getMenu().removeItem(R.id.ItemGISecond);
                    popupMenu.getMenu().findItem(R.id.ItemGIThird).setTitle("Add To Queue");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.ItemGIThird:
                                    MediaPlayerHelper.getInstance().AddToQueue(null,music,context);
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });


        }
        else if(type == 5){
            if(keys.get(position) == null) return;
            ArrayList<Music> music = this.treeMusic.get(keys.get(position));
            if(music == null) return;

            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_baseline_album_24);
            holder.imageViewImage.setImageDrawable(drawable);
            holder.textViewHeader.setText(music.get(0).getAlbum());
            holder.textViewFooter.setText(music.size()+" songs");

            holder.buttonDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context,holder.buttonDetails);
                    popupMenu.inflate(R.menu.menu_genericitem);
                    popupMenu.getMenu().removeItem(R.id.ItemGIFirst);
                    popupMenu.getMenu().removeItem(R.id.ItemGISecond);
                    popupMenu.getMenu().findItem(R.id.ItemGIThird).setTitle("Add To Queue");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.ItemGIThird:
                                    MediaPlayerHelper.getInstance().AddToQueue(null,music,context);
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });


        }




        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if(type == 2 || type == 4 || type == 5){
            return keys.size();
        }
        if(music == null) return  0;
        return music.size();
    }

    ArrayList<Music> musicBackup;
    TreeMap<String,ArrayList<Music>> treeMusicBackup;
    HashMap<String,ArrayList<Music>> hashMusicBackup;
    Map<Integer,String> keysBackup = new HashMap<Integer, String>();

    public void Filter(String text){
        text = text.trim().toLowerCase();
        if(text.trim().isEmpty()){
            if(type == 1){
                music.clear();
                music.addAll(musicBackup);
            }
            else if(type == 2){
                hashMusic.clear();
                hashMusic.putAll(hashMusicBackup);
            }
            else if(type == 3){

            }
            else if(type == 4){
                treeMusic.clear();
                treeMusic.putAll(treeMusicBackup);
                keys.clear();
                keys.putAll(keysBackup);
            }
            else if(type == 5){
                treeMusic.clear();
                treeMusic.putAll(treeMusicBackup);
                keys.clear();
                keys.putAll(keysBackup);
            }
        }
        else{
            if(type == 1){
                music.clear();
                music.addAll(musicBackup);
                for(int i = 0; i < music.size(); i++) {
                    if(!music.get(i).getTitle().toLowerCase().contains(text)){
                        music.remove(i);
                        i--;
                    }
                }
            }
            else if(type == 2){
                hashMusic.clear();
                hashMusic.putAll(hashMusicBackup);
            }
            else if(type == 3){

            }
            else if(type == 4){
                treeMusic.clear();
                treeMusic.putAll(treeMusicBackup);
                keys.clear();
                keys.putAll(keysBackup);

                for(int i = 0; i < keys.size(); i++) {
                    if(treeMusic.get(keys.get(i)) != null && !treeMusic.get(keys.get(i)).get(0).getArtist().toLowerCase().contains(text)){
                        treeMusic.remove(keys.get(i));
                    }
                }

                keys.clear();
                int i = 0;
                for(Map.Entry<String, ArrayList<Music>> entry : treeMusic.entrySet()) {
                    keys.put(i, entry.getKey());
                    i++;
                }



            }
            else if(type == 5){
                treeMusic.clear();
                treeMusic.putAll(treeMusicBackup);
                keys.clear();
                keys.putAll(keysBackup);


                for(int i = 0; i < keys.size(); i++) {
                    if(treeMusic.get(keys.get(i)) != null && !treeMusic.get(keys.get(i)).get(0).getAlbum().toLowerCase().contains(text)){
                        treeMusic.remove(keys.get(i));
                    }
                }

                keys.clear();

                int i = 0;
                for(Map.Entry<String, ArrayList<Music>> entry : treeMusic.entrySet()) {
                    keys.put(i, entry.getKey());
                    i++;
                }


            }

        }

        notifyDataSetChanged();
    }
}
