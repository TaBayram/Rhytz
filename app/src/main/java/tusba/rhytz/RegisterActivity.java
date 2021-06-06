package tusba.rhytz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import tusba.rhytz.models.ThemePreference;
import tusba.rhytz.models.User;
import java.util.Hashtable;
import java.util.List;

import tusba.rhytz.models.User;
import tusba.rhytz.models.FirebaseClass;
import tusba.rhytz.models.FirebaseInterface;
import tusba.rhytz.models.Music;
import tusba.rhytz.models.Musician;

public class RegisterActivity extends AppCompatActivity implements FirebaseInterface {

    FirebaseClass firebase;
    TextView tvMessage;
    EditText etName,etSurname, etEmail, etUsername, etPassword, etPasswordReenter;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    Button btnRegister, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemePreference themePreference = new ThemePreference(this);
        setTheme(themePreference.GetThemePreferenceInt());
        setContentView(R.layout.register);

        TakeComponents();


        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    public void InputControlAddUser(){
        tvMessage.setVisibility(View.GONE);
        tvMessage.setText("");
        String name = etName.getText().toString().trim();
        String surname=etSurname.getText().toString().trim();
        String username=etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordreenter = etPasswordReenter.getText().toString().trim();
        String gender ="";

        if (rgGender.getCheckedRadioButtonId()!=-1)
        {
            if (rgGender.getCheckedRadioButtonId()==R.id.rbFemale)
            {
                gender="Male";
            }
            else
            {
                gender = "Female";
            }
        }

        if (name.isEmpty())
        {
            tvMessage.setText("Enter Name");
            tvMessage.setTextColor(Color.RED);
            etName.requestFocus();
            tvMessage.setVisibility(View.VISIBLE);
            return;
        }

        if (email.isEmpty())
        {
            tvMessage.setText("Enter email");
            tvMessage.setTextColor(Color.RED);
            etEmail.requestFocus();
            tvMessage.setVisibility(View.VISIBLE);
            return;
        }

        if(password.isEmpty() || passwordreenter.isEmpty() || !password.equals(passwordreenter) )
        {
            tvMessage.setText("Password does not match");
            tvMessage.setTextColor(Color.RED);
            etPassword.requestFocus();
            tvMessage.setVisibility(View.VISIBLE);
            return;
        }

        if (gender.isEmpty())
        {
            tvMessage.setText("Select Gender");
            tvMessage.setTextColor(Color.RED);
            tvMessage.setVisibility(View.VISIBLE);
            return;
        }

        User user = new User("0",name,surname,email,username,password,gender);
        firebase.AddUserToFirebase(this,user);

        tvMessage.setText("User Created Succesfuly");
        tvMessage.setTextColor(Color.GREEN);
        tvMessage.setVisibility(View.VISIBLE);
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    void TakeComponents(){

        firebase = new FirebaseClass(this.getApplicationContext());
        tvMessage =  (TextView) findViewById(R.id.messageTextForRegister);
        etName =  (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etEmail = findViewById(R.id.etEmailForRegister);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPasswordForRegister);
        etPasswordReenter = (EditText) findViewById(R.id.rePassword);

        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);

        btnBack = (Button) findViewById(R.id.btnBackToLogin);
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
    public void GetUserInfoWithMailResult(tusba.rhytz.models.User user) {

    }

    @Override
    public void CheckMailExistResult(boolean result) {
        tvMessage =  (TextView) findViewById(R.id.messageTextForRegister);
        if(result){
            tvMessage.setText("Aynı mail kullanımda !");
            tvMessage.setTextColor(Color.RED);
            tvMessage.setVisibility(View.VISIBLE);
        }
        else{
            String username = etUsername.getText().toString();
            firebase.CheckUsernamelExist(this,username);
        }
    }

    @Override
    public void CheckUsernameExistResult(boolean result) {
        tvMessage =  (TextView) findViewById(R.id.messageTextForRegister);
        if(result){
            tvMessage.setText("Aynı kullanıcı adı kullanımda !");
            tvMessage.setTextColor(Color.RED);
            tvMessage.setVisibility(View.VISIBLE);
        }
        else{
           InputControlAddUser();
        }
    }

    @Override
    public void LoginToAppResult(User user) {

    }

    @Override
    public void UpdateUser(boolean result) {

    }

    @Override
    public void GetUserResult(User user) {

    }


    @Override
    public void TESTINT(List<String> list) {

    }

    public void AddUserToFirebase(View view){
        String mail = etEmail.getText().toString();
        firebase.CheckMailExist(this,mail);
    }

    public void ShowToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}