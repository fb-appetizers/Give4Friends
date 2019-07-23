package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.give4friends.Adapters.CharitySearchAdapter;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.net.CharityClient;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.meta.When;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.give4friends.DonateActivity.friend;

public class DonateSearchCharity extends AppCompatActivity implements Serializable {
    private TextView friendz;
    public static Charity charity;
    private EditText etCharity;
    private RecyclerView rvCharitySearch;
    private Button btnSubmit;
    private ImageButton cancel;
    CharityClient client;
    ArrayList<CharityAPI> acharities;
    CharitySearchAdapter charityAdapter;
    ProgressBar miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_search_charity);

        Intent intent = getIntent();

        friendz = findViewById(R.id.friendSelected);

//        friendInfo = (ParseUser) intent.getSerializableExtra("friend");

        friendz.setText(friend.getUsername());

        etCharity = findViewById(R.id.etCharity);
        rvCharitySearch = findViewById(R.id.rvCharitySearch);
        etCharity = findViewById(R.id.etCharity);
        btnSubmit = findViewById(R.id.btnSubmit);
        cancel = findViewById(R.id.cancel);

        acharities = new ArrayList<CharityAPI>();

        charityAdapter = new CharitySearchAdapter(acharities, true);
        //Added another field to check if this is in the Donate Search charity.

        miActionProgressItem = findViewById(R.id.progressBar);


        // attach the adapter to the RecyclerView
        rvCharitySearch.setAdapter(charityAdapter);

        // Set layout manager to position the items
        rvCharitySearch.setLayoutManager(new LinearLayoutManager(this));

        //When you hit submit the recycler view updates
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResponse(etCharity.getText().toString(),false);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonateSearchCharity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getResponse(String search, boolean search_by_name){

        client = new CharityClient();
        showProgressBar();
        client.getCharities(search, false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hideProgressBar();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.isSuccessful()){
                    final String myResponse = response.body().string();

                    DonateSearchCharity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray charityArray;
                                charityArray = new JSONArray(myResponse);


                                final ArrayList <CharityAPI> charities = CharityAPI.fromJSON(charityArray);

                                acharities.clear();
                                for(CharityAPI charityAPI : charities){
                                    acharities.add(charityAPI);
                                    charityAdapter.notifyDataSetChanged();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                hideProgressBar();
            }
        });
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisibility(View.INVISIBLE);
    }
}
