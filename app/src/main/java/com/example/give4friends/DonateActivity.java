package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.give4friends.Adapters.DonateAdapter;
import com.example.give4friends.models.Charity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DonateActivity extends AppCompatActivity implements Serializable {
    private SearchView searchFriend;
    private Button cancelSearchBtn;
    private RecyclerView rvFriends;
    public static ParseUser currentFriend;
    public static Charity currentCharity;
    public static boolean donateNow;
    public static String charityName2;
    private ImageButton cancel;
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


        searchFriend = findViewById(R.id.friends);
        rvFriends = findViewById(R.id.rvFriends);
        cancel = findViewById(R.id.ibcancelFinal);

        friends = new ArrayList<ParseUser>();

        //recyclerSetUp();
        populateRelations();

        searchFriend.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")){
                    friends.clear();
                    adapter.notifyDataSetChanged();
                    populateRelations();
                }
                else{
                    queryFriends(s.toString());
                }
                return false;
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
                adapter.notifyDataSetChanged();
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

    private void recyclerSetUp(){
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvFriends.setLayoutManager(linearLayoutManager);
        adapter = new DonateAdapter(friends, true, null, null);
        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
    }
}
