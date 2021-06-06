package tusba.rhytz.helpers;

import android.widget.SeekBar;

import tusba.rhytz.models.Music;

public interface PlayerListenerHelper {

    void SetState(boolean isPlaying);
    void SetDuration(String duration);
    void SetCurrent(String duration);
    void SetInformation(Music music);
    void SetSeekBar(int percentage);
}
