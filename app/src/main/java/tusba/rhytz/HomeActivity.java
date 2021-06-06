package tusba.rhytz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import tusba.rhytz.helpers.MediaPlayerHelper;
import tusba.rhytz.models.Music;
import tusba.rhytz.models.ThemePreference;
import tusba.rhytz.ui.main.GenresFragment;
import tusba.rhytz.ui.main.SectionsPagerAdapter;
import tusba.rhytz.ui.main.SongsFragment;

public class HomeActivity extends SlideMenu {

    private static final int PERMISSION_REQUEST = 1;
    public ArrayList<Music> music;
    public View bottomPlayer;
    TabLayout tabs;
    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;
    SearchView searchView;
    int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemePreference themePreference = new ThemePreference(this);
        setTheme(themePreference.GetThemePreferenceInt());



        LayoutInflater inflater= LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_home,null,false);
        drawer.addView(v,0);





        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        searchView = findViewById(R.id.searchViewHome);

        Button buttonDrawer = findViewById(R.id.buttonHomeTitle);

        buttonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);

            }
        });



        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
            else{
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        }
        else{
            Music();
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if(position == 2 && MediaPlayerHelper.getInstance().isPlaylistChanged){
                    for(int i = 0; i < sectionsPagerAdapter.fragments.size(); i++){
                        if(sectionsPagerAdapter.fragments.get(i) instanceof  SongsFragment && ((SongsFragment) sectionsPagerAdapter.fragments.get(i)).getType() == currentPage)
                            ((SongsFragment)sectionsPagerAdapter.fragments.get(i)).Refresh();
                    }
                    MediaPlayerHelper.getInstance().isPlaylistChanged = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(int i = 0; i < sectionsPagerAdapter.fragments.size(); i++){
                    if(sectionsPagerAdapter.fragments.get(i) instanceof  SongsFragment && ((SongsFragment) sectionsPagerAdapter.fragments.get(i)).getType() == currentPage)
                        ((SongsFragment)sectionsPagerAdapter.fragments.get(i)).Filter(query);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    for(int i = 0; i < sectionsPagerAdapter.fragments.size(); i++){
                        if(sectionsPagerAdapter.fragments.get(i) instanceof  SongsFragment && ((SongsFragment) sectionsPagerAdapter.fragments.get(i)).getType() == currentPage)
                            ((SongsFragment)sectionsPagerAdapter.fragments.get(i)).Filter(newText);

                    }
                }
                return false;
            }
        });


    }

    private void Music(){
        MediaPlayerHelper.getInstance().SetLocalMusic(this);
        MediaPlayerHelper.getInstance().SetOnlineMusic(this);
        music = MediaPlayerHelper.getInstance().GetAllMusic();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                        Music();
                    }
                    else{
                        Toast.makeText(this,"Permission Couldn't be Granted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return;
                }
            }
        }
    }


    public void Refresh(){

        for(int i = 0; i < sectionsPagerAdapter.fragments.size(); i++){
            if(sectionsPagerAdapter.fragments.get(i) instanceof  SongsFragment)
                ((SongsFragment)sectionsPagerAdapter.fragments.get(i)).Refresh();
            else if(sectionsPagerAdapter.fragments.get(i) instanceof GenresFragment)
                ((GenresFragment)sectionsPagerAdapter.fragments.get(i)).Refresh();
        }

    }

    public void Theme(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
