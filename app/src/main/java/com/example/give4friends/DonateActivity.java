package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.give4friends.Adapters.DonateAdapter;
import com.example.give4friends.models.Charity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DonateActivity extends AppCompatActivity implements Serializable {
    private EditText searchFriend;
    private Button cancelSearchBtn;
    private RecyclerView rvFriends;
    public static ParseUser currentFriend;
    public static Charity currentCharity;
    public static boolean donateNow;
    public static String charityName2;
    private ImageButton cancel;

    ArrayList<ParseUser> friends;
    DonateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        Intent intent = getIntent();
        donateNow = intent.getBooleanExtra("donateNow", false);

        searchFriend = findViewById(R.id.searchFriend);
        cancelSearchBtn = findViewById(R.id.cancelSearchBtn);
        rvFriends = findViewById(R.id.rvFriends);
        cancel = findViewById(R.id.ibcancelFinal);

        friends = new ArrayList<ParseUser>();

        adapter = new DonateAdapter(friends);

        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));


        cancelSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFriend.clearFocus();
                searchFriend.getText().clear();





            }
        });

        searchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {

                if (count == 0 ){
                    friends.clear();
                    adapter.notifyDataSetChanged();

                }
                if(count > 0 ){

                    queryFriends(searchFriend.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }

    protected void queryFriends(String name){
        ParseQuery<ParseUser> query1 = ParseUser.getQuery();

        ParseQuery<ParseUser> q1 = query1.whereContains("username", name);

        ParseQuery<ParseUser> query2 = ParseUser.getQuery();
        ParseQuery<ParseUser> q2 = query2.whereContains("firstName", name);

        List<ParseQuery<ParseUser>> queries = new ArrayList<>();

        queries.add(q2);
        queries.add(q1);


        ParseQuery<ParseUser> main_query = ParseQuery.or(queries);

        main_query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if(e != null){
                    Log.e("DonateAdapter", "Error with query");
                    e.printStackTrace();
                    return;
                }
                friends.clear();
                friends.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
