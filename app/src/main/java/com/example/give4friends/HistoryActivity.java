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





    @Override
    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Give4Friends");
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.toolbar_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charity_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.etCharity:
                Toast.makeText(this, "Charity Search selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), CharitySearch.class);

                startActivityForResult(intent,0);
                return true;
            case R.id.transactionHistory:
                Toast.makeText(this, "Transaction History selected", Toast.LENGTH_LONG).show();

                return true;
            case R.id.useOffline:
                Toast.makeText(this, "Use Offline selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), SettingsActivity.class);

                startActivityForResult(intent,2);
                return true;
            case R.id.logOut:
                Toast.makeText(this, "logging out...", Toast.LENGTH_LONG).show();
                logOut();
            default:
//                Log.e()
        }
        return true;
    }


}
