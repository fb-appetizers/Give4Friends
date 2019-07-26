package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.give4friends.Adapters.DonateSearchAdapter;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.net.CharityClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.give4friends.DonateActivity.currentFriend;

public class DonateSearchCharity extends AppCompatActivity implements Serializable {
    private TextView friendsUserName;
    private TextView friendsName;
    private ImageView friendsImage;
    public static Charity charity;
    private EditText etCharity;
    private RecyclerView rvCharitySearch;
    private Button btnCancel;
    private ImageButton cancel;
    CharityClient client;
    ArrayList<CharityAPI> acharities;
    DonateSearchAdapter charityAdapter;
    ProgressBar miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_search_charity);

        friendsUserName = findViewById(R.id.friendsUserName);
        friendsName = findViewById(R.id.friendsName);
        friendsImage = findViewById(R.id.friendsImage);

        setUpFriend();

        etCharity = findViewById(R.id.etCharity);
        rvCharitySearch = findViewById(R.id.rvCharitySearch);
        etCharity = findViewById(R.id.etCharity);
        btnCancel = findViewById(R.id.btnCancel);
        cancel = findViewById(R.id.ibcancelFinal);

        acharities = new ArrayList<CharityAPI>();

        charityAdapter = new DonateSearchAdapter(acharities);
        //Added another field to check if this is in the Donate Search charity.

        miActionProgressItem = findViewById(R.id.progressBar);

        // attach the adapter to the RecyclerView
        rvCharitySearch.setAdapter(charityAdapter);

        // Set layout manager to position the items
        rvCharitySearch.setLayoutManager(new LinearLayoutManager(this));

        //When you hit submit the recycler view updates
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etCharity.getText().clear();
                etCharity.clearFocus();



            }
        });

        etCharity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {

                if (count == 0 ){
                    if(client!=null) {
                        client.getClient().dispatcher().cancelAll();
                    }
                    acharities.clear();
                    charityAdapter.notifyDataSetChanged();
                    hideProgressBar();

                }
                if(count > 0 ){

                    getResponse(etCharity.getText().toString(),false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpFriend(){
        friendsUserName.setText("@" + currentFriend.getUsername());
        friendsName.setText(currentFriend.get("firstName").toString() + " " + currentFriend.get("lastName"));

        ParseFile image = currentFriend.getParseFile("profileImage");

        if(image != null){
            Glide.with(getApplicationContext())
                    .load(image.getUrl())
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20))
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

                                acharities.clear() ;
                                for(CharityAPI charityAPI : charities){
                                    acharities.add(charityAPI);
                                    charityAdapter.notifyDataSetChanged();
                                }
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

    public void getEffective(final String search, final boolean search_by_name){

        //TODO -- do the and operator
        ParseQuery<Charity> query1 = new ParseQuery<Charity>(Charity.class);
        ParseQuery<Charity> q1 = query1.whereMatches("name", "("+search+")", "i");


        ParseQuery<Charity> postQuery = new ParseQuery<Charity>(Charity.class)
                .whereEqualTo("highlyEffective", true);

        ParseQuery<Charity> q2 = postQuery.whereMatchesQuery("name",q1);


        q2.findInBackground(new FindCallback<Charity>() {
            //iterate through query
            @Override
            public void done(List<Charity> objects, ParseException e) {

                acharities.clear() ;
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        acharities.add(CharityAPI.fromParse(objects.get(i)));
                    }
                    charityAdapter.notifyDataSetChanged();
                } else {
                    Log.e("MainActivity", "Can't get transaction");
                    e.printStackTrace();
                }
                getResponse(search, search_by_name);

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
