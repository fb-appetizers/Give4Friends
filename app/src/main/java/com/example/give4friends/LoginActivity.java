package com.example.give4friends;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.give4friends.models.Transaction;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private Button signUp;

    ProgressBar loadingProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.createAccount);
        loadingProgressBar = findViewById(R.id.loading);



        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, Main_Fragment_Branch.class);
            startActivity(intent);

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            login(username.getText().toString(), password.getText().toString());
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }


    @Override
    protected void onResume() {
        super.onResume();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, Main_Fragment_Branch.class);
            startActivity(intent);
        }
    }

    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Log.d("login", "Login Successful");
                    Intent intent = new Intent(LoginActivity.this, Main_Fragment_Branch.class);
                    startActivity(intent);

                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    user.saveInBackground();
                    user.saveInBackground(); // does it save onto phone
                }
                else{
                    Log.e("login", "Login failure.");
                    Toast.makeText(getApplicationContext(), "Login Failure", Toast.LENGTH_SHORT).show();
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
                }
        });
    }
}
