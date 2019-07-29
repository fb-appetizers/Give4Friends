package com.example.give4friends.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.give4friends.Adapters.DonateAdapter;
import com.example.give4friends.R;
import com.example.give4friends.SettingsActivity;
import com.example.give4friends.models.Charity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Search_User_Fragment extends Fragment implements Serializable {
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_user_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        searchFriend = view.findViewById(R.id.searchFriend);
        cancelSearchBtn = view.findViewById(R.id.cancelSearchBtn);
        rvFriends = view.findViewById(R.id.rvFriends);
        cancel = view.findViewById(R.id.ibcancelFinal);

        friends = new ArrayList<ParseUser>();

        //recyclerSetUp();
        populateRelations();

        configureToolbar();

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
                    populateRelations();
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
                friends.clear();
                adapter.notifyDataSetChanged();

            }
        });
    }

    protected void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextSize(24);
        toolbarTitle.setText("Search User");

        toolbar.setNavigationIcon(R.drawable.ic_x);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pops back the fragment if you cancel
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
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
                friends.clear();
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
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFriends.setLayoutManager(linearLayoutManager);
        adapter = new DonateAdapter(friends, false);
        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}









