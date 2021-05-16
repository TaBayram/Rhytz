package tusba.rhytz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class SettingsActivity extends SlideMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater= LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_settings,null,false);
        drawer.addView(v,0);
    }
}