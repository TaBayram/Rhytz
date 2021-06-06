package tusba.rhytz.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import tusba.rhytz.R;
import tusba.rhytz.helpers.MediaPlayerHelper;

public class ThemePreference {
    Context context;

    public ThemePreference(Context context) {
        this.context = context;
    }

    public boolean SetThemePreference(String theme){
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE); //getSharedPreferences(Preference_name, MODE_PRIVATE) birden fazla kayıt için kullanılır.
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("theme",String.valueOf(theme));

            editor.apply();

            return true;
        }
        catch (Exception ex){ Log.i("hata",ex.getMessage());return false;}
    }

    public String GetThemePreference(){
        try{
            return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("theme", "Dark");
        }
        catch (Exception ex){return "Dark";}
    }


    public int GetThemePreferenceInt(){
        try{
            String theme = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("theme", "Dark");

            if(theme.equals("Lollipop")){
                return R.style.Theme_Rhytz_Lollipop;
            }
            else if(theme.equals("Dark")){
                return R.style.Theme_Rhytz_Dark;
            }
            else if(theme.equals("Light")){
                return R.style.Theme_Rhytz_Light;
            }
            else{
                return  R.style.Theme_Rhytz_Dark;
            }
        }
        catch (Exception ex){return 0;}

    }

}
