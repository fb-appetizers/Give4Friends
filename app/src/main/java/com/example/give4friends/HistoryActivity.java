package com.example.give4friends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.give4friends.Adapters.TransactionAdapter;
import com.example.give4friends.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends MainActivity{

    //Will need to call .setVisibility(View.VISIBLE) on amount witch is
    // setVisibility(View.INVISIBLE) on mainActivity
    ParseUser myUser;
    private ImageButton cancelBtn;
    private SwipeRefreshLayout swipeContainer;
    Boolean friend;


    @Override
    protected void populate(){

        //unwrap
        myUser = getIntent().getParcelableExtra("user");
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
        });}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friend =  getIntent().getBooleanExtra("friend", false);
        setContentView(R.layout.activity_transaction_history);

        cancelBtn = findViewById(R.id.cancelTrans);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Implement Recycler View
        rvTransactions = findViewById(R.id.rvTransactionsTrans);
        // Initialize array list of transactions
        transactions = new ArrayList<Transaction>();
        // Construct Adapter
        transactionAdapter = new TransactionAdapter(transactions, friend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTransactions.setLayoutManager(linearLayoutManager);
        rvTransactions.setAdapter(transactionAdapter);
        rvTransactions.scrollToPosition(0);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainerTrans);
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






}
