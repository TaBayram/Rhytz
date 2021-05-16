package tusba.rhytz.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tusba.rhytz.MusicPlayer;
import tusba.rhytz.R;

public  class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    Context context;
    ArrayList<Music> music;

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView textViewTitle,textViewDuration,textViewArtist;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewItemGenericHeader);
            textViewDuration = itemView.findViewById(R.id.textViewItemGenericFoot);
            textViewArtist = itemView.findViewById(R.id.textViewItemArtist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MusicPlayer.class);
                    intent.putExtra("song", music.get(Integer.parseInt(itemView.getTag().toString())));
                    context.startActivity(intent);

                }
            });

        }
    }

    public MusicAdapter(Context context, ArrayList<Music> music){
        this.context = context;
        this.music = music;
    }

    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Music music = this.music.get(position);
        holder.textViewTitle.setText(music.getTitle());
        holder.textViewArtist.setText((music.getArtist()));
        holder.textViewDuration.setText((music.getDurationFormatted()));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return music.size();
    }
}
