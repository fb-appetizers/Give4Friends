package com.example.give4friends.Fragments;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.give4friends.Adapters.TransactionAdapter;
import com.example.give4friends.Main_Fragment_Branch;
import com.example.give4friends.R;
import com.example.give4friends.SettingsActivity;
import com.example.give4friends.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class History_Fragment extends Main_Transaction_Fragment {
    ParseUser myUser;
    private SwipeRefreshLayout swipeContainer;
    Boolean friend;

    public History_Fragment(ParseUser myUser, boolean friend) {
        // This sets the value of myUser using a constructor
        this.myUser = myUser;
        this.friend =friend;
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        if(!friend){
            configureToolbar();
        }else {
            configureToolbarStripped();
        }
        setHasOptionsMenu(false);
//        // Implement Recycler View
        rvTransactions = view.findViewById(R.id.rvTransactions);
        // Initialize array list of transactions
        transactions = new ArrayList<Transaction>();
        // Construct Adapter
        transactionAdapter = new TransactionAdapter(transactions, friend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvTransactions.setLayoutManager(linearLayoutManager);
        rvTransactions.setAdapter(transactionAdapter);
        rvTransactions.scrollToPosition(0);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populate();
                swipeContainer.setRefreshing(false);
            }

        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        populate();
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
        mainQuery.setLimit(20);
        mainQuery.addDescendingOrder(Transaction.KEY_CREATED_AT);
        mainQuery.findInBackground(new FindCallback<Transaction>() {
            //iterate through query
            @Override
            public void done(List<Transaction> objects, ParseException e) {
                if (e == null){
                    transactions.clear();
                    for (int i = 0; i < objects.size(); ++i){

                        transactions.add(objects.get(i));
//                        transactionAdapter.notifyItemInserted(transactions.size() - 1);
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



    private void configureToolbarStripped() {
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

            }
        });

    }






}
