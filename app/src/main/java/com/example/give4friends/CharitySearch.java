package com.example.give4friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.give4friends.Adapters.CharitySearchAdapter;
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
    private RecyclerView rvCharitySearch;
    private Button btnSubmit;
    private RecyclerView rvCharitySugg;
    private CardView cardView;

    private TextView tvCharitySugg;
    private TextInputLayout tiCharity;

    CharityClient client;
    ArrayList<CharityAPI> acharitiesLower;
    ArrayList<CharityAPI> acharitiesUpper;
    CharitySuggAdapter charityAdapterUpper;
    CharitySearchAdapter charityAdapterLower;
    ConstraintLayout constraintLayoutMain;
    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_search);

        //TODO reduce whitespace
        configureToolbar();

        etCharity = findViewById(R.id.etCharity);
        rvCharitySearch = findViewById(R.id.rvCharitySearch);
        rvCharitySugg = findViewById(R.id.rvCharitySugg);
        etCharity = findViewById(R.id.etCharity);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvCharitySugg = findViewById(R.id.tvCharitySugg);
        tiCharity = findViewById(R.id.tiCharity);
        constraintLayoutMain = findViewById(R.id.clCharitySearch);
        cardView = findViewById(R.id.cvSugg);


        acharitiesLower = new ArrayList<CharityAPI>();
        acharitiesUpper = new ArrayList<CharityAPI>();

        charityAdapterUpper = new CharitySuggAdapter(acharitiesUpper);
        charityAdapterLower = new CharitySearchAdapter(acharitiesLower, false);



        // attach the adapter to the RecyclerView
        rvCharitySugg.setAdapter(charityAdapterUpper);
        rvCharitySearch.setAdapter(charityAdapterLower);


        // Set layout manager to position the items
        rvCharitySugg.setLayoutManager(new LinearLayoutManager(this));
        rvCharitySearch.setLayoutManager(new LinearLayoutManager(this));


        getResponseSuggested();






//TODO -- search up MODALS/POPUP

        //TODO move click listener
        tvCharitySugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!cardView.isShown()) {



                    tvCharitySugg.setText("Close Charity Suggestions");
                    cardView.setVisibility(View.VISIBLE);


                    btnSubmit.setVisibility(View.VISIBLE);
                    tiCharity.setVisibility(View.VISIBLE);
                    etCharity.setVisibility(View.VISIBLE);


                }else{

                    tvCharitySugg.setText("Open Charity Suggestions");

                    cardView.setVisibility(View.GONE);


                    btnSubmit.setVisibility(View.GONE);
                    tiCharity.setVisibility(View.GONE);
                    etCharity.setVisibility(View.GONE);



                }

            }
        });
        //When you hit submit the recycler view updates
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResponseLower(etCharity.getText().toString(),false);

                tvCharitySugg.setText("Open Charity Suggestions");
                cardView.setVisibility(View.GONE);

                btnSubmit.setVisibility(View.GONE);
                tiCharity.setVisibility(View.GONE);
                etCharity.setVisibility(View.GONE);




            }
        });



    }


    private void getResponseLower(String search, boolean search_by_name){

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

                                acharitiesLower.clear();
                                for(CharityAPI charityAPI : charities){
                                    acharitiesLower.add(charityAPI);
                                }
                                charityAdapterLower.notifyDataSetChanged();
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
                Intent intent = new Intent(CharitySearch.this, MainActivity.class);
                startActivity(intent);
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
                return true;
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.logOut:
                Toast.makeText(this, "logging out...", Toast.LENGTH_LONG).show();
                logOut();
//            case R.id.toolbar
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
