package com.example.give4friends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private Button change;


    private TextView tvTextBox;
    private TextView tvMission;
    private ImageView ivRating;
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

        /*
        Parameter:
        String search - String of what you want to be searched by the API
        boolean search_by_name - boolean parameter. Input true if you want to search the API by name

        Output:
        String array


         */
        Integer pageNum = 1;
        Integer pageSize = 20;
        String APP_ID  = "45f1c9cb";
        String API_KEY = "a6ec0399e2688d91fe0d419cf84443d0";
        String searchType = search_by_name ? "NAME_ONLY" : "DEFAULT";
        OkHttpClient client = new OkHttpClient();

        String base_url = "https://api.data.charitynavigator.org/v2/Organizations";
        //app_id code from the account
        base_url = base_url + "?" + "app_id=" + APP_ID;
        //API key from the account
        base_url = base_url + "&" + "app_key=" + API_KEY;
        //Number of results per page
        base_url = base_url + "&" + "pageSize=" + pageSize.toString();
        //The number of pages
        base_url = base_url + "&" + "pageNum=" + pageNum.toString();
        // Is the charity rated
        base_url = base_url + "&" + "rated=" + "true";

        if (!search.equals("")){
            base_url = base_url + "&" + "search=" + search;
        }

        base_url = base_url + "&" + "searchType=" + searchType;




        Request request = new Request.Builder()
                .url(base_url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    final String myResponse = response.body().string();

                    try {
                        final JSONArray object = new JSONArray(myResponse);

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object2 = object.getJSONObject(0);

                                    tvTextBox.setText(object2.getString("charityName"));
                                    tvMission.setText(object2.getString("mission"));


                                    String url = object2.getJSONObject("currentRating")
                                            .getJSONObject("ratingImage")
                                            .getString("large");


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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }





}
