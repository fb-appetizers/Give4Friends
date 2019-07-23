package com.example.give4friends;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

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

    @Override
    protected void populate(){
        //get query
        ParseQuery<Transaction> postQueryFriend = new ParseQuery<Transaction>(Transaction.class)
                .whereEqualTo(Transaction.KEY_FRIEND_ID, ParseUser.getCurrentUser());
        ParseQuery<Transaction> postQueryMe = new ParseQuery<Transaction>(Transaction.class)
                .whereEqualTo(Transaction.KEY_DONOR_ID, ParseUser.getCurrentUser());

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







}
