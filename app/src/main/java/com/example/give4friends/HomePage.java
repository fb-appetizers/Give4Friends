package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.parse.ParseUser;

public class HomePage extends AppCompatActivity {
    private TextView hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        hello = findViewById(R.id.hello);

        hello.setText("Hello " + ParseUser.getCurrentUser().getUsername() + "!");
    }
}
