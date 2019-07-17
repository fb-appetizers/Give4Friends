package com.example.give4friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
    CharityClient client;
    ArrayList <CharityAPI> acharities;
    CharityViewAdapter charityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCharity = findViewById(R.id.etCharity);
        rvCharitySearch = findViewById(R.id.rvCharitySearch);
        etCharity = findViewById(R.id.etCharity);

        acharities = new ArrayList<CharityAPI>();



//        tvTextBox = findViewById(R.id.tvCharityName);
//        tvMission = findViewById(R.id.tvMission);
//        ivRating = findViewById(R.id.ivRating);

        getReponse("", false);



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


    private void getReponse(String search, boolean search_by_name){

        client = new CharityClient();
        client.getCharities("", false, new Callback() {
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
