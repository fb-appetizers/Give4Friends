package com.example.give4friends.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.give4friends.Adapters.CharityProfileAdapter;
import com.example.give4friends.Cutom_Classes.EndlessRecyclerViewScrollListener;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.models.Comments;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Charity_Profile_Fragment extends Fragment {

    RecyclerView rvCPProfile;
    ArrayList<Object> items;
    CharityAPI charity;
    CharityProfileAdapter itemsAdapter;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    public static Integer MAX_NUMBER_OF_COMMENTS = 2;
    public static Integer MAX_NUMBER_OF_PROFILES = 1;
    public Date LastCommentCreatedAt = null;

    public Charity_Profile_Fragment(CharityAPI charity) {
        this.charity = charity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_charity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        configureToolbar();
        rvCPProfile = view.findViewById(R.id.rvCPProfile);
        items = new ArrayList<Object>();
        itemsAdapter = new CharityProfileAdapter(items);
        // attach the adapter to the RecyclerView
        rvCPProfile.setAdapter(itemsAdapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        // Set layout manager to position the items
        rvCPProfile.setLayoutManager(linearLayoutManager);


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerCharityProfile);


        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Resets the scroll listener so even during refreshes you can still scroll
                scrollListener.resetState();

                items.clear();
                itemsAdapter.notifyDataSetChanged();
                populateProfile();
                populateComments();
                swipeContainer.setRefreshing(false);

            }

        });


        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(totalItemsCount > MAX_NUMBER_OF_PROFILES) {
                    populateComments();
                }


            }
        };


        scrollListener.resetState();
        rvCPProfile.addOnScrollListener(scrollListener);


        items.clear();
        itemsAdapter.notifyDataSetChanged();
        populateProfile();
        populateComments();


    }


    private void populateProfile(){
        items.add(charity);
        itemsAdapter.notifyItemInserted(items.size() - 1);

    }


    private void populateComments(){
        //First find the charity with the ID in parse

        ParseQuery<Charity> charityParseQuery = new ParseQuery<Charity>(Charity.class);
        charityParseQuery.include(Charity.KEY_CHARITY_ID);
        charityParseQuery.whereEqualTo("charityName", charity.getEin());

        charityParseQuery.getFirstInBackground(new GetCallback<Charity>() {
            @Override
            public void done(Charity charity, ParseException e) {
                if (e != null) {
                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {

                        return;
                    } else {
                        Log.e("CharitySearchAdapter", "Error with query of charity");
                    }
                } else {
                    ParseRelation<Comments>  comments= charity.getRelation("UserComments");
                    ParseQuery<Comments> commentQuery = comments.getQuery();

                    commentQuery.setLimit(MAX_NUMBER_OF_COMMENTS);
                    commentQuery.orderByDescending("createdAt");

                    // If there are items that aren't the regular
                    if(items.size() > MAX_NUMBER_OF_PROFILES ){

                        commentQuery.whereLessThan("createdAt",  LastCommentCreatedAt);

                    }

                    commentQuery.findInBackground(new FindCallback<Comments>() {
                        @Override
                        public void done(List<Comments> comment_obj, ParseException e) {

                            if (e != null) {
                                // There was an error
                            } else {
                                // results have all the charities the current user liked.
                                // go through relation adding charities
                                for (int i = 0; i < comment_obj.size(); i++) {
                                    items.add(comment_obj.get(i));

                                    if(i == (comment_obj.size()-1)){
                                        LastCommentCreatedAt = comment_obj.get(i).getDate("createdAt");
                                    }
                                }
                                itemsAdapter.notifyDataSetChanged();
                            }

                        }


                    });






                }
            }
        });

    }


    protected void configureToolbar() {

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.homeToolbar);
        toolbarTitle.setText(charity.getName());
        toolbarTitle.setTextSize(17);

        toolbar.setNavigationIcon(R.drawable.ic_x);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();

            }
        });

    }




}
