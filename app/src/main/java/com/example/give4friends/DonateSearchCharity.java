package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.give4friends.Adapters.CharitySearchAdapter;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.net.CharityClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.meta.When;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DonateSearchCharity extends AppCompatActivity {
    private TextView friend;
    private EditText etCharity;
    private RecyclerView rvCharitySearch;
    private Button btnSubmit;
    private ImageButton cancel;
    CharityClient client;
    ArrayList<CharityAPI> acharities;
    CharitySearchAdapter charityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_search_charity);

        Intent intent = getIntent();

        friend = findViewById(R.id.friendSelected);

        friend.setText(intent.getStringExtra("friend"));

        etCharity = findViewById(R.id.etCharity);
        rvCharitySearch = findViewById(R.id.rvCharitySearch);
        etCharity = findViewById(R.id.etCharity);
        btnSubmit = findViewById(R.id.btnSubmit);
        cancel = findViewById(R.id.cancel);

        acharities = new ArrayList<CharityAPI>();

        charityAdapter = new CharitySearchAdapter(acharities);

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
        client.getCharities(search, false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

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
            }
        });

    }
}
