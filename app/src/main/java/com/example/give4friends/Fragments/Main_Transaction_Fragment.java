package com.example.give4friends.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.give4friends.Adapters.TransactionAdapter;
import com.example.give4friends.Cutom_Classes.EndlessRecyclerViewScrollListener;
import com.example.give4friends.R;
import com.example.give4friends.SettingsActivity;
import com.example.give4friends.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main_Transaction_Fragment extends Fragment {

    private ImageButton suggBtn;

    public static Integer MAX_NUMBER_OF_TRANSACTIONS = 20;
    protected RecyclerView rvTransactions;
    protected List<Transaction> transactions;
    protected TransactionAdapter transactionAdapter;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
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
        setHasOptionsMenu(true);

        // Implement Recycler View
        rvTransactions = view.findViewById(R.id.rvTransactions);
        // Initialize array list of transactions
        transactions = new ArrayList<Transaction>();
        // Construct Adapter
        transactionAdapter = new TransactionAdapter(transactions, true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvTransactions.setLayoutManager(linearLayoutManager);
        rvTransactions.setAdapter(transactionAdapter);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Resets the scroll listener so even during refreshes you can still scroll
                scrollListener.resetState();
                //Clear the old set when reloading
                transactions.clear();
                transactionAdapter.notifyDataSetChanged();

                populate();
                swipeContainer.setRefreshing(false);

                //You might also want to clear the glide cache data here??
            }

        });


        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //Clear the old set when reloading
        transactions.clear();
        transactionAdapter.notifyDataSetChanged();
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

    private void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);

        toolbarTitle.setTextSize(30);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater main_activity_inflater = getActivity().getMenuInflater();
        main_activity_inflater.inflate(R.menu.main_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.impactCalculator:
                // Create a new fragment instead of an activity
                Fragment fragment1 = new Impact_Calculator_Fragment();
                FragmentManager fragmentManager1 = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fragmentManager1.beginTransaction().
                        replace(R.id.flContainer, fragment1)
                        .addToBackStack(null).commit();

                return true;

            case R.id.searchPeople:
                // Create a new fragment instead of an activity
                Fragment fragment2 = new Search_User_Fragment();
                FragmentManager fragmentManager2 = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fragmentManager2.beginTransaction().
                        replace(R.id.flContainer, fragment2)
                        .addToBackStack(null).commit();


                return true;
            default:

        }
        return true;
    }



    protected boolean populate(){
        //get query
        ParseQuery<Transaction> postQuery = new ParseQuery<Transaction>(Transaction.class);
        //Used to set a limit to the number of transactions

        postQuery.setLimit(MAX_NUMBER_OF_TRANSACTIONS);
        postQuery.orderByDescending(Transaction.KEY_CREATED_AT);

        if(transactions.size() > 0 ){

            Date createdAt = transactions.get(transactions.size() - 1).getCreatedAt();

            postQuery.whereLessThan(Transaction.KEY_CREATED_AT, createdAt);

        }


        postQuery.findInBackground(new FindCallback<Transaction>() {
            //iterate through query
            @Override
            public void done(List<Transaction> transactionList, ParseException e) {
                if (e == null){
                    for(int i=0;i<transactionList.size();i++){
                        Transaction transaction = transactionList.get(i);
                        transactions.add(transaction);

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




}




