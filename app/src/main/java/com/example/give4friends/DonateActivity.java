package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.give4friends.Adapters.DonateAdapter;
import com.example.give4friends.models.User;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DonateActivity extends AppCompatActivity {
    private EditText searchFriend;
    private Button searchBtn;
    private RecyclerView rvFriends;
    public static ParseUser friend;

    ArrayList<ParseUser> friends;
    DonateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        searchFriend = findViewById(R.id.searchFriend);
        searchBtn = findViewById(R.id.searchBtn);
        rvFriends = findViewById(R.id.rvFriends);

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

//        searchFriends.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                queryFriends(searchFriends.getText);
//                return false;
//            }
//        });
//        searchFriends.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextChange(String s) {
//                friends.clear();
//                queryFriends(s);
//                return true; // check later if suppose to be true or false
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                queryFriends(s);
//                return true;
//            }
//        });
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
//
//        query.whereContains("firstName", name);
//
//        query.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> objects, ParseException e) {
//                if(e != null){
//                    Log.e("DonateAdapter", "Error with query");
//                    e.printStackTrace();
//                    return;
//                }
//                friends.addAll(objects);
//            }
//        });
//
//        query.whereEqualTo("lastName", name);
//
//        query.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> objects, ParseException e) {
//                if(e != null){
//                    Log.e("DonateAdapter", "Error with query");
//                    e.printStackTrace();
//                    return;
//                }
//                friends.addAll(objects);
//            }
//        });
    }
}
