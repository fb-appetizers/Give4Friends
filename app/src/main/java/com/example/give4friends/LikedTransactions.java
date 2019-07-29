package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.give4friends.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class LikedTransactions extends HistoryActivity {


    @Override
    protected void populate(){

        //get query
        ParseQuery<Transaction> query = new ParseQuery<Transaction>(Transaction.class)
                .whereContains(Transaction.KEY_LIKES_USERS, ParseUser.getCurrentUser().getObjectId());

        query.setLimit(20);
        query.addDescendingOrder(Transaction.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Transaction>() {
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

