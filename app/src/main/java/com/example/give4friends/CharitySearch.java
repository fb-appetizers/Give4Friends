package com.example.give4friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alespero.expandablecardview.ExpandableCardView;
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
    private Animation animationUp;
    private Animation animationDown;
    private TextView tvCharitySugg;
    private TextInputLayout tiCharity;
    CharityClient client;
    ArrayList<CharityAPI> acharitiesLower;
    ArrayList<CharityAPI> acharitiesUpper;
    CharitySuggAdapter charityAdapterUpper;
    CharitySearchAdapter charityAdapterLower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_search);

        configureToolbar();

        etCharity = findViewById(R.id.etCharity);
        rvCharitySearch = findViewById(R.id.rvCharitySearch);
        rvCharitySugg = findViewById(R.id.rvCharitySugg);
        etCharity = findViewById(R.id.etCharity);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvCharitySugg = findViewById(R.id.tvCharitySugg);
        tiCharity = findViewById(R.id.tiCharity);

        cardView = findViewById(R.id.cvSugg);


        //TODO setup a collapsing suggestions view
        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);





        acharitiesLower = new ArrayList<CharityAPI>();
        acharitiesUpper = new ArrayList<CharityAPI>();

        charityAdapterUpper = new CharitySuggAdapter(acharitiesUpper);
        charityAdapterLower = new CharitySearchAdapter(acharitiesLower);



        // attach the adapter to the RecyclerView
        rvCharitySugg.setAdapter(charityAdapterUpper);
        rvCharitySearch.setAdapter(charityAdapterLower);


        // Set layout manager to position the items
        rvCharitySugg.setLayoutManager(new LinearLayoutManager(this));
        rvCharitySearch.setLayoutManager(new LinearLayoutManager(this));


        getResponseUpper();


        tvCharitySugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!cardView.isShown()) {
                    cardView.setVisibility(View.VISIBLE);
                    cardView.startAnimation(animationDown);

                    btnSubmit.setVisibility(View.VISIBLE);
                    tiCharity.setVisibility(View.VISIBLE);
                    etCharity.setVisibility(View.VISIBLE);

                }else{
                    cardView.setVisibility(View.GONE);
                    cardView.startAnimation(animationUp);

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

                if(cardView.isShown()){
                    cardView.setVisibility(View.GONE);
                    cardView.startAnimation(animationUp);

                    btnSubmit.setVisibility(View.GONE);
                    tiCharity.setVisibility(View.GONE);
                    etCharity.setVisibility(View.GONE);


                }

//                Toast.makeText(getApplicationContext(),etCharity.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }


    private void getResponseLower(String search, boolean search_by_name){

        client = new CharityClient();
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }


    private void getResponseUpper(){
        ParseQuery<Charity> postQuery = new ParseQuery<Charity>(Charity.class);
        postQuery.setLimit(20);



            postQuery.findInBackground(new FindCallback<Charity>() {
                @Override
                public void done(List<Charity> charities, ParseException e) {
                    for(Charity charity : charities){
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
                Intent intent = new Intent(CharitySearch.this, CharitySearch.class);
                startActivity(intent);
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

    public void logOut(){
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
