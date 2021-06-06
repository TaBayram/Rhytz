package tusba.rhytz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
<<<<<<< HEAD

import tusba.rhytz.models.User;

=======

import tusba.rhytz.model.User;

>>>>>>> parent of 014f814 (v 0.1.3)
public class RegisterActivity extends AppCompatActivity {
    TextView tvMessage;
    EditText etName,etSurname, etEmail, etUsername, etPassword, etPasswordReenter;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    Button btnRegister, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void InputControlAddUser(View v){
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
            if (rgGender.getCheckedRadioButtonId()==R.id.rbMale)
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

            User user = new User();
            user.setName(name);
            user.setSurname(surname);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setGender(gender);

            tvMessage.setText("User Created Succesfuly");
            tvMessage.setTextColor(Color.GREEN);
            tvMessage.setVisibility(View.VISIBLE);



    }
    void TakeComponents(){
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
}