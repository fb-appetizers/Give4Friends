package com.example.give4friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.give4friends.Adapters.CharitySuggAdapter;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.net.CharityClient;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CharitySearch extends AppCompatActivity {

    private EditText etCharity;
    private TextInputLayout tiCharity;
    private Button btnSubmit;
    private RecyclerView rvCharitySugg;

    CharityClient client;

    ArrayList<CharityAPI> acharitiesUpper;
    CharitySuggAdapter charityAdapterUpper;

    ConstraintLayout constraintLayoutMain;
    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_search);

        //TODO reduce whitespace

        etCharity = findViewById(R.id.etCharity);

        rvCharitySugg = findViewById(R.id.rvCharitySugg);
        etCharity = findViewById(R.id.etCharity);
        btnSubmit = findViewById(R.id.btnCancel);
        tiCharity = findViewById(R.id.tiCharity);

        etCharity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                acharitiesUpper.clear();
                charityAdapterUpper.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
            }


        });

        constraintLayoutMain = findViewById(R.id.clCharitySearch);
        acharitiesUpper = new ArrayList<CharityAPI>();
        charityAdapterUpper = new CharitySuggAdapter(acharitiesUpper);

        // attach the adapter to the RecyclerView
        rvCharitySugg.setAdapter(charityAdapterUpper);

        // Set layout manager to position the items
        rvCharitySugg.setLayoutManager(new LinearLayoutManager(this));

        getResponseSuggested();
        //TODO -- search up MODALS/POPUP

        //When you hit submit the recycler view updates
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResponseSearch(etCharity.getText().toString(),false);

            }
        });

    }

    private void getResponseSearch(String search, boolean search_by_name){

        client = new CharityClient();
        showProgressBar();
        client.getCharities(search, false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()){
                    final String myResponse = response.body().string();

                    CharitySearch.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray charityArray;
                                charityArray = new JSONArray(myResponse);

                                final ArrayList <CharityAPI> charities = CharityAPI.fromJSON(charityArray);

                                acharitiesUpper.clear();
                                for(CharityAPI charityAPI : charities){
                                    acharitiesUpper.add(charityAPI);
                                }
                                charityAdapterUpper.notifyDataSetChanged();
                                hideProgressBar();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                hideProgressBar();
                            }
                        }
                    });
                }
            }
        });

    }

    private void getResponseSuggested(){

        ParseUser mainUser = ParseUser.getCurrentUser();

        ParseQuery<ParseUser> postQuery = new ParseQuery<ParseUser>(ParseUser.class);
        postQuery.include("charityArray");
        postQuery.setLimit(1);
        postQuery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());

        postQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                ParseUser mainUser = objects.get(0);// They'll only be one
                List <Charity> charities = mainUser.getList("charityArray");
                if (charities == null){
                    charities = new ArrayList<Charity>();
                }
                for (Charity charity : charities) {
                    acharitiesUpper.add(CharityAPI.fromParse(charity));
                }
                charityAdapterUpper.notifyDataSetChanged();
            }
        });
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Give4Friends");
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CharitySearch.this, ProfileActivity.class);
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

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.etCharity:
                Toast.makeText(this, "Charity Search selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.transactionHistory:
                Toast.makeText(this, "Transaction History selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.useOffline:
                Toast.makeText(this, "Use Offline selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.logOut:
                Toast.makeText(this, "logging out...", Toast.LENGTH_LONG).show();
                logOut();
            default:
//                Log.e()
        }
        return true;
    }


    public void showProgressBar() {
        // Show progress item

        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    public void logOut(){
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
//        miActionProgressItem.setVisible(true);
        super.onPrepareOptionsMenu(menu);

        return true;
    }
}