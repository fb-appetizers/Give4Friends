package com.example.give4friends.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.give4friends.Adapters.TransactionAdapter;
import com.example.give4friends.Cutom_Classes.EndlessRecyclerViewScrollListener;
import com.example.give4friends.Main_Fragment_Branch;
import com.example.give4friends.R;
import com.example.give4friends.SettingsActivity;
import com.example.give4friends.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class History_Fragment extends Main_Transaction_Fragment {
    ParseUser myUser;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    public static Integer MAX_NUMBER_OF_TRANSACTIONS = 20;
    Boolean friend;

    public History_Fragment(ParseUser myUser, boolean friend) {
        // This sets the value of myUser using a constructor
        this.myUser = myUser;
        this.friend = friend;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if(!friend){
            configureToolbar();
        }else {
            configureToolbarStripped();
        }
        setHasOptionsMenu(false);
        // Implement Recycler View
        rvTransactions = view.findViewById(R.id.rvTransactions);
        // Initialize array list of transactions
        transactions = new ArrayList<Transaction>();
        // Construct Adapter
        transactionAdapter = new TransactionAdapter(transactions, friend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


        removeFlickering();




        rvTransactions.setLayoutManager(linearLayoutManager);
        rvTransactions.setAdapter(transactionAdapter);
        rvTransactions.scrollToPosition(0);


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                transactions.clear();
                populate();
                swipeContainer.setRefreshing(false);
                scrollListener.resetState();
            }

        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_purple,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        transactions.clear();
        populate();



        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                populate();

            }


        };

        scrollListener.resetState();

        rvTransactions.addOnScrollListener(scrollListener);
    }


    private void removeFlickering(){
        // This basically removed a lot of the flickering from getting new transactions
        rvTransactions.getItemAnimator().setChangeDuration(0);
        RecyclerView.ItemAnimator animator = rvTransactions.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        rvTransactions.setHasFixedSize(true);
        transactionAdapter.setHasStableIds(true);

    }

    @Override
    protected boolean populate(){


        //get query
        ParseQuery<Transaction> postQueryFriend = new ParseQuery<Transaction>(Transaction.class)
                .whereEqualTo(Transaction.KEY_FRIEND_ID, myUser);
        ParseQuery<Transaction> postQueryMe = new ParseQuery<Transaction>(Transaction.class)
                .whereEqualTo(Transaction.KEY_DONOR_ID, myUser);

        List<ParseQuery<Transaction>> queries = new ArrayList<ParseQuery<Transaction>>();
        queries.add(postQueryFriend);
        queries.add(postQueryMe);


        ParseQuery<Transaction> mainQuery = ParseQuery.or(queries);
        mainQuery.setLimit(MAX_NUMBER_OF_TRANSACTIONS);
        mainQuery.addDescendingOrder(Transaction.KEY_CREATED_AT);


        if(transactions.size() > 0 ){

            Date createdAt = transactions.get(transactions.size() - 1).getCreatedAt();
            mainQuery.whereLessThan(Transaction.KEY_CREATED_AT, createdAt);

        }
        mainQuery.findInBackground(new FindCallback<Transaction>() {
            //iterate through query
            @Override
            public void done(List<Transaction> transactionList, ParseException e) {
                if (e == null){

                    for (int i = 0; i < transactionList.size(); ++i){
                        Transaction transaction = transactionList.get(i);

                        transactions.add(transaction);

                        // Save the last transaction
                        if(i == (transactionList.size()-1)){
                            try {
                                transaction.save();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }



                    }
                    transactionAdapter.notifyDataSetChanged();
                }else {
                    Log.e("MainActivity", "Can't get transaction");
                    e.printStackTrace();
                }
            }
        });


        return false;
    }



    protected void configureToolbarStripped() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);


        toolbarTitle.setTextSize(30);
        toolbarTitle.setText("Friend History");



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
    private void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);


        toolbarTitle.setTextSize(30);
        toolbarTitle.setText("History");



        toolbar.setNavigationIcon(R.drawable.ic_settings);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                ((Activity)getContext()).overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }
        });

    }

    private void setUp(){
        if(!friend){
            configureToolbar();
        }else {
            configureToolbarStripped();
        }
        setHasOptionsMenu(false);
    }






}
