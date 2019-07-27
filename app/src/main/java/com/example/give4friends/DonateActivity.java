package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.example.give4friends.Adapters.FavCharitiesAdapter;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class DonateActivity extends AppCompatActivity implements Serializable {
    private EditText searchFriend;
    private Button cancelSearchBtn;
    protected RecyclerView rvFriends;
    public static ParseUser currentFriend;
    public static Charity currentCharity;
    public static boolean donateNow;
    public static String charityName2;
    private ImageButton cancel;
    private ImageButton addFriend;
    Context context;

    ArrayList<ParseUser> friends;
    DonateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        context = this;

        Intent intent = getIntent();
        donateNow = intent.getBooleanExtra("donateNow", false);

        searchFriend = findViewById(R.id.searchFriend);
        cancelSearchBtn = findViewById(R.id.cancelSearchBtn);
        rvFriends = findViewById(R.id.rvFriends);
        cancel = findViewById(R.id.ibcancelFinal);
        addFriend = findViewById(R.id.ibAddFriend);

        friends = new ArrayList<ParseUser>();

        //recyclerSetUp();
        populateRelations();

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
                if (count == 0) {
                    friends.clear();
                    adapter.notifyDataSetChanged();
                }
                if (count > 0) {
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

    protected void queryFriends(String name) {
        ParseQuery<ParseUser> query1 = ParseUser.getQuery();

        ParseQuery<ParseUser> q1 = query1.whereMatches("username", "(" + name + ")", "i");

        ParseQuery<ParseUser> query2 = ParseUser.getQuery();
        ParseQuery<ParseUser> q2 = query2.whereMatches("firstName", "(" + name + ")", "i");

        ParseQuery<ParseUser> query3 = ParseUser.getQuery();
        ParseQuery<ParseUser> q3 = query3.whereMatches("lastName", "(" + name + ")", "i");


        List<ParseQuery<ParseUser>> queries = new ArrayList<>();

        queries.add(q3);
        queries.add(q2);
        queries.add(q1);


        ParseQuery<ParseUser> main_query = ParseQuery.or(queries);

        main_query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (e != null) {
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

    private void populateRelations() {
        //Get relation
        final ParseRelation<ParseUser> recentFriends = ParseUser.getCurrentUser().getRelation("friendsRecent");
        //Get all charities in relation
        recentFriends.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    // results have all the charities the current user liked.
                    // go through relation adding charities
                    for (int i = 0; i < objects.size(); i++) {
                        friends.add((ParseUser) objects.get(i));
                    }
                }
                recyclerSetUp();
            }

        });
    }

    protected void recyclerSetUp(){
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvFriends.setLayoutManager(linearLayoutManager);
        adapter = new DonateAdapter(friends, false);
        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
    }

}
