package com.example.give4friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.example.give4friends.Adapters.CharitySearchAdapter;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.net.CharityClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CharitySearch extends AppCompatActivity {


    private EditText etCharity;
    private RecyclerView rvCharitySearch;
    private Button btnSubmit;
    CharityClient client;
    ArrayList<CharityAPI> acharities;
    CharitySearchAdapter charityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_search);

        etCharity = findViewById(R.id.etCharity);
        rvCharitySearch = findViewById(R.id.rvCharitySearch);
        etCharity = findViewById(R.id.etCharity);
        btnSubmit = findViewById(R.id.btnSubmit);

        acharities = new ArrayList<CharityAPI>();

        charityAdapter = new CharitySearchAdapter(acharities);


        // attach the adapter to the RecyclerView
        rvCharitySearch.setAdapter(charityAdapter);

        // Set layout manager to position the items
        rvCharitySearch.setLayoutManager(new LinearLayoutManager(this));


//        getResponse("", false);


        //When you hit submit the recycler view updates
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResponse(etCharity.getText().toString(),false);

//                Toast.makeText(getApplicationContext(),etCharity.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charity_menu, menu);

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

                    CharitySearch.this.runOnUiThread(new Runnable() {
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

}
