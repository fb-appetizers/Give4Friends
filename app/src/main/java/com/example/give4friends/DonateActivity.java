package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.give4friends.Adapters.DonateAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DonateActivity extends AppCompatActivity {
    private EditText searchFriend;
    private Button searchBtn;
    private RecyclerView rvFriends;
    public static ParseUser friend;
    private ImageButton cancel;

    ArrayList<ParseUser> friends;
    DonateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        searchFriend = findViewById(R.id.searchFriend);
        searchBtn = findViewById(R.id.searchBtn);
        rvFriends = findViewById(R.id.rvFriends);
        cancel = findViewById(R.id.cancel);

        friends = new ArrayList<ParseUser>();

        adapter = new DonateAdapter(friends);

        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryFriends(searchFriend.getText().toString());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void queryFriends(String name){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContains("username", name);
//
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e != null){
                    Log.e("DonateAdapter", "Error with query");
                    e.printStackTrace();
                    return;
                }
                friends.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
