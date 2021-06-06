package tusba.rhytz;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class SlideMenu extends AppCompatActivity {
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView naView;
    ActionBarDrawerToggle toggle;

    Button btnHome,btnLibrary,btnProfile,btnEqualizer,btnSettings,btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_menu);
        drawer = findViewById(R.id.drawerMenu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        naView = findViewById(R.id.navView);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,0,0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        btnHome=findViewById(R.id.menuHome);
        btnLibrary=findViewById(R.id.menuLibrary);
        btnProfile=findViewById(R.id.menuProfile);
        btnEqualizer=findViewById(R.id.menuEqualizer);
        btnLogout = findViewById(R.id.menuLogout);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_backto_home = new Intent(SlideMenu.this,MainActivity.class);
                startActivity(intent_backto_home);
            }
        });

        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_backto_library = new Intent(SlideMenu.this,MusicLibrary.class);
                startActivity(intent_backto_library);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_profile = new Intent(SlideMenu.this,ProfilePage.class);
                startActivity(intent_to_profile);
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