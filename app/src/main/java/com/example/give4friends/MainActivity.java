package com.example.give4friends;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.give4friends.Adapters.CharityViewAdapter;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.net.CharityClient;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button change;


    private EditText etCharity;
    private RecyclerView rvCharitySearch;
    private Button btnSubmit;
    CharityClient client;
    ArrayList <CharityAPI> acharities;
    CharityViewAdapter charityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Give4Friends");
        toolbar.setNavigationIcon(R.drawable.ic_account_4);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        etCharity = findViewById(R.id.etCharity);
        rvCharitySearch = findViewById(R.id.rvCharitySearch);
        etCharity = findViewById(R.id.etCharity);
        btnSubmit = findViewById(R.id.btnSubmit);

        acharities = new ArrayList<CharityAPI>();

//        tvTextBox = findViewById(R.id.tvCharityName);
//        tvMission = findViewById(R.id.tvMission);
//        ivRating = findViewById(R.id.ivRating);
        charityAdapter = new CharityViewAdapter(acharities);


        // attach the adapter to the RecyclerView
        rvCharitySearch.setAdapter(charityAdapter);

        // Set layout manager to position the items
        rvCharitySearch.setLayoutManager(new LinearLayoutManager(this));


        getResponse("", false);


        //When you hit submit the recycler view updates
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResponse(etCharity.getText().toString(),false);

//                Toast.makeText(getApplicationContext(),etCharity.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });


        change = findViewById(R.id.button2);

        // for testing

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
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
                //TODO: link to the suggestions page which is currently in the main activity
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
            default:
//                Log.e()
        }
        return true;
    }

    private void getResponse(String search, boolean search_by_name){

        client = new CharityClient();
        client.getCharities(search, false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.isSuccessful()){
                    final String myResponse = response.body().string();

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONArray charityArray;
                                    charityArray = new JSONArray(myResponse);


                                    final ArrayList <CharityAPI> charities = CharityAPI.fromJSON(charityArray);

                                    acharities.clear();
                                    for(CharityAPI charityAPI : charities){
                                        acharities.add(charityAPI);
                                    }
                                    charityAdapter.notifyDataSetChanged();


//                                    tvTextBox.setText(charities.get(0).getName());
//                                    tvMission.setText(charities.get(0).getMission());

//                                    String url = charities.get(0).ratingsUrl;
//
//                                    if (url != null) {
//                                        Glide.with(getApplicationContext())
//                                                .load(url)
//                                                .into(ivRating);
//                                    }else{
//                                        Glide.with(getApplicationContext())
//                                                .load(R.drawable.noratings)
//                                                .into(ivRating);
//
//                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });




                }
            }
        });

    }

    public void logOut(){
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
