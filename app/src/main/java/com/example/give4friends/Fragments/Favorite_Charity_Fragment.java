package com.example.give4friends.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.give4friends.Adapters.CharitySearchAdapter;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Favorite_Charity_Fragment extends Fragment {


    CharitySearchAdapter feedAdapter;
    ArrayList<Object> charities;
    RecyclerView rvCharities;
    private SwipeRefreshLayout swipeContainer;
    ParseUser myUser;


    public Favorite_Charity_Fragment(ParseUser myUser) {
        this.myUser = myUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fav_tab, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Below for recycler view of charities
        rvCharities = (RecyclerView) view.findViewById(R.id.rvFavCharities);
        // initialize the array list of charities
        charities = new ArrayList<Object>();
        populateRelations();
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedAdapter.clear();
                feedAdapter.addAll(charities);
                populateRelations();
                swipeContainer.setRefreshing(false);
            }

        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }


    private void populateRelations() {
        charities.add("Favorite Charities");
        //Get relation
        final ParseRelation<Charity> favCharities = myUser.getRelation("favCharities");

        ParseQuery postQuery = favCharities.getQuery();

        postQuery.orderByDescending("updatedAt");
        //Get all charities in relation
        postQuery.findInBackground(new FindCallback<Charity>() {
            @Override
            public void done(List<Charity> objects, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    // results have all the charities the current user liked.

                    // go through relation adding charities
                    for (int i = 0; i < objects.size(); i++) {
                        charities.add((Charity) objects.get(i));

                    }
                    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    rvCharities.setLayoutManager(linearLayoutManager);

                    //construct the adapter from this datasource
                    feedAdapter = new CharitySearchAdapter(charities, false, false, true);
                    //RecyclerView setup (layout manager, use adapter)
                    rvCharities.setAdapter(feedAdapter);
                    rvCharities.scrollToPosition(0);
                }
            }
        });


    }
}
