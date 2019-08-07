package com.example.give4friends.Fragments;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.give4friends.R;
import com.example.give4friends.SettingsActivity;
import com.example.give4friends.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class Liked_Transactions_Fragment extends History_Fragment {
    public Liked_Transactions_Fragment(ParseUser myUser, boolean friend) {
        super(myUser, friend);
    }

    @Override
    protected boolean populate(){

        //get query
        ParseQuery<Transaction> query = new ParseQuery<Transaction>(Transaction.class)
                .whereContains(Transaction.KEY_LIKES_USERS, ParseUser.getCurrentUser().getObjectId());

        query.setLimit(MAX_NUMBER_OF_TRANSACTIONS);
        query.addDescendingOrder(Transaction.KEY_UPDATED_AT);

        if(transactions.size() > 0 ){
            Date updatedAt = transactions.get(transactions.size() - 1).getCreatedAt();
            query.whereLessThan(Transaction.KEY_UPDATED_AT, updatedAt);
        }

        query.findInBackground(new FindCallback<Transaction>() {
            //iterate through query
            @Override
            public void done(List<Transaction> transactionList, ParseException e) {
                if (e == null){

                    for (int i = 0; i < transactionList.size(); ++i){
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

    @Override
    protected void configureToolbarStripped() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);


        toolbarTitle.setTextSize(30);
        toolbarTitle.setText("Liked Transactions");


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



}
