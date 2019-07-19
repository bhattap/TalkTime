package com.example.mymessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private Button signupBtn2;
    private EditText emailSignup, usernameSignup, passwordSignup, firstNameSignup, lastNameSignup;
    private TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");

        signupBtn2 = findViewById(R.id.signupBtn2);
        emailSignup = findViewById(R.id.emailSignup);
        usernameSignup = findViewById(R.id.usernameSignup);
        passwordSignup = findViewById(R.id.passwordSignup);
        backToLogin = findViewById(R.id.backToLogin);
        firstNameSignup = findViewById(R.id.firstNameSignup);
        lastNameSignup = findViewById(R.id.lastNameSignup);

        signupBtn2.setOnClickListener(this);
        backToLogin.setOnClickListener(this);
    }

    private void transitionToHomeScreen() {
        Intent intent = new Intent(this, TabbedScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case (R.id.signupBtn2):
                if (emailSignup.getText().toString().isEmpty()
                        || usernameSignup.getText().toString().isEmpty()
                        || passwordSignup.getText().toString().isEmpty()
                        || firstNameSignup.getText().toString().isEmpty()
                        || lastNameSignup.getText().toString().isEmpty()) {
                    FancyToast.makeText(this, "All fields required",
                            Toast.LENGTH_SHORT, FancyToast.INFO,
                            false).show();
                } else {
                    signupNewUser();
                }
                break;

            case (R.id.backToLogin):
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    private void signupNewUser() {
        ParseUser newUSer = new ParseUser();
        newUSer.setEmail(emailSignup.getText().toString());
        newUSer.setUsername(usernameSignup.getText().toString());
        newUSer.setPassword(passwordSignup.getText().toString());
        newUSer.put("firstName", firstNameSignup.getText().toString());
        newUSer.put("lastName", lastNameSignup.getText().toString());
        newUSer.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    transitionToHomeScreen();
                    FancyToast.makeText(Signup.this, "Welcome " + firstNameSignup.getText(),
                            Toast.LENGTH_SHORT, FancyToast.SUCCESS,
                            false).show();
                }else{
                    FancyToast.makeText(Signup.this, e.getMessage(),
                            Toast.LENGTH_LONG, FancyToast.ERROR,
                            false).show();
                }
            }
        });
    }
}
