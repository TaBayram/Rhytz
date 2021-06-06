package tusba.rhytz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;
import java.util.List;

import tusba.rhytz.models.FirebaseClass;
import tusba.rhytz.models.FirebaseInterface;
import tusba.rhytz.models.Music;
import tusba.rhytz.models.Musician;
import tusba.rhytz.models.ThemePreference;
import tusba.rhytz.models.User;
import tusba.rhytz.models.MyUser;

public class ProfilePage extends SlideMenu implements FirebaseInterface {
    FirebaseClass firebase;
    EditText name,surname,email,username,password;
    RadioGroup rgGenderProfile,rgTheme;
    RadioButton rbMale,rbFemale,rbDark,rbLight,rbLollipop;
    Button update;
    User newUser;
    ThemePreference themePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater= LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_profile_page,null,false);
        drawer.addView(v,0);

        firebase = new FirebaseClass(this);

        name = (EditText) findViewById(R.id.prName);
        surname = (EditText) findViewById(R.id.prSurname);
        email = (EditText) findViewById(R.id.prMail);
        username = (EditText) findViewById(R.id.prUsername);
        password = (EditText) findViewById(R.id.prPassword);

        rgGenderProfile = (RadioGroup) findViewById(R.id.rgGenderProfile);
        rgTheme = (RadioGroup) findViewById(R.id.rgTheme);

        rbMale = (RadioButton) findViewById(R.id.rbMaleProfile);
        rbFemale = (RadioButton) findViewById(R.id.rbFemaleProfile);
        rbDark = (RadioButton) findViewById(R.id.rbDark);
        rbLight = (RadioButton) findViewById(R.id.rbLight);
        rbLollipop = (RadioButton) findViewById(R.id.rbLollipop);

        update = (Button) findViewById(R.id.prUpdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUser();
            }
        });

        themePreference = new ThemePreference(this);

        SetUserInfo();

    }

    public void SetUserInfo(){
        User myUser = MyUser.myUser;

        name.setText(myUser.getName());
        surname.setText(myUser.getSurname());
        email.setText(myUser.getEmail());
        username.setText(myUser.getUsername());
        password.setText(myUser.getPassword());

        if(myUser.getGender().equals("Male")){
             rbMale.setChecked(true);} else {  rbFemale.setChecked(true);}

        String theme = themePreference.GetThemePreference();
        if(theme == "error"){theme = "Dark";}

        if(theme == "Dark"){rbDark.setChecked(true);}
        else if(theme == "Light"){rbLight.setChecked(true);}
        else if(theme == "Lollipop"){rbLollipop.setChecked(true);}
        else{rbDark.setChecked(true);}
    }

    public void UpdateUser(){

        String oldTheme = themePreference.GetThemePreference();
        String newTheme = "";

        if(rgTheme.getCheckedRadioButtonId() == rbDark.getId()){newTheme = "Dark";}
        else if(rgTheme.getCheckedRadioButtonId() == rbLight.getId()){newTheme = "Light";}
        else if(rgTheme.getCheckedRadioButtonId() == rbLollipop.getId()){newTheme = "Lollipop";}

        if(!oldTheme.equals(newTheme)) {themePreference.SetThemePreference(newTheme);}

       String newName = name.getText().toString();
       String newSurname = surname.getText().toString();
       String newEmail = email.getText().toString();
       String newUsername = username.getText().toString();
       String newPassword = password.getText().toString();
       String newGender = "";

       if(rgGenderProfile.getCheckedRadioButtonId() == rbMale.getId()){  Log.i("test","erkek"); newGender = "Male";} else{ Log.i("test","kadın"); newGender = "Female";}

       newUser = new User( MyUser.myUser.getId(),newName,newSurname,newEmail,newUsername,newPassword,newGender);

       firebase.UpdateUser(this,newUser);

    }

    @Override
    public void AddAudioToFirebaseResult(boolean result) {

    }

    @Override
    public void AddMusicianToFirebaseResult(boolean result) {

    }

    @Override
    public void AddUserToFirebaseResult(boolean result) {

    }

    @Override
    public void GetCategoriesResult(Hashtable<String, String> list) {

    }

    @Override
    public void GetAllMusicResult(List<Music> list) {

    }

    @Override
    public void GetMusicWithGenreResult(List<Music> list) {

    }

    @Override
    public void GetMusicWithMusicianIdResult(List<Music> list) {

    }

    @Override
    public void GetAllMusicianResult(List<Musician> list) {

    }

    @Override
    public void GetMusicianWithIdResult(List<Musician> list) {

    }

    @Override
    public void GetUserInfoWithMailResult(User user) {

    }

    @Override
    public void CheckMailExistResult(boolean result) {

    }

    @Override
    public void CheckUsernameExistResult(boolean result) {

    }

    @Override
    public void LoginToAppResult(User user) {

    }

    @Override
    public void UpdateUser(boolean result) {
        if(result){ MyUser.myUser = newUser;  ShowToast("Bilgiler Güncellendi !");}
         else{ShowToast("Bilgiler Güncellenirken Hata Meydana Gedli !");}
    }

    @Override
    public void GetUserResult(User user) {

    }

    @Override
    public void TESTINT(List<String> list) {

    }

    public void ShowToast(String message){
        Toast.makeText(this,String.valueOf(message),Toast.LENGTH_LONG).show();
    }
}