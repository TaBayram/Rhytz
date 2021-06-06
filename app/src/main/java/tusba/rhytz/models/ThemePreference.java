package tusba.rhytz.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
            return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("theme", "error");
        }
        catch (Exception ex){return "error";}
    }

}
