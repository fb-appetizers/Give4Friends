package com.example.give4friends.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.give4friends.Adapters.CharitySuggAdapter;
import com.example.give4friends.Adapters.MilestoneAdapter;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.Milestone;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Milestone_Fragment extends Fragment {

    MilestoneAdapter feedAdapter;
    ArrayList<Object> milestones;
    RecyclerView rvMilestones;
    private SwipeRefreshLayout swipeContainer;
    ParseUser myUser;


    // we get the user
    public Milestone_Fragment(ParseUser myUser) {
        this.myUser = myUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_milestone_tab, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Below for recycler view of charities
        rvMilestones = (RecyclerView) view.findViewById(R.id.rvMilestones);

        milestones = new ArrayList<Object>();
        populateRelations();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedAdapter.clear();
                feedAdapter.addAll(milestones);
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
        String name = "Unnamed";
        if (myUser.getObjectId() == ParseUser.getCurrentUser().getObjectId()) {
            milestones.add("Your Milestones");
        } else {
            name = myUser.getString("firstName");
            milestones.add(name + "'s Milestones");
        }

        milestones.addAll(Milestone.getAllMilestones());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        rvMilestones.setLayoutManager(gridLayoutManager);

        ArrayList<Object> milestonesCompleted = (ArrayList<Object>) myUser.getList("milestones_completed");

        //construct the adapter from this datasource
        //TODO add to the parameters of Milestone adapter
        feedAdapter = new MilestoneAdapter(milestones, myUser, milestonesCompleted);
        //RecyclerView setup (layout manager, use adapter)
        rvMilestones.setAdapter(feedAdapter);
        rvMilestones.scrollToPosition(0);
    }


}

