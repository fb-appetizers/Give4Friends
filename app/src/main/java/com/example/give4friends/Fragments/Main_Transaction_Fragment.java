package com.example.give4friends.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.give4friends.Adapters.TransactionAdapter;
import com.example.give4friends.DonateActivity;
import com.example.give4friends.R;
import com.example.give4friends.SettingsActivity;
import com.example.give4friends.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Main_Transaction_Fragment extends Fragment {

    private ImageButton suggBtn;

    public static Integer MAX_NUMBER_OF_TRANSACTIONS = 20;
    protected RecyclerView rvTransactions;
    protected List<Transaction> transactions;
    protected TransactionAdapter transactionAdapter;
    private SwipeRefreshLayout swipeContainer;
    boolean friend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_main, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);

        configureToolbar();
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
        transactionAdapter = new TransactionAdapter(transactions, true);


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

    private void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Give4Friends");


        toolbar.setNavigationIcon(R.drawable.ic_settings);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);


            }
        });
    }



    protected boolean populate(){
        //get query
        ParseQuery<Transaction> postQuery = new ParseQuery<Transaction>(Transaction.class);
        //Used to set a limit to the number of transactions
        postQuery.setLimit(MAX_NUMBER_OF_TRANSACTIONS);
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


        });
        return false;
    }


}




