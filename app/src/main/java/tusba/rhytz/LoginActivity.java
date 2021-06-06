package tusba.rhytz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;
import java.util.List;

import tusba.rhytz.models.FirebaseClass;
import tusba.rhytz.models.FirebaseInterface;
import tusba.rhytz.models.Music;
import tusba.rhytz.models.Musician;
import tusba.rhytz.models.MyUser;
import tusba.rhytz.models.User;

public class LoginActivity extends AppCompatActivity implements FirebaseInterface {

    FirebaseClass firebase;
    Button btRegister,btnLogin;
    EditText etEmail, etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ShowToast("djlsgdsg");
        firebase = new FirebaseClass(this.getApplicationContext());

        CheckBox remember = findViewById(R.id.cbRemember);
        btRegister =  findViewById(R.id.registerToLogin);
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent_to_register = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent_to_register);
            }
        });

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if(checkbox.equals("true")){
            Intent intent_sign_control = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent_sign_control);
        }
        else if(checkbox.equals("false")){
            Toast.makeText(this,"Please sign in.", Toast.LENGTH_SHORT).show();
        }


        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked())
                {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Checked", Toast.LENGTH_SHORT).show();
                }
                else if(!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "UnChecked", Toast.LENGTH_SHORT).show();
                }
            }
        });



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
        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);
        TextView tvMessage =  (TextView) findViewById(R.id.messageText);
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(user != null){
            MyUser.myUser = user;
            Intent intent_to_login = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent_to_login);
        }
        else{
            if (email.equals("")){
                tvMessage.setText("Enter Your E-mail");
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setTextColor(Color.RED);
                etEmail.requestFocus();
                return;
            }
            if (password.equals("")){
                tvMessage.setText("Enter Your Password");
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setTextColor(Color.RED);
                etPassword.requestFocus();
                return;
            }
        }
        Intent intent_to_login = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intent_to_login);

    @Override
    public void UpdateUser(boolean result) {

    }

    @Override
    public void GetUserResult(User user) {

    }

    @Override
    public void TESTINT(List<String> list) {

    }

    public void LoginToApp(View view){
        String mail = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        firebase.LoginToApp(this,mail,password);
    }
    public void ShowToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}