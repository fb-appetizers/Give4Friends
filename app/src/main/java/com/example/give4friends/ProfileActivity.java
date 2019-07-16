package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.give4friends.models.Charity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    FavCharitiesAdapter feedAdapter;
    ArrayList<Charity> charities;
    RecyclerView rvCharities;
    private SwipeRefreshLayout swipeContainer;
    private Object FavCharitiesAdapter;
    private Button btEditProfile;

    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBio;
    public TextView tvTotalRaised;
    public TextView tvTotalDonated;


    ParseUser myUser = ParseUser.getCurrentUser();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = this;

        btEditProfile = findViewById(R.id.btEditProfile);


        btEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();

            }
        });


        //Below for recycler view of charities

        //find the RecyclerView
        rvCharities = (RecyclerView) findViewById(R.id.rvFavCharities);

        // get favorite charities from user

        // initialize the array list of charities
        charities = new ArrayList<Charity>();


        //Get relation
        ParseRelation<Charity> favCharities = myUser.getRelation("favCharities");
        //Get all charities in relation
        favCharities.getQuery().findInBackground(new FindCallback<Charity>() {
            @Override
            public void done(List<Charity> objects, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    // results have all the charities the current user liked.
                    // go through relation adding charities
                    for(int i = 0; i < objects.size(); i++){
                        charities.add(objects.get(i));

                    }

                }

            }
        });




        //construct the adapter from this datasource
        feedAdapter = new FavCharitiesAdapter(charities);

        //RecyclerView setup (layout manager, use adapter)

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        rvCharities.setLayoutManager(linearLayoutManager);
        rvCharities.setAdapter(feedAdapter);
        rvCharities.scrollToPosition(0);


        // Below for static elements of profile


        ivProfileImage = (ImageView) findViewById((R.id.ivProfileImage));
        tvUserName = (TextView) findViewById(R.id.tvName);
        tvBio= (TextView) findViewById(R.id.tvBio);
        tvTotalRaised= (TextView) findViewById((R.id.tvTotalRaised));
        tvTotalDonated = (TextView) findViewById((R.id.tvTotalDonated));


        tvUserName.setText(myUser.getUsername());
        tvBio.setText(myUser.getString("Bio"));
        tvTotalDonated.setText("$" + myUser.getNumber("totalDonated"));
        tvTotalRaised.setText("$" +  myUser.getNumber("totalRaised"));

        // Handles images
        Glide.with(context)
                .load(myUser.getParseFile("profileImage").getUrl())
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(20)))
                .into(ivProfileImage);



    }
}





        // Might be valuable code for later -- getting one user from database
        // Didn't use because we are just looking at current user

        /*

        // Actually query and fill
        //get query -- get user info and fill views
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        //Test
        query.whereEqualTo(User.KEY_ID, "RClE3nhbpc");
        // query.whereEqualTo(User.KEY_ID, ParseUser.getCurrentUser());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            //iterate through query
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null ){
                    // fill views
                    User myUser = (User) object;
                    tvUserName.setText(myUser.getKeyUsername());
                    tvBio.setText(myUser.getKeyBio());
                    tvTotalDonated.setText("$" + myUser.getKeyTotalDonated());
                    tvTotalRaised.setText("$" +  myUser.getKeyTotalRaised());

                    // Handles images
                    Glide.with(context)
                            .load(myUser.getKeyProfileImage().getUrl())
                            .apply(new RequestOptions()
                                    .transforms(new CenterCrop(), new RoundedCorners(20)))
                            .into(ivProfileImage);

                }else {
                    Log.e("ProfileActivity", "Can't get post");
                    e.printStackTrace();
                }
            }
        });





        /*
        // query.whereEqualTo(User.KEY_ID, ParseUser.getCurrentUser());
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            //iterate through query
            @Override
            public void done(ParseUser object, ParseException e) {

                if (e == null ){


                    // fill views


                    tvUserName.setText(object.getUsername());
                    tvBio.setText(object.getString("Bio"));
                    tvTotalDonated.setText("$" + object.getNumber("totalDonated"));
                    tvTotalRaised.setText("$" +  object.getNumber("totalRaised"));

                    // Handles images
                    Glide.with(context)
                            .load(object.getParseFile("profileImage").getUrl())
                            .apply(new RequestOptions()
                                    .transforms(new CenterCrop(), new RoundedCorners(20)))
                            .into(ivProfileImage);
                    */
        // query.whereEqualTo(User.KEY_ID, ParseUser.getCurrentUser());



