package com.example.mymessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginBtn1;
    private TextView signupBtn1;
    private EditText usernameLogin, passwordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Log In");

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();
        loginBtn1 = findViewById(R.id.loginBtn1);
        signupBtn1 = findViewById(R.id.signupBtn1);
        loginBtn1.setOnClickListener(this);
        signupBtn1.setOnClickListener(this);

        usernameLogin = findViewById(R.id.usernameLogin);
        passwordLogin = findViewById(R.id.passwordLogin);

        alreadyLoggedIn();


    }

    private void alreadyLoggedIn() {
        if (ParseUser.getCurrentUser()!=null){
            transitionToHomeScreen();
        }
    }

    private void transitionToHomeScreen() {
        Intent intent = new Intent(this, UsersScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case (R.id.loginBtn1):
                if (usernameLogin.getText().toString().isEmpty()
                        || passwordLogin.getText().toString().isEmpty()) {
                    FancyToast.makeText(MainActivity.this,
                            "Username and password required", Toast.LENGTH_SHORT,
                            FancyToast.INFO, false).show();
                } else {
                    ParseUser.logInInBackground(usernameLogin.getText().toString(), passwordLogin.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e==null){
                                    transitionToHomeScreen();
                                    FancyToast.makeText(MainActivity.this,
                                            "Welcome back", Toast.LENGTH_SHORT,
                                            FancyToast.SUCCESS, false).show();
                                } else {
                                FancyToast.makeText(MainActivity.this,
                                        e.getMessage(), Toast.LENGTH_SHORT,
                                        FancyToast.ERROR, false).show();
                            }
                        }
                    });
                }
                break;
            case (R.id.signupBtn1):
                Intent intent = new Intent(this, Signup.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
