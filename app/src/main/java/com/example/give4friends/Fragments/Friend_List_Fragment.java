package com.example.give4friends.Fragments;

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
import com.example.give4friends.models.Charity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Friend_List_Fragment extends Fragment implements Serializable {

    private RecyclerView rvFriends;


    ArrayList<ParseUser> users;
    ParseRelation<ParseUser> friends;
    ArrayList<String> localFriends;
    DonateAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        rvFriends = view.findViewById(R.id.rvFriends);

        users = new ArrayList<ParseUser>();
        localFriends = new ArrayList<String>();


        getFriends();

        configureToolbar();

    }

    protected void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar2);


        TextView toolbarTitle = toolbar.findViewById(R.id.tvtoolbar_title_main);
        toolbarTitle.setTextSize(24);
        toolbarTitle.setText("Friends");

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


    private void recyclerSetUp() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFriends.setLayoutManager(linearLayoutManager);
        adapter = new DonateAdapter(users, false, friends, localFriends);
        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private void getFriends() {
        friends = ParseUser.getCurrentUser().getRelation("friends");
        friends.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                localFriends.clear();
                if (e != null) {
                    // There was an error
                } else {
                    // results have all the charities the current user liked.
                    // go through relation adding charities
                    for (int i = 0; i < objects.size(); i++) {
                        users.add(objects.get(i));
                        ParseUser tempFriend = (ParseUser) objects.get(i);
                        localFriends.add(tempFriend.getObjectId());
                    }
                }
                recyclerSetUp();

            }

        });
    }
}




