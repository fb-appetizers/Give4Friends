package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.give4friends.Adapters.CharitySearchAdapter;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.net.CharityClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.give4friends.DonateActivity.currentFriend;

public class DonateSearchCharity extends AppCompatActivity implements Serializable {
    private TextView friendsUserName;
    private TextView friendsName;
    private TextView topResult;
    private ImageView friendsImage;
    public static Charity charity;
    private SearchView svCharity;
    private RecyclerView rvCharitySearch;
    private ImageButton cancel;
    CharityClient client;
    ArrayList<Object> items;
    CharitySearchAdapter charityAdapter;
    ProgressBar miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_search_charity);

        friendsUserName = findViewById(R.id.friendsUserName);
        friendsName = findViewById(R.id.friendsName);
        friendsImage = findViewById(R.id.friendsImage);

        setUpFriend();

        rvCharitySearch = findViewById(R.id.rvCharitySearch);
        svCharity = findViewById(R.id.svCharity);
        cancel = findViewById(R.id.ibcancelFinal);
        topResult = findViewById(R.id.topResult);
        //Added another field to check if this is in the Donate Search charity.
        miActionProgressItem = findViewById(R.id.progressBar);

        topResult.setVisibility(View.GONE);




        svCharity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")){
                    if(client!=null) {
                        client.getClient().dispatcher().cancelAll();
                    }

                    getFavs();
                    topResult.setVisibility(View.GONE);

                }
                else{
                    getResponse(s.toString(),false);
                }
                return false;
            }
        });

        items = new ArrayList<>();

        charityAdapter = new CharitySearchAdapter(items, true, true, false);
        // attach the adapter to the RecyclerView
        rvCharitySearch.setAdapter(charityAdapter);
        // Set layout manager to position the items
        rvCharitySearch.setLayoutManager(new LinearLayoutManager(this));

        getFavs();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    private void setUpFriend(){
        friendsUserName.setText("@" + currentFriend.getUsername());
        friendsName.setText(currentFriend.get("firstName").toString() + " " + currentFriend.get("lastName"));


        String imageURL = currentFriend.getString("profileImageURL");

        if(imageURL != null){

            Date imageDate = currentFriend.getDate("profileImageCreatedAt");

            Glide.with(getApplicationContext())
                    .load(imageURL)
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20))
                            .signature(new ObjectKey(imageDate))
                            .circleCrop())
                    .into(friendsImage);
        }
    }

    private void getResponse(String search, boolean search_by_name){

        if(client!=null){
            //Clears all of the previous client calls
            client.getClient().dispatcher().cancelAll();
        }

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

                    DonateSearchCharity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray charityArray;
                                charityArray = new JSONArray(myResponse);
                                final ArrayList <CharityAPI> charities = CharityAPI.fromJSON(charityArray);

                                items.clear() ;
                                for(CharityAPI charityAPI : charities){
                                    items.add(charityAPI);
                                    charityAdapter.notifyDataSetChanged();
                                }
                                hideProgressBar();
                                topResult.setVisibility(View.VISIBLE);

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


    public void getRecommended(){

        items.add("Recommended Effective Charities");
        ParseQuery<Charity> postQuery = new ParseQuery<Charity>(Charity.class)
                .whereEqualTo("highlyEffective", true);

        postQuery.findInBackground(new FindCallback<Charity>() {
            //iterate through query
            @Override
            public void done(List<Charity> objects, ParseException e) {

                if (e == null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        items.add(CharityAPI.fromParse(objects.get(i)));
                    }
                } else {
                    Log.e("MainActivity", "Can't get transaction");
                    e.printStackTrace();
                }
                charityAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getFavs() {
        items.clear();
        //Get relation
        final ParseRelation<Charity> favCharities = currentFriend.getRelation("favCharities");
        //Get all charities in relation
        favCharities.getQuery().findInBackground(new FindCallback<Charity>() {
            @Override
            public void done(List<Charity> objects, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    // results have all the charities the current user liked.
                    // go through relation adding charities
                    if(objects.size() != 0){
                        items.add(currentFriend.get("firstName").toString() + "'s Favorite Charities");
                    }
                    for (int i = 0; i < objects.size(); i++) {
                        items.add(CharityAPI.fromParse((Charity) objects.get(i)));
                    }
                }
                getRecommended();
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
