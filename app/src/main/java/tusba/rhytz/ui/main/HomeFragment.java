package tusba.rhytz.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import tusba.rhytz.R;

import tusba.rhytz.helpers.MediaPlayerHelper;


/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

    Button buttonLocal,buttonOnline,buttonMix;


    public HomeFragment() {

    }

    public static HomeFragment newInstance(int position) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        buttonLocal = getView().findViewById(R.id.buttonFragmentHomeLocal);
        buttonMix = getView().findViewById(R.id.buttonFragmentHomeMix);
        buttonOnline = getView().findViewById(R.id.buttonFragmentHomeStream);
        Context context = getContext();

        buttonOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerHelper.getInstance().setFilterSetting(2);
                Toast.makeText(context,"Stream Only!",Toast.LENGTH_SHORT).show();

            }

        });

        buttonLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerHelper.getInstance().setFilterSetting(1);
                Toast.makeText(context,"Local Only!",Toast.LENGTH_SHORT).show();
            }
        });

        buttonMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerHelper.getInstance().setFilterSetting(0);
                Toast.makeText(context,"Mix!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Refresh(){

    }



}