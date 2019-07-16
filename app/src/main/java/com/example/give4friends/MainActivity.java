package com.example.give4friends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.give4friends.models.Charity;
import com.example.give4friends.net.CharityClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private Button change;


    private TextView tvTextBox;
    private TextView tvMission;
    private ImageView ivRating;
    CharityClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tvTextBox = findViewById(R.id.tvCharityName);
        tvMission = findViewById(R.id.tvMission);
        ivRating = findViewById(R.id.ivRating);

        getReponse("", false);



        change = findViewById(R.id.button2);

        // for testing

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
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


                                    final ArrayList <Charity> charities = Charity.fromJSON(charityArray);


                                    tvTextBox.setText(charities.get(0).getName());
                                    tvMission.setText(charities.get(0).getMission());

                                    String url = charities.get(0).ratingsUrl;

                                    if (url != null) {
                                        Glide.with(getApplicationContext())
                                                .load(url)
                                                .into(ivRating);
                                    }else{
                                        Glide.with(getApplicationContext())
                                                .load(R.drawable.noratings)
                                                .into(ivRating);

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
