package tusba.rhytz.models;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import tusba.rhytz.R;
import tusba.rhytz.ShowMusics;

public class MusicAdapter extends ArrayAdapter {

    List<Music> musicList;
    Context context;
    int resourceID;
    final MusicAdapter adapterClass = this;

    public MusicAdapter(@NonNull Context context, int resource, @NonNull List<Music> list){
        super(context, resource, list);

        this.musicList = list;
        this.context = context;
        this.resourceID = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(resourceID,null,false);

        TextView musicNameTXT = (TextView) view.findViewById(R.id.musicNameTXT);
        TextView durationTXT = (TextView) view.findViewById(R.id.durationTXT);

        final Music music = musicList.get(position);

        musicNameTXT.setText(music.getName());
        durationTXT.setText(music.getDuration());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"basıldı",Toast.LENGTH_LONG).show();
                try {
                    ((ShowMusics)context).PlayMusic(musicList.get(position));
                } catch (IOException e) {
                    Log.i(""," HATA");
                }
            }
        });

        return view;
    }


}
