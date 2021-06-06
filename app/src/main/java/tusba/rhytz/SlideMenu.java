package tusba.rhytz;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import tusba.rhytz.helpers.MediaPlayerHelper;
import tusba.rhytz.models.ThemePreference;

public class SlideMenu extends AppCompatActivity {
    DrawerLayout drawer;

    NavigationView naView;
    ActionBarDrawerToggle toggle;

    Button btnHome,btnProfile,btnEqualizer,btnSleepTimer,btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ThemePreference themePreference = new ThemePreference(this);
        setTheme(themePreference.GetThemePreferenceInt());

        setContentView(R.layout.activity_slide_menu);
        drawer = findViewById(R.id.drawerMenu);
        naView = findViewById(R.id.navView);
        drawer.addDrawerListener(toggle);


        btnHome=findViewById(R.id.menuHome);
        btnSleepTimer=findViewById(R.id.menuSleepTimer);
        btnProfile=findViewById(R.id.menuProfile);
        btnEqualizer=findViewById(R.id.menuEqualizer);
        btnLogout = findViewById(R.id.menuLogout);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SlideMenu.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        btnSleepTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SlideMenu.this,SleepActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SlideMenu.this,ProfilePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        btnEqualizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int  sessionId = MediaPlayerHelper.getInstance().getMediaPlayer().getAudioSessionId();
                Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);

                startActivityForResult(intent,123);

            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember","false");
                editor.apply();

                finish();

                Intent intent_backto_login = new Intent(SlideMenu.this,LoginActivity.class);
                intent_backto_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_backto_login);
            }
        });

    }
    //public boolean onTouchEvent(MotionEvent touchEvent){
    //    switch (touchEvent.getAction()){
    //        case MotionEvent.ACTION_DOWN:
    //            x1=touchEvent.getX();
    //            y1=touchEvent.getY();
    //            break;
    //        case MotionEvent.ACTION_UP:
    //            x2=touchEvent.getX();
    //            y2=touchEvent.getY();
    //            if(x1<x2){
    //                drawer.openDrawer(Gravity.LEFT);
    //            }
    //            if(x1>x2){
    //                drawer.closeDrawer(Gravity.LEFT);
    //            }
    //            break;
    //    }
    //    return true;
    //}


}