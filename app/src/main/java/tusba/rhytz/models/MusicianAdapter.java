package tusba.rhytz.models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import tusba.rhytz.R;
import tusba.rhytz.ShowMusicians;
import tusba.rhytz.ShowMusics;

public class MusicianAdapter extends ArrayAdapter {

    List<Musician> musicianList;
    Context context;
    int resourceID;
    final MusicianAdapter adapterClass = this;

    public MusicianAdapter(@NonNull Context context, int resource, @NonNull List<Musician> list){
        super(context, resource, list);

        this.musicianList = list;
        this.context = context;
        this.resourceID = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(resourceID,null,false);

        TextView musicianNameTxt = (TextView) view.findViewById(R.id.musician_nameTXT);
        TextView musicianIdTXT = (TextView) view.findViewById(R.id.musician_idTXT);


        final Musician musician = musicianList.get(position);

        musicianNameTxt.setText(musician.getName());
        musicianIdTXT.setText(musician.getId());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"basıldı",Toast.LENGTH_LONG).show();
                try {
                    ((ShowMusicians)context).ShowMusiciansMusic(musicianList.get(position));
                } catch (IOException e) {
                    Log.i(""," HATA");
                }
            }
        });

        return view;
    }


}
