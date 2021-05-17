package tusba.rhytz.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    Map<Integer,String> keys = new HashMap<Integer, String>();


    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView textViewHeader;
        TextView textViewFooter;
        ImageView imageViewImage;
        Button buttonDetails;

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
                        Intent intent = new Intent(context, MusicPlayer.class);
                        intent.putExtra("song", music.get(Integer.parseInt(itemView.getTag().toString())));
                        context.startActivity(intent);
                    }
                    else if(type == 4 || type == 5){
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

        if(type == 4){
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
            for(Map.Entry<String, ArrayList<Music>> entry : treeMusic.entrySet()){
                keys.put(i,entry.getKey());
                i++;
            }
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
            Drawable drawable = context.getResources().getDrawable(R.drawable.gradient_1);
            holder.imageViewImage.setImageDrawable(drawable);
            holder.textViewHeader.setText(music.getTitle());
            holder.textViewFooter.setText(music.getArtist());
        }
        else if(type == 2){


        }
        else if(type == 4){
            if(keys.get(position) == null) return;
            ArrayList<Music> music = this.treeMusic.get(keys.get(position));
            if(music == null) return;
            Drawable drawable = context.getResources().getDrawable(R.drawable.gradient_9);
            holder.imageViewImage.setImageDrawable(drawable);
            holder.textViewHeader.setText(music.get(0).getArtist());
            holder.textViewFooter.setText(music.size()+" songs");
            Drawable img = Drawable.createFromPath(music.get(0).getAlbumArt());
            holder.imageViewImage.setImageDrawable(img);


        }
        else if(type == 5){
            if(keys.get(position) == null) return;
            ArrayList<Music> music = this.treeMusic.get(keys.get(position));
            if(music == null) return;

            Drawable drawable = context.getResources().getDrawable(R.drawable.gradient_6);
            holder.imageViewImage.setImageDrawable(drawable);
            holder.textViewHeader.setText(music.get(0).getAlbum());
            holder.textViewFooter.setText(music.size()+" songs");

        }

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if(type == 4 || type == 5){
            return keys.size();
        }
        if(music == null) return  0;
        return music.size();
    }
}
