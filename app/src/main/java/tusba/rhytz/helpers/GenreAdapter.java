package tusba.rhytz.helpers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import tusba.rhytz.GenericSmallLibraryActivity;
import tusba.rhytz.R;
import tusba.rhytz.models.Music;

public  class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    Context context;
    ArrayList<Music> music;
    TreeMap<String,ArrayList<Music>> treeMusic;
    Map<Integer,String> keys = new HashMap<Integer, String>();

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView textViewGenreName,textViewFooter;
        ImageView imageViewGenre;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGenreName = itemView.findViewById(R.id.textViewItemGenreName);
            textViewFooter = itemView.findViewById(R.id.textViewItemGenreFooter);
            imageViewGenre = itemView.findViewById(R.id.imageViewItemGenreAlbum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GenericSmallLibraryActivity.class);
                    intent.putExtra("music", treeMusic.get(keys.get(itemView.getTag())));
                    intent.putExtra("type", 3);
                    context.startActivity(intent);
                }
            });

        }
    }

    public GenreAdapter(Context context, ArrayList<Music> music){
        this.context = context;
        this.music = music;

        Map<String,ArrayList<Music>> mapMusic = new HashMap<String, ArrayList<Music>>();
        for(Music song: music){
            if(mapMusic.get(song.getGenre()) == null){
                mapMusic.put(song.getGenre(),new ArrayList<Music>());
            }
            mapMusic.get(song.getGenre()).add(song);
        }
        treeMusic = new TreeMap<String, ArrayList<Music>>();
        treeMusic.putAll(mapMusic);
        int i = 0;
        for(Map.Entry<String, ArrayList<Music>> entry : treeMusic.entrySet()){
            keys.put(i,entry.getKey());
            i++;
        }
    }

    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){

        if(keys.get(position) == null) return;
        ArrayList<Music> music = this.treeMusic.get(keys.get(position));
        if(music == null) return;
        holder.textViewGenreName.setText(music.get(0).getGenre());
        holder.textViewFooter.setText(music.size() +" Songs");
        //holder.imageViewGenre.setImageIcon((music.getArtist()));



        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return treeMusic.size();
    }
}
