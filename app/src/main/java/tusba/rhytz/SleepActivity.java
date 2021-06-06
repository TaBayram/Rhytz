package tusba.rhytz;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import tusba.rhytz.helpers.MediaPlayerHelper;

public class SleepActivity extends SlideMenu {

    Button buttonReset,buttonStop,buttonStart;
    TextView textViewCurrent;
    EditText editTextTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater= LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_sleep,null,false);
        drawer.addView(v,0);

        buttonStop = findViewById(R.id.buttonSTStop);
        buttonReset = findViewById(R.id.buttonSTReset);
        buttonStart = findViewById(R.id.buttonSTStart);
        textViewCurrent = findViewById(R.id.textViewSTCurrent);
        editTextTime = findViewById(R.id.editTextTimeSTSet);
        Context context = this;
        MediaPlayerHelper mediaPlayerHelper = MediaPlayerHelper.getInstance();
        if(mediaPlayerHelper.isSleepTimerOn){
            CurrentTimer();
        }


        Button buttonDrawer = findViewById(R.id.buttonSTRhytz);

        buttonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);

            }
        });


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = editTextTime.getText().toString();
                String[] times = time.split(":");

                int minute = 0;
                int j = 0;
                for(int i = times.length-1; i >= 0; i--){
                    try {
                        if (j == 0)
                            minute += Integer.parseInt(times[i]);
                        else if (j == 1)
                            minute += Integer.parseInt(times[i]) * 60;
                        else if (j == 2)
                            minute += Integer.parseInt(times[i]) * 360;
                    }
                    catch (Exception e){

                    }
                    j++;
                }
                if(minute <= 0) return;
                mediaPlayerHelper.StartSleepTimer(minute);
                CurrentTimer();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayerHelper.isSleepTimerOn) {
                    mediaPlayerHelper.StopSleepTimer();
                    buttonStop.setText("Resume");
                }
                else {
                    mediaPlayerHelper.StartSleepTimer(0);
                    buttonStop.setText("Pause");
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerHelper.StopSleepTimer();
                mediaPlayerHelper.sleepTimeCountdown = mediaPlayerHelper.sleepTime;
                buttonStop.setText("Resume");
            }
        });

    }


    private void CurrentTimer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    textViewCurrent.setText(MediaPlayerHelper.getInstance().GetTimeWithMiliSecond(MediaPlayerHelper.getInstance().sleepTimeCountdown*1000));
                });
            }
        }, 000, 100);
    }
}