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

public class LoginActivity extends AppCompatActivity {
    Button btRegister,btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        CheckBox remember = findViewById(R.id.cbRemember);
        btRegister =  findViewById(R.id.registerToLogin);
        btnLogin = findViewById(R.id.btnLogin);

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
            Intent intent_sign_control = new Intent(LoginActivity.this,MainActivity.class);
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
    public void UserLogin(View v)
    {
        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);
        TextView tvMessage =  (TextView) findViewById(R.id.messageText);
        String username = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.equals("")){
            tvMessage.setText("Enter your User Name.");
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setTextColor(Color.RED);
            etEmail.requestFocus();
            return;
        }
        if (password.equals("")){
            tvMessage.setText("Enter your Password.");
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setTextColor(Color.RED);
            etPassword.requestFocus();
            return;
        }
        Intent intent_to_login = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent_to_login);





    }

}