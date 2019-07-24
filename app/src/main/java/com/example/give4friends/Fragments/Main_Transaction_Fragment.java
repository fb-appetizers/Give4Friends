package com.example.give4friends.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.give4friends.Adapters.TransactionAdapter;
import com.example.give4friends.DonateActivity;
import com.example.give4friends.MainActivity;
import com.example.give4friends.R;
import com.example.give4friends.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Main_Transaction_Fragment extends Fragment {

    private ImageButton suggBtn;


    protected RecyclerView rvTransactions;
    protected List<Transaction> transactions;
    protected TransactionAdapter transactionAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_main, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        suggBtn = view.findViewById(R.id.suggBtn);

        suggBtn.setBackgroundDrawable(null);

        suggBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DonateActivity.class);
                intent.putExtra("donateNow", false);
                startActivity(intent);
            }
        });

        // Implement Recycler View
        rvTransactions = view.findViewById(R.id.rvTransactions);
        // Initialize array list of transactions
        transactions = new ArrayList<Transaction>();
        // Construct Adapter
        transactionAdapter = new TransactionAdapter(transactions);

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



    protected void populate(){
        //get query
        ParseQuery<Transaction> postQuery = new ParseQuery<Transaction>(Transaction.class);
        postQuery.setLimit(10);
        postQuery.orderByDescending(Transaction.KEY_CREATED_AT);

        postQuery.findInBackground(new FindCallback<Transaction>() {
            //iterate through query
            @Override
            public void done(List<Transaction> transactionList, ParseException e) {
                if (e == null){

                    //Clear the old set when reloading
                    transactions.clear();

                    for(Transaction transaction : transactionList){

                        transactions.add(transaction);
                    }
                    transactionAdapter.notifyDataSetChanged();
                }else {
                    Log.e("MainActivity", "Can't get transaction");
                    e.printStackTrace();
                }
            }


        });}



}




