package tusba.rhytz.model;

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

import tusba.rhytz.MusicPlayer;
import tusba.rhytz.R;

public  class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    Context context;
    ArrayList<Music> music;

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView textViewGenreName;
        ImageView imageViewGenre;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGenreName = itemView.findViewById(R.id.textViewItemGenreName);
            imageViewGenre = itemView.findViewById(R.id.imageViewItemGenreAlbum);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MusicPlayer.class);
                    intent.putExtra("song", music.get(Integer.parseInt(itemView.getTag().toString())));
                    context.startActivity(intent);

                }
            });*/

        }
    }

    public GenreAdapter(Context context, ArrayList<Music> music){
        this.context = context;
        this.music = music;
    }

    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Music music = this.music.get(position);
        holder.textViewGenreName.setText(music.getGenre());
        //holder.imageViewGenre.setImageIcon((music.getArtist()));

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return music.size();
    }
}
